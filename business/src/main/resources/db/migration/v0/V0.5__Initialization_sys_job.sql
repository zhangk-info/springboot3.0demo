-- ----------------------------
-- 定时任务调度表
-- ----------------------------
drop table if exists sys_job;
CREATE TABLE `sys_job` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `job_name` varchar(255) DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(255) DEFAULT NULL COMMENT '任务组名',
  `job_order` int(1) DEFAULT NULL COMMENT '组内执行顺利，值越大执行优先级越高，最大值9，最小值1',
  `job_type` varchar(255) DEFAULT NULL COMMENT '1、java类;2、spring bean名称;3、rest调用;4、jar调用;9其他',
  `execute_path` varchar(255) DEFAULT NULL COMMENT 'job_type=3时，rest调用地址，仅支持rest get协议,需要增加String返回值，0成功，1失败;job_type=4时，jar路径;其它值为空',
  `class_name` varchar(255) DEFAULT NULL COMMENT 'job_type=1时，类完整路径;job_type=2时，spring bean名称;其它值为空',
  `method_name` varchar(255) DEFAULT NULL COMMENT '任务方法',
  `method_params_value` varchar(255) DEFAULT NULL COMMENT '参数值jsonStr',
  `cron_expression` varchar(255) DEFAULT NULL COMMENT 'cron执行表达式',
  `misfire_policy` varchar(255) DEFAULT NULL COMMENT '错失执行策略（1错失周期立即执行 2错失周期执行一次 3下周期执行）',
  `job_status` varchar(255) DEFAULT NULL COMMENT '状态（0、未发布;1、已发布;2、运行中;3、暂停;4、删除;）',
  `job_execute_status` varchar(255) DEFAULT NULL COMMENT '状态（0正常 1异常）',
  `start_time` datetime DEFAULT NULL COMMENT '首次执行时间',
  `previous_time` datetime DEFAULT NULL COMMENT '上次执行时间',
  `next_time` datetime DEFAULT NULL COMMENT '下次执行时间',
  `remark` text DEFAULT NULL COMMENT '备注信息',
  `end_time` datetime DEFAULT NULL COMMENT '任务截至时间',
  -- 默认字段
  `is_delete` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_at` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_by` varchar(64) DEFAULT '' COMMENT '删除者',
  `delete_at` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- 定时任务调度记录表
-- ----------------------------
drop table if exists sys_job_log;
CREATE TABLE `sys_job_log` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `job_id` bigint(20) NOT NULL COMMENT '任务ID',
  `job_name` varchar(255) DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(255) DEFAULT NULL COMMENT '任务组名',
  `job_order` int(1) DEFAULT NULL COMMENT '组内执行顺利，值越大执行优先级越高，最大值9，最小值1',
  `job_type` varchar(255) DEFAULT NULL COMMENT '1、java类;2、spring bean名称;3、rest调用;4、jar调用;9其他',
  `execute_path` varchar(255) DEFAULT NULL COMMENT 'job_type=3时，rest调用地址，仅支持rest get协议,需要增加String返回值，0成功，1失败;job_type=4时，jar路径;其它值为空',
  `class_name` varchar(255) DEFAULT NULL COMMENT 'job_type=1时，类完整路径;job_type=2时，spring bean名称;其它值为空',
  `method_name` varchar(255) DEFAULT NULL COMMENT '任务方法',
  `method_params_value` varchar(255) DEFAULT NULL COMMENT '参数值jsonStr',
  `cron_expression` varchar(255) DEFAULT NULL COMMENT 'cron执行表达式',
  `job_message` text DEFAULT NULL COMMENT '日志信息',
  `job_log_status` varchar(255) DEFAULT NULL COMMENT '执行状态（0正常 1失败）',
  `execute_time` varchar(255) DEFAULT NULL COMMENT '执行时间',
  `exception_info` text DEFAULT NULL COMMENT '异常信息',
  -- 默认字段
  `is_delete` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_at` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_by` varchar(64) DEFAULT '' COMMENT '删除者',
  `delete_at` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;