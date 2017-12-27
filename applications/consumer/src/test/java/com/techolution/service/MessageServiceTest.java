package com.techolution.service;

import static org.assertj.core.api.Assertions.assertThat;
import com.techolution.Receiver;
import com.techolution.model.Message;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by tdelesio on 11/21/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class MessageServiceTest {

    //private static String RECEIVER_TOPIC = "greeting";
    private static String RECEIVER_TOPIC = "receiver.t";

    @Autowired
    private Receiver receiver;

    private KafkaTemplate<String, Message> template;

//    @Value(value = "${kafka.bootstrapAddress}")
//    private String bootstrapAddress;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, RECEIVER_TOPIC);


    @Before
    public void setUp() throws Exception {
        // set up the Kafka producer properties
        Map<String, Object> senderProperties =
                KafkaTestUtils.senderProps(embeddedKafka.getBrokersAsString());

        senderProperties.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        senderProperties.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
//        senderProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
//                bootstrapAddress);

        // create a Kafka producer factory
        ProducerFactory<String, Message> producerFactory =
                new DefaultKafkaProducerFactory<>(senderProperties);


        // create a Kafka template
        template = new KafkaTemplate<>(producerFactory);
        // set the default topic to send to
        template.setDefaultTopic(RECEIVER_TOPIC);

        // wait until the partitions are assigned
        for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry
                .getListenerContainers()) {
            ContainerTestUtils.waitForAssignment(messageListenerContainer,
                    embeddedKafka.getPartitionsPerTopic());
        }
    }

        @Test
        public void testReceive() throws Exception {
            // send the message
            String greeting = "Hello Spring Kafka Receiver!";
            Message message = new Message(greeting, "tim");
            template.sendDefault(message);

            receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
            // check that the message was received
            System.err.println(receiver.getLatch().getCount());
            assertThat(receiver.getLatch().getCount()).isEqualTo(0);
        }

    
}
