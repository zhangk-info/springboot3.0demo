package com.xlj.framework.mq;

import com.xlj.user.entity.User;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 发布订阅模式作为消息队列
 * 消费者必需在线 ！！！
 * 使用监听不需要while循环
 * 没有保证了只能一个消费者消费消息 redisV7.0.0以上可使用的getShardedTopic保证了
 */
@Service
public class TopicService {

    private static final String REDIS_TOPIC_MQ = "redis_topic_mq";

    @Autowired
    private RedissonClient redissonClient;

    public void send(User msg) {
        if (Objects.nonNull(msg)) {
//            RTopic topic = redissonClient.getShardedTopic(REDIS_TOPIC_MQ);
            RTopic topic = redissonClient.getTopic(REDIS_TOPIC_MQ);
            topic.publishAsync(msg);
        }
    }
}