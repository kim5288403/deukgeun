## 득근득근

이 프로젝트는 프로젝트는 회원 사용자가 트레이닝 트레이너를 직접 구할 수 있도록 돕는 플랫폼입니다. 이 프로젝트를 사용하여 회원 사용자와 트레이너 사용자 간의 매칭을 쉽게 만들고 관리할 수 있습니다.
![image](https://github.com/kim5288403/deukgeun/assets/76669119/cee64335-e65e-4770-bc0e-fc49f293ea85)

## 🖥 주요 개발 내용

1. Spring Security와 JWT를 활용한 인증 및 인가 관리
2. Redis + Spring cache를 활용한 cache 관리
3. [data.kca.kr](http://data.kca.kr/) open api를 활용한 국가공인자격증 진위 여부
4. froala editor 라이브러리를 활용한 게시물 서버 및 DB 저장
5. AWS S3 를 활용한 이미지 저장
6. GitHub Action으로 CI/CD 구현
7. KafKa를 활용한 Event Driven Architecture 설계
8. Domain Driven Development 설계
9. BDD 스타일 Test Driven Development 개발
10. 카카오페이 간편 결제(iamport open api 사용)

## 🛠 **개발 환경**

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
- kafka 7.3.3
- iamport 0.2.23

## 🌐 **인프라 환경**

- Ubuntu 22.04.3 LTS
- Docker
- Docker-composer
- Oracle cloud

## **소프트웨어 설계 및 개발**
- Domain Driven Design
- Event Driven Architecture
- Test Driven Development

## **통합/배포**
- github
- github action
- CI/CD

## **테스트 코드**
BDD 스타일 TDD 형식으로 작성했습니다.

- 개발 순서
  - Repository
  - Service
  - Controller

- Repository
  - DataJpaTest
  - Test DB H2 DB를 사용하여 테스트했습니다.
          
- Service, Controller
  - SpringBootTest

## 🔔 **참고**

이 프로젝트는 java 8 + Spring Boot 2.7.11을 사용하여 개발되었습니다. 프론트엔드는 html5 무료 템플릿으로 구현되었으며 반응형 웹개발도 구현했습니다, jQuery를 사용하여 Ajax 통신을 처리합니다. 데이터베이스는 MySQL 8.0.33를 사용하며, Thymeleaf를 사용하여 뷰를 렌더링합니다.

## **작성자 정보**

- 작성자: limJyeok
- 이메일 주소: kim5288403[@gmail.com](mailto:myusername@example.com)
- GitHub: [**https://github.com/kim5288403**](https://github.com/kim5288403/deukgeun)
- [득근득근 바로가기](https://deukgeun.duckdns.org/)

