package leader;

import common.model.*;
import common.util.DBUtil;

import java.sql.*;
import java.util.*;

public class MeetingCreator {

    public boolean createMeeting(Meeting meeting) {
        String insertMeeting = "INSERT INTO meeting (date, timeslot_id, room_id) VALUES (?, ?, ?)";
        String insertParticipants = "INSERT INTO meeting_participant (meeting_id, user_id) VALUES (?, ?)";

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);

            // 1. 회의 생성
            try (PreparedStatement ps = conn.prepareStatement(insertMeeting, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, meeting.getDate());
                ps.setInt(2, meeting.getTimeSlot().getId());
                ps.setInt(3, meeting.getRoom().getId());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int meetingId = rs.getInt(1);

                        // 2. 참가자 저장
                        try (PreparedStatement partStmt = conn.prepareStatement(insertParticipants)) {
                            for (User u : meeting.getParticipants()) {
                                partStmt.setInt(1, meetingId);
                                partStmt.setInt(2, u.getId());
                                partStmt.addBatch();
                            }
                            partStmt.executeBatch();
                        }

                        conn.commit();
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteMeeting(int meetingId) {
        String deleteParticipants = "DELETE FROM meeting_participant WHERE meeting_id = ?";
        String deleteMeeting = "DELETE FROM meeting WHERE id = ?";

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(deleteParticipants);
                 PreparedStatement ps2 = conn.prepareStatement(deleteMeeting)) {

                ps1.setInt(1, meetingId);
                ps1.executeUpdate();

                ps2.setInt(1, meetingId);
                ps2.executeUpdate();

                conn.commit();
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Room> findAvailableRooms(String date, int timeSlotId) {
        List<Room> availableRooms = new ArrayList<>();
        String sql = """
            SELECT r.id, r.name, r.capacity
            FROM room r
            WHERE r.id NOT IN (
                SELECT room_id FROM meeting
                WHERE date = ? AND timeslot_id = ?
            )
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, date);
            ps.setInt(2, timeSlotId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Room room = new Room(rs.getInt("id"), rs.getString("name"), rs.getInt("capacity"));
                    availableRooms.add(room);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availableRooms;
    }

    public List<Meeting> findMeetingsByTeam(int teamId) {
        List<Meeting> meetings = new ArrayList<>();

        String sql = """
            SELECT m.id, m.date, m.timeslot_id, m.room_id, r.name as room_name, r.capacity
            FROM meeting m
            JOIN room r ON m.room_id = r.id
            JOIN meeting_participant mp ON mp.meeting_id = m.id
            JOIN user u ON mp.user_id = u.id
            WHERE u.team_id = ?
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, teamId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TimeSlot slot = new TimeSlot(rs.getInt("timeslot_id"), "", ""); // 시간 문자열은 생략
                    Room room = new Room(rs.getInt("room_id"), rs.getString("room_name"), rs.getInt("capacity"));
                    Meeting meeting = new Meeting(rs.getString("date"), slot, room, new ArrayList<>());
                    meeting.setId(rs.getInt("id"));
                    meetings.add(meeting);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return meetings;
    }
}