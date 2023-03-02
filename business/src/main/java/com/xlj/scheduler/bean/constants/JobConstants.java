package com.xlj.scheduler.bean.constants;

/**
 * 任务执行常量
 *
 * @author zhangkun
 * @date 2020/12/10 8:48
 */
public interface JobConstants {

    /**
     * 消息配置Service
     */
    String JOB_TEST_SERVICE = "jobDemo";
    String JOB_TEST_SERVICE_METHOD_NAME = "test";

    /**
     * 错失执行策略（1错失周期立即执行 2错失周期执行一次 3下周期执行）
     */
    String MISFIRE_POLICY_NOW = "1";
    String MISFIRE_POLICY_NOE = "2";
    String MISFIRE_POLICY_NEXT = "3";

    /**
     * 重复类型，0.不重复 1.天 2.周 3.月 4.年
     */
    String REPEAT_RATE_UNIT_NON = "0";
    String REPEAT_RATE_UNIT_DAY = "1";
    String REPEAT_RATE_UNIT_WEEK = "2";
    String REPEAT_RATE_UNIT_MONTH = "3";
    String REPEAT_RATE_UNIT_YEAR = "4";
}
