
-- Create database for Civil Society project --

DROP DATABASE IF EXISTS `civs`;
CREATE DATABASE `civs`
       DEFAULT CHARACTER SET `utf8`
       DEFAULT COLLATE `utf8_unicode_ci`;

USE `civs`;

DROP TABLE IF EXISTS `assistances`;
DROP TABLE IF EXISTS `assistance_groups`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `reg_options`;

CREATE TABLE `reg_options` (
  `id` bigint(20) NOT NULL,
  `code` char(5) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB COMMENT="Secret code for registration";

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL auto_increment,
  `create_time` timestamp NOT NULL default NOW(),
  `login` varchar(64) NOT NULL,
  `passwd` varchar(32) NOT NULL COLLATE utf8_bin,
  `role` char(5) NOT NULL default 'USER',
  `enabled` bool NOT NULL default 1,
  `options` varchar(2048) default '{}',
  PRIMARY KEY  (`id`),
  CONSTRAINT `u_uni_login` UNIQUE KEY (`login`)
) ENGINE=InnoDB COMMENT="Registered users";

CREATE TABLE `assistance_groups` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(32) NOT NULL,
  `read_only` bool NOT NULL default 0,
  PRIMARY KEY  (`id`),
  CONSTRAINT `u_ag_name` UNIQUE KEY (`name`)
) ENGINE=InnoDB COMMENT="Groups of assistance";

CREATE TABLE `assistances` (
  `id` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) NOT NULL,
  `create_time` timestamp NOT NULL default NOW(),
  `group_id` bigint(20) NOT NULL,
  `description` varchar(1024) NOT NULL,
  `approved` bool NOT NULL default 0,
  `enabled` bool NOT NULL default 1,
  PRIMARY KEY  (`id`),
  CONSTRAINT `a_user_key` FOREIGN KEY (`user_id`) REFERENCES users(`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `a_group_key` FOREIGN KEY (`group_id`) REFERENCES assistance_groups(`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB COMMENT="Assistances (kind of help) from registered users";

