-- Пользователи (пароли: password)
INSERT INTO users (username, email, password, first_name, last_name, role, grade, school, subject, qualification, avatar, created_at, enabled, email_verified) 
SELECT 'student1', 'student1@school.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Алексей', 'Иванов', 'STUDENT', 5, 'Школа №1', NULL, NULL, NULL, CURRENT_TIMESTAMP, true, true
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'student1');

INSERT INTO users (username, email, password, first_name, last_name, role, grade, school, subject, qualification, avatar, created_at, enabled, email_verified) 
SELECT 'student2', 'student2@school.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Мария', 'Петрова', 'STUDENT', 6, 'Школа №2', NULL, NULL, NULL, CURRENT_TIMESTAMP, true, true
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'student2');

INSERT INTO users (username, email, password, first_name, last_name, role, grade, school, subject, qualification, avatar, created_at, enabled, email_verified) 
SELECT 'student3', 'student3@school.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Дмитрий', 'Сидоров', 'STUDENT', 7, 'Школа №1', NULL, NULL, NULL, CURRENT_TIMESTAMP, true, true
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'student3');

INSERT INTO users (username, email, password, first_name, last_name, role, grade, school, subject, qualification, avatar, created_at, enabled, email_verified) 
SELECT 'student4', 'student4@school.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Анна', 'Козлова', 'STUDENT', 5, 'Школа №3', NULL, NULL, NULL, CURRENT_TIMESTAMP, true, true
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'student4');

INSERT INTO users (username, email, password, first_name, last_name, role, grade, school, subject, qualification, avatar, created_at, enabled, email_verified) 
SELECT 'student5', 'student5@school.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Сергей', 'Новиков', 'STUDENT', 6, 'Школа №2', NULL, NULL, NULL, CURRENT_TIMESTAMP, true, true
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'student5');

INSERT INTO users (username, email, password, first_name, last_name, role, grade, school, subject, qualification, avatar, created_at, enabled, email_verified) 
SELECT 'teacher1', 'teacher1@school.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Елена', 'Смирнова', 'TEACHER', NULL, NULL, 'Английский язык', 'Высшая категория', NULL, CURRENT_TIMESTAMP, true, true
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'teacher1');

INSERT INTO users (username, email, password, first_name, last_name, role, grade, school, subject, qualification, avatar, created_at, enabled, email_verified) 
SELECT 'teacher2', 'teacher2@school.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Игорь', 'Волков', 'TEACHER', NULL, NULL, 'Математика', 'Первая категория', NULL, CURRENT_TIMESTAMP, true, true
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'teacher2');

INSERT INTO users (username, email, password, first_name, last_name, role, grade, school, subject, qualification, avatar, created_at, enabled, email_verified) 
SELECT 'teacher3', 'teacher3@school.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Ольга', 'Морозова', 'TEACHER', NULL, NULL, 'История', 'Высшая категория', NULL, CURRENT_TIMESTAMP, true, true
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'teacher3');

INSERT INTO users (username, email, password, first_name, last_name, role, grade, school, subject, qualification, avatar, created_at, enabled, email_verified) 
SELECT 'teacher4', 'teacher4@school.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Александр', 'Лебедев', 'TEACHER', NULL, NULL, 'Физика', 'Первая категория', NULL, CURRENT_TIMESTAMP, true, true
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'teacher4');

INSERT INTO users (username, email, password, first_name, last_name, role, grade, school, subject, qualification, avatar, created_at, enabled, email_verified) 
SELECT 'admin', 'admin@school.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Администратор', 'Системы', 'ADMIN', NULL, NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP, true, true
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

-- Классы
INSERT INTO classes (name, description, grade, teacher_id, active, created_at) 
SELECT '5А - Английский язык', 'Английский язык для 5 класса А', 5, 6, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM classes WHERE name = '5А - Английский язык');

INSERT INTO classes (name, description, grade, teacher_id, active, created_at) 
SELECT '6Б - Математика', 'Математика для 6 класса Б', 6, 7, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM classes WHERE name = '6Б - Математика');

INSERT INTO classes (name, description, grade, teacher_id, active, created_at) 
SELECT '7А - История', 'История для 7 класса А', 7, 8, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM classes WHERE name = '7А - История');

INSERT INTO classes (name, description, grade, teacher_id, active, created_at) 
SELECT '5Б - Английский язык', 'Английский язык для 5 класса Б', 5, 6, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM classes WHERE name = '5Б - Английский язык');

-- Связи классов и студентов
INSERT INTO classes_students (class_id, student_id) 
SELECT 1, 1
WHERE NOT EXISTS (SELECT 1 FROM classes_students WHERE class_id = 1 AND student_id = 1);

INSERT INTO classes_students (class_id, student_id) 
SELECT 1, 4
WHERE NOT EXISTS (SELECT 1 FROM classes_students WHERE class_id = 1 AND student_id = 4);

INSERT INTO classes_students (class_id, student_id) 
SELECT 2, 2
WHERE NOT EXISTS (SELECT 1 FROM classes_students WHERE class_id = 2 AND student_id = 2);

INSERT INTO classes_students (class_id, student_id) 
SELECT 2, 5
WHERE NOT EXISTS (SELECT 1 FROM classes_students WHERE class_id = 2 AND student_id = 5);

INSERT INTO classes_students (class_id, student_id) 
SELECT 3, 3
WHERE NOT EXISTS (SELECT 1 FROM classes_students WHERE class_id = 3 AND student_id = 3);

INSERT INTO classes_students (class_id, student_id) 
SELECT 4, 1
WHERE NOT EXISTS (SELECT 1 FROM classes_students WHERE class_id = 4 AND student_id = 1);

INSERT INTO classes_students (class_id, student_id) 
SELECT 4, 4
WHERE NOT EXISTS (SELECT 1 FROM classes_students WHERE class_id = 4 AND student_id = 4);

-- Уроки
INSERT INTO lessons (title, description, order_number, class_id, video_url, audio_url, image_url, active, created_at) 
SELECT 'Урок 1: Приветствие', 'Изучаем основные приветствия на английском языке', 1, 1, 'https://example.com/videos/hello.mp4', 'https://example.com/audio/hello.mp3', 'https://example.com/images/hello.jpg', true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM lessons WHERE title = 'Урок 1: Приветствие' AND class_id = 1);

INSERT INTO lessons (title, description, order_number, class_id, video_url, audio_url, image_url, active, created_at) 
SELECT 'Урок 2: Числа от 1 до 10', 'Изучаем числа от 1 до 10 на английском языке', 2, 1, 'https://example.com/videos/numbers.mp4', 'https://example.com/audio/numbers.mp3', 'https://example.com/images/numbers.jpg', true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM lessons WHERE title = 'Урок 2: Числа от 1 до 10' AND class_id = 1);

INSERT INTO lessons (title, description, order_number, class_id, video_url, audio_url, image_url, active, created_at) 
SELECT 'Урок 1: Сложение', 'Основы сложения чисел', 1, 2, 'https://example.com/videos/addition.mp4', NULL, 'https://example.com/images/addition.jpg', true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM lessons WHERE title = 'Урок 1: Сложение' AND class_id = 2);

INSERT INTO lessons (title, description, order_number, class_id, video_url, audio_url, image_url, active, created_at) 
SELECT 'Урок 2: Вычитание', 'Основы вычитания чисел', 2, 2, 'https://example.com/videos/subtraction.mp4', NULL, 'https://example.com/images/subtraction.jpg', true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM lessons WHERE title = 'Урок 2: Вычитание' AND class_id = 2);

INSERT INTO lessons (title, description, order_number, class_id, video_url, audio_url, image_url, active, created_at) 
SELECT 'Урок 1: Древний мир', 'Введение в историю древнего мира', 1, 3, 'https://example.com/videos/ancient.mp4', 'https://example.com/audio/ancient.mp3', 'https://example.com/images/ancient.jpg', true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM lessons WHERE title = 'Урок 1: Древний мир' AND class_id = 3);

INSERT INTO lessons (title, description, order_number, class_id, video_url, audio_url, image_url, active, created_at) 
SELECT 'Урок 1: Приветствие (5Б)', 'Изучаем основные приветствия на английском языке', 1, 4, 'https://example.com/videos/hello.mp4', 'https://example.com/audio/hello.mp3', 'https://example.com/images/hello.jpg', true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM lessons WHERE title = 'Урок 1: Приветствие (5Б)' AND class_id = 4);

-- Слова
INSERT INTO words (word, translation, example, pronunciation, audio_url, image_url, lesson_id, active, created_at) 
SELECT 'Hello', 'Привет', 'Hello, how are you?', 'həˈloʊ', 'https://example.com/audio/hello_word.mp3', 'https://example.com/images/hello_word.jpg', 1, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM words WHERE word = 'Hello' AND lesson_id = 1);

INSERT INTO words (word, translation, example, pronunciation, audio_url, image_url, lesson_id, active, created_at) 
SELECT 'Goodbye', 'До свидания', 'Goodbye, see you later!', 'ˌɡʊdˈbaɪ', 'https://example.com/audio/goodbye_word.mp3', 'https://example.com/images/goodbye_word.jpg', 1, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM words WHERE word = 'Goodbye' AND lesson_id = 1);

INSERT INTO words (word, translation, example, pronunciation, audio_url, image_url, lesson_id, active, created_at) 
SELECT 'Thank you', 'Спасибо', 'Thank you very much!', 'ˈθæŋk ju', 'https://example.com/audio/thankyou_word.mp3', 'https://example.com/images/thankyou_word.jpg', 1, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM words WHERE word = 'Thank you' AND lesson_id = 1);

INSERT INTO words (word, translation, example, pronunciation, audio_url, image_url, lesson_id, active, created_at) 
SELECT 'Please', 'Пожалуйста', 'Please help me.', 'pliz', 'https://example.com/audio/please_word.mp3', 'https://example.com/images/please_word.jpg', 1, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM words WHERE word = 'Please' AND lesson_id = 1);

INSERT INTO words (word, translation, example, pronunciation, audio_url, image_url, lesson_id, active, created_at) 
SELECT 'One', 'Один', 'I have one apple.', 'wʌn', 'https://example.com/audio/one_word.mp3', 'https://example.com/images/one_word.jpg', 2, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM words WHERE word = 'One' AND lesson_id = 2);

INSERT INTO words (word, translation, example, pronunciation, audio_url, image_url, lesson_id, active, created_at) 
SELECT 'Two', 'Два', 'I see two cats.', 'tu', 'https://example.com/audio/two_word.mp3', 'https://example.com/images/two_word.jpg', 2, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM words WHERE word = 'Two' AND lesson_id = 2);

INSERT INTO words (word, translation, example, pronunciation, audio_url, image_url, lesson_id, active, created_at) 
SELECT 'Three', 'Три', 'There are three books.', 'θri', 'https://example.com/audio/three_word.mp3', 'https://example.com/images/three_word.jpg', 2, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM words WHERE word = 'Three' AND lesson_id = 2);

INSERT INTO words (word, translation, example, pronunciation, audio_url, image_url, lesson_id, active, created_at) 
SELECT 'Four', 'Четыре', 'I have four pencils.', 'fɔr', 'https://example.com/audio/four_word.mp3', 'https://example.com/images/four_word.jpg', 2, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM words WHERE word = 'Four' AND lesson_id = 2);

INSERT INTO words (word, translation, example, pronunciation, audio_url, image_url, lesson_id, active, created_at) 
SELECT 'Five', 'Пять', 'Five students in the class.', 'faɪv', 'https://example.com/audio/five_word.mp3', 'https://example.com/images/five_word.jpg', 2, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM words WHERE word = 'Five' AND lesson_id = 2);

INSERT INTO words (word, translation, example, pronunciation, audio_url, image_url, lesson_id, active, created_at) 
SELECT 'Plus', 'Плюс', 'Two plus two equals four.', 'plʌs', NULL, NULL, 3, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM words WHERE word = 'Plus' AND lesson_id = 3);

INSERT INTO words (word, translation, example, pronunciation, audio_url, image_url, lesson_id, active, created_at) 
SELECT 'Equals', 'Равно', 'One plus one equals two.', 'ˈikwəlz', NULL, NULL, 3, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM words WHERE word = 'Equals' AND lesson_id = 3);

INSERT INTO words (word, translation, example, pronunciation, audio_url, image_url, lesson_id, active, created_at) 
SELECT 'Sum', 'Сумма', 'The sum of 3 and 4 is 7.', 'sʌm', NULL, NULL, 3, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM words WHERE word = 'Sum' AND lesson_id = 3);

INSERT INTO words (word, translation, example, pronunciation, audio_url, image_url, lesson_id, active, created_at) 
SELECT 'Minus', 'Минус', 'Five minus two equals three.', 'ˈmaɪnəs', NULL, NULL, 4, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM words WHERE word = 'Minus' AND lesson_id = 4);

INSERT INTO words (word, translation, example, pronunciation, audio_url, image_url, lesson_id, active, created_at) 
SELECT 'Difference', 'Разность', 'The difference between 10 and 3 is 7.', 'ˈdɪfərəns', NULL, NULL, 4, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM words WHERE word = 'Difference' AND lesson_id = 4);

INSERT INTO words (word, translation, example, pronunciation, audio_url, image_url, lesson_id, active, created_at) 
SELECT 'Ancient', 'Древний', 'Ancient civilizations were very advanced.', 'ˈeɪnʃənt', 'https://example.com/audio/ancient_word.mp3', 'https://example.com/images/ancient_word.jpg', 5, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM words WHERE word = 'Ancient' AND lesson_id = 5);

INSERT INTO words (word, translation, example, pronunciation, audio_url, image_url, lesson_id, active, created_at) 
SELECT 'Civilization', 'Цивилизация', 'Egypt was an ancient civilization.', 'ˌsɪvələˈzeɪʃən', 'https://example.com/audio/civilization_word.mp3', 'https://example.com/images/civilization_word.jpg', 5, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM words WHERE word = 'Civilization' AND lesson_id = 5);

INSERT INTO words (word, translation, example, pronunciation, audio_url, image_url, lesson_id, active, created_at) 
SELECT 'Empire', 'Империя', 'The Roman Empire was very powerful.', 'ˈɛmpaɪər', 'https://example.com/audio/empire_word.mp3', 'https://example.com/images/empire_word.jpg', 5, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM words WHERE word = 'Empire' AND lesson_id = 5);

INSERT INTO words (word, translation, example, pronunciation, audio_url, image_url, lesson_id, active, created_at) 
SELECT 'Hello', 'Привет', 'Hello, how are you?', 'həˈloʊ', 'https://example.com/audio/hello_word.mp3', 'https://example.com/images/hello_word.jpg', 6, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM words WHERE word = 'Hello' AND lesson_id = 6);

INSERT INTO words (word, translation, example, pronunciation, audio_url, image_url, lesson_id, active, created_at) 
SELECT 'Goodbye', 'До свидания', 'Goodbye, see you later!', 'ˌɡʊdˈbaɪ', 'https://example.com/audio/goodbye_word.mp3', 'https://example.com/images/goodbye_word.jpg', 6, true, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM words WHERE word = 'Goodbye' AND lesson_id = 6);

-- Прогресс студентов
INSERT INTO student_progress (student_id, word_id, score, attempts, completed, last_attempt_at, completed_at, created_at) 
SELECT 1, 1, 85, 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM student_progress WHERE student_id = 1 AND word_id = 1);

INSERT INTO student_progress (student_id, word_id, score, attempts, completed, last_attempt_at, completed_at, created_at) 
SELECT 1, 2, 90, 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM student_progress WHERE student_id = 1 AND word_id = 2);

INSERT INTO student_progress (student_id, word_id, score, attempts, completed, last_attempt_at, completed_at, created_at) 
SELECT 1, 3, 75, 3, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM student_progress WHERE student_id = 1 AND word_id = 3);

INSERT INTO student_progress (student_id, word_id, score, attempts, completed, last_attempt_at, completed_at, created_at) 
SELECT 1, 4, 80, 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM student_progress WHERE student_id = 1 AND word_id = 4);

INSERT INTO student_progress (student_id, word_id, score, attempts, completed, last_attempt_at, completed_at, created_at) 
SELECT 1, 5, 95, 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM student_progress WHERE student_id = 1 AND word_id = 5);

INSERT INTO student_progress (student_id, word_id, score, attempts, completed, last_attempt_at, completed_at, created_at) 
SELECT 1, 6, 70, 4, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM student_progress WHERE student_id = 1 AND word_id = 6);

INSERT INTO student_progress (student_id, word_id, score, attempts, completed, last_attempt_at, completed_at, created_at) 
SELECT 2, 10, 88, 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM student_progress WHERE student_id = 2 AND word_id = 10);

INSERT INTO student_progress (student_id, word_id, score, attempts, completed, last_attempt_at, completed_at, created_at) 
SELECT 2, 11, 92, 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM student_progress WHERE student_id = 2 AND word_id = 11);

INSERT INTO student_progress (student_id, word_id, score, attempts, completed, last_attempt_at, completed_at, created_at) 
SELECT 2, 12, 85, 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM student_progress WHERE student_id = 2 AND word_id = 12);

INSERT INTO student_progress (student_id, word_id, score, attempts, completed, last_attempt_at, completed_at, created_at) 
SELECT 2, 13, 78, 3, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM student_progress WHERE student_id = 2 AND word_id = 13);

INSERT INTO student_progress (student_id, word_id, score, attempts, completed, last_attempt_at, completed_at, created_at) 
SELECT 2, 14, 90, 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM student_progress WHERE student_id = 2 AND word_id = 14);

INSERT INTO student_progress (student_id, word_id, score, attempts, completed, last_attempt_at, completed_at, created_at) 
SELECT 3, 15, 82, 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM student_progress WHERE student_id = 3 AND word_id = 15);

INSERT INTO student_progress (student_id, word_id, score, attempts, completed, last_attempt_at, completed_at, created_at) 
SELECT 3, 16, 87, 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM student_progress WHERE student_id = 3 AND word_id = 16);

INSERT INTO student_progress (student_id, word_id, score, attempts, completed, last_attempt_at, completed_at, created_at) 
SELECT 3, 17, 93, 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM student_progress WHERE student_id = 3 AND word_id = 17);

INSERT INTO student_progress (student_id, word_id, score, attempts, completed, last_attempt_at, completed_at, created_at) 
SELECT 4, 1, 60, 1, false, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM student_progress WHERE student_id = 4 AND word_id = 1);

INSERT INTO student_progress (student_id, word_id, score, attempts, completed, last_attempt_at, completed_at, created_at) 
SELECT 4, 2, 85, 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM student_progress WHERE student_id = 4 AND word_id = 2);

INSERT INTO student_progress (student_id, word_id, score, attempts, completed, last_attempt_at, completed_at, created_at) 
SELECT 4, 3, 45, 1, false, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM student_progress WHERE student_id = 4 AND word_id = 3);

INSERT INTO student_progress (student_id, word_id, score, attempts, completed, last_attempt_at, completed_at, created_at) 
SELECT 5, 10, 70, 2, false, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM student_progress WHERE student_id = 5 AND word_id = 10);

INSERT INTO student_progress (student_id, word_id, score, attempts, completed, last_attempt_at, completed_at, created_at) 
SELECT 5, 11, 88, 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM student_progress WHERE student_id = 5 AND word_id = 11); 