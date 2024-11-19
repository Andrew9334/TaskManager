# Проект Task Manager

Этот проект представляет собой систему управления задачами с возможностью аутентификации пользователей, управления ролями и выполнения CRUD-операций над задачами.

## Требования

Для локального запуска приложения вам потребуется:

- Docker
- Docker Compose

## Структура проекта

- **Backend**: Сервис на Spring Boot, реализующий логику управления задачами и аутентификацию пользователей.
- **Database**: PostgreSQL база данных, используемая для хранения информации о пользователях, задачах и комментариях.

## Установка и запуск проекта

### 1. Клонируйте репозиторий

Сначала клонируйте репозиторий на вашу локальную машину:

```bash
git clone https://github.com/ваш-репозиторий.git
cd ваш-репозиторий

```
### 2. Настройка Docker Compose
Проект использует Docker Compose для настройки и запуска базы данных (PostgreSQL) и приложения на Spring Boot. Все необходимые конфигурации уже включены в файл docker-compose.yml.

Структура файла docker-compose.yml:
```bash
version: '3.8'

services:
  db:
    image: postgres:13
    container_name: task_manager_db
    environment:
      - POSTGRES_USER=your_db_user
      - POSTGRES_PASSWORD=your_db_password
      - POSTGRES_DB=your_db_name
    ports:
      - "5432:5432"
    volumes:
      - taskmanager-db-data:/var/lib/postgresql/data
    networks:
      - taskmanager-network

  backend:
    image: openjdk:17-jdk
    container_name: task_manager_backend
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/your_db_name
      - SPRING_DATASOURCE_USERNAME=your_db_user
      - SPRING_DATASOURCE_PASSWORD=your_db_password
      - SPRING_JPA_HIBERNATE_DDL_AUTO=update
      - SPRING_JPA_SHOW_SQL=true
    depends_on:
      - db
    networks:
      - taskmanager-network

networks:
  taskmanager-network:
    driver: bridge

volumes:
  taskmanager-db-data:
```

### 3. Настройка переменных окружения
Перед запуском контейнеров убедитесь, что вы настроили правильные значения для переменных:

POSTGRES_USER: имя пользователя базы данных
POSTGRES_PASSWORD: пароль пользователя базы данных
POSTGRES_DB: имя базы данных
Для этого откройте файл docker-compose.yml и замените your_db_user, your_db_password и your_db_name на значения, которые вы хотите использовать.

Также, в файле настроек Spring Boot (application.properties или application.yml) проверьте, что параметры подключения к базе данных используют те же значения.

### 4. Запуск приложения
   Перейдите в корневую директорию проекта (где находится файл docker-compose.yml) и выполните команду:
```bash
docker-compose up --build
````

Это создаст и запустит два контейнера:

* db — контейнер с PostgreSQL базой данных.
* backend — контейнер с приложением Spring Boot.
Важное замечание:
* Если вы впервые запускаете проект, то Docker Compose создаст контейнеры и сети, а также выполнит сборку проекта, что может занять некоторое время.
* Если вам нужно перезапустить контейнеры, используйте команду:
````bash
docker-compose restart
````
### 5. Доступ к приложению
   После успешного запуска приложения, вы сможете получить доступ к API по следующему адресу:
````bash
http://localhost:8080
````
### 6. Работа с базой данных
Приложение использует PostgreSQL для хранения данных. Если вы хотите получить доступ к базе данных, используйте любой клиент для PostgreSQL (например, pgAdmin или командную строку).

Параметры подключения:

* Host: localhost
* Port: 5432
* Username: user
* Password: password
* Database: task_manager

### 7. Остановка и удаление контейнеров
   Для остановки контейнеров выполните команду:
````bash
docker-compose down
````
Если вы хотите также удалить созданные контейнеры и данные базы данных, используйте:
````bash
docker-compose down -v
````
### 8. Тестирование
   В проекте настроены юнит-тесты для проверки работы основных компонентов. Чтобы выполнить тесты, используйте Maven или Gradle, в зависимости от вашего инструмента сборки:

Если вы используете Maven:
````bash
mvn test
````
### 9. Логи
   Для просмотра логов работы контейнеров используйте команду:
````bash
docker-compose logs -f
````
Это позволит вам наблюдать за логами контейнеров в реальном времени.

Полезные команды
* Собрать и запустить проект:

```bash
docker-compose up --build
```
* Остановить проект:

```bash
docker-compose down
```

* Остановить проект с удалением данных:

```bash
docker-compose down -v
```

* Просмотр логов:

```bash
docker-compose logs -f
```

----------------------------------------------

## Разработка
Если вы хотите вносить изменения в проект, просто измените код в директории backend, и контейнер с приложением автоматически поднимется с новыми изменениями.
