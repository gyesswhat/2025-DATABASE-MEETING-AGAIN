## 📌 JDBC 드라이버 설정 방법 안내

이 프로젝트는 **MySQL 데이터베이스와 연동**되며, JDBC 드라이버가 필요합니다.  
사용하는 IDE에 따라 아래 지침을 따라 설정해주세요.

---

### ✅ Eclipse 사용자 (교수님 가이드 그대로)

1. [교수님 제공 링크](https://dhan-description.tistory.com/85)에서  
   `mysql-connector-java-8.0.26.jar` 다운로드
2. 이클립스에서 프로젝트 폴더 우클릭 → `Properties`
3. `Java Build Path` → `Libraries` 탭 → `Classpath` 선택
4. **[Add External JARs]** 클릭 후 다운로드한 `.jar` 파일 선택
5. `Apply and Close` 버튼 클릭

---

### ✅ IntelliJ 사용자

1. `lib/` 폴더를 프로젝트 루트에 생성 (또는 이미 있는 경우)
2. `mysql-connector-java-8.0.26.jar` 파일을 `lib/` 폴더에 복사
3. IntelliJ 왼쪽 `Project` 탭에서 `lib` 폴더 우클릭  
   → `Add as Library` 선택
4. JDBC 연결 코드에서 정상적으로 인식됩니다.

📎 참고: 연결 확인용 코드를 DBUtil에 추가해두었습니다.

--- 

## 📌 Commit Message Convention
협업을 위한 일관된 커밋 메시지 작성을 위해 아래 규칙을 따릅니다.

### ✅ 기본 형식

- `타입: 간결한 설명`
- 예시:
    - `feat: 사용자 로그인 기능 추가`
    - `fix: DB 연결 오류 수정`

### 📚 커밋 타입 목록

- **init**: 초기 커밋 (프로젝트 셋업 등)
- **feat**: 새로운 기능 추가
- **fix**: 버그 수정
- **refactor**: 리팩토링 (기능 변화 없이 코드 개선)
- **style**: 코드 스타일 변경 (세미콜론, 공백 등)
- **chore**: 빌드 설정, 패키지 매니저 설정 등 기타 변경
- **docs**: 문서 수정 (README 등)
- **test**: 테스트 코드 추가/수정
- **perf**: 성능 개선
- **build**: 빌드 관련 설정 수정 (예: gradle, jar 설정 등)

## 🌿 브랜치 네이밍 규칙

작업자는 각자 담당하는 기능별로 브랜치를 만들되,  
다음 형식을 따릅니다:

### ✅ 브랜치명 형식

```
feature/기능-이름
```

### ✅ 예시 
실제 RnR과 다릅니다, 참고용으로 봐주세요.

| 작업자   | 담당 기능    | 브랜치명                     |
|----------|--------------|------------------------------|
| woorim   | 로그인        | feature/login-woorim         |
| sumin    | 사용자 기능   | feature/member-sumin         |
| yoonji   | 관리자 기능   | feature/admin-yoonji         |
| gyeore   | 시스템/공통   | feature/system-gyeore        |

### ✅ 브랜치 생성 명령어 예시

```bash
git checkout main
git pull origin main
git checkout -b feature/login-woorim
git push -u origin feature/login-woorim
```

> `-u` 옵션은 해당 로컬 브랜치와 원격 브랜치를 연결시켜 줍니다.

## 📁 프로젝트 구조
해당 내용 참고하셔서 개발하시면 됩니다. feat 구분할 때도 참고해주세요.

```text
📁 MeetingAgainApp/
├── 📁 lib/
│   └── 📄 mysql-connector-java-8.0.26.jar         # MySQL DB 연동을 위한 JDBC 드라이버
├── 📁 resources/
│   └── 🗃️ db.properties                           # DB 연결 URL, 유저명, 비밀번호 등 설정 파일
├── 📁 src/
│   ├── 📁 app/
│   │   └── 🖼️ Main.java                           # 프로그램 진입점. JFrame 초기 화면 호출 (예: LoginView)

│   ├── 📁 common/
│   │   ├── 📁 model/
│   │   │   ├── 🧩 User.java                       # 사용자 정보 객체 (id, password, role, team 등)
│   │   │   ├── 🧩 Room.java                       # 회의실 정보 객체 (id, name, capacity, availability)
│   │   │   ├── 🧩 Meeting.java                    # 회의 정보 객체 (date, timeSlot, room, participants)
│   │   │   └── 🧩 TimeSlot.java                   # 시간대 정보 객체 (id, startTime, endTime)
│   │   └── 🛠️ DBUtil.java                         # DB 연결 및 쿼리 실행 유틸리티 (Connection 반환, Close 처리)

│   ├── 📁 login/
│   │   ├── 🖼️ LoginView.java                      # 로그인 창 구성 (JFrame), ID/비밀번호 입력 필드 및 로그인 버튼
│   │   ├── 🎮 LoginController.java                # 로그인 버튼 클릭 시, AuthService로 로그인 요청 전달
│   │   └── 🔐 AuthService.java                    # DB에서 사용자 인증 수행, 사용자 정보 반환

│   ├── 📁 member/
│   │   ├── 🖼️ MemberView.java                     # 일반 사용자 메뉴 화면 (회의 시간 등록, 알림 설정 등 구성)
│   │   ├── 🎮 MemberController.java               # 버튼 클릭 시 동작 정의 (예: 회의 시간 저장 요청)
│   │   ├── 📝 MeetingPreferenceForm.java          # 회의 날짜 및 시간대 선택 UI 구성 (JPanel 또는 Dialog)
│   │   └── ⏰ AlarmManager.java                   # 회의 일정 알림 기능 로직 (JOptionPane 알림 등)

│   ├── 📁 leader/
│   │   ├── 🖼️ LeaderView.java                     # 팀 리더 메뉴 화면 (회의 생성, 멤버 시간 조회 등)
│   │   ├── 🎮 LeaderController.java               # 리더 기능 이벤트 처리 (회의 생성 요청 등)
│   │   ├── 📝 MeetingCreator.java                 # 회의 목적/장소 등 폼 구성 및 입력 처리 (JPanel 또는 Dialog)
│   │   └── 📊 MemberAvailabilityViewer.java       # 팀원들의 가능 시간대를 표 형태로 보여주는 컴포넌트

│   ├── 📁 admin/
│   │   ├── 🖼️ AdminView.java                      # 관리자 대시보드. 회의실 추가 기능만 남는다면 단일 View로 대체 가능
│   │   ├── 🎮 AdminController.java                # RoomManager 호출만 한다면 직접 호출 방식도 고려
│   │   └── 🏢 RoomManager.java                    # 회의실 CRUD 중 '추가'만 담당. DB에 새로운 회의실 INSERT

│   └── 📁 system/
│       └── ✅ ReservationValidator.java           # 중복 예약, 과거 시간 예약 등 공통 예약 검증 로직 수행

├── ⚙️ .classpath                                  # lib/mysql-connector-java 포함
├── ⚙️ .project                                    # Eclipse용 프로젝트 설정
└── 📄 README.md                                   # 프로젝트 설명
```