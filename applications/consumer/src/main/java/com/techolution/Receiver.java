package com.techolution;

import com.techolution.model.Message;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * Created by tdelesio on 12/13/17.
 */
@Component
public class Receiver {

    private CountDownLatch latch = new CountDownLatch(1);

    public CountDownLatch getLatch() {
        return latch;
    }

    @KafkaListener(topics = "${kafka.topic.receiver}", containerFactory = "greetingKafkaListenerContainerFactory")
    public void listen(Message message) {

        System.out.println("Received Messasge: " + message);
        latch.countDown();
    }


}
