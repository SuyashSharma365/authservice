package com.suyash.authservice.eventProducer;

import com.suyash.authservice.model.UserDetailsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsProducer {

    private final KafkaTemplate<String , UserDetailsDto> kafkaTemplate;

    @Autowired
    UserDetailsProducer(KafkaTemplate<String, UserDetailsDto> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

}
