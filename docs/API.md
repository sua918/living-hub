# HTTP 및 API 규칙

## 1. 기본 방향

초기 UI는 Spring MVC와 Thymeleaf를 사용한다.
외부 또는 별도 프론트엔드가 필요할 때 REST API를 추가한다.

Controller는 다음만 담당한다.

- 요청 파싱
- 인증 사용자 식별
- DTO 검증
- Application Service 호출
- 응답 또는 ViewModel 변환

업무 규칙과 소유권 판단을 Controller에 넣지 않는다.

## 2. 경로

서버 렌더링:

```text
/dashboard
/issues
/issues/new
/issues/{publicId}
/tasks
/residences
```

REST API를 공개할 경우:

```text
/api/v1/issues
/api/v1/issues/{publicId}
/api/v1/tasks
```

내부 DB PK를 외부 계약으로 고정하지 않는다.

## 3. 메서드

- `GET`: 조회
- `POST`: 생성 또는 명령
- `PUT`: 전체 교체가 실제로 필요한 경우만
- `PATCH`: 부분 상태 변경
- `DELETE`: 삭제 정책이 명확할 때

GET 요청으로 상태를 변경하지 않는다.

## 4. DTO

요청과 응답 DTO를 분리한다.

```text
CreateIssueRequest
UpdateIssueRequest
ChangeIssueStatusRequest
IssueDetailResponse
IssueListItemResponse
```

규칙:

- Entity를 반환하지 않는다.
- 요청에서 ownerId, createdAt, resolvedAt 같은 서버 관리 필드를 받지 않는다.
- enum은 허용 가능한 값과 오류 메시지를 명확히 한다.
- 날짜와 시간의 시간대 의미를 문서화한다.

## 5. 오류 응답

REST API 예시:

```json
{
  "code": "ISSUE_NOT_FOUND",
  "message": "생활 이슈를 찾을 수 없습니다.",
  "fieldErrors": [],
  "traceId": "..."
}
```

규칙:

- 오류 코드는 안정적인 기계용 식별자다.
- 사용자 메시지와 내부 로그를 분리한다.
- 없는 리소스와 접근 불가 응답은 정보 노출 위험을 고려해 설계한다.
- 스택 트레이스를 응답하지 않는다.
- Validation 오류는 필드별로 제공한다.
- 도메인 예외를 HTTP 상태 코드로 한곳에서 변환한다.

## 6. 페이지네이션과 정렬

목록이 커질 가능성이 있는 API는 페이지네이션을 사용한다.

허용 정렬 필드는 서버 allowlist로 제한한다.

```text
createdAt
dueAt
priority
status
```

클라이언트가 임의 컬럼명이나 쿼리 조각을 전달하지 못하게 한다.

## 7. 멱등성

자동 할 일 생성, 외부 알림 요청, 반복 작업은 멱등성을 고려한다.

- 출처 기반 unique key
- 요청 idempotency key
- 처리 상태 저장

중복 실행이 사용자에게 중복 할 일이나 중복 발송을 만들지 않아야 한다.

## 8. API 변경

호환성을 깨는 변경 전 확인:

- 필드 삭제 또는 의미 변경
- enum 값 변경
- 경로 변경
- 인증 방식 변경
- 오류 코드 변경

공개 API가 생기면 변경 정책과 지원 기간을 별도로 문서화한다.
