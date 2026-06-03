package com.suyash.authservice.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suyash.authservice.model.UserDetailsDto;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class UserDetailsSerializer implements Serializer<UserDetailsDto> {

    private static final Logger logger =
            LoggerFactory.getLogger(UserDetailsSerializer.class);

    @Override
    public void configure(Map<String , ?> configs , boolean isKey){

    }

    @Override
    public byte[] serialize(String args0 , UserDetailsDto dto){
        byte[] returnValue = null;
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            returnValue = objectMapper.writeValueAsString(dto).getBytes();
        }
        catch(Exception e){
            logger.error("Failed to serialize UserDetailsDto", e);
        }
        return returnValue;
    }

    @Override
    public void close(){}


}
