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
-- Table structure for table `recommendation_poi`
--

DROP TABLE IF EXISTS `recommendation_poi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recommendation_poi` (
  `recommendation_id` bigint NOT NULL,
  `poi_id` bigint NOT NULL,
  KEY `FKnsqb0tkbr7nn6v823oxsn77gj` (`poi_id`),
  KEY `FKshr2buqyhwd5gv34bsrrbflk7` (`recommendation_id`),
  CONSTRAINT `FKnsqb0tkbr7nn6v823oxsn77gj` FOREIGN KEY (`poi_id`) REFERENCES `tb_pois` (`id`),
  CONSTRAINT `FKshr2buqyhwd5gv34bsrrbflk7` FOREIGN KEY (`recommendation_id`) REFERENCES `tb_recommendation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recommendation_poi`
--

LOCK TABLES `recommendation_poi` WRITE;
/*!40000 ALTER TABLE `recommendation_poi` DISABLE KEYS */;
INSERT INTO `recommendation_poi` VALUES (1,4),(1,36),(1,7),(1,29),(1,30),(2,21),(2,18),(2,35),(2,28),(2,32),(3,2),(3,3),(3,18),(3,21),(3,22),(4,20),(4,8),(4,12),(4,36),(4,14),(5,18),(5,28),(5,32),(5,8),(5,17),(6,20),(6,6),(6,17),(6,10),(6,1),(7,34),(7,29),(7,30),(7,19),(7,5),(8,32),(8,28),(8,11),(8,22),(8,18),(9,20),(9,6),(9,16),(9,17),(9,10),(10,4),(10,36),(10,27),(10,22),(10,26),(11,8),(11,20),(11,27),(11,21),(11,36),(12,6),(12,1),(12,19),(12,17),(12,10),(13,2),(13,3),(13,32),(13,28),(13,18),(14,20),(14,31),(14,6),(14,27),(14,17),(15,26),(15,22),(15,29),(15,30),(15,7),(16,18),(16,32),(16,28),(16,21),(16,4),(17,8),(17,7),(17,26),(17,9),(17,36),(18,34),(18,35),(18,4),(18,32),(18,28),(19,20),(19,8),(19,18),(19,14),(19,28),(20,26),(20,22),(20,34),(20,32),(20,28),(21,20),(21,8),(21,12),(21,36),(21,31),(22,21),(22,34),(22,29),(22,30),(22,19),(23,2),(23,3),(23,18),(23,21),(23,22),(24,20),(24,31),(24,27),(24,8),(24,6),(25,32),(25,28),(25,18),(25,12),(25,34),(26,36),(26,14),(26,7),(26,27),(26,25),(27,6),(27,16),(27,17),(27,10),(27,1),(28,32),(28,28),(28,18),(28,8),(28,36),(29,34),(29,35),(29,4),(29,32),(29,28),(30,4),(30,21),(30,18),(30,22),(30,26);
/*!40000 ALTER TABLE `recommendation_poi` ENABLE KEYS */;
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
