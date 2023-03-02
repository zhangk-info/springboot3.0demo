package com.xlj.scheduler.event;

import com.xlj.scheduler.bean.entity.SysJobLog;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 定时任务日志多线程事件
 */
@Getter
@AllArgsConstructor
public class SysJobLogEvent {
    private final SysJobLog sysJobLog;
}
