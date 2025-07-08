-- Создание схемы базы данных для Anecole Language Learning Platform

-- Создание enum типов
CREATE TYPE user_role AS ENUM ('STUDENT', 'TEACHER', 'ADMIN');

-- Создание таблицы пользователей
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role user_role NOT NULL,
    grade INTEGER,
    school VARCHAR(100),
    subject VARCHAR(100),
    qualification VARCHAR(100),
    avatar VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login_at TIMESTAMP,
    enabled BOOLEAN DEFAULT true,
    email_verified BOOLEAN DEFAULT false
);

-- Создание таблицы классов
CREATE TABLE IF NOT EXISTS classes (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    grade INTEGER,
    teacher_id BIGINT REFERENCES users(id),
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Создание таблицы связи классов и студентов
CREATE TABLE IF NOT EXISTS classes_students (
    class_id BIGINT REFERENCES classes(id) ON DELETE CASCADE,
    student_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (class_id, student_id)
);

-- Создание таблицы уроков
CREATE TABLE IF NOT EXISTS lessons (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    order_number INTEGER NOT NULL,
    class_id BIGINT REFERENCES classes(id) ON DELETE CASCADE,
    video_url VARCHAR(500),
    audio_url VARCHAR(500),
    image_url VARCHAR(500),
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Создание таблицы слов
CREATE TABLE IF NOT EXISTS words (
    id BIGSERIAL PRIMARY KEY,
    word VARCHAR(100) NOT NULL,
    translation VARCHAR(100) NOT NULL,
    example TEXT,
    pronunciation VARCHAR(100),
    audio_url VARCHAR(500),
    image_url VARCHAR(500),
    lesson_id BIGINT REFERENCES lessons(id) ON DELETE CASCADE,
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Создание таблицы прогресса студентов
CREATE TABLE IF NOT EXISTS student_progress (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    lesson_id BIGINT REFERENCES lessons(id) ON DELETE CASCADE,
    word_id BIGINT REFERENCES words(id) ON DELETE CASCADE,
    score INTEGER CHECK (score >= 0 AND score <= 100),
    attempts INTEGER DEFAULT 0,
    completed BOOLEAN DEFAULT false,
    last_attempt_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(student_id, word_id)
);

-- Создание индексов для улучшения производительности
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);
CREATE INDEX IF NOT EXISTS idx_classes_teacher_id ON classes(teacher_id);
CREATE INDEX IF NOT EXISTS idx_lessons_class_id ON lessons(class_id);
CREATE INDEX IF NOT EXISTS idx_words_lesson_id ON words(lesson_id);
CREATE INDEX IF NOT EXISTS idx_student_progress_student_id ON student_progress(student_id);
CREATE INDEX IF NOT EXISTS idx_student_progress_word_id ON student_progress(word_id);
CREATE INDEX IF NOT EXISTS idx_student_progress_completed ON student_progress(completed);

-- Создание последовательностей для автоинкремента
CREATE SEQUENCE IF NOT EXISTS users_id_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS classes_id_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS lessons_id_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS words_id_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS student_progress_id_seq START WITH 1; 