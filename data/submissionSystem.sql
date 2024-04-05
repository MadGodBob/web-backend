/*
Navicat MySQL Data Transfer

Source Server         : Mysql
Source Server Version : 50735
Source Host           : localhost:3306
Source Database       : submissionSystem

Target Server Type    : MYSQL
Target Server Version : 50735
File Encoding         : 65001

Date: 2024-03-10 9:18:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Initial database submissionSystem
-- ----------------------------
drop database if exists submissionSystem;
create database submissionSystem;
use submissionSystem;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_code` varchar(40) NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `sex` tinyint(1) DEFAULT '1',
  `enabled` tinyint(1) DEFAULT '1',
  `password` varchar(41) DEFAULT NULL,
  `department` varchar(128) DEFAULT NULL,
  `phone` varchar(32) DEFAULT NULL,
  `email` varchar(128) DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL COMMENT '创建人',
  `updated_by` int(11) DEFAULT NULL COMMENT '更新人',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk` (`user_code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('1', 'root', '管理员', '1', '1', '*81F5E21E35407D884A6CD4A731AEBFB6AF209E1B', null, null, null, '1', '1', '2021-01-01 08:00:00', '2023-01-11 11:41:40');
INSERT INTO `admin` VALUES ('2', 'test', '测试账户', '0', '1', '*94BDCEBE19083CE2A1F959FD02F964C7AF4CFC29', null, null, null, '1', '1', '2023-01-10 22:14:16', '2023-01-11 13:00:57');

-- ----------------------------
-- Table structure for admin_priv
-- ----------------------------
DROP TABLE IF EXISTS `admin_priv`;
CREATE TABLE `admin_priv` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `admin_id` varchar(40) NOT NULL,
  `mod_id` varchar(64) NOT NULL,
  `priv` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `iUnique` (`admin_id`,`mod_id`,`priv`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin_priv
-- ----------------------------
INSERT INTO `admin_priv` VALUES ('14', '2', 'admin', 'page');
INSERT INTO `admin_priv` VALUES ('2', '3', 'admin', 'add');
INSERT INTO `admin_priv` VALUES ('3', '3', 'admin', 'delete');

-- ----------------------------
-- Table structure for students
-- ----------------------------
DROP TABLE IF EXISTS `students`;
CREATE TABLE `students` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL COMMENT '名字',
  `studentID` int(12) DEFAULT NULL UNIQUE COMMENT '学号',
  `class_ID` varchar(50) DEFAULT NULL COMMENT '班级id',
  `class_name` varchar(50) DEFAULT NULL COMMENT '班级名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of students
-- ----------------------------
INSERT INTO `students` VALUES ('1', '张三', '202200001', '0101', '1班');
INSERT INTO `students` VALUES ('2', '李四', '202200002', '0101', '1班');

-- ----------------------------
-- Table structure for classes
-- ----------------------------
DROP TABLE IF EXISTS `classes`;
CREATE TABLE `classes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `class_id` varchar(50) NOT NULL UNIQUE COMMENT '班级id',
  `class_name` varchar(50) DEFAULT NULL COMMENT '班级名称',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `updated_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of classes
-- ----------------------------
INSERT INTO `classes` VALUES ('1', '0101', '1班', '重点班', '2019-12-03 17:31:28', '2023-03-03 10:36:23', '1', '1');
INSERT INTO `classes` VALUES ('2', '0102', '2班', '重点班', '2019-12-03 17:48:06', '2023-03-03 10:36:29', '1', '1');

-- ----------------------------
-- Table structure for class_task
-- ----------------------------
DROP TABLE IF EXISTS `class_task`;
CREATE TABLE `class_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `class_id` varchar(50) NOT NULL COMMENT '班级id',
  `class_name` varchar(50) DEFAULT NULL COMMENT '班级名称',
  `task_id` varchar(50) DEFAULT NULL UNIQUE COMMENT '作业任务id',
  `task_description` varchar(255) DEFAULT NULL COMMENT '作业描述',
  `task_state` varchar(50) DEFAULT "OFF" COMMENT '状态',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of class_task
-- ----------------------------
INSERT INTO `class_task` VALUES ('1', '0101', '1班', '010101', '试使用微积分推导球体积公式', 'OFF', '2022-12-03 17:31:28');
INSERT INTO `class_task` VALUES ('2', '0102', '2班', '010201', null, 'OFF', '2022-12-03 17:48:06');

-- ----------------------------
-- Table structure for submission
-- ----------------------------
DROP TABLE IF EXISTS `submission`;
CREATE TABLE `submission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `studentID` int(12) DEFAULT NULL UNIQUE COMMENT '学号',
  `class_ID` varchar(50) NOT NULL COMMENT '班级id',
  `class_name` varchar(50) DEFAULT NULL COMMENT '班级名称',
  `task_id` varchar(50) DEFAULT NULL COMMENT '作业任务id',
  `answer` varchar(255) DEFAULT NULL COMMENT '答案',
  `img` mediumblob DEFAULT NULL COMMENT '图片',
  `submit_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of submission
-- ----------------------------
INSERT INTO `submission` VALUES ('1', '张三', '202200001', '1', '1班', '0101', '不会', null, '2023-12-03 17:31:28');
INSERT INTO `submission` VALUES ('2', '李四', '202200002', '1', '1班', '0101', '易证', null, '2023-12-03 17:48:06');





-- ----------------------------
-- Table structure for login_log
-- ----------------------------
DROP TABLE IF EXISTS `login_log`;
CREATE TABLE `login_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_code` varchar(64) NOT NULL,
  `ip_address` varchar(46) NOT NULL,
  `name` varchar(64) DEFAULT NULL,
  `os` varchar(128) DEFAULT NULL,
  `browser` varchar(128) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of login_log
-- ----------------------------
INSERT INTO `login_log` VALUES ('1', 'root', '127.0.0.1', '管理员', 'Windows 11', 'Chrome 10 108.0.0.0', '2023-01-11 15:45:16');
INSERT INTO `login_log` VALUES ('2', 'root', '127.0.0.1', '管理员', 'Windows 11', 'Chrome 10 V108.0.0.0', '2023-01-11 15:45:53');