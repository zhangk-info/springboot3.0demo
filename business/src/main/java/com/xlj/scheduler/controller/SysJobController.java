package com.xlj.scheduler.controller;

import com.xlj.common.entity.DataResp;
import com.xlj.scheduler.bean.entity.SysJob;
import com.xlj.scheduler.service.SysJobService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 定时任务调度表
 *
 * @date 2019-01-27 10:04:42
 */
@RestController
public class SysJobController {

    @Autowired
    private SysJobService sysJobService;

    /**
     * 保存定时任务
     *
     * @param job
     * @return
     */
    @Operation(description = "保存定时任务")
    @PostMapping("/quartz/addOrUpdate")
    public DataResp<Long> addOrUpdate(@RequestBody SysJob job) {
        sysJobService.addOrUpdateJob(job);
        return DataResp.success(job.getId());
    }

    @Operation(description = "移除定时任务")
    @PostMapping("/quartz/removeJob")
    public DataResp removeJob(String jobGroup, String jobName) {
        sysJobService.removeJob(jobGroup, jobName);
        return DataResp.success();
    }
}
