# University SMS REST API

Фінальний проєкт: RESTful API системи управління університетом на Spring Boot 3, Spring Data JPA та PostgreSQL.

## Стек

- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- PostgreSQL
- DTO + validation
- Swagger / Springdoc OpenAPI
- JUnit 5 / Mockito / Spring Test

## Як запустити

### 1. Запустити PostgreSQL

```bash
docker compose up -d
```

### 2. Запустити застосунок

Якщо в тебе встановлений Gradle:

```bash
gradle bootRun
```

Якщо хочеш додати Gradle Wrapper у репозиторій:

```bash
gradle wrapper
./gradlew bootRun
```

На Windows після створення wrapper:

```bash
gradlew.bat bootRun
```

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

## Основні endpoint-и

### Students

- `POST /api/students` — створити студента
- `GET /api/students` — список студентів + фільтри `status`, `year`, `search`
- `GET /api/students/{id}` — отримати студента
- `PUT /api/students/{id}` — оновити студента
- `DELETE /api/students/{id}` — видалити студента
- `GET /api/students/unpaid` — студенти з неоплаченими курсами
- `GET /api/students/top?limit=5` — топ студентів за GPA
- `GET /api/students/{id}/transcript` — транскрипт студента з GPA

### Teachers

- `POST /api/teachers`
- `GET /api/teachers`
- `GET /api/teachers/{id}`
- `PUT /api/teachers/{id}`
- `DELETE /api/teachers/{id}`

### Courses

- `POST /api/courses`
- `GET /api/courses?teacherId=1&credits=5`
- `GET /api/courses/{id}`
- `PUT /api/courses/{id}`
- `DELETE /api/courses/{id}`

### Enrollments

- `POST /api/enrollments` — створити зарахування
- `GET /api/enrollments`
- `GET /api/enrollments/{id}`
- `DELETE /api/enrollments/{id}`
- `PUT /api/enrollments/{id}/grade` — поставити оцінку
- `PUT /api/enrollments/{id}/paid` — позначити оплату
- `GET /api/enrollments/gpa?courseId=1&semester=Spring` — середній GPA по курсу або семестру

## Приклади JSON

### Створити студента

```json
{
  "firstName": "Ivan",
  "lastName": "Petrenko",
  "email": "ivan.petrenko@example.com",
  "enrollmentYear": 2026,
  "status": "ACTIVE"
}
```

### Створити викладача

```json
{
  "firstName": "Olena",
  "lastName": "Shevchenko",
  "email": "olena.shevchenko@example.com",
  "dateOfBirth": "1985-04-15",
  "position": "LECTURER"
}
```

### Створити курс

```json
{
  "name": "Java Spring Boot",
  "credits": 5,
  "description": "REST API with Spring Boot and JPA",
  "teacherId": 1
}
```

### Створити зарахування

```json
{
  "studentId": 1,
  "courseId": 1,
  "semester": "Spring",
  "year": 2026
}
```

### Поставити оцінку

```json
{
  "grade": "A"
}
```

## Запуск тестів

```bash
gradle test
```

Або після створення wrapper:

```bash
./gradlew test
```

У проєкті є unit-тести сервісного шару та інтеграційні тести контролера.

## Структура

```text
src/main/java/ua/university/sms/
 ├── controller/
 ├── service/
 ├── repository/
 ├── model/
 │    ├── dto/
 │    ├── entity/
 │    └── interfaces/
 └── exception/
```
