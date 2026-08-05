# Living Hub

주거 문제, 후속 할 일, 계약, 생활비, 식품, 연락 기록을 한곳에 모아
**오늘 처리해야 할 행동**을 보여주는 개인 생활 관리 웹 서비스다.

## 핵심 문제

생활 정보는 문자, 메모, 달력, 은행 앱, 냉장고와 기억 속에 흩어져 있다.
문제를 발견하는 것보다 다음 연락일, 계약 통보기한, 납부일처럼
`후속 조치`를 놓치지 않는 것이 더 어렵다.

Living Hub는 각 기록을 단순 보관하는 데서 끝내지 않고,
관련 행동과 기한을 통합 대시보드로 연결한다.

## 첫 번째 완성 흐름

```text
인터넷 문제 등록
→ 처리 기록 추가
→ 집주인 연락 기록
→ 재확인 할 일 생성
→ 오늘 대시보드에 표시
→ 해결 완료
```

## 권장 기술 방향

- Java 21
- Spring Boot
- Spring MVC + Thymeleaf
- Spring Data JPA
- Spring Security
- PostgreSQL
- Flyway
- Bean Validation
- Actuator
- Testcontainers

정확한 의존성 버전은 빌드 파일에서 고정하고, 무의미한 최신화는 피한다.

## 아키텍처

하나의 애플리케이션으로 배포하는 **모듈형 모놀리스**다.
기능별 최상위 패키지를 모듈 경계로 삼고, 모듈 내부는
`presentation → application → domain ← infrastructure` 방향을 따른다.

초기 핵심 모듈:

- `identity`: 사용자와 인증 연동 정보
- `residence`: 거주지
- `issue`: 생활 이슈와 처리 기록
- `task`: 후속 행동과 기한
- `dashboard`: 여러 모듈의 읽기 모델 조합

후속 모듈:

- `contract`
- `finance`
- `food`
- `communication`
- `notification`

## 문서

- 제품 전체: `docs/PROJECT.md`
- 아키텍처: `docs/ARCHITECTURE.md`
- 도메인: `docs/DOMAIN.md`
- 보안: `docs/SECURITY.md`
- API: `docs/API.md`
- 개발 규칙: `docs/DEVELOPMENT.md`
- 테스트: `docs/TESTING.md`
- 에이전트 작업 방식: `docs/AI_AGENT_WORKFLOW.md`
- 현재 진행 상태: `docs/PROJECT_STATUS.md`
- 주요 변경 이력: `CHANGELOG.md`
- 설계 결정: `docs/decisions/`

LLM 에이전트는 먼저 루트 `AGENTS.md`를 읽고 필요한 문서로 이동한다.
