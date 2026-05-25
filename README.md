Spring Boot приложение с авторизацией, админ-панелью для управления пользователями и выгрузкой Excel-отчета по франшизам Anilibria.

Что умеет проект
Авторизация через Spring Security.
Роли ADMIN и USER.
Админ-панель для просмотра, создания, редактирования и удаления пользователей.
Страница пользователя с информацией о текущем аккаунте.
Получение данных о франшизах из Anilibria API.
Генерация .xlsx Excel-файла с франшизами за выбранный год.
Локальный запуск приложения и MySQL через Docker Compose.

Технологии
Java 11.
Spring Boot 2.6.2.
Spring Web.
Spring Security.
Spring Data JPA / Hibernate.
Thymeleaf.
MySQL 8.0.
Apache POI для генерации Excel.
Bootstrap 4.3.1, jQuery, Popper.js через WebJars.
Maven Wrapper.
Docker и Docker Compose.


Требования
Docker Desktop.
JDK 11 или новее.
PowerShell.
В проекте есть Maven Wrapper, поэтому отдельно устанавливать Maven не обязательно.

Запуск через Docker Compose
Сначала нужно собрать jar-файл приложения:

cd C:\Users\Admin\Desktop\проект\demo>
.\mvnw.cmd -DskipTests package
Если локальный Maven cache дает ошибки доступа, можно собрать с cache внутри проекта:

.\mvnw.cmd "-Dmaven.repo.local=.maven-repo" -DskipTests package
После этого запустить контейнеры из корня проекта:

cd C:\Users\Admin\Desktop\проект\demo>
docker compose up -d --build
Приложение будет доступно по адресу:
http://localhost:8080

Данные для входа администратора по умолчанию:
login: admin
password: admin

Перезапуск после изменений в коде
Так как Dockerfile копирует уже собранный jar из target, после изменений в Java, HTML, CSS или JS нужно заново собрать jar:

cd C:\Users\Admin\Desktop\проект\demo>
.\mvnw.cmd "-Dmaven.repo.local=.maven-repo" -DskipTests package
Потом пересобрать и перезапустить контейнер приложения:

cd C:\Users\Admin\Desktop\проект\demo>
docker compose build app
docker compose up -d --no-deps app

Полезные команды
Посмотреть запущенные контейнеры:
docker compose ps

Посмотреть логи приложения:
docker compose logs -f app

Посмотреть логи MySQL:
docker compose logs -f mysql

Остановить контейнеры без удаления базы:
docker compose down

Остановить контейнеры и удалить данные MySQL:
docker compose down -v

Настройки окружения
Основные переменные задаются в docker-compose.yml:
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
APP_INIT_ADMIN_USERNAME
APP_INIT_ADMIN_PASSWORD
По умолчанию приложение подключается к базе anilibria_adventures, пользователь MySQL root, пароль root.
