# 🌐 Seoul Wifi Page

서울시 공공 와이파이 데이터를 활용한 웹 애플리케이션으로, 사용자의 위치 기반으로 근처 WiFi 정보를 조회하고 북마크, 검색 기록 기능을 제공하는 서비스입니다. Java Servlet, SQLite3, OpenAPI, JSON(Gson)을 이용하여 구현되었습니다.

---

## ✨ 주요 기능

### 👤 사용자 기능
- 현재 위치(lat, lon) 기반 WiFi 목록 조회 (거리순 정렬)
- WiFi 상세 정보 확인
- 검색 이력 저장 및 조회
- 즐겨찾기(Bookmark) 추가, 삭제
- 즐겨찾기 그룹 생성, 수정, 삭제 및 그룹별 보기

### 🚀 API/데이터 처리
- 서울시 공공와이파이 OpenAPI 호출 (최대 1000단위 반복 호출)
- 받아온 JSON 데이터를 GSON으로 파싱 후 DB 저장
- 사용자 위치와 와이파이 간 거리 계산 (Haversine 공식 사용)
- SQLite3 DB는 프로젝트 실행 시 새로 생성됨

---

## 🔧 기술 스택

| 기술         | 설명                                      |
|--------------|-------------------------------------------|
| Java         | Servlet 기반 백엔드 로직 구현             |
| SQLite3      | 경량 내장형 DB, 웹 실행 시 새로 생성됨   |
| OkHttp3      | 외부 API 호출을 위한 HTTP 클라이언트     |
| Gson         | JSON 검사 & 파싱                     |
| HTML/CSS     | 기본 프론트 화면 구성                     |

---

## 📂 디렉토리 구조 (주요 클래스 위주)

```
src
├── controller               # Java Servlet 컨트롤러
│   ├── FetchApiServlet.java
│   ├── GetResultsServlet.java
│   ├── SaveBookmarkServlet.java
│   ├── SaveSearchHistoryServlet.java
│   └── ...
├── service                  # 비즈니스 로직 (거리 계산, DB처리 위임)
│   ├── IndexService.java
│   ├── BookmarkService.java
│   └── ...
├── repository               # SQLite 연결 및 쿼리 실행
│   ├── IndexRepo.java
│   ├── BookmarkRepo.java
│   └── ...
├── dto                      # 데이터 전달 객체
│   ├── WifiRow.java
│   ├── Bookmark.java
│   └── ...
```

---

## 🔎 실행 방법

### ☕ Servlet 기반 실행
1. 이 프로젝트를 Tomcat이 포함된 Servlet Container에 배포합니다.
2. `/fetch-api?latitude=37.5665&longitude=126.9780` 등을 호출하여 데이터 초기화합니다.
3. 메인 페이지에서 `/get-results` API를 통해 결과를 확인합니다.

> H2 대신 SQLite를 사용하며, 실행 시 DB 파일(`wifiinfo.db`)은 프로젝트 루트에 생성됩니다.

---

## ⚠️ 참고사항
- OpenAPI 인증키는 `config.properties` 에서 관리됩니다 (GitHub에는 포함되지 않음)
- 개발 및 연습용으로, 보안/에러 처리/멀티유저 환경은 최소화되어 있음
- 와이파이 검색 이력은 간단한 위치 저장으로만 구성됨 (추후 확장 가능)

---

## 📄 라이선스 & 제작자
- 이 프로젝트는 개인 연습 목적으로 개발되어 그룹 또는 개인 다운로드에 자유롭게 사용할 수 있습니다.

> 프로젝트 문의 또는 예제적 패턴이 필요할 경우 issue 로 보내주세요!

---

## 🚀 향후 개선 방향
- 검색 이력 기반 추천 기능
- 지도 기반 마커 출력 연동
- 사용자 인증 기능 및 멀티유저 확장
- 페이징 처리, 정렬 기준 추가

