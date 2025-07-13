# 🐳 Docker Compose для Anecole Backend

Этот документ описывает настройку и использование Docker Compose для локального тестирования и разработки Anecole Backend.

## 📋 Обзор

Docker Compose конфигурация включает в себя все необходимые сервисы для полноценной разработки и тестирования:

- **PostgreSQL** - основная база данных
- **pgAdmin** - веб-интерфейс для управления БД
- **Redis** - кэширование и сессии
- **Elasticsearch** - полнотекстовый поиск
- **Kibana** - визуализация данных Elasticsearch
- **Prometheus** - сбор метрик
- **Grafana** - визуализация метрик

## 🚀 Быстрый старт

### 1. Установка зависимостей

Убедитесь, что у вас установлены:
- Docker Desktop
- Docker Compose

### 2. Настройка переменных окружения

```bash
# Скопируйте пример файла переменных окружения
cp env.example .env

# Отредактируйте .env под свои нужды
nano .env
```

### 3. Запуск сервисов

```bash
# Запуск только PostgreSQL (базовый вариант)
./docker-compose-manager.sh basic

# Запуск всех сервисов
./docker-compose-manager.sh all

# Запуск с инструментами разработки
./docker-compose-manager.sh tools

# Запуск с кэшированием
./docker-compose-manager.sh cache

# Запуск с поиском
./docker-compose-manager.sh search

# Запуск с мониторингом
./docker-compose-manager.sh monitoring
```

## 📊 Доступные сервисы

| Сервис | URL | Описание | Профиль |
|--------|-----|----------|---------|
| PostgreSQL | localhost:5432 | База данных | Основной |
| pgAdmin | http://localhost:5050 | Управление БД | tools |
| Redis | localhost:6379 | Кэширование | cache |
| Elasticsearch | http://localhost:9200 | Поиск | search |
| Kibana | http://localhost:5601 | Визуализация | search |
| Prometheus | http://localhost:9090 | Метрики | monitoring |
| Grafana | http://localhost:3000 | Дашборды | monitoring |

## 🔧 Управление сервисами

### Основные команды

```bash
# Показать статус всех сервисов
./docker-compose-manager.sh status

# Показать логи всех сервисов
./docker-compose-manager.sh logs

# Показать логи конкретного сервиса
./docker-compose-manager.sh logs postgres

# Перезапустить сервис
./docker-compose-manager.sh restart postgres

# Остановить все сервисы
./docker-compose-manager.sh stop

# Остановить и очистить данные
./docker-compose-manager.sh clean

# Проверка здоровья сервисов
./docker-compose-manager.sh health
```

### Резервное копирование

```bash
# Создать резервную копию БД
./docker-compose-manager.sh backup

# Восстановить БД из резервной копии
./docker-compose-manager.sh restore backup_20231201_120000.sql
```

## 🗄️ База данных PostgreSQL

### Подключение

```bash
# Параметры подключения
Host: localhost
Port: 5432
Database: anecole_db
Username: anecole_user
Password: anecole_password
```

### Управление через pgAdmin

1. Откройте http://localhost:5050
2. Войдите с учетными данными:
   - Email: admin@anecole.com
   - Password: admin123
3. Добавьте новый сервер:
   - Name: Anecole Local
   - Host: postgres
   - Port: 5432
   - Database: anecole_db
   - Username: anecole_user
   - Password: anecole_password

### Прямое подключение

```bash
# Подключение к БД через Docker
docker-compose exec postgres psql -U anecole_user -d anecole_db

# Выполнение SQL запроса
docker-compose exec postgres psql -U anecole_user -d anecole_db -c "SELECT * FROM users;"
```

## 🔍 Поиск с Elasticsearch

### Проверка работы Elasticsearch

```bash
# Проверка статуса кластера
curl http://localhost:9200/_cluster/health

# Проверка индексов
curl http://localhost:9200/_cat/indices
```

### Управление через Kibana

1. Откройте http://localhost:5601
2. Перейдите в Dev Tools
3. Выполните запросы:

```json
// Проверка статуса
GET _cluster/health

// Создание индекса для уроков
PUT /anecole-lessons
{
  "mappings": {
    "properties": {
      "title": { "type": "text" },
      "description": { "type": "text" },
      "content": { "type": "text" }
    }
  }
}
```

## 📈 Мониторинг с Prometheus и Grafana

### Prometheus

1. Откройте http://localhost:9090
2. Перейдите в Status -> Targets
3. Проверьте, что все targets в состоянии UP

### Grafana

1. Откройте http://localhost:3000
2. Войдите с учетными данными:
   - Username: admin
   - Password: admin123
3. Добавьте источник данных Prometheus:
   - URL: http://prometheus:9090
   - Access: Server (default)

### Полезные метрики

```promql
# Количество HTTP запросов
http_requests_total

# Время отклика
http_request_duration_seconds

# Использование памяти JVM
jvm_memory_used_bytes

# Активные соединения с БД
hikaricp_connections_active
```

## 🔄 Профили запуска

### basic (только PostgreSQL)
```bash
docker-compose up -d postgres
```

### tools (PostgreSQL + pgAdmin)
```bash
docker-compose --profile tools up -d
```

### cache (PostgreSQL + Redis)
```bash
docker-compose --profile cache up -d
```

### search (PostgreSQL + Elasticsearch + Kibana)
```bash
docker-compose --profile search up -d
```

### monitoring (PostgreSQL + Prometheus + Grafana)
```bash
docker-compose --profile monitoring up -d
```

## ⚙️ Конфигурация

### Переменные окружения

Основные переменные в файле `.env`:

```bash
# База данных
DB_HOST=localhost
DB_PORT=5432
DB_NAME=anecole_db
DB_USERNAME=anecole_user
DB_PASSWORD=anecole_password

# JWT
JWT_SECRET=your-super-secret-jwt-key
JWT_EXPIRATION=86400000

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=redis_password

# Elasticsearch
ELASTICSEARCH_HOST=localhost
ELASTICSEARCH_PORT=9200
```

### Настройка Spring Boot

Добавьте в `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  
  redis:
    host: ${REDIS_HOST}
    port: ${REDIS_PORT}
    password: ${REDIS_PASSWORD}
  
  elasticsearch:
    rest:
      uris: ${ELASTICSEARCH_URL}
```

## 🛠️ Разработка

### Запуск приложения

```bash
# Запустите сервисы
./docker-compose-manager.sh basic

# Запустите Spring Boot приложение
./mvnw spring-boot:run
```

### Тестирование

```bash
# Быстрый тест API
./quick-api-test.sh

# Нагрузочное тестирование
./run-load-testing.sh
```

### Отладка

```bash
# Просмотр логов PostgreSQL
./docker-compose-manager.sh logs postgres

# Просмотр логов Redis
./docker-compose-manager.sh logs redis

# Просмотр логов Elasticsearch
./docker-compose-manager.sh logs elasticsearch
```

## 🚨 Устранение неполадок

### Проблемы с PostgreSQL

```bash
# Проверка статуса
./docker-compose-manager.sh health

# Перезапуск сервиса
./docker-compose-manager.sh restart postgres

# Просмотр логов
./docker-compose-manager.sh logs postgres
```

### Проблемы с памятью

```bash
# Очистка неиспользуемых ресурсов
docker system prune -f

# Очистка томов
docker volume prune -f
```

### Проблемы с портами

```bash
# Проверка занятых портов
lsof -i :5432
lsof -i :6379
lsof -i :9200

# Остановка конфликтующих сервисов
sudo lsof -ti:5432 | xargs kill -9
```

## 📋 Чек-лист

### Перед запуском
- [ ] Docker Desktop запущен
- [ ] Файл `.env` настроен
- [ ] Порт 5432 свободен
- [ ] Достаточно места на диске

### После запуска
- [ ] PostgreSQL доступен на localhost:5432
- [ ] pgAdmin доступен на localhost:5050
- [ ] Spring Boot приложение подключается к БД
- [ ] API тесты проходят успешно

## 🔗 Полезные ссылки

- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [PostgreSQL Docker Image](https://hub.docker.com/_/postgres)
- [Redis Docker Image](https://hub.docker.com/_/redis)
- [Elasticsearch Docker Image](https://www.elastic.co/guide/elasticsearch/reference/current/docker.html)
- [Prometheus Docker Image](https://prometheus.io/docs/prometheus/latest/installation/)
- [Grafana Docker Image](https://grafana.com/docs/grafana/latest/installation/docker/)

---

**Удачной разработки! 🚀** 