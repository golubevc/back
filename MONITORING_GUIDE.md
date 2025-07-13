# Руководство по мониторингу Anecole Backend

## 🚀 Быстрый старт

### Запуск мониторинга
```bash
./docker-compose-manager.sh monitoring
```

### Доступ к сервисам
- **Grafana**: http://localhost:3000 (admin/admin123)
- **Prometheus**: http://localhost:9090
- **cAdvisor**: http://localhost:8081
- **Node Exporter**: http://localhost:9100

## 📊 Доступные дашборды

### 1. Spring Boot Performance Metrics
**UID**: `spring-boot-metrics`
- **RPS** (Requests Per Second)
- **RT** (Response Time) - P95 & P99
- **SLA** (Success Rate) - 2xx Responses
- **Error Rate** - 5xx Responses
- **JVM Memory Usage**
- **Database Connection Pool**
- **Requests by Endpoint**
- **Response Time by Endpoint**

### 2. HTTP Status Monitoring
**UID**: `http-status-dashboard`
- **2xx Success Responses**
- **4xx Client Errors**
- **5xx Server Errors**
- **Success Rate (%)**
- **All HTTP Status Codes**
- **Requests by Endpoint and Status**

### 3. Node Exporter - System Metrics
**UID**: `node-exporter-dashboard`
- **CPU Usage (%)**
- **Memory Usage**
- **Disk Usage**
- **Network I/O**
- **Load Average**
- **Processes**

### 4. cAdvisor - Container Metrics
**UID**: `cadvisor-dashboard`
- **Container CPU Usage (%)**
- **Container Memory Usage**
- **Container Network I/O**
- **Container Disk I/O**
- **Container CPU Limits**
- **Container Memory Limits**

## 🔧 Конфигурация

### Ограничения ресурсов
Все сервисы имеют ограничения по CPU и памяти:

| Сервис | CPU Limit | Memory Limit | CPU Reservation | Memory Reservation |
|--------|-----------|--------------|-----------------|-------------------|
| PostgreSQL | 0.5 | 512M | 0.25 | 256M |
| Prometheus | 0.5 | 512M | 0.25 | 256M |
| Grafana | 0.2 | 256M | 0.1 | 128M |
| cAdvisor | 0.2 | 256M | 0.1 | 128M |
| Node Exporter | 0.1 | 128M | 0.05 | 64M |

### Масштабирование (Docker Swarm)
Для автоскейлинга используйте Docker Swarm:

```bash
# Инициализация Swarm
docker swarm init

# Запуск стека
docker stack deploy -c docker-compose.yml anecole

# Масштабирование сервиса
docker service scale anecole_anecole-backend=3
```

## 📈 Полезные запросы Prometheus

### RPS (Requests Per Second)
```promql
rate(http_requests_total[5m])
```

### Response Time P95
```promql
histogram_quantile(0.95, rate(http_request_duration_seconds_bucket[5m])) * 1000
```

### Success Rate
```promql
sum(rate(http_requests_total{status=~"2.."}[5m])) / sum(rate(http_requests_total[5m])) * 100
```

### CPU Usage
```promql
100 - (avg by (instance) (irate(node_cpu_seconds_total{mode="idle"}[5m])) * 100)
```

### Memory Usage
```promql
(node_memory_MemTotal_bytes - node_memory_MemAvailable_bytes) / node_memory_MemTotal_bytes * 100
```

### Container CPU Usage
```promql
sum(rate(container_cpu_usage_seconds_total{name!=""}[5m])) by (name) * 100
```

## 🛠️ Управление сервисами

### Статус сервисов
```bash
./docker-compose-manager.sh status
```

### Логи сервисов
```bash
./docker-compose-manager.sh logs prometheus
./docker-compose-manager.sh logs grafana
```

### Остановка/запуск
```bash
./docker-compose-manager.sh stop
./docker-compose-manager.sh monitoring
```

## 🔍 Диагностика

### Проверка targets Prometheus
```bash
curl http://localhost:9090/api/v1/targets
```

### Проверка метрик Spring Boot
```bash
curl http://localhost:8080/actuator/prometheus
```

### Проверка метрик Node Exporter
```bash
curl http://localhost:9100/metrics
```

### Проверка метрик cAdvisor
```bash
curl http://localhost:8081/metrics
```

## 📝 Настройка алертов

Для настройки алертов добавьте в `monitoring/prometheus.yml`:

```yaml
rule_files:
  - "alerts.yml"

alerting:
  alertmanagers:
    - static_configs:
        - targets:
          - alertmanager:9093
```

## 🚨 Типичные проблемы

### 1. Prometheus не собирает метрики
- Проверьте доступность targets: http://localhost:9090/targets
- Убедитесь, что Spring Boot приложение запущено и доступно
- Проверьте конфигурацию в `monitoring/prometheus.yml`

### 2. Grafana не показывает данные
- Проверьте подключение к Prometheus в Data Sources
- Убедитесь, что Prometheus собирает метрики
- Проверьте запросы в панелях дашбордов

### 3. Node Exporter не работает на macOS
- Используйте исправленную конфигурацию в docker-compose.yml
- Проверьте права доступа к файловой системе

## 📚 Дополнительные ресурсы

- [Prometheus Query Language](https://prometheus.io/docs/prometheus/latest/querying/)
- [Grafana Documentation](https://grafana.com/docs/)
- [cAdvisor Metrics](https://github.com/google/cadvisor/blob/master/docs/storage/prometheus.md)
- [Node Exporter Metrics](https://prometheus.io/docs/guides/node-exporter/)

## 🔄 Обновление дашбордов

Дашборды автоматически загружаются из папки `monitoring/grafana/dashboards/`.
Для обновления:

1. Отредактируйте JSON файлы дашбордов
2. Перезапустите Grafana: `./docker-compose-manager.sh restart grafana`

---

**Автор**: Anecole Development Team  
**Версия**: 1.0  
**Дата**: 2025-01-13 