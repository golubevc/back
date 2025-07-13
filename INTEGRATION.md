# Интеграция с React Frontend

## Обзор

Этот документ описывает интеграцию бекенда Anecole с React фронтендом.

## Базовые настройки

### CORS
Бекенд уже настроен для работы с фронтендом на `http://localhost:3000`

### Аутентификация
- Используйте JWT токены в заголовке `Authorization: Bearer <token>`
- Токен получается при входе через `/api/auth/login`
- Токен валидируется через `/api/auth/validate`

## Основные API эндпоинты для фронтенда

### Аутентификация
```javascript
// Вход
POST /api/auth/login
{
  "username": "student1",
  "password": "password"
}

// Регистрация
POST /api/auth/register
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

// Валидация токена
POST /api/auth/validate
Headers: Authorization: Bearer <token>
```

### Пользователи
```javascript
// Получить текущего пользователя
GET /api/users/me
Headers: Authorization: Bearer <token>

// Обновить профиль
PUT /api/users/{id}
Headers: Authorization: Bearer <token>
```

### Классы
```javascript
// Получить классы пользователя
GET /api/classes/student/{studentId}  // для студентов
GET /api/classes/teacher/{teacherId}  // для учителей

// Получить конкретный класс
GET /api/classes/{id}
```

### Уроки
```javascript
// Получить уроки класса
GET /api/lessons/class/{classId}

// Получить конкретный урок
GET /api/lessons/{id}

// Получить слова урока
GET /api/lessons/{lessonId}/words
```

### Прогресс
```javascript
// Получить прогресс студента
GET /api/students/{id}/progress

// Обновить прогресс по слову
POST /api/students/{studentId}/progress/word/{wordId}
{
  "score": 85,
  "completed": true
}
```

## Примеры использования в React

### Настройка axios
```javascript
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Добавляем токен к запросам
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Обработка ошибок
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;
```

### Аутентификация
```javascript
// Вход
const login = async (username, password) => {
  try {
    const response = await api.post('/auth/login', { username, password });
    const { token, user } = response.data;
    
    localStorage.setItem('token', token);
    localStorage.setItem('user', JSON.stringify(user));
    
    return user;
  } catch (error) {
    throw new Error('Ошибка входа');
  }
};

// Выход
const logout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
  window.location.href = '/login';
};

// Проверка токена
const validateToken = async () => {
  try {
    await api.post('/auth/validate');
    return true;
  } catch (error) {
    return false;
  }
};
```

### Получение данных
```javascript
// Получить классы студента
const getStudentClasses = async (studentId) => {
  const response = await api.get(`/classes/student/${studentId}`);
  return response.data;
};

// Получить уроки класса
const getClassLessons = async (classId) => {
  const response = await api.get(`/lessons/class/${classId}`);
  return response.data;
};

// Получить слова урока
const getLessonWords = async (lessonId) => {
  const response = await api.get(`/lessons/${lessonId}/words`);
  return response.data;
};

// Получить прогресс студента
const getStudentProgress = async (studentId) => {
  const response = await api.get(`/students/${studentId}/progress`);
  return response.data;
};
```

### Обновление прогресса
```javascript
// Обновить прогресс по слову
const updateWordProgress = async (studentId, wordId, score, completed) => {
  const response = await api.post(`/students/${studentId}/progress/word/${wordId}`, {
    score,
    completed
  });
  return response.data;
};
```

## Роли и права доступа

### STUDENT
- Просмотр своих классов и уроков
- Обновление своего прогресса
- Просмотр статистики

### TEACHER
- Управление своими классами
- Создание и редактирование уроков
- Просмотр прогресса студентов
- Использование конструктора уроков

### ADMIN
- Полный доступ ко всем данным
- Управление пользователями
- Системная статистика

## Обработка ошибок

```javascript
const handleApiError = (error) => {
  if (error.response) {
    // Сервер ответил с ошибкой
    const { status, data } = error.response;
    
    switch (status) {
      case 400:
        return 'Неверные данные запроса';
      case 401:
        return 'Необходима авторизация';
      case 403:
        return 'Доступ запрещен';
      case 404:
        return 'Ресурс не найден';
      case 500:
        return 'Ошибка сервера';
      default:
        return 'Произошла ошибка';
    }
  } else if (error.request) {
    // Запрос был отправлен, но ответ не получен
    return 'Ошибка сети';
  } else {
    // Ошибка при настройке запроса
    return 'Ошибка приложения';
  }
};
```

## Тестовые данные

Используйте тестовые аккаунты из [TEST_ACCOUNTS.md](TEST_ACCOUNTS.md):

- **Студент**: `student1` / `password`
- **Учитель**: `teacher1` / `password`
- **Администратор**: `admin` / `password`

## Рекомендации по разработке

1. **Состояние приложения**: Используйте Redux, Zustand или Context API для управления состоянием
2. **Маршрутизация**: React Router для навигации
3. **Валидация**: React Hook Form или Formik для форм
4. **UI библиотека**: Material-UI, Ant Design или Chakra UI
5. **HTTP клиент**: Axios для API запросов
6. **Типизация**: TypeScript для лучшей разработки

## Пример структуры React приложения

```
src/
├── components/
│   ├── auth/
│   │   ├── LoginForm.jsx
│   │   └── RegisterForm.jsx
│   ├── dashboard/
│   │   ├── StudentDashboard.jsx
│   │   └── TeacherDashboard.jsx
│   ├── lessons/
│   │   ├── LessonList.jsx
│   │   ├── LessonDetail.jsx
│   │   └── WordCard.jsx
│   └── common/
│       ├── Header.jsx
│       ├── Sidebar.jsx
│       └── Loading.jsx
├── services/
│   ├── api.js
│   ├── auth.js
│   └── lessons.js
├── hooks/
│   ├── useAuth.js
│   └── useApi.js
├── context/
│   └── AuthContext.jsx
└── utils/
    ├── constants.js
    └── helpers.js
```

## Дополнительные возможности

Бекенд поддерживает дополнительные функции:

1. **Конструктор уроков** - для учителей
2. **Статистика и аналитика** - для всех ролей
3. **Поиск и фильтрация** - для уроков и слов
4. **Управление пользователями** - для администраторов

Подробную документацию API см. в [API_DOCUMENTATION.md](API_DOCUMENTATION.md) 