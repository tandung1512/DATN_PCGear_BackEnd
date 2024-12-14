# Bước 1: Sử dụng image openjdk:17
FROM openjdk:17

# Bước 2: Đặt thư mục làm việc trong container
WORKDIR /app

# Bước 3: Sao chép file JAR của ứng dụng vào container
COPY target/DATN_PCGear_BackEnd-0.1.jar /app/app.jar

# Bước 4: Mở cổng 8080 trong container
EXPOSE 8080

# Bước 5: Lệnh để chạy ứng dụng khi container khởi động
ENTRYPOINT ["java", "-jar", "app.jar"]