package leader;

import java.util.List;
import common.model.User;
import common.model.Room;
import common.model.Meeting;

public class LeaderController {
    private final MeetingCreator meetingCreator;
    private final MemberAvailabilityViewer availabilityViewer;

    public LeaderController() {
        this.meetingCreator = new MeetingCreator();
        this.availabilityViewer = new MemberAvailabilityViewer();
    }

    public boolean reserveMeeting(Meeting meeting) {
        return meetingCreator.createMeeting(meeting);
    }

    public boolean cancelMeeting(int meetingId) {
        return meetingCreator.deleteMeeting(meetingId);
    }

    public List<Room> getAvailableRooms(String date, int timeslotId) {
        return meetingCreator.findAvailableRooms(date, timeslotId);
    }

    public List<Meeting> getReservedMeetingsByTeam(int teamId) {
        return meetingCreator.findMeetingsByTeam(teamId);
    }

    public List<User> getAvailableMembers(String date, int timeSlotId) {
        return availabilityViewer.getAvailableUsers(date, timeSlotId);
    }
}