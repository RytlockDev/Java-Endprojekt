-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.41 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.12.0.7122
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for kosten
DROP DATABASE IF EXISTS `kosten`;
CREATE DATABASE IF NOT EXISTS `kosten` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `kosten`;

-- Dumping structure for table kosten.kostenart
DROP TABLE IF EXISTS `kostenart`;
CREATE TABLE IF NOT EXISTS `kostenart` (
  `ka_id` int NOT NULL AUTO_INCREMENT,
  `beschreibung` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `einzelverguetung` decimal(14,2) DEFAULT NULL,
  PRIMARY KEY (`ka_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table kosten.kostenart: ~4 rows (approximately)
DELETE FROM `kostenart`;
INSERT INTO `kostenart` (`ka_id`, `beschreibung`, `einzelverguetung`) VALUES
	(1, 'Tagesgeld', 46.40),
	(2, 'Uebernachtung', 100.00),
	(3, 'Fahrtkosten', 0.35),
	(4, 'Sontiges', 100.00);

-- Dumping structure for table kosten.mitarbeiter
DROP TABLE IF EXISTS `mitarbeiter`;
CREATE TABLE IF NOT EXISTS `mitarbeiter` (
  `pers_id` int NOT NULL AUTO_INCREMENT,
  `nachname` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `vorname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `strasse` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `plz` int NOT NULL,
  `ort` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`pers_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table kosten.mitarbeiter: ~4 rows (approximately)
DELETE FROM `mitarbeiter`;
INSERT INTO `mitarbeiter` (`pers_id`, `nachname`, `vorname`, `strasse`, `plz`, `ort`) VALUES
	(1, 'Franz', 'Mueller', 'Fransiskaner-Kloster-Strasse 44', 50015, 'Bayern'),
	(2, 'Sabine', 'Friedrich', 'Hauptstrasse 23', 1002, 'Berlin'),
	(3, 'Heinz-Gerhard', 'Stellzig', 'Isernhagenerstarsse 23', 30519, 'Hannover'),
	(4, 'Marianne', 'Mueller', 'Im Krebsfeld 23', 30159, 'Hannover');

-- Dumping structure for table kosten.reisekosten
DROP TABLE IF EXISTS `reisekosten`;
CREATE TABLE IF NOT EXISTS `reisekosten` (
  `rechnungsnummer` int NOT NULL AUTO_INCREMENT,
  `datum` date NOT NULL,
  `pers_id` int NOT NULL,
  `ka_id` int NOT NULL,
  `anzahl` int NOT NULL,
  `gesamtverguetung` decimal(14,2) NOT NULL,
  PRIMARY KEY (`rechnungsnummer`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table kosten.reisekosten: ~10 rows (approximately)
DELETE FROM `reisekosten`;
INSERT INTO `reisekosten` (`rechnungsnummer`, `datum`, `pers_id`, `ka_id`, `anzahl`, `gesamtverguetung`) VALUES
	(1, '2025-10-10', 1, 1, 3, 139.20),
	(2, '2025-10-10', 1, 2, 2, 200.00),
	(3, '2025-10-10', 1, 4, 2, 200.00),
	(4, '2025-10-01', 2, 3, 402, 140.70),
	(5, '2025-10-01', 2, 4, 2, 200.00),
	(6, '2025-05-23', 3, 3, 223, 78.05),
	(7, '2025-01-15', 2, 1, 5, 232.00),
	(8, '2025-01-15', 2, 2, 4, 400.00),
	(9, '2025-01-15', 2, 3, 521, 182.35),
	(10, '2025-01-15', 2, 4, 3, 300.00);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
