package com.techolution.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.springframework.kafka.test.assertj.KafkaConditions.key;
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasValue;

import com.techolution.Sender;
import com.techolution.model.Message;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by tdelesio on 11/21/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class MessageServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceTest.class);

    private static String SENDER_TOPIC = "sender.t";

    @Autowired
    private Sender sender;

    private KafkaMessageListenerContainer<String, Message> container;

    private BlockingQueue<ConsumerRecord<String, Message>> records;

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, SENDER_TOPIC);


    @Before
    public void setUp() {
        Map<String, Object> consumerProperties =
                KafkaTestUtils.consumerProps("sender", "false", embeddedKafka);
        

        // create a Kafka consumer factory
        DefaultKafkaConsumerFactory<String, Message> consumerFactory =
                new DefaultKafkaConsumerFactory<String, Message>(consumerProperties, new StringDeserializer(), new JsonDeserializer<>(Message.class));

        // set the topic that needs to be consumed
        ContainerProperties containerProperties = new ContainerProperties(SENDER_TOPIC);

        // create a Kafka MessageListenerContainer
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);

        // create a thread safe queue to store the received message
        records = new LinkedBlockingQueue<>();

        // setup a Kafka message listener
        container.setupMessageListener(new MessageListener<String, Message>() {
            @Override
            public void onMessage(ConsumerRecord<String, Message> record) {
                LOGGER.debug("test-listener received message='{}'", record.toString());
                records.add(record);
            }
        });

        // start the container and underlying message listener
        container.start();

        // wait until the container has the required number of assigned
        try {
        ContainerTestUtils.waitForAssignment(container, embeddedKafka.getPartitionsPerTopic());}
        catch (Exception e)
        {
            fail();
        }
    }

    @After
    public void tearDown() {
        // stop the container
        container.stop();
    }

    @Test
    public void testSend() throws InterruptedException {
        // send the message
        String greeting = "Hello Spring Kafka Sender!";
        Message m = new Message(greeting, "tim");
        sender.send(SENDER_TOPIC, m);

        // check that the message was received
        ConsumerRecord<String, Message> received = records.poll(10, TimeUnit.SECONDS);
        // Hamcrest Matchers to check the value
        System.out.println(received.value().getMsg());

        assertEquals(received.value().getMsg(), greeting);
        // AssertJ Condition to check the key
        assertThat(received).has(key(null));
    }

    
}
