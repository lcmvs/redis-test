package com.lcm.test.redissontest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @description:
 * @author: lcm
 * @create: 2020-07-23 14:37
 **/
@Slf4j
@SpringBootTest
public class LockTest {

    public static final String DATE_PATTERN="yyyy-MM-dd HH:mm:ss";

    public static final ThreadLocal<SimpleDateFormat> DATE_FORMATE =ThreadLocal.withInitial(new Supplier<SimpleDateFormat>() {
        @Override
        public SimpleDateFormat get() {
            return new SimpleDateFormat(DATE_PATTERN);
        }
    });

    private static final String LOCK_KEY = "redission:lock";

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void test() throws InterruptedException {
        // 启动一个线程 A ，去占有锁
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 加锁以后 10 秒钟自动解锁
                // 无需调用 unlock 方法手动解锁
                final RLock lock = redissonClient.getLock(LOCK_KEY);
                lock.lock(10, TimeUnit.SECONDS);
                log.info("{}:线程 A 获取锁成功，10s后释放锁",DATE_FORMATE.get().format(new Date()));
            }
        }).start();

        // 主线程 sleep 1 秒，保证线程 A 成功持有锁
        Thread.sleep(1000L);

        // 尝试加锁，最多等待 100 秒，上锁以后 10 秒自动解锁
        log.info("{}:主线程尝试获取锁",DATE_FORMATE.get().format(new Date()));
        final RLock lock = redissonClient.getLock(LOCK_KEY);
        boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
        if (res) {
            log.info("{}:主线程获取锁成功",DATE_FORMATE.get().format(new Date()));
        } else {
            log.info("{}:主线程获取锁失败",DATE_FORMATE.get().format(new Date()));
        }
    }

}
