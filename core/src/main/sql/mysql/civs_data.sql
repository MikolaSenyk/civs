
-- Insert initial data for Civil Society project --

USE `civs`;

SET NAMES utf8;

INSERT INTO `reg_options` (id, code) VALUES (1,'12345');

INSERT INTO `users`
(login, passwd, full_name, role, enabled) VALUES
('test', 'admin', 'Admin for testing', 'ADMIN', 1);
