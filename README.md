# Comic reading website

> **Mô tả ngắn gọn:** Đây là dự án quản lý và cung cấp truyện, với dữ liệu được crawl từ otruyen.cc, xử lý, lưu trữ các thông tin và url truyện, chapter và các thông tin liên quan và cập nhật liên tục, hỗ trợ đăng nhập bằng google, facebook. Dự án hỗ trợ tương tác người dùng qua frontend React và backend Spring Boot, với dữ liệu lưu trữ trên MySQL, MongoDB và Redis. Trong tương lai, dự án sẽ tích hợp AI để gợi ý truyện hoặc trả lời thông tin liên quan đến truyện.

---

## Mục lục

1. [Công nghệ sử dụng](#công-nghệ-sử-dụng)  
2. [Cấu trúc dự án](#cấu-trúc-dự-án)  
3. [Hướng dẫn cài đặt](#hướng-dẫn-cài-đặt)  
4. [Sử dụng API](#sử-dụng-api)  
5. [Tính năng](#tính-năng)  
6. [Hướng phát triển](#hướng-phát-triển)  
7. [Công cụ hỗ trợ](#công-cụ-hỗ-trợ)  
8. [Liên hệ](#liên-hệ)

---

## Công nghệ sử dụng

- **Frontend:**  
  - React, HTML, CSS, JavaScript  
- **Backend:**  
  - Spring Boot (Java)  
- **Database:**  
  - MySQL, MongoDB, Redis  
  - Cloudinary (lưu trữ ảnh)  
- **Tools:**  
  - Postman, Docker, IntelliJ IDEA, VS Code, PyCharm, Hoppscotch, WebStorm, MongoDB Compass

---

## Cấu trúc dự án

### Frontend (React)
```
my-app/
├── public/
├── src/
│   ├── api/           # API calls
│   ├── assets/        # Icons, images
│   ├── components/    # Reusable components (Button, Navbar, Footer...)
│   ├── context/       # React contexts (AppContext, AuthContext)
│   ├── hooks/         # Custom hooks
│   ├── layouts/       # Layout components
│   ├── pages/         # Pages (Auth, Home, Profile, ComicDetail...)
│   ├── routes/        # App routing
│   ├── utils/         # Utility functions
│   ├── App.jsx        # Root component
│   └── main.jsx       # Entry point
├── package.json
├── vite.config.js
└── tailwind.config.js
```

### Backend (Spring Boot)
```
src/main/java/com/example/projectrestfulapi/
├── config/            # Cors, JWT, Security configs
├── controller/        # REST controllers
├── domain/            # Entity classes (SQL & NoSQL)
├── dto/               # Request & Response DTOs
├── exception/         # Exception handling
├── mapper/            # Mapping entities to DTOs
├── repository/        # Repositories (SQL & NoSQL)
├── security/          # Security config, filters, handlers
├── service/           # Business logic services
├── util/              # Utilities (JWT, OTP, Response)
└── ProjectRestFulApiApplication.java  # Main application
```

---

## Hướng dẫn cài đặt

### Frontend
```bash
cd my-app
npm install
npm start
```
- Chạy mặc định tại `http://localhost:517x`

### Docker
```bash
docker-compose up -d
```
- Vào nơi chứa folder chứa file `docker-compose.yml` trong backend để tạo và chạy redis:

### Backend
```bash
./mvnw spring-boot:run
```
- Chạy mặc định tại `http://localhost:8080`

### Database
- Sử dụng file `db-comic-web.sql` để tạo cơ sở dữ liệu và các bảng trong MySQL.
- MySQL chạy mặc định trên **port 3306**.
- Ví dụ import các bảng:
```bash
mysql -u root -p comic_db < db-comic-web.sql
```
---

## Sử dụng API
- API RESTful được viết bằng Spring Boot. Dùng Postman, Hoppscotch để kiểm thử.
- Endpoint chính:
  - Đăng nhập: `http://localhost:8080/api/v1/auth/login`
  - Đăng ký: `http://localhost:8080/api/v1/auth/register`
  - Truyện mới: `http://localhost:8080/api/v1/comics/new?page=1`
  - Truyện mới cập nhật: `http://localhost:8080/api/v1/comics/new-update?page=1`
  - Truyện đã hoàn thành: `http://localhost:8080/api/v1/comics/completed?page=1`
  - Thể loại: `http://localhost:8080/api/v1/categories`
  - Tìm truyện theo tên: `http://localhost:8080/api/v1/comics/search?keyword=tên truyện&page=1`
  - Tìm truyện theo thể loại: `http://localhost:8080/api/v1/comics/thể loại?page=1`

---

## Tính năng
- Xử lý và lưu trữ dữ liệu trên MySQL
- Lưu trữ tin nhắn và bình luận trên MongoDB
- Lưu ảnh đại diện tài khoản trên Cloudinary
- Cache dữ liệu bằng Redis
- Giao diện React responsive, cập nhật dữ liệu thời gian thực
- Hỗ trợ cả đăng nhập bằng google và facebook

---

## Hướng phát triển
- Tích hợp AI (gợi ý truyện, chatbot)
- Hệ thống phân quyền người dùng & admin
- Triển khai mobile app hoặc PWA
- Tự động crawl theo thời gian nhất định

---

## Công cụ hỗ trợ
- Postman, Hoppscotch: kiểm thử API
- Docker: tạo và sử dụng redis (container hóa ứng dụng trong tương lai)
- IntelliJ IDEA, VS Code, PyCharm, WebStorm: phát triển frontend & backend và tool crawl comic
- Cloudinary: lưu trữ ảnh

---

## Liên hệ
- Telegram: `https://t.me/hcongdev`