# Быстрый запуск Anecole Backend

## Шаг 1: Проверка требований
```bash
# Проверка версии Java (должна быть 24)
java -version

# Проверка версии Maven
mvn -version
```

## Шаг 2: Запуск приложения
```bash
# Переход в директорию backend
cd back

# Сборка и запуск
mvn spring-boot:run
```

## Шаг 3: Проверка работы
После запуска приложение будет доступно по адресам:

- **API Base URL**: http://localhost:8080/api
- **H2 Database Console**: http://localhost:8080/api/h2-console
- **Swagger Documentation**: http://localhost:8080/api/swagger-ui/index.html

## Шаг 4: Тестирование API

### Авторизация
```bash
# Регистрация студента
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "teststudent",
    "email": "test@school.com",
    "password": "password",
    "firstName": "Тест",
    "lastName": "Студент",
    "role": "STUDENT",
    "grade": 5
  }'

# Вход в систему
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "student1",
    "password": "password"
  }'
```

### Получение данных
```bash
# Получить всех студентов
curl http://localhost:8080/api/students

# Получить все уроки
curl http://localhost:8080/api/lessons

# Получить всех учителей
curl http://localhost:8080/api/teachers
```

## Тестовые аккаунты для входа

### Студенты
- **student1** / password (5 класс)
- **student2** / password (6 класс)
- **student3** / password (7 класс)

### Учителя
- **teacher1** / password (Английский язык)
- **teacher2** / password (Математика)
- **teacher3** / password (История)

### Администратор
- **admin** / password

## H2 Database Console
Для просмотра базы данных:
1. Откройте http://localhost:8080/api/h2-console
2. Введите настройки:
   - **JDBC URL**: `jdbc:h2:mem:anecoledb`
   - **Username**: `sa`
   - **Password**: `password`
3. Нажмите "Connect"

## Возможные проблемы

### Порт 8080 занят
```bash
# Измените порт в application.yml
server:
  port: 8081
```

### Ошибки сборки
```bash
# Очистка и пересборка
mvn clean install
```

### Проблемы с Java версией
Убедитесь, что используется Java 24:
```bash
# Установка Java 24 (macOS)
brew install openjdk@24

# Установка Java 24 (Ubuntu)
sudo apt install openjdk-24-jdk
``` 