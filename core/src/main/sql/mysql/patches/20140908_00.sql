-- Add table with prices --

USE `civs`;

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
