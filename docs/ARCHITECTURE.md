# 아키텍처

## 1. 선택: 모듈형 모놀리스

초기에는 하나의 Spring Boot 애플리케이션과 하나의 PostgreSQL 데이터베이스를 사용한다.

선택 이유:

- 배포와 디버깅이 단순하다.
- 트랜잭션을 자연스럽게 사용할 수 있다.
- 개인 프로젝트 규모에서 운영 비용이 낮다.
- 기능별 경계를 유지하면 나중에 필요한 모듈만 분리할 수 있다.

마이크로서비스 전환은 목표가 아니다.
독립적인 확장·배포·장애 격리가 실제로 필요해질 때만 검토한다.

## 2. 최상위 패키지

```text
com.example.livinghub
├─ LivingHubApplication.java
├─ identity
├─ residence
├─ issue
├─ task
├─ dashboard
├─ contract
├─ finance
├─ food
├─ communication
├─ notification
└─ common
```

최상위 기능 패키지 하나를 애플리케이션 모듈 하나로 본다.
`common`은 편의 코드 저장소가 아니며, 정말 여러 모듈에 공통인 기술 요소만 둔다.

## 3. 모듈 내부 구조

```text
issue
├─ presentation
│  ├─ web
│  └─ api
├─ application
│  ├─ IssueCommandService
│  ├─ IssueQueryService
│  ├─ port
│  └─ dto
├─ domain
│  ├─ Issue
│  ├─ IssueLog
│  ├─ IssueStatus
│  ├─ IssueRepository
│  └─ event
└─ infrastructure
   ├─ persistence
   └─ config
```

작은 모듈에서는 비어 있는 계층을 억지로 만들지 않는다.
복잡도가 생길 때만 하위 패키지를 추가한다.

## 4. 의존성 방향

```text
presentation → application → domain
                         ↑
                infrastructure
```

- `presentation`: HTTP, 폼, JSON, 인증 사용자 해석
- `application`: 유스케이스, 트랜잭션, 권한·소유권 조정
- `domain`: 상태, 규칙, 불변식, 도메인 이벤트
- `infrastructure`: JPA, 외부 API, 파일 저장소, 메시지 제공자

Domain은 Spring MVC, Controller, 외부 SDK에 의존하지 않는다.
JPA 사용 수준은 프로젝트 난이도에 따라 허용하되, Entity가 HTTP 계약이 되어서는 안 된다.

## 5. 모듈 간 통신

### 동기 호출

다른 모듈의 공개 Application Facade 또는 조회 인터페이스만 호출한다.

```text
issue.application.IssueFacade
task.application.TaskFacade
```

금지:

```text
task → issue.infrastructure.JpaIssueRepository
task → issue.domain.Issue 엔티티 직접 수정
```

### 비동기 내부 이벤트

즉시 응답에 필요하지 않은 후속 작업은 내부 이벤트를 고려한다.

예:

```text
IssueFollowUpRequested
ContractNoticeDateReached
TaskCompleted
```

이벤트에는 필요한 식별자와 최소 데이터만 담는다.
다른 모듈의 Entity를 이벤트에 넣지 않는다.

초기 이벤트는 같은 프로세스 안에서 처리한다.
이벤트 브로커는 실제 필요가 생기기 전까지 도입하지 않는다.

## 6. 상태와 자동 생성

자동 생성된 할 일은 출처를 가져야 한다.

```text
Task
- sourceType
- sourceId
- sourceRule
- sourceVersion
```

같은 규칙으로 중복 생성되지 않도록 멱등 키를 둔다.

```text
unique(user_id, source_type, source_id, source_rule)
```

사용자가 자동 생성 항목을 수정하거나 삭제할 때의 정책은 기능별로 명시한다.

## 7. 데이터베이스

초기에는 모듈별 논리적 테이블 접두어를 사용한다.

```text
identity_users
residence_residences
issue_issues
issue_logs
task_tasks
contract_contracts
```

규칙:

- PK는 내부 식별자다.
- 외부 공개 식별자는 추측 방지가 필요하면 별도 UUID를 사용한다.
- 모든 사용자 소유 테이블은 `owner_id` 또는 명확한 소유 경로를 가진다.
- 시간은 DB와 서버에서 UTC로 저장하고 표시 시 사용자 시간대로 변환한다.
- 금액은 부동소수점이 아닌 정수 최소 화폐 단위 또는 `BigDecimal`을 사용한다.
- 삭제 정책은 도메인별로 정한다. 무조건 soft delete를 적용하지 않는다.
- 스키마 변경은 Flyway로만 수행한다.

## 8. 조회 모델

Dashboard가 다른 모듈의 Entity를 모아 수정하지 않는다.
다음 중 단순한 방법부터 선택한다.

1. 각 모듈의 Query API를 호출해 조합
2. 읽기 전용 Projection
3. 성능 문제가 확인되면 별도 Dashboard Read Model

초기에는 1번으로 시작한다.

## 9. 외부 인터페이스

서버 렌더링 UI를 우선한다.

```text
Browser
→ Spring MVC Controller
→ Application Service
→ Domain/Repository
→ Thymeleaf View
```

REST API가 필요해지면 같은 Application Service를 사용하되
웹 폼 DTO와 API DTO는 분리한다.

## 10. 확장 기준

새 기능을 추가할 때 다음을 확인한다.

- 기존 모듈의 하위 기능인가?
- 독립적인 용어, 데이터, 정책, 변경 이유가 있는가?
- 다른 모듈 없이도 설명 가능한가?
- 공개 API가 필요한가?

독립된 책임이 있으면 새 최상위 모듈로 만든다.
단순한 화면 또는 조회 방식 차이라면 새 모듈을 만들지 않는다.

## 11. 모듈 분리 후보

다음 조건이 반복적으로 나타날 때 별도 서비스 분리를 검토한다.

- 독립 배포가 필수
- 장애 격리가 필수
- 처리량과 확장 방식이 크게 다름
- 별도 보안 경계가 필요
- 데이터 소유권을 완전히 분리해야 함

분리 전에 ADR로 근거와 비용을 기록한다.

## 12. 운영 가능성

초기부터 다음은 유지한다.

- 구조화 로그와 요청 상관관계 ID
- Health 확인
- 최소 메트릭
- 환경별 외부 설정
- DB 백업과 복원 절차
- 오류 추적에 필요한 맥락
- 민감정보 마스킹

Spring Modulith는 모듈 구조 검증과 문서화가 필요해질 때 선택적으로 도입한다.
도구가 없더라도 패키지 경계와 아키텍처 테스트는 유지한다.
