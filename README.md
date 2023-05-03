## 득근득근

이 프로젝트는 트레이너와 회원간에 연결을 돕는 웹 어플리케이션입니다. 현재는 트레이너 부분만 개발중이며 트레이너 사용자는 로그인하여 pt홍보 게시글을 추가, 수정, 삭제 할 수 있으며 게시글에 추가할 내용으로 자격증, 프로필 이미지 정보를 추가할 수 있습니다.

## **사용 기술**

- Java 8
- Spring Boot 2.7.11
- Gradle 7.6
- Spring Data JPA 2.7.11
- MySQL 8.0.33
- Thymeleaf 2.7.11
- jQuery 3.6.0
- Jsonwebtoken 0.9.1
- Security 5.7.8
- Webflux 5.3.27
- redis 2.7.11

## **기능**

- 회원가입, 이메일 인증, 로그인, 로그아웃
    - 인증, 인가 (Sptring Security + JWT)
- 게시글 추가(froala editor 라이브러리 사용), 수정, 삭제
- 자격증 추가( 자격증 진위 여부 open api 사용), 수정, 삭제
- 프로필 이미지 추가, 수정, 삭제
- 게시글 검색 및 정렬
- 게시글 상세 정보 (프로필 이미지, 게시글 내용, pt가격, 자격증) 표시

## **설치 및 실행**

1. MySQL 8.0.33 데이터베이스를 설치합니다.

 2. 이 프로젝트를 clone 합니다.

```java
git clone https://github.com/kim5288403/deukgeun.git
```

1. Redis-x64-3.0.504.msi를 설치합니다.
    1.  https://github.com/microsoftarchive/redis/releases
2. 자격증 진위 여부 api를 위한 api key 발급
    1. [https://www.kca.kr/contentsView.do?pageId=www177](https://www.kca.kr/contentsView.do?pageId=www177)

 5. **`application.properties`** 파일에서 MySQL 연결 정보와  수정과 여러 설정을 추가합니다.

```java
spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name?useSSL=false&characterEncoding=UTF-8&serverTimezone=Asia/Seoul
spring.datasource.username=your_database_username
spring.datasource.password=your_database_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.properties.hibernate.show_sql=true

spring.jpa.hibernate.ddl-auto=update

spring.servlet.multipart.maxFileSize=10MB
spring.servlet.multipart.maxRequestSize=10MB

spring.mail.host=your_email_host
spring.mail.port=your_email_port
spring.mail.username=your_email_username
spring.mail.password=your_email_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

logging.level.org.springframework.security=info

jwt.secretKey=deuckgeunproject
//30 * 60 * 1000
jwt.authTokenTime=1800000
//60 * 60 * 3000
jwt.refreshTokenTime=10800000

trainer.profile.filePath=C:\\eclipse\\deukgeun_workspace\\deukgeun\\src\\main\\resources\\static\\images\\trainer\\profile
trainer.post.filePath=C:\\eclipse\\deukgeun_workspace\\deukgeun\\src\\main\\resources\\static\\images\\trainer\\post\\
trainer.post.url=http://localhost:8080/api/trainer/post/image/
trainer.mail.email=your_project_email
trainer.license.api.key=your_license_api_key
trainer.license.api.uri=http://data.kca.kr/api/v1/cq/certificate/check

deukgeun.role.trainer=trainer

spring.redis.host=127.0.0.1
spring.redis.port=6379

spring.cache.type = redis
spring.cache.redis.cache-null-values=true
```

1. 프로그램을 실행합니다.

```java
spring run DeukgeunApplication.java
```

1. 브라우저에서 http://localhost:8080으로 접속합니다.

## **참고**

이 프로젝트는 java 8 + Spring Boot 2.7.11을 사용하여 개발되었습니다. 프론트엔드는 html5 무료 템플릿으로 구현되었으며 반응형 웹개발도 구현했습니다, jQuery를 사용하여 Ajax 통신을 처리합니다. 데이터베이스는 MySQL 8.0.33를 사용하며, Thymeleaf를 사용하여 뷰를 렌더링합니다.

## **작성자 정보**

- 작성자: limJyeok
- 이메일 주소: kim5288403**[@gmail.com](mailto:myusername@example.com)**
- GitHub: [**https://github.com/myusername**](https://github.com/kim5288403/deukgeun)
