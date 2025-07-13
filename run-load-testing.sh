#!/bin/bash

# Скрипт для запуска нагрузочного тестирования Anecole API
# Автор: Anecole Development Team
# Дата: $(date)

set -e

# Цвета для вывода
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Конфигурация
API_BASE_URL="http://localhost:8080/api"
TEST_DURATION="5m"
CONCURRENT_USERS=10
RAMP_UP_TIME="30s"

# Функции для логирования
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

# Проверка зависимостей
check_dependencies() {
    log_info "Проверка зависимостей..."
    
    # Проверка Node.js
    if ! command -v node &> /dev/null; then
        log_error "Node.js не установлен. Установите Node.js для запуска Artillery."
        exit 1
    fi
    
    # Проверка npm
    if ! command -v npm &> /dev/null; then
        log_error "npm не установлен."
        exit 1
    fi
    
    # Проверка Artillery
    if ! command -v artillery &> /dev/null; then
        log_warning "Artillery не установлен. Устанавливаем..."
        npm install -g artillery
    fi
    
    # Проверка JMeter (опционально)
    if command -v jmeter &> /dev/null; then
        log_success "JMeter найден"
        JMETER_AVAILABLE=true
    else
        log_warning "JMeter не найден. Установите JMeter для дополнительного тестирования."
        JMETER_AVAILABLE=false
    fi
    
    log_success "Зависимости проверены"
}

# Проверка доступности API
check_api_health() {
    log_info "Проверка доступности API..."
    
    # Проверяем, что бекенд запущен
    if ! curl -s -f "$API_BASE_URL/auth/login" > /dev/null 2>&1; then
        log_error "API недоступен по адресу $API_BASE_URL"
        log_error "Убедитесь, что бекенд запущен: ./mvnw spring-boot:run"
        exit 1
    fi
    
    # Тестируем логин
    RESPONSE=$(curl -s -X POST "$API_BASE_URL/auth/login" \
        -H "Content-Type: application/json" \
        -d '{"username":"student1","password":"password"}' || echo "ERROR")
    
    if [[ "$RESPONSE" == "ERROR" ]] || [[ "$RESPONSE" == *"error"* ]]; then
        log_error "Ошибка при тестировании логина"
        exit 1
    fi
    
    log_success "API доступен и работает корректно"
}

# Создание директорий для отчетов
create_directories() {
    log_info "Создание директорий для отчетов..."
    
    mkdir -p reports
    mkdir -p logs
    mkdir -p results
    
    log_success "Директории созданы"
}

# Запуск Artillery тестирования
run_artillery_test() {
    log_info "Запуск Artillery нагрузочного тестирования..."
    
    local timestamp=$(date +"%Y%m%d_%H%M%S")
    local report_file="reports/artillery_report_${timestamp}.json"
    local log_file="logs/artillery_${timestamp}.log"
    
    # Запускаем Artillery
    artillery run \
        --output "$report_file" \
        --config load-testing-artillery.yml \
        2>&1 | tee "$log_file"
    
    if [ $? -eq 0 ]; then
        log_success "Artillery тестирование завершено успешно"
        log_info "Отчет сохранен в: $report_file"
        log_info "Лог сохранен в: $log_file"
    else
        log_error "Ошибка при выполнении Artillery тестирования"
        exit 1
    fi
}

# Запуск JMeter тестирования (если доступен)
run_jmeter_test() {
    if [ "$JMETER_AVAILABLE" = true ]; then
        log_info "Запуск JMeter нагрузочного тестирования..."
        
        local timestamp=$(date +"%Y%m%d_%H%M%S")
        local results_dir="results/jmeter_${timestamp}"
        
        mkdir -p "$results_dir"
        
        # Запускаем JMeter
        jmeter -n \
            -t load-testing-jmeter.jmx \
            -l "$results_dir/results.jtl" \
            -e \
            -o "$results_dir/report" \
            -JbaseUrl="$API_BASE_URL"
        
        if [ $? -eq 0 ]; then
            log_success "JMeter тестирование завершено успешно"
            log_info "Результаты сохранены в: $results_dir"
        else
            log_error "Ошибка при выполнении JMeter тестирования"
        fi
    else
        log_warning "JMeter недоступен, пропускаем JMeter тестирование"
    fi
}

# Генерация отчета
generate_report() {
    log_info "Генерация итогового отчета..."
    
    local timestamp=$(date +"%Y%m%d_%H%M%S")
    local report_file="reports/load_testing_summary_${timestamp}.md"
    
    cat > "$report_file" << EOF
# Отчет о нагрузочном тестировании Anecole API

**Дата:** $(date)  
**Время выполнения:** $TEST_DURATION  
**Количество пользователей:** $CONCURRENT_USERS  
**Время разогрева:** $RAMP_UP_TIME  

## Результаты тестирования

### Artillery
- Конфигурация: load-testing-artillery.yml
- Процессор: load-testing-processor.js
- Отчеты: reports/artillery_report_*.json

### JMeter
- Конфигурация: load-testing-jmeter.jmx
- Результаты: results/jmeter_*/

## Метрики производительности

### Целевые показатели
- Время отклика P95: < 2000ms
- Время отклика P99: < 5000ms
- Успешность запросов: > 95%
- Ошибки 5xx: < 1%

### Результаты
*Результаты будут добавлены после выполнения тестирования*

## Рекомендации

1. **Мониторинг ресурсов** - отслеживайте использование CPU и памяти
2. **Масштабирование** - при необходимости увеличьте ресурсы сервера
3. **Кэширование** - рассмотрите возможность кэширования часто запрашиваемых данных
4. **Оптимизация БД** - проверьте индексы и запросы к базе данных

## Следующие шаги

1. Анализ результатов тестирования
2. Выявление узких мест производительности
3. Оптимизация проблемных эндпоинтов
4. Повторное тестирование после оптимизации

---
*Отчет сгенерирован автоматически*
EOF
    
    log_success "Отчет сгенерирован: $report_file"
}

# Очистка временных файлов
cleanup() {
    log_info "Очистка временных файлов..."
    
    # Удаляем временные файлы, если они есть
    rm -f *.tmp
    rm -f *.log.tmp
    
    log_success "Очистка завершена"
}

# Основная функция
main() {
    log_info "Начало нагрузочного тестирования Anecole API"
    log_info "Время: $(date)"
    log_info "API URL: $API_BASE_URL"
    
    # Выполняем проверки
    check_dependencies
    check_api_health
    create_directories
    
    # Запускаем тестирование
    run_artillery_test
    run_jmeter_test
    
    # Генерируем отчет
    generate_report
    
    # Очистка
    cleanup
    
    log_success "Нагрузочное тестирование завершено успешно!"
    log_info "Проверьте директорию reports/ для получения результатов"
}

# Обработка сигналов
trap 'log_error "Тестирование прервано пользователем"; exit 1' INT TERM

# Запуск основной функции
main "$@" 