package com.techolution;

import com.techolution.model.Message;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;


@SpringBootApplication
public class ProducerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }

    @Autowired
    private Sender sender;
//    @Value(value = "${greeting.topic.name}")
//    private String topicName;
//
//    @Autowired
//    private KafkaTemplate<String, Message> kafkaTemplate;
//
//    @Autowired
//    private ProducerFactory factory;

    @Override
    public void run(String... args) throws Exception {

//        SpringApplication.run(ProducerApplication.class, args);

//        Message message = new Message("Hello World", "tim");
//        sender.send("${kafka.topic.reciever}", message );

//        Producer<String, Message> producer = factory.createProducer();

//        TracingProducer<String, Message> tracingProducer = kafkaTracing.producer(producer);
//        tracingProducer.send(new ProducerRecord<K, V>("my-topic", key, value));

//        kafkaTemplate.send(topicName, message);
    }
}
