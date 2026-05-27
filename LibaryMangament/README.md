# Library Management (Spring Boot)

REST API backend for a library management system with books, members, and borrow records.

## Run

```bash
mvn spring-boot:run
```

The application loads `.env` values automatically and initializes sample data from `src/main/resources/schema.sql` and `src/main/resources/data.sql`.

## Useful URLs

- `http://localhost:8080/books`
- `http://localhost:8080/members`
- `http://localhost:8080/borrow/overdue`
- `http://localhost:8080/h2-console`

## API Endpoints

- `GET /books`
- `GET /books/{id}`
- `POST /books`
- `PUT /books/{id}`
- `DELETE /books/{id}`
- `GET /members`
- `GET /members/{id}`
- `POST /members`
- `PUT /members/{id}`
- `DELETE /members/{id}`
- `GET /members/{id}/history`
- `POST /borrow`
- `POST /return/{borrowId}`
- `GET /borrow/overdue`
