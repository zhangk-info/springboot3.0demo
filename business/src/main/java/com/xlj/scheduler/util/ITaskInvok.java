package com.xlj.scheduler.util;


import com.xlj.scheduler.bean.entity.SysJob;
import com.xlj.scheduler.exception.TaskException;

/**
 * 定时任务反射实现接口类
 */
public interface ITaskInvok {

    /**
     * 执行反射方法
     *
     * @param sysJob 配置类
     * @throws TaskException
     */
    void invokMethod(SysJob sysJob) throws TaskException;
}
