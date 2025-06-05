package member;

import java.time.LocalDateTime;

public class TimePreference {
    private int id;
    private int userId;
    private int teamId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int priority;

    public TimePreference() {}

    public TimePreference(int userId, int teamId, LocalDateTime startTime, LocalDateTime endTime, int priority) {
        this.userId = userId;
        this.teamId = teamId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priority = priority;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    @Override
    public String toString() {
        return "TimePreference{" +
                "userId=" + userId +
                ", teamId=" + teamId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", priority=" + priority +
                '}';
    }
}