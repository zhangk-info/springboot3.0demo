package com.xlj.job;

import cn.hutool.core.date.DateUtil;
import com.xlj.DemoApplication;
import com.xlj.scheduler.demo.JobDemo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.UUID;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = DemoApplication.class)
@ComponentScan({"com.xlj.common"})
public class JobTest {

    @Autowired
    private JobDemo jobDemo;

    @Test
    public void job() throws Exception {
        // 创建定时任务
        jobDemo.addJob("test", UUID.randomUUID().toString(), DateUtil.offsetSecond(new Date(), 10), "{\"k\":\"v\"}");
    }
}
