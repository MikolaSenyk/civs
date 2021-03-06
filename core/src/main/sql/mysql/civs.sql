
-- Create database for Civil Society project --

DROP DATABASE IF EXISTS `civs`;
CREATE DATABASE `civs`
       DEFAULT CHARACTER SET `utf8`
       DEFAULT COLLATE `utf8_unicode_ci`;

USE `civs`;

DROP TABLE IF EXISTS `letters`;
DROP TABLE IF EXISTS `assistence_group_links`;
DROP TABLE IF EXISTS `assistances`;
DROP TABLE IF EXISTS `prices`;
DROP TABLE IF EXISTS `assistance_groups`;
DROP TABLE IF EXISTS `images`;
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

CREATE TABLE `images` (
  `id` bigint(20) NOT NULL auto_increment,
  `create_time` timestamp NOT NULL default NOW(),
  `user_id` bigint(20) NOT NULL,
  `folder` varchar(64) NOT NULL,
  `ext` char(3) NOT NULL,
  PRIMARY KEY  (`id`),
  CONSTRAINT `img_user_key` FOREIGN KEY (`user_id`) REFERENCES users(`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB COMMENT="Uploaded images by users";

CREATE TABLE `assistance_groups` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(32) NOT NULL,
  `level` int(10) NOT NULL default 1,
  `parent_id` bigint(20),
  PRIMARY KEY  (`id`),
  CONSTRAINT `u_ag_name` UNIQUE KEY (`name`),
  CONSTRAINT `ag_parent_key` FOREIGN KEY (`parent_id`) REFERENCES assistance_groups(`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB COMMENT="Groups of assistance";

CREATE TABLE `prices` (
  `id` bigint(20) NOT NULL auto_increment,
  `group_id` bigint(20) NOT NULL,
  `name` varchar(64) NOT NULL,
  `measure` varchar(16),
  `grade_one` decimal(10,2),
  `grade_two` decimal(10,2),
  `out_of_season` decimal(10,2),
  PRIMARY KEY  (`id`),
  CONSTRAINT `pr_group_key` FOREIGN KEY (`group_id`) REFERENCES assistance_groups(`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB COMMENT="Recommended prices for assistance in groups";

CREATE TABLE `assistances` (
  `id` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) NOT NULL,
  `create_time` timestamp NOT NULL default NOW(),
  `description` varchar(1024) NOT NULL,
  `approved` bool NOT NULL default 0,
  `enabled` bool NOT NULL default 1,
  PRIMARY KEY  (`id`),
  CONSTRAINT `a_user_key` FOREIGN KEY (`user_id`) REFERENCES users(`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB COMMENT="Assistances (kind of help) from registered users";

CREATE TABLE `assistance_group_links` (
  `id` bigint(20) NOT NULL auto_increment,
  `assistance_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  CONSTRAINT `agl_uni` UNIQUE KEY (`assistance_id`, `group_id`),
  CONSTRAINT `agl_assistance_key` FOREIGN KEY (`assistance_id`) REFERENCES assistances(`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `agl_group_key` FOREIGN KEY (`group_id`) REFERENCES assistance_groups(`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB COMMENT="Links between Assistances and groups";

CREATE TABLE `letters` (
  `id` bigint(20) NOT NULL auto_increment,
  `create_time` timestamp NOT NULL default NOW(),
  `from_user_id` bigint(20) NOT NULL,
  `to_user_id` bigint(20) NOT NULL,
  `message` varchar(512) NOT NULL,
  `read` bool NOT NULL default 0,
  PRIMARY KEY  (`id`),
  CONSTRAINT `from_user_key` FOREIGN KEY (`from_user_id`) REFERENCES users(`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `to_user_key` FOREIGN KEY (`to_user_id`) REFERENCES users(`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB COMMENT="Messages of users";
