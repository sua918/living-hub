# ADR-0003: package-by-feature를 사용한다

- 상태: Accepted
- 날짜: 2026-08-05

## 맥락

Controller, Service, Repository별 전역 패키지는
기능이 늘수록 한 변경의 관련 파일을 흩어 놓고 모듈 경계를 약하게 만든다.

## 결정

`issue`, `task`, `contract`와 같은 기능별 최상위 패키지를 사용한다.
각 모듈 내부에 presentation, application, domain, infrastructure를 둔다.

## 결과

장점:

- 기능의 응집도가 높다.
- 변경 영향 범위를 찾기 쉽다.
- 모듈별 AGENTS.md와 테스트를 배치하기 쉽다.

비용:

- 공통 코드 위치를 신중히 정해야 한다.
- 작은 기능에도 과도한 계층을 만들 위험이 있다.

## 후속 조치

빈 계층을 미리 만들지 않고, 필요할 때만 하위 패키지를 추가한다.
