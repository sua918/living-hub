# Changelog

이 문서는 Living Hub의 사용자 기능, 공개 계약, 보안, DB 스키마,
운영 방식에 대한 의미 있는 변경을 기록한다.

## Unreleased

### Added

- Java 21과 Spring Boot 기반 애플리케이션 초기 골격을 구성했다.
- PostgreSQL, Spring Data JPA, Flyway와 Testcontainers PostgreSQL을 이용한 영속성 연동 검증 기반을 추가했다.
- 사용자 내부 PK, 정규화된 사용자명과 비밀번호 해시만 저장하는 `identity_users` 스키마 및 영속성 모델을 추가했다.
- 공통·로컬·테스트·운영 Spring Profile을 분리하고 DB 인증정보를 환경별 외부 설정으로 격리했다.
- 제품, 아키텍처, 도메인, 보안, API, 개발, 테스트 및 에이전트 작업 지침을 문서화했다.
- 에이전트 자율 local commit, 다음 작업 핸드오프와 Codex Goal 적합성 판단 절차를 문서화했다.
- 모듈형 모놀리스와 package-by-feature 구조를 설계 결정으로 채택했다.

### Security

- 세션 기반 인증 원칙과 모든 사용자 소유 데이터에 대한 소유권 검증 원칙을 문서화했다.
- 정규화된 사용자명, 내부 PK 비노출, 비밀번호 단방향 해시와 탈퇴 사용자명 재사용 금지 정책을 결정했다.
