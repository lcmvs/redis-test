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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
class RedisTestApplicationTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testStringSetKey() {
        stringRedisTemplate.opsForValue().set("lcm", "lcm");
        String str = stringRedisTemplate.opsForValue().get("lcm");
        log.info("获取lcm的value：{}",str);
    }

    @Test
    void templateSetTest() throws JsonProcessingException {
        for(int i=0;i<100;i++){
            stringRedisTemplate.opsForValue().set(i+"",i+"");
            stringRedisTemplate.opsForValue().get(i+"");
        }
        log.info("预热完成");
        long a=0L;
        long b=0L;
        for(int i=0;i<10;i++){
            a=a+stringRedisTemplateSetTest(1000);
            b=b+redisTemplateSetTest(1000);
        }
        long c=0L;
        long d=0L;
        for(int i=0;i<10;i++){
            c=c+stringRedisTemplateGetTest(1000);
            d=d+redisTemplateGetTest(1000);
        }
        log.info("{}；{}",a/10,b/10);
        log.info("{}；{}",c/10,d/10);
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

    long redisTemplateSetTest(int num) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(User.class));
        String key="test.2.";
        List<User> list=User.createUser(num);
        long startTime = System.currentTimeMillis();
        for(int i=0;i<num;i++){
            redisTemplate.opsForValue().set(key+i, list.get(i));
        }
        long time=System.currentTimeMillis()-startTime;
        log.info("RedisTemplate使用Jackson操作set{}次，花费{}ms",num,System.currentTimeMillis()-startTime);
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

    long redisTemplateGetTest(int num) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(User.class));
        String key="test.2.";
        List<User> list=User.createUser(num);
        long startTime = System.currentTimeMillis();
        for(int i=0;i<num;i++){
            User user = (User) redisTemplate.opsForValue().get(key + i);
        }
        long time=System.currentTimeMillis()-startTime;
        log.info("RedisTemplate使用Jackson操作get{}次，花费{}ms",num,System.currentTimeMillis()-startTime);
        return time;
    }


}
