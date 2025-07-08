-- Миграция V1: Обновление существующих данных
-- Обновляем существующие данные для лучшего тестирования

-- Обновляем существующих пользователей с более подробной информацией
UPDATE users SET 
    email = 'student1@school.com',
    first_name = 'Алексей',
    last_name = 'Иванов',
    grade = 5,
    school = 'Школа №1',
    enabled = true,
    email_verified = true
WHERE username = 'student1';

UPDATE users SET 
    email = 'student2@school.com',
    first_name = 'Мария',
    last_name = 'Петрова',
    grade = 6,
    school = 'Школа №2',
    enabled = true,
    email_verified = true
WHERE username = 'student2';

UPDATE users SET 
    email = 'student3@school.com',
    first_name = 'Дмитрий',
    last_name = 'Сидоров',
    grade = 7,
    school = 'Школа №1',
    enabled = true,
    email_verified = true
WHERE username = 'student3';

UPDATE users SET 
    email = 'student4@school.com',
    first_name = 'Анна',
    last_name = 'Козлова',
    grade = 5,
    school = 'Школа №3',
    enabled = true,
    email_verified = true
WHERE username = 'student4';

UPDATE users SET 
    email = 'student5@school.com',
    first_name = 'Сергей',
    last_name = 'Новиков',
    grade = 6,
    school = 'Школа №2',
    enabled = true,
    email_verified = true
WHERE username = 'student5';

-- Обновляем учителей
UPDATE users SET 
    email = 'teacher1@school.com',
    first_name = 'Елена',
    last_name = 'Смирнова',
    subject = 'Английский язык',
    qualification = 'Высшая категория',
    enabled = true,
    email_verified = true
WHERE username = 'teacher1';

UPDATE users SET 
    email = 'teacher2@school.com',
    first_name = 'Игорь',
    last_name = 'Волков',
    subject = 'Математика',
    qualification = 'Первая категория',
    enabled = true,
    email_verified = true
WHERE username = 'teacher2';

UPDATE users SET 
    email = 'teacher3@school.com',
    first_name = 'Ольга',
    last_name = 'Морозова',
    subject = 'История',
    qualification = 'Высшая категория',
    enabled = true,
    email_verified = true
WHERE username = 'teacher3';

UPDATE users SET 
    email = 'teacher4@school.com',
    first_name = 'Александр',
    last_name = 'Лебедев',
    subject = 'Физика',
    qualification = 'Первая категория',
    enabled = true,
    email_verified = true
WHERE username = 'teacher4';

-- Обновляем администратора
UPDATE users SET 
    email = 'admin@school.com',
    first_name = 'Администратор',
    last_name = 'Системы',
    qualification = 'Главный администратор',
    enabled = true,
    email_verified = true
WHERE username = 'admin';

-- Обновляем классы с более подробной информацией
UPDATE classes SET 
    name = '5А - Английский язык',
    description = 'Английский язык для 5 класса А. Изучаем основы грамматики и лексики.',
    grade = 5,
    active = true
WHERE id = 1;

UPDATE classes SET 
    name = '6Б - Математика',
    description = 'Математика для 6 класса Б. Основы алгебры и геометрии.',
    grade = 6,
    active = true
WHERE id = 2;

UPDATE classes SET 
    name = '7А - История',
    description = 'История для 7 класса А. История России и всемирная история.',
    grade = 7,
    active = true
WHERE id = 3;

UPDATE classes SET 
    name = '5Б - Английский язык',
    description = 'Английский язык для 5 класса Б. Продвинутый курс английского языка.',
    grade = 5,
    active = true
WHERE id = 4;

-- Обновляем уроки с более подробной информацией
UPDATE lessons SET 
    title = 'Урок 1: Приветствие',
    description = 'Изучаем основные приветствия на английском языке. Учимся здороваться и прощаться.',
    video_url = 'https://example.com/videos/hello.mp4',
    audio_url = 'https://example.com/audio/hello.mp3',
    image_url = 'https://example.com/images/hello.jpg',
    active = true
WHERE id = 1;

UPDATE lessons SET 
    title = 'Урок 2: Числа от 1 до 10',
    description = 'Изучаем числа от 1 до 10 на английском языке. Учимся считать и произносить числа.',
    video_url = 'https://example.com/videos/numbers.mp4',
    audio_url = 'https://example.com/audio/numbers.mp3',
    image_url = 'https://example.com/images/numbers.jpg',
    active = true
WHERE id = 2;

UPDATE lessons SET 
    title = 'Урок 1: Сложение',
    description = 'Основы сложения чисел. Изучаем правила сложения и решаем примеры.',
    video_url = 'https://example.com/videos/addition.mp4',
    image_url = 'https://example.com/images/addition.jpg',
    active = true
WHERE id = 3;

UPDATE lessons SET 
    title = 'Урок 2: Вычитание',
    description = 'Основы вычитания чисел. Изучаем правила вычитания и решаем примеры.',
    video_url = 'https://example.com/videos/subtraction.mp4',
    image_url = 'https://example.com/images/subtraction.jpg',
    active = true
WHERE id = 4;

UPDATE lessons SET 
    title = 'Урок 1: Древний мир',
    description = 'Введение в историю древнего мира. Изучаем древние цивилизации.',
    video_url = 'https://example.com/videos/ancient.mp4',
    audio_url = 'https://example.com/audio/ancient.mp3',
    image_url = 'https://example.com/images/ancient.jpg',
    active = true
WHERE id = 5;

UPDATE lessons SET 
    title = 'Урок 1: Приветствие (5Б)',
    description = 'Изучаем основные приветствия на английском языке для продвинутого уровня.',
    video_url = 'https://example.com/videos/hello.mp4',
    audio_url = 'https://example.com/audio/hello.mp3',
    image_url = 'https://example.com/images/hello.jpg',
    active = true
WHERE id = 6;

-- Обновляем слова с более подробной информацией
UPDATE words SET 
    example = 'Hello, how are you today?',
    pronunciation = 'həˈloʊ',
    audio_url = 'https://example.com/audio/hello_word.mp3',
    image_url = 'https://example.com/images/hello_word.jpg',
    active = true
WHERE word = 'Hello' AND lesson_id = 1;

UPDATE words SET 
    example = 'Goodbye, see you later!',
    pronunciation = 'ˌɡʊdˈbaɪ',
    audio_url = 'https://example.com/audio/goodbye_word.mp3',
    image_url = 'https://example.com/images/goodbye_word.jpg',
    active = true
WHERE word = 'Goodbye' AND lesson_id = 1;

UPDATE words SET 
    example = 'Thank you very much for your help!',
    pronunciation = 'ˈθæŋk ju',
    audio_url = 'https://example.com/audio/thankyou_word.mp3',
    image_url = 'https://example.com/images/thankyou_word.jpg',
    active = true
WHERE word = 'Thank you' AND lesson_id = 1;

UPDATE words SET 
    example = 'Please help me with this task.',
    pronunciation = 'pliz',
    audio_url = 'https://example.com/audio/please_word.mp3',
    image_url = 'https://example.com/images/please_word.jpg',
    active = true
WHERE word = 'Please' AND lesson_id = 1;

-- Обновляем прогресс студентов с более реалистичными данными
UPDATE student_progress SET 
    score = 85,
    attempts = 2,
    completed = true,
    last_attempt_at = CURRENT_TIMESTAMP,
    completed_at = CURRENT_TIMESTAMP
WHERE student_id = 1 AND word_id = 1;

UPDATE student_progress SET 
    score = 90,
    attempts = 1,
    completed = true,
    last_attempt_at = CURRENT_TIMESTAMP,
    completed_at = CURRENT_TIMESTAMP
WHERE student_id = 1 AND word_id = 2;

UPDATE student_progress SET 
    score = 75,
    attempts = 3,
    completed = true,
    last_attempt_at = CURRENT_TIMESTAMP,
    completed_at = CURRENT_TIMESTAMP
WHERE student_id = 1 AND word_id = 3;

-- Комментарий: Обновлены существующие данные с более подробной информацией
-- Все пользователи теперь имеют полные профили с email, именами и квалификациями
-- Уроки содержат подробные описания и медиа-контент
-- Слова имеют примеры, произношение и аудио/видео материалы
-- Прогресс студентов обновлен с реалистичными оценками 