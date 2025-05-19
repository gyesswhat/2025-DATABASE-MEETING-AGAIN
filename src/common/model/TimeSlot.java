package common.model;

public class TimeSlot {
    private int id;
    private String startTime;
    private String endTime;

    public TimeSlot(int id, String startTime, String endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() { return id; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }

    public void setId(int id) { this.id = id; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
}
