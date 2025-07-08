-- Миграция V2: Добавление тестовых администраторов
-- Добавляем дополнительных администраторов для тестирования

-- Администратор системы (основной)
INSERT INTO users (username, email, password, first_name, last_name, role, grade, school, subject, qualification, avatar, created_at, enabled, email_verified) 
VALUES ('admin', 'admin@school.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Администратор', 'Системы', 'ADMIN', NULL, NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP, true, true)
ON DUPLICATE KEY UPDATE 
    email = VALUES(email),
    first_name = VALUES(first_name),
    last_name = VALUES(last_name),
    enabled = VALUES(enabled),
    email_verified = VALUES(email_verified);

-- Тестовый администратор 1
INSERT INTO users (username, email, password, first_name, last_name, role, grade, school, subject, qualification, avatar, created_at, enabled, email_verified) 
VALUES ('admin1', 'admin1@school.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Иван', 'Петров', 'ADMIN', NULL, NULL, NULL, 'Системный администратор', NULL, CURRENT_TIMESTAMP, true, true)
ON DUPLICATE KEY UPDATE 
    email = VALUES(email),
    first_name = VALUES(first_name),
    last_name = VALUES(last_name),
    qualification = VALUES(qualification),
    enabled = VALUES(enabled),
    email_verified = VALUES(email_verified);

-- Тестовый администратор 2
INSERT INTO users (username, email, password, first_name, last_name, role, grade, school, subject, qualification, avatar, created_at, enabled, email_verified) 
VALUES ('admin2', 'admin2@school.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Мария', 'Сидорова', 'ADMIN', NULL, NULL, NULL, 'Администратор образования', NULL, CURRENT_TIMESTAMP, true, true)
ON DUPLICATE KEY UPDATE 
    email = VALUES(email),
    first_name = VALUES(first_name),
    last_name = VALUES(last_name),
    qualification = VALUES(qualification),
    enabled = VALUES(enabled),
    email_verified = VALUES(email_verified);

-- Тестовый администратор 3 (супер-админ)
INSERT INTO users (username, email, password, first_name, last_name, role, grade, school, subject, qualification, avatar, created_at, enabled, email_verified) 
VALUES ('superadmin', 'superadmin@school.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Александр', 'Козлов', 'ADMIN', NULL, NULL, NULL, 'Главный администратор', NULL, CURRENT_TIMESTAMP, true, true)
ON DUPLICATE KEY UPDATE 
    email = VALUES(email),
    first_name = VALUES(first_name),
    last_name = VALUES(last_name),
    qualification = VALUES(qualification),
    enabled = VALUES(enabled),
    email_verified = VALUES(email_verified);

-- Комментарий: Все администраторы используют пароль 'password'
-- Доступные тестовые аккаунты администраторов:
-- admin/password - Администратор Системы
-- admin1/password - Иван Петров (Системный администратор)
-- admin2/password - Мария Сидорова (Администратор образования)
-- superadmin/password - Александр Козлов (Главный администратор) 