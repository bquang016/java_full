# GIAI ĐOẠN 1: Xây dựng ứng dụng bằng Maven
# Sử dụng một image có sẵn Maven và Java 21 để build code
FROM maven:3.9-eclipse-temurin-21-alpine AS build

# Đặt thư mục làm việc bên trong image là /app
WORKDIR /app

# Sao chép file pom.xml và các thư mục con vào trước để tận dụng cache của Docker
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .

# Tải các dependency cần thiết
RUN mvn dependency:go-offline

# Sao chép toàn bộ mã nguồn vào
COPY src ./src

# Chạy lệnh build của Maven để tạo ra file .jar, bỏ qua các bài test
RUN mvn clean install -DskipTests


# GIAI ĐOẠN 2: Tạo image cuối cùng để chạy ứng dụng
# Sử dụng một image Java JRE nhỏ gọn hơn để tiết kiệm dung lượng
FROM eclipse-temurin:21-jre-alpine

# Đặt thư mục làm việc
WORKDIR /app

# Sao chép file .jar đã được build từ giai đoạn 1 vào image hiện tại
COPY --from=build /app/target/*.jar app.jar

# Thông báo cho Docker rằng container sẽ lắng nghe trên cổng 8085
EXPOSE 8085

# Lệnh sẽ được thực thi khi container khởi động
ENTRYPOINT ["java", "-jar", "app.jar"]