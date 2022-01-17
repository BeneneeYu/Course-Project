package com.webproject.backend.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * @description
 * 简单的消息生成类
 */

public class Message {

    private static final Logger logger = LoggerFactory.getLogger(Message.class);

    public static HashMap<String,Object> newMessage(String message){
        HashMap<String,Object> map = new HashMap<>();
        map.put("message",message);
        return map;
    }

    public static String newJsonStringMessage(String message){
        try {
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, Object> map = new HashMap<>();
            map.put("message", message);
            return mapper.writeValueAsString(map);
        }catch (JsonProcessingException ex){
            logger.error(ex.toString());
        }
        return null;
    }
}
