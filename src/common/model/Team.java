// 파일 위치: teamPJT/src/common/model/Team.java
package common.model;

/**
 * team 테이블 구조에 대응하는 엔티티 클래스
 *
 * DB 테이블: db025_team
 * ----------------------------
 * | 컬럼 이름 | 자료형          |
 * ----------------------------
 * | id        | INT AUTO_INCREMENT (PK) |
 * | name      | VARCHAR(50)            |
 * ----------------------------
 */
public class Team {
    private int id;
    private String name;

    public Team() { }

    public Team(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // --- Getter / Setter ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}