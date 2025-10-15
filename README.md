- Dự án sử dụng các công nghệ sau đây:
   + frontend: HTML, css, js, react
   + backend: spring boot
   + database: mySQL, mongoDB, Redis, Firebase Storage
   + tool: postman, docker, intellji, visual studio code, pycharm
- Data được crawl về và tự xử lí, viết api, data được cập nhật mới liên tục. Dự án có tích hợp AI để gợi ý truyện hoặc để người dùng hỏi thông tin cần thiết về truyện.
- AI là tự lấy data ra để cho AI sử dụng, tích hợp tự động học hỏi qua mỗi câu hỏi hoặc data mới mỗi ngày.

* frontend
├── 📁 node_modules/ 🚫 (auto-hidden)
├── 📁 public/
├── 📁 src/
│   ├── 📁 api/
│   │   ├── 📄 Account.jsx
│   │   ├── 📄 Client.jsx
│   │   ├── 📄 Comic.jsx
│   │   └── 📄 axiosClient.jsx
│   ├── 📁 assets/
│   │   ├── 📁 icons/
│   │   │   └── 🖼️ logo.png
│   │   └── 📁 images/
│   ├── 📁 components/
│   │   ├── 📁 Button/
│   │   │   ├── 🎨 Button.css
│   │   │   └── 📄 Button.jsx
│   │   ├── 📁 Navbar/
│   │   │   ├── 📄 Menu.jsx
│   │   │   ├── 🎨 Navbar.css
│   │   │   └── 📄 Navbar.jsx
│   │   └── 📁 Pagination/
│   │       └── 📄 Pagination.jsx
│   ├── 📁 context/
│   ├── 📁 hooks/
│   ├── 📁 layouts/
│   │   └── 📄 MainLayout.jsx
│   ├── 📁 pages/
│   │   ├── 📄 404.jsx
│   │   ├── 📄 Category.jsx
│   │   ├── 📄 Follow.jsx
│   │   ├── 📄 Help.jsx
│   │   ├── 📄 History.jsx
│   │   ├── 🎨 Home.css
│   │   ├── 📄 Home.jsx
│   │   ├── 📄 Login.jsx
│   │   ├── 📄 Profile.jsx
│   │   └── 📄 Register.jsx
│   ├── 📁 routes/
│   │   └── 📄 AppRoutes.jsx
│   ├── 📁 utils/
│   ├── 📄 App.jsx
│   ├── 🎨 index.css
│   └── 📄 main.jsx
├── 🚫 .gitignore
├── 📖 README.md
├── 📄 eslint.config.js
├── 🌐 index.html
├── 📄 package-lock.json
├── 📄 package.json
├── 📄 postcss.config.js
├── 📄 tailwind.config.js
└── 📄 vite.config.js

* backend
├── 📁 .idea/ 🚫 (auto-hidden)
├── 📁 .mvn/
│   └── 📁 wrapper/
│       └── 📄 maven-wrapper.properties
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/
│   │   │   └── 📁 com/
│   │   │       └── 📁 example/
│   │   │           └── 📁 projectrestfulapi/
│   │   │               ├── 📁 config/
│   │   │               │   ├── ☕ CorsConfig.java
│   │   │               │   ├── ☕ JwtConfig.java
│   │   │               │   └── ☕ SecurityConfig.java
│   │   │               ├── 📁 controller/
│   │   │               │   ├── ☕ AccountController.java
│   │   │               │   ├── ☕ AccountFollowComicController.java
│   │   │               │   ├── ☕ AuthController.java
│   │   │               │   ├── ☕ CategoryController.java
│   │   │               │   ├── ☕ ComicController.java
│   │   │               │   ├── ☕ GroupController.java
│   │   │               │   ├── ☕ ImageChapterController.java
│   │   │               │   ├── ☕ MessageController.java
│   │   │               │   └── ☕ UserController.java
│   │   │               ├── 📁 domain/
│   │   │               │   ├── 📁 NOSQL/
│   │   │               │   │   ├── ☕ Group.java
│   │   │               │   │   └── ☕ Message.java
│   │   │               │   └── 📁 SQL/
│   │   │               │       ├── ☕ Account.java
│   │   │               │       ├── ☕ AccountFollowComic.java
│   │   │               │       ├── ☕ Category.java
│   │   │               │       ├── ☕ Chapter.java
│   │   │               │       ├── ☕ Comic.java
│   │   │               │       ├── ☕ ComicCategory.java
│   │   │               │       ├── ☕ ImageChapter.java
│   │   │               │       ├── ☕ Role.java
│   │   │               │       ├── ☕ Status.java
│   │   │               │       └── ☕ User.java
│   │   │               ├── 📁 dto/
│   │   │               │   ├── 📁 request/
│   │   │               │   │   ├── 📁 User/
│   │   │               │   │   │   └── ☕ UserUpdateRequestDTO.java
│   │   │               │   │   ├── 📁 account/
│   │   │               │   │   │   ├── ☕ LoginAccountDTO.java
│   │   │               │   │   │   ├── ☕ RegisterAccountDTO.java
│   │   │               │   │   │   └── ☕ VerificationEmailRequestDTO.java
│   │   │               │   │   └── 📁 message/
│   │   │               │   │       └── ☕ GroupRequestDTO.java
│   │   │               │   └── 📁 response/
│   │   │               │       ├── 📁 comic/
│   │   │               │       │   ├── ☕ CategoryResponseDTO.java
│   │   │               │       │   ├── ☕ ChapterResponseDTO.java
│   │   │               │       │   └── ☕ ComicResponseDTO.java
│   │   │               │       ├── 📁 message/
│   │   │               │       │   └── ☕ GroupResponseDTO.java
│   │   │               │       ├── 📁 token/
│   │   │               │       │   └── ☕ RefreshToken.java
│   │   │               │       ├── 📁 user/
│   │   │               │       │   ├── ☕ UserLoginResponseDTO.java
│   │   │               │       │   └── ☕ UserRegisterResponseDTO.java
│   │   │               │       └── ☕ FormatResponseDTO.java
│   │   │               ├── 📁 exception/
│   │   │               │   ├── ☕ GlobalExceptionHandler.java
│   │   │               │   ├── ☕ InvalidException.java
│   │   │               │   └── ☕ NumberError.java
│   │   │               ├── 📁 mapper/
│   │   │               │   ├── ☕ CategoryMapper.java
│   │   │               │   ├── ☕ ChapterMapper.java
│   │   │               │   ├── ☕ ComicMapper.java
│   │   │               │   ├── ☕ GroupMapper.java
│   │   │               │   ├── ☕ RefreshTokenMapper.java
│   │   │               │   └── ☕ UserMapper.java
│   │   │               ├── 📁 repository/
│   │   │               │   ├── 📁 NOSQL/
│   │   │               │   │   ├── ☕ GroupRepository.java
│   │   │               │   │   └── ☕ MessageRepository.java
│   │   │               │   └── 📁 SQL/
│   │   │               │       ├── ☕ AccountFollowComicRepository.java
│   │   │               │       ├── ☕ AccountRepository.java
│   │   │               │       ├── ☕ CategoryRepository.java
│   │   │               │       ├── ☕ ChapterRepository.java
│   │   │               │       ├── ☕ ComicCategoryRepository.java
│   │   │               │       ├── ☕ ComicRepository.java
│   │   │               │       ├── ☕ ImageChapterRepository.java
│   │   │               │       ├── ☕ RoleRepository.java
│   │   │               │       ├── ☕ StatusRepository.java
│   │   │               │       └── ☕ UserRepository.java
│   │   │               ├── 📁 security/
│   │   │               │   ├── 📁 detail/
│   │   │               │   │   └── ☕ AccountDetailService.java
│   │   │               │   ├── 📁 filter/
│   │   │               │   │   └── ☕ JwtAuthenticationFilter.java
│   │   │               │   └── 📁 handler/
│   │   │               │       └── ☕ CustomAuthenticationEntryPoint.java
│   │   │               ├── 📁 service/
│   │   │               │   ├── ☕ AccountFollowComicService.java
│   │   │               │   ├── ☕ AccountService.java
│   │   │               │   ├── ☕ AuthService.java
│   │   │               │   ├── ☕ CategoryService.java
│   │   │               │   ├── ☕ ChapterService.java
│   │   │               │   ├── ☕ ComicService.java
│   │   │               │   ├── ☕ EmailVerificationService.java
│   │   │               │   ├── ☕ GroupService.java
│   │   │               │   ├── ☕ ImageChapterService.java
│   │   │               │   ├── ☕ MessageService.java
│   │   │               │   └── ☕ UserService.java
│   │   │               ├── 📁 util/
│   │   │               │   ├── 📁 Captcha/
│   │   │               │   ├── 📁 OTPMail/
│   │   │               │   │   └── ☕ OtpUtil.java
│   │   │               │   ├── 📁 ResponseUtil/
│   │   │               │   │   └── ☕ ResponseUtil.java
│   │   │               │   └── 📁 Security/
│   │   │               │       └── ☕ JwtUtil.java
│   │   │               └── ☕ ProjectRestFulApiApplication.java
│   │   └── 📁 resources/
│   │       ├── 📁 static/
│   │       ├── 📁 templates/
│   │       └── 📄 application.properties
│   └── 📁 test/
│       └── 📁 java/
│           └── 📁 com/
│               └── 📁 example/
│                   └── 📁 projectrestfulapi/
│                       └── ☕ ProjectRestFulApiApplicationTests.java
├── 📁 target/ 🚫 (auto-hidden)
├── 🔒 .env 🚫 (auto-hidden)
├── 📄 .gitattributes
├── 🚫 .gitignore
├── 🐳 Dockerfile
├── 📝 HELP.md 🚫 (auto-hidden)
├── 📄 ProjectRestFulAPI.iml 🚫 (auto-hidden)
├── ⚙️ docker-compose.yml
├── 📄 mvnw
├── 🐚 mvnw.cmd
└── 📄 pom.xml