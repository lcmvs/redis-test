package com.lcm.test.redistest.message;

import com.lcm.test.redistest.pojo.User;
import com.lcm.test.redistest.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: lcm
 * @create: 2020-07-23 10:33
 **/
@Slf4j
@Component
public class Topic2MessageListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("topic2消费：{}", new String(message.getBody()));
    }

}
