package com.lcm.test.redistest.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcm.test.redistest.pojo.User;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @description:
 * @author: lcm
 * @create: 2020-07-22 11:03
 **/
@Slf4j
public class JacksonJsonUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    public static Map Json2Map(String str) throws JsonProcessingException {
        return mapper.readValue(str, Map.class);
    }

    public static <T> T Json2Object(String str, Class<T> clazz) throws JsonProcessingException {
        return mapper.readValue(str,clazz);
    }

    public static String Object2Json(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }

    public static void main(String[] args) throws JsonProcessingException {
        User user=new User();
        String json = Object2Json(user);
        log.info("序列化:{}",json);
        User user1 = Json2Object(json, User.class);
        log.info("反序列化:{}",user1);
    }

}
