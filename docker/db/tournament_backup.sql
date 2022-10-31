-- MySQL dump 10.13  Distrib 8.0.25, for Linux (x86_64)
--
-- Host: localhost    Database: tournament
-- ------------------------------------------------------
-- Server version	8.0.25

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `application_user`
--

DROP TABLE IF EXISTS `application_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `application_user` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `first_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `last_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application_user`
--

LOCK TABLES `application_user` WRITE;
/*!40000 ALTER TABLE `application_user` DISABLE KEYS */;
INSERT INTO `application_user` VALUES (1,'jkowalski@gmail.com','$2a$10$FhM/tff7C6DU5PA87BdgR.spZS7l6h0LYH2WDd7KchD6nayELNdzK','Jan','Kowalski',1),(2,'anowak@gmail.com','$2a$10$FhM/tff7C6DU5PA87BdgR.spZS7l6h0LYH2WDd7KchD6nayELNdzK','Anna','Nowak',1),(3,'wgorczak@gmail.com','$2a$10$FhM/tff7C6DU5PA87BdgR.spZS7l6h0LYH2WDd7KchD6nayELNdzK','Wiktor','Górczak',1),(4,'mszczepaniak@gmail.com','$2a$10$FhM/tff7C6DU5PA87BdgR.spZS7l6h0LYH2WDd7KchD6nayELNdzK','Michał','Szczepaniak',1);
/*!40000 ALTER TABLE `application_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `point`
--

DROP TABLE IF EXISTS `point`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `point` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `tournament_id` int unsigned NOT NULL,
  `participant_id` int unsigned NOT NULL,
  `license_number` int unsigned NOT NULL,
  `ranking_position` int unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `point_tournament_fkey` (`tournament_id`),
  KEY `point_participant_fkey` (`participant_id`),
  CONSTRAINT `point_participant_fkey` FOREIGN KEY (`participant_id`) REFERENCES `application_user` (`id`),
  CONSTRAINT `point_tournament_fkey` FOREIGN KEY (`tournament_id`) REFERENCES `tournament` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `point`
--

LOCK TABLES `point` WRITE;
/*!40000 ALTER TABLE `point` DISABLE KEYS */;
INSERT INTO `point` VALUES (1,1,1,123,3),(2,1,2,6553,56),(3,1,3,789,56);
/*!40000 ALTER TABLE `point` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tournament`
--

DROP TABLE IF EXISTS `tournament`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tournament` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `discipline` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `starting_date` date DEFAULT NULL,
  `latitude` float NOT NULL,
  `longitude` float NOT NULL,
  `participants` int unsigned NOT NULL,
  `max_number_of_participants` int unsigned NOT NULL,
  `organizer_id` int unsigned NOT NULL,
  `ladder_generated` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `tournament_user_fkey` (`organizer_id`),
  CONSTRAINT `tournament_user_fkey` FOREIGN KEY (`organizer_id`) REFERENCES `application_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tournament`
--

LOCK TABLES `tournament` WRITE;
/*!40000 ALTER TABLE `tournament` DISABLE KEYS */;
INSERT INTO `tournament` VALUES (1,'Alpha','SoloQ','2021-06-25',52.5063,13.4436,3,8,1,false);
/*!40000 ALTER TABLE `tournament` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tournament_participant`
--

DROP TABLE IF EXISTS `tournament_participant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tournament_participant` (
  `tournament_id` int unsigned NOT NULL,
  `participant_id` int unsigned NOT NULL,
  PRIMARY KEY (`tournament_id`,`participant_id`),
  KEY `tournament_participant_participant_fkey` (`participant_id`),
  CONSTRAINT `tournament_participant_participant_fkey` FOREIGN KEY (`participant_id`) REFERENCES `application_user` (`id`),
  CONSTRAINT `tournament_participant_tournament_fkey` FOREIGN KEY (`tournament_id`) REFERENCES `tournament` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tournament_participant`
--

LOCK TABLES `tournament_participant` WRITE;
/*!40000 ALTER TABLE `tournament_participant` DISABLE KEYS */;
INSERT INTO `tournament_participant` VALUES (1,1),(1,2),(1,3);
/*!40000 ALTER TABLE `tournament_participant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `verification_token`
--

DROP TABLE IF EXISTS `verification_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `verification_token` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int unsigned NOT NULL,
  `token` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `expiry_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `token_user_fkey` (`user_id`),
  CONSTRAINT `token_user_fkey` FOREIGN KEY (`user_id`) REFERENCES `application_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `verification_token`
--

LOCK TABLES `verification_token` WRITE;
/*!40000 ALTER TABLE `verification_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `verification_token` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

CREATE TABLE `game` (
  `id` int(10) UNSIGNED NOT NULL,
  `game_number` int(10) UNSIGNED NOT NULL,
  `tournament_id` int(10) UNSIGNED NOT NULL,
  `first_player_id` int(10) UNSIGNED,
  `second_player_id` int(10) UNSIGNED,
  `winner_player_id` int(10) UNSIGNED,
  `old_one_id` int(10) UNSIGNED,
  `old_two_id` int(10) UNSIGNED
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

ALTER TABLE `game`
  ADD PRIMARY KEY (`id`); 
  
ALTER TABLE `game`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;    
  
ALTER TABLE `game`
  ADD CONSTRAINT `game_tournament_fkey` FOREIGN KEY (`tournament_id`) REFERENCES `tournament` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `game_player_one_fkey` FOREIGN KEY (`first_player_id`) REFERENCES `application_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `game_player_two_fkey` FOREIGN KEY (`second_player_id`) REFERENCES `application_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `game_winner_fkey` FOREIGN KEY (`winner_player_id`) REFERENCES `application_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

CREATE USER 'tournament'@'%' IDENTIFIED BY 'mysecretpassword';
GRANT ALL PRIVILEGES ON *.* TO 'tournament'@'%' WITH GRANT OPTION;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-06-28 10:55:39
