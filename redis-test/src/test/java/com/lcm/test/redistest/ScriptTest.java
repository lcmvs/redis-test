package com.lcm.test.redistest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;


import java.io.IOException;
import java.util.Collections;

/**
 * @description:
 * @author: lcm
 * @create: 2020-07-23 14:09
 **/
@Slf4j
@SpringBootTest
public class ScriptTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void test() throws IOException {
        // 读取 /resources/lua/compareAndSet.lua 脚本 。
        String  scriptContents = IOUtils.toString(getClass().getResourceAsStream("/lua/compareAndSet.lua"), "UTF-8");
        // 创建 RedisScript 对象
        RedisScript<Long> script = new DefaultRedisScript<>(scriptContents, Long.class);
        // 执行 LUA 脚本
        Long result = stringRedisTemplate.execute(script, Collections.singletonList("test:lua"), "lcm", "123");
        log.info("CAS设置{}",result==1?"成功":"失败");
    }

}
