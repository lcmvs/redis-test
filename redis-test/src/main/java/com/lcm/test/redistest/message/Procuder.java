package com.lcm.test.redistest.message;

import com.lcm.test.redistest.pojo.User;
import com.lcm.test.redistest.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: lcm
 * @create: 2020-07-23 10:32
 **/
@Component
public class Procuder {

    public static final String TOPIC_1="topic:1";

    public static final String TOPIC_2="topic:2";

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public void sendTopic1(User user){
        stringRedisTemplate.convertAndSend(TOPIC_1, JsonUtil.Object2Json(user));
    }

    public void sendTopic2(String str){
        stringRedisTemplate.convertAndSend(TOPIC_2, str);
    }

}
