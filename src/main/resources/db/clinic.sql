CREATE SCHEMA `clinic` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE `clinic`.`patient` (
  `id` INT NOT NULL AUTO_INCREMENT, PRIMARY KEY (`id`),
  `date` DATETIME NULL ,
  `last_name`  VARCHAR(255) NULL ,
  `name`  VARCHAR(255) NULL ,
  `pesel`  VARCHAR(255) NULL );


INSERT INTO `clinic`.`patient` (`id`, `date`, `last_name`, `name`, `pesel`) VALUES ('1', '1981-03-03', 'Smutny', 'Adam', '8863434346');
INSERT INTO `clinic`.`patient` (`id`, `date`, `last_name`, `name`, `pesel`) VALUES ('2', '1981-04-03', 'Leśny', 'Jan', '4634534534');
INSERT INTO `clinic`.`patient` (`id`, `date`, `last_name`, `name`, `pesel`) VALUES ('3', '1981-05-03', 'Polny', 'Sławomir', '6786785677');
INSERT INTO `clinic`.`patient` (`id`, `date`, `last_name`, `name`, `pesel`) VALUES ('4', '1981-06-03', 'Pochmurny', 'Stafan', '9655445343');
INSERT INTO `clinic`.`patient` (`id`, `date`, `last_name`, `name`, `pesel`) VALUES ('5', '1981-07-03', 'Słoneczna', 'Anna', '6786786786');
INSERT INTO `clinic`.`patient` (`id`, `date`, `last_name`, `name`, `pesel`) VALUES ('6', '1981-08-03', 'Radosna', 'Sylwia', '7235456456');
INSERT INTO `clinic`.`patient` (`id`, `date`, `last_name`, `name`, `pesel`) VALUES ('7', '1981-09-03', 'Piłkarski', 'Tomasz', '8464562252');
INSERT INTO `clinic`.`patient` (`id`, `date`, `last_name`, `name`, `pesel`) VALUES ('8', '1981-10-03', 'Muzykalna', 'Paulina', '9235325235');
INSERT INTO `clinic`.`patient` (`id`, `date`, `last_name`, `name`, `pesel`) VALUES ('9', '1981-11-03', 'Wielki', 'Marek', '7347473466');
INSERT INTO `clinic`.`patient` (`id`, `date`, `last_name`, `name`, `pesel`) VALUES ('10', '1981-12-03', 'Spiewający', 'Adam', '4563434346');