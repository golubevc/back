-- Миграция V3: Добавление дополнительных тестовых данных
-- Добавляем больше тестовых данных для полноценного тестирования

-- Дополнительные студенты
INSERT INTO users (username, email, password, first_name, last_name, role, grade, school, subject, qualification, avatar, created_at, enabled, email_verified) 
VALUES 
('student6', 'student6@school.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Елена', 'Волкова', 'STUDENT', 8, 'Школа №1', NULL, NULL, NULL, CURRENT_TIMESTAMP, true, true),
('student7', 'student7@school.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Михаил', 'Лебедев', 'STUDENT', 9, 'Школа №2', NULL, NULL, NULL, CURRENT_TIMESTAMP, true, true),
('student8', 'student8@school.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Анна', 'Морозова', 'STUDENT', 10, 'Школа №3', NULL, NULL, NULL, CURRENT_TIMESTAMP, true, true),
('student9', 'student9@school.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Денис', 'Соколов', 'STUDENT', 11, 'Школа №1', NULL, NULL, NULL, CURRENT_TIMESTAMP, true, true),
('student10', 'student10@school.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Кристина', 'Попова', 'STUDENT', 5, 'Школа №4', NULL, NULL, NULL, CURRENT_TIMESTAMP, true, true)
ON DUPLICATE KEY UPDATE 
    email = VALUES(email),
    first_name = VALUES(first_name),
    last_name = VALUES(last_name),
    grade = VALUES(grade),
    school = VALUES(school),
    enabled = VALUES(enabled),
    email_verified = VALUES(email_verified);

-- Дополнительные учителя
INSERT INTO users (username, email, password, first_name, last_name, role, grade, school, subject, qualification, avatar, created_at, enabled, email_verified) 
VALUES 
('teacher5', 'teacher5@school.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Наталья', 'Кузнецова', 'TEACHER', NULL, NULL, 'Биология', 'Высшая категория', NULL, CURRENT_TIMESTAMP, true, true),
('teacher6', 'teacher6@school.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Сергей', 'Соколов', 'TEACHER', NULL, NULL, 'Химия', 'Первая категория', NULL, CURRENT_TIMESTAMP, true, true),
('teacher7', 'teacher7@school.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Татьяна', 'Попова', 'TEACHER', NULL, NULL, 'Литература', 'Высшая категория', NULL, CURRENT_TIMESTAMP, true, true),
('teacher8', 'teacher8@school.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Андрей', 'Васильев', 'TEACHER', NULL, NULL, 'Информатика', 'Первая категория', NULL, CURRENT_TIMESTAMP, true, true)
ON DUPLICATE KEY UPDATE 
    email = VALUES(email),
    first_name = VALUES(first_name),
    last_name = VALUES(last_name),
    subject = VALUES(subject),
    qualification = VALUES(qualification),
    enabled = VALUES(enabled),
    email_verified = VALUES(email_verified);

-- Дополнительные классы
INSERT INTO classes (name, description, grade, teacher_id, active, created_at) 
VALUES 
('8А - Биология', 'Биология для 8 класса А', 8, 15, true, CURRENT_TIMESTAMP),
('9Б - Химия', 'Химия для 9 класса Б', 9, 16, true, CURRENT_TIMESTAMP),
('10А - Литература', 'Литература для 10 класса А', 10, 17, true, CURRENT_TIMESTAMP),
('11Б - Информатика', 'Информатика для 11 класса Б', 11, 18, true, CURRENT_TIMESTAMP),
('6А - Физика', 'Физика для 6 класса А', 6, 9, true, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE 
    name = VALUES(name),
    description = VALUES(description),
    grade = VALUES(grade),
    teacher_id = VALUES(teacher_id),
    active = VALUES(active);

-- Связи дополнительных классов и студентов
INSERT INTO classes_students (class_id, student_id) 
VALUES 
(5, 6), -- Елена в 8А (Биология)
(6, 7), -- Михаил в 9Б (Химия)
(7, 8), -- Анна в 10А (Литература)
(8, 9), -- Денис в 11Б (Информатика)
(9, 10), -- Кристина в 6А (Физика)
(5, 1), -- Алексей также в 8А
(6, 2), -- Мария также в 9Б
(7, 3), -- Дмитрий также в 10А
(8, 4), -- Анна также в 11Б
(9, 5)  -- Сергей также в 6А
ON DUPLICATE KEY UPDATE class_id = VALUES(class_id);

-- Дополнительные уроки
INSERT INTO lessons (title, description, order_number, class_id, video_url, audio_url, image_url, active, created_at) 
VALUES 
('Урок 1: Клетка', 'Изучаем строение клетки', 1, 5, 'https://example.com/videos/cell.mp4', 'https://example.com/audio/cell.mp3', 'https://example.com/images/cell.jpg', true, CURRENT_TIMESTAMP),
('Урок 2: Молекулы', 'Основы молекулярной химии', 1, 6, 'https://example.com/videos/molecules.mp4', 'https://example.com/audio/molecules.mp3', 'https://example.com/images/molecules.jpg', true, CURRENT_TIMESTAMP),
('Урок 1: Поэзия', 'Изучаем русскую поэзию', 1, 7, 'https://example.com/videos/poetry.mp4', 'https://example.com/audio/poetry.mp3', 'https://example.com/images/poetry.jpg', true, CURRENT_TIMESTAMP),
('Урок 1: Программирование', 'Основы программирования на Python', 1, 8, 'https://example.com/videos/python.mp4', NULL, 'https://example.com/images/python.jpg', true, CURRENT_TIMESTAMP),
('Урок 1: Механика', 'Основы механики', 1, 9, 'https://example.com/videos/mechanics.mp4', 'https://example.com/audio/mechanics.mp3', 'https://example.com/images/mechanics.jpg', true, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE 
    title = VALUES(title),
    description = VALUES(description),
    order_number = VALUES(order_number),
    class_id = VALUES(class_id),
    video_url = VALUES(video_url),
    audio_url = VALUES(audio_url),
    image_url = VALUES(image_url),
    active = VALUES(active);

-- Дополнительные слова для новых уроков
INSERT INTO words (word, translation, example, pronunciation, audio_url, image_url, lesson_id, active, created_at) 
VALUES 
-- Слова для урока "Клетка" (Биология)
('Cell', 'Клетка', 'Every living organism is made of cells.', 'sɛl', 'https://example.com/audio/cell_word.mp3', 'https://example.com/images/cell_word.jpg', 7, true, CURRENT_TIMESTAMP),
('Nucleus', 'Ядро', 'The nucleus contains genetic material.', 'ˈnukliəs', 'https://example.com/audio/nucleus_word.mp3', 'https://example.com/images/nucleus_word.jpg', 7, true, CURRENT_TIMESTAMP),
('Membrane', 'Мембрана', 'The cell membrane protects the cell.', 'ˈmɛmbreɪn', 'https://example.com/audio/membrane_word.mp3', 'https://example.com/images/membrane_word.jpg', 7, true, CURRENT_TIMESTAMP),

-- Слова для урока "Молекулы" (Химия)
('Molecule', 'Молекула', 'Water is made of H2O molecules.', 'ˈmɑləkjul', 'https://example.com/audio/molecule_word.mp3', 'https://example.com/images/molecule_word.jpg', 8, true, CURRENT_TIMESTAMP),
('Atom', 'Атом', 'Atoms are the building blocks of matter.', 'ˈætəm', 'https://example.com/audio/atom_word.mp3', 'https://example.com/images/atom_word.jpg', 8, true, CURRENT_TIMESTAMP),
('Chemical', 'Химический', 'Chemical reactions create new substances.', 'ˈkɛmɪkəl', 'https://example.com/audio/chemical_word.mp3', 'https://example.com/images/chemical_word.jpg', 8, true, CURRENT_TIMESTAMP),

-- Слова для урока "Поэзия" (Литература)
('Poetry', 'Поэзия', 'Poetry uses rhythm and rhyme.', 'ˈpoʊətri', 'https://example.com/audio/poetry_word.mp3', 'https://example.com/images/poetry_word.jpg', 9, true, CURRENT_TIMESTAMP),
('Rhyme', 'Рифма', 'Words that rhyme sound similar.', 'raɪm', 'https://example.com/audio/rhyme_word.mp3', 'https://example.com/images/rhyme_word.jpg', 9, true, CURRENT_TIMESTAMP),
('Verse', 'Стих', 'A verse is a line of poetry.', 'vɜrs', 'https://example.com/audio/verse_word.mp3', 'https://example.com/images/verse_word.jpg', 9, true, CURRENT_TIMESTAMP),

-- Слова для урока "Программирование" (Информатика)
('Code', 'Код', 'Programmers write code to create software.', 'koʊd', NULL, 'https://example.com/images/code_word.jpg', 10, true, CURRENT_TIMESTAMP),
('Variable', 'Переменная', 'Variables store data in programs.', 'ˈvɛriəbəl', NULL, 'https://example.com/images/variable_word.jpg', 10, true, CURRENT_TIMESTAMP),
('Function', 'Функция', 'Functions perform specific tasks.', 'ˈfʌŋkʃən', NULL, 'https://example.com/images/function_word.jpg', 10, true, CURRENT_TIMESTAMP),

-- Слова для урока "Механика" (Физика)
('Force', 'Сила', 'Force causes objects to move.', 'fɔrs', 'https://example.com/audio/force_word.mp3', 'https://example.com/images/force_word.jpg', 11, true, CURRENT_TIMESTAMP),
('Motion', 'Движение', 'Motion is the change in position.', 'ˈmoʊʃən', 'https://example.com/audio/motion_word.mp3', 'https://example.com/images/motion_word.jpg', 11, true, CURRENT_TIMESTAMP),
('Energy', 'Энергия', 'Energy cannot be created or destroyed.', 'ˈɛnərdʒi', 'https://example.com/audio/energy_word.mp3', 'https://example.com/images/energy_word.jpg', 11, true, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE 
    word = VALUES(word),
    translation = VALUES(translation),
    example = VALUES(example),
    pronunciation = VALUES(pronunciation),
    audio_url = VALUES(audio_url),
    image_url = VALUES(image_url),
    lesson_id = VALUES(lesson_id),
    active = VALUES(active);

-- Дополнительный прогресс студентов
INSERT INTO student_progress (student_id, word_id, score, attempts, completed, last_attempt_at, completed_at, created_at) 
VALUES 
-- Прогресс Елены (student6) - Биология
(6, 18, 92, 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 19, 88, 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 20, 95, 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Прогресс Михаила (student7) - Химия
(7, 21, 85, 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 22, 90, 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 23, 78, 3, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Прогресс Анны (student8) - Литература
(8, 24, 87, 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 25, 93, 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 26, 89, 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Прогресс Дениса (student9) - Информатика
(9, 27, 91, 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 28, 86, 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 29, 94, 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Прогресс Кристины (student10) - Физика
(10, 30, 82, 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 31, 88, 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 32, 75, 3, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE 
    score = VALUES(score),
    attempts = VALUES(attempts),
    completed = VALUES(completed),
    last_attempt_at = VALUES(last_attempt_at),
    completed_at = VALUES(completed_at);

-- Комментарий: Добавлено 5 новых студентов, 4 новых учителя, 5 новых классов, 5 новых уроков и 15 новых слов
-- Общее количество тестовых данных:
-- - 10 студентов (student1-student10)
-- - 8 учителей (teacher1-teacher8)
-- - 4 администратора (admin, admin1, admin2, superadmin)
-- - 9 классов
-- - 11 уроков
-- - 32 слова
-- - Прогресс для всех студентов 