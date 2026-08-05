# 테스트 전략

## 1. 목표

테스트는 코드 줄 수를 늘리는 것이 아니라 다음 위험을 잡아야 한다.

- 업무 규칙 오류
- 사용자 간 데이터 노출
- 모듈 경계 붕괴
- DB 스키마와 구현 불일치
- HTTP 계약 변경
- 자동 생성 중복
- 날짜와 시간대 오류

## 2. 테스트 계층

### Domain Unit Test

대상:

- 상태 전이
- 완료·해결 규칙
- 날짜 계산
- 금액 계산
- 중복 방지 키
- 불변식

Spring Context 없이 빠르게 실행한다.

### Application Test

대상:

- 유스케이스
- 트랜잭션
- 소유권
- Repository 상호작용
- 이벤트 발행
- 중복 요청

### Web Slice Test

대상:

- 라우팅
- 요청 검증
- 인증·CSRF
- View 이름 또는 JSON 규격
- 예외 변환

### Persistence Test

대상:

- 실제 PostgreSQL 쿼리
- 제약 조건
- 인덱스 전제
- Projection
- 동시성 또는 locking이 필요한 부분

가능하면 Testcontainers로 운영 DB와 같은 종류를 사용한다.

### End-to-End / 주요 통합 테스트

핵심 흐름만 유지한다.

```text
로그인
→ 거주지 생성
→ 이슈 생성
→ 처리 기록 추가
→ 후속 할 일 생성
→ 대시보드 확인
→ 해결 완료
```

## 3. 보안 테스트

모든 사용자 소유 리소스 기능에는 최소 다음이 필요하다.

1. 비인증 요청 거부
2. 소유자 요청 성공
3. 다른 사용자 요청 거부
4. 존재하지 않는 ID 처리
5. 쓰기 요청 CSRF 검증
6. 허용되지 않은 필드 변경 차단
7. 오류 응답에 내부 정보 없음

## 4. 아키텍처 테스트

자동 검사 대상:

- 모듈 간 Repository 직접 의존 금지
- 다른 모듈의 infrastructure 참조 금지
- presentation에서 repository 직접 호출 금지
- domain에서 presentation 참조 금지
- Entity가 API 응답 타입으로 사용되지 않음

Spring Modulith 검증 또는 ArchUnit을 선택적으로 사용한다.

## 5. DB 테스트

- 빈 DB에 모든 Flyway migration 적용
- unique, foreign key, not null 제약 확인
- 사용자 소유 조건이 빠진 쿼리 방지
- UTC 저장과 시간대 표시 확인
- 금액 정밀도 확인
- 자동 할 일 멱등성 확인

## 6. 테스트 데이터

- 실제 개인정보를 사용하지 않는다.
- Builder 또는 Fixture를 모듈별로 둔다.
- 과도한 공통 Fixture로 테스트 의도를 숨기지 않는다.
- 시간 의존 테스트는 `Clock`을 주입해 고정한다.
- 랜덤 테스트 데이터가 실패 재현을 방해하지 않게 seed를 관리한다.

## 7. 완료 전 실행

프로젝트에 실제 명령이 정해지면 아래를 루트 AGENTS에 링크한다.

예시:

```bash
./gradlew test
./gradlew check
```

DB 통합 테스트와 정적 분석을 별도 task로 두는 경우,
CI와 로컬 실행 방법을 README에 기록한다.

## 8. 버그 수정 규칙

버그를 수정하기 전에 실패를 재현하는 테스트를 먼저 추가한다.
재현이 어려우면 원인, 관찰 조건, 검증 방법을 문서화한다.
