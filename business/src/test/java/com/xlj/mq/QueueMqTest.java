package com.xlj.mq;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * 使用阻塞队列作为消息队列
 * while循环中处理队列是否已空，队列空了之后会等待到下一条消息发送再继续接收
 */
@Slf4j
public class QueueMqTest {

    private static final String REDIS_QUEUE_MQ = "redis_queue_mq";

    @Test
    public void testQueue() throws InterruptedException {

        Config config = new Config();
        config.useSingleServer().setAddress("redis://81.68.104.177:6379").setDatabase(10).setPassword("test");

        RedissonClient redissonClient = Redisson.create(config);
        new Thread(() -> {
            RBlockingDeque<String> blockingDeque = redissonClient.getBlockingDeque(REDIS_QUEUE_MQ);
            while (true) {
                try {
                    String message = blockingDeque.takeLast();
                    log.info("从队列 {} 中读取到消息：{}.", REDIS_QUEUE_MQ, message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        Thread.currentThread().join();
    }

    @Test
    public void testQueue2() throws InterruptedException {

        Config config = new Config();
        config.useSingleServer().setAddress("redis://81.68.104.177:6379").setDatabase(10).setPassword("test");

        RedissonClient redissonClient = Redisson.create(config);
        new Thread(() -> {
            RBlockingDeque<String> blockingDeque = redissonClient.getBlockingDeque(REDIS_QUEUE_MQ);
            while (true) {
                try {
                    String message = blockingDeque.takeLast();
                    log.info("从队列 {} 中读取到消息：{}.", REDIS_QUEUE_MQ, message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        Thread.currentThread().join();
    }

    @Test
    public void testSend() throws InterruptedException {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://81.68.104.177:6379").setDatabase(10).setPassword("test");

        RedissonClient redissonClient = Redisson.create(config);

        new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                RBlockingDeque<String> blockingDeque = redissonClient.getBlockingDeque(REDIS_QUEUE_MQ);

                try {
                    blockingDeque.putFirst("消息" + i);
                    log.info("将消息: {} 插入到队列。", "消息" + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Thread.currentThread().join();
    }
}