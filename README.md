## 득근득근

득근득근 프로젝트는 회원 사용자와 트레이너 사용자 간의 효과적인 트레이닝 매칭을 지원하는 플랫폼입니다. 이 플랫폼을 통해 사용자들은 트레이너를 쉽게 찾아볼 수 있고, 트레이닝 서비스를 이용하며 매칭을 관리할 수 있습니다.
![image](https://github.com/kim5288403/deukgeun/assets/76669119/cee64335-e65e-4770-bc0e-fc49f293ea85)

## 🖥 주요 개발 내용

#### 득근득근 프로젝트의 주요 개발 내용은 다음과 같습니다.

- CI/CD 구현: GitHub Action을 활용하여 지속적 통합 및 지속적 배포를 구현했습니다.

- Event Driven Architecture (EDA) 설계: Kafka를 활용하여 이벤트 주도 아키텍처를 구성하여 서비스 간의 비동기 통신을 가능하게 했습니다.

- Domain Driven Development (DDD) 설계: 도메인 주도 개발 방식을 채택하여 각 도메인을 모델링하고 개발했습니다.

- Test Driven Development (TDD) 개발: BDD 스타일로 TDD를 수행하여 소프트웨어의 품질을 유지했습니다.

- 인증 및 인가 관리: Spring Security와 JWT(Json Web Token)를 사용하여 사용자의 인증 및 인가를 관리했습니다.

- 캐시 관리: Redis와 Spring Cache를 활용하여 캐시를 효율적으로 관리했습니다.

- 공인 자격증 확인: data.kca.kr의 Open API를 활용하여 국가 공인 자격증의 진위 여부를 확인할 수 있도록 구현했습니다.

- 게시물 관리: Froala Editor 라이브러리를 사용하여 게시물을 서버와 DB에 저장하고 AWS S3를 활용하여 이미지를 저장했습니다.

- 결제 시스템: 카카오페이의 간편 결제를 구현하기 위해 Iamport Open API를 사용했습니다.

## 🛠 **개발 환경**

- 언어: Java 8
- 프레임워크: Spring Boot 2.7.11
- 빌드 도구: Gradle 7.6
- 데이터베이스: MySQL 8.0.33
- 템플릿 엔진: Thymeleaf 2.7.11
- 클라이언트 측 라이브러리: jQuery 3.6.0
- 보안 및 인증: Jsonwebtoken 0.9.1, Spring Security 5.7.8
- 비동기 프로그래밍: Webflux 5.3.27
- 캐싱: Redis 2.7.11
- 이벤트 버스: Kafka 7.3.3

## 🌐 **인프라 환경**
- 클라우드: Oracle Cloud
- 운영 체제: Ubuntu 22.04.3 LTS
- 컨테이너 관리: Docker

## 👨‍💻 **소프트웨어 설계 및 개발**
- 도메인 주도 개발 (DDD)
- 이벤트 주도 아키텍처 (EDA)
- 테스트 주도 개발 (TDD)

## 😸 **Github action CI/CD 설정**

#### Continuous Integration (CI) 설정
CI는 소스 코드의 변경 사항을 지속적으로 통합하고 테스트하는 프로세스입니다. 이를 위해 GitHub Actions를 사용하여 다음과 같이 설정할 수 있습니다.
- 통합 테스트 자동화
  - GitHub Actions 워크플로우를 설정하여 코드가 변경될 때마다 자동으로 통합 테스트를 실행합니다.
  - 테스트 스크립트를 실행하여 빌드 및 단위 테스트를 수행합니다.
  - 테스트가 실패하면 알림을 보내고 빌드가 실패로 표시됩니다.

#### Continuous Deployment (CD) 설정

CD는 통합 테스트를 통과한 코드를 실제 서버에 자동으로 배포하는 프로세스입니다. SSH를 사용하여 서버에 연결하고, 배포 스크립트를 실행하는 것을 포함합니다.

- SSH 서버 연결 및 배포
  - GitHub Actions 워크플로우를 설정하여 통합 테스트가 성공하면 자동으로 서버에 연결하고 배포 스크립트를 실행합니다.
  - 서버에 대한 SSH 키를 GitHub Secrets에 안전하게 저장하고 사용합니다.

## 👨‍🔬 **Test-Driven Development (TDD) 개발 순서**

TDD는 소프트웨어 개발을 위한 접근 방식으로, 테스트 케이스를 먼저 작성하고 그에 따라 코드를 개발하는 방식입니다. 다음은 TDD를 따르는 개발 순서입니다.

#### 1. Repository
- 테스트 작성: Repository 클래스에 대한 테스트 케이스를 작성합니다. 주로 데이터베이스 연동을 테스트합니다.
  
- 코드 작성: 테스트를 통과할 수 있도록 Repository 클래스를 구현합니다.

#### 2. Service
- 테스트 작성: Service 클래스에 대한 테스트 케이스를 작성합니다. 비즈니스 로직을 테스트합니다.
- 코드 작성: 테스트를 통과할 수 있도록 Service 클래스를 구현합니다. Repository 클래스와 연동하여 데이터를 처리합니다.

#### 3. Controller
- 테스트 작성: Controller 클래스에 대한 테스트 케이스를 작성합니다. API 엔드포인트를 테스트합니다.
- 코드 작성: 테스트를 통과할 수 있도록 Controller 클래스를 구현합니다. Service 클래스와 연동하여 요청을 처리하고 응답을 생성합니다.

## 🔔 **참고**

이 프로젝트는 java 8 + Spring Boot 2.7.11을 사용하여 개발되었습니다. 프론트엔드는 html5 무료 템플릿으로 구현되었으며 반응형 웹개발도 구현했습니다, jQuery를 사용하여 Ajax 통신을 처리합니다. 데이터베이스는 MySQL 8.0.33를 사용하며, Thymeleaf를 사용하여 뷰를 렌더링합니다.

## 📝 **작성자 정보**

- 작성자: limJyeok
- 이메일 주소: kim5288403[@gmail.com](mailto:myusername@example.com)
- GitHub: [**https://github.com/kim5288403**](https://github.com/kim5288403/deukgeun)
- [득근득근 바로가기](https://deukgeun.duckdns.org/)

