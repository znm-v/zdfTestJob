-- 清理并重新初始化数据库

USE sweetbite;

-- 删除所有表
DROP TABLE IF EXISTS `invite_code`;
DROP TABLE IF EXISTS `points_log`;
DROP TABLE IF EXISTS `order_item`;
DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS `gift`;
DROP TABLE IF EXISTS `food`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `couple_account`;

-- 重新创建表
-- 1. 情侣账户表
CREATE TABLE `couple_account` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `couple_name` VARCHAR(100) NOT NULL COMMENT '情侣昵称（如：Felix & Marry）',
  `avatar_url` VARCHAR(500) COMMENT '头像URL',
  `together_days` INT DEFAULT 0 COMMENT '在一起天数',
  `together_date` DATE COMMENT '在一起的日期',
  `total_points` INT DEFAULT 0 COMMENT '总积分',
  `used_points` INT DEFAULT 0 COMMENT '已使用积分',
  `available_points` INT DEFAULT 0 COMMENT '可用积分',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='情侣账户表';

-- 2. 用户表
CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `couple_account_id` BIGINT NOT NULL COMMENT '情侣账户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码（加密）',
  `nickname` VARCHAR(50) COMMENT '昵称',
  `phone` VARCHAR(20) COMMENT '手机号',
  `email` VARCHAR(100) COMMENT '邮箱',
  `avatar` VARCHAR(500) COMMENT '头像',
  `gender` TINYINT COMMENT '性别：0-女，1-男',
  `role` VARCHAR(20) DEFAULT 'USER' COMMENT '角色：USER-用户，ADMIN-管理员',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
  `last_login_time` DATETIME COMMENT '最后登录时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_couple_account_id` (`couple_account_id`),
  KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 3. 食物表
CREATE TABLE `food` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` VARCHAR(100) NOT NULL COMMENT '食物名称',
  `description` VARCHAR(500) COMMENT '描述',
  `image_url` VARCHAR(500) COMMENT '图片URL',
  `category` VARCHAR(50) NOT NULL COMMENT '分类：热门、主食、饮品、甜品',
  `points` INT NOT NULL COMMENT '所需积分',
  `stock` INT DEFAULT -1 COMMENT '库存：-1表示无限',
  `sales` INT DEFAULT 0 COMMENT '销量',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-下架，1-上架',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category`),
  KEY `idx_status` (`status`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='食物表';

-- 4. 礼品表
CREATE TABLE `gift` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` VARCHAR(100) NOT NULL COMMENT '礼品名称',
  `description` VARCHAR(500) COMMENT '描述',
  `image_url` VARCHAR(500) COMMENT '图片URL',
  `points` INT NOT NULL COMMENT '所需积分',
  `stock` INT DEFAULT -1 COMMENT '库存：-1表示无限',
  `sales` INT DEFAULT 0 COMMENT '兑换次数',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-下架，1-上架',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='礼品表';

-- 5. 订单表
CREATE TABLE `order` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_no` VARCHAR(50) NOT NULL COMMENT '订单号',
  `couple_account_id` BIGINT NOT NULL COMMENT '情侣账户ID',
  `user_id` BIGINT NOT NULL COMMENT '下单用户ID',
  `order_type` TINYINT NOT NULL COMMENT '订单类型：1-点餐，2-兑换',
  `total_points` INT NOT NULL COMMENT '总积分',
  `item_count` INT NOT NULL COMMENT '商品数量',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-已取消，1-待支付，2-已支付，3-已完成',
  `pay_time` DATETIME COMMENT '支付时间',
  `complete_time` DATETIME COMMENT '完成时间',
  `cancel_time` DATETIME COMMENT '取消时间',
  `remark` VARCHAR(500) COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_couple_account_id` (`couple_account_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- 6. 订单项目表
CREATE TABLE `order_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` BIGINT NOT NULL COMMENT '订单ID',
  `item_type` TINYINT NOT NULL COMMENT '项目类型：1-食物，2-礼品',
  `item_id` BIGINT NOT NULL COMMENT '项目ID',
  `quantity` INT NOT NULL COMMENT '数量',
  `points` INT NOT NULL COMMENT '单价积分',
  `total_points` INT NOT NULL COMMENT '总积分',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单项目表';

-- 7. 积分流水表
CREATE TABLE `points_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `couple_account_id` BIGINT NOT NULL COMMENT '情侣账户ID',
  `change_type` TINYINT NOT NULL COMMENT '变动类型：1-系统发放，2-消费扣除，3-退款返还，4-管理员调整',
  `change_points` INT NOT NULL COMMENT '变动积分',
  `before_points` INT NOT NULL COMMENT '变动前积分',
  `after_points` INT NOT NULL COMMENT '变动后积分',
  `remark` VARCHAR(500) COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_couple_account_id` (`couple_account_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='积分流水表';

-- 8. 邀请码表
CREATE TABLE `invite_code` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `couple_account_id` BIGINT NOT NULL COMMENT '情侣账户ID',
  `code` VARCHAR(50) NOT NULL COMMENT '邀请码',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0-未使用，1-已使用',
  `used_by_user_id` BIGINT COMMENT '使用者用户ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_couple_account_id` (`couple_account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='邀请码表';

-- 插入测试数据
-- 1. 插入情侣账户
INSERT INTO `couple_account` (`couple_name`, `together_date`, `total_points`, `used_points`, `available_points`, `status`)
VALUES ('Felix & Marry', '2024-01-01', 1000, 0, 1000, 1);

-- 2. 插入测试用户
-- 用户名: felix, 密码: 123456
-- BCrypt加密: $2a$10$slYQmyNdGzin7olVN3/p2OPST9/PgBkqquzi.Ss7KIUgO2t0jKMUm
INSERT INTO `user` (`couple_account_id`, `username`, `password`, `nickname`, `phone`, `role`, `status`)
VALUES (1, 'felix', '$2a$10$slYQmyNdGzin7olVN3/p2OPST9/PgBkqquzi.Ss7KIUgO2t0jKMUm', 'Felix', '13800138000', 'ADMIN', 1);

-- 3. 插入第二个测试用户
-- 用户名: marry, 密码: 123456
INSERT INTO `user` (`couple_account_id`, `username`, `password`, `nickname`, `phone`, `role`, `status`)
VALUES (1, 'marry', '$2a$10$slYQmyNdGzin7olVN3/p2OPST9/PgBkqquzi.Ss7KIUgO2t0jKMUm', 'Marry', '13800138001', 'USER', 1);

-- 4. 插入测试食物
INSERT INTO `food` (`name`, `description`, `image_url`, `category`, `points`, `stock`, `status`, `sort_order`)
VALUES 
('红烧肉', '美味的红烧肉', '', '热门', 50, -1, 1, 1),
('宫保鸡丁', '经典川菜', '', '热门', 40, -1, 1, 2),
('番茄鸡蛋面', '清汤面条', '', '主食', 30, -1, 1, 3),
('奶茶', '香浓奶茶', '', '饮品', 20, -1, 1, 4),
('提拉米苏', '意大利甜品', '', '甜品', 35, -1, 1, 5);

-- 5. 插入测试礼品
INSERT INTO `gift` (`name`, `description`, `image_url`, `points`, `stock`, `status`, `sort_order`)
VALUES 
('玫瑰花束', '99朵红玫瑰', '', 500, -1, 1, 1),
('情侣手链', '成对的手链', '', 300, -1, 1, 2),
('电影票', '两张电影票', '', 200, -1, 1, 3),
('香水', '高级香水', '', 400, -1, 1, 4);
