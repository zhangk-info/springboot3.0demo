package com.xlj.framework.mq;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void onMessage() {
        RBlockingDeque<String> blockingDeque = redissonClient.getBlockingDeque(REDIS_QUEUE_MQ);
        while (true) {
            try {
                String message = blockingDeque.takeLast();
                log.info("从队列 {} 中读取到消息：{}.", REDIS_QUEUE_MQ, message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}