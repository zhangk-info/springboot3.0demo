package com.xlj.mq;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.StreamGroup;
import org.redisson.api.StreamMessageId;
import org.redisson.api.stream.StreamAddArgs;
import org.redisson.api.stream.StreamReadGroupArgs;
import org.redisson.config.Config;

import java.util.List;
import java.util.Map;

/**
 * 使用流作为消息队列 内容小于512Mb
 * It extends RBucket interface and size is limited to 512Mb.
 * while循环中未处理队列是否已空，会一直读取
 * 保证了只能会有一个消费者消费消息
 */
@Slf4j
public class StreamMqTest {

    private static final String REDIS_STREAM_MQ = "redis_stream_mq";
    private static final String GROUP_NAME = "group1";

    @Test
    public void testQueue() throws InterruptedException {

        Config config = new Config();
        config.useSingleServer().setAddress("redis://81.68.104.177:6379").setDatabase(10).setPassword("test");
        RedissonClient redissonClient = Redisson.create(config);
        new Thread(() -> {
            RStream<String, String> stream = redissonClient.getStream(REDIS_STREAM_MQ);
            List<StreamGroup> streamGroups = stream.listGroups();
            boolean hasGroup = false;
            for (int i = 0; i < streamGroups.size(); i++) {
                if (streamGroups.get(i).getName().equals(GROUP_NAME)) {
                    hasGroup = true;
                }
            }
            if (!hasGroup){
                stream.createGroup(GROUP_NAME, StreamMessageId.ALL);
            }
            String consumerName = IdUtil.randomUUID();

            while (true) {
                StreamReadGroupArgs streamReadGroupArgs = StreamReadGroupArgs.neverDelivered();
                streamReadGroupArgs.count(1);
                Map<StreamMessageId, Map<String, String>> messages = stream.readGroup(GROUP_NAME, consumerName, streamReadGroupArgs);
                for (Map.Entry<StreamMessageId, Map<String, String>> entry : messages.entrySet()) {
                    // 得到每条消息
                    Map<String, String> msg = entry.getValue();
                    log.info("从 {} 的组 {} 中读取到消息：{}.", REDIS_STREAM_MQ, GROUP_NAME, msg);
                    StreamMessageId streamMessageId = entry.getKey();
                    // 处理一条 ack一条
                    stream.ack(GROUP_NAME, streamMessageId);
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
            RStream<String, String> stream = redissonClient.getStream(REDIS_STREAM_MQ);
            List<StreamGroup> streamGroups = stream.listGroups();
            boolean hasGroup = false;
            for (int i = 0; i < streamGroups.size(); i++) {
                if (streamGroups.get(i).getName().equals(GROUP_NAME)) {
                    hasGroup = true;
                }
            }
            if (!hasGroup){
                stream.createGroup(GROUP_NAME, StreamMessageId.ALL);
            }

            String consumerName = IdUtil.randomUUID();

            while (true) {
                StreamReadGroupArgs streamReadGroupArgs = StreamReadGroupArgs.neverDelivered();
                streamReadGroupArgs.count(1);
                Map<StreamMessageId, Map<String, String>> messages = stream.readGroup(GROUP_NAME, consumerName, streamReadGroupArgs);
                for (Map.Entry<StreamMessageId, Map<String, String>> entry : messages.entrySet()) {
                    // 得到每条消息
                    Map<String, String> msg = entry.getValue();
                    log.info("从 {} 的组 {} 中读取到消息：{}.", REDIS_STREAM_MQ, GROUP_NAME, msg);
                    StreamMessageId streamMessageId = entry.getKey();
                    // 处理一条 ack一条
                    stream.ack(GROUP_NAME, streamMessageId);
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
                RStream<String, String> stream = redissonClient.getStream(REDIS_STREAM_MQ);
                String msgId = IdUtil.randomUUID();
                StreamAddArgs<String, String> streamAddArgs = StreamAddArgs.entry(msgId, "消息" + i);
                stream.add(streamAddArgs);
                log.info("将消息: {} 插入到Stream队列。", "消息" + i);
            }
        }).start();

        Thread.currentThread().join();
    }
}