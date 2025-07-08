# Тестирование Backend проекта

Этот документ описывает структуру тестов для backend проекта Anecole.

## Структура тестов

```
src/test/java/com/school/anecole/
├── controller/           # Тесты контроллеров
│   ├── AuthControllerTest.java
│   ├── LessonControllerTest.java
│   ├── StudentControllerTest.java
│   └── TeacherControllerTest.java
├── service/             # Тесты сервисов
│   ├── AuthServiceTest.java
│   ├── LessonServiceTest.java
│   ├── StudentServiceTest.java
│   └── TeacherServiceTest.java
├── model/               # Тесты моделей
│   └── UserTest.java
├── dto/                 # Тесты DTO классов
│   ├── AuthRequestTest.java
│   ├── AuthResponseTest.java
│   └── RegisterRequestTest.java
├── repository/          # Тесты репозиториев
│   └── UserRepositoryTest.java
├── security/            # Тесты безопасности
│   └── JwtTokenProviderTest.java
└── integration/         # Интеграционные тесты
    └── AuthIntegrationTest.java
```

## Типы тестов

### 1. Юнит тесты (Unit Tests)
- **Модели**: Тестируют структуру данных, геттеры/сеттеры, валидацию
- **DTO**: Тестируют передачу данных между слоями
- **Сервисы**: Тестируют бизнес-логику с использованием Mockito
- **Контроллеры**: Тестируют HTTP endpoints с использованием MockMvc

### 2. Интеграционные тесты (Integration Tests)
- Тестируют взаимодействие между компонентами
- Используют реальную базу данных (H2 in-memory)
- Тестируют полный flow от HTTP запроса до ответа

### 3. Тесты репозиториев (Repository Tests)
- Тестируют взаимодействие с базой данных
- Используют @DataJpaTest для изолированного тестирования JPA

## Запуск тестов

### Запуск всех тестов
```bash
mvn test
```

### Запуск конкретного теста
```bash
mvn test -Dtest=AuthServiceTest
```

### Запуск тестов с подробным выводом
```bash
mvn test -Dspring.profiles.active=test
```

### Запуск только юнит тестов
```bash
mvn test -Dtest="*Test" -DfailIfNoTests=false
```

### Запуск только интеграционных тестов
```bash
mvn test -Dtest="*IntegrationTest" -DfailIfNoTests=false
```

## Конфигурация тестов

### application-test.yml
- Использует H2 in-memory базу данных
- Включает подробное логирование SQL запросов
- Настроен для быстрого выполнения тестов

### Аннотации
- `@ExtendWith(MockitoExtension.class)` - для юнит тестов с Mockito
- `@SpringBootTest` - для интеграционных тестов
- `@DataJpaTest` - для тестов репозиториев
- `@ActiveProfiles("test")` - для использования тестового профиля
- `@Transactional` - для автоматического отката изменений в БД

## Покрытие тестами

### Модели (Models)
- ✅ User - полное покрытие
- ⏳ Lesson - требует создания
- ⏳ Class - требует создания
- ⏳ Word - требует создания
- ⏳ StudentProgress - требует создания

### Сервисы (Services)
- ✅ AuthService - полное покрытие
- ✅ LessonService - полное покрытие
- ✅ StudentService - полное покрытие
- ✅ TeacherService - полное покрытие

### Контроллеры (Controllers)
- ✅ AuthController - полное покрытие
- ✅ LessonController - полное покрытие
- ✅ StudentController - полное покрытие
- ✅ TeacherController - полное покрытие

### DTO
- ✅ AuthRequest - полное покрытие
- ✅ AuthResponse - полное покрытие
- ✅ RegisterRequest - полное покрытие

### Безопасность (Security)
- ✅ JwtTokenProvider - полное покрытие
- ⏳ JwtAuthenticationFilter - требует создания
- ⏳ CustomUserDetailsService - требует создания

### Репозитории (Repositories)
- ✅ UserRepository - полное покрытие
- ⏳ LessonRepository - требует создания
- ⏳ ClassRepository - требует создания
- ⏳ WordRepository - требует создания
- ⏳ StudentProgressRepository - требует создания

## Лучшие практики

### 1. Именование тестов
- Используйте описательные имена: `testLoginSuccess()`, `testRegisterDuplicateUsername()`
- Следуйте паттерну: `test[MethodName][Scenario]()`

### 2. Структура тестов
- **Arrange**: Подготовка данных и моков
- **Act**: Выполнение тестируемого метода
- **Assert**: Проверка результатов

### 3. Изоляция тестов
- Каждый тест должен быть независимым
- Используйте `@BeforeEach` для общей настройки
- Очищайте состояние между тестами

### 4. Мокирование
- Мокируйте только внешние зависимости
- Используйте `@Mock` и `@InjectMocks` для сервисов
- Проверяйте вызовы моков с помощью `verify()`

### 5. Assertions
- Используйте специфичные assertions: `assertEquals()`, `assertNotNull()`
- Проверяйте как позитивные, так и негативные сценарии
- Тестируйте граничные случаи

## Отладка тестов

### Включение подробного логирования
```yaml
logging:
  level:
    com.school.anecole: DEBUG
    org.springframework.security: DEBUG
    org.hibernate.SQL: DEBUG
```

### Запуск с отладкой
```bash
mvn test -Dspring.profiles.active=test -Dlogging.level.com.school.anecole=DEBUG
```

### Просмотр SQL запросов
В тестовом профиле включено логирование SQL запросов для отладки взаимодействия с базой данных.

## Добавление новых тестов

1. Создайте тестовый класс в соответствующей папке
2. Следуйте существующим паттернам именования
3. Используйте подходящие аннотации для типа теста
4. Добавьте тесты для всех публичных методов
5. Покройте позитивные и негативные сценарии
6. Обновите этот README при добавлении новых тестов

## Проблемы и решения

### Ошибки компиляции
- Убедитесь, что все зависимости добавлены в `pom.xml`
- Проверьте импорты в тестовых классах

### Ошибки выполнения тестов
- Проверьте конфигурацию в `application-test.yml`
- Убедитесь, что база данных доступна
- Проверьте логи для деталей ошибок

### Медленное выполнение тестов
- Используйте `@DirtiesContext` только при необходимости
- Оптимизируйте настройку моков
- Рассмотрите использование `@TestPropertySource` для специфичных настроек 