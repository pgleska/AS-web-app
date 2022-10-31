-- phpMyAdmin SQL Dump
-- version 4.6.6deb5ubuntu0.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Apr 16, 2021 at 02:22 PM
-- Server version: 5.7.32-0ubuntu0.18.04.1
-- PHP Version: 7.2.24-0ubuntu0.18.04.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bettercoach`
--

-- --------------------------------------------------------

--
-- Table structure for table `application_user`
--
CREATE TABLE `application_user` (
  `id` int(10) UNSIGNED NOT NULL,
  `email` varchar(255) COLLATE utf8_bin NOT NULL,
  `password` varchar(255) COLLATE utf8_bin NOT NULL,
  `first_name` varchar(255) COLLATE utf8_bin NOT NULL,
  `last_name` varchar(255) COLLATE utf8_bin NOT NULL,
  `enabled` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Table structure for table `verification_token`
--
CREATE TABLE `verification_token` (
  `id` int(10) UNSIGNED NOT NULL,
  `user_id` int(10) UNSIGNED NOT NULL,
  `token` varchar(255) COLLATE utf8_bin NOT NULL,
  `expiry_date` timestamp DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Table structure for table `tournament`
--
CREATE TABLE `tournament` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(255) COLLATE utf8_bin NOT NULL,
  `discipline` varchar(255) COLLATE utf8_bin NOT NULL,
  `starting_date` date DEFAULT NULL,
  `latitude` float(10) UNSIGNED NOT NULL,
  `longitude` float(10) UNSIGNED NOT NULL,
  `participants` int(10) UNSIGNED NOT NULL,
  `max_number_of_participants` int(10) UNSIGNED NOT NULL,
  `organizer_id` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Table structure for table `tournament_participant`
--
CREATE TABLE `tournament_participant` (
  `tournament_id` int(10) UNSIGNED NOT NULL,
  `participant_id` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Table structure for table `point`
--
CREATE TABLE `point` (
  `id` int(10) UNSIGNED NOT NULL,
  `tournament_id` int(10) UNSIGNED NOT NULL,
  `participant_id` int(10) UNSIGNED NOT NULL,
  `license_number` int(10) UNSIGNED NOT NULL,
  `ranking_position` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- --------------------------------------------------------

--
-- Indexes for dumped tables
--

--
-- Indexes for table `application_user`
--
ALTER TABLE `application_user`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `verification_token`
--
ALTER TABLE `verification_token`
  ADD PRIMARY KEY (`id`);


--
-- Indexes for table `tournament`
--
ALTER TABLE `tournament`
  ADD PRIMARY KEY (`id`);
  
--
-- Indexes for table `tournament_participant`
--
ALTER TABLE `tournament_participant`
  ADD PRIMARY KEY (`tournament_id`,`participant_id`);


--
-- Indexes for table `tournament_participant`
--
ALTER TABLE `point`
  ADD PRIMARY KEY (`id`);  
-- --------------------------------------------------------

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `application_user`
--
ALTER TABLE `application_user`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `verification_token`
--
ALTER TABLE `verification_token`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;


--
-- AUTO_INCREMENT for table `tournament`
--
ALTER TABLE `tournament`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
  
--
-- AUTO_INCREMENT for table `point`
--
ALTER TABLE `point`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;  
-- --------------------------------------------------------

--
-- Constraints for dumped tables
--

--
-- Constraints for table `verification_token`
--
ALTER TABLE `verification_token`
  ADD CONSTRAINT `token_user_fkey` FOREIGN KEY (`user_id`) REFERENCES `application_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `tournament`
--
ALTER TABLE `tournament`
  ADD CONSTRAINT `tournament_user_fkey` FOREIGN KEY (`organizer_id`) REFERENCES `application_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
  
--
-- Constraints for table `tournament_participant`
--
ALTER TABLE `tournament_participant`
  ADD CONSTRAINT `tournament_participant_tournament_fkey` FOREIGN KEY (`tournament_id`) REFERENCES `tournament` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `tournament_participant_participant_fkey` FOREIGN KEY (`participant_id`) REFERENCES `application_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `point`
--
ALTER TABLE `point`
  ADD CONSTRAINT `point_tournament_fkey` FOREIGN KEY (`tournament_id`) REFERENCES `tournament` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `point_participant_fkey` FOREIGN KEY (`participant_id`) REFERENCES `application_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
  
-- --------------------------------------------------------

INSERT INTO `application_user`(`id`, `email`, `password`, `first_name`, `last_name`, `enabled`) VALUES
(1, 'jkowalski@gmail.com', '$2a$10$FhM/tff7C6DU5PA87BdgR.spZS7l6h0LYH2WDd7KchD6nayELNdzK', 'Jan', 'Kowalski', true),
(2, 'anowak@gmail.com', '$2a$10$FhM/tff7C6DU5PA87BdgR.spZS7l6h0LYH2WDd7KchD6nayELNdzK', 'Anna', 'Nowak', true),
(3, 'wgorczak@gmail.com', '$2a$10$FhM/tff7C6DU5PA87BdgR.spZS7l6h0LYH2WDd7KchD6nayELNdzK', 'Wiktor', 'Górczak', true),
(4, 'mszczepaniak@gmail.com', '$2a$10$FhM/tff7C6DU5PA87BdgR.spZS7l6h0LYH2WDd7KchD6nayELNdzK', 'Michał', 'Szczepaniak', true);
  
-- --------------------------------------------------------

CREATE USER 'tournament'@'%' IDENTIFIED BY 'mysecretpassword';
GRANT ALL PRIVILEGES ON *.* TO 'tournament'@'%' WITH GRANT OPTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
