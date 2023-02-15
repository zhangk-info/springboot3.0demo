package com.xlj.framework.mq;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.StreamGroup;
import org.redisson.api.StreamMessageId;
import org.redisson.api.stream.StreamAddArgs;
import org.redisson.api.stream.StreamReadGroupArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 使用流作为消息队列 内容小于512Mb
 * It extends RBucket interface and size is limited to 512Mb.
 * while循环中未处理队列是否已空，会一直读取
 * 保证了只能会有一个消费者消费消息
 */
@Slf4j
@Service
public class StreamService {

    private static final String REDIS_STREAM_MQ = "redis_stream_mq";
    private static final String GROUP_NAME = "group1";

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 发送消息到队列
     *
     * @param message 发送消息
     */
    public StreamMessageId sendMessage(String message) {
        RStream<String, String> stream = redissonClient.getStream(REDIS_STREAM_MQ);
        String msgId = IdUtil.randomUUID();
        StreamAddArgs<String, String> streamAddArgs = StreamAddArgs.entry(msgId, message);
        return stream.add(streamAddArgs);
    }

    /**
     * 消费者消费消息
     */
    public void consumerMessage() {
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
            Map<StreamMessageId, Map<String, String>> messages = stream.readGroup(GROUP_NAME, consumerName, StreamReadGroupArgs.neverDelivered());
            for (Map.Entry<StreamMessageId, Map<String, String>> entry : messages.entrySet()) {
                // 得到每条消息
                Map<String, String> msg = entry.getValue();
                log.info("从 {} 的组 {} 中读取到消息：{}.", REDIS_STREAM_MQ, GROUP_NAME, msg);
                StreamMessageId streamMessageId = entry.getKey();
                // 处理一条 ack一条
                stream.ack(GROUP_NAME, streamMessageId);
            }
        }
    }

}
