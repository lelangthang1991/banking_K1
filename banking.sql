-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: banking
-- ------------------------------------------------------
-- Server version	8.0.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `card`
--

DROP TABLE IF EXISTS `card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `card` (
  `card_number` varchar(20) NOT NULL,
  `balance` double(14,2) DEFAULT NULL,
  `pin_code` varchar(6) DEFAULT NULL,
  `card_type` int DEFAULT NULL,
  `is_activated` tinyint(1) DEFAULT NULL,
  `create_person` varchar(300) DEFAULT NULL,
  `update_person` varchar(300) DEFAULT NULL,
  `update_date` timestamp NULL DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT NULL,
  `level` int DEFAULT NULL,
  `daily_limit_amount` double(14,2) DEFAULT NULL,
  `monthly_limit_amount` double(14,2) DEFAULT NULL,
  `monthly_available_transfer` varchar(15) DEFAULT NULL,
  `daily_available_transfer` varchar(15) DEFAULT NULL,
  `user_email` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`card_number`),
  KEY `fk_account_user` (`user_email`),
  CONSTRAINT `fk_account_user` FOREIGN KEY (`user_email`) REFERENCES `user` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `card`
--

LOCK TABLES `card` WRITE;
/*!40000 ALTER TABLE `card` DISABLE KEYS */;
INSERT INTO `card` VALUES ('1001207874495501',100000999.90,'1234',1,1,'hoanganh2.dev@gmail.com','hoanganh2.dev@gmail.com','2022-06-13 10:02:42','2022-06-10 03:17:40',1,100000000.00,1000000000.00,'998992999.9','100000000','hoanganh2.dev@gmail.com'),('1001214692663827',973544.00,'1234',1,1,'hoanganh2.dev@gmail.com','hoanganh25022000@gmail.com','2022-06-13 02:09:10','2022-06-10 03:17:43',1,100000000.00,1000000000.00,'990874544','100000000','hoanganh2.dev@gmail.com'),('1001416877292373',31798000.00,'1234',1,1,'lehongcong2k@gmail.com','hoanganh25022000@gmail.com','2022-06-10 06:15:05','2022-06-10 03:39:28',1,100000000.00,1000000000.00,'1000000000','100000000','lehongcong2k@gmail.com'),('1001485064420644',-21153000.00,'1234',1,1,'lehongcong2k@gmail.com','lehongcong2k@gmail.com','2022-06-13 02:26:25','2022-06-10 03:46:36',1,100000000.00,1000000000.00,'973697000','100000000','lehongcong2k@gmail.com'),('1001549641636202',10915344.10,'1234',1,1,'hoanganh2.dev@gmail.com','hoanganh2.dev@gmail.com','2022-06-10 07:29:30','2022-06-10 07:29:30',1,100000000.00,1000000000.00,'1000000000','100000000','hoanganh2.dev@gmail.com'),('1001672512403593',0.00,'1234',1,1,'hoanganh25022000@gmail.com','hoanganh25022000@gmail.com','2022-06-13 03:02:36','2022-06-13 03:02:36',1,100000000.00,1000000000.00,'1000000000','100000000','hoanganh25022000@gmail.com');
/*!40000 ALTER TABLE `card` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `uuid` varchar(300) DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT NULL,
  `status_code` varchar(50) DEFAULT NULL,
  `status_description` varchar(300) DEFAULT NULL,
  `card_number` varchar(20) DEFAULT NULL,
  `transaction_type` int DEFAULT NULL,
  `beneficiary_card_number` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log`
--

LOCK TABLES `log` WRITE;
/*!40000 ALTER TABLE `log` DISABLE KEYS */;
/*!40000 ALTER TABLE `log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `session`
--

DROP TABLE IF EXISTS `session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `session` (
  `session_id` varchar(255) NOT NULL,
  `device_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `ip_address` varchar(50) DEFAULT NULL,
  `refresh_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `expired` timestamp NULL DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT NULL,
  `user_email` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`session_id`),
  KEY `fk_session_user` (`user_email`),
  CONSTRAINT `fk_session_user` FOREIGN KEY (`user_email`) REFERENCES `user` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `session`
--

LOCK TABLES `session` WRITE;
/*!40000 ALTER TABLE `session` DISABLE KEYS */;
INSERT INTO `session` VALUES ('011e0e40-3137-40cd-99a8-775b52bfd5fe','PostmanRuntime/7.29.0','0:0:0:0:0:0:0:1','eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJob2FuZ2FuaDI1MDIyMDAwQGdtYWlsLmNvbSIsInJvbGVzIjoiMCIsImV4cCI6MTY1NTI5MjI0MCwiaWF0IjoxNjU1MTEyMjQwfQ._fFYiuQLPG9MDT945BLhB9mD24gJGk2aDb7rOJNzSLY0OhTH73qwBbnf8t16sUG5mbn2lll8u1Gi2JMXfx6fTQ','2022-06-15 11:24:00','2022-06-13 09:24:01','hoanganh25022000@gmail.com'),('1ec7e5c5-9226-4378-9522-194284ddd326','PostmanRuntime/7.29.0','0:0:0:0:0:0:0:1','eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJob2FuZ2FuaDIuZGV2QGdtYWlsLmNvbSIsInJvbGVzIjoiMSIsImV4cCI6MTY1NTM3NDgzNCwiaWF0IjoxNjU1MTk0ODM0fQ.iKk78oGQ05Nu28JsqPBFMZKYcLzDQaawOBtr2HcDPXtRXAtEY5FwYLbOqQh_qY0oj24d2AxTjyuyo3RZI-OjTw','2022-06-16 10:20:34','2022-06-14 08:20:35','hoanganh2.dev@gmail.com'),('3faddf2f-c336-4dbf-8bdd-9417328177aa','PostmanRuntime/7.29.0','0:0:0:0:0:0:0:1','eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJob2FuZ2FuaDI1MDIyMDAwQGdtYWlsLmNvbSIsInJvbGVzIjoiMCIsImV4cCI6MTY1NTI5MzI2MywiaWF0IjoxNjU1MTEzMjYzfQ.GIRA2Gce3CTLCOYuUs1_RXkCGxr7cSk6iHbpcqxj1I1x3Ct_GSRpotN8u7bjiRDKCqOxYKEj1a0QDNFiLoM1mQ','2022-06-15 11:41:03','2022-06-13 09:41:04','hoanganh25022000@gmail.com'),('44de34b2-b688-414d-8958-586a3bd3f63d','PostmanRuntime/7.29.0','0:0:0:0:0:0:0:1','eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJob2FuZ2FuaDI1MDIyMDAwQGdtYWlsLmNvbSIsInJvbGVzIjoiMCIsImV4cCI6MTY1NTI5MjI2MSwiaWF0IjoxNjU1MTEyMjYxfQ.fujyrB8y8bc5aMQw2ZBuAq1Kqq2e8CHWdKOX8lbv7kaGA2pyk5QyWNuULx3qEj9pke5LDhI7Lvglpt7ACG1kZA','2022-06-15 11:24:21','2022-06-13 09:24:21','hoanganh25022000@gmail.com'),('4c94fae1-aa7a-4b84-b29a-8f82ba460f50','Dart/2.16 (dart:io)','10.0.106.50','eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6emVyb3owMDAwQGdtYWlsLmNvbSIsInJvbGVzIjoiMCIsImV4cCI6MTY1NTI4OTk5OCwiaWF0IjoxNjU1MTA5OTk4fQ.dpWjqMBmb1IlKYDPOBGSthc2FcitfWJhYdUI44DfsHQHkowO7M8ldlxBbks_DS08uO5JDsAZK-MG0T_Z2TqYrQ','2022-06-15 10:46:38','2022-06-13 08:46:39','zzeroz0000@gmail.com'),('67993c4d-06f1-4384-9b78-72539aa4bba1','PostmanRuntime/7.29.0','0:0:0:0:0:0:0:1','eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJob2FuZ2FuaDI1MDIyMDAwQGdtYWlsLmNvbSIsInJvbGVzIjoiMCIsImV4cCI6MTY1NTM1MjEzOSwiaWF0IjoxNjU1MTcyMTM5fQ.3dja9EE9Vy-d5U3Jrye3RI4g2-GSNxbkLZlCftLMKZTi6-fKVYwhG8XDrVjjZ_EBOzI83lxDUADbm5j1pxttMQ','2022-06-16 04:02:19','2022-06-14 02:02:20','hoanganh25022000@gmail.com'),('68b8153f-ff9b-4944-8c71-a6baaa52d179','PostmanRuntime/7.29.0','0:0:0:0:0:0:0:1','eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJob2FuZ2FuaDIuZGV2QGdtYWlsLmNvbSIsInJvbGVzIjoiMSIsImV4cCI6MTY1NTI5MzM1MywiaWF0IjoxNjU1MTEzMzUzfQ.Uc3Qpxq_MgiqmQQWLXhd1Ppf0XqTEbZm7csZ6TtcQpVbYcYP6zDzEUKOYgxmU4RMOJbaj3JL_H32vi1DwGbe3g','2022-06-15 11:42:33','2022-06-13 09:42:34','hoanganh2.dev@gmail.com'),('696205a5-19fb-4697-aa39-bc4d130a8c55','PostmanRuntime/7.29.0','0:0:0:0:0:0:0:1','eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJob2FuZ2FuaDIuZGV2QGdtYWlsLmNvbSIsInJvbGVzIjoiMSIsImV4cCI6MTY1NTM1MzEyNywiaWF0IjoxNjU1MTczMTI3fQ.Eqz4yMbKiJ0i__cv4m6zNKgtYdTH-bjgyVB4KIyoCRC6zPeOUSf1SS9eyDYvs72DZmoY_qOCxpFSfRi5BlUcIA','2022-06-16 04:18:47','2022-06-14 02:18:48','hoanganh2.dev@gmail.com'),('95215fbd-f050-446f-bd45-72bf643c99f7','PostmanRuntime/7.29.0','0:0:0:0:0:0:0:1','eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJob2FuZ2FuaDIuZGV2QGdtYWlsLmNvbSIsInJvbGVzIjoiMSIsImV4cCI6MTY1NTM1MTYzNSwiaWF0IjoxNjU1MTcxNjM1fQ.reyjCbGgeyymLqLEfVJHU8IU2RT20URonlIyhYOT9x64R6beAJztF3Hajo4ugAeXRwH8cvYYCWni2x4vaE_HBQ','2022-06-16 03:53:55','2022-06-14 01:53:55','hoanganh2.dev@gmail.com'),('a7498cd1-d462-4027-af77-133753743bb5','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.63 Safari/537.36 Edg/102.0.1245.39','10.0.106.51','eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJob2FuZ2FuaDIuZGV2QGdtYWlsLmNvbSIsInJvbGVzIjoiMSIsImV4cCI6MTY1NTI5MDc2MSwiaWF0IjoxNjU1MTEwNzYxfQ.DE1ojTXWGb4W7ONNAz2l6iFQRPseRBkrSw_22myyDeyS5Sa-cKU6M2D3LTVne-zsx_DkXfVRrE4VEj-49gWQyQ','2022-06-15 10:59:21','2022-06-13 08:59:22','hoanganh2.dev@gmail.com'),('c495e1cd-1dbc-4fce-8a71-018848aaa75d','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.63 Safari/537.36 Edg/102.0.1245.39','10.0.106.51','eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJob2FuZ2FuaDIuZGV2QGdtYWlsLmNvbSIsInJvbGVzIjoiMSIsImV4cCI6MTY1NTI5MjM1MCwiaWF0IjoxNjU1MTEyMzUwfQ.cRLJ6kYKhtFUyeINmLjK5za8h0hqNPkONetQZD1U49SMJrAadqLwHz3Ezz3nhilz9_iYOpopyNJArn-ZejiqCg','2022-06-15 11:25:50','2022-06-13 09:25:50','hoanganh2.dev@gmail.com'),('c52aec3d-0ab9-4a84-a1b1-4b8206221e47','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.63 Safari/537.36 Edg/102.0.1245.39','10.0.106.51','eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJob2FuZ2FuaDIuZGV2QGdtYWlsLmNvbSIsInJvbGVzIjoiMSIsImV4cCI6MTY1NTI5MTkzOCwiaWF0IjoxNjU1MTExOTM4fQ.CXitUY4ED8oI-V-12eiOGzvoicaM_p0du0koHKq5V0kO5CRKKdJNBmQzrrBsYDZLqC_PlK3i7FCQZVeswLxMtA','2022-06-15 11:18:58','2022-06-13 09:18:58','hoanganh2.dev@gmail.com'),('ced1f68a-1e0b-4219-9be0-6e99dc9a4720','Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.63 Safari/537.36 Edg/102.0.1245.39','10.0.106.51','eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJob2FuZ2FuaDI1MDIyMDAwQGdtYWlsLmNvbSIsInJvbGVzIjoiMCIsImV4cCI6MTY1NTI5MTg4NCwiaWF0IjoxNjU1MTExODg0fQ.XwYU1xa-VyvNUv3T2xteRdunX1xnAdSCVNIxSLcxWLtUYgaSH07R4L4LLFtLrKUzzU0dwYWuLyu-1FbsBWpkQw','2022-06-15 11:18:04','2022-06-13 09:18:05','hoanganh25022000@gmail.com'),('f61b6901-023e-4778-a295-9712e27a8c4a','PostmanRuntime/7.29.0','0:0:0:0:0:0:0:1','eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJob2FuZ2FuaDI1MDIyMDAwQGdtYWlsLmNvbSIsInJvbGVzIjoiMCIsImV4cCI6MTY1NTI4MjE1NCwiaWF0IjoxNjU1MTAyMTU0fQ.dnvyet5oCZLSt18ADbZMvD_vna0J1xIDAP3B2T_UZlaYw2LxFWTU7iZWbSLWA_cJclxOaIvndLzAKXJw3wdWVg','2022-06-15 08:35:54','2022-06-13 06:35:54','hoanganh25022000@gmail.com');
/*!40000 ALTER TABLE `session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction` (
  `transaction_id` varchar(300) NOT NULL,
  `amount` double(14,2) DEFAULT NULL,
  `balance` double(14,2) DEFAULT NULL,
  `body` varchar(200) DEFAULT NULL,
  `transfer_number` varchar(20) DEFAULT NULL,
  `unit_currency` varchar(20) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `transaction_type` int DEFAULT NULL,
  `beneficiary_card_number` varchar(20) DEFAULT NULL,
  `beneficiary_name` varchar(200) DEFAULT NULL,
  `beneficiary_email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `beneficiary_phone` varchar(12) DEFAULT NULL,
  `create_person` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT NULL,
  `fee` double(14,2) DEFAULT NULL,
  `card_number` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `fk_transaction_account` (`card_number`),
  CONSTRAINT `fk_transaction_account` FOREIGN KEY (`card_number`) REFERENCES `card` (`card_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction`
--

LOCK TABLES `transaction` WRITE;
/*!40000 ALTER TABLE `transaction` DISABLE KEYS */;
INSERT INTO `transaction` VALUES ('0436267d-324c-4550-aec3-e63cc84a68dc',50000.00,4949000.00,'chuyen','1001485064420644','VND',1,4,'1001416877292373','Coong Lee Hoong','lehongcong2k@gmail.com','0379557111','lehongcong2k@gmail.com','2022-06-10 06:46:27',0.00,'1001416877292373'),('04b0f140-322f-45f4-a304-c1275a078e20',9000000.00,973544.00,'NGUYỄN HOÀNG ANH TRANSFER MONEY','1001214692663827','VND',1,4,'1001549641636202','Nguyễn Hoàng Anh','hoanganh2.dev@gmail.com','0379557097','hoanganh2.dev@gmail.com','2022-06-10 09:30:18',0.00,'1001549641636202'),('15325dfb-d47a-43c0-9906-f8ee8a5a8ef1',4949000.00,-1000.00,'chuyen',NULL,'VND',1,3,'1001416877292373','Coong Lee Hoong','lehongcong2k@gmail.com','0379557111','lehongcong2k@gmail.com','2022-06-10 08:32:59',1000.00,'1001485064420644'),('1820e5fd-cd94-4bb4-8706-48e602e0b77f',301000.00,-151000.00,'sbsj',NULL,'VND',1,3,'1001416877292373','Coong Lee Hoong','lehongcong2k@gmail.com','0379557111','lehongcong2k@gmail.com','2022-06-10 08:36:30',1000.00,'1001485064420644'),('1d1ac7f3-f9af-454e-820e-080381ac70ad',4948000.00,-1000.00,'chuyen','1001485064420644','VND',1,4,'1001416877292373','Coong Lee Hoong','lehongcong2k@gmail.com','0379557111','lehongcong2k@gmail.com','2022-06-10 08:32:59',0.00,'1001416877292373'),('211186cf-808e-4066-ae1a-43f0fbdab3a3',911000.00,99097000.00,'NGUYỄN HOÀNG ANH TRANSFER MONEY',NULL,'VND',1,3,'1001549641636202','Nguyễn Hoàng Anh','hoanganh2.dev@gmail.com','0379557097','hoanganh2.dev@gmail.com','2022-06-10 07:51:17',1000.00,'1001207874495501'),('2c81fb40-7511-4696-aa63-94a69cb9784c',1001000.00,-1152000.00,'hsbs',NULL,'VND',1,3,'1001416877292373','Coong Lee Hoong','lehongcong2k@gmail.com','0379557111','lehongcong2k@gmail.com','2022-06-10 08:37:04',1000.00,'1001485064420644'),('2ed3c144-8f3c-4b1d-9999-a4204bf79846',101000.00,150000.00,'Coong Lee Hoong Deposit money',NULL,'VND',1,1,'1001485064420644','Coong Lee Hoong','lehongcong2k@gmail.com','0379557111','lehongcong2k@gmail.com','2022-06-10 08:35:57',0.00,'1001485064420644'),('349ef952-37bf-4d66-a207-00c54d9f960d',10000000.00,10000000.00,'Nguyễn Hoàng Anh Deposit money',NULL,'VND',1,1,'1001214692663827','Nguyễn Hoàng Anh','hoanganh2.dev@gmail.com','0379557097','hoanganh2.dev@gmail.com','2022-06-10 04:26:38',0.00,'1001214692663827'),('361849cf-83c4-4e23-8c6b-d9e2cd3ee252',300000.00,-151000.00,'sbsj','1001485064420644','VND',1,4,'1001416877292373','Coong Lee Hoong','lehongcong2k@gmail.com','0379557111','lehongcong2k@gmail.com','2022-06-10 08:36:30',0.00,'1001416877292373'),('45c2c48d-0890-4c02-b799-3442004f022e',123456.00,9974544.00,'NGUYỄN HOÀNG ANH TRANSFER MONEY','1001214692663827','VND',1,4,'1001549641636202','Nguyễn Hoàng Anh','hoanganh2.dev@gmail.com','0379557097','hoanganh2.dev@gmail.com','2022-06-10 07:49:27',0.00,'1001549641636202'),('499e556c-5dca-45ac-98b4-efe614b1bb1f',5000000.00,5550000.00,'Coong Lee Hoong Deposit money',NULL,'VND',1,1,'1001416877292373','Coong Lee Hoong','lehongcong2k@gmail.com','0379557111','lehongcong2k@gmail.com','2022-06-10 06:46:45',0.00,'1001416877292373'),('4c76fdbe-fc81-46fa-ae86-4807cc6e062b',910000.00,99097000.00,'NGUYỄN HOÀNG ANH TRANSFER MONEY','1001207874495501','VND',1,4,'1001549641636202','Nguyễn Hoàng Anh','hoanganh2.dev@gmail.com','0379557097','hoanganh2.dev@gmail.com','2022-06-10 07:51:17',0.00,'1001549641636202'),('4d06d7fe-95b5-4938-9987-61e8b64a9486',51000.00,786888.00,'Nguyễn Hoàng Anh Withdraw money',NULL,'VND',1,2,'1001549641636202','Nguyễn','hoanganh2.dev@gmail.com','0379557097','hoanganh2.dev@gmail.com','2022-06-10 07:43:56',1000.00,'1001549641636202'),('5c3a4659-2fa6-4b11-8b87-29a379e71100',20001000.00,-21153000.00,'shhs',NULL,'VND',1,3,'1001416877292373','Coong Lee Hoong','lehongcong2k@gmail.com','0379557111','lehongcong2k@gmail.com','2022-06-10 08:38:31',1000.00,'1001485064420644'),('6078959e-e25a-4292-86d9-94c3802942c4',50000.00,10099000.00,'Nguyễn Hoàng Anh Deposit money',NULL,'VND',1,1,'1001214692663827','Nguyễn Hoàng Anh','hoanganh2.dev@gmail.com','0379557097','hoanganh2.dev@gmail.com','2022-06-10 07:41:24',0.00,'1001214692663827'),('61e863cf-e032-4d6f-9411-b79a17df1c02',1000000.00,-1152000.00,'hsbs','1001485064420644','VND',1,4,'1001416877292373','Coong Lee Hoong','lehongcong2k@gmail.com','0379557111','lehongcong2k@gmail.com','2022-06-10 08:37:04',0.00,'1001416877292373'),('6276abbb-0ea0-40a0-9063-753288aae472',999999.00,1999999.00,'Nguyễn Hoàng Anh Deposit money',NULL,'VND',1,1,'1001207874495501','Nguyễn Hoàng Anh','hoanganh2.dev@gmail.com','0379557097','hoanganh2.dev@gmail.com','2022-06-10 07:30:49',0.00,'1001207874495501'),('6777db10-ca64-4b5b-a7e9-8a01b6444534',500000.00,500000.00,'Coong Lee Hoong Deposit money',NULL,'VND',1,1,'1001416877292373','Coong Lee Hoong','lehongcong2k@gmail.com','0379557111','lehongcong2k@gmail.com','2022-06-10 03:46:50',0.00,'1001416877292373'),('6c4b79c5-eff8-45bc-b350-7edd1fb39cd5',50000.00,49000.00,'Coong Lee Hoong Deposit money',NULL,'VND',1,1,'1001485064420644','Coong Lee Hoong','lehongcong2k@gmail.com','0379557111','lehongcong2k@gmail.com','2022-06-10 08:34:32',0.00,'1001485064420644'),('785e369e-6a5e-4895-90a6-9a9239fcb3f5',9001000.00,973544.00,'NGUYỄN HOÀNG ANH TRANSFER MONEY',NULL,'VND',1,3,'1001549641636202','Nguyễn Hoàng Anh','hoanganh2.dev@gmail.com','0379557097','hoanganh2.dev@gmail.com','2022-06-10 09:30:18',1000.00,'1001214692663827'),('86af292d-860d-46db-b342-d96b5ae06295',96000.10,99000999.90,'NGUYỄN HOÀNG ANH TRANSFER MONEY',NULL,'VND',1,3,'1001549641636202','Nguyễn Hoàng Anh','hoanganh2.dev@gmail.com','0379557097','hoanganh2.dev@gmail.com','2022-06-10 07:52:34',1000.00,'1001207874495501'),('87609c24-fd03-415f-ae7a-4e6414a66202',500000.00,5449000.00,'Coong Lee Hoong Deposit money',NULL,'VND',1,1,'1001485064420644','Coong Lee Hoong','lehongcong2k@gmail.com','0379557111','lehongcong2k@gmail.com','2022-06-10 08:30:41',0.00,'1001485064420644'),('88da9ed8-442e-4bb6-a717-3abd2d3fb071',10001000.00,9999000.00,'Nguyễn Hoàng Anh Withdraw money',NULL,'VND',1,2,'1001214692663827','Nguyễn','hoanganh2.dev@gmail.com','0379557097','hoanganh2.dev@gmail.com','2022-06-10 06:09:47',1000.00,'1001214692663827'),('998a31d1-a6c8-4bc1-a44f-d3d8bc3c5732',50000.00,10049000.00,'Nguyễn Hoàng Anh Deposit money',NULL,'VND',1,1,'1001214692663827','Nguyễn Hoàng Anh','hoanganh2.dev@gmail.com','0379557097','hoanganh2.dev@gmail.com','2022-06-10 07:40:35',0.00,'1001214692663827'),('99b7f407-8085-43de-ad4d-925fe4894d4b',95000.10,99000999.90,'NGUYỄN HOÀNG ANH TRANSFER MONEY','1001207874495501','VND',1,4,'1001549641636202','Nguyễn Hoàng Anh','hoanganh2.dev@gmail.com','0379557097','hoanganh2.dev@gmail.com','2022-06-10 07:52:34',0.00,'1001549641636202'),('9added0a-6c2a-4ce0-bfba-811f80ced710',888888.00,888888.00,'Nguyễn Hoàng Anh Deposit money',NULL,'VND',1,1,'1001549641636202','Nguyễn Hoàng Anh','hoanganh2.dev@gmail.com','0379557097','hoanganh2.dev@gmail.com','2022-06-10 07:31:30',0.00,'1001549641636202'),('9e1c7427-faf2-4741-a475-26a04096ae37',501000.00,4948000.00,'Coong Lee Hoong Withdraw money',NULL,'VND',1,2,'1001485064420644','Coong','lehongcong2k@gmail.com','0379557111','lehongcong2k@gmail.com','2022-06-10 08:31:20',1000.00,'1001485064420644'),('a5ccb4f1-9d77-4ddc-8788-80aaa2a4b302',1000000.00,1000000.00,'Nguyễn Hoàng Anh Deposit money',NULL,'VND',1,1,'1001207874495501','Nguyễn Hoàng Anh','hoanganh2.dev@gmail.com','0379557097','hoanganh2.dev@gmail.com','2022-06-10 06:48:03',0.00,'1001207874495501'),('a82ce704-4700-4380-a9ba-e98f90ea396a',100000000.00,100008000.00,'Nguyễn Hoàng Anh Deposit money',NULL,'VND',1,1,'1001207874495501','Nguyễn Hoàng Anh','hoanganh2.dev@gmail.com','0379557097','hoanganh2.dev@gmail.com','2022-06-10 07:46:59',0.00,'1001207874495501'),('b1144ec2-97cc-4473-9266-2319ad391697',51000.00,4949000.00,'chuyen',NULL,'VND',1,3,'1001416877292373','Coong Lee Hoong','lehongcong2k@gmail.com','0379557111','lehongcong2k@gmail.com','2022-06-10 06:46:27',1000.00,'1001485064420644'),('b3b87625-ddf5-4fbf-82db-550384eecd4f',1000000.00,100000999.90,'Nguyễn Hoàng Anh Deposit money',NULL,'VND',1,1,'1001207874495501','Nguyễn Hoàng Anh','hoanganh2.dev@gmail.com','0379557097','hoanganh2.dev@gmail.com','2022-06-13 10:02:42',0.00,'1001207874495501'),('ce375295-3752-47f2-941d-7347caf6978a',124456.00,9974544.00,'NGUYỄN HOÀNG ANH TRANSFER MONEY',NULL,'VND',1,3,'1001549641636202','Nguyễn Hoàng Anh','hoanganh2.dev@gmail.com','0379557097','hoanganh2.dev@gmail.com','2022-06-10 07:49:27',1000.00,'1001214692663827'),('d1d7b9c0-8e0c-4691-880f-6f13785a7b6f',20000000.00,-21153000.00,'shhs','1001485064420644','VND',1,4,'1001416877292373','Coong Lee Hoong','lehongcong2k@gmail.com','0379557111','lehongcong2k@gmail.com','2022-06-10 08:38:31',0.00,'1001416877292373'),('dac69ffe-4ec7-4e98-8b3b-d928acc0a3c2',5000000.00,5000000.00,'Coong Lee Hoong Deposit money',NULL,'VND',1,1,'1001485064420644','Coong Lee Hoong','lehongcong2k@gmail.com','0379557111','lehongcong2k@gmail.com','2022-06-10 06:45:31',0.00,'1001485064420644'),('df5f43c5-d2a4-458d-8805-a8ab88163f09',1991999.00,8000.00,'Nguyễn Hoàng Anh Withdraw money',NULL,'VND',1,2,'1001207874495501','Nguyễn','hoanganh2.dev@gmail.com','0379557097','hoanganh2.dev@gmail.com','2022-06-10 07:33:11',1000.00,'1001207874495501'),('fbf58f17-f02f-4e2c-8687-8b781c0621f1',10000000.00,20000000.00,'Nguyễn Hoàng Anh Deposit money',NULL,'VND',1,1,'1001214692663827','Nguyễn Hoàng Anh','hoanganh2.dev@gmail.com','0379557097','hoanganh2.dev@gmail.com','2022-06-10 06:09:35',0.00,'1001214692663827'),('fc461e97-9da0-4e18-a719-1e6ee2556c0d',51000.00,837888.00,'Nguyễn Hoàng Anh Withdraw money',NULL,'VND',1,2,'1001549641636202','Nguyễn','hoanganh2.dev@gmail.com','0379557097','hoanganh2.dev@gmail.com','2022-06-10 07:43:06',1000.00,'1001549641636202');
/*!40000 ALTER TABLE `transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `email` varchar(300) NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(200) DEFAULT NULL,
  `gender` int DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `address` longtext,
  `phone` varchar(12) DEFAULT NULL,
  `verify_code` varchar(100) DEFAULT NULL,
  `is_activated` tinyint(1) DEFAULT NULL,
  `create_person` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_person` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_date` timestamp NULL DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT NULL,
  `role` int DEFAULT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('adipisicing@gmail.123','$2a$10$SdZK25TGhc5rv7QFPHcosepqCFxrkoAANVqf81m5dBRFQ6b1GilUG','amet','consectetur dolor exercitation',1,'1997-12-03','sed proident in consectetur enim','12345678910','YugIHOHeXQ',0,'adipisicing@gmail.123','adipisicing@gmail.123','2022-06-03 01:46:30','2022-06-03 01:46:30',NULL),('HOANGANH123@GMAIL.com','$2a$10$sGyAA.4bkAFVOpPN.HRTOuPpGYYX.YmywRySNjlTAx75psLwdRn4G','amet','consectetur dolor exercitation',1,'1997-12-03','sed proident in consectetur enim','354687321654','hEAZZHKBNq',1,'HOANGANH123@GMAIL.com','HOANGANH123@GMAIL.com','2022-06-03 01:56:41','2022-06-03 01:56:41',NULL),('hoanganh2.dev@gmail.com','$2a$10$B1IcwttV4J7VNL1Fa9cRUuquO1TobJQXeeOj4swBmgiTciAKOlJh6','Nguyễn','Hoàng Anh',1,'2022-05-25','Đồng Nai','0379557097','',1,'hoanganh2.dev@gmail.com','hoanganh2.dev@gmail.com','2022-05-30 04:30:02','2022-05-30 04:30:02',1),('hoanganh25022000@gmail.com','$2a$10$AqZQkoRoNuzVHFYBDl6Oru8vPZd25r/7CPkNkQrEZMuUbvbZgpBTi','Nguyễn','Hoàng Anh',1,'2000-02-25','Bình Thuận','0362006314','',1,'Đại Tấn',NULL,NULL,NULL,0),('hoanganh9876@gmail.com','$2a$10$RyQHKc7NQClruPv08teXpOeOazUlpJPZ6oLAvsFGzvrfFBq/DPzeG','amet','consectetur dolor exercitation',1,'1997-12-03','sed proident in consectetur enim','0362006325','vkKLJNsGlD',1,'hoanganh9876@gmail.com','hoanganh9876@gmail.com','2022-06-03 02:57:19','2022-06-03 02:57:19',NULL),('huynhtan12133@123123123.com','$2a$10$can7LvrY10iJPpJWTmWZHedsnG.drevpQexzB9vF2ObejW3cAUbeC','123123','123123',1,'2022-05-03','ADASDFASDASD','1231231231','IPbVsBiZna',1,'huynhtan12133@123123123.com','huynhtan12133@123123123.com','2022-05-30 06:33:03','2022-05-30 06:33:03',NULL),('huynhtan12133@gmail.com','$2a$10$Z5pXFFWfM1/cDkqv8T6c5eZVCQjQa2FFg1HrttYcWdtC7kUIGnHLu','Huỳnh','Đại Tấn',0,'2000-02-25','Bình Thuận','0362006314','',1,'Đại Tấn','Đại Tấn',NULL,NULL,0),('lehongcong.dev@gmail.com','$2a$10$9zFoaG9wH715fWFAVoxztueTU1X.HINGtnNdo4OPNj6lP7SLE/Xy2','Coong DEV','Lee Hoong DEV',1,'2000-02-25','Đồng Nai','0379557112','MsIdLLDyww',1,'lehongcong.dev@gmail.com','lehongcong.dev@gmail.com','2022-06-01 06:41:53','2022-06-01 06:41:53',NULL),('lehongcong.present@gmail.com','$2a$10$sYfnFHGH0lWTcK6jeD/6HeRzyHCFEHyl4YfvNyHR8sNcuQQUDiTkO','Coong','Lee Hoong',1,'2022-05-25','Đồng Nai','036200456465','exsXjWUSJg',1,'lehongcong.present@gmail.com','lehongcong.present@gmail.com','2022-05-30 04:30:59','2022-05-30 04:30:59',1),('lehongcong2.dev@gmail.com','$2a$10$spGGFuJhyp2k9P7waezPmuZwqzjhPfMKVhRCXojpNkoR2gVVhxU8m','Coong DEV','Lee Hoong DEV',1,'2000-02-25','Đồng Nai','0379557113','fVjsQJDgLC',1,'lehongcong2.dev@gmail.com','lehongcong2.dev@gmail.com','2022-06-01 06:47:13','2022-06-01 06:47:13',NULL),('lehongcong2k@gmail.com','$2a$10$GKO7mWFmlmtyGhzSw1YhB.pWK25Pq1RQo3TNKn0fB6h.f9UczODcK','Coong','Lee Hoong',1,'2000-02-25','Đồng Nai','0379557111','jEaLumSQLC',1,'lehongcong2k@gmail.com','lehongcong2k@gmail.com','2022-05-31 02:22:55','2022-05-31 02:22:55',1),('lehongcong3.dev@gmail.com','$2a$10$53gHv7A1aGzTtCQgFhuisubqrcStiIMTOQZyS3Je3Z2bfYp1IiQcy','Coong DEV','Lee Hoong DEV',1,'2000-02-25','Đồng Nai','0379557123','rmsOkZHfQs',1,'lehongcong3.dev@gmail.com','lehongcong3.dev@gmail.com','2022-06-01 06:53:12','2022-06-01 06:53:12',NULL),('lehongcong5kdev@gmail.com','$2a$10$60BRfvxd9MYoubkDz/adlOVLkjraJNO95AaVHVmFDXxmIJcYkMhVa','Coong DEV','Lee Hoong DEV',1,'2000-02-25','Đồng Nai','0379557103','hygEGAygbN',1,'lehongcong5kdev@gmail.com','lehongcong5kdev@gmail.com','2022-06-01 07:03:39','2022-06-01 07:03:39',NULL),('lehongcong8Kdev@gmail.com','$2a$10$1BeP3kTeieUR4w.wnuSx9OwG3Mx8WwePRSkxOlQ3wmU8qks.T8cu.','Coong DEV','Lee Hoong DEV',1,'2000-02-25','Đồng Nai','0379557108','uAfSUFSKGl',0,'lehongcong8Kdev@gmail.com','lehongcong8Kdev@gmail.com','2022-06-06 06:21:37','2022-06-06 06:21:37',NULL),('minh.ho@asdasdasdasd.com','$2a$10$0TMkJi430ktvMOUruQ9E1.WW3JINtF.DcivEsNxFHy/BAorHcYas.','asd','asdasd',-1,'2022-05-03','123123123123','0902909012','qBfFKqsgSN',1,'minh.ho@asdasdasdasd.com','minh.ho@asdasdasdasd.com','2022-05-30 04:46:51','2022-05-30 04:46:51',NULL),('minh.ho@asdasdasdasdas.com','$2a$10$ueyWc8IPx0gHIr7m1zx4Veb1AlVi1D.qmT3bNkF5TRI6NFM4nPCEq','asd','asdasd',1,'2022-06-02','123123123123','123123123123','hbnizvKBqH',1,'minh.ho@asdasdasdasdas.com','minh.ho@asdasdasdasdas.com','2022-06-01 03:18:30','2022-06-01 03:18:30',NULL),('minh.ho@bstarsolutions.com','$2a$10$wH9qLD3O/ttW5fEFgkkkfuBivx1E4Cu4KnwLLp/pLS1XTUZXUfDka','asd','asdasd',1,'2022-05-04','123123123123','13123123123','KhCGBA',1,'minh.ho@bstarsolutions.com','minh.ho@bstarsolutions.com','2022-05-30 04:35:26','2022-05-30 04:35:26',NULL),('minh.ho@bstarsolutions.comasdsad','$2a$10$HwBvXKAFWtA8oBP9L8wUAey9/oD2swQr3UiIpVY3b8cjcYlMrbJy.','asd','asdasd',1,'2022-06-09','123123123123','0344383712','cBSyErtiMV',0,'minh.ho@bstarsolutions.comasdsad','minh.ho@bstarsolutions.comasdsad','2022-06-10 01:51:53','2022-06-10 01:51:53',NULL),('minh.ho@bstarsolutions123123123.com','$2a$10$jdgpNBu0UV6xSCL26ZGzIeowwoG/GPAjJ7b7/c2bvJWL4.v2j6BtO','asd','asdasd',1,'2022-06-02','123123123123','31312312312','ayrKbSDphb',1,'minh.ho@bstarsolutions123123123.com','minh.ho@bstarsolutions123123123.com','2022-06-01 02:55:42','2022-06-01 02:55:42',NULL),('minh.ho@sfsfsdf.com','$2a$10$vSCt66j70kKVMroE6hWjlOcpTmSyTJLHkA0WAWX6gt4g40yEYN5wu','asd','asdasd',1,'2022-05-10','123123123123','0344387371','XqbFVNBaqJ',1,'minh.ho@sfsfsdf.com','minh.ho@sfsfsdf.com','2022-05-30 04:41:02','2022-05-30 04:41:02',NULL),('minhho.technolgy@gmail.com','$2a$10$BK45pub4au40gCaoP6WZtOZ30u1/GWcBpPJEXNM8L3APIgbsejBp6','Nguyễn','Đại Tấn',NULL,'2000-02-25','Bình Thuận','0362006314',NULL,1,'Đại Tấn',NULL,NULL,NULL,0),('nguyenbui469@gmail.com','$2a$10$5nxtzQcow/n2iABmreVPYOeOwIIXk4B054W8.FTpb3YfZTXO4tHea','Nguuyen','Bui Xuan',1,'2022-06-10','ADASDFASDASD','0909999999','',0,'nguyenbui469@gmail.com','nguyenbui469@gmail.com','2022-06-10 03:41:23','2022-06-10 03:41:23',NULL),('pomay49063@krunsea.com','$2a$10$RGV2FBOgjN3eVbJw18qx4O9364cKXdZfJSi5MAgk2aT3KhUjPwyea','asd','asdasd',1,'2022-06-09','123123123123','0344387377','vphMTOEGHM',0,'pomay49063@krunsea.com','pomay49063@krunsea.com','2022-06-10 02:17:59','2022-06-10 02:17:59',NULL),('tan1@gmail.com','$2a$10$PV9FjrxWvVN3dq4aB7LsuOglq8yzG6SZQXJUDE5na5dm34bZw6cKC','Tan','Huynh',1,'2022-06-14','ADASDFASDASD','0931231201','hGHAXcUoAc',0,'tan1@gmail.com','tan1@gmail.com','2022-06-13 04:53:03','2022-06-13 04:53:03',NULL),('tan1213@gmail.com','$2a$10$Qg2xO/wPIw.JWPrysN2TLuuQ/b.Uh94/3UYjOyRwu6BBurSIKaT1a','123123','123123',0,'2022-06-13','ADASDFASDASD','0931231200','McOKtEhSKM',0,'tan1213@gmail.com','tan1213@gmail.com','2022-06-13 04:50:13','2022-06-13 04:50:13',NULL),('zzeroz0000@gmail.com','$2a$10$9Dqiat66y/91ozlymKKnlOTj0A4J4eQ/xqqinVXsrlnyvTZj5FRpa','Huỳnh','Đại Tấn',NULL,'2000-02-25','Bình Thuận','0362006314','LPmbpQ',1,'Đại Tấn',NULL,NULL,NULL,0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-06-15 11:20:57
