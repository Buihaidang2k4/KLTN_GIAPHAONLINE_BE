# KLTN_GIAPHAONLINE Backend

Backend Spring Boot cho ứng dụng Gia Phả Online, xây dựng bằng Java 21 và Spring Boot 3.5.9.

## Mô tả

Ứng dụng cung cấp API backend cho hệ thống gia phả, quản lý tài khoản, thành viên gia đình, bài viết, sự kiện, album, subscription, thanh toán và thông báo.

## Công nghệ chính

- Java 21
- Spring Boot 3.5.9
- Spring Web
- Spring Data JPA
- Spring Security
- Spring OAuth2 Client / Resource Server
- Spring AMQP (RabbitMQ)
- Spring Data Redis
- Spring WebSocket / STOMP
- Spring Mail
- Thymeleaf
- MapStruct
- Lombok
- PostgreSQL
- MinIO
- Swagger / OpenAPI
- Bucket4j rate limiting
- Apache POI
- Jsoup
- JavaCV
- okhttp3

## Tính năng chính

- Quản lý tài khoản, xác thực JWT, refresh token
- Xác thực OAuth2 và resource server
- Email thông báo, xác thực tài khoản, quên mật khẩu
- Upload lưu trữ media với MinIO
- Bộ nhớ cache / session với Redis
- Messaging / event với RabbitMQ
- WebSocket realtime
- API REST đầy đủ với Swagger UI
- Hỗ trợ thanh toán VNPAY
- Tính năng gia đình: thành viên, mời gia nhập, sự kiện, bài viết, album, achievements
- Hỗ trợ import/export file và xử lý nội dung sạch với Jsoup

## Cấu trúc thư mục chính

- `src/main/java/com/codewithdang/kltn_giaphaonline` - mã nguồn chính
  - `config` - cấu hình ứng dụng
  - `controller` - REST controller
  - `dto` - DTO request/response
  - `entity` - thực thể JPA
  - `enums` - enum dùng chung
  - `events` - event, producer, listener
  - `exception` - xử lý lỗi
  - `mapper` - MapStruct mapper
  - `repo` - repository JPA
  - `service` - business logic
  - `utils` - tiện ích
- `src/main/resources` - tài nguyên
  - `application.properties` - cấu hình chung
  - `application-dev.properties` - cấu hình development
  - `application-prod.properties` - cấu hình production
  - `db/migration` - script migration
  - `templates/email` - mẫu email Thymeleaf
- `src/Dockerfile` - Docker build backend
- `compose.yml` - Docker Compose cho PostgreSQL, Redis, RabbitMQ, MinIO, backend

## Yêu cầu môi trường

- Java 21
- Maven
- Docker & Docker Compose (nếu dùng container)
- PostgreSQL
- Redis
- RabbitMQ
- MinIO

## Chạy local với Maven

### 1. Cài đặt

Windows:
```powershell
cd d:\Code\PROJECT\KLTN\KLTN_GIAPHAONLINE_BE
./mvnw.cmd clean package -DskipTests
```

Linux / macOS:
```bash
./mvnw clean package -DskipTests
```

### 2. Chạy ứng dụng

Sử dụng profile `dev`:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Hoặc chạy file jar:

```bash
java -jar target/KLTN_GIAPHAONLINE-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

## Chạy bằng Docker Compose

### 1. Tạo file `.env`

Tại thư mục gốc, tạo file `.env` với thông tin cần thiết như sau:

```env
DB_USERNAME=root
DB_PASSWORD=root
REDIS_PASSWORD=root
RABBITMQ_USERNAME=guest
RABBITMQ_PASSWORD=guest
MINIO_ACCESS_KEY=minioadmin
MINIO_SECRET_KEY=minioadmin
FRONTEND_URL=http://localhost:5173
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-email-password
JWT_SECRET=your-jwt-secret
JWT_EXPIRATION=86400000
JWT_REFRESHABLE_DURATION=2592000000
PAY_URL=https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
TMN_CODE=YOUR_TMN_CODE
SECRET_KEY=YOUR_SECRET_KEY
RETURN_URL=http://localhost:5173/payment-return
VERSION=2.1.0
```

### 2. Chạy Docker Compose

```bash
docker compose -f compose.yml up --build
```

### 3. Dừng dịch vụ

```bash
docker compose -f compose.yml down
```

## Cấu hình môi trường quan trọng

### PostgreSQL

- `DB_URL` - URL kết nối JDBC
- `DB_USERNAME` - tên tài khoản DB
- `DB_PASSWORD` - mật khẩu DB

### Redis

- `REDIS_HOST`
- `REDIS_PORT`
- `REDIS_PASSWORD`

### RabbitMQ

- `RABBITMQ_HOST`
- `RABBITMQ_PORT`
- `RABBITMQ_USERNAME`
- `RABBITMQ_PASSWORD`

### MinIO

- `MINIO_URL`
- `MINIO_PUBLIC_URL`
- `MINIO_ACCESS_KEY`
- `MINIO_SECRET_KEY`

### Mail

- `MAIL_USERNAME`
- `MAIL_PASSWORD`

### JWT

- `JWT_SECRET`
- `JWT_EXPIRATION`
- `JWT_REFRESHABLE_DURATION`

### VNPAY

- `PAY_URL`
- `TMN_CODE`
- `SECRET_KEY`
- `RETURN_URL`
- `VERSION`

## Cấu hình Spring Boot mặc định

- `server.port=8080`
- `spring.jpa.hibernate.ddl-auto=update`
- `spring.jpa.show-sql=true` (dev)
- `springdoc.swagger-ui.path=/swagger-ui.html`
- `api.prefix=api/v1`
- `minio.bucket=family`
- `app.frontend.base-url=http://localhost:5173`

## API và Swagger

Sau khi chạy backend, truy cập:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Tài nguyên hữu ích

- `compose.yml` - khởi động PostgreSQL, Redis, RabbitMQ, MinIO và backend
- `src/Dockerfile` - tạo Docker image backend
- `src/main/resources/templates/email` - mẫu email gửi xác thực, reset password, thông báo
- `src/main/resources/db/migration` - script migration cơ bản

## Ghi chú

- Profile production dùng `application-prod.properties`, load biến môi trường từ `.env` và `compose.yml`.
- Profile development dùng `application-dev.properties` và các service chạy ở `localhost`.
- Nếu cần cấu hình thêm, chỉnh file `application.properties`, `application-dev.properties`, `application-prod.properties`.
- `springflyway` hiện không bật trong cấu hình mặc định.

## Lệnh hữu ích

- Build: `./mvnw clean package -DskipTests`
- Chạy dev: `./mvnw spring-boot:run -Dspring-boot.run.profiles=dev`
- Chạy Docker: `docker compose -f compose.yml up --build`
- Dừng Docker: `docker compose -f compose.yml down`

---

