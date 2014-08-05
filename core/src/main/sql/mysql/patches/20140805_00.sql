-- Multi level assistance groups --

USE `civs`;

TRUNCATE TABLE `assistances`;

ALTER TABLE `assistances`
DROP FOREIGN KEY `a_group_key`,
DROP COLUMN `group_id`;

ALTER TABLE `assistance_groups`
DROP COLUMN `read_only`,
ADD COLUMN `level` int(10) NOT NULL default 1,
ADD COLUMN `parent_id` bigint(20),
ADD CONSTRAINT `ag_parent_key` FOREIGN KEY (`parent_id`) REFERENCES assistance_groups(`id`) ON UPDATE CASCADE ON DELETE CASCADE;

CREATE TABLE `assistance_group_links` (
  `id` bigint(20) NOT NULL auto_increment,
  `assistance_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`id`),
  CONSTRAINT `agl_uni` UNIQUE KEY (`assistance_id`, `group_id`),
  CONSTRAINT `agl_assistance_key` FOREIGN KEY (`assistance_id`) REFERENCES assistances(`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `agl_group_key` FOREIGN KEY (`group_id`) REFERENCES assistance_groups(`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB COMMENT="Links between Assistances and groups";
