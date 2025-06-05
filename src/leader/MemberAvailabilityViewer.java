package leader;

import common.model.User;
import common.util.DBUtil;

import java.sql.*;
import java.util.*;

public class MemberAvailabilityViewer {

    public List<User> getAvailableUsers(String date, int timeSlotId) {
        List<User> users = new ArrayList<>();

        String sql = """
            SELECT u.id, u.username, u.role, u.team_id
            FROM user u
            WHERE u.id NOT IN (
                SELECT mp.user_id
                FROM meeting m
                JOIN meeting_participant mp ON m.id = mp.meeting_id
                WHERE m.date = ? AND m.timeslot_id = ?
            )
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, date);
            ps.setInt(2, timeSlotId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setRole(rs.getString("role"));
                    user.setTeamId(rs.getInt("team_id"));
                    users.add(user);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }
}
