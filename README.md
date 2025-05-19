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