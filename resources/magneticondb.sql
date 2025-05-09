-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: magneticondb
-- ------------------------------------------------------
-- Server version	9.1.0

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
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `volid` varchar(4) NOT NULL,
  `certification` varchar(100) NOT NULL,
  `skills` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`volid`),
  CONSTRAINT `fk_volunteer_admin` FOREIGN KEY (`volid`) REFERENCES `volunteer` (`volid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES ('V002','Leadership Certification','Community Support'),('V009','Community Leadership Certification','Emergency Assistance, Crisis Management, Team Coordination');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attendance`
--

DROP TABLE IF EXISTS `attendance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `attendance` (
  `servid` varchar(4) NOT NULL,
  `volid` varchar(4) NOT NULL,
  `attendid` varchar(4) CHARACTER SET utf8mb3 NOT NULL,
  `date` date NOT NULL,
  `timein` time(4) DEFAULT NULL,
  `timeout` time(6) DEFAULT NULL,
  `attendstat` enum('Present','Absent','','') CHARACTER SET utf8mb3 NOT NULL,
  PRIMARY KEY (`servid`,`volid`,`attendid`),
  UNIQUE KEY `attendid_UNIQUE` (`attendid`),
  KEY `fk_volunteer_idx` (`volid`),
  CONSTRAINT `fk_service_attendance` FOREIGN KEY (`servid`) REFERENCES `service` (`servid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_volunteer_attendance` FOREIGN KEY (`volid`) REFERENCES `volunteer` (`volid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attendance`
--

LOCK TABLES `attendance` WRITE;
/*!40000 ALTER TABLE `attendance` DISABLE KEYS */;
INSERT INTO `attendance` VALUES ('S001','V001','A001','2025-03-18','07:30:00.0000','15:32:00.000000','Present'),('S001','V008','A002','2025-03-18','00:00:00.0000','00:00:00.000000','Absent'),('S002','V003','A003','2025-01-22','06:42:00.0000','16:30:00.000000','Present'),('S002','V010','A004','2025-01-22','06:30:00.0000','16:30:00.000000','Present'),('S002','V010','A005','2025-01-23','06:30:00.0000','16:30:00.000000','Present'),('S004','V006','A006','2025-03-01','00:00:00.0000','00:00:00.000000','Absent'),('S004','V013','A007','2025-03-01','10:00:00.0000','00:00:00.000000','Present'),('S005','V007','A008','2024-08-14','07:30:00.0000','00:00:00.000000','Present'),('S005','V007','A009','2024-08-14','07:30:00.0000','00:00:00.000000','Present'),('S005','V014','A010','2024-08-14','07:38:00.0000','00:00:00.000000','Present'),('S006','V001','A011','2025-03-21','00:00:00.0000','00:00:00.000000','Absent'),('S006','V008','A012','2025-03-21','09:42:00.0000','16:32:00.000000','Present'),('S008','V004','A013','2024-10-16','08:32:00.0000','15:32:00.000000','Present'),('S008','V004','A014','2024-10-17','08:32:00.0000','15:32:00.000000','Present'),('S008','V012','A015','2024-10-16','08:35:00.0000','15:32:00.000000','Present'),('S008','V012','A016','2024-10-17','08:32:00.0000','15:32:00.000000','Present'),('S010','V007','A017','2025-03-20','06:42:00.0000','16:30:00.000000','Present'),('S010','V014','A018','2025-03-20','00:00:00.0000','00:00:00.000000','Absent'),('S011','V006','A019','2024-01-23','00:00:00.0000','00:00:00.000000','Absent'),('S011','V013','A020','2024-01-23','06:30:00.0000','16:30:00.000000','Present'),('S013','V004','A021','2025-03-22','09:42:00.0000','00:00:00.000000','Present'),('S013','V012','A022','2025-03-22','08:42:00.0000','00:00:00.000000','Present'),('S013','V012','A023','2025-03-22','08:42:00.0000','00:00:00.000000','Present'),('S015','V003','A024','2023-04-15','00:00:00.0000','00:00:00.000000','Absent'),('S015','V003','A025','2023-04-15','00:00:00.0000','00:00:00.000000','Absent'),('S015','V010','A026','2023-04-16','06:42:00.0000','16:30:00.000000','Present');
/*!40000 ALTER TABLE `attendance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `beneficiary`
--

DROP TABLE IF EXISTS `beneficiary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `beneficiary` (
  `servid` varchar(4) NOT NULL,
  `benid` varchar(4) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`servid`,`benid`),
  KEY `fk_beneficiary_groups_beneficiary_idx` (`benid`),
  CONSTRAINT `fk_beneficiary_groups_beneficiary` FOREIGN KEY (`benid`) REFERENCES `beneficiary_groups` (`benid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_service_beneficiary` FOREIGN KEY (`servid`) REFERENCES `service` (`servid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `beneficiary`
--

LOCK TABLES `beneficiary` WRITE;
/*!40000 ALTER TABLE `beneficiary` DISABLE KEYS */;
INSERT INTO `beneficiary` VALUES ('S001','B001'),('S002','B002'),('S003','B003'),('S004','B004'),('S005','B005'),('S001','B006'),('S006','B006'),('S002','B007'),('S007','B007'),('S003','B008'),('S008','B008'),('S004','B009'),('S009','B009'),('S005','B010'),('S010','B010'),('S011','B011'),('S012','B012'),('S013','B013'),('S014','B014'),('S015','B015');
/*!40000 ALTER TABLE `beneficiary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `beneficiary_groups`
--

DROP TABLE IF EXISTS `beneficiary_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `beneficiary_groups` (
  `benid` varchar(4) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `bengroup` varchar(100) NOT NULL,
  `bendesc` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`benid`),
  UNIQUE KEY `bengroup_UNIQUE` (`bengroup`),
  UNIQUE KEY `bendesc_UNIQUE` (`bendesc`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `beneficiary_groups`
--

LOCK TABLES `beneficiary_groups` WRITE;
/*!40000 ALTER TABLE `beneficiary_groups` DISABLE KEYS */;
INSERT INTO `beneficiary_groups` VALUES ('B001','Community Health','Beneficiaries receiving free health checkups.'),('B002','Food Insecure Families','Families in need of regular food supplies.'),('B003','Disaster Victims','People affected by natural disasters.'),('B004','Underprivileged Students','Students receiving educational support.'),('B005','Environmental Volunteers','Participants in clean-up drives.'),('B006','Blood Donors','Regular blood donors supporting hospitals.'),('B007','Clothing Aid Recipients','People receiving clothing donations.'),('B008','Animal Rescue Groups','Organizations caring for rescued animals.'),('B009','Senior Citizens','Elderly individuals receiving community support.'),('B010','Marginalized Communities','Individuals receiving free legal aid.'),('B011','Mental Health Advocates','Groups promoting mental health awareness.'),('B012','Youth Leaders','Young individuals in leadership training.'),('B013','Green Warriors','Groups participating in tree planting.'),('B014','Water Access Projects','Communities receiving clean water support.'),('B015','Vocational Learners','Adults learning new skills for employment.');
/*!40000 ALTER TABLE `beneficiary_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `volid` varchar(4) NOT NULL,
  `availability` enum('Full-Time','Part-Time','Flexible') DEFAULT NULL,
  PRIMARY KEY (`volid`),
  CONSTRAINT `fk_volunteer_member` FOREIGN KEY (`volid`) REFERENCES `volunteer` (`volid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES ('V001','Part-Time'),('V003','Part-Time'),('V004','Full-Time'),('V005','Part-Time'),('V006','Flexible'),('V007','Part-Time'),('V008','Part-Time'),('V010','Part-Time'),('V011','Full-Time'),('V012','Flexible'),('V013','Part-Time'),('V014','Part-Time'),('V015','Part-Time');
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service`
--

DROP TABLE IF EXISTS `service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service` (
  `servid` varchar(4) NOT NULL,
  `teamid` varchar(4) NOT NULL,
  `sname` varchar(100) NOT NULL,
  `sdesc` varchar(100) NOT NULL,
  `sstat` enum('Completed','Ongoing','Assigned') NOT NULL,
  PRIMARY KEY (`servid`),
  KEY `fk_team_service_idx` (`teamid`),
  CONSTRAINT `fk_team_service` FOREIGN KEY (`teamid`) REFERENCES `team` (`teamid`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service`
--

LOCK TABLES `service` WRITE;
/*!40000 ALTER TABLE `service` DISABLE KEYS */;
INSERT INTO `service` VALUES ('S001','T001','Food Distribution','Distributes food and meals to underprivileged communities.','Completed'),('S002','T002','Health Checkup','Provides free health checkups and consultations for communities.','Ongoing'),('S003','T003','Disaster Relief','Provides emergency relief and aid during natural disasters.','Assigned'),('S004','T004','Education Support','Offers tutoring and educational support for underprivileged students.','Ongoing'),('S005','T005','Clean Up Drive','Organizes community clean-up drives for environmental awareness.','Completed'),('S006','T001','Blood Donation','Hosts blood donation campaigns to support local hospitals.','Ongoing'),('S007','T002','Clothing Drive','Collects and distributes clothes to those in need.','Assigned'),('S008','T003','Animal Care','Provides care and shelter for rescued animals.','Completed'),('S009','T004','Vocational Training','Offers skill development and vocational training for adults.','Assigned'),('S010','T005','Legal Aid','Provides free legal assistance to marginalized communities.','Ongoing'),('S011','T004','Mental Health Support','Organizes workshops and provides resources for mental health.','Completed'),('S012','T005','Youth Empowerment','Offers leadership training and mentorship to young individuals.','Assigned'),('S013','T003','Tree Planting','Organizes tree planting events to promote environmental conservation.','Ongoing'),('S014','T001','Water for All','Provides access to clean water in remote and underserved areas.','Assigned'),('S015','T002','Elderly Support','Organizes events and provides resources for elderly care.','Completed');
/*!40000 ALTER TABLE `service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service_schedule`
--

DROP TABLE IF EXISTS `service_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_schedule` (
  `servid` varchar(4) NOT NULL,
  `schedid` varchar(10) CHARACTER SET utf8mb3 NOT NULL,
  `sstat` enum('','Completed','Not Assigned','Assigned') DEFAULT NULL,
  `sstart` date DEFAULT NULL,
  `send` date DEFAULT NULL,
  PRIMARY KEY (`servid`,`schedid`),
  UNIQUE KEY `schedid_UNIQUE` (`schedid`),
  CONSTRAINT `fk_service_service_schedule` FOREIGN KEY (`servid`) REFERENCES `service` (`servid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_schedule`
--

LOCK TABLES `service_schedule` WRITE;
/*!40000 ALTER TABLE `service_schedule` DISABLE KEYS */;
INSERT INTO `service_schedule` VALUES ('S001','SS001','Assigned','2025-03-18','0000-00-00'),('S001','SS016','Assigned','2025-03-26','0000-00-00'),('S002','SS002','Completed','2025-01-22','2025-01-23'),('S002','SS017','Completed','2024-01-22','2024-01-23'),('S003','SS003','Not Assigned','0000-00-00','0000-00-00'),('S003','SS018','Not Assigned','0000-00-00','0000-00-00'),('S004','SS004','Assigned','2025-03-01','0000-00-00'),('S004','SS019','Not Assigned','0000-00-00','0000-00-00'),('S005','SS005','Completed','2024-08-14','2024-08-14'),('S005','SS020','Completed','2024-09-14','2024-09-14'),('S006','SS006','Assigned','2025-03-21','0000-00-00'),('S007','SS007','Not Assigned','0000-00-00','0000-00-00'),('S008','SS008','Completed','2024-10-16','2024-10-17'),('S009','SS009','Not Assigned','0000-00-00','0000-00-00'),('S010','SS010','Assigned','2025-03-20','0000-00-00'),('S011','SS011','Completed','2024-01-23','2024-01-23'),('S012','SS012','Not Assigned','0000-00-00','0000-00-00'),('S013','SS013','Assigned','2025-03-22','0000-00-00'),('S014','SS014','Not Assigned','0000-00-00','0000-00-00'),('S015','SS015','Completed','2023-04-15','2023-04-16');
/*!40000 ALTER TABLE `service_schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_assignment`
--

DROP TABLE IF EXISTS `task_assignment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_assignment` (
  `servid` varchar(4) NOT NULL,
  `volid` varchar(4) NOT NULL,
  `taskstat` enum('Assigned','In Progress','Completed','') CHARACTER SET utf8mb3 NOT NULL,
  `tadesc` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`servid`,`volid`),
  KEY `fk_volunteer_team_idx` (`volid`),
  CONSTRAINT `fk_service_team` FOREIGN KEY (`servid`) REFERENCES `service` (`servid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_volunteer_team` FOREIGN KEY (`volid`) REFERENCES `volunteer` (`volid`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_assignment`
--

LOCK TABLES `task_assignment` WRITE;
/*!40000 ALTER TABLE `task_assignment` DISABLE KEYS */;
INSERT INTO `task_assignment` VALUES ('S001','V001','In Progress','Assisting in distributing food packages to the community.'),('S001','V008','In Progress','Helping with food distribution logistics and coordination.'),('S002','V003','Completed','Assisting in setting up and conducting basic health screenings.'),('S002','V010','Completed','Patient registration.'),('S004','V006','In Progress','Helping facilitate educational sessions for children.'),('S004','V013','In Progress','Assisting with tutoring and educational materials distribution.'),('S005','V007','Completed','Participating in a community clean-up drive.'),('S005','V014','Completed','Collecting and disposing of waste in designated areas.'),('S006','V001','In Progress','Assisting in organizing a blood donation drive.'),('S006','V008','In Progress','Managing donor registration and post-donation care.'),('S008','V004','Completed','Helping with feeding and care of rescued animals.'),('S008','V012','Completed','Assisting in cleaning and maintaining animal shelters.'),('S010','V007','In Progress','Supporting legal aid sessions for underserved communities.'),('S010','V014','In Progress','Assisting in legal documentation and client coordination.'),('S011','V006','Completed','Helping with mental health awareness sessions.'),('S011','V013','Completed','Providing support for group discussions and activities.'),('S013','V004','In Progress','Planting trees in designated reforestation areas.'),('S013','V012','In Progress','Assisting with watering and maintaining newly planted trees.'),('S015','V003','Completed','Supporting elderly individuals with daily activities.'),('S015','V010','Completed','Assisting in companionship and recreational activities for seniors.');
/*!40000 ALTER TABLE `task_assignment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team`
--

DROP TABLE IF EXISTS `team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `team` (
  `teamid` varchar(4) NOT NULL,
  `tname` varchar(10) NOT NULL,
  `tdesc` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`teamid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team`
--

LOCK TABLES `team` WRITE;
/*!40000 ALTER TABLE `team` DISABLE KEYS */;
INSERT INTO `team` VALUES ('T001','Team 1','United by a common purpose that we strive to accomplish. '),('T002','Team 2','A highly adaptable team.'),('T003','Team 3','A versatile group that shifts roles.'),('T004','Team 4','A team with one heart and soul.'),('T005','Team 5','A team that serves genuinely.');
/*!40000 ALTER TABLE `team` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `volunteer`
--

DROP TABLE IF EXISTS `volunteer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `volunteer` (
  `volid` varchar(4) NOT NULL,
  `lname` varchar(100) NOT NULL,
  `fname` varchar(100) NOT NULL,
  `address` varchar(100) NOT NULL,
  `phone` varchar(11) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `bday` varchar(100) NOT NULL,
  `sex` enum('Male','Female') NOT NULL,
  `volstat` enum('Active','Inactive') NOT NULL,
  `role` enum('Member','Admin') NOT NULL,
  PRIMARY KEY (`volid`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `volunteer`
--

LOCK TABLES `volunteer` WRITE;
/*!40000 ALTER TABLE `volunteer` DISABLE KEYS */;
INSERT INTO `volunteer` VALUES ('V001','Reyes','Maria','123 Mabini St., Manila, PH','9171234567','maria.reyes@email.com','password1','1990-05-15','Female','Active','Member'),('V002','Santos','John','456 Rizal Ave., Cebu, PH','9234567890','john.santos@email.com','password2','1985-03-22','Male','Active','Admin'),('V003','Cruz','Ana','789 Quezon Blvd., Davao, PH','9181234567','ana.cruz@email.com','password3','1993-12-01','Female','Active','Member'),('V004','Garcia','Luis','321 Bonifacio Rd., Baguio, PH','9321234567','luis.garcia@email.com','password4','1988-07-19','Male','Active','Member'),('V005','Aquino','Isabel','234 Lagro St., Quezon City, PH','9151234567','isabel.aquino@email.com','password5','1995-11-10','Female','Inactive','Member'),('V006','Ramos','Carlos','1238 Sampaloc St., Manila, PH','9263456789','carlos.ramos@email.com','password6','1982-04-05','Male','Active','Member'),('V007','Mendoza','Teresa','9875 EDSA, Makati, PH','9322567890','teresa.mendoza@email.com','password7','1992-06-25','Female','Active','Member'),('V008','Fernandez','Miguel','3655 P. Burgos St., Cavite, PH','9421234567','miguel.fernandez@email.com','password8','1989-09-12','Male','Active','Member'),('V009','Lopez','Juan','411 Shaw Blvd., Mandaluyong, PH','9122334456','juan.lopez@email.com','password9','1994-02-02','Male','Active','Admin'),('V010','Torres','Luz','180 Taft Ave., Manila, PH','9175678901','luz.torres@email.com','password10','1987-01-29','Female','Active','Member'),('V011','De La Cruz','Alberto','22A Roxas Blvd., ParaÃ±aque, PH','9314567890','alberto.delacruz@email.com','password11','1990-08-21','Male','Inactive','Member'),('V012','Santiago','Maria','7654 Aguinaldo St., Cavite, PH','9252223334','maria.santiago@email.com','password12','1993-12-12','Female','Active','Member'),('V013','Morales','Felipe','123 San Juan St., QC, PH','9187654321','felipe.morales@email.com','password13','1986-03-04','Male','Active','Member'),('V014','Dela Rosa','Kim','321 Bagumbayan St., Pasig, PH','9451234567','kim.delarosa@email.com','password14','1991-02-28','Female','Active','Member'),('V015','Villanueva','Elias','458 St. Peter Rd., Manila, PH','9192347890','elias.villanueva@email.com','password15','1980-11-11','Male','Inactive','Member');
/*!40000 ALTER TABLE `volunteer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `volunteer_team`
--

DROP TABLE IF EXISTS `volunteer_team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `volunteer_team` (
  `volid` varchar(4) NOT NULL,
  `teamid` varchar(4) NOT NULL,
  `vtdesc` varchar(100) CHARACTER SET utf8mb3 DEFAULT NULL,
  PRIMARY KEY (`volid`,`teamid`),
  KEY `fk_team_volunteer_team_idx` (`teamid`),
  CONSTRAINT `fk_team_volunteer_team` FOREIGN KEY (`teamid`) REFERENCES `team` (`teamid`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_volunteer_volunteer_team` FOREIGN KEY (`volid`) REFERENCES `volunteer` (`volid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `volunteer_team`
--

LOCK TABLES `volunteer_team` WRITE;
/*!40000 ALTER TABLE `volunteer_team` DISABLE KEYS */;
INSERT INTO `volunteer_team` VALUES ('V001','T001',NULL),('V001','T003',NULL),('V003','T002',NULL),('V004','T001',NULL),('V004','T003',NULL),('V006','T004',NULL),('V006','T005',NULL),('V007','T004',NULL),('V007','T005',NULL),('V008','T001',NULL),('V010','T002',NULL),('V012','T003',NULL),('V013','T004',NULL),('V014','T005',NULL);
/*!40000 ALTER TABLE `volunteer_team` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'magneticondb'
--
/*!50003 DROP PROCEDURE IF EXISTS `addbeneficiarygroup` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `addbeneficiarygroup`(benid varchar(4), bengroup varchar(100), bendesc varchar(100))
BEGIN
	IF benid IS NULL OR bengroup IS NULL OR bendesc IS NULL THEN
		SIGNAL SQLSTATE '45000' SET message_text = 
        'Beneficiary ID, Beneficary group and Beneficiary description must not be empty.';
    ELSE 
	INSERT INTO beneficiary_groups values (benid, bengroup, bendesc);
	END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `addteam` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `addteam`(teamid varchar(5), tname varchar(10), tdesc varchar(100))
BEGIN
	IF teamid IS NULL OR tname IS NULL THEN
		SIGNAL SQLSTATE '45000' SET message_text = 
        'Team ID and Team name must not be empty.';
    ELSE 
	INSERT INTO team values (teamid, tname, tdesc);
	END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-10  0:47:53
