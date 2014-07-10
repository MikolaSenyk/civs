-- Uploaded images --

USE `civs`;

CREATE TABLE `images` (
  `id` bigint(20) NOT NULL auto_increment,
  `create_time` timestamp NOT NULL default NOW(),
  `user_id` bigint(20) NOT NULL,
  `folder` varchar(64) NOT NULL,
  `ext` char(3) NOT NULL,
  PRIMARY KEY  (`id`),
  CONSTRAINT `img_user_key` FOREIGN KEY (`user_id`) REFERENCES users(`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB COMMENT="Uploaded images by users";
