CREATE USER 'paper_admin'@'localhost' IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON * . * TO 'paper_admin'@'localhost';

drop database if exists papier_picker;
create database papier_picker;
use papier_picker;

ALTER DATABASE papier_picker DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;

CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` char(68) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`)
);

INSERT INTO `users` 
VALUES 
('admin','{bcrypt}$2a$10$IqgHbW.5MWbaZQh9bvlUEOtOiJl9TTONT8qixwuJoBZkZnXkKcxLi',1);




CREATE TABLE `authorities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `authority` varchar(50) NOT NULL,
  UNIQUE KEY `authorities_idx_1` (`username`,`authority`),
  CONSTRAINT `authorities_ibfk_1` FOREIGN KEY (`username`) REFERENCES `users` (`username`),
  PRIMARY KEY (`id`)
);

INSERT INTO `authorities` 
VALUES 
(1,'admin','ROLE_ADMIN');


create TABLE `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
   CONSTRAINT `student_users_connect` FOREIGN KEY (`username`) REFERENCES `users` (`username`),
	PRIMARY KEY (`id`)
);

INSERT INTO `student` 
VALUES 
(1,'Konto','Administratora',null,'admin');

CREATE TABLE `temat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `temat` varchar(50) NOT NULL,
  `profesor_id` int(11) default null,
  `student_id` int(11) DEFAULT NULL unique,
   CONSTRAINT `profesor_temat_connect` FOREIGN KEY (`profesor_id`) REFERENCES `student` (`id`),
   CONSTRAINT `student_temat_connect` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`),
	PRIMARY KEY (`id`)
);

CREATE TABLE `propozycja` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `temat` varchar(50) NOT NULL,
  `profesor_id` int(11) default null,
  `student_id` int(11) DEFAULT NULL,
   CONSTRAINT `profesor_prop_connect` FOREIGN KEY (`profesor_id`) REFERENCES `student` (`id`),
   CONSTRAINT `student_prop_connect` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`),
	PRIMARY KEY (`id`)
);


