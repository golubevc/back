# API Documentation - Anecole Backend

## Общая информация

**Base URL**: `http://localhost:8080/api`  
**Content-Type**: `application/json`  
**Authentication**: JWT Bearer Token

## Аутентификация

### Вход в систему
```http
POST /auth/login
```

**Request Body:**
```json
{
  "username": "student1",
  "password": "password"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "username": "student1",
    "email": "student1@school.com",
    "firstName": "Алексей",
    "lastName": "Иванов",
    "role": "STUDENT",
    "grade": 5,
    "school": "Школа №1"
  }
}
```

### Регистрация
```http
POST /auth/register
```

**Request Body:**
```json
{
  "username": "newuser",
  "email": "newuser@school.com",
  "password": "password123",
  "firstName": "Иван",
  "lastName": "Петров",
  "role": "STUDENT",
  "grade": 6,
  "school": "Школа №1"
}
```

### Валидация токена
```http
POST /auth/validate
```

**Headers:**
```
Authorization: Bearer <token>
```

## Управление пользователями (Администратор)

### Получить всех пользователей
```http
GET /users
Authorization: Bearer <admin_token>
```

### Получить пользователя по ID
```http
GET /users/{id}
Authorization: Bearer <admin_token>
```

### Получить пользователей по роли
```http
GET /users/role/{role}
Authorization: Bearer <admin_token>
```

**Роли**: `STUDENT`, `TEACHER`, `ADMIN`

### Поиск пользователей
```http
GET /users/search?term=алексей
Authorization: Bearer <admin_token>
```

### Создать пользователя
```http
POST /users
Authorization: Bearer <admin_token>
```

**Request Body:**
```json
{
  "username": "newstudent",
  "email": "newstudent@school.com",
  "password": "password123",
  "firstName": "Мария",
  "lastName": "Иванова",
  "role": "STUDENT",
  "grade": 7,
  "school": "Школа №2"
}
```

### Обновить пользователя
```http
PUT /users/{id}
Authorization: Bearer <admin_token>
```

### Удалить пользователя
```http
DELETE /users/{id}
Authorization: Bearer <admin_token>
```

### Включить пользователя
```http
PUT /users/{id}/enable
Authorization: Bearer <admin_token>
```

### Отключить пользователя
```http
PUT /users/{id}/disable
Authorization: Bearer <admin_token>
```

### Изменить роль пользователя
```http
PUT /users/{id}/role?role=TEACHER
Authorization: Bearer <admin_token>
```

### Статистика пользователей
```http
GET /users/stats/count
Authorization: Bearer <admin_token>
```

**Response:**
```json
{
  "totalUsers": 22,
  "activeUsers": 20,
  "students": 10,
  "teachers": 8,
  "admins": 4
}
```

### Количество активных пользователей
```http
GET /users/stats/active
Authorization: Bearer <admin_token>
```

### Количество пользователей по роли
```http
GET /users/stats/role/{role}
Authorization: Bearer <admin_token>
```

## Студенты

### Получить всех студентов
```http
GET /students
Authorization: Bearer <token>
```

### Получить студента по ID
```http
GET /students/{id}
Authorization: Bearer <token>
```

### Получить классы студента
```http
GET /students/{id}/classes
Authorization: Bearer <token>
```

### Получить уроки студента
```http
GET /students/{id}/lessons
Authorization: Bearer <token>
```

### Получить прогресс студента
```http
GET /students/{id}/progress
Authorization: Bearer <token>
```

### Обновить прогресс по слову
```http
POST /students/{studentId}/progress/word/{wordId}
Authorization: Bearer <token>
```

**Request Body:**
```json
{
  "score": 85,
  "completed": true
}
```

## Учителя

### Получить всех учителей
```http
GET /teachers
Authorization: Bearer <token>
```

### Получить классы учителя
```http
GET /teachers/{id}/classes
Authorization: Bearer <token>
```

### Создать класс
```http
POST /teachers/classes
Authorization: Bearer <teacher_token>
```

**Request Body:**
```json
{
  "name": "8А - Английский язык",
  "description": "Английский язык для 8 класса А",
  "grade": 8
}
```

### Обновить класс
```http
PUT /teachers/classes/{id}
Authorization: Bearer <teacher_token>
```

### Удалить класс
```http
DELETE /teachers/classes/{id}
Authorization: Bearer <teacher_token>
```

## Классы

### Получить все классы
```http
GET /classes
Authorization: Bearer <token>
```

### Получить класс по ID
```http
GET /classes/{id}
Authorization: Bearer <token>
```

### Получить классы учителя
```http
GET /classes/teacher/{teacherId}
Authorization: Bearer <token>
```

### Получить классы студента
```http
GET /classes/student/{studentId}
Authorization: Bearer <token>
```

### Создать класс
```http
POST /classes
Authorization: Bearer <teacher_token>
```

### Обновить класс
```http
PUT /classes/{id}
Authorization: Bearer <teacher_token>
```

### Удалить класс
```http
DELETE /classes/{id}
Authorization: Bearer <teacher_token>
```

## Уроки

### Получить все уроки
```http
GET /lessons
Authorization: Bearer <token>
```

### Получить урок по ID
```http
GET /lessons/{id}
Authorization: Bearer <token>
```

### Получить уроки класса
```http
GET /lessons/class/{classId}
Authorization: Bearer <token>
```

### Получить слова урока
```http
GET /lessons/{lessonId}/words
Authorization: Bearer <token>
```

### Создать урок
```http
POST /lessons
Authorization: Bearer <teacher_token>
```

**Request Body:**
```json
{
  "title": "Урок 1: Приветствие",
  "description": "Изучаем основные приветствия",
  "orderNumber": 1,
  "classId": 1,
  "videoUrl": "https://example.com/video.mp4",
  "audioUrl": "https://example.com/audio.mp3",
  "imageUrl": "https://example.com/image.jpg"
}
```

### Обновить урок
```http
PUT /lessons/{id}
Authorization: Bearer <teacher_token>
```

### Удалить урок
```http
DELETE /lessons/{id}
Authorization: Bearer <teacher_token>
```

## Слова

### Получить все слова
```http
GET /words
Authorization: Bearer <token>
```

### Поиск слов
```http
GET /words/search?term=hello
Authorization: Bearer <token>
```

### Получить слова урока
```http
GET /words/lesson/{lessonId}
Authorization: Bearer <token>
```

### Создать слово
```http
POST /words
Authorization: Bearer <teacher_token>
```

**Request Body:**
```json
{
  "word": "Hello",
  "translation": "Привет",
  "example": "Hello, how are you?",
  "pronunciation": "həˈloʊ",
  "audioUrl": "https://example.com/audio.mp3",
  "imageUrl": "https://example.com/image.jpg",
  "lessonId": 1
}
```

### Обновить слово
```http
PUT /words/{id}
Authorization: Bearer <teacher_token>
```

### Удалить слово
```http
DELETE /words/{id}
Authorization: Bearer <teacher_token>
```

## Конструктор уроков

### Создать урок
```http
POST /lesson-builder/create
Authorization: Bearer <teacher_token>
```

**Request Body:**
```json
{
  "title": "Новый урок",
  "description": "Описание урока",
  "classId": 1,
  "words": [
    {
      "word": "New",
      "translation": "Новый",
      "example": "This is a new book",
      "pronunciation": "njuː"
    }
  ]
}
```

### Обновить урок
```http
PUT /lesson-builder/{lessonId}
Authorization: Bearer <teacher_token>
```

### Опубликовать урок
```http
POST /lesson-builder/{lessonId}/publish
Authorization: Bearer <teacher_token>
```

### Получить черновики учителя
```http
GET /lesson-builder/teacher/{teacherId}/drafts
Authorization: Bearer <teacher_token>
```

### Получить уроки студента
```http
GET /lesson-builder/student/{studentId}/lessons
Authorization: Bearer <token>
```

## Модели данных

### User
```json
{
  "id": 1,
  "username": "student1",
  "email": "student1@school.com",
  "firstName": "Алексей",
  "lastName": "Иванов",
  "role": "STUDENT",
  "grade": 5,
  "school": "Школа №1",
  "subject": null,
  "qualification": null,
  "avatar": null,
  "enabled": true,
  "emailVerified": true,
  "createdAt": "2024-01-01T10:00:00"
}
```

### Class
```json
{
  "id": 1,
  "name": "5А - Английский язык",
  "description": "Английский язык для 5 класса А",
  "grade": 5,
  "teacherId": 6,
  "active": true,
  "createdAt": "2024-01-01T10:00:00"
}
```

### Lesson
```json
{
  "id": 1,
  "title": "Урок 1: Приветствие",
  "description": "Изучаем основные приветствия",
  "orderNumber": 1,
  "classId": 1,
  "videoUrl": "https://example.com/video.mp4",
  "audioUrl": "https://example.com/audio.mp3",
  "imageUrl": "https://example.com/image.jpg",
  "active": true,
  "createdAt": "2024-01-01T10:00:00"
}
```

### Word
```json
{
  "id": 1,
  "word": "Hello",
  "translation": "Привет",
  "example": "Hello, how are you?",
  "pronunciation": "həˈloʊ",
  "audioUrl": "https://example.com/audio.mp3",
  "imageUrl": "https://example.com/image.jpg",
  "lessonId": 1,
  "active": true,
  "createdAt": "2024-01-01T10:00:00"
}
```

### StudentProgress
```json
{
  "id": 1,
  "studentId": 1,
  "wordId": 1,
  "score": 85,
  "attempts": 2,
  "completed": true,
  "lastAttemptAt": "2024-01-01T10:00:00",
  "completedAt": "2024-01-01T10:00:00",
  "createdAt": "2024-01-01T10:00:00"
}
```

## Коды ошибок

- `200` - Успешный запрос
- `201` - Ресурс создан
- `400` - Неверный запрос
- `401` - Не авторизован
- `403` - Доступ запрещен
- `404` - Ресурс не найден
- `500` - Внутренняя ошибка сервера

## Тестовые аккаунты

### Студенты
- `student1` / `password` - Алексей Иванов (5 класс)
- `student2` / `password` - Мария Петрова (6 класс)
- `student3` / `password` - Дмитрий Сидоров (7 класс)

### Учителя
- `teacher1` / `password` - Елена Смирнова (Английский язык)
- `teacher2` / `password` - Игорь Волков (Математика)
- `teacher3` / `password` - Ольга Морозова (История)

### Администраторы
- `admin` / `password` - Администратор Системы
- `admin1` / `password` - Иван Петров (Системный администратор)
- `admin2` / `password` - Мария Сидорова (Администратор образования)
- `superadmin` / `password` - Александр Козлов (Главный администратор)

## Примеры использования

### Получение всех студентов (для администратора)
```bash
curl -X GET "http://localhost:8080/api/users/role/STUDENT" \
  -H "Authorization: Bearer <admin_token>"
```

### Создание нового класса (для учителя)
```bash
curl -X POST "http://localhost:8080/api/teachers/classes" \
  -H "Authorization: Bearer <teacher_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "9А - Физика",
    "description": "Физика для 9 класса А",
    "grade": 9
  }'
```

### Обновление прогресса студента
```bash
curl -X POST "http://localhost:8080/api/students/1/progress/word/1" \
  -H "Authorization: Bearer <student_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "score": 90,
    "completed": true
  }'
```

### Поиск пользователей (для администратора)
```bash
curl -X GET "http://localhost:8080/api/users/search?term=алексей" \
  -H "Authorization: Bearer <admin_token>"
```

### Получение статистики пользователей
```bash
curl -X GET "http://localhost:8080/api/users/stats/count" \
  -H "Authorization: Bearer <admin_token>"
``` 