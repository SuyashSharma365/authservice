package com.suyash.authservice.eventProducer;

import com.suyash.authservice.model.UserDetailsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsProducer {

    private final KafkaTemplate<String , UserDetailsDto> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String TOPIC_NAME;

    @Autowired
    UserDetailsProducer(KafkaTemplate<String, UserDetailsDto> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendEventToKafka(UserDetailsDto userDetailsDto){
        Message<UserDetailsDto>message = MessageBuilder
                .withPayload(userDetailsDto)
                .setHeader(KafkaHeaders.TOPIC ,  TOPIC_NAME)
                .build();
        kafkaTemplate.send(message);
    }

}
