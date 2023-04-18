package com.xlj.framework.mq;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 使用阻塞队列作为消息队列
 * while循环中处理队列是否已空，队列空了之后会等待到下一条消息发送再继续接收
 * 队列保证了只能会有一个消费者消费消息
 */
@Slf4j
@Service
public class QueueService {

    private static final String REDIS_QUEUE_MQ = "redis_queue_mq";
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 发送消息到队列头部
     *
     * @param message
     */
    public void sendMessage(String message) {
        RBlockingDeque<String> blockingDeque = redissonClient.getBlockingDeque(REDIS_QUEUE_MQ);

        try {
            blockingDeque.putFirst(message);
            log.info("将消息: {} 插入到队列。", message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从队列尾部阻塞读取消息，若没有消息，线程就会阻塞等待新消息插入，防止 CPU 空转
     * 在runner中用单独的线程跑
     */
    public void onMessage(Config config) {
        // 在使用同一个client的时候不会处理消息 所以copy并创建一个新的client
        RedissonClient redissonClientCopy = Redisson.create(config);
        RBlockingDeque<String> blockingDeque = redissonClientCopy.getBlockingDeque(REDIS_QUEUE_MQ);

        while (true) {
            log.info("{} 从队列 {} 中等待读取消息", redissonClientCopy, REDIS_QUEUE_MQ);
            try {
                String message = blockingDeque.poll(30, TimeUnit.SECONDS);
                if (StringUtils.isBlank(message)) {
                    log.info("一段时间从队列 {} 中读取到 空 消息，当前时间：{}", REDIS_QUEUE_MQ, DateUtil.now());
                } else {
                    log.info("从队列 {} 中读取到消息：{}.开始处理", REDIS_QUEUE_MQ, message);
                    log.info("从队列 {} 中读取到消息：{}.处理完成", REDIS_QUEUE_MQ, message);
                }
            } catch (InterruptedException e1) {
                log.info("消息处理时捕获到InterruptedException：" + e1.getMessage());
                blockingDeque = redissonClientCopy.getBlockingDeque(REDIS_QUEUE_MQ);
            } catch (Exception e) {
                log.error("处理一条消息失败：" + e.getMessage(), e);
            }
        }
    }
}