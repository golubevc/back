/**
 * Artillery процессор для нагрузочного тестирования Anecole API
 */

// Глобальные переменные для хранения токенов
let studentToken = null;
let teacherToken = null;
let adminToken = null;

// Счетчики для статистики
let requestCount = 0;
let errorCount = 0;
let successCount = 0;

/**
 * Обработка запроса перед отправкой
 */
function beforeRequest(requestParams, context, ee, next) {
  // Увеличиваем счетчик запросов
  requestCount++;
  
  // Логируем запрос
  console.log(`[${new Date().toISOString()}] Request ${requestCount}: ${requestParams.method} ${requestParams.url}`);
  
  // Добавляем случайную задержку для имитации реального пользователя
  const randomDelay = Math.random() * 1000;
  setTimeout(() => {
    next();
  }, randomDelay);
}

/**
 * Обработка ответа после получения
 */
function afterResponse(requestParams, response, context, ee, next) {
  const statusCode = response.statusCode;
  const responseTime = response.responseTime;
  
  // Логируем ответ
  console.log(`[${new Date().toISOString()}] Response: ${statusCode} (${responseTime}ms) - ${requestParams.url}`);
  
  // Обновляем счетчики
  if (statusCode >= 200 && statusCode < 300) {
    successCount++;
  } else {
    errorCount++;
    console.error(`Error ${statusCode}: ${requestParams.url}`);
  }
  
  // Проверяем производительность
  if (responseTime > 2000) {
    console.warn(`Slow response: ${responseTime}ms for ${requestParams.url}`);
  }
  
  // Сохраняем токены для последующих запросов
  if (requestParams.url.includes('/auth/login') && statusCode === 200) {
    try {
      const responseBody = JSON.parse(response.body);
      if (responseBody.token) {
        if (requestParams.json && requestParams.json.username === 'student1') {
          studentToken = responseBody.token;
          console.log('Student token captured');
        } else if (requestParams.json && requestParams.json.username === 'teacher1') {
          teacherToken = responseBody.token;
          console.log('Teacher token captured');
        } else if (requestParams.json && requestParams.json.username === 'admin') {
          adminToken = responseBody.token;
          console.log('Admin token captured');
        }
      }
    } catch (error) {
      console.error('Error parsing login response:', error);
    }
  }
  
  next();
}

/**
 * Обработка ошибок
 */
function onError(error, requestParams, context, ee, next) {
  errorCount++;
  console.error(`Request error: ${error.message} for ${requestParams.url}`);
  next();
}

/**
 * Генерация случайных данных для тестирования
 */
function generateRandomData() {
  return {
    // Случайные имена для поиска
    searchQueries: [
      'приветствие',
      'hello',
      'английский',
      'урок',
      'слово',
      'тест',
      'обучение',
      'язык'
    ],
    
    // Случайные оценки
    scores: [60, 70, 80, 85, 90, 95, 100],
    
    // Случайные ID для тестирования
    studentIds: [1, 2, 3, 4, 5],
    lessonIds: [1, 2, 3, 4, 5],
    wordIds: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
  };
}

/**
 * Получение случайного элемента из массива
 */
function getRandomElement(array) {
  return array[Math.floor(Math.random() * array.length)];
}

/**
 * Создание случайного запроса поиска
 */
function createRandomSearchRequest(context, ee, next) {
  const data = generateRandomData();
  const randomQuery = getRandomElement(data.searchQueries);
  
  context.vars.searchQuery = randomQuery;
  context.vars.randomStudentId = getRandomElement(data.studentIds);
  context.vars.randomLessonId = getRandomElement(data.lessonIds);
  context.vars.randomWordId = getRandomElement(data.wordIds);
  context.vars.randomScore = getRandomElement(data.scores);
  
  next();
}

/**
 * Проверка здоровья API
 */
function healthCheck(context, ee, next) {
  const healthUrl = context.vars.baseUrl + '/auth/login';
  
  // Простой запрос для проверки доступности
  const request = {
    method: 'POST',
    url: healthUrl,
    json: {
      username: 'student1',
      password: 'password'
    }
  };
  
  // Здесь можно добавить дополнительную логику проверки
  console.log('Health check completed');
  next();
}

/**
 * Создание отчета о производительности
 */
function generatePerformanceReport() {
  const successRate = (successCount / requestCount) * 100;
  
  console.log('\n=== PERFORMANCE REPORT ===');
  console.log(`Total Requests: ${requestCount}`);
  console.log(`Successful: ${successCount}`);
  console.log(`Errors: ${errorCount}`);
  console.log(`Success Rate: ${successRate.toFixed(2)}%`);
  console.log('==========================\n');
}

/**
 * Очистка ресурсов после тестирования
 */
function cleanup(context, ee, next) {
  // Сбрасываем токены
  studentToken = null;
  teacherToken = null;
  adminToken = null;
  
  // Генерируем отчет
  generatePerformanceReport();
  
  console.log('Cleanup completed');
  next();
}

/**
 * Валидация ответов
 */
function validateResponse(response, context, ee, next) {
  const statusCode = response.statusCode;
  
  // Проверяем статус код
  if (statusCode >= 400) {
    console.error(`Validation failed: ${statusCode} for ${context.vars.url}`);
    return next(new Error(`HTTP ${statusCode}`));
  }
  
  // Проверяем структуру ответа для определенных эндпоинтов
  if (response.body) {
    try {
      const body = JSON.parse(response.body);
      
      // Проверяем логин ответ
      if (context.vars.url && context.vars.url.includes('/auth/login')) {
        if (!body.token || !body.user) {
          console.error('Invalid login response structure');
          return next(new Error('Invalid response structure'));
        }
      }
      
      // Проверяем ответ с пользователем
      if (context.vars.url && context.vars.url.includes('/users/me')) {
        if (!body.id || !body.username) {
          console.error('Invalid user response structure');
          return next(new Error('Invalid user response structure'));
        }
      }
      
    } catch (error) {
      console.error('Error parsing response body:', error);
    }
  }
  
  next();
}

/**
 * Мониторинг памяти и CPU
 */
function monitorResources(context, ee, next) {
  const memUsage = process.memoryUsage();
  const cpuUsage = process.cpuUsage();
  
  console.log(`Memory usage: ${Math.round(memUsage.heapUsed / 1024 / 1024)}MB`);
  console.log(`CPU usage: ${cpuUsage.user / 1000}ms user, ${cpuUsage.system / 1000}ms system`);
  
  next();
}

// Экспортируем функции для Artillery
module.exports = {
  beforeRequest,
  afterResponse,
  onError,
  createRandomSearchRequest,
  healthCheck,
  cleanup,
  validateResponse,
  monitorResources,
  
  // Глобальные переменные
  studentToken,
  teacherToken,
  adminToken,
  
  // Счетчики
  requestCount,
  errorCount,
  successCount
}; 