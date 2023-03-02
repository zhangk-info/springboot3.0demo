package com.xlj.scheduler.demo;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xlj.scheduler.bean.constants.JobConstants;
import com.xlj.scheduler.bean.entity.SysJob;
import com.xlj.scheduler.bean.enums.JobTypeQuartzEnum;
import com.xlj.scheduler.bean.enums.QuartzEnum;
import com.xlj.scheduler.bean.util.CronUtil;
import com.xlj.scheduler.service.SysJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

/**
 * @date 2020/12/9 10:15
 * @description: 消息通知定时任务
 */
@Slf4j
@Component
public class JobDemo {

    @Autowired
    private SysJobService sysJobService;

    /**
     * 添加定时任务。注意jobGroup + jobName组合构成唯一值
     * jobGroup jobName为 timeFieldMsgHandler_[id]
     *
     * @param jobGroup          任务分组
     * @param primaryId         主表主键或者唯一ID
     * @param executeTime       执行时间
     * @param methodParamsValue 传递参数
     */
    public void addJob(String jobGroup, String primaryId, Date executeTime, String methodParamsValue) {
        String jobName = "testHandler_" + primaryId;
        String cronExpression = CronUtil.createCron("0", executeTime, null, null);
        // 判断定时任务中是否有unitId + primaryId为主键且时间和当前的时间相同的定时任务
        SysJob sysJob = sysJobService.getOne(new LambdaQueryWrapper<SysJob>()
                .eq(SysJob::getJobGroup, jobGroup)
                .eq(SysJob::getJobName, jobName)
                .ne(SysJob::getJobStatus, "4"));
        if (Objects.nonNull(sysJob)) {
            if (!sysJob.getCronExpression().equals(cronExpression)) {
                // 如果新计算出来的表达式和原来的表达式不一样，那么删除原来的并创建新的 设置overwrite-existing-jobs: true之后可以不用删除直接覆盖已有
                log.debug("新计算出来的表达式【" + cronExpression + "】和原来的表达式【" + sysJob.getCronExpression() + "】不一样，那么删除原来的并创建新的");
                sysJobService.removeJob(jobGroup, jobName);
            } else {
                return;
            }
        }
        // 创建新的
        sysJob = new SysJob();
        sysJob.setCronExpression(cronExpression);
        sysJob.setJobGroup(jobGroup);
        sysJob.setJobName(jobName);
        sysJob.setJobType(JobTypeQuartzEnum.SPRING_BEAN.getType());
        sysJob.setClassName(JobConstants.JOB_TEST_SERVICE);
        sysJob.setMethodName(JobConstants.JOB_TEST_SERVICE_METHOD_NAME);
        sysJob.setMisfirePolicy(JobConstants.MISFIRE_POLICY_NOE);
        sysJob.setMethodParamsValue(methodParamsValue);
        sysJob.setEndTime(DateUtil.offsetDay(new Date(), 1));
        sysJobService.addOrUpdateJob(sysJob);
        log.debug("创建新的定时任务成功，jobId: " + sysJob.getId());
    }

    /**
     * 测试定时任务
     */
    public String test(String jsonData) {
        log.info("测试定时任务: " + jsonData);
        return QuartzEnum.JOB_LOG_STATUS_SUCCESS.getType();
    }
}
