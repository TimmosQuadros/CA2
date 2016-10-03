-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`Person`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Person` (
  `idPerson` INT NOT NULL,
  `firstName` VARCHAR(45) NULL,
  `lastName` VARCHAR(45) NULL,
  PRIMARY KEY (`idPerson`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Cityinfo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Cityinfo` (
  `idCityinfo` INT NOT NULL,
  `Zip` INT NULL,
  `City` VARCHAR(45) NULL,
  PRIMARY KEY (`idCityinfo`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Company`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Company` (
  `idCompany` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  `description` VARCHAR(45) NULL,
  `cvr` INT NULL,
  `numEmployees` INT NULL,
  `marketValue` DOUBLE NULL,
  PRIMARY KEY (`idCompany`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Address` (
  `idAddress` INT NOT NULL,
  `street` VARCHAR(45) NULL,
  `additionalInfo` VARCHAR(45) NULL,
  `Cityinfo_idCityinfo` INT NOT NULL,
  PRIMARY KEY (`idAddress`),
  INDEX `fk_Address_Cityinfo1_idx` (`Cityinfo_idCityinfo` ASC),
  CONSTRAINT `fk_Address_Cityinfo1`
    FOREIGN KEY (`Cityinfo_idCityinfo`)
    REFERENCES `mydb`.`Cityinfo` (`idCityinfo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Hobby`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Hobby` (
  `idHobby` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  `description` VARCHAR(45) NULL,
  PRIMARY KEY (`idHobby`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Hobby_has_Person`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Hobby_has_Person` (
  `Hobby_idHobby` INT NOT NULL,
  `Person_idPerson` INT NOT NULL,
  PRIMARY KEY (`Hobby_idHobby`, `Person_idPerson`),
  INDEX `fk_Hobby_has_Person_Person1_idx` (`Person_idPerson` ASC),
  INDEX `fk_Hobby_has_Person_Hobby_idx` (`Hobby_idHobby` ASC),
  CONSTRAINT `fk_Hobby_has_Person_Hobby`
    FOREIGN KEY (`Hobby_idHobby`)
    REFERENCES `mydb`.`Hobby` (`idHobby`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Hobby_has_Person_Person1`
    FOREIGN KEY (`Person_idPerson`)
    REFERENCES `mydb`.`Person` (`idPerson`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`InfoEntity`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`InfoEntity` (
  `idInfoEntity` INT NOT NULL,
  `email` VARCHAR(45) NULL,
  `Address_idAddress` INT NOT NULL,
  `Company_idCompany` INT NOT NULL,
  `Person_idPerson` INT NOT NULL,
  PRIMARY KEY (`idInfoEntity`, `Company_idCompany`, `Person_idPerson`),
  INDEX `fk_InfoEntity_Address1_idx` (`Address_idAddress` ASC),
  INDEX `fk_InfoEntity_Company1_idx` (`Company_idCompany` ASC),
  INDEX `fk_InfoEntity_Person1_idx` (`Person_idPerson` ASC),
  CONSTRAINT `fk_InfoEntity_Address1`
    FOREIGN KEY (`Address_idAddress`)
    REFERENCES `mydb`.`Address` (`idAddress`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_InfoEntity_Company1`
    FOREIGN KEY (`Company_idCompany`)
    REFERENCES `mydb`.`Company` (`idCompany`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_InfoEntity_Person1`
    FOREIGN KEY (`Person_idPerson`)
    REFERENCES `mydb`.`Person` (`idPerson`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Phone`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Phone` (
  `idPhone` INT NOT NULL,
  `number` INT NULL,
  `description` VARCHAR(45) NULL,
  `InfoEntity_idInfoEntity` INT NOT NULL,
  PRIMARY KEY (`idPhone`),
  INDEX `fk_Phone_InfoEntity1_idx` (`InfoEntity_idInfoEntity` ASC),
  CONSTRAINT `fk_Phone_InfoEntity1`
    FOREIGN KEY (`InfoEntity_idInfoEntity`)
    REFERENCES `mydb`.`InfoEntity` (`idInfoEntity`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
