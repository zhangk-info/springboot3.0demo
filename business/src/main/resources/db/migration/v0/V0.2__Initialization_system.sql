-- ----------------------------
-- 1、部门表
-- ----------------------------
drop table if exists sys_dept;
create table sys_dept (
    dept_id           bigint(20) UNSIGNED      not null auto_increment    comment '部门id',
    parent_id         bigint(20) UNSIGNED      default 0                  comment '父部门id',
    ancestors         varchar(50)     default ''                 comment '祖级列表',
    dept_name         varchar(30)     default ''                 comment '部门名称',
    order_num         int(4)          default 0                  comment '显示顺序',
    leader            varchar(20)     default null               comment '负责人',
    phone             varchar(11)     default null               comment '联系电话',
    email             varchar(50)     default null               comment '邮箱',
    status            char(1)         default '0'                comment '部门状态（0正常 1停用）',
    del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
    create_by         varchar(64)     default ''                 comment '创建者',
    create_at 	      datetime                                   comment '创建时间',
    update_by         varchar(64)     default ''                 comment '更新者',
    update_at         datetime                                   comment '更新时间',
    delete_by         varchar(64)     default ''                 comment '删除者',
    delete_at         datetime                                   comment '删除时间',
  primary key (dept_id)
) engine=innodb auto_increment=200 comment = '部门表';

-- ----------------------------
-- 初始化-部门表数据
-- ----------------------------
insert into sys_dept values
(100,  0,   '0',          '星联金',   0, '星联金', '15888888888', 'test@email.com', '0', '0', 1, sysdate(), null, sysdate(), null, sysdate())
,(101,  100, '0,100',      '深圳总公司', 1, '星联金', '15888888888', 'test@email.com', '0', '0', 1, sysdate(), null, sysdate(), null, sysdate())
,(102,  100, '0,100',      '长沙分公司', 2, '星联金', '15888888888', 'test@email.com', '0', '0', 1, sysdate(), null, sysdate(), null, sysdate())
,(103,  101, '0,100,101',  '研发部门',   1, '星联金', '15888888888', 'test@email.com', '0', '0', 1, sysdate(), null, sysdate(), null, sysdate())
,(104,  101, '0,100,101',  '市场部门',   2, '星联金', '15888888888', 'test@email.com', '0', '0', 1, sysdate(), null, sysdate(), null, sysdate())
,(105,  101, '0,100,101',  '测试部门',   3, '星联金', '15888888888', 'test@email.com', '0', '0', 1, sysdate(), null, sysdate(), null, sysdate())
,(106,  101, '0,100,101',  '财务部门',   4, '星联金', '15888888888', 'test@email.com', '0', '0', 1, sysdate(), null, sysdate(), null, sysdate())
,(107,  101, '0,100,101',  '运维部门',   5, '星联金', '15888888888', 'test@email.com', '0', '0', 1, sysdate(), null, sysdate(), null, sysdate())
,(108,  102, '0,100,102',  '市场部门',   1, '星联金', '15888888888', 'test@email.com', '0', '0', 1, sysdate(), null, sysdate(), null, sysdate())
,(109,  102, '0,100,102',  '财务部门',   2, '星联金', '15888888888', 'test@email.com', '0', '0', 1, sysdate(), null, sysdate(), null, sysdate());


-- ----------------------------
-- 2、用户信息表
-- ----------------------------
drop table if exists sys_user;
create table sys_user (
    id                bigint(20) UNSIGNED      not null auto_increment    comment '用户ID',
    store_id          bigint(20) UNSIGNED      null                       comment '展厅ID',
    dept_id           bigint(20) UNSIGNED      default null               comment '部门ID',
    user_name         varchar(30)     not null                   comment '用户账号',
    nick_name         varchar(30)     not null                   comment '用户昵称',
    user_type         varchar(2)      default '00'               comment '用户类型（00系统用户）',
    email             varchar(50)     default ''                 comment '用户邮箱',
    phonenumber       varchar(11)     default ''                 comment '手机号码',
    sex               char(1)         default '0'                comment '用户性别（0男 1女 2未知）',
    avatar            varchar(100)    default ''                 comment '头像地址',
    password          varchar(100)    default ''                 comment '密码',
    status            char(1)         default '0'                comment '帐号状态（0正常 1停用）',
    del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
    login_ip          varchar(128)    default ''                 comment '最后登录IP',
    login_date        datetime                                   comment '最后登录时间',
    create_by         varchar(64)     default ''                 comment '创建者',
    create_at 	      datetime                                   comment '创建时间',
    update_by         varchar(64)     default ''                 comment '更新者',
    update_at         datetime                                   comment '更新时间',
    delete_by         varchar(64)     default ''                 comment '删除者',
    delete_at         datetime                                   comment '删除时间',
  primary key (id)
) engine=innodb auto_increment=100 comment = '用户信息表';

-- ----------------------------
-- 初始化-用户信息表数据
-- ----------------------------
insert into sys_user values
(1,  null, 100, 'admin', '超级管理员', '00', 'test@email.com', '18990442158', '1', '', 'f1c830a6ccac94c973bf51afa4cb5423', '0', '0', '127.0.0.1', sysdate(), 1, sysdate(), null, null, null, null),
(2,  1, 103, 'storeAdmin',    '星联金', '00', 'test@email.com',  '18990442158', '1', '', 'f1c830a6ccac94c973bf51afa4cb5423', '0', '0', '127.0.0.1', sysdate(), 1, sysdate(), null, null, null, null);


-- ----------------------------
-- 3、岗位信息表
-- ----------------------------
drop table if exists sys_post;
create table sys_post
(
    post_id       bigint(20) UNSIGNED      not null auto_increment    comment '岗位ID',
    post_code     varchar(64)     not null                   comment '岗位编码',
    post_name     varchar(50)     not null                   comment '岗位名称',
    post_sort     int(4)          not null                   comment '显示顺序',
    status        char(1)         not null                   comment '状态（0正常 1停用）',
    create_by         varchar(64)     default ''                 comment '创建者',
    create_at 	      datetime                                   comment '创建时间',
    update_by         varchar(64)     default ''                 comment '更新者',
    update_at         datetime                                   comment '更新时间',
    delete_by         varchar(64)     default ''                 comment '删除者',
    delete_at         datetime                                   comment '删除时间',
  primary key (post_id)
) engine=innodb comment = '岗位信息表';

-- ----------------------------
-- 初始化-岗位信息表数据
-- ----------------------------
insert into sys_post values(1, 'ceo',  '董事长',    1, '0', 1, sysdate(), null, null, null, null),
(2, 'se',   '项目经理',  2, '0', 1, sysdate(), null, null, null, null),
(3, 'hr',   '人力资源',  3, '0', 1, sysdate(), null, null, null, null),
(4, 'user', '普通员工',  4, '0', 1, sysdate(), null, null, null, null);


-- ----------------------------
-- 4、角色信息表
-- ----------------------------
drop table if exists sys_role;
create table sys_role (
    role_id              bigint(20) UNSIGNED      not null auto_increment    comment '角色ID',
    role_name            varchar(30)     not null                   comment '角色名称',
    role_key             varchar(100)    not null                   comment '角色权限字符串',
    role_sort            int(4)          not null                   comment '显示顺序',
    data_scope           char(1)         default '1'                comment '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
    menu_check_strictly  tinyint(1)      default 1                  comment '菜单树选择项是否关联显示',
    dept_check_strictly  tinyint(1)      default 1                  comment '部门树选择项是否关联显示',
    status               char(1)         not null                   comment '角色状态（0正常 1停用）',
    del_flag             char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
    create_by         varchar(64)     default ''                 comment '创建者',
    create_at 	      datetime                                   comment '创建时间',
    update_by         varchar(64)     default ''                 comment '更新者',
    update_at         datetime                                   comment '更新时间',
    delete_by         varchar(64)     default ''                 comment '删除者',
    delete_at         datetime                                   comment '删除时间',
  primary key (role_id)
) engine=innodb auto_increment=100 comment = '角色信息表';

-- ----------------------------
-- 初始化-角色信息表数据
-- ----------------------------
insert into sys_role values
('1', '超级管理员',  1,  1, 1, 1, 1, '0', '0', 1, sysdate(), null, null, null, null),
('2', '展厅管理员', 'store_admin', 2, 2, 1, 1, '0', '0', 1, sysdate(), null, null, null, null);


-- ----------------------------
-- 5、菜单权限表
-- ----------------------------
drop table if exists sys_menu;
create table sys_menu (
    menu_id           bigint(20) UNSIGNED      not null auto_increment    comment '菜单ID',
    menu_name         varchar(50)     not null                   comment '菜单名称',
    parent_id         bigint(20) UNSIGNED      default 0                  comment '父菜单ID',
    order_num         int(4)          default 0                  comment '显示顺序',
    path              varchar(200)    default ''                 comment '路由地址',
    component         varchar(255)    default null               comment '组件路径',
    query             varchar(255)    default null               comment '路由参数',
    is_frame          int(1)          default 1                  comment '是否为外链（0是 1否）',
    is_cache          int(1)          default 0                  comment '是否缓存（0缓存 1不缓存）',
    menu_type         char(1)         default ''                 comment '菜单类型（M目录 C菜单 F按钮）',
    visible           char(1)         default 0                  comment '菜单状态（0显示 1隐藏）',
    status            char(1)         default 0                  comment '菜单状态（0正常 1停用）',
    perms             varchar(100)    default null               comment '权限标识',
    icon              varchar(100)    default '#'                comment '菜单图标',
    create_by         varchar(64)     default ''                 comment '创建者',
    create_at 	      datetime                                   comment '创建时间',
    update_by         varchar(64)     default ''                 comment '更新者',
    update_at         datetime                                   comment '更新时间',
    delete_by         varchar(64)     default ''                 comment '删除者',
    delete_at         datetime                                   comment '删除时间',
  primary key (menu_id)
) engine=innodb auto_increment=2000 comment = '菜单权限表';

-- ----------------------------
-- 初始化-菜单信息表数据
-- ----------------------------
-- 一级菜单
insert into sys_menu values
('1', '系统管理', '0', '1', 'system',           null, '', 1, 0, 'M', '0', '0', '', 'system',   1, sysdate(), null, null, null, null)
,('2', '系统监控', '0', '2', 'monitor',          null, '', 1, 0, 'M', '0', '0', '', 'monitor',  1, sysdate(), null, null, null, null)
-- ,('3', '系统工具', '0', '3', 'tool',             null, '', 1, 0, 'M', '0', '0', '', 'tool',     1, sysdate(), null, null, null, null)
-- 二级菜单
,('100',  '用户管理', '1',   '1', 'user',       'system/user/index',        '', 1, 0, 'C', '0', '0', 'system:user:list',        'user',          1, sysdate(), null, null, null, null)
,('101',  '角色管理', '1',   '2', 'role',       'system/role/index',        '', 1, 0, 'C', '0', '0', 'system:role:list',        'peoples',       1, sysdate(), null, null, null, null)
,('102',  '菜单管理', '1',   '3', 'menu',       'system/menu/index',        '', 1, 0, 'C', '0', '0', 'system:menu:list',        'tree-table',    1, sysdate(), null, null, null, null)
,('103',  '部门管理', '1',   '4', 'dept',       'system/dept/index',        '', 1, 0, 'C', '0', '0', 'system:dept:list',        'tree',          1, sysdate(), null, null, null, null)
,('104',  '岗位管理', '1',   '5', 'post',       'system/post/index',        '', 1, 0, 'C', '0', '0', 'system:post:list',        'post',          1, sysdate(), null, null, null, null)
,('105',  '字典管理', '1',   '6', 'dict',       'system/dict/index',        '', 1, 0, 'C', '0', '0', 'system:dict:list',        'dict',          1, sysdate(), null, null, null, null)
,('106',  '参数设置', '1',   '7', 'config',     'system/config/index',      '', 1, 0, 'C', '0', '0', 'system:config:list',      'edit',          1, sysdate(), null, null, null, null)
,('107',  '通知公告', '1',   '8', 'notice',     'system/notice/index',      '', 1, 0, 'C', '0', '0', 'system:notice:list',      'message',       1, sysdate(), null, null, null, null)
,('108',  '日志管理', '1',   '9', 'log',        '',                         '', 1, 0, 'M', '0', '0', '',                        'log',           1, sysdate(), null, null, null, null)
-- ,('109',  '在线用户', '2',   '1', 'online',     'monitor/online/index',     '', 1, 0, 'C', '0', '0', 'monitor:online:list',     'online',        1, sysdate(), null, null, null, null)
-- ,('110',  '定时任务', '2',   '2', 'job',        'monitor/job/index',        '', 1, 0, 'C', '0', '0', 'monitor:job:list',        'job',           1, sysdate(), null, null, null, null)
-- ,('111',  '数据监控', '2',   '3', 'druid',      'monitor/druid/index',      '', 1, 0, 'C', '0', '0', 'monitor:druid:list',      'druid',         1, sysdate(), null, null, null, null)
-- ,('112',  '服务监控', '2',   '4', 'server',     'monitor/server/index',     '', 1, 0, 'C', '0', '0', 'monitor:server:list',     'server',        1, sysdate(), null, null, null, null)
-- ,('113',  '缓存监控', '2',   '5', 'cache',      'monitor/cache/index',      '', 1, 0, 'C', '0', '0', 'monitor:cache:list',      'redis',         1, sysdate(), null, null, null, null)
,('114',  '缓存列表', '2',   '6', 'cacheList',  'monitor/cache/list',       '', 1, 0, 'C', '0', '0', 'monitor:cache:list',      'redis-list',    1, sysdate(), null, null, null, null)
-- ,('115',  '表单构建', '3',   '1', 'build',      'tool/build/index',         '', 1, 0, 'C', '0', '0', 'tool:build:list',         'build',         1, sysdate(), null, null, null, null)
-- ,('116',  '代码生成', '3',   '2', 'gen',        'tool/gen/index',           '', 1, 0, 'C', '0', '0', 'tool:gen:list',           'code',          1, sysdate(), null, null, null, null)
-- ,('117',  '系统接口', '3',   '3', 'swagger',    'tool/swagger/index',       '', 1, 0, 'C', '0', '0', 'tool:swagger:list',       'swagger',       1, sysdate(), null, null, null, null)
-- 三级菜单
,('500',  '操作日志', '108', '1', 'operlog',    'monitor/operlog/index',    '', 1, 0, 'C', '0', '0', 'monitor:operlog:list',    'form',          1, sysdate(), null, null, null, null)
,('501',  '登录日志', '108', '2', 'logininfor', 'monitor/logininfor/index', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', 'logininfor',    1, sysdate(), null, null, null, null)
-- 用户管理按钮
,('1000', '用户查询', '100', '1',  '', '', '', 1, 0, 'F', '0', '0', 'system:user:query',          '#', 1, sysdate(), null, null, null, null)
,('1001', '用户新增', '100', '2',  '', '', '', 1, 0, 'F', '0', '0', 'system:user:add',            '#', 1, sysdate(), null, null, null, null)
,('1002', '用户修改', '100', '3',  '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit',           '#', 1, sysdate(), null, null, null, null)
,('1003', '用户删除', '100', '4',  '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove',         '#', 1, sysdate(), null, null, null, null)
,('1004', '用户导出', '100', '5',  '', '', '', 1, 0, 'F', '0', '0', 'system:user:export',         '#', 1, sysdate(), null, null, null, null)
,('1005', '用户导入', '100', '6',  '', '', '', 1, 0, 'F', '0', '0', 'system:user:import',         '#', 1, sysdate(), null, null, null, null)
,('1006', '重置密码', '100', '7',  '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd',       '#', 1, sysdate(), null, null, null, null)
-- 角色管理按钮
,('1007', '角色查询', '101', '1',  '', '', '', 1, 0, 'F', '0', '0', 'system:role:query',          '#', 1, sysdate(), null, null, null, null)
,('1008', '角色新增', '101', '2',  '', '', '', 1, 0, 'F', '0', '0', 'system:role:add',            '#', 1, sysdate(), null, null, null, null)
,('1009', '角色修改', '101', '3',  '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit',           '#', 1, sysdate(), null, null, null, null)
,('1010', '角色删除', '101', '4',  '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove',         '#', 1, sysdate(), null, null, null, null)
,('1011', '角色导出', '101', '5',  '', '', '', 1, 0, 'F', '0', '0', 'system:role:export',         '#', 1, sysdate(), null, null, null, null)
-- 菜单管理按钮
,('1012', '菜单查询', '102', '1',  '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query',          '#', 1, sysdate(), null, null, null, null)
,('1013', '菜单新增', '102', '2',  '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add',            '#', 1, sysdate(), null, null, null, null)
,('1014', '菜单修改', '102', '3',  '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit',           '#', 1, sysdate(), null, null, null, null)
,('1015', '菜单删除', '102', '4',  '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove',         '#', 1, sysdate(), null, null, null, null)
-- 部门管理按钮
,('1016', '部门查询', '103', '1',  '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query',          '#', 1, sysdate(), null, null, null, null)
,('1017', '部门新增', '103', '2',  '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add',            '#', 1, sysdate(), null, null, null, null)
,('1018', '部门修改', '103', '3',  '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit',           '#', 1, sysdate(), null, null, null, null)
,('1019', '部门删除', '103', '4',  '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove',         '#', 1, sysdate(), null, null, null, null)
-- 岗位管理按钮
,('1020', '岗位查询', '104', '1',  '', '', '', 1, 0, 'F', '0', '0', 'system:post:query',          '#', 1, sysdate(), null, null, null, null)
,('1021', '岗位新增', '104', '2',  '', '', '', 1, 0, 'F', '0', '0', 'system:post:add',            '#', 1, sysdate(), null, null, null, null)
,('1022', '岗位修改', '104', '3',  '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit',           '#', 1, sysdate(), null, null, null, null)
,('1023', '岗位删除', '104', '4',  '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove',         '#', 1, sysdate(), null, null, null, null)
,('1024', '岗位导出', '104', '5',  '', '', '', 1, 0, 'F', '0', '0', 'system:post:export',         '#', 1, sysdate(), null, null, null, null)
-- 字典管理按钮
,('1025', '字典查询', '105', '1', '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:query',          '#', 1, sysdate(), null, null, null, null)
,('1026', '字典新增', '105', '2', '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:add',            '#', 1, sysdate(), null, null, null, null)
,('1027', '字典修改', '105', '3', '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit',           '#', 1, sysdate(), null, null, null, null)
,('1028', '字典删除', '105', '4', '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove',         '#', 1, sysdate(), null, null, null, null)
,('1029', '字典导出', '105', '5', '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:export',         '#', 1, sysdate(), null, null, null, null)
-- 参数设置按钮
,('1030', '参数查询', '106', '1', '#', '', '', 1, 0, 'F', '0', '0', 'system:config:query',        '#', 1, sysdate(), null, null, null, null)
,('1031', '参数新增', '106', '2', '#', '', '', 1, 0, 'F', '0', '0', 'system:config:add',          '#', 1, sysdate(), null, null, null, null)
,('1032', '参数修改', '106', '3', '#', '', '', 1, 0, 'F', '0', '0', 'system:config:edit',         '#', 1, sysdate(), null, null, null, null)
,('1033', '参数删除', '106', '4', '#', '', '', 1, 0, 'F', '0', '0', 'system:config:remove',       '#', 1, sysdate(), null, null, null, null)
,('1034', '参数导出', '106', '5', '#', '', '', 1, 0, 'F', '0', '0', 'system:config:export',       '#', 1, sysdate(), null, null, null, null)
-- 通知公告按钮
,('1035', '公告查询', '107', '1', '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:query',        '#', 1, sysdate(), null, null, null, null)
,('1036', '公告新增', '107', '2', '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:add',          '#', 1, sysdate(), null, null, null, null)
,('1037', '公告修改', '107', '3', '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit',         '#', 1, sysdate(), null, null, null, null)
,('1038', '公告删除', '107', '4', '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove',       '#', 1, sysdate(), null, null, null, null)
-- 操作日志按钮
,('1039', '操作查询', '500', '1', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query',      '#', 1, sysdate(), null, null, null, null)
,('1040', '操作删除', '500', '2', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove',     '#', 1, sysdate(), null, null, null, null)
,('1041', '日志导出', '500', '3', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export',     '#', 1, sysdate(), null, null, null, null)
-- 登录日志按钮
,('1042', '登录查询', '501', '1', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query',   '#', 1, sysdate(), null, null, null, null)
,('1043', '登录删除', '501', '2', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove',  '#', 1, sysdate(), null, null, null, null)
,('1044', '日志导出', '501', '3', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export',  '#', 1, sysdate(), null, null, null, null)
,('1045', '账户解锁', '501', '4', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:unlock',  '#', 1, sysdate(), null, null, null, null)
-- 在线用户按钮
-- ,('1046', '在线查询', '109', '1', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query',       '#', 1, sysdate(), null, null, null, null)
-- ,('1047', '批量强退', '109', '2', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 1, sysdate(), null, null, null, null)
-- ,('1048', '单条强退', '109', '3', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 1, sysdate(), null, null, null, null)
-- 定时任务按钮
-- ,('1049', '任务查询', '110', '1', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query',          '#', 1, sysdate(), null, null, null, null)
-- ,('1050', '任务新增', '110', '2', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add',            '#', 1, sysdate(), null, null, null, null)
-- ,('1051', '任务修改', '110', '3', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit',           '#', 1, sysdate(), null, null, null, null)
-- ,('1052', '任务删除', '110', '4', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove',         '#', 1, sysdate(), null, null, null, null)
-- ,('1053', '状态修改', '110', '5', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus',   '#', 1, sysdate(), null, null, null, null)
-- ,('1054', '任务导出', '110', '6', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export',         '#', 1, sysdate(), null, null, null, null)
-- 代码生成按钮
-- ,('1055', '生成查询', '116', '1', '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query',             '#', 1, sysdate(), null, null, null, null)
-- ,('1056', '生成修改', '116', '2', '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit',              '#', 1, sysdate(), null, null, null, null)
-- ,('1057', '生成删除', '116', '3', '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove',            '#', 1, sysdate(), null, null, null, null)
-- ,('1058', '导入代码', '116', '4', '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import',            '#', 1, sysdate(), null, null, null, null)
-- ,('1059', '预览代码', '116', '5', '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview',           '#', 1, sysdate(), null, null, null, null)
-- ,('1060', '生成代码', '116', '6', '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code',              '#', 1, sysdate(), null, null, null, null)
,(2000, '基础配置', 0, 2, 'base', NULL, NULL, 1, 0, 'M', '0', '0', '', 'system', '1', sysdate(), null, null, null, null)
,(2001, '展厅管理', 2004, 1, 'store', 'store/store/index', NULL, 1, 0, 'C', '0', '0', 'base:store:list', 'star', '1', sysdate(), null, null, null, null)
,(2002, '纯度管理', 2000, 2, 'purity', 'base/purity/index', NULL, 1, 0, 'C', '0', '0', 'base:purity:list', 'color', '1', sysdate(), null, null, null, null)
,(2003, '款式管理', 2004, 2, 'fashion', 'store/fashion/index', NULL, 1, 0, 'C', '0', '0', 'store:fashion:list', 'dict', '1', sysdate(), null, null, null, null)
,(2004, '展厅信息', 0, 3, 'store', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'documentation', '1', sysdate(), null, null, null, null)
,(2005, '会员等级', 2000, 1, 'vipLevel', 'base/vipLevel/index', NULL, 1, 0, 'C', '0', '0', 'base:vipLevel:list', 'documentation', '1', sysdate(), null, null, null, null)
,(2006, '轮播图', 2004, 3, 'banner', 'store/banner/index', NULL, 1, 0, 'C', '0', '0', 'store:banner:list', 'documentation', '1', sysdate(), null, null, null, null)
,(2007, '标签管理', 2004, 4, 'label', 'store/label/index', NULL, 1, 0, 'C', '0', '0', 'store:label:list', 'documentation', '1', sysdate(), null, null, null, null)
,(2008, '客服管理', 2004, 5, 'customer-service', 'store/customerService/index', NULL, 1, 0, 'C', '0', '0', 'store:customerService:list', 'documentation', '1', sysdate(), null, null, null, null)
,(2009, '客服维护', 0, 4, 'maintain', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'documentation', '1', sysdate(), null, null, null, null)
,(2010, '意见反馈', 2009, 1, 'feedback', 'maintain/feedback/index', NULL, 1, 0, 'C', '0', '0', 'maintain:feedback:list', 'documentation', '1', sysdate(), null, null, null, null)

;

-- ----------------------------
-- 6、用户和角色关联表  用户N-1角色
-- ----------------------------
drop table if exists sys_user_role;
create table sys_user_role (
    user_id   bigint(20) UNSIGNED not null comment '用户ID',
    role_id   bigint(20) UNSIGNED not null comment '角色ID',
    primary key(user_id, role_id)
) engine=innodb comment = '用户和角色关联表';

-- ----------------------------
-- 初始化-用户和角色关联表数据
-- ----------------------------
insert into sys_user_role values
('1', '1')
,('2', '2');


-- ----------------------------
-- 7、角色和菜单关联表  角色1-N菜单
-- ----------------------------
drop table if exists sys_role_menu;
create table sys_role_menu (
    role_id   bigint(20) UNSIGNED not null comment '角色ID',
    menu_id   bigint(20) UNSIGNED not null comment '菜单ID',
    primary key(role_id, menu_id)
) engine=innodb comment = '角色和菜单关联表';

-- ----------------------------
-- 初始化-角色和菜单关联表数据
-- ----------------------------
INSERT INTO `sys_role_menu`(`role_id`, `menu_id`) VALUES
(2, 1)
,(2, 108)
,(2, 500)
,(2, 501)
,(2, 1039)
,(2, 1040)
,(2, 1041)
,(2, 1042)
,(2, 1043)
,(2, 1044)
,(2, 1045)
;


-- ----------------------------
-- 8、角色和部门关联表  角色1-N部门
-- ----------------------------
drop table if exists sys_role_dept;
create table sys_role_dept (
    role_id   bigint(20) UNSIGNED not null comment '角色ID',
    dept_id   bigint(20) UNSIGNED not null comment '部门ID',
    primary key(role_id, dept_id)
) engine=innodb comment = '角色和部门关联表';

-- ----------------------------
-- 初始化-角色和部门关联表数据
-- ----------------------------
insert into sys_role_dept values ('2', '100');
insert into sys_role_dept values ('2', '101');
insert into sys_role_dept values ('2', '105');


-- ----------------------------
-- 9、用户与岗位关联表  用户1-N岗位
-- ----------------------------
drop table if exists sys_user_post;
create table sys_user_post
(
    user_id   bigint(20) UNSIGNED not null comment '用户ID',
    post_id   bigint(20) UNSIGNED not null comment '岗位ID',
    primary key (user_id, post_id)
) engine=innodb comment = '用户与岗位关联表';

-- ----------------------------
-- 初始化-用户与岗位关联表数据
-- ----------------------------
insert into sys_user_post values ('1', '1');
insert into sys_user_post values ('2', '2');


-- ----------------------------
-- 10、操作日志记录
-- ----------------------------
drop table if exists sys_oper_log;
create table sys_oper_log (
    oper_id           bigint(20) UNSIGNED      not null auto_increment    comment '日志主键',
    title             varchar(50)     default ''                 comment '模块标题',
    business_type     int(2)          default 0                  comment '业务类型（0其它 1新增 2修改 3删除）',
    method            varchar(100)    default ''                 comment '方法名称',
    request_method    varchar(10)     default ''                 comment '请求方式',
    operator_type     int(1)          default 0                  comment '操作类别（0其它 1后台用户 2手机端用户）',
    oper_name         varchar(50)     default ''                 comment '操作人员',
    dept_name         varchar(50)     default ''                 comment '部门名称',
    oper_url          varchar(255)    default ''                 comment '请求URL',
    oper_ip           varchar(128)    default ''                 comment '主机地址',
    oper_location     varchar(255)    default ''                 comment '操作地点',
    oper_param        varchar(2000)   default ''                 comment '请求参数',
    json_result       varchar(2000)   default ''                 comment '返回参数',
    status            int(1)          default 0                  comment '操作状态（0正常 1异常）',
    error_msg         varchar(2000)   default ''                 comment '错误消息',
    oper_time         datetime                                   comment '操作时间',
    primary key (oper_id)
) engine=innodb auto_increment=100 comment = '操作日志记录';


-- ----------------------------
-- 11、字典类型表
-- ----------------------------
drop table if exists sys_dict_type;
create table sys_dict_type
(
    dict_id          bigint(20) UNSIGNED      not null auto_increment     comment '字典主键',
    dict_name        varchar(100)    default ''                  comment '字典名称',
    dict_type        varchar(100)    default ''                  comment '字典类型',
    status           char(1)         default '0'                 comment '状态（0正常 1停用）',
    create_by         varchar(64)     default ''                 comment '创建者',
    create_at 	      datetime                                   comment '创建时间',
    update_by         varchar(64)     default ''                 comment '更新者',
    update_at         datetime                                   comment '更新时间',
    delete_by         varchar(64)     default ''                 comment '删除者',
    delete_at         datetime                                   comment '删除时间',
  primary key (dict_id),
  unique (dict_type)
) engine=innodb auto_increment=100 comment = '字典类型表';

insert into sys_dict_type values
(1,  '用户性别', 'sys_user_sex',        '0', 1, sysdate(), null, null, null, null)
,(2,  '菜单状态', 'sys_show_hide',       '0', 1, sysdate(), null, null, null, null)
,(3,  '系统开关', 'sys_normal_disable',  '0', 1, sysdate(), null, null, null, null)
,(4,  '任务状态', 'sys_job_status',      '0', 1, sysdate(), null, null, null, null)
,(5,  '任务分组', 'sys_job_group',       '0', 1, sysdate(), null, null, null, null)
,(6,  '系统是否', 'sys_yes_no',          '0', 1, sysdate(), null, null, null, null)
,(7,  '通知类型', 'sys_notice_type',     '0', 1, sysdate(), null, null, null, null)
,(8,  '通知状态', 'sys_notice_status',   '0', 1, sysdate(), null, null, null, null)
,(9,  '操作类型', 'sys_oper_type',       '0', 1, sysdate(), null, null, null, null)
,(10, '系统状态', 'sys_common_status',   '0', 1, sysdate(), null, null, null, null);


-- ----------------------------
-- 12、字典数据表
-- ----------------------------
drop table if exists sys_dict_data;
create table sys_dict_data
(
    dict_code        bigint(20) UNSIGNED      not null auto_increment    comment '字典编码',
    dict_sort        int(4)          default 0                  comment '字典排序',
    dict_label       varchar(100)    default ''                 comment '字典标签',
    dict_value       varchar(100)    default ''                 comment '字典键值',
    dict_type        varchar(100)    default ''                 comment '字典类型',
    css_class        varchar(100)    default null               comment '样式属性（其他样式扩展）',
    list_class       varchar(100)    default null               comment '表格回显样式',
    is_default       char(1)         default 'N'                comment '是否默认（Y是 N否）',
    status           char(1)         default '0'                comment '状态（0正常 1停用）',
    create_by         varchar(64)     default ''                 comment '创建者',
    create_at 	      datetime                                   comment '创建时间',
    update_by         varchar(64)     default ''                 comment '更新者',
    update_at         datetime                                   comment '更新时间',
    delete_by         varchar(64)     default ''                 comment '删除者',
    delete_at         datetime                                   comment '删除时间',
  primary key (dict_code)
) engine=innodb auto_increment=100 comment = '字典数据表';

insert into sys_dict_data values
(1,  1,  '男',       '0',       'sys_user_sex',        '',   '',        'Y', '0', 1, sysdate(), null, null, null, null)
,(2,  2,  '女',       '1',       'sys_user_sex',        '',   '',        'N', '0', 1, sysdate(), null, null, null, null)
,(3,  3,  '未知',     '2',       'sys_user_sex',        '',   '',        'N', '0', 1, sysdate(), null, null, null, null)
,(4,  1,  '显示',     '0',       'sys_show_hide',       '',   'primary', 'Y', '0', 1, sysdate(), null, null, null, null)
,(5,  2,  '隐藏',     '1',       'sys_show_hide',       '',   'danger',  'N', '0', 1, sysdate(), null, null, null, null)
,(6,  1,  '正常',     '0',       'sys_normal_disable',  '',   'primary', 'Y', '0', 1, sysdate(), null, null, null, null)
,(7,  2,  '停用',     '1',       'sys_normal_disable',  '',   'danger',  'N', '0', 1, sysdate(), null, null, null, null)
,(8,  1,  '正常',     '0',       'sys_job_status',      '',   'primary', 'Y', '0', 1, sysdate(), null, null, null, null)
,(9,  2,  '暂停',     '1',       'sys_job_status',      '',   'danger',  'N', '0', 1, sysdate(), null, null, null, null)
,(10, 1,  '默认',     'DEFAULT', 'sys_job_group',       '',   '',        'Y', '0', 1, sysdate(), null, null, null, null)
,(11, 2,  '系统',     'SYSTEM',  'sys_job_group',       '',   '',        'N', '0', 1, sysdate(), null, null, null, null)
,(12, 1,  '是',       'Y',       'sys_yes_no',          '',   'primary', 'Y', '0', 1, sysdate(), null, null, null, null)
,(13, 2,  '否',       'N',       'sys_yes_no',          '',   'danger',  'N', '0', 1, sysdate(), null, null, null, null)
,(14, 1,  '通知',     '1',       'sys_notice_type',     '',   'warning', 'Y', '0', 1, sysdate(), null, null, null, null)
,(15, 2,  '公告',     '2',       'sys_notice_type',     '',   'success', 'N', '0', 1, sysdate(), null, null, null, null)
,(16, 1,  '正常',     '0',       'sys_notice_status',   '',   'primary', 'Y', '0', 1, sysdate(), null, null, null, null)
,(17, 2,  '关闭',     '1',       'sys_notice_status',   '',   'danger',  'N', '0', 1, sysdate(), null, null, null, null)
,(18, 99, '其他',     '0',       'sys_oper_type',       '',   'info',    'N', '0', 1, sysdate(), null, null, null, null)
,(19, 1,  '新增',     '1',       'sys_oper_type',       '',   'info',    'N', '0', 1, sysdate(), null, null, null, null)
,(20, 2,  '修改',     '2',       'sys_oper_type',       '',   'info',    'N', '0', 1, sysdate(), null, null, null, null)
,(21, 3,  '删除',     '3',       'sys_oper_type',       '',   'danger',  'N', '0', 1, sysdate(), null, null, null, null)
,(22, 4,  '授权',     '4',       'sys_oper_type',       '',   'primary', 'N', '0', 1, sysdate(), null, null, null, null)
,(23, 5,  '导出',     '5',       'sys_oper_type',       '',   'warning', 'N', '0', 1, sysdate(), null, null, null, null)
,(24, 6,  '导入',     '6',       'sys_oper_type',       '',   'warning', 'N', '0', 1, sysdate(), null, null, null, null)
,(25, 7,  '强退',     '7',       'sys_oper_type',       '',   'danger',  'N', '0', 1, sysdate(), null, null, null, null)
,(26, 8,  '生成代码', '8',       'sys_oper_type',       '',   'warning', 'N', '0', 1, sysdate(), null, null, null, null)
,(27, 9,  '清空数据', '9',       'sys_oper_type',       '',   'danger',  'N', '0', 1, sysdate(), null, null, null, null)
,(28, 1,  '成功',     '0',       'sys_common_status',   '',   'primary', 'N', '0', 1, sysdate(), null, null, null, null)
,(29, 2,  '失败',     '1',       'sys_common_status',   '',   'danger',  'N', '0', 1, sysdate(), null, null, null, null)
;


-- ----------------------------
-- 13、参数配置表
-- ----------------------------
drop table if exists sys_config;
create table sys_config (
    config_id         int(5)          not null auto_increment    comment '参数主键',
    config_name       varchar(100)    default ''                 comment '参数名称',
    config_key        varchar(100)    default ''                 comment '参数键名',
    config_value      varchar(500)    default ''                 comment '参数键值',
    config_type       char(1)         default 'N'                comment '系统内置（Y是 N否）',
    create_by         varchar(64)     default ''                 comment '创建者',
    create_at 	      datetime                                   comment '创建时间',
    update_by         varchar(64)     default ''                 comment '更新者',
    update_at         datetime                                   comment '更新时间',
    delete_by         varchar(64)     default ''                 comment '删除者',
    delete_at         datetime                                   comment '删除时间',
    remark            varchar(500)    default null               comment '备注',
    primary key (config_id)
) engine=innodb auto_increment=100 comment = '参数配置表';

insert into sys_config values
(1, '主框架页-默认皮肤样式名称',     'sys.index.skinName',            'skin-blue',     'Y', 1, sysdate(), null, null, null, null, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow' )
,(2, '用户管理-账号初始密码',         'sys.user.initPassword',         '123456',        'Y', 1, sysdate(), null, null, null, null, '初始化密码 123456' )
,(3, '主框架页-侧边栏主题',           'sys.index.sideTheme',           'theme-dark',    'Y', 1, sysdate(), null, null, null, null, '深色主题theme-dark，浅色主题theme-light' )
,(4, '账号自助-验证码开关',           'sys.account.captchaEnabled',    'true',          'Y', 1, sysdate(), null, null, null, null, '是否开启验证码功能（true开启，false关闭）');


-- ----------------------------
-- 14、系统访问记录
-- ----------------------------
drop table if exists sys_logininfor;
create table sys_logininfor (
    info_id        bigint(20) UNSIGNED     not null auto_increment   comment '访问ID',
    user_name      varchar(50)    default ''                comment '用户账号',
    ipaddr         varchar(128)   default ''                comment '登录IP地址',
    login_location varchar(255)   default ''                comment '登录地点',
    browser        varchar(50)    default ''                comment '浏览器类型',
    os             varchar(50)    default ''                comment '操作系统',
    status         char(1)        default '0'               comment '登录状态（0成功 1失败）',
    msg            varchar(255)   default ''                comment '提示消息',
    login_time     datetime                                 comment '访问时间',
  primary key (info_id)
) engine=innodb auto_increment=100 comment = '系统访问记录';


-- ----------------------------
-- 17、通知公告表
-- ----------------------------
drop table if exists sys_notice;
create table sys_notice (
    notice_id         int(4)          not null auto_increment    comment '公告ID',
    notice_title      varchar(50)     not null                   comment '公告标题',
    notice_type       char(1)         not null                   comment '公告类型（1通知 2公告）',
    notice_content    longblob        default null               comment '公告内容',
    status            char(1)         default '0'                comment '公告状态（0正常 1关闭）',
    create_by         varchar(64)     default ''                 comment '创建者',
    create_at 	      datetime                                   comment '创建时间',
    update_by         varchar(64)     default ''                 comment '更新者',
    update_at         datetime                                   comment '更新时间',
    delete_by         varchar(64)     default ''                 comment '删除者',
    delete_at         datetime                                   comment '删除时间',
  primary key (notice_id)
) engine=innodb auto_increment=10 comment = '通知公告表';

-- ----------------------------
-- 初始化-公告信息表数据
-- ----------------------------
insert into sys_notice values
('1', '温馨提醒：2018-07-01 星联金新版本发布啦', '2', '新版本内容', '0', 1, sysdate(), null, null, null, null),
('2', '维护通知：2018-07-01 星联金系统凌晨维护', '1', '维护内容',   '0', 1, sysdate(), null, null, null, null);

