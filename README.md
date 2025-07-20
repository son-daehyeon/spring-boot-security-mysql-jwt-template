<img src="https://r2cdn.perplexity.ai/pplx-full-logo-primary-dark%402x.png" class="logo" width="120"/>

# Spring Boot Security MySQL JWT Template

## ⚡️ 전용 IntelliJ Plugin

이 템플릿과 함께 사용할 수 있는 **Spring Boot Domain Generator** IntelliJ 플러그인이 있습니다.
`Ctrl + Shift + S` 단축키로 완전한 CRUD 구조의 도메인을 자동 생성할 수 있어 개발 생산성을 크게 향상시킵니다.

📦 **플러그인 저장소**: https://github.com/son-daehyeon/intellij-spring-boot-domain-generator

### 주요 기능

- **⚡ 원클릭 생성**: `Ctrl + Shift + S`로 즉시 도메인 생성
- **🎯 완전한 CRUD**: Controller, Service, Entity, Repository, DTO, Exception 자동 생성
- **🔗 템플릿 호환**: 이 템플릿과 완벽 호환

### 자동 생성 구조

```
domain/
└── [도메인명]/
    ├── controller/[도메인명]Controller.java
    ├── service/[도메인명]Service.java
    ├── entity/[도메인명]Entity.java
    ├── repository/[도메인명]Repository.java
    ├── dto/[도메인명]RequestDto.java, [도메인명]ResponseDto.java
    └── exception/[도메인명]NotFoundException.java
```

## 🛠 기술 스택

- **Java 21**
- **Spring Boot 3.5.3**
- **Spring Security**
- **JWT (JSON Web Token)**
- **MySQL**
- **Redis**
- **JPA/Hibernate**
- **Gradle**

## 🚀 실행 방법

### 1. 저장소 클론

```bash
git clone https://github.com/son-daehyeon/spring-boot-security-mysql-jwt-template.git
cd spring-boot-security-mysql-jwt-template
```

### 2. 환경 변수 설정

프로젝트 루트에 `.env` 파일을 생성합니다.

```env
# Database
MYSQL_HOST=localhost
MYSQL_PORT=3306
MYSQL_DATABASE=your_database
MYSQL_USERNAME=your_username
MYSQL_PASSWORD=your_password

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=your_redis_password

# JWT
JWT_KEY=your-secret-key-here
```

### 3. 애플리케이션 실행

```bash
./gradlew bootRun
```

## 🚀 CI/CD 배포 (GitHub Actions)

| 단계              | 설명                                                                                       |
|:----------------|:-----------------------------------------------------------------------------------------|
| **Build Image** | -  코드 체크아웃<br>-  Buildx 설정<br>-  Docker Hub 로그인<br>-  애플리케이션 이미지를 `commit SHA` 태그로 빌드·푸시 |
| **Deploy EC2**  | -  SSH로 EC2 접속<br>-  최신 이미지 Pull<br>-  기존 컨테이너 중지·삭제<br>-  새 컨테이너를 `.env` 파일과 함께 실행      |

### 필요한 GitHub Secrets/Variables 설정

#### 🔐 Secrets (민감한 정보)

| 이름                   | 설명                           | 예시 값                                 |
|:---------------------|:-----------------------------|:-------------------------------------|
| `DOCKERHUB_PASSWORD` | Docker Hub 계정 패스워드           | `your_dockerhub_password`            |
| `EC2_HOST`           | EC2 인스턴스의 퍼블릭 IP 주소          | `13.125.123.456`                     |
| `EC2_PORT`           | EC2 SSH 접속 포트                | `22`                                 |
| `EC2_USER`           | EC2 SSH 사용자명                 | `ubuntu` (Amazon Linux: `ec2-user`)  |
| `EC2_SSH_KEY`        | EC2 접속용 SSH 개인 키 (PEM 파일 내용) | `-----BEGIN RSA PRIVATE KEY-----...` |

#### 📋 Variables (공개 가능한 설정)

| 이름                   | 설명                  | 예시 값                      |
|:---------------------|:--------------------|:--------------------------|
| `DOCKERHUB_USERNAME` | Docker Hub 사용자명     | `your_dockerhub_username` |
| `DOCKER_IMAGE_NAME`  | Docker 이미지 이름       | `spring-boot-template`    |
| `ENV_FILE`           | EC2 서버의 환경 변수 파일 경로 | `/home/ubuntu/.env`       |
| `DOCKER_NETWORK`     | Docker 네트워크 이름      | `app-network`             |

#### 설정 방법

1. **GitHub 저장소** → **Settings** → **Secrets and variables** → **Actions**
2. **Secrets** 탭에서 민감한 정보 등록
3. **Variables** 탭에서 공개 설정 등록

## 🏷️ GitHub Label 관리 시스템

### 라벨 카테고리

| 카테고리     | 라벨                                                                                       | 설명       |
|:---------|:-----------------------------------------------------------------------------------------|:---------|
| **타입**   | `bug`, `feature`, `enhancement`, `documentation`, `question`                             | 이슈 유형 분류 |
| **우선순위** | `priority/critical`, `priority/high`, `priority/medium`, `priority/low`                  | 작업 우선순위  |
| **상태**   | `status/needs-triage`, `status/in-progress`, `status/blocked`, `status/ready-for-review` | 진행 상황    |
| **영역**   | `area/authentication`, `area/database`, `area/security`, `area/api`, `area/deployment`   | 기술 영역    |
| **기여**   | `good-first-issue`, `help-wanted`                                                        | 기여자 가이드  |

### Label Sync 사용 방법

```bash
cd scripts
npm install
npm start <GITHUB_TOKEN> [REPO_OWNER] [REPO_NAME]
```

### 이슈 템플릿

- **🐛 Bug Report**: 버그 신고 (자동 라벨: `bug`, `status/needs-triage`)
- **✨ Feature Request**: 기능 제안 (자동 라벨: `feature`, `status/needs-triage`)
- **❓ Question**: 사용법 질문 (자동 라벨: `question`, `help-wanted`)
- **📚 Documentation**: 문서 개선 (자동 라벨: `documentation`, `status/needs-triage`)

## 📋 API 명세서

### 공통 응답

```json
{
  "success": true,
  "message": null,
  "data": {}
}
```

### 인증 API

| 메서드  | 경로                    | 설명                                                  |
|:-----|:----------------------|:----------------------------------------------------|
| POST | `/auth/register`      | 회원가입                                                |
| POST | `/auth/login`         | 로그인                                                 |
| POST | `/auth/refresh-token` | 토큰 갱신                                               |
| GET  | `/auth/me`            | 내 정보 조회 (헤더: `Authorization: Bearer {accessToken}`) |
