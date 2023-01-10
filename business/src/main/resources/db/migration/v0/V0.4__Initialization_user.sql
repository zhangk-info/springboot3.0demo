-- ----------------------------
-- 员工或者客户
-- ----------------------------
drop table if exists user;
CREATE TABLE `user`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `openid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信openid',
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '电话号码',
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户名',
  `nickname` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '昵称',
  `cover_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '头像',
  `company_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '店铺名称',
  `province_id` bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '省份ID',
  `province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '省份名称',
  `city_id` bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '城市ID',
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '城市名称',
  `district_id` bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '县区ID',
  `district` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '县区名称',
  `last_store_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '登录后默认选择的store_id',
  `balance` decimal(10, 2) NULL DEFAULT NULL COMMENT '预存保险费',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '金店店铺详细地址',
   `is_delete` tinyint(1) NOT NULL DEFAULT 0,
   create_by         varchar(64)     default ''                 comment '创建者',
   create_at 	     datetime                                   comment '创建时间',
   update_by         varchar(64)     default ''                 comment '更新者',
   update_at         datetime                                   comment '更新时间',
   delete_by         varchar(64)     default ''                 comment '删除者',
   delete_at         datetime                                   comment '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;


-- 插入一条默认数据用于测试
INSERT INTO `user` (id, `openid`, `mobile`, `password`, `username`, `nickname`, `cover_url`, `company_name`, `province_id`, `province`, `city_id`, `city`, `district_id`, `district`, `create_at`, `update_at`, `delete_at`, `is_delete`, `last_store_id`, `balance`, `address`) VALUES
(1, '', '18990442158', 'f1c830a6ccac94c973bf51afa4cb5423', NULL, '', 'https://zhubao-xinglianjin.oss-cn-chengdu.aliyuncs.com/site/20221227/cMxS70FTyBJaerqkzxaa0yz8f7h8M8OFO3dCAJ8J.png', '光合首饰', 110000, '北京市', 110100, '北京城区', 110106, '丰台区', '2022-10-26 11:41:47', '2022-12-27 17:19:18', '2022-10-26 11:41:47', 0, 1, 140.00, '梨花街88号0');
