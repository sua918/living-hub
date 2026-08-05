# Project Status

마지막 업데이트: 2026-08-05

## 현재 단계

Phase 0 — 기반. Spring Boot 애플리케이션의 기본 실행 환경과 PostgreSQL 기반 영속성 검증 기반을 마련하고, 첫 기능 모듈을 구현할 준비를 하는 단계다.

## 완료

- Java 21과 Spring Boot 4.1.0 기반 초기 프로젝트 골격
- Gradle Wrapper와 Spring MVC, Thymeleaf, Spring Security, Bean Validation, Actuator 의존성 구성
- PostgreSQL, Spring Data JPA, Flyway 의존성 구성
- Testcontainers PostgreSQL을 사용하는 애플리케이션 컨텍스트·데이터소스·JPA·Flyway 연동 테스트 기반
- 제품, 아키텍처, 도메인, 보안, API, 개발, 테스트 및 에이전트 작업 지침 문서
- 에이전트 자율 local commit, 다음 작업 핸드오프, Codex Goal 적합성 판단 정책
- 모듈형 모놀리스, 세션 기반 인증, package-by-feature 설계 결정
- 로컬 Git 저장소 초기화와 GitHub Public 저장소 연결 및 최초 원격 백업

## 진행 중

- 없음

## 다음 작업

1. 공통·로컬·테스트·운영 Spring Profile을 분리하고 비밀값 외부 주입 및 테스트 DB 격리를 검증한다.
2. 첫 도메인용 Flyway 마이그레이션과 모듈별 영속성 테스트를 기존 Testcontainers 기반 위에 추가한다.
3. Phase 0 범위의 인증 및 사용자 소유권 기반을 구현하고 권한 경계 테스트를 추가한다.
4. CI와 모듈 경계 테스트 기반을 마련한다.

## 주요 결정

- 하나의 애플리케이션과 PostgreSQL로 시작하는 모듈형 모놀리스를 사용한다. 자세한 내용은 [ADR 0001](decisions/0001-modular-monolith.md)을 참조한다.
- 인증은 서버 세션 기반으로 설계한다. 자세한 내용은 [ADR 0002](decisions/0002-session-based-authentication.md)를 참조한다.
- 기능별 최상위 패키지를 모듈 경계로 삼는다. 자세한 내용은 [ADR 0003](decisions/0003-package-by-feature.md)을 참조한다.

## 알려진 문제와 위험

- 기능 모듈, 사용자 기능, 인증·인가 흐름은 아직 구현되지 않았다.
- Flyway 마이그레이션이 아직 없어 테스트에서 적용된 마이그레이션 수는 0이다.
- 테스트 profile 외의 공통·로컬·운영 환경 설정 분리는 아직 구성되지 않았다.
- 최종 배포 환경, 파일 저장소, 외부 알림 제공자, 공개 REST API 제공 시점, LLM 기능의 실제 도입 범위는 미결정이다.
