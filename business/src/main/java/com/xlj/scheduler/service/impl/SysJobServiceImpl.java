package com.xlj.scheduler.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xlj.scheduler.bean.entity.SysJob;
import com.xlj.scheduler.bean.enums.QuartzEnum;
import com.xlj.scheduler.mapper.SysJobMapper;
import com.xlj.scheduler.service.SysJobService;
import com.xlj.scheduler.util.TaskUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 定时任务调度表
 *
 * @date 2019-01-27 10:04:42
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysJobServiceImpl extends ServiceImpl<SysJobMapper, SysJob> implements SysJobService {

    @Autowired
    private TaskUtil taskUtil;
    @Autowired
    private Scheduler scheduler;

    @Override
    public Long addOrUpdateJob(SysJob sysJob) {
        log.info("收到定时任务数据[{}]", JSONUtil.toJsonStr(sysJob));
        Assert.notNull(sysJob, "任务不能为空");
        SysJob job = baseMapper.selectOne(
                Wrappers.<SysJob>lambdaQuery()
                        .eq(SysJob::getJobGroup, sysJob.getJobGroup())
                        .eq(SysJob::getJobName, sysJob.getJobName())
                        .ne(SysJob::getJobStatus, "4"));

        if (job != null) {
            sysJob.setId(job.getId());
            baseMapper.updateById(sysJob);
        } else {
            baseMapper.insert(sysJob);
        }
        taskUtil.addOrUpateJob(sysJob, scheduler);
        return sysJob.getId();
    }

    @Override
    public void removeJob(String jobGroup, String jobName) {
        Assert.notNull(jobGroup, "任务组不能为空");
        Assert.notNull(jobName, "任务名不能为空");
        SysJob job = baseMapper.selectOne(
                Wrappers.<SysJob>lambdaQuery()
                        .eq(SysJob::getJobGroup, jobGroup)
                        .eq(SysJob::getJobName, jobName)
                        .ne(SysJob::getJobStatus, "4"));
        if (job != null) {
            job.setJobStatus(QuartzEnum.JOB_STATUS_DEL.getType());
            baseMapper.updateById(job);
            taskUtil.removeJob(job, scheduler);
        }
    }
}
