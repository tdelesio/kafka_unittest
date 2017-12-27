package com.techolution;

import com.techolution.model.Message;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;


@SpringBootApplication





public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }





    //  @Autowired
	// @LoadBalanced
	// private OAuth2RestOperations secureRestTemplate;
}
