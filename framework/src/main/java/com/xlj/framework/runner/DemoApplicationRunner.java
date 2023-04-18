package com.xlj.framework.runner;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.xlj.common.encrypted_transfer.Sm2EnableCondition;
import com.xlj.common.entity.AjaxResult;
import com.xlj.common.entity.DataResp;
import com.xlj.common.sgcc.Sm2Utils;
import com.xlj.common.sgcc.Sm4Utils;
import com.xlj.framework.mq.QueueService;
import com.xlj.system.service.ISysConfigService;
import com.xlj.system.service.ISysDictTypeService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springdoc.core.converters.ConverterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class DemoApplicationRunner implements ApplicationRunner {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ISysDictTypeService sysDictTypeService;
    @Autowired
    private QueueService queueService;
    @Autowired
    private RedissonClient redissonClient;

    @Bean
    public Sm4Utils sm4Utils() {
        return new Sm4Utils();
    }

    @Conditional(Sm2EnableCondition.class)
    @Bean
    public Sm2Utils sm2Utils() {
        return new Sm2Utils();
    }

    /**
     * 项目启动后做一些事情
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 增加自定义的返回类型 用以支持springdoc @link ResponseSupportConverter.resolve isResponseTypeWrapper(cls) org.springdoc.core.converters.ConverterUtils
        ConverterUtils.addResponseWrapperToIgnore(DataResp.class);
        ConverterUtils.addResponseWrapperToIgnore(AjaxResult.class);
        // 项目启动时，初始化参数到缓存
        sysConfigService.loadingConfigCache();
        // 项目启动时，初始化字典到缓存
        sysDictTypeService.loadingDictCache();

        // 执行redis队列消息消费线程
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNamePrefix("redis-queue-consumer-pool-").build();
        ExecutorService redisQueueConsumerThreadPool = new ThreadPoolExecutor(1, 1,
                1000000, TimeUnit.DAYS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        redisQueueConsumerThreadPool.execute(() -> {
            log.info("开始执行redis队列消息消费线程 ---------------   ---------------   ---------------  ");
            queueService.onMessage(redissonClient.getConfig());
        });
    }
}
