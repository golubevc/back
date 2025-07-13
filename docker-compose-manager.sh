#!/bin/bash

# Скрипт для управления Docker Compose сервисами Anecole
# Автор: Anecole Development Team

set -e

# Цвета для вывода
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Конфигурация
COMPOSE_FILE="docker-compose.yml"
PROJECT_NAME="anecole"

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

log_header() {
    echo -e "${CYAN}================================${NC}"
    echo -e "${CYAN}$1${NC}"
    echo -e "${CYAN}================================${NC}"
}

# Проверка зависимостей
check_dependencies() {
    log_info "Проверка зависимостей..."
    
    if ! command -v docker &> /dev/null; then
        log_error "Docker не установлен. Установите Docker для продолжения."
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        log_error "Docker Compose не установлен. Установите Docker Compose для продолжения."
        exit 1
    fi
    
    if ! docker info &> /dev/null; then
        log_error "Docker не запущен. Запустите Docker для продолжения."
        exit 1
    fi
    
    log_success "Зависимости проверены"
}

# Запуск базовых сервисов (только PostgreSQL)
start_basic() {
    log_header "Запуск базовых сервисов"
    
    docker-compose -f "$COMPOSE_FILE" -p "$PROJECT_NAME" up -d postgres
    
    log_info "Ожидание готовности PostgreSQL..."
    sleep 10
    
    if docker-compose -f "$COMPOSE_FILE" -p "$PROJECT_NAME" ps postgres | grep -q "Up"; then
        log_success "PostgreSQL запущен и готов к работе"
        log_info "Подключение: localhost:5432"
        log_info "База данных: anecole_db"
        log_info "Пользователь: anecole_user"
        log_info "Пароль: anecole_password"
    else
        log_error "Ошибка запуска PostgreSQL"
        exit 1
    fi
}

# Запуск всех сервисов
start_all() {
    log_header "Запуск всех сервисов"
    
    docker-compose -f "$COMPOSE_FILE" -p "$PROJECT_NAME" up -d
    
    log_info "Ожидание готовности сервисов..."
    sleep 15
    
    log_success "Все сервисы запущены"
    show_status
}

# Запуск с дополнительными инструментами
start_with_tools() {
    log_header "Запуск с инструментами разработки"
    
    docker-compose -f "$COMPOSE_FILE" -p "$PROJECT_NAME" --profile tools up -d
    
    log_info "Ожидание готовности сервисов..."
    sleep 15
    
    log_success "Сервисы с инструментами запущены"
    show_status
}

# Запуск с кэшированием
start_with_cache() {
    log_header "Запуск с кэшированием"
    
    docker-compose -f "$COMPOSE_FILE" -p "$PROJECT_NAME" --profile cache up -d
    
    log_info "Ожидание готовности сервисов..."
    sleep 15
    
    log_success "Сервисы с кэшированием запущены"
    show_status
}

# Запуск с поиском
start_with_search() {
    log_header "Запуск с поиском"
    
    docker-compose -f "$COMPOSE_FILE" -p "$PROJECT_NAME" --profile search up -d
    
    log_info "Ожидание готовности сервисов..."
    sleep 30
    
    log_success "Сервисы с поиском запущены"
    show_status
}

# Запуск с мониторингом
start_with_monitoring() {
    log_header "Запуск с мониторингом"
    
    docker-compose -f "$COMPOSE_FILE" -p "$PROJECT_NAME" --profile monitoring up -d
    
    log_info "Ожидание готовности сервисов..."
    sleep 20
    
    log_success "Сервисы с мониторингом запущены"
    show_status
}

# Остановка сервисов
stop_services() {
    log_header "Остановка сервисов"
    
    docker-compose -f "$COMPOSE_FILE" -p "$PROJECT_NAME" down
    
    log_success "Сервисы остановлены"
}

# Остановка с удалением данных
stop_and_clean() {
    log_header "Остановка и очистка данных"
    
    docker-compose -f "$COMPOSE_FILE" -p "$PROJECT_NAME" down -v
    
    log_success "Сервисы остановлены и данные очищены"
}

# Показать статус сервисов
show_status() {
    log_header "Статус сервисов"
    
    docker-compose -f "$COMPOSE_FILE" -p "$PROJECT_NAME" ps
    
    echo
    log_info "Доступные сервисы:"
    echo "  PostgreSQL:     localhost:5432"
    echo "  pgAdmin:        http://localhost:5050 (admin@anecole.com / admin123)"
    echo "  Redis:          localhost:6379"
    echo "  Elasticsearch:  http://localhost:9200"
    echo "  Kibana:         http://localhost:5601"
    echo "  Prometheus:     http://localhost:9090"
    echo "  Grafana:        http://localhost:3000 (admin / admin123)"
}

# Показать логи
show_logs() {
    local service=${1:-""}
    
    if [[ -z "$service" ]]; then
        log_header "Логи всех сервисов"
        docker-compose -f "$COMPOSE_FILE" -p "$PROJECT_NAME" logs -f
    else
        log_header "Логи сервиса: $service"
        docker-compose -f "$COMPOSE_FILE" -p "$PROJECT_NAME" logs -f "$service"
    fi
}

# Перезапуск сервиса
restart_service() {
    local service=$1
    
    if [[ -z "$service" ]]; then
        log_error "Укажите имя сервиса для перезапуска"
        exit 1
    fi
    
    log_header "Перезапуск сервиса: $service"
    
    docker-compose -f "$COMPOSE_FILE" -p "$PROJECT_NAME" restart "$service"
    
    log_success "Сервис $service перезапущен"
}

# Резервное копирование базы данных
backup_database() {
    local backup_file="backup_$(date +%Y%m%d_%H%M%S).sql"
    
    log_header "Резервное копирование базы данных"
    
    docker-compose -f "$COMPOSE_FILE" -p "$PROJECT_NAME" exec -T postgres \
        pg_dump -U anecole_user -d anecole_db > "$backup_file"
    
    if [[ $? -eq 0 ]]; then
        log_success "Резервная копия создана: $backup_file"
    else
        log_error "Ошибка создания резервной копии"
        exit 1
    fi
}

# Восстановление базы данных
restore_database() {
    local backup_file=$1
    
    if [[ -z "$backup_file" ]]; then
        log_error "Укажите файл резервной копии для восстановления"
        exit 1
    fi
    
    if [[ ! -f "$backup_file" ]]; then
        log_error "Файл резервной копии не найден: $backup_file"
        exit 1
    fi
    
    log_header "Восстановление базы данных из: $backup_file"
    
    docker-compose -f "$COMPOSE_FILE" -p "$PROJECT_NAME" exec -T postgres \
        psql -U anecole_user -d anecole_db < "$backup_file"
    
    if [[ $? -eq 0 ]]; then
        log_success "База данных восстановлена"
    else
        log_error "Ошибка восстановления базы данных"
        exit 1
    fi
}

# Проверка здоровья сервисов
health_check() {
    log_header "Проверка здоровья сервисов"
    
    # Проверка PostgreSQL
    if docker-compose -f "$COMPOSE_FILE" -p "$PROJECT_NAME" exec -T postgres pg_isready -U anecole_user -d anecole_db &> /dev/null; then
        log_success "PostgreSQL: OK"
    else
        log_error "PostgreSQL: ERROR"
    fi
    
    # Проверка Redis (если запущен)
    if docker-compose -f "$COMPOSE_FILE" -p "$PROJECT_NAME" ps redis &> /dev/null; then
        if docker-compose -f "$COMPOSE_FILE" -p "$PROJECT_NAME" exec -T redis redis-cli ping &> /dev/null; then
            log_success "Redis: OK"
        else
            log_error "Redis: ERROR"
        fi
    fi
    
    # Проверка Elasticsearch (если запущен)
    if docker-compose -f "$COMPOSE_FILE" -p "$PROJECT_NAME" ps elasticsearch &> /dev/null; then
        if curl -s http://localhost:9200/_cluster/health &> /dev/null; then
            log_success "Elasticsearch: OK"
        else
            log_error "Elasticsearch: ERROR"
        fi
    fi
}

# Показать справку
show_help() {
    log_header "Справка по использованию"
    
    echo "Использование: $0 [КОМАНДА] [ПАРАМЕТРЫ]"
    echo
    echo "Команды:"
    echo "  basic              Запуск только PostgreSQL"
    echo "  all                Запуск всех сервисов"
    echo "  tools              Запуск с pgAdmin"
    echo "  cache              Запуск с Redis"
    echo "  search             Запуск с Elasticsearch и Kibana"
    echo "  monitoring         Запуск с Prometheus и Grafana"
    echo "  stop               Остановка всех сервисов"
    echo "  clean              Остановка и очистка данных"
    echo "  status             Показать статус сервисов"
    echo "  logs [СЕРВИС]      Показать логи (всех или конкретного сервиса)"
    echo "  restart СЕРВИС     Перезапустить сервис"
    echo "  backup             Создать резервную копию БД"
    echo "  restore ФАЙЛ       Восстановить БД из резервной копии"
    echo "  health             Проверка здоровья сервисов"
    echo "  help               Показать эту справку"
    echo
    echo "Примеры:"
    echo "  $0 basic           # Запустить только PostgreSQL"
    echo "  $0 all             # Запустить все сервисы"
    echo "  $0 logs postgres   # Показать логи PostgreSQL"
    echo "  $0 backup          # Создать резервную копию"
    echo "  $0 restore backup_20231201_120000.sql"
}

# Основная функция
main() {
    check_dependencies
    
    case "${1:-help}" in
        basic)
            start_basic
            ;;
        all)
            start_all
            ;;
        tools)
            start_with_tools
            ;;
        cache)
            start_with_cache
            ;;
        search)
            start_with_search
            ;;
        monitoring)
            start_with_monitoring
            ;;
        stop)
            stop_services
            ;;
        clean)
            stop_and_clean
            ;;
        status)
            show_status
            ;;
        logs)
            show_logs "$2"
            ;;
        restart)
            restart_service "$2"
            ;;
        backup)
            backup_database
            ;;
        restore)
            restore_database "$2"
            ;;
        health)
            health_check
            ;;
        help|--help|-h)
            show_help
            ;;
        *)
            log_error "Неизвестная команда: $1"
            show_help
            exit 1
            ;;
    esac
}

# Обработка сигналов
trap 'log_error "Операция прервана пользователем"; exit 1' INT TERM

# Запуск основной функции
main "$@" 