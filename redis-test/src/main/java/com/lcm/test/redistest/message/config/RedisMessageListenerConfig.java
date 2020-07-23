package com.lcm.test.redistest.message.config;

import com.lcm.test.redistest.message.Procuder;
import com.lcm.test.redistest.message.Topic1MessageListener;
import com.lcm.test.redistest.message.Topic2MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @description:
 * @author: lcm
 * @create: 2020-07-23 10:33
 **/
@Configuration
public class RedisMessageListenerConfig {

    @Autowired
    Topic1MessageListener topic1MessageListener;

    @Autowired
    Topic2MessageListener topic2MessageListener;

    @Bean
    public RedisMessageListenerContainer listenerContainer(RedisConnectionFactory factory) {
        // 创建 RedisMessageListenerContainer 对象
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();

        // 设置 RedisConnection 工厂。它就是实现多种 Java Redis 客户端接入的秘密工厂。
        container.setConnectionFactory(factory);

        // 添加监听器
        container.addMessageListener(topic1MessageListener,new ChannelTopic(Procuder.TOPIC_1));
        container.addMessageListener(topic2MessageListener,new ChannelTopic(Procuder.TOPIC_2));

        return container;
    }


}
