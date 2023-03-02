package com.xlj.scheduler.util;

import cn.hutool.core.util.StrUtil;
import com.xlj.scheduler.bean.entity.SysJob;
import com.xlj.scheduler.bean.entity.SysJobLog;
import com.xlj.scheduler.bean.enums.JobTypeQuartzEnum;
import com.xlj.scheduler.bean.enums.QuartzEnum;
import com.xlj.scheduler.event.SysJobLogEvent;
import com.xlj.scheduler.exception.TaskException;
import com.xlj.scheduler.service.SysJobService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Date;

/**
 * 定时任务反射工具类
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class TaskInvokUtil {
    private final ApplicationEventPublisher publisher;

    @Autowired
    private SysJobService sysJobService;
    @Autowired
    private ApplicationContext applicationContext;

    @SneakyThrows(value = Exception.class)
    public void invokMethod(SysJob sysJob, Trigger trigger) {

        ITaskInvok iTaskInvok;
        //执行开始时间
        long startTime;
        //执行结束时间
        long endTime;
        //获取执行开始时间
        startTime = System.currentTimeMillis();
        //更新定时任务表内的状态、执行时间、上次执行时间、下次执行时间等信息
        SysJob updateSysjob = new SysJob();
        updateSysjob.setId(sysJob.getId());
        //日志
        SysJobLog sysJobLog = new SysJobLog();
        sysJobLog.setJobId(sysJob.getId());
        sysJobLog.setJobName(sysJob.getJobName());
        sysJobLog.setJobGroup(sysJob.getJobGroup());
        sysJobLog.setJobOrder(sysJob.getJobOrder());
        sysJobLog.setJobType(sysJob.getJobType());
        sysJobLog.setExecutePath(sysJob.getExecutePath());
        sysJobLog.setClassName(sysJob.getClassName());
        sysJobLog.setMethodName(sysJob.getMethodName());
        sysJobLog.setMethodParamsValue(sysJob.getMethodParamsValue());
        sysJobLog.setCronExpression(sysJob.getCronExpression());
        try {
            if (StringUtils.isBlank(sysJob.getJobType())) {
                log.info("定时任务类型无对应反射方式，反射类型为空");
                throw new TaskException("定时任务类型无对应反射方式，反射类型为空");
            } else {
                if (JobTypeQuartzEnum.JAVA.getType().equals(sysJob.getJobType())) {
                    iTaskInvok = (ITaskInvok) applicationContext.getBean("javaClassTaskInvok");
                    iTaskInvok.invokMethod(sysJob);
                } else if (JobTypeQuartzEnum.SPRING_BEAN.getType().equals(sysJob.getJobType())) {
                    iTaskInvok = (ITaskInvok) applicationContext.getBean("springBeanTaskInvok");
                    iTaskInvok.invokMethod(sysJob);
                } else if (JobTypeQuartzEnum.REST.getType().equals(sysJob.getJobType())) {
                    iTaskInvok = (ITaskInvok) applicationContext.getBean("restTaskInvok");
                    iTaskInvok.invokMethod(sysJob);
                } else if (JobTypeQuartzEnum.JAR.getType().equals(sysJob.getJobType())) {
                    iTaskInvok = (ITaskInvok) applicationContext.getBean("jarTaskInvok");
                    iTaskInvok.invokMethod(sysJob);
                } else {
                    log.info("定时任务类型无对应反射方式，反射类型:{}", sysJob.getJobType());
                    throw new TaskException("");
                }
            }

            //获取执行结束时间
            endTime = System.currentTimeMillis();
            sysJobLog.setJobMessage(QuartzEnum.JOB_LOG_STATUS_SUCCESS.getDescription());
            sysJobLog.setJobLogStatus(QuartzEnum.JOB_LOG_STATUS_SUCCESS.getType());
            sysJobLog.setExecuteTime(String.valueOf(endTime - startTime));
            //任务表信息更新
            updateSysjob.setJobExecuteStatus(QuartzEnum.JOB_LOG_STATUS_SUCCESS.getType());
            updateSysjob.setStartTime(trigger.getStartTime());
            updateSysjob.setPreviousTime(trigger.getPreviousFireTime());
            updateSysjob.setNextTime(trigger.getNextFireTime());
        } catch (Throwable e) {
            //获取执行结束时间
            endTime = System.currentTimeMillis();
            log.error("定时任务执行失败，任务名称：{}；任务组名：{}，cron执行表达式：{}，执行时间：{}", sysJob.getJobName(), sysJob.getJobGroup(), sysJob.getCronExpression(), new Date(), e);
            sysJobLog.setJobMessage(QuartzEnum.JOB_LOG_STATUS_FAIL.getDescription());
            sysJobLog.setJobLogStatus(QuartzEnum.JOB_LOG_STATUS_FAIL.getType());
            sysJobLog.setExecuteTime(String.valueOf(endTime - startTime));
            sysJobLog.setExceptionInfo(StrUtil.sub(e.getMessage(), 0, 2000));
            //任务表信息更新
            updateSysjob.setJobExecuteStatus(QuartzEnum.JOB_LOG_STATUS_FAIL.getType());
            updateSysjob.setStartTime(trigger.getStartTime());
            updateSysjob.setPreviousTime(trigger.getPreviousFireTime());
            updateSysjob.setNextTime(trigger.getNextFireTime());
        } finally {
            publisher.publishEvent(new SysJobLogEvent(sysJobLog));
            sysJobService.updateById(updateSysjob);
        }
    }
}
