package com.suyash.authservice.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suyash.authservice.model.UserDetailsDto;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class UserDetailsSerializer implements Serializer<UserDetailsDto> {

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
            e.printStackTrace();
        }
        return returnValue;
    }

    @Override
    public void close(){}


}
