package com.lcm.test.redistest;

import com.lcm.test.redistest.message.Procuder;
import com.lcm.test.redistest.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @description:
 * @author: lcm
 * @create: 2020-07-23 13:50
 **/
@Slf4j
@SpringBootTest
public class PubSubTest {

    @Autowired
    Procuder procuder;

    @Test
    public void test(){
        procuder.sendTopic1(new User());
        procuder.sendTopic2("test");
    }

}
