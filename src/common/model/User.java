// 파일 위치: teamPJT/src/common/model/User.java
package common.model;

/**
 * user 테이블 구조에 대응하는 엔티티 클래스
 *
 * DB 테이블: meetingdb.user
 * ---------------------------------------------------------------------------------
 * | 컬럼 이름   | 자료형           | 제약 조건                            |
 * ---------------------------------------------------------------------------------
 * | id          | INT AUTO_INCREMENT | PRIMARY KEY                      |
 * | username    | VARCHAR(50)      | UNIQUE, NOT NULL                   |
 * | password    | VARCHAR(100)     | NOT NULL (SHA-256 해시된 값 저장)   |
 * | role        | VARCHAR(20)      | NOT NULL ("admin", "leader", "member") |
 * | team_id     | INT              | FOREIGN KEY (meetingdb.team.id)     |
 * ---------------------------------------------------------------------------------
 */
public class User {
    private int id;
    private String username;
    private String password; // 이미 해시된 상태로 저장
    private String role;
    private int teamId;      // user.team_id 컬럼

    // 1) 매개변수가 없는 기본 생성자 (AuthService 에서 필요)
    public User() { }

    // 2) 모든 필드를 한 번에 초기화하는 생성자 (필요하다면 사용)
    public User(int id, String username, String password, String role, int teamId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.teamId = teamId;
    }

    // --- Getter / Setter 메서드들 ---
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public int getTeamId() {
        return teamId;
    }
    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
}