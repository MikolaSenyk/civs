-- Update length of users.login field as for email and add assistances.is_available field ass on/off feature --

USE `civs`;

ALTER TABLE `users`
MODIFY COLUMN `login` varchar(64) NOT NULL;

ALTER TABLE `assistances`
ADD COLUMN `enabled` bool NOT NULL default 1;
