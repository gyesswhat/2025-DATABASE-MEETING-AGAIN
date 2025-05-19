package common.model;

import java.util.List;

public class Meeting {
    private int id;
    private String date;
    private TimeSlot timeSlot;
    private Room room;
    private List<User> participants;

    public Meeting(String date, TimeSlot timeSlot, Room room, List<User> participants) {
        this.date = date;
        this.timeSlot = timeSlot;
        this.room = room;
        this.participants = participants;
    }

    public int getId() { return id; }
    public String getDate() { return date; }
    public TimeSlot getTimeSlot() { return timeSlot; }
    public Room getRoom() { return room; }
    public List<User> getParticipants() { return participants; }

    public void setId(int id) { this.id = id; }
    public void setDate(String date) { this.date = date; }
    public void setTimeSlot(TimeSlot timeSlot) { this.timeSlot = timeSlot; }
    public void setRoom(Room room) { this.room = room; }
    public void setParticipants(List<User> participants) { this.participants = participants; }
}
