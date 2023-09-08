## 득근득근

이 프로젝트는 프로젝트는 회원 사용자가 트레이닝 트레이너를 직접 구할 수 있도록 돕는 플랫폼입니다. 이 프로젝트를 사용하여 회원 사용자와 트레이너 사용자 간의 매칭을 쉽게 만들고 관리할 수 있습니다.

![logo](https://github.com/kim5288403/deukgeun/assets/76669119/882fed53-a60d-4f9d-be09-d9f7b5bd0c6b)

[득근득근 바로가기](https://deukgeun.duckdns.org/)

## **주요 기능**

- 회원가입, 이메일 인증, 로그인, 로그아웃
    - 인증, 인가 (Sptring Security + JWT)
- 게시글 추가, 수정, 삭제 (froala editor 라이브러리 사용)
- 자격증 추가, 수정, 삭제 (자격증 진위 여부 open api 사용)
- 프로필 이미지 추가, 수정, 삭제 (AWS S3 사용)
- 게시글 검색 및 정렬
- 카카오페이 간편 결제(iamport open api 사용)

## **개발 환경**

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

## **인프라 환경**

- ubuntu 22.04.3 LTS
- docker
- docker-composer
- oracle cloud

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

## **참고**

이 프로젝트는 java 8 + Spring Boot 2.7.11을 사용하여 개발되었습니다. 프론트엔드는 html5 무료 템플릿으로 구현되었으며 반응형 웹개발도 구현했습니다, jQuery를 사용하여 Ajax 통신을 처리합니다. 데이터베이스는 MySQL 8.0.33를 사용하며, Thymeleaf를 사용하여 뷰를 렌더링합니다.

## **작성자 정보**

- 작성자: limJyeok
- 이메일 주소: kim5288403[@gmail.com](mailto:myusername@example.com)
- GitHub: [**https://github.com/kim5288403**](https://github.com/kim5288403/deukgeun)
