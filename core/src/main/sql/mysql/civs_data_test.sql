
-- Test data for Civil Sosiety project --

USE `civs`;

SET NAMES utf8;

INSERT INTO `users`
(id, login, passwd, role, enabled) VALUES
(1, 'test', 'admin', 'ADMIN', 1),
(2, 'zap', 'zap', 'USER', 1);

INSERT INTO `assistance_groups` (id, name) VALUES
(2, 'Техніка'),
(3, 'Ліки');
