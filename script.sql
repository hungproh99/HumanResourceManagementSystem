CREATE DATABASE IF NOT EXISTS `human-resource-management` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
USE `human-resource-management`;
-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: 34.101.229.25    Database: human-resource-management
-- ------------------------------------------------------
-- Server version	8.0.26-google

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN = 0;

--
-- GTID state at the beginning of the backup
--

SET @@GLOBAL.GTID_PURGED = /*!80000 '+'*/ 'e3adf621-db63-11ec-b7c1-42010ab80002:1-84710';

--
-- Table structure for table `area`
--

DROP TABLE IF EXISTS `area`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `area`
(
    `area_id` bigint NOT NULL AUTO_INCREMENT,
    `name`    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`area_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `area`
--

LOCK TABLES `area` WRITE;
/*!40000 ALTER TABLE `area`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `area`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bank`
--

DROP TABLE IF EXISTS `bank`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bank`
(
    `bank_id`        bigint NOT NULL,
    `account_name`   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `account_number` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `address`        varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name_bank`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`bank_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bank`
--

LOCK TABLES `bank` WRITE;
/*!40000 ALTER TABLE `bank`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `bank`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contract_type`
--

DROP TABLE IF EXISTS `contract_type`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contract_type`
(
    `type_id` bigint NOT NULL,
    `name`    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`type_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contract_type`
--

LOCK TABLES `contract_type` WRITE;
/*!40000 ALTER TABLE `contract_type`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `contract_type`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `discipline`
--

DROP TABLE IF EXISTS `discipline`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discipline`
(
    `discipline_id`          bigint NOT NULL,
    `date`                   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `description`            varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `punishment`             varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `status`                 varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `title`                  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `type_id`                bigint                                  DEFAULT NULL,
    `working_information_id` bigint                                  DEFAULT NULL,
    PRIMARY KEY (`discipline_id`),
    KEY `FK7pa865hyrhp12fljb3rqh7h28` (`type_id`),
    KEY `FKhrolhrw5rulta6u6ujyjuly0x` (`working_information_id`),
    CONSTRAINT `FK7pa865hyrhp12fljb3rqh7h28` FOREIGN KEY (`type_id`) REFERENCES `discipline_type` (`type_id`),
    CONSTRAINT `FKhrolhrw5rulta6u6ujyjuly0x` FOREIGN KEY (`working_information_id`) REFERENCES `working_information` (`working_information_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discipline`
--

LOCK TABLES `discipline` WRITE;
/*!40000 ALTER TABLE `discipline`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `discipline`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `discipline_type`
--

DROP TABLE IF EXISTS `discipline_type`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discipline_type`
(
    `type_id` bigint NOT NULL,
    `name`    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`type_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discipline_type`
--

LOCK TABLES `discipline_type` WRITE;
/*!40000 ALTER TABLE `discipline_type`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `discipline_type`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `education`
--

DROP TABLE IF EXISTS `education`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `education`
(
    `education_id` bigint NOT NULL,
    `certificate`  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `end_date`     date                                    DEFAULT NULL,
    `name_school`  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `start_date`   date                                    DEFAULT NULL,
    `status`       varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `employee_id`  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`education_id`),
    KEY `FKrphg8gbx569xvj1txkkt91uy4` (`employee_id`),
    CONSTRAINT `FKrphg8gbx569xvj1txkkt91uy4` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `education`
--

LOCK TABLES `education` WRITE;
/*!40000 ALTER TABLE `education`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `education`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee`
(
    `employee_id`    varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `address`        varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `avatar`         varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `birth_date`     date                                    DEFAULT NULL,
    `company_email`  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `full_name`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `gender`         char(1) COLLATE utf8mb4_unicode_ci      DEFAULT NULL,
    `insurance_id`   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `manager_id`     varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `marital_status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `password`       varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `personal_email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `phone_number`   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `tax_code`       varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `work_status`    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `card_id`        varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `facebook`       varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `nick_name`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `bank_id`        bigint                                  DEFAULT NULL,
    `role_type`      bigint                                  DEFAULT NULL,
    PRIMARY KEY (`employee_id`),
    KEY `FKe4gss4ey1fu5mxpe82eqnvac6` (`card_id`),
    KEY `FKicptso0qh4vmwt76bgf5hm9p6` (`bank_id`),
    KEY `FKqsqu0215r8to2n2ejj7ejcimv` (`role_type`),
    CONSTRAINT `FKe4gss4ey1fu5mxpe82eqnvac6` FOREIGN KEY (`card_id`) REFERENCES `identity_card` (`card_id`),
    CONSTRAINT `FKicptso0qh4vmwt76bgf5hm9p6` FOREIGN KEY (`bank_id`) REFERENCES `bank` (`bank_id`),
    CONSTRAINT `FKqsqu0215r8to2n2ejj7ejcimv` FOREIGN KEY (`role_type`) REFERENCES `role_type` (`type_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee`
    DISABLE KEYS */;
INSERT INTO `employee`
VALUES ('hungnd01', 'Ha Noi', NULL, '1999-08-04', 'hungnd01@fpt.edu.vn', 'Nguyen Duc Hung', '1', NULL, 'huynq100', NULL,
        '$2a$10$nFA0YQzDtIstG95N.KqNFeKBztl5oXu2ezisXmriftl8u5iwRY/xa', 'hungpro@gmail.com', '0912345678', NULL, NULL,
        NULL, NULL, NULL, NULL, NULL),
       ('huynq100', NULL, NULL, NULL, 'huynq100@fpt.edu.vn', NULL, '1', NULL, NULL, NULL,
        '$2a$10$6qwZWubXua2vTYMsXLmbhuYeCkpgJUGeVC6Cvib3zyffX7rLP2iiu', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL),
       ('huynqhe141565', NULL, NULL, NULL, 'huynqhe141565@fpt.edu.vn', NULL, '1', NULL, NULL, NULL,
        '$2a$10$nFA0YQzDtIstG95N.KqNFeKBztl5oXu2ezisXmriftl8u5iwRY/xa', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL);
/*!40000 ALTER TABLE `employee`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_role`
--

DROP TABLE IF EXISTS `employee_role`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_role`
(
    `employee_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `role_id`     bigint                                  NOT NULL,
    KEY `FK7jol9jrbtlt6ctiehegh6besp` (`role_id`),
    KEY `FKo7rvk7ejtx29vru9cyhf7o68a` (`employee_id`),
    CONSTRAINT `FK7jol9jrbtlt6ctiehegh6besp` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`),
    CONSTRAINT `FKo7rvk7ejtx29vru9cyhf7o68a` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_role`
--

LOCK TABLES `employee_role` WRITE;
/*!40000 ALTER TABLE `employee_role`
    DISABLE KEYS */;
INSERT INTO `employee_role`
VALUES ('huynq100', 1),
       ('huynqhe141565', 3);
/*!40000 ALTER TABLE `employee_role`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `identity_card`
--

DROP TABLE IF EXISTS `identity_card`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `identity_card`
(
    `card_id`            varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `date_of_expiry`     date                                    DEFAULT NULL,
    `nationality`        varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `place_of_origin`    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `place_of_residence` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `provide_date`       date                                    DEFAULT NULL,
    `provide_place`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`card_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `identity_card`
--

LOCK TABLES `identity_card` WRITE;
/*!40000 ALTER TABLE `identity_card`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `identity_card`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job`
--

DROP TABLE IF EXISTS `job`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job`
(
    `job_id`      bigint NOT NULL AUTO_INCREMENT,
    `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `position`    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `title`       varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`job_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job`
--

LOCK TABLES `job` WRITE;
/*!40000 ALTER TABLE `job`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `job`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `laudatory`
--

DROP TABLE IF EXISTS `laudatory`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `laudatory`
(
    `laudatory_id`           bigint NOT NULL,
    `date`                   date                                    DEFAULT NULL,
    `description`            varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `reward`                 varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `status`                 varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `title`                  varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `type_id`                bigint                                  DEFAULT NULL,
    `working_information_id` bigint                                  DEFAULT NULL,
    PRIMARY KEY (`laudatory_id`),
    KEY `FKmtp17mjpsckvcbq4cthq5rc50` (`type_id`),
    KEY `FKbgpr6edk7yydps2e6xklmb3` (`working_information_id`),
    CONSTRAINT `FKbgpr6edk7yydps2e6xklmb3` FOREIGN KEY (`working_information_id`) REFERENCES `working_information` (`working_information_id`),
    CONSTRAINT `FKmtp17mjpsckvcbq4cthq5rc50` FOREIGN KEY (`type_id`) REFERENCES `laudatory_type` (`type_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `laudatory`
--

LOCK TABLES `laudatory` WRITE;
/*!40000 ALTER TABLE `laudatory`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `laudatory`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `laudatory_type`
--

DROP TABLE IF EXISTS `laudatory_type`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `laudatory_type`
(
    `type_id` bigint NOT NULL,
    `name`    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`type_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `laudatory_type`
--

LOCK TABLES `laudatory_type` WRITE;
/*!40000 ALTER TABLE `laudatory_type`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `laudatory_type`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `office`
--

DROP TABLE IF EXISTS `office`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `office`
(
    `office_id` bigint NOT NULL AUTO_INCREMENT,
    `address`   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `name`      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`office_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `office`
--

LOCK TABLES `office` WRITE;
/*!40000 ALTER TABLE `office`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `office`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `relative_information`
--

DROP TABLE IF EXISTS `relative_information`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `relative_information`
(
    `relative_id`          bigint NOT NULL AUTO_INCREMENT,
    `birth_date`           date                                    DEFAULT NULL,
    `parent_name`          varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `status`               varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `type`                 varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `employee_employee_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `employee_id`          varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `type_id`              bigint                                  DEFAULT NULL,
    PRIMARY KEY (`relative_id`),
    KEY `FKmyfymr8slbfi6w66yvv48sl4l` (`employee_employee_id`),
    KEY `FKmwkd588kl8gl2rfvi52gxut3` (`employee_id`),
    KEY `FKkkno01o28goapehpudc6pvxgq` (`type_id`),
    CONSTRAINT `FKkkno01o28goapehpudc6pvxgq` FOREIGN KEY (`type_id`) REFERENCES `relative_type` (`type_id`),
    CONSTRAINT `FKmwkd588kl8gl2rfvi52gxut3` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`),
    CONSTRAINT `FKmyfymr8slbfi6w66yvv48sl4l` FOREIGN KEY (`employee_employee_id`) REFERENCES `employee` (`employee_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `relative_information`
--

LOCK TABLES `relative_information` WRITE;
/*!40000 ALTER TABLE `relative_information`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `relative_information`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `relative_type`
--

DROP TABLE IF EXISTS `relative_type`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `relative_type`
(
    `type_id` bigint NOT NULL,
    `name`    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`type_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `relative_type`
--

LOCK TABLES `relative_type` WRITE;
/*!40000 ALTER TABLE `relative_type`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `relative_type`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role`
(
    `role_id` bigint NOT NULL AUTO_INCREMENT,
    `role`    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`role_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role`
    DISABLE KEYS */;
INSERT INTO `role`
VALUES (1, 'ADMIN'),
       (2, 'MANAGER'),
       (3, 'USER');
/*!40000 ALTER TABLE `role`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_type`
--

DROP TABLE IF EXISTS `role_type`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_type`
(
    `type_id` bigint NOT NULL AUTO_INCREMENT,
    `role`    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`type_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_type`
--

LOCK TABLES `role_type` WRITE;
/*!40000 ALTER TABLE `role_type`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `role_type`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `salary`
--

DROP TABLE IF EXISTS `salary`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `salary`
(
    `laudatory_id`           bigint NOT NULL,
    `end_date`               date                                    DEFAULT NULL,
    `final_salary`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `start_date`             date                                    DEFAULT NULL,
    `subsidize`              varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `working_information_id` bigint                                  DEFAULT NULL,
    PRIMARY KEY (`laudatory_id`),
    KEY `FKcufgqt5je8savk7883du8yjl9` (`working_information_id`),
    CONSTRAINT `FKcufgqt5je8savk7883du8yjl9` FOREIGN KEY (`working_information_id`) REFERENCES `working_information` (`working_information_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `salary`
--

LOCK TABLES `salary` WRITE;
/*!40000 ALTER TABLE `salary`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `salary`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `work_contract`
--

DROP TABLE IF EXISTS `work_contract`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_contract`
(
    `contract_id`    varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `end_date`       date                                    DEFAULT NULL,
    `name`           varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `representative` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `start_date`     date                                    DEFAULT NULL,
    `status`         varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`contract_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `work_contract`
--

LOCK TABLES `work_contract` WRITE;
/*!40000 ALTER TABLE `work_contract`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `work_contract`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `working_contract`
--

DROP TABLE IF EXISTS `working_contract`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `working_contract`
(
    `working_contract_id` bigint NOT NULL,
    `base_salary`         varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `company_name`        varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `end_date`            date                                    DEFAULT NULL,
    `start_date`          date                                    DEFAULT NULL,
    `status`              varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `type_id`             bigint                                  DEFAULT NULL,
    `employee_id`         varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`working_contract_id`),
    KEY `FKm8lgm02fo7lbyk17tngsgw3h5` (`type_id`),
    KEY `FKsex4ws8s50gfamj8lgmweqgwr` (`employee_id`),
    CONSTRAINT `FKm8lgm02fo7lbyk17tngsgw3h5` FOREIGN KEY (`type_id`) REFERENCES `contract_type` (`type_id`),
    CONSTRAINT `FKsex4ws8s50gfamj8lgmweqgwr` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `working_contract`
--

LOCK TABLES `working_contract` WRITE;
/*!40000 ALTER TABLE `working_contract`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `working_contract`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `working_history`
--

DROP TABLE IF EXISTS `working_history`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `working_history`
(
    `working_history_id` bigint NOT NULL,
    `company_name`       varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `end_date`           date                                    DEFAULT NULL,
    `start_date`         date                                    DEFAULT NULL,
    `type_id`            bigint                                  DEFAULT NULL,
    `employee_id`        varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`working_history_id`),
    KEY `FK6qaysc7f06g54a5xuyd150b6v` (`type_id`),
    KEY `FKifvk8c7onm47vh7rbdm6r7kun` (`employee_id`),
    CONSTRAINT `FK6qaysc7f06g54a5xuyd150b6v` FOREIGN KEY (`type_id`) REFERENCES `contract_type` (`type_id`),
    CONSTRAINT `FKifvk8c7onm47vh7rbdm6r7kun` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `working_history`
--

LOCK TABLES `working_history` WRITE;
/*!40000 ALTER TABLE `working_history`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `working_history`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `working_information`
--

DROP TABLE IF EXISTS `working_information`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `working_information`
(
    `working_information_id` bigint NOT NULL AUTO_INCREMENT,
    `recent_promotion`       varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `area_id`                bigint                                  DEFAULT NULL,
    `employee_id`            varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `job_id`                 bigint                                  DEFAULT NULL,
    `office_id`              bigint                                  DEFAULT NULL,
    `contract_id`            varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `end_date`               date                                    DEFAULT NULL,
    `start_date`             date                                    DEFAULT NULL,
    `working_contract_id`    bigint                                  DEFAULT NULL,
    PRIMARY KEY (`working_information_id`),
    KEY `FKq2cvtxao1wj50nd64aaon78jn` (`area_id`),
    KEY `FKq46oy1tdgvxrgnb9nny8fhtuo` (`employee_id`),
    KEY `FKo27chcpaxtqqroa8yjmejnee` (`job_id`),
    KEY `FKqtdqt0fyxv2hixajsqq6hdoo4` (`office_id`),
    KEY `FKh7tlqlj45n1e1iwcqu4mode8` (`contract_id`),
    KEY `FKpwt7lucj88wg8odl56cb82h29` (`working_contract_id`),
    CONSTRAINT `FKh7tlqlj45n1e1iwcqu4mode8` FOREIGN KEY (`contract_id`) REFERENCES `work_contract` (`contract_id`),
    CONSTRAINT `FKo27chcpaxtqqroa8yjmejnee` FOREIGN KEY (`job_id`) REFERENCES `job` (`job_id`),
    CONSTRAINT `FKpwt7lucj88wg8odl56cb82h29` FOREIGN KEY (`working_contract_id`) REFERENCES `working_contract` (`working_contract_id`),
    CONSTRAINT `FKq2cvtxao1wj50nd64aaon78jn` FOREIGN KEY (`area_id`) REFERENCES `area` (`area_id`),
    CONSTRAINT `FKq46oy1tdgvxrgnb9nny8fhtuo` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`),
    CONSTRAINT `FKqtdqt0fyxv2hixajsqq6hdoo4` FOREIGN KEY (`office_id`) REFERENCES `office` (`office_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `working_information`
--

LOCK TABLES `working_information` WRITE;
/*!40000 ALTER TABLE `working_information`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `working_information`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `working_place`
--

DROP TABLE IF EXISTS `working_place`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `working_place`
(
    `working_place_id`    bigint NOT NULL,
    `area_id`             bigint DEFAULT NULL,
    `job_id`              bigint DEFAULT NULL,
    `office_id`           bigint DEFAULT NULL,
    `working_contract_id` bigint DEFAULT NULL,
    PRIMARY KEY (`working_place_id`),
    KEY `FKl7p5df5v5ogy5t8kcmxtt10xn` (`area_id`),
    KEY `FKji3m1jnm02ymvmhv5gt72akd2` (`job_id`),
    KEY `FK6h05fawo00dapm4d8t2fs4ca3` (`office_id`),
    KEY `FKmsdabpyr5vht6g61nopmud2d2` (`working_contract_id`),
    CONSTRAINT `FK6h05fawo00dapm4d8t2fs4ca3` FOREIGN KEY (`office_id`) REFERENCES `office` (`office_id`),
    CONSTRAINT `FKji3m1jnm02ymvmhv5gt72akd2` FOREIGN KEY (`job_id`) REFERENCES `job` (`job_id`),
    CONSTRAINT `FKl7p5df5v5ogy5t8kcmxtt10xn` FOREIGN KEY (`area_id`) REFERENCES `area` (`area_id`),
    CONSTRAINT `FKmsdabpyr5vht6g61nopmud2d2` FOREIGN KEY (`working_contract_id`) REFERENCES `working_contract` (`working_contract_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `working_place`
--

LOCK TABLES `working_place` WRITE;
/*!40000 ALTER TABLE `working_place`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `working_place`
    ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2022-05-29 18:02:40