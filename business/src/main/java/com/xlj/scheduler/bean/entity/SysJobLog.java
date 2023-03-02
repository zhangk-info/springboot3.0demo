package com.xlj.scheduler.bean.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.xlj.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 定时任务执行日志表
 *
 * @date 2019-01-27 13:40:20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SysJobLog", description = "定时任务日志")
public class SysJobLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    private Long jobId;
    /**
     * 任务名称
     */
    private String jobName;
    /**
     * 任务组名
     */
    private String jobGroup;
    /**
     * 组内执行顺利，值越大执行优先级越高，最大值9，最小值1
     */
    private Integer jobOrder;
    /**
     * 1、java类;2、spring bean名称;3、rest调用;4、jar调用;9其他
     */
    private String jobType;
    /**
     * job_type=3时，rest调用地址，仅支持post协议;job_type=4时，jar路径;其它值为空
     */
    private String executePath;
    /**
     * job_type=1时，类完整路径;job_type=2时，spring bean名称;其它值为空
     */
    private String className;
    /**
     * 任务方法
     */
    private String methodName;
    /**
     * 参数值
     */
    private String methodParamsValue;
    /**
     * cron执行表达式
     */
    private String cronExpression;
    /**
     * 日志信息
     */
    private String jobMessage;
    /**
     * 执行状态（0正常 1失败）
     */
    private String jobLogStatus;
    /**
     * 执行时间
     */
    private String executeTime;
    /**
     * 异常信息
     */
    private String exceptionInfo;

    @Schema(description = "删除状态")
    @TableLogic
    private Boolean isDelete;

}
