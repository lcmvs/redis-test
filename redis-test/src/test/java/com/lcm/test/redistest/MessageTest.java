package com.lcm.test.redistest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: lcm
 * @create: 2020-07-23 10:41
 **/
@Slf4j
@SpringBootTest
public class MessageTest {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void test(){
        int num = 1000;
//        //队列
//        queueTest(num);
//        //栈
//        stackTest(num);
        //批量队列
        queueBatchTest(num);
    }

    @Test
    public void queueTest(int num){
        ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
        String key="lcm:test:list";
        long startTime = System.currentTimeMillis();
        for(int i=0;i<num;i++){
            listOperations.rightPush(key,i+"");
        }
        long popStartTime = System.currentTimeMillis();
        for(int i=0;i<num;i++){
            listOperations.leftPop(key);
        }
        long popEndTime = System.currentTimeMillis();
        log.info("入队{}ms,出队{}ms",popStartTime-startTime,popEndTime-popStartTime);
    }

    @Test
    public void stackTest(int num){
        ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
        String key="lcm:test:list";
        long startTime = System.currentTimeMillis();
        for(int i=0;i<num;i++){
            listOperations.rightPush(key,i+"");
        }
        long popStartTime = System.currentTimeMillis();
        for(int i=0;i<num;i++){
            listOperations.rightPop(key);
        }
        long popEndTime = System.currentTimeMillis();
        log.info("入栈{}ms,出栈{}ms",popStartTime-startTime,popEndTime-popStartTime);
    }

    @Test
    public void queueBatchTest(int num){
        ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
        String key="lcm:test:list";
        long startTime = System.currentTimeMillis();
        List<String> list=new ArrayList<>(num);
        for(int i=0;i<num;i++){
            list.add(i+"");
        }
        listOperations.rightPushAll(key,list);
        long popStartTime = System.currentTimeMillis();
        List<String> list1 = listOperations.range(key, 0L, num);
        long popEndTime = System.currentTimeMillis();
        log.info("批量入队{}ms,批量出队{}ms",popStartTime-startTime,popEndTime-popStartTime);
    }

    @Test
    public void stackBatchTest(int num){
        ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
        String key="lcm:test:list";
        long startTime = System.currentTimeMillis();
        List<String> list=new ArrayList<>(num);
        for(int i=0;i<num;i++){
            list.add(i+"");
        }
        listOperations.rightPushAll(key,list);
        long popStartTime = System.currentTimeMillis();
        List<String> list1 = listOperations.range(key, 0L, num);
        long popEndTime = System.currentTimeMillis();
        log.info("批量入栈{}ms,批量出栈{}ms",popStartTime-startTime,popEndTime-popStartTime);
    }

}
