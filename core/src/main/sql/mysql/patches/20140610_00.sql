-- Inner messaging system --

USE `civs`;

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
