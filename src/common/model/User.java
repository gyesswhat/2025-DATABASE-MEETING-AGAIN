package common.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String role;
    private String team;

    // Constructor, Getter, Setter
    public User(int id, String username, String password, String role, String team) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.team = team;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getTeam() { return team; }

    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
    public void setTeam(String team) { this.team = team; }
}
