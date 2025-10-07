# Base image Java 21
FROM openjdk:21-jdk-slim

# Thư mục làm việc trong container
WORKDIR /app

# Copy Maven wrapper và pom.xml để cache dependencies
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Tải dependencies offline (giúp build nhanh hơn)
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy toàn bộ source code
COPY src ./src

# Expose port Spring Boot
EXPOSE 8080

# Build project
RUN ./mvnw clean package -DskipTests

# Chạy Spring Boot
CMD ["java", "-jar", "target/your-app.jar"]
