# Anecole Backend

Spring Boot приложение для образовательной платформы Anecole.

## 🚀 Быстрый старт

### Требования
- Java 22+
- Maven 3.6+

### Запуск
```bash
# Клонирование репозитория
git clone <repository-url>
cd back

# Запуск приложения
./mvnw spring-boot:run
```

Приложение будет доступно по адресу: `http://localhost:8080`

## 📊 База данных

### Миграции
Проект использует SQL миграции для управления схемой базы данных:

- `V1__update_existing_data.sql` - Обновление существующих данных
- `V2__add_test_administrators.sql` - Добавление тестовых администраторов
- `V3__add_test_data.sql` - Добавление дополнительных тестовых данных

### Тестовые данные
База данных содержит полный набор тестовых данных:

- **22 пользователя**: 10 студентов, 8 учителей, 4 администратора
- **9 классов** с разными предметами
- **11 уроков** с полным контентом
- **32 слова** с примерами и произношением
- **Прогресс студентов** для демонстрации статистики

## 👥 Тестовые аккаунты

### Основные аккаунты
- **Студент**: `student1` / `password`
- **Учитель**: `teacher1` / `password`
- **Администратор**: `admin` / `password`

### Все аккаунты
Подробный список всех тестовых аккаунтов см. в [TEST_ACCOUNTS.md](TEST_ACCOUNTS.md)

## 🔧 API Endpoints

### Аутентификация
- `POST /api/auth/login` - Вход в систему
- `POST /api/auth/register` - Регистрация
- `POST /api/auth/validate` - Валидация токена

### Студенты
- `GET /api/students` - Все студенты
- `GET /api/students/{id}` - Студент по ID
- `GET /api/students/{id}/classes` - Классы студента
- `GET /api/students/{id}/lessons` - Уроки студента
- `GET /api/students/{id}/progress` - Прогресс студента
- `POST /api/students/{studentId}/progress/word/{wordId}` - Обновление прогресса

### Учителя
- `GET /api/teachers` - Все учителя
- `GET /api/teachers/{id}/classes` - Классы учителя
- `POST /api/teachers/classes` - Создание класса
- `PUT /api/teachers/classes/{id}` - Обновление класса
- `DELETE /api/teachers/classes/{id}` - Удаление класса

### Классы
- `GET /api/classes` - Все классы
- `GET /api/classes/{id}` - Класс по ID
- `GET /api/classes/teacher/{teacherId}` - Классы учителя
- `GET /api/classes/student/{studentId}` - Классы студента
- `POST /api/classes` - Создание класса
- `PUT /api/classes/{id}` - Обновление класса
- `DELETE /api/classes/{id}` - Удаление класса

### Уроки
- `GET /api/lessons` - Все уроки
- `GET /api/lessons/{id}` - Урок по ID
- `GET /api/lessons/class/{classId}` - Уроки класса
- `GET /api/lessons/{lessonId}/words` - Слова урока
- `POST /api/lessons` - Создание урока
- `PUT /api/lessons/{id}` - Обновление урока
- `DELETE /api/lessons/{id}` - Удаление урока

### Слова
- `GET /api/words` - Все слова
- `GET /api/words/search` - Поиск слов
- `GET /api/words/lesson/{lessonId}` - Слова урока
- `POST /api/words` - Создание слова
- `PUT /api/words/{id}` - Обновление слова
- `DELETE /api/words/{id}` - Удаление слова

### Конструктор уроков
- `POST /api/lesson-builder/create` - Создание урока
- `PUT /api/lesson-builder/{lessonId}` - Обновление урока
- `POST /api/lesson-builder/{lessonId}/publish` - Публикация урока
- `GET /api/lesson-builder/teacher/{teacherId}/drafts` - Черновики учителя
- `GET /api/lesson-builder/student/{studentId}/lessons` - Уроки студента

## 🔐 Безопасность

### JWT Аутентификация
- Токены JWT для аутентификации
- Ролевая система (STUDENT, TEACHER, ADMIN)
- Защищенные эндпоинты с @PreAuthorize

### CORS
Настроен для работы с фронтендом на `http://localhost:3000`

## 📝 Логирование

Приложение использует SLF4J для логирования:
- Логи аутентификации
- Логи API запросов
- Логи ошибок

## 🧪 Тестирование

### Unit тесты
```bash
./mvnw test
```

### Интеграционные тесты
```bash
./mvnw verify
```

## 📚 Документация

- [API Documentation](API_DOCUMENTATION.md) - Полная документация API
- [Test Accounts](TEST_ACCOUNTS.md) - Список тестовых аккаунтов
- [Integration Guide](../front-mono/INTEGRATION.md) - Руководство по интеграции с фронтендом

## 🏗️ Архитектура

### Слои приложения
- **Controllers** - REST API эндпоинты
- **Services** - Бизнес-логика
- **Repositories** - Доступ к данным
- **Models** - Сущности базы данных
- **DTOs** - Объекты передачи данных
- **Security** - JWT аутентификация и авторизация

### Основные компоненты
- **AuthService** - Аутентификация и регистрация
- **StudentService** - Управление студентами
- **TeacherService** - Управление учителями
- **ClassService** - Управление классами
- **LessonService** - Управление уроками
- **WordService** - Управление словами
- **LessonBuilderService** - Конструктор уроков

## 🔄 Миграции

### Создание новой миграции
1. Создайте файл в `src/main/resources/db/migration/`
2. Именуйте файл по схеме: `V{номер}__{описание}.sql`
3. Добавьте SQL команды для изменения схемы или данных

### Пример миграции
```sql
-- V4__add_new_feature.sql
ALTER TABLE users ADD COLUMN phone VARCHAR(20);
INSERT INTO users (username, email, password, role) 
VALUES ('newuser', 'new@example.com', '$2a$10$...', 'STUDENT');
```

## 🚀 Развертывание

### Локальная разработка
```bash
./mvnw spring-boot:run
```

### Продакшн
```bash
./mvnw clean package
java -jar target/anecole-0.0.1-SNAPSHOT.jar
```

## 📞 Поддержка

Для вопросов и предложений создавайте issues в репозитории проекта. 