CREATE DATABASE  IF NOT EXISTS `wedding` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;
USE `wedding`;
-- MySQL dump 10.13  Distrib 5.6.23, for Win64 (x86_64)
--
-- Host: localhost    Database: wedding
-- ------------------------------------------------------
-- Server version	5.1.73-community


DROP TABLE IF EXISTS `user_uum_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_uum_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `userid` bigint(20) NOT NULL COMMENT 'user/enduser表id，可以扩展包括前后台的用户权限控制',
  `roleid` bigint(20) NOT NULL COMMENT 'user_role表的roleid',
  `applytime` datetime DEFAULT NULL COMMENT ' 生成时间',
  `flag` int(20) DEFAULT '0' COMMENT '0正常 1失效',
  PRIMARY KEY (`id`),
  KEY `roleid_user_role_roleid` (`roleid`),
  CONSTRAINT `roleid_user_role_roleid` FOREIGN KEY (`roleid`) REFERENCES `user_role` (`roleid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;


