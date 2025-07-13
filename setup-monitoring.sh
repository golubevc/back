#!/bin/bash

# Скрипт для настройки мониторинга Spring Boot с Prometheus и Grafana
# Автор: Anecole Development Team

set -e

# Цвета для вывода
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

log_header() {
    echo -e "${CYAN}================================${NC}"
    echo -e "${CYAN}$1${NC}"
    echo -e "${CYAN}================================${NC}"
}

# Проверка зависимостей
check_dependencies() {
    log_info "Проверка зависимостей..."
    
    if ! command -v docker &> /dev/null; then
        log_error "Docker не установлен"
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        log_error "Docker Compose не установлен"
        exit 1
    fi
    
    log_success "Зависимости проверены"
}

# Запуск мониторинга
start_monitoring() {
    log_header "Запуск системы мониторинга"
    
    # Запускаем мониторинг
    ./docker-compose-manager.sh monitoring
    
    log_info "Ожидание готовности сервисов..."
    sleep 30
    
    log_success "Мониторинг запущен"
    log_info "Prometheus: http://localhost:9090"
    log_info "Grafana: http://localhost:3000 (admin/admin123)"
}

# Настройка Spring Boot метрик
setup_spring_metrics() {
    log_header "Настройка Spring Boot метрик"
    
    # Проверяем, есть ли уже зависимости в pom.xml
    if grep -q "spring-boot-starter-actuator" pom.xml; then
        log_info "Spring Boot Actuator уже настроен"
    else
        log_warning "Добавьте в pom.xml зависимости для мониторинга:"
        echo
        echo "<!-- Spring Boot Actuator -->"
        echo "<dependency>"
        echo "    <groupId>org.springframework.boot</groupId>"
        echo "    <artifactId>spring-boot-starter-actuator</artifactId>"
        echo "</dependency>"
        echo
        echo "<!-- Micrometer Prometheus Registry -->"
        echo "<dependency>"
        echo "    <groupId>io.micrometer</groupId>"
        echo "    <artifactId>micrometer-registry-prometheus</artifactId>"
        echo "</dependency>"
        echo
    fi
    
    # Создаем конфигурацию для метрик
    if [[ ! -f "src/main/resources/application-monitoring.yml" ]]; then
        log_info "Создание конфигурации мониторинга..."
        
        cat > src/main/resources/application-monitoring.yml << 'EOF'
# Конфигурация мониторинга Spring Boot
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus,env,configprops
      base-path: /actuator
  endpoint:
    health:
      show-details: always
      show-components: always
    prometheus:
      enabled: true
  metrics:
    export:
      prometheus:
        enabled: true
    tags:
      application: anecole-backend
      environment: development
    distribution:
      percentiles-histogram:
        http.server.requests: true
      percentiles:
        http.server.requests: 0.5, 0.95, 0.99
  web:
    server:
      request:
        log-enabled: true

# Настройки логирования для мониторинга
logging:
  level:
    org.springframework.web: INFO
    org.springframework.security: INFO
    com.school.anecole: DEBUG
    io.micrometer: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"

# Настройки безопасности для актуатора
spring:
  security:
    user:
      name: admin
      password: admin123
EOF
        
        log_success "Конфигурация мониторинга создана"
    else
        log_info "Конфигурация мониторинга уже существует"
    fi
}

# Проверка доступности сервисов
check_services() {
    log_header "Проверка доступности сервисов"
    
    # Проверка Prometheus
    if curl -s http://localhost:9090/api/v1/status/config > /dev/null; then
        log_success "Prometheus доступен"
    else
        log_error "Prometheus недоступен"
        return 1
    fi
    
    # Проверка Grafana
    if curl -s http://localhost:3000/api/health > /dev/null; then
        log_success "Grafana доступен"
    else
        log_error "Grafana недоступен"
        return 1
    fi
    
    return 0
}

# Настройка дашбордов Grafana
setup_grafana_dashboards() {
    log_header "Настройка дашбордов Grafana"
    
    log_info "Ожидание готовности Grafana..."
    sleep 10
    
    # Проверяем, что дашборды загружены
    if curl -s http://localhost:3000/api/search?type=dash-db | grep -q "spring-boot-metrics"; then
        log_success "Дашборды загружены автоматически"
    else
        log_warning "Дашборды не загружены автоматически. Загрузите их вручную:"
        log_info "1. Откройте http://localhost:3000"
        log_info "2. Войдите с admin/admin123"
        log_info "3. Перейдите в Dashboards -> Import"
        log_info "4. Загрузите файлы из monitoring/grafana/dashboards/"
    fi
}

# Генерация тестовой нагрузки
generate_test_load() {
    log_header "Генерация тестовой нагрузки"
    
    log_info "Запуск быстрого теста API..."
    if ./quick-api-test.sh; then
        log_success "Базовый тест пройден"
    else
        log_warning "Базовый тест не пройден"
    fi
    
    log_info "Для генерации нагрузки запустите:"
    log_info "./run-load-testing.sh"
}

# Показать полезные команды
show_useful_commands() {
    log_header "Полезные команды"
    
    echo "🔍 Проверка метрик Spring Boot:"
    echo "  curl http://localhost:8080/actuator/metrics"
    echo "  curl http://localhost:8080/actuator/prometheus"
    echo
    echo "📊 Просмотр дашбордов:"
    echo "  Prometheus: http://localhost:9090"
    echo "  Grafana: http://localhost:3000"
    echo
    echo "📈 Полезные запросы Prometheus:"
    echo "  # RPS"
    echo "  rate(http_requests_total[5m])"
    echo
    echo "  # Response Time P95"
    echo "  histogram_quantile(0.95, rate(http_request_duration_seconds_bucket[5m]))"
    echo
    echo "  # Success Rate"
    echo "  sum(rate(http_requests_total{status=~\"2..\"}[5m])) / sum(rate(http_requests_total[5m])) * 100"
    echo
    echo "🛠️ Управление сервисами:"
    echo "  ./docker-compose-manager.sh status"
    echo "  ./docker-compose-manager.sh logs prometheus"
    echo "  ./docker-compose-manager.sh logs grafana"
}

# Основная функция
main() {
    log_header "Настройка мониторинга Spring Boot"
    
    check_dependencies
    setup_spring_metrics
    start_monitoring
    
    if check_services; then
        setup_grafana_dashboards
        generate_test_load
        show_useful_commands
        
        log_success "Мониторинг настроен успешно!"
        log_info "Откройте Grafana: http://localhost:3000"
        log_info "Логин: admin / admin123"
    else
        log_error "Ошибка при настройке мониторинга"
        exit 1
    fi
}

# Обработка сигналов
trap 'log_error "Операция прервана пользователем"; exit 1' INT TERM

# Запуск основной функции
main "$@" 