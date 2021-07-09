SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for activity
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity`  (
  `activity_id` bigint NOT NULL,
  `preview_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '展示图地址',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动内容',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `expired_time` datetime NULL DEFAULT NULL COMMENT '过期时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '1-true',
  `activity_title` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动标题',
  `activity_page` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动页面地址',
  `deploy_uid` bigint NULL DEFAULT NULL COMMENT '发布者id',
  `activity_status` int NULL DEFAULT 0 COMMENT '0-未发布 1-已发布 2-已失效',
  `pv` bigint NULL DEFAULT NULL COMMENT '访问量',
  `deploy_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`activity_id`) USING BTREE,
  INDEX `index_depoly_uid`(`deploy_uid`) USING BTREE,
  INDEX `index_create_time`(`create_time`) USING BTREE,
  INDEX `index_expired_time`(`expired_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address`  (
  `address_id` bigint NOT NULL,
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `consignee` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货人姓名',
  `consignee_phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货人电话',
  `province` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市',
  `country` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '县区',
  `street` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `is_default` tinyint(1) NULL DEFAULT NULL COMMENT '默认地址 1-true',
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`address_id`) USING BTREE,
  INDEX `fk_address_user_info_1`(`user_id`) USING BTREE,
  CONSTRAINT `fk_address_user_info_1` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `category_id` bigint NOT NULL,
  `category_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `category_desc` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类描述',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`category_id`) USING BTREE,
  INDEX `index_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for collection
-- ----------------------------
DROP TABLE IF EXISTS `collection`;
CREATE TABLE `collection`  (
  `coll_id` bigint NOT NULL COMMENT '收藏id',
  `category_id` bigint NULL DEFAULT NULL COMMENT '分类id',
  `product_id` bigint NULL DEFAULT NULL COMMENT '商品id',
  `shop_id` bigint NULL DEFAULT NULL COMMENT '商家id',
  `user_id` bigint NULL DEFAULT NULL COMMENT '收藏用户id',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  `coll_status` int NULL DEFAULT 0 COMMENT '状态 0-正常 1-失效',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除 1-true',
  PRIMARY KEY (`coll_id`) USING BTREE,
  INDEX `fk_collection_category_1`(`category_id`) USING BTREE,
  INDEX `index_user_id`(`user_id`) USING BTREE,
  INDEX `index_shop_id`(`shop_id`) USING BTREE,
  INDEX `index_product_id`(`product_id`) USING BTREE,
  INDEX `index_create_time`(`create_time`) USING BTREE,
  CONSTRAINT `fk_collection_user_info_1` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for coupon
-- ----------------------------
DROP TABLE IF EXISTS `coupon`;
CREATE TABLE `coupon`  (
  `coupon_id` bigint NOT NULL,
  `activity_id` bigint NULL DEFAULT NULL,
  `coupon_type` int NULL DEFAULT NULL COMMENT '折扣类型 0-满减 1-百分比',
  `coupon_step` int NULL DEFAULT NULL COMMENT '折扣梯度 满足则应用一次规则',
  `coupon_value` int NULL DEFAULT NULL COMMENT '应用一次的折扣值，折扣类型为 1 则表示百分比',
  `apply_multi` tinyint(1) NULL DEFAULT NULL COMMENT '是否按照梯度多次应用 1-true',
  `count` int NULL DEFAULT NULL COMMENT '优惠券数量 -1为无限制',
  `user_limit` int NULL DEFAULT NULL COMMENT '每人领取数量',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '1-true',
  PRIMARY KEY (`coupon_id`) USING BTREE,
  INDEX `fk_discount_rule_activity_1`(`activity_id`) USING BTREE,
  CONSTRAINT `fk_discount_rule_activity_1` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`activity_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `notice_id` bigint NOT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容',
  `notice_title` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `deploy_uid` bigint NULL DEFAULT NULL COMMENT '发送通知的用户id',
  `user_id` bigint NULL DEFAULT NULL COMMENT '接受通知的用户id',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '1-true',
  `is_read` tinyint(1) NULL DEFAULT 0 COMMENT '已读 1-true',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `read_time` datetime NULL DEFAULT NULL COMMENT '已读时间',
  PRIMARY KEY (`notice_id`) USING BTREE,
  INDEX `fk_notice_user_info_1`(`user_id`) USING BTREE,
  INDEX `index_create_time`(`create_time`) USING BTREE,
  CONSTRAINT `fk_notice_user_info_1` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `orders_id` bigint NOT NULL COMMENT '订单编号',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `shop_id` bigint NULL DEFAULT NULL COMMENT '卖家商店id',
  `total_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '订单总价格',
  `discount_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠减免价格',
  `orders_status` int NULL DEFAULT 0 COMMENT '状态 0-未支付 1-待发货 2-待收货 3-待退货 4-已退货 5-已完成 6-失效',
  `pay_id` bigint NULL DEFAULT NULL COMMENT '支付单号',
  `pay_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付类型 银行卡 微信 支付宝',
  `express_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '快递单号',
  `express_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '快递公司名',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '1-true',
  `express_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发货地址',
  `express_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '运费价格',
  `address_id` bigint NULL DEFAULT NULL COMMENT '用户收货地址id',
  `coupon_id` bigint NULL DEFAULT NULL COMMENT '应用优惠券',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `finish_time` datetime NULL DEFAULT NULL COMMENT '收货时间',
  `express_time` datetime NULL DEFAULT NULL COMMENT '发货时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `refund_time` datetime NULL DEFAULT NULL COMMENT '申请退款时间',
  PRIMARY KEY (`orders_id`) USING BTREE,
  INDEX `fk_orders_user_info_1`(`user_id`) USING BTREE,
  INDEX `fk_orders_address_1`(`address_id`) USING BTREE,
  INDEX `index_express_id`(`express_id`) USING BTREE,
  INDEX `index_create_time`(`create_time`) USING BTREE,
  INDEX `index_pay_id`(`pay_id`) USING BTREE,
  INDEX `index_shop_id`(`shop_id`) USING BTREE,
  CONSTRAINT `fk_orders_address_1` FOREIGN KEY (`address_id`) REFERENCES `address` (`address_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_orders_user_info_1` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for orders_detail
-- ----------------------------
DROP TABLE IF EXISTS `orders_detail`;
CREATE TABLE `orders_detail`  (
  `detail_id` bigint NOT NULL,
  `orders_id` bigint NULL DEFAULT NULL COMMENT '订单编号',
  `product_id` bigint NULL DEFAULT NULL COMMENT '商品编号',
  `product_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品型号',
  `product_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '冗余-商品名',
  `product_size` int NULL DEFAULT NULL COMMENT '商品数量',
  `activity_id` bigint NULL DEFAULT NULL COMMENT '参与活动id',
  `product_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品价格',
  `category_id` bigint NULL DEFAULT NULL COMMENT '分类id',
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`detail_id`) USING BTREE,
  INDEX `fk_detail_orders_orders_1`(`orders_id`) USING BTREE,
  INDEX `fk_detail_orders_product_1`(`product_id`) USING BTREE,
  INDEX `index_category_id`(`category_id`) USING BTREE,
  CONSTRAINT `fk_detail_orders_orders_1` FOREIGN KEY (`orders_id`) REFERENCES `orders` (`orders_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_detail_orders_product_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `perm_id` bigint NOT NULL AUTO_INCREMENT,
  `perm_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限名称',
  `perm_desc` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限描述',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`perm_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 66 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `product_id` bigint NOT NULL,
  `product_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名',
  `product_desc` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品描述',
  `product_pic` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品图册地址，逗号分隔，最多五个',
  `preview_img` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预览图地址',
  `stock` int NULL DEFAULT NULL COMMENT '库存量',
  `category_id` bigint NULL DEFAULT NULL COMMENT '分类id',
  `shop_id` bigint NULL DEFAULT NULL COMMENT '所属商店id',
  `product_status` int NULL DEFAULT 1 COMMENT '状态 0-正常 1-未发布 2-失效',
  `original_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '原定价',
  `sale_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '销售价',
  `product_types` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品型号，逗号分割',
  `product_tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品标签，逗号分割，用于搜索最多10个',
  `product_company` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品生产公司名',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除 0-false',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `deploy_time` datetime NULL DEFAULT NULL COMMENT '上架时间',
  `pv` bigint NOT NULL DEFAULT 0,
  `intro_page` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`product_id`) USING BTREE,
  INDEX `fk_product_category_1`(`category_id`) USING BTREE,
  INDEX `fk_product_shop_1`(`shop_id`) USING BTREE,
  INDEX `index_create_time`(`create_time`) USING BTREE,
  INDEX `index_saleprice`(`sale_price`) USING BTREE COMMENT 'orderby',
  CONSTRAINT `fk_product_category_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_product_shop_1` FOREIGN KEY (`shop_id`) REFERENCES `shop` (`shop_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT,
  `role_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名',
  `role_desc` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for role_perm
-- ----------------------------
DROP TABLE IF EXISTS `role_perm`;
CREATE TABLE `role_perm`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL,
  `perm_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `roleperm_unique`(`role_id`, `perm_id`) USING BTREE,
  INDEX `fk_role_perm_role_1`(`role_id`) USING BTREE,
  INDEX `fk_role_perm_permission_1`(`perm_id`) USING BTREE,
  CONSTRAINT `fk_role_perm_permission_1` FOREIGN KEY (`perm_id`) REFERENCES `permission` (`perm_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_role_perm_role_1` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for shop
-- ----------------------------
DROP TABLE IF EXISTS `shop`;
CREATE TABLE `shop`  (
  `shop_id` bigint NOT NULL,
  `shop_icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `shop_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_id` bigint NULL DEFAULT NULL,
  `activity_id` bigint NULL DEFAULT NULL,
  `category_id` bigint NULL DEFAULT NULL COMMENT '店铺分类',
  `shop_desc` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '店铺描述',
  `alipay_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝商户ID',
  `wepay_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信支付商户ID',
  PRIMARY KEY (`shop_id`) USING BTREE,
  INDEX `fk_shop_user_info_1`(`user_id`) USING BTREE,
  INDEX `fk_shop_activity_1`(`activity_id`) USING BTREE,
  INDEX `fk_shop_category_1`(`category_id`) USING BTREE,
  CONSTRAINT `fk_shop_activity_1` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`activity_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_shop_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_shop_user_info_1` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for shop_car
-- ----------------------------
DROP TABLE IF EXISTS `shop_car`;
CREATE TABLE `shop_car`  (
  `car_id` bigint NOT NULL COMMENT '购物车id',
  `user_id` bigint NULL DEFAULT NULL COMMENT '买家id',
  `product_id` bigint NULL DEFAULT NULL COMMENT '商品id',
  `shop_id` bigint NULL DEFAULT NULL COMMENT '卖家id',
  `product_img` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '购物车预览图地址',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
  `valid` tinyint(1) NULL DEFAULT 0 COMMENT '失效 1-true',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除 1-true',
  `product_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '加入购物车时的价格',
  `valid_time` datetime NULL DEFAULT NULL COMMENT '失效时间',
  `product_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品型号',
  `product_size` int NULL DEFAULT NULL COMMENT '购买数量',
  `category_id` bigint NULL DEFAULT NULL COMMENT '分类id',
  `product_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  PRIMARY KEY (`car_id`) USING BTREE,
  INDEX `fk_shop_car_user_info_1`(`user_id`) USING BTREE,
  INDEX `fk_shop_car_product_1`(`product_id`) USING BTREE,
  INDEX `fk_shop_car_category_1`(`category_id`) USING BTREE,
  INDEX `index_create_time`(`create_time`) USING BTREE,
  INDEX `index_shop_id`(`shop_id`) USING BTREE,
  CONSTRAINT `fk_shop_car_category_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_shop_car_product_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_shop_car_user_info_1` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_coupon
-- ----------------------------
DROP TABLE IF EXISTS `user_coupon`;
CREATE TABLE `user_coupon`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NULL DEFAULT NULL,
  `coupon_id` bigint NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `count` int NULL DEFAULT 0 COMMENT '持有量',
  `max_count` int NULL DEFAULT 0 COMMENT '最大持有量',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `fk_user_coupon_coupon_1`(`coupon_id`, `user_id`) USING BTREE,
  INDEX `fk_user_coupon_user_info_1`(`user_id`) USING BTREE,
  CONSTRAINT `fk_user_coupon_coupon_1` FOREIGN KEY (`coupon_id`) REFERENCES `coupon` (`coupon_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_user_coupon_user_info_1` FOREIGN KEY (`user_id`) REFERENCES `user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `user_id` bigint NOT NULL,
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '唯一用户名',
  `phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '唯一电话',
  `email` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码 md5hash',
  `salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'md5hash salt',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像地址',
  `role_id` bigint NULL DEFAULT NULL COMMENT '用户角色',
  `user_status` int NULL DEFAULT 0 COMMENT '0-正常 1-封号',
  `user_desc` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '简介',
  `nick_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `fk_user_info_role_1`(`role_id`) USING BTREE,
  CONSTRAINT `fk_user_info_role_1` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Triggers structure for table orders_detail
-- ----------------------------
DROP TRIGGER IF EXISTS `add_product_pv`;
delimiter ;;
CREATE TRIGGER `add_product_pv` AFTER INSERT ON `orders_detail` FOR EACH ROW begin
		set @pid=new.product_id;
    set @size=new.product_size;
    update product set pv = pv+@size where product_id=@pid;
end
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
