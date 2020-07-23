package com.lcm.test.redistest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lcm.test.redistest.pojo.User;
import com.lcm.test.redistest.util.JacksonJsonUtil;
import com.lcm.test.redistest.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @description:
 * @author: lcm
 * @create: 2020-07-23 09:16
 **/
@Slf4j
@SpringBootTest
public class PipelineTest {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void test() throws JsonProcessingException {
        stringRedisTemplateSetTest(1000);
        stringRedisTemplatePipelineSetTest(100,10);
        stringRedisTemplatePipelineSetTest(10,100);
        stringRedisTemplatePipelineSetTest(1,1000);

        stringRedisTemplateGetTest(1000);
        stringRedisTemplatePipelineGetTest(100,10);
        stringRedisTemplatePipelineGetTest(10,100);
        stringRedisTemplatePipelineGetTest(1,1000);
    }

    long stringRedisTemplateSetTest(int num) throws JsonProcessingException {
        String key="test.1.";
        List<User> list=User.createUser(num);
        long startTime = System.currentTimeMillis();
        for(int i=0;i<num;i++){
            stringRedisTemplate.opsForValue().set(key+i, JacksonJsonUtil.Object2Json(list.get(i)));
        }
        long time=System.currentTimeMillis()-startTime;
        log.info("StringRedisTemplate使用FastJson操作set{}次，花费{}ms",num,time);
        return time;
    }

    long stringRedisTemplateGetTest(int num) throws JsonProcessingException {
        String key="test.1.";
        long startTime = System.currentTimeMillis();
        for(int i=0;i<num;i++){
            User user = JacksonJsonUtil.Json2Object(stringRedisTemplate.opsForValue().get(key + i), User.class);
        }
        long time=System.currentTimeMillis()-startTime;
        log.info("StringRedisTemplate使用FastJson操作get{}次，花费{}ms",num,System.currentTimeMillis()-startTime);
        return time;
    }

    long stringRedisTemplatePipelineSetTest(int a,int b){
        String key="test.3.";
        int num=a*b;
        List<User> list=User.createUser(num);
        long startTime = System.currentTimeMillis();
        for(int i=0;i<a;i++){
            stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    for(int i=0;i<b;i++){
                        connection.set((key+i).getBytes(), JsonUtil.Object2Json(list.get(i)).getBytes());
                    }
                    return null;
                }
            });
        }
        long time=System.currentTimeMillis()-startTime;
        log.info("Pipeline操作{}次，每次批量set{}个数据，总共花费{}ms",a,b,time);
        return time;
    }

    long stringRedisTemplatePipelineGetTest(int a,int b) {
        String key="test.1.";
        long startTime = System.currentTimeMillis();
        for(int i=0;i<a;i++){
            List<String> list = (List<String>)(List)stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    for (int i = 0; i < b; i++) {
                        connection.get((key + i).getBytes());
                    }
                    return null;
                }
            });
            List<User> users = list.stream().map(new Function<String, User>() {
                @Override
                public User apply(String s) {
                    return JsonUtil.Json2Object(s,User.class);
                }
            }).collect(Collectors.toList());
        }
        long time=System.currentTimeMillis()-startTime;
        log.info("Pipeline操作{}次，每次批量get{}个数据，总共花费{}ms",a,b,time);
        return time;
    }

}
