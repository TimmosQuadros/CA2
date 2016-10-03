-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `mydb` ;

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`cityinfo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`cityinfo` (
  `idCityinfo` INT(11) NOT NULL,
  `City` VARCHAR(255) NULL DEFAULT NULL,
  `Zip` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`idCityinfo`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `mydb`.`address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`address` (
  `idAddress` INT(11) NOT NULL,
  `additionalInfo` VARCHAR(255) NULL DEFAULT NULL,
  `street` VARCHAR(255) NULL DEFAULT NULL,
  `Cityinfo_idCityinfo` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`idAddress`),
  INDEX `FK_address_Cityinfo_idCityinfo` (`Cityinfo_idCityinfo` ASC),
  CONSTRAINT `FK_address_Cityinfo_idCityinfo`
    FOREIGN KEY (`Cityinfo_idCityinfo`)
    REFERENCES `mydb`.`cityinfo` (`idCityinfo`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `mydb`.`infoentity`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`infoentity` (
  `ID` INT(11) NOT NULL,
  `DTYPE` VARCHAR(31) NULL DEFAULT NULL,
  `Company_ID` INT(11) NULL DEFAULT NULL,
  `email` VARCHAR(255) NULL DEFAULT NULL,
  `Person_ID` INT(11) NULL DEFAULT NULL,
  `address_idAddress` INT(11) NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_infoentity_address1_idx` (`address_idAddress` ASC),
  CONSTRAINT `fk_infoentity_address1`
    FOREIGN KEY (`address_idAddress`)
    REFERENCES `mydb`.`address` (`idAddress`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `mydb`.`company`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`company` (
  `ID` INT(11) NOT NULL,
  `cvr` INT(11) NULL DEFAULT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `marketValue` DOUBLE NULL DEFAULT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `numEmployees` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  CONSTRAINT `FK_company_ID`
    FOREIGN KEY (`ID`)
    REFERENCES `mydb`.`infoentity` (`ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `mydb`.`hobby`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`hobby` (
  `idHobby` INT(11) NOT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`idHobby`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `mydb`.`hobby_has_person`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`hobby_has_person` (
  `Hobby_idHobby` INT(11) NOT NULL,
  `Person_ID` INT(11) NOT NULL,
  PRIMARY KEY (`Hobby_idHobby`, `Person_ID`),
  INDEX `FK_hobby_has_person_Person_ID` (`Person_ID` ASC),
  CONSTRAINT `FK_hobby_has_person_Hobby_idHobby`
    FOREIGN KEY (`Hobby_idHobby`)
    REFERENCES `mydb`.`hobby` (`idHobby`),
  CONSTRAINT `FK_hobby_has_person_Person_ID`
    FOREIGN KEY (`Person_ID`)
    REFERENCES `mydb`.`infoentity` (`ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `mydb`.`person`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`person` (
  `ID` INT(11) NOT NULL,
  `firstName` VARCHAR(255) NULL DEFAULT NULL,
  `lastName` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  CONSTRAINT `FK_person_ID`
    FOREIGN KEY (`ID`)
    REFERENCES `mydb`.`infoentity` (`ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `mydb`.`phone`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`phone` (
  `idPhone` INT(11) NOT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `number` INT(11) NULL DEFAULT NULL,
  `InfoEntity_idInfoEntity` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`idPhone`),
  INDEX `FK_phone_InfoEntity_idInfoEntity` (`InfoEntity_idInfoEntity` ASC),
  CONSTRAINT `FK_phone_InfoEntity_idInfoEntity`
    FOREIGN KEY (`InfoEntity_idInfoEntity`)
    REFERENCES `mydb`.`infoentity` (`ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
