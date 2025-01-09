# 기본 이미지 설정
FROM bellsoft/liberica-openjdk-alpine:17

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY build/libs/danjam-server-0.0.1-SNAPSHOT.jar app.jar

# 빌드된 JAR 파일을 실행
CMD ["java", "-jar", "app.jar"]
