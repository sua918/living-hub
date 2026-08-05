# 권장 저장소 구조

```text
living-hub/
├─ AGENTS.md
├─ README.md
├─ build.gradle
├─ settings.gradle
├─ gradlew
├─ gradlew.bat
├─ gradle/
├─ compose.yml
├─ .env.example
├─ docs/
│  ├─ PROJECT.md
│  ├─ ARCHITECTURE.md
│  ├─ DOMAIN.md
│  ├─ SECURITY.md
│  ├─ API.md
│  ├─ DEVELOPMENT.md
│  ├─ TESTING.md
│  ├─ AI_AGENT_WORKFLOW.md
│  ├─ decisions/
│  │  ├─ README.md
│  │  ├─ 0001-modular-monolith.md
│  │  ├─ 0002-session-based-authentication.md
│  │  └─ 0003-package-by-feature.md
│  └─ templates/
│     ├─ ADR_TEMPLATE.md
│     └─ MODULE_AGENTS_TEMPLATE.md
└─ src/
   ├─ main/
   │  ├─ java/com/example/livinghub/
   │  │  ├─ LivingHubApplication.java
   │  │  ├─ identity/
   │  │  ├─ residence/
   │  │  ├─ issue/
   │  │  │  ├─ AGENTS.md
   │  │  │  ├─ presentation/
   │  │  │  ├─ application/
   │  │  │  ├─ domain/
   │  │  │  └─ infrastructure/
   │  │  ├─ task/
   │  │  ├─ dashboard/
   │  │  └─ common/
   │  └─ resources/
   │     ├─ application.yml
   │     ├─ templates/
   │     ├─ static/
   │     └─ db/migration/
   └─ test/
      └─ java/com/example/livinghub/
```

## 문서 분리 원칙

루트 `AGENTS.md`에는 다음만 둔다.

- 프로젝트 한 줄 설명
- 어디에 무슨 문서가 있는지
- 절대 위반하면 안 되는 규칙
- 공통 작업 순서
- 완료 기준

상세 설명은 다음으로 보낸다.

- 제품과 범위 → `docs/PROJECT.md`
- 아키텍처 → `docs/ARCHITECTURE.md`
- 보안 → `docs/SECURITY.md`
- 개발 방식 → `docs/DEVELOPMENT.md`
- 테스트 → `docs/TESTING.md`
- 에이전트 절차 → `docs/AI_AGENT_WORKFLOW.md`
- 결정의 이유 → `docs/decisions/`
- 모듈 고유 규칙 → 각 모듈의 `AGENTS.md`

## 로컬 AGENTS.md를 남발하지 않는 기준

초기에는 루트 파일 하나로 시작한다.
다음처럼 모듈 고유 규칙이 생겼을 때만 추가한다.

- 복잡한 상태 전이
- 외부 연동
- 민감 데이터
- 모듈 고유 실행 명령
- 반드시 함께 바꿔야 하는 파일 집합

따라서 첫 구현 단계에서는 `issue/AGENTS.md`만 추가하고,
다른 모듈은 복잡성이 생길 때 추가해도 충분하다.
