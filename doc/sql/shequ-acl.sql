/*
SQLyog Ultimate - MySQL GUI v8.2 
MySQL - 8.0.28 : Database - shequ-acl
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`shequ-acl` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `shequ-acl`;

/*Table structure for table `admin` */

DROP TABLE IF EXISTS `admin`;

CREATE TABLE `admin` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '会员id',
  `username` varchar(20) NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(32) NOT NULL DEFAULT '' COMMENT '密码',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机',
  `ware_id` bigint NOT NULL DEFAULT '0' COMMENT '仓库id（默认为：0为平台用户）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uname` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

/*Data for the table `admin` */

insert  into `admin`(`id`,`username`,`password`,`name`,`phone`,`ware_id`,`create_time`,`update_time`,`is_deleted`) values (1,'admin','96e79218965eb72c92a549dd5a330112','admin',NULL,0,'2021-05-31 18:08:43','2023-03-13 09:37:08',0),(2,'pingtai1','96e79218965eb72c92a549dd5a330112','张华1',NULL,0,'2021-06-01 08:46:22','2023-03-31 13:09:21',1),(3,'chengdu','96e79218965eb72c92a549dd5a330112','李二清',NULL,1,'2021-06-18 17:18:29','2023-03-31 13:09:35',1),(4,'shangguigu','dbe236c0fce6dc0a6bed67606cc87f86','张晓霞',NULL,0,'2021-09-27 09:37:39','2023-03-31 13:09:35',1),(5,'liziran','dbe236c0fce6dc0a6bed67606cc87f86','李子然',NULL,0,'2022-01-18 14:54:59','2022-01-18 14:55:02',0),(6,'huanghua','dbe236c0fce6dc0a6bed67606cc87f86','黄华',NULL,0,'2022-01-18 14:55:17','2023-03-13 09:38:20',0),(7,'licui','dbe236c0fce6dc0a6bed67606cc87f86','李翠',NULL,0,'2022-01-18 14:55:35','2023-03-31 13:07:47',1),(8,'guoqing','dbe236c0fce6dc0a6bed67606cc87f86','郭庆',NULL,0,'2022-01-18 14:55:58','2023-03-31 13:07:47',1),(10,'atguigu1','96e79218965eb72c92a549dd5a330112','testatguigu1','',0,'2023-03-31 12:55:19','2023-03-31 13:05:33',1),(11,'atguigu01','96e79218965eb72c92a549dd5a330112','atguigu',NULL,0,'2023-03-31 13:08:49','2023-03-31 13:08:49',0);

/*Table structure for table `admin_login_log` */

DROP TABLE IF EXISTS `admin_login_log`;

CREATE TABLE `admin_login_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `admin_id` bigint DEFAULT NULL,
  `ip` varchar(64) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `user_agent` varchar(100) DEFAULT NULL COMMENT '浏览器登录类型',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='后台用户登录日志表';

/*Data for the table `admin_login_log` */

/*Table structure for table `admin_role` */

DROP TABLE IF EXISTS `admin_role`;

CREATE TABLE `admin_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` bigint NOT NULL DEFAULT '0' COMMENT '角色id',
  `admin_id` bigint NOT NULL DEFAULT '0' COMMENT '用户id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_user_id` (`admin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb3 COMMENT='用户角色';

/*Data for the table `admin_role` */

insert  into `admin_role`(`id`,`role_id`,`admin_id`,`create_time`,`update_time`,`is_deleted`) values (1,1,1,'2021-05-31 18:09:02','2023-04-03 10:25:41',1),(15,6,5,'2023-04-03 11:07:13','2023-04-03 11:07:39',1),(16,3,5,'2023-04-03 11:07:13','2023-04-03 11:07:39',1),(17,3,5,'2023-04-03 11:07:39','2023-04-03 11:07:39',0),(18,2,5,'2023-04-03 11:07:39','2023-04-03 11:07:39',0),(19,1,1,'2023-04-03 11:14:05','2023-04-03 11:14:05',0);

/*Table structure for table `permission` */

DROP TABLE IF EXISTS `permission`;

CREATE TABLE `permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `pid` bigint NOT NULL DEFAULT '0' COMMENT '所属上级',
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT '名称',
  `code` varchar(50) DEFAULT NULL COMMENT '名称code',
  `to_code` varchar(100) DEFAULT NULL COMMENT '跳转页面code',
  `type` tinyint NOT NULL DEFAULT '0' COMMENT '类型(1:菜单,2:按钮)',
  `status` tinyint DEFAULT NULL COMMENT '状态(0:禁止,1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`),
  KEY `idx_pid` (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='权限';

/*Data for the table `permission` */

insert  into `permission`(`id`,`pid`,`name`,`code`,`to_code`,`type`,`status`,`create_time`,`update_time`,`is_deleted`) values (1,0,'全部数据',NULL,NULL,1,NULL,'2021-05-31 18:05:37','2021-09-27 13:37:59',0),(2,1,'权限管理','Acl',NULL,1,NULL,'2021-05-31 18:05:37','2021-05-31 19:36:53',0),(3,2,'用户管理','User',NULL,1,NULL,'2021-05-31 18:05:37','2021-05-31 19:36:58',0),(4,2,'角色管理','Role',NULL,1,NULL,'2021-05-31 18:05:37','2021-05-31 19:37:02',0),(5,2,'菜单管理','Permission',NULL,1,NULL,'2021-05-31 18:05:37','2021-05-31 19:37:05',0),(6,3,'分配角色','btn.User.assgin',NULL,2,NULL,'2021-05-31 18:05:37','2021-06-01 08:35:35',0),(7,3,'添加','btn.User.add',NULL,2,NULL,'2021-05-31 18:05:37','2021-06-01 08:34:29',0),(8,3,'修改','btn.User.update',NULL,2,NULL,'2021-05-31 18:05:37','2021-06-01 08:34:45',0),(9,3,'删除','btn.User.remove',NULL,2,NULL,'2021-05-31 18:05:37','2023-04-03 14:02:43',1),(10,4,'修改','btn.Role.update',NULL,2,NULL,'2021-05-31 18:05:37','2021-06-01 08:36:20',0),(11,4,'分配权限','btn.Role.assgin','RoleAuth',2,NULL,'2021-05-31 18:05:37','2021-06-01 08:36:56',0),(12,4,'添加','btn.Role.add',NULL,2,NULL,'2021-05-31 18:05:37','2021-06-01 08:36:08',0),(13,4,'删除','btn.Role.remove',NULL,2,NULL,'2021-05-31 18:05:37','2021-06-01 08:36:32',0),(14,4,'角色权限','role.acl',NULL,2,NULL,'2021-05-31 18:05:37','2021-06-01 08:37:22',1),(15,5,'查看','btn.permission.list',NULL,2,NULL,'2021-05-31 18:05:37','2021-05-31 19:32:52',0),(16,5,'添加','btn.Permission.add',NULL,2,NULL,'2021-05-31 18:05:37','2021-06-01 08:37:39',0),(17,5,'修改','btn.Permission.update',NULL,2,NULL,'2021-05-31 18:05:37','2021-06-01 08:37:47',0),(18,5,'删除','btn.Permission.remove',NULL,2,NULL,'2021-05-31 18:05:37','2021-06-01 08:37:54',0),(19,1,'订单管理','Order',NULL,1,NULL,'2021-06-18 16:38:51','2021-06-18 16:48:22',0),(20,19,'订单列表','OrderInfo','',1,NULL,'2021-06-18 16:39:21','2021-06-18 16:42:36',0),(21,19,'发货列表','DetailList',NULL,1,NULL,'2021-06-18 16:40:07','2021-06-18 16:40:16',0),(22,19,'订单详情','btn.OrderInfo.show','OrderInfoShow',2,NULL,'2021-06-18 16:42:30','2021-06-18 16:43:03',0),(23,1,'商品管理','Product',NULL,1,NULL,'2021-06-18 16:45:55','2021-06-18 16:48:27',0),(24,23,'商品分类','Category',NULL,1,NULL,'2021-06-18 16:46:44','2021-06-18 16:46:55',0),(25,24,'添加分类','btn.Category.add','CategoryAdd',2,NULL,'2021-06-18 16:48:01','2021-06-18 16:48:57',0),(26,24,'修改分类','btn.Category.edit','CategoryEdit',2,NULL,'2021-06-18 16:50:11','2021-06-18 16:50:11',0),(27,23,'平台属性分组','AttrGroup','',1,NULL,'2021-06-18 16:52:12','2021-06-18 16:52:12',0),(28,27,'添加','btn.AttrGroup.add','AttrGroupAdd',2,NULL,'2021-06-18 16:53:04','2021-06-18 16:54:05',0),(29,27,'修改','btn.AttrGroup.edit','AttrGroupEdit',2,NULL,'2021-06-18 16:53:22','2021-06-18 16:54:04',0),(30,27,'平台属性列表','btn.AttrGroup.list','AttrList',2,NULL,'2021-06-18 16:54:34','2021-06-18 16:54:57',0),(31,27,'属性添加',NULL,'AttrAdd',2,NULL,'2021-06-18 16:56:42','2021-06-18 16:57:09',0),(32,27,'属性修改',NULL,'AttrEdit',2,NULL,'2021-06-18 16:56:57','2021-06-18 16:57:10',0),(33,23,'SKU列表','SkuInfo',NULL,1,NULL,'2021-06-18 16:59:13','2021-06-18 16:59:13',0),(34,33,'添加',NULL,'SkuInfoAdd',2,NULL,'2021-06-18 16:59:30','2021-06-18 17:01:14',0),(35,33,'修改',NULL,'SkuInfoEdit',2,NULL,'2021-06-18 16:59:43','2021-06-18 17:01:14',0),(36,1,'营销活动管理','Activity',NULL,1,NULL,'2021-06-18 17:04:15','2021-06-18 17:04:15',0),(37,36,'活动列表','ActivityInfo',NULL,1,NULL,'2021-06-18 17:05:13','2021-06-18 17:06:22',0),(38,37,'添加','','ActivityInfoAdd',2,NULL,'2021-06-18 17:05:41','2021-06-18 17:06:13',0),(39,37,'修改',NULL,'ActivityInfoEdit',2,NULL,'2021-06-18 17:05:54','2021-06-18 17:06:20',0),(40,36,'优惠券列表','CouponInfo',NULL,1,NULL,'2021-06-18 17:06:41','2021-06-18 17:07:18',0),(41,40,'添加',NULL,'CouponInfoAdd',2,NULL,'2021-06-18 17:06:57','2021-06-18 17:07:22',0),(42,40,'修改',NULL,'CouponInfoEdit',2,NULL,'2021-06-18 17:07:11','2021-06-18 17:07:25',0),(43,40,'规则',NULL,'CouponInfoRule',2,NULL,'2021-06-18 17:07:49','2021-06-18 17:07:49',0),(44,37,'规则',NULL,'ActivityInfoRule',2,NULL,'2021-06-18 17:08:09','2021-06-18 17:08:12',0),(45,36,'秒杀活动列表','Seckill',NULL,1,NULL,'2021-06-18 17:08:44','2021-06-18 17:08:44',0),(46,45,'秒杀时间段',NULL,'SeckillTime',2,NULL,'2021-06-18 17:09:23','2021-06-18 17:15:15',0),(47,45,'秒杀时间段选择',NULL,'SelectSeckillTime',2,NULL,'2021-06-18 17:09:28','2021-06-18 17:10:03',0),(48,45,'秒杀商品列表',NULL,'SeckillSku',2,NULL,'2021-06-18 17:09:43','2021-06-18 17:10:00',0),(49,1,'团长管理','Leader',NULL,1,NULL,'2021-06-18 17:15:44','2021-06-18 17:17:24',0),(50,49,'团长待审核','LeaderCheck','',1,NULL,'2021-06-18 17:16:02','2021-06-18 17:17:25',0),(51,49,'团长已审核','leader',NULL,1,NULL,'2021-06-18 17:16:17','2021-06-18 17:17:30',0),(52,1,'系统管理','Sys',NULL,1,NULL,'2021-06-22 13:44:36','2021-06-22 13:44:39',0),(53,52,'开通区域','RegionWare',NULL,1,NULL,'2021-06-22 13:45:06','2021-06-22 13:45:06',0),(54,3,'查看','btn.User.list',NULL,2,NULL,'2021-09-27 09:42:24','2023-04-03 14:01:40',1),(55,4,'查看',' btn.Role.list','',2,NULL,'2021-09-27 09:43:49','2021-09-27 09:43:49',0),(100,1,'全部','btn.all',NULL,2,NULL,'2021-09-27 13:35:24','2022-01-18 17:47:37',1),(101,0,'1','1',NULL,2,1,'2023-03-11 16:44:02','2023-03-11 16:44:05',1);

/*Table structure for table `role` */

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(20) NOT NULL DEFAULT '' COMMENT '角色名称',
  `role_code` varchar(20) DEFAULT NULL COMMENT '角色编码',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb3 COMMENT='角色';

/*Data for the table `role` */

insert  into `role`(`id`,`role_name`,`role_code`,`remark`,`create_time`,`update_time`,`is_deleted`) values (1,'系统管理员','SYSTEM',NULL,'2021-05-31 18:09:18','2021-05-31 18:09:18',0),(2,'平台管理员',NULL,NULL,'2021-06-01 08:38:40','2021-06-18 17:13:17',0),(3,'区域仓库管理员',NULL,NULL,'2021-06-18 17:12:21','2021-06-18 17:12:21',0),(4,'产品管理员',NULL,NULL,'2021-09-27 09:37:13','2023-03-30 11:37:30',1),(5,'区域运营atguigu1',NULL,NULL,'2022-01-18 14:57:40','2023-03-30 11:37:30',1),(6,'产品录入人员',NULL,NULL,'2022-01-18 14:58:02','2022-01-18 14:58:02',0),(7,'产品审核人员',NULL,NULL,'2022-01-18 14:58:12','2023-03-30 11:32:16',1),(8,'团长管理员',NULL,NULL,'2022-01-18 14:58:30','2023-03-30 11:32:16',1),(24,'测试管理员atguigu',NULL,'testatguigu','2023-03-30 11:29:12','2023-03-30 11:30:51',1),(25,'测试atguigu',NULL,NULL,'2023-03-30 11:35:27','2023-03-30 11:36:51',1);

/*Table structure for table `role_permission` */

DROP TABLE IF EXISTS `role_permission`;

CREATE TABLE `role_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL DEFAULT '0',
  `permission_id` bigint NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB AUTO_INCREMENT=351 DEFAULT CHARSET=utf8mb3 COMMENT='角色权限';

/*Data for the table `role_permission` */

insert  into `role_permission`(`id`,`role_id`,`permission_id`,`create_time`,`update_time`,`is_deleted`) values (1,1,1,'2021-05-31 18:09:40','2023-03-13 09:39:13',1),(2,1,2,'2021-05-31 18:09:44','2023-03-13 09:39:13',1),(3,1,3,'2021-05-31 18:09:51','2023-03-13 09:39:13',1),(4,1,4,'2021-05-31 18:09:55','2023-03-13 09:39:13',1),(5,1,5,'2021-05-31 18:09:59','2023-03-13 09:39:13',1),(6,1,6,'2021-05-31 18:10:04','2023-03-13 09:39:13',1),(7,1,7,'2021-05-31 18:10:11','2023-03-13 09:39:13',1),(8,1,8,'2021-05-31 18:10:16','2023-03-13 09:39:13',1),(9,1,9,'2021-05-31 18:10:20','2023-03-13 09:39:13',1),(10,1,10,'2021-05-31 18:10:26','2023-03-13 09:39:13',1),(11,1,11,'2021-05-31 18:10:30','2023-03-13 09:39:13',1),(12,2,1,'2021-06-01 08:46:02','2021-06-18 17:14:17',1),(13,2,2,'2021-06-01 08:46:02','2021-06-18 17:14:17',1),(14,2,3,'2021-06-01 08:46:02','2021-06-18 17:14:17',1),(15,2,6,'2021-06-01 08:46:02','2021-06-18 17:14:17',1),(16,2,7,'2021-06-01 08:46:02','2021-06-18 17:14:17',1),(17,2,8,'2021-06-01 08:46:02','2021-06-18 17:14:17',1),(18,2,9,'2021-06-01 08:46:02','2021-06-18 17:14:17',1),(19,2,4,'2021-06-01 08:46:02','2021-06-18 17:14:17',1),(20,2,10,'2021-06-01 08:46:02','2021-06-18 17:14:17',1),(21,2,11,'2021-06-01 08:46:02','2021-06-18 17:14:17',1),(22,2,12,'2021-06-01 08:46:02','2021-06-18 17:14:17',1),(23,2,13,'2021-06-01 08:46:02','2021-06-18 17:14:17',1),(24,3,1,'2021-06-18 17:12:49','2021-06-18 17:14:31',1),(25,3,23,'2021-06-18 17:12:49','2021-06-18 17:14:31',1),(26,3,33,'2021-06-18 17:12:49','2021-06-18 17:14:31',1),(27,3,34,'2021-06-18 17:12:49','2021-06-18 17:14:31',1),(28,3,35,'2021-06-18 17:12:49','2021-06-18 17:14:31',1),(29,3,36,'2021-06-18 17:12:49','2021-06-18 17:14:31',1),(30,3,37,'2021-06-18 17:12:49','2021-06-18 17:14:31',1),(31,3,38,'2021-06-18 17:12:49','2021-06-18 17:14:31',1),(32,3,39,'2021-06-18 17:12:49','2021-06-18 17:14:31',1),(33,3,44,'2021-06-18 17:12:49','2021-06-18 17:14:31',1),(34,3,40,'2021-06-18 17:12:49','2021-06-18 17:14:31',1),(35,3,41,'2021-06-18 17:12:49','2021-06-18 17:14:31',1),(36,3,42,'2021-06-18 17:12:50','2021-06-18 17:14:31',1),(37,3,43,'2021-06-18 17:12:50','2021-06-18 17:14:31',1),(38,3,45,'2021-06-18 17:12:50','2021-06-18 17:14:31',1),(39,3,46,'2021-06-18 17:12:50','2021-06-18 17:14:31',1),(40,3,47,'2021-06-18 17:12:50','2021-06-18 17:14:31',1),(41,3,48,'2021-06-18 17:12:50','2021-06-18 17:14:31',1),(42,2,1,'2021-06-18 17:14:17','2021-06-18 17:17:38',1),(43,2,2,'2021-06-18 17:14:17','2021-06-18 17:17:38',1),(44,2,3,'2021-06-18 17:14:17','2021-06-18 17:17:38',1),(45,2,6,'2021-06-18 17:14:17','2021-06-18 17:17:38',1),(46,2,7,'2021-06-18 17:14:17','2021-06-18 17:17:38',1),(47,2,8,'2021-06-18 17:14:17','2021-06-18 17:17:38',1),(48,2,9,'2021-06-18 17:14:17','2021-06-18 17:17:38',1),(49,2,4,'2021-06-18 17:14:18','2021-06-18 17:17:38',1),(50,2,10,'2021-06-18 17:14:18','2021-06-18 17:17:38',1),(51,2,11,'2021-06-18 17:14:18','2021-06-18 17:17:38',1),(52,2,12,'2021-06-18 17:14:18','2021-06-18 17:17:38',1),(53,2,13,'2021-06-18 17:14:18','2021-06-18 17:17:38',1),(54,2,5,'2021-06-18 17:14:18','2021-06-18 17:17:38',1),(55,2,15,'2021-06-18 17:14:18','2021-06-18 17:17:38',1),(56,2,16,'2021-06-18 17:14:18','2021-06-18 17:17:38',1),(57,2,17,'2021-06-18 17:14:18','2021-06-18 17:17:38',1),(58,2,18,'2021-06-18 17:14:18','2021-06-18 17:17:38',1),(59,2,19,'2021-06-18 17:14:18','2021-06-18 17:17:38',1),(60,2,20,'2021-06-18 17:14:18','2021-06-18 17:17:38',1),(61,2,22,'2021-06-18 17:14:18','2021-06-18 17:17:38',1),(62,2,23,'2021-06-18 17:14:18','2021-06-18 17:17:38',1),(63,2,24,'2021-06-18 17:14:18','2021-06-18 17:17:38',1),(64,2,25,'2021-06-18 17:14:18','2021-06-18 17:17:38',1),(65,2,26,'2021-06-18 17:14:18','2021-06-18 17:17:38',1),(66,2,27,'2021-06-18 17:14:18','2021-06-18 17:17:38',1),(67,2,28,'2021-06-18 17:14:18','2021-06-18 17:17:38',1),(68,2,29,'2021-06-18 17:14:19','2021-06-18 17:17:38',1),(69,2,30,'2021-06-18 17:14:19','2021-06-18 17:17:38',1),(70,2,31,'2021-06-18 17:14:19','2021-06-18 17:17:38',1),(71,2,32,'2021-06-18 17:14:19','2021-06-18 17:17:38',1),(72,3,1,'2021-06-18 17:14:31','2021-11-12 00:56:06',1),(73,3,19,'2021-06-18 17:14:31','2021-11-12 00:56:06',1),(74,3,20,'2021-06-18 17:14:31','2021-11-12 00:56:06',1),(75,3,21,'2021-06-18 17:14:31','2021-11-12 00:56:06',1),(76,3,22,'2021-06-18 17:14:31','2021-11-12 00:56:06',1),(77,3,23,'2021-06-18 17:14:31','2021-11-12 00:56:06',1),(78,3,33,'2021-06-18 17:14:32','2021-11-12 00:56:06',1),(79,3,34,'2021-06-18 17:14:32','2021-11-12 00:56:06',1),(80,3,35,'2021-06-18 17:14:32','2021-11-12 00:56:06',1),(81,3,36,'2021-06-18 17:14:32','2021-11-12 00:56:06',1),(82,3,37,'2021-06-18 17:14:32','2021-11-12 00:56:06',1),(83,3,38,'2021-06-18 17:14:32','2021-11-12 00:56:06',1),(84,3,39,'2021-06-18 17:14:32','2021-11-12 00:56:06',1),(85,3,44,'2021-06-18 17:14:32','2021-11-12 00:56:06',1),(86,3,40,'2021-06-18 17:14:32','2021-11-12 00:56:06',1),(87,3,41,'2021-06-18 17:14:32','2021-11-12 00:56:06',1),(88,3,42,'2021-06-18 17:14:32','2021-11-12 00:56:06',1),(89,3,43,'2021-06-18 17:14:32','2021-11-12 00:56:06',1),(90,3,45,'2021-06-18 17:14:32','2021-11-12 00:56:06',1),(91,3,46,'2021-06-18 17:14:32','2021-11-12 00:56:06',1),(92,3,47,'2021-06-18 17:14:32','2021-11-12 00:56:06',1),(93,3,48,'2021-06-18 17:14:32','2021-11-12 00:56:06',1),(94,2,1,'2021-06-18 17:17:39','2021-06-18 17:22:00',1),(95,2,2,'2021-06-18 17:17:39','2021-06-18 17:22:00',1),(96,2,3,'2021-06-18 17:17:39','2021-06-18 17:22:00',1),(97,2,6,'2021-06-18 17:17:39','2021-06-18 17:22:00',1),(98,2,7,'2021-06-18 17:17:39','2021-06-18 17:22:00',1),(99,2,8,'2021-06-18 17:17:39','2021-06-18 17:22:00',1),(100,2,9,'2021-06-18 17:17:39','2021-06-18 17:22:00',1),(101,2,4,'2021-06-18 17:17:39','2021-06-18 17:22:00',1),(102,2,10,'2021-06-18 17:17:39','2021-06-18 17:22:00',1),(103,2,11,'2021-06-18 17:17:39','2021-06-18 17:22:00',1),(104,2,12,'2021-06-18 17:17:39','2021-06-18 17:22:00',1),(105,2,13,'2021-06-18 17:17:40','2021-06-18 17:22:00',1),(106,2,5,'2021-06-18 17:17:40','2021-06-18 17:22:00',1),(107,2,15,'2021-06-18 17:17:40','2021-06-18 17:22:00',1),(108,2,16,'2021-06-18 17:17:40','2021-06-18 17:22:00',1),(109,2,17,'2021-06-18 17:17:40','2021-06-18 17:22:00',1),(110,2,18,'2021-06-18 17:17:40','2021-06-18 17:22:00',1),(111,2,23,'2021-06-18 17:17:40','2021-06-18 17:22:00',1),(112,2,24,'2021-06-18 17:17:40','2021-06-18 17:22:00',1),(113,2,25,'2021-06-18 17:17:40','2021-06-18 17:22:00',1),(114,2,26,'2021-06-18 17:17:40','2021-06-18 17:22:00',1),(115,2,27,'2021-06-18 17:17:40','2021-06-18 17:22:00',1),(116,2,28,'2021-06-18 17:17:40','2021-06-18 17:22:00',1),(117,2,29,'2021-06-18 17:17:40','2021-06-18 17:22:00',1),(118,2,30,'2021-06-18 17:17:40','2021-06-18 17:22:00',1),(119,2,31,'2021-06-18 17:17:40','2021-06-18 17:22:00',1),(120,2,32,'2021-06-18 17:17:40','2021-06-18 17:22:00',1),(121,2,49,'2021-06-18 17:17:40','2021-06-18 17:22:00',1),(122,2,50,'2021-06-18 17:17:40','2021-06-18 17:22:00',1),(123,2,51,'2021-06-18 17:17:40','2021-06-18 17:22:00',1),(124,2,1,'2021-06-18 17:22:00','2021-09-27 13:40:48',1),(125,2,19,'2021-06-18 17:22:00','2021-09-27 13:40:48',1),(126,2,20,'2021-06-18 17:22:00','2021-09-27 13:40:48',1),(127,2,23,'2021-06-18 17:22:00','2021-09-27 13:40:48',1),(128,2,24,'2021-06-18 17:22:00','2021-09-27 13:40:48',1),(129,2,25,'2021-06-18 17:22:00','2021-09-27 13:40:48',1),(130,2,26,'2021-06-18 17:22:00','2021-09-27 13:40:48',1),(131,2,27,'2021-06-18 17:22:00','2021-09-27 13:40:48',1),(132,2,28,'2021-06-18 17:22:00','2021-09-27 13:40:48',1),(133,2,29,'2021-06-18 17:22:00','2021-09-27 13:40:48',1),(134,2,30,'2021-06-18 17:22:00','2021-09-27 13:40:48',1),(135,2,31,'2021-06-18 17:22:00','2021-09-27 13:40:48',1),(136,2,32,'2021-06-18 17:22:00','2021-09-27 13:40:48',1),(137,2,49,'2021-06-18 17:22:00','2021-09-27 13:40:48',1),(138,2,50,'2021-06-18 17:22:00','2021-09-27 13:40:48',1),(139,2,51,'2021-06-18 17:22:00','2021-09-27 13:40:48',1),(140,4,1,'2021-09-27 09:39:47','2021-09-27 09:44:27',1),(141,4,2,'2021-09-27 09:39:47','2021-09-27 09:44:27',1),(142,4,3,'2021-09-27 09:39:47','2021-09-27 09:44:27',1),(143,4,6,'2021-09-27 09:39:47','2021-09-27 09:44:27',1),(144,4,9,'2021-09-27 09:39:47','2021-09-27 09:44:27',1),(145,4,1,'2021-09-27 09:44:27','2021-09-27 13:45:22',1),(146,4,2,'2021-09-27 09:44:27','2021-09-27 13:45:22',1),(147,4,3,'2021-09-27 09:44:27','2021-09-27 13:45:22',1),(148,4,54,'2021-09-27 09:44:27','2021-09-27 13:45:22',1),(149,4,4,'2021-09-27 09:44:27','2021-09-27 13:45:22',1),(150,4,55,'2021-09-27 09:44:27','2021-09-27 13:45:22',1),(151,4,5,'2021-09-27 09:44:27','2021-09-27 13:45:22',1),(152,4,15,'2021-09-27 09:44:27','2021-09-27 13:45:22',1),(153,2,1,'2021-09-27 13:40:48','2022-01-18 14:46:25',1),(154,2,23,'2021-09-27 13:40:48','2022-01-18 14:46:25',1),(155,2,24,'2021-09-27 13:40:48','2022-01-18 14:46:25',1),(156,2,25,'2021-09-27 13:40:48','2022-01-18 14:46:25',1),(157,2,26,'2021-09-27 13:40:48','2022-01-18 14:46:25',1),(158,2,27,'2021-09-27 13:40:48','2022-01-18 14:46:25',1),(159,2,28,'2021-09-27 13:40:48','2022-01-18 14:46:25',1),(160,2,29,'2021-09-27 13:40:48','2022-01-18 14:46:25',1),(161,2,30,'2021-09-27 13:40:48','2022-01-18 14:46:25',1),(162,2,31,'2021-09-27 13:40:48','2022-01-18 14:46:25',1),(163,2,32,'2021-09-27 13:40:48','2022-01-18 14:46:25',1),(164,2,100,'2021-09-27 13:40:48','2022-01-18 14:46:25',1),(165,4,1,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(166,4,2,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(167,4,3,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(168,4,6,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(169,4,7,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(170,4,8,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(171,4,9,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(172,4,54,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(173,4,4,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(174,4,10,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(175,4,11,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(176,4,12,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(177,4,13,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(178,4,55,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(179,4,5,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(180,4,15,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(181,4,16,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(182,4,17,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(183,4,18,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(184,4,19,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(185,4,20,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(186,4,21,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(187,4,22,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(188,4,23,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(189,4,24,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(190,4,25,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(191,4,26,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(192,4,27,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(193,4,28,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(194,4,29,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(195,4,30,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(196,4,31,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(197,4,32,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(198,4,33,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(199,4,34,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(200,4,35,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(201,4,36,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(202,4,37,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(203,4,38,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(204,4,39,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(205,4,44,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(206,4,40,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(207,4,41,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(208,4,42,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(209,4,43,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(210,4,45,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(211,4,46,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(212,4,47,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(213,4,48,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(214,4,49,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(215,4,50,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(216,4,51,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(217,4,52,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(218,4,53,'2021-09-27 13:45:23','2021-09-27 13:45:23',0),(219,3,1,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(220,3,2,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(221,3,3,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(222,3,6,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(223,3,7,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(224,3,8,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(225,3,9,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(226,3,54,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(227,3,4,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(228,3,10,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(229,3,11,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(230,3,12,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(231,3,13,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(232,3,55,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(233,3,5,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(234,3,15,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(235,3,16,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(236,3,17,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(237,3,18,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(238,3,23,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(239,3,33,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(240,3,34,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(241,3,35,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(242,3,36,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(243,3,37,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(244,3,38,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(245,3,39,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(246,3,44,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(247,3,40,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(248,3,41,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(249,3,42,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(250,3,43,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(251,3,45,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(252,3,46,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(253,3,47,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(254,3,48,'2021-11-12 00:56:06','2021-11-12 00:56:06',0),(255,2,1,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(256,2,2,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(257,2,3,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(258,2,6,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(259,2,7,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(260,2,8,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(261,2,9,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(262,2,54,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(263,2,4,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(264,2,10,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(265,2,11,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(266,2,12,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(267,2,13,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(268,2,55,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(269,2,5,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(270,2,15,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(271,2,16,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(272,2,17,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(273,2,18,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(274,2,23,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(275,2,24,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(276,2,25,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(277,2,26,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(278,2,27,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(279,2,28,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(280,2,29,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(281,2,30,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(282,2,31,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(283,2,32,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(284,2,52,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(285,2,53,'2022-01-18 14:46:25','2022-01-18 14:46:58',1),(286,2,1,'2022-01-18 14:46:58','2022-01-18 14:46:58',0),(287,2,23,'2022-01-18 14:46:58','2022-01-18 14:46:58',0),(288,2,24,'2022-01-18 14:46:58','2022-01-18 14:46:58',0),(289,2,25,'2022-01-18 14:46:58','2022-01-18 14:46:58',0),(290,2,26,'2022-01-18 14:46:58','2022-01-18 14:46:58',0),(291,2,27,'2022-01-18 14:46:58','2022-01-18 14:46:58',0),(292,2,28,'2022-01-18 14:46:58','2022-01-18 14:46:58',0),(293,2,29,'2022-01-18 14:46:58','2022-01-18 14:46:58',0),(294,2,30,'2022-01-18 14:46:58','2022-01-18 14:46:58',0),(295,2,31,'2022-01-18 14:46:58','2022-01-18 14:46:58',0),(296,2,32,'2022-01-18 14:46:58','2022-01-18 14:46:58',0),(297,1,1,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(298,1,2,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(299,1,3,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(300,1,6,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(301,1,7,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(302,1,8,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(303,1,9,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(304,1,54,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(305,1,4,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(306,1,10,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(307,1,11,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(308,1,12,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(309,1,13,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(310,1,55,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(311,1,5,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(312,1,15,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(313,1,16,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(314,1,17,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(315,1,18,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(316,1,19,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(317,1,20,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(318,1,21,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(319,1,22,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(320,1,23,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(321,1,24,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(322,1,25,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(323,1,26,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(324,1,27,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(325,1,28,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(326,1,29,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(327,1,30,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(328,1,31,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(329,1,32,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(330,1,33,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(331,1,34,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(332,1,35,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(333,1,36,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(334,1,37,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(335,1,38,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(336,1,39,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(337,1,44,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(338,1,40,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(339,1,41,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(340,1,42,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(341,1,43,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(342,1,45,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(343,1,46,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(344,1,47,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(345,1,48,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(346,1,49,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(347,1,50,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(348,1,51,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(349,1,52,'2023-03-13 09:39:13','2023-03-13 09:39:13',0),(350,1,53,'2023-03-13 09:39:13','2023-03-13 09:39:13',0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
