-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: db_recommenderpe
-- ------------------------------------------------------
-- Server version	8.0.41

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
-- Table structure for table `tb_preferences`
--

DROP TABLE IF EXISTS `tb_preferences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_preferences` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `city` varchar(30) DEFAULT NULL,
  `complement` varchar(50) DEFAULT NULL,
  `country` varchar(20) DEFAULT NULL,
  `neighborhood` varchar(30) DEFAULT NULL,
  `house_number` int DEFAULT NULL,
  `states` varchar(2) DEFAULT NULL,
  `street` varchar(40) DEFAULT NULL,
  `zip_code` varchar(8) DEFAULT NULL,
  `date` datetime(6) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlfrkw2amdy83t4dc32c19nocr` (`user_id`),
  CONSTRAINT `FKlfrkw2amdy83t4dc32c19nocr` FOREIGN KEY (`user_id`) REFERENCES `tb_users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_preferences`
--

LOCK TABLES `tb_preferences` WRITE;
/*!40000 ALTER TABLE `tb_preferences` DISABLE KEYS */;
INSERT INTO `tb_preferences` VALUES (1,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-08 22:58:23.101821',1),(2,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-08 23:10:12.564374',1),(3,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-08 23:12:52.037612',1),(4,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-08 23:25:29.419771',1),(5,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-08 23:28:37.094455',1),(6,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-08 23:31:26.045171',1),(7,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-08 23:33:45.156776',1),(8,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-08 23:38:32.666225',1),(9,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-08 23:40:46.416840',1),(10,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-08 23:42:41.807860',1),(11,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-09 01:42:15.817070',1),(12,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-09 01:44:13.252608',1),(13,'Olinda',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-09 01:46:22.064359',1),(14,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-09 01:51:33.322617',1),(15,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-09 01:53:53.914712',1),(16,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-09 01:57:28.489972',1),(17,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-09 02:12:29.669149',1),(18,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-09 02:14:30.822235',1),(19,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-09 02:16:59.389064',1),(20,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-09 02:19:22.602814',1),(21,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-10 01:07:47.767066',1),(22,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-10 01:10:49.086464',1),(23,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-10 01:12:49.553872',1),(24,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-10 01:14:55.018812',1),(25,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-10 01:16:54.868229',1),(26,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-10 01:18:50.984220',1),(27,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-10 01:20:39.759526',1),(28,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-10 01:24:56.703466',1),(29,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-10 01:26:31.119496',1),(30,'Recife',NULL,'Brasil','Olinda',90,'PE','Rua curuça','53350350','2025-07-10 01:28:25.397233',1);
/*!40000 ALTER TABLE `tb_preferences` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-13 15:02:27
