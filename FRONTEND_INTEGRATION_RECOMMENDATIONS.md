# Рекомендации по интеграции с React Frontend

## Анализ текущего состояния

### ✅ Что уже готово для фронтенда:

1. **Полная аутентификация и авторизация**
   - JWT токены с ролевой системой
   - Эндпоинты для входа, регистрации, валидации
   - CORS настроен для `http://localhost:3000`

2. **Основные CRUD операции**
   - Пользователи (студенты, учителя, администраторы)
   - Классы и уроки
   - Слова с переводами
   - Прогресс студентов

3. **Специальные функции**
   - Конструктор уроков для учителей
   - Статистика и аналитика
   - Поиск по урокам и словам

4. **Документация**
   - Swagger UI на `/swagger-ui.html`
   - Подробная API документация
   - Тестовые аккаунты

## 🔧 Что добавлено для фронтенда:

### Новые эндпоинты:

1. **`GET /api/users/me`** - Получить текущего пользователя
2. **`GET /api/stats/dashboard`** - Общая статистика дашборда
3. **`GET /api/stats/student/{id}`** - Статистика студента
4. **`GET /api/stats/teacher/{id}`** - Статистика учителя
5. **`GET /api/stats/class/{id}`** - Статистика класса
6. **`GET /api/stats/lesson/{id}`** - Статистика урока
7. **`GET /api/search/lessons?query=...`** - Поиск уроков
8. **`GET /api/search/words?query=...`** - Поиск слов
9. **`GET /api/search/global?query=...`** - Общий поиск

## 🚀 Рекомендации по разработке фронтенда:

### 1. Структура проекта React

```
front-mono/
├── src/
│   ├── components/
│   │   ├── auth/
│   │   │   ├── LoginForm.tsx
│   │   │   ├── RegisterForm.tsx
│   │   │   └── ProtectedRoute.tsx
│   │   ├── dashboard/
│   │   │   ├── StudentDashboard.tsx
│   │   │   ├── TeacherDashboard.tsx
│   │   │   └── AdminDashboard.tsx
│   │   ├── lessons/
│   │   │   ├── LessonList.tsx
│   │   │   ├── LessonDetail.tsx
│   │   │   ├── WordCard.tsx
│   │   │   └── ProgressTracker.tsx
│   │   ├── classes/
│   │   │   ├── ClassList.tsx
│   │   │   └── ClassDetail.tsx
│   │   ├── stats/
│   │   │   ├── StatsDashboard.tsx
│   │   │   └── ProgressChart.tsx
│   │   └── common/
│   │       ├── Header.tsx
│   │       ├── Sidebar.tsx
│   │       ├── Loading.tsx
│   │       └── ErrorBoundary.tsx
│   ├── services/
│   │   ├── api.ts
│   │   ├── auth.ts
│   │   ├── lessons.ts
│   │   ├── classes.ts
│   │   ├── stats.ts
│   │   └── search.ts
│   ├── hooks/
│   │   ├── useAuth.ts
│   │   ├── useApi.ts
│   │   └── useLocalStorage.ts
│   ├── context/
│   │   ├── AuthContext.tsx
│   │   └── AppContext.tsx
│   ├── types/
│   │   ├── auth.ts
│   │   ├── lesson.ts
│   │   ├── user.ts
│   │   └── api.ts
│   └── utils/
│       ├── constants.ts
│       ├── helpers.ts
│       └── validation.ts
├── public/
└── package.json
```

### 2. Основные зависимости

```json
{
  "dependencies": {
    "react": "^18.2.0",
    "react-dom": "^18.2.0",
    "react-router-dom": "^6.8.0",
    "axios": "^1.3.0",
    "@mui/material": "^5.11.0",
    "@mui/icons-material": "^5.11.0",
    "@emotion/react": "^11.10.0",
    "@emotion/styled": "^11.10.0",
    "react-hook-form": "^7.43.0",
    "react-query": "^3.39.0",
    "recharts": "^2.5.0",
    "date-fns": "^2.29.0"
  },
  "devDependencies": {
    "@types/react": "^18.0.0",
    "@types/react-dom": "^18.0.0",
    "typescript": "^4.9.0",
    "vite": "^4.1.0",
    "@vitejs/plugin-react": "^3.1.0"
  }
}
```

### 3. Настройка API клиента

```typescript
// src/services/api.ts
import axios, { AxiosInstance, AxiosResponse } from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

class ApiClient {
  private client: AxiosInstance;

  constructor() {
    this.client = axios.create({
      baseURL: API_BASE_URL,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    this.setupInterceptors();
  }

  private setupInterceptors() {
    // Request interceptor
    this.client.interceptors.request.use(
      (config) => {
        const token = localStorage.getItem('token');
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => Promise.reject(error)
    );

    // Response interceptor
    this.client.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response?.status === 401) {
          localStorage.removeItem('token');
          localStorage.removeItem('user');
          window.location.href = '/login';
        }
        return Promise.reject(error);
      }
    );
  }

  // Auth methods
  async login(username: string, password: string) {
    const response = await this.client.post('/auth/login', { username, password });
    return response.data;
  }

  async register(userData: RegisterRequest) {
    const response = await this.client.post('/auth/register', userData);
    return response.data;
  }

  async validateToken() {
    const response = await this.client.post('/auth/validate');
    return response.data;
  }

  // User methods
  async getCurrentUser() {
    const response = await this.client.get('/users/me');
    return response.data;
  }

  // Lesson methods
  async getLessons() {
    const response = await this.client.get('/lessons');
    return response.data;
  }

  async getLessonById(id: number) {
    const response = await this.client.get(`/lessons/${id}`);
    return response.data;
  }

  async getLessonWords(lessonId: number) {
    const response = await this.client.get(`/lessons/${lessonId}/words`);
    return response.data;
  }

  // Stats methods
  async getDashboardStats() {
    const response = await this.client.get('/stats/dashboard');
    return response.data;
  }

  async getStudentStats(studentId: number) {
    const response = await this.client.get(`/stats/student/${studentId}`);
    return response.data;
  }

  // Search methods
  async searchLessons(query: string) {
    const response = await this.client.get(`/search/lessons?query=${encodeURIComponent(query)}`);
    return response.data;
  }

  async searchWords(query: string) {
    const response = await this.client.get(`/search/words?query=${encodeURIComponent(query)}`);
    return response.data;
  }
}

export const apiClient = new ApiClient();
```

### 4. Контекст аутентификации

```typescript
// src/context/AuthContext.tsx
import React, { createContext, useContext, useState, useEffect } from 'react';
import { apiClient } from '../services/api';

interface User {
  id: number;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  role: 'STUDENT' | 'TEACHER' | 'ADMIN';
  grade?: number;
  school?: string;
}

interface AuthContextType {
  user: User | null;
  loading: boolean;
  login: (username: string, password: string) => Promise<void>;
  logout: () => void;
  register: (userData: any) => Promise<void>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    checkAuth();
  }, []);

  const checkAuth = async () => {
    try {
      const token = localStorage.getItem('token');
      if (token) {
        const userData = await apiClient.getCurrentUser();
        setUser(userData);
      }
    } catch (error) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
    } finally {
      setLoading(false);
    }
  };

  const login = async (username: string, password: string) => {
    const response = await apiClient.login(username, password);
    localStorage.setItem('token', response.token);
    localStorage.setItem('user', JSON.stringify(response.user));
    setUser(response.user);
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setUser(null);
  };

  const register = async (userData: any) => {
    const response = await apiClient.register(userData);
    localStorage.setItem('token', response.token);
    localStorage.setItem('user', JSON.stringify(response.user));
    setUser(response.user);
  };

  return (
    <AuthContext.Provider value={{ user, loading, login, logout, register }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
```

### 5. Защищенные маршруты

```typescript
// src/components/auth/ProtectedRoute.tsx
import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

interface ProtectedRouteProps {
  children: React.ReactNode;
  allowedRoles?: string[];
}

export const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ 
  children, 
  allowedRoles 
}) => {
  const { user, loading } = useAuth();
  const location = useLocation();

  if (loading) {
    return <div>Loading...</div>;
  }

  if (!user) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  if (allowedRoles && !allowedRoles.includes(user.role)) {
    return <Navigate to="/unauthorized" replace />;
  }

  return <>{children}</>;
};
```

### 6. Пример компонента дашборда

```typescript
// src/components/dashboard/StudentDashboard.tsx
import React, { useState, useEffect } from 'react';
import { 
  Box, 
  Grid, 
  Card, 
  CardContent, 
  Typography, 
  CircularProgress 
} from '@mui/material';
import { useAuth } from '../../context/AuthContext';
import { apiClient } from '../../services/api';

export const StudentDashboard: React.FC = () => {
  const { user } = useAuth();
  const [stats, setStats] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (user) {
      loadStats();
    }
  }, [user]);

  const loadStats = async () => {
    try {
      const data = await apiClient.getStudentStats(user!.id);
      setStats(data);
    } catch (error) {
      console.error('Error loading stats:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box p={3}>
      <Typography variant="h4" gutterBottom>
        Добро пожаловать, {user?.firstName}!
      </Typography>
      
      <Grid container spacing={3}>
        <Grid item xs={12} md={4}>
          <Card>
            <CardContent>
              <Typography variant="h6">Изучено слов</Typography>
              <Typography variant="h4">{stats?.completedWords || 0}</Typography>
            </CardContent>
          </Card>
        </Grid>
        
        <Grid item xs={12} md={4}>
          <Card>
            <CardContent>
              <Typography variant="h6">Средний балл</Typography>
              <Typography variant="h4">
                {stats?.averageScore ? Math.round(stats.averageScore) : 0}%
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        
        <Grid item xs={12} md={4}>
          <Card>
            <CardContent>
              <Typography variant="h6">Уроков доступно</Typography>
              <Typography variant="h4">{stats?.totalLessons || 0}</Typography>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Box>
  );
};
```

## 🔄 Следующие шаги:

1. **Создать React приложение** с предложенной структурой
2. **Настроить маршрутизацию** с защищенными маршрутами
3. **Реализовать компоненты** для каждой роли пользователя
4. **Добавить обработку ошибок** и состояний загрузки
5. **Протестировать интеграцию** с бекендом

## 📝 Примечания:

- Бекенд готов к работе с фронтендом
- Все необходимые эндпоинты реализованы
- CORS настроен для локальной разработки
- Тестовые данные доступны для демонстрации
- Swagger UI доступен для тестирования API

Для начала разработки фронтенда используйте тестовые аккаунты из `TEST_ACCOUNTS.md`. 