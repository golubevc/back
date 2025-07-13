#!/bin/bash

# Быстрый тест API Anecole
# Проверяет основные эндпоинты перед нагрузочным тестированием

set -e

# Цвета
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

API_BASE="http://localhost:8080/api"
TOKEN=""

log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

# Проверка доступности API
check_api_availability() {
    log_info "Проверка доступности API..."
    
    if curl -s -f "$API_BASE/auth/login" > /dev/null 2>&1; then
        log_success "API доступен"
        return 0
    else
        log_error "API недоступен по адресу $API_BASE"
        return 1
    fi
}

# Тест логина студента
test_student_login() {
    log_info "Тест логина студента..."
    
    RESPONSE=$(curl -s -X POST "$API_BASE/auth/login" \
        -H "Content-Type: application/json" \
        -d '{"username":"student1","password":"password"}' || echo "ERROR")
    
    if [[ "$RESPONSE" == "ERROR" ]]; then
        log_error "Ошибка при логине студента"
        return 1
    fi
    
    # Извлекаем токен
    TOKEN=$(echo "$RESPONSE" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
    
    if [[ -z "$TOKEN" ]]; then
        log_error "Токен не получен"
        return 1
    fi
    
    log_success "Студент успешно авторизован"
    return 0
}

# Тест получения текущего пользователя
test_get_current_user() {
    log_info "Тест получения текущего пользователя..."
    
    RESPONSE=$(curl -s -X GET "$API_BASE/users/me" \
        -H "Authorization: Bearer $TOKEN" || echo "ERROR")
    
    if [[ "$RESPONSE" == "ERROR" ]]; then
        log_error "Ошибка при получении пользователя"
        return 1
    fi
    
    if echo "$RESPONSE" | grep -q '"username":"student1"'; then
        log_success "Информация о пользователе получена"
        return 0
    else
        log_error "Неверная информация о пользователе"
        return 1
    fi
}

# Тест получения статистики дашборда
test_dashboard_stats() {
    log_info "Тест получения статистики дашборда..."
    
    RESPONSE=$(curl -s -X GET "$API_BASE/stats/dashboard" \
        -H "Authorization: Bearer $TOKEN" || echo "ERROR")
    
    if [[ "$RESPONSE" == "ERROR" ]]; then
        log_error "Ошибка при получении статистики"
        return 1
    fi
    
    if echo "$RESPONSE" | grep -q '"totalLessons"'; then
        log_success "Статистика дашборда получена"
        return 0
    else
        log_error "Неверная структура статистики"
        return 1
    fi
}

# Тест получения уроков
test_get_lessons() {
    log_info "Тест получения уроков..."
    
    RESPONSE=$(curl -s -X GET "$API_BASE/lessons" \
        -H "Authorization: Bearer $TOKEN" || echo "ERROR")
    
    if [[ "$RESPONSE" == "ERROR" ]]; then
        log_error "Ошибка при получении уроков"
        return 1
    fi
    
    if echo "$RESPONSE" | grep -q '"id"'; then
        log_success "Список уроков получен"
        return 0
    else
        log_error "Неверная структура списка уроков"
        return 1
    fi
}

# Тест поиска
test_search() {
    log_info "Тест поиска уроков..."
    
    RESPONSE=$(curl -s -X GET "$API_BASE/search/lessons?query=приветствие" \
        -H "Authorization: Bearer $TOKEN" || echo "ERROR")
    
    if [[ "$RESPONSE" == "ERROR" ]]; then
        log_error "Ошибка при поиске"
        return 1
    fi
    
    log_success "Поиск работает"
    return 0
}

# Тест логина учителя
test_teacher_login() {
    log_info "Тест логина учителя..."
    
    RESPONSE=$(curl -s -X POST "$API_BASE/auth/login" \
        -H "Content-Type: application/json" \
        -d '{"username":"teacher1","password":"password"}' || echo "ERROR")
    
    if [[ "$RESPONSE" == "ERROR" ]]; then
        log_error "Ошибка при логине учителя"
        return 1
    fi
    
    TEACHER_TOKEN=$(echo "$RESPONSE" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
    
    if [[ -z "$TEACHER_TOKEN" ]]; then
        log_error "Токен учителя не получен"
        return 1
    fi
    
    log_success "Учитель успешно авторизован"
    return 0
}

# Тест получения классов
test_get_classes() {
    log_info "Тест получения классов..."
    
    RESPONSE=$(curl -s -X GET "$API_BASE/classes" \
        -H "Authorization: Bearer $TEACHER_TOKEN" || echo "ERROR")
    
    if [[ "$RESPONSE" == "ERROR" ]]; then
        log_error "Ошибка при получении классов"
        return 1
    fi
    
    if echo "$RESPONSE" | grep -q '"id"'; then
        log_success "Список классов получен"
        return 0
    else
        log_error "Неверная структура списка классов"
        return 1
    fi
}

# Тест логина администратора
test_admin_login() {
    log_info "Тест логина администратора..."
    
    RESPONSE=$(curl -s -X POST "$API_BASE/auth/login" \
        -H "Content-Type: application/json" \
        -d '{"username":"admin","password":"password"}' || echo "ERROR")
    
    if [[ "$RESPONSE" == "ERROR" ]]; then
        log_error "Ошибка при логине администратора"
        return 1
    fi
    
    ADMIN_TOKEN=$(echo "$RESPONSE" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
    
    if [[ -z "$ADMIN_TOKEN" ]]; then
        log_error "Токен администратора не получен"
        return 1
    fi
    
    log_success "Администратор успешно авторизован"
    return 0
}

# Тест получения пользователей (только для админа)
test_get_users() {
    log_info "Тест получения пользователей..."
    
    RESPONSE=$(curl -s -X GET "$API_BASE/users" \
        -H "Authorization: Bearer $ADMIN_TOKEN" || echo "ERROR")
    
    if [[ "$RESPONSE" == "ERROR" ]]; then
        log_error "Ошибка при получении пользователей"
        return 1
    fi
    
    if echo "$RESPONSE" | grep -q '"id"'; then
        log_success "Список пользователей получен"
        return 0
    else
        log_error "Неверная структура списка пользователей"
        return 1
    fi
}

# Основная функция
main() {
    log_info "Начало быстрого тестирования API"
    log_info "Время: $(date)"
    
    local tests_passed=0
    local tests_failed=0
    
    # Массив тестов
    tests=(
        "check_api_availability"
        "test_student_login"
        "test_get_current_user"
        "test_dashboard_stats"
        "test_get_lessons"
        "test_search"
        "test_teacher_login"
        "test_get_classes"
        "test_admin_login"
        "test_get_users"
    )
    
    # Выполняем тесты
    for test in "${tests[@]}"; do
        if $test; then
            ((tests_passed++))
        else
            ((tests_failed++))
        fi
        echo
    done
    
    # Итоговый отчет
    echo "========================================"
    log_info "ИТОГОВЫЙ ОТЧЕТ"
    echo "========================================"
    log_success "Пройдено тестов: $tests_passed"
    
    if [[ $tests_failed -gt 0 ]]; then
        log_error "Провалено тестов: $tests_failed"
    else
        log_success "Все тесты пройдены успешно!"
    fi
    
    echo "========================================"
    
    if [[ $tests_failed -eq 0 ]]; then
        log_success "API готов к нагрузочному тестированию!"
        return 0
    else
        log_error "Обнаружены проблемы с API. Исправьте их перед нагрузочным тестированием."
        return 1
    fi
}

# Запуск
main "$@" 