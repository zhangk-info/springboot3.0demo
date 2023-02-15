package com.xlj.mq;

import com.xlj.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 发布订阅模式作为消息队列
 * 消费者必需在线 ！！！
 * 使用监听不需要while循环
 * 没有保证了只能一个消费者消费消息 redisV7.0.0以上可使用的getShardedTopic保证了
 */
@Slf4j
public class TopicTest {

    private static final String REDIS_TOPIC_MQ = "redis_topic_mq";

    @Test
    public void testTopic() {
        RedissonClient redisson = null;
        try {
            AtomicInteger integer = new AtomicInteger(0);
            Config config = new Config();
            config.useSingleServer().setAddress("redis://81.68.104.177:6379").setDatabase(10).setPassword("test");
            redisson = Redisson.create(config);
//            RTopic topic = Redisson.create(redisson.getConfig()).getShardedTopic(CacheConstants.MINI_PROGRAM_MSG_SEND_TOPIC);
            RTopic topic = Redisson.create(redisson.getConfig()).getTopic(REDIS_TOPIC_MQ);
            RedissonClient finalRedisson = redisson;
            topic.addListener(User.class, (charSequence, obj) -> {
                RLock lock = finalRedisson.getLock(REDIS_TOPIC_MQ + obj.getId());
                try {
                    boolean res = lock.tryLock(0, 3, TimeUnit.SECONDS);
                    if (res) {
                        System.out.println("Redisson监听器收到消息:" + obj);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (lock.isLocked()) { // 是否还是锁定状态
                        if (lock.isHeldByCurrentThread()) { // 是当前执行线程的锁
                            lock.unlock(); // 释放锁
                        }
                    }
                }
            });
            Thread.sleep(600 * 1000);
            System.out.println(integer.get());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            redisson.shutdown();
        }
    }

    @Test
    public void testTopic2() {
        RedissonClient redisson = null;
        try {
            AtomicInteger integer = new AtomicInteger(0);
            Config config = new Config();
            config.useSingleServer().setAddress("redis://81.68.104.177:6379").setDatabase(10).setPassword("test");
            redisson = Redisson.create(config);
//            RTopic topic = Redisson.create(redisson.getConfig()).getShardedTopic(CacheConstants.MINI_PROGRAM_MSG_SEND_TOPIC);
            RTopic topic = Redisson.create(redisson.getConfig()).getTopic(REDIS_TOPIC_MQ);
            RedissonClient finalRedisson = redisson;
            topic.addListener(User.class, (charSequence, obj) -> {
                RLock lock = finalRedisson.getLock(REDIS_TOPIC_MQ + obj.getId());
                try {
                    boolean res = lock.tryLock(0, 3, TimeUnit.SECONDS);
                    if (res) {
                        System.out.println("Redisson监听器收到消息:" + obj);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (lock.isLocked()) { // 是否还是锁定状态
                        if (lock.isHeldByCurrentThread()) { // 是当前执行线程的锁
                            lock.unlock(); // 释放锁
                        }
                    }
                }
            });
            Thread.sleep(600 * 1000);
            System.out.println(integer.get());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            redisson.shutdown();
        }
    }

    @Test
    public void testSend() {
        RedissonClient redisson = null;
        try {
            Config config = new Config();
            config.useSingleServer().setAddress("redis://81.68.104.177:6379").setDatabase(10).setPassword("test");

            redisson = Redisson.create(config);

//            RTopic topic1 = redisson.getShardedTopic(CacheConstants.MINI_PROGRAM_MSG_SEND_TOPIC);
            RTopic topic1 = redisson.getTopic(REDIS_TOPIC_MQ);
            for (int i = 0; i < 100; i++) {
                User user = new User();
                user.setId(Long.parseLong(i + ""));
                System.out.println(topic1.publish(user));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            redisson.shutdown();
        }
    }
}
