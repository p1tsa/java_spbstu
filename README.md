Репозиторий для курсовой работы по дисциплине «Приёмы программирования на языке Java» студента Цвиркун Никиты группы 5130203/20102
---
## Overview
This project is a Spring Boot–based task manager application that evolved through several development stages. It started with a basic REST API using in-memory storage, including controllers for tasks, users, and notifications, each supporting essential CRUD operations. Later, H2 was added as an in-memory database, followed by a migration to PostgreSQL with Flyway for versioned schema updates. The application was containerized using Docker and integrated with Redis for caching and RabbitMQ for message-based communication. In the final stages, scheduled jobs were added to detect overdue tasks, and asynchronous processing was implemented to handle notifications efficiently in the background.

--- 

### How to run

1. Install **Docker** and **Docker Compose**
2. Run the services:
   ```bash
   docker-compose up --build
   ```

---

### API Endpoints

TaskController (/api/tasks)

    POST /api/tasks — создать новую задачу.

    GET /api/tasks/user/{userId} — получить все задачи пользователя.

    GET /api/tasks/user/{userId}/pending — получить только незавершённые и не удалённые задачи.

    DELETE /api/tasks/{taskId} — "удалить" задачу (логически, пометить как удалённую).

UserController (/api/users)

    POST /api/users/register — регистрация нового пользователя.

    GET /api/users/login?username={username} — вход (эмуляция, просто возвращает пользователя по username).

NotificationController (/api/notifications)

    GET /api/notifications/user/{userId} — получить все уведомления пользователя.

    GET /api/notifications/user/{userId}/unread — получить только непрочитанные уведомления.
