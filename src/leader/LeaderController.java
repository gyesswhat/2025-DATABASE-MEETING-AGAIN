package leader;

import common.model.Room;
import common.util.DBUtil;
import common.service.RoomSearchService;
import leader.TeamManagementService.TeamOperationResult;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeaderController {

    private final TeamManagementService teamService;

    public LeaderController() {
        this.teamService = new TeamManagementService();
    }

    // 테이블 존재 여부 확인
    private boolean tableExists(String tableName) {
        try (Connection conn = DBUtil.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet resultSet = meta.getTables(null, null, tableName, new String[]{"TABLE"});
            return resultSet.next();
        } catch (SQLException e) {
            System.err.println("테이블 존재 확인 중 오류: " + e.getMessage());
            return false;
        }
    }

    // 예약 테이블이 없는 경우 생성
    private void createReservationTableIfNotExists() {
        if (!tableExists("db2025_reservation")) {
            String createTableSQL ="CREATE TABLE IF NOT EXISTS db2025_reservation ("
            		+ "id INT AUTO_INCREMENT PRIMARY KEY, "
            		+ "room_id INT NOT NULL, user_id INT NOT NULL, "
            		+ "people_count INT NOT NULL,"
            		+ "reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
            		+ "FOREIGN KEY (room_id) REFERENCES db2025_room(id),"
            		+ "FOREIGN KEY (user_id) REFERENCES db2025_user(id))";

            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(createTableSQL)) {
                stmt.executeUpdate();
                System.out.println("✅ db2025_reservation 테이블이 생성되었습니다.");
            } catch (SQLException e) {
                System.err.println("❌ 예약 테이블 생성 실패: " + e.getMessage());
            }
        }
    }

    public List<Room> getAvailableRooms() {
        List<Room> list = new ArrayList<>();

        createReservationTableIfNotExists();

        String sql;
        if (tableExists("db2025_reservation")) {
            sql = "SELECT * FROM db2025_room WHERE id NOT IN (SELECT room_id FROM db2025_reservation)";
        } else {
            sql = "SELECT * FROM db2025_room";
        }

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Room r = new Room();
                r.setId(rs.getInt("id"));
                r.setName(rs.getString("name"));
                r.setCapacity(rs.getInt("capacity"));
                list.add(r);
            }

        } catch (SQLException e) {
            System.err.println("예약 가능한 회의실 조회 중 오류: " + e.getMessage());

            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM db2025_room");
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Room r = new Room();
                    r.setId(rs.getInt("id"));
                    r.setName(rs.getString("name"));
                    r.setCapacity(rs.getInt("capacity"));
                    list.add(r);
                }
            } catch (SQLException fallbackError) {
                System.err.println("Fallback 조회도 실패: " + fallbackError.getMessage());
            }
        }

        return list;
    }

    public List<Room> getReservedRooms() {
        List<Room> list = new ArrayList<>();

        if (!tableExists("db2025_reservation")) {
            return list;
        }

        String sql = "SELECT r.* FROM db2025_room r JOIN db2025_reservation res ON r.id = res.room_id";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Room r = new Room();
                r.setId(rs.getInt("id"));
                r.setName(rs.getString("name"));
                r.setCapacity(rs.getInt("capacity"));
                list.add(r);
            }

        } catch (SQLException e) {
            System.err.println("예약된 회의실 조회 중 오류: " + e.getMessage());
        }

        return list;
    }

    public boolean reserveRoom(int roomId, int peopleCount, int userId) {
        createReservationTableIfNotExists();

        if (isRoomAlreadyReserved(roomId)) {
            return false;
        }

        String sql = "INSERT INTO db2025_reservation (room_id, user_id, people_count) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            stmt.setInt(2, userId);
            stmt.setInt(3, peopleCount);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("회의실 예약 중 오류: " + e.getMessage());
        }

        return false;
    }

    public boolean cancelReservation(int roomId) {
        if (!tableExists("db2025_reservation")) {
            return false;
        }

        String sql = "DELETE FROM db2025_reservation WHERE room_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("예약 취소 중 오류: " + e.getMessage());
        }

        return false;
    }

    private boolean isRoomAlreadyReserved(int roomId) {
        if (!tableExists("db2025_reservation")) {
            return false;
        }

        String sql = "SELECT COUNT(*) FROM db2025_reservation WHERE room_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("예약 확인 중 오류: " + e.getMessage());
        }

        return false;
    }

    // ================ 팀 관리 기능 ================

    public String getTeamMemberInfo(int teamId) {
        return teamService.findTeamMemberInfo(teamId);
    }

    public TeamOperationResult addTeamMember(String username, int teamId) {
        return teamService.addUserToTeam(username, teamId, teamId);
    }

    public TeamOperationResult removeTeamMember(String username, int teamId) {
        return teamService.removeUserFromTeam(username, teamId);
    }


    public List<Room> searchRooms(String searchKeyword, int minCapacity) {
        List<Room> list = new ArrayList<>();

        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM db2025_room WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        // 검색 키워드가 있으면 이름으로 검색
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sqlBuilder.append(" AND name LIKE ?");
            parameters.add("%" + searchKeyword.trim() + "%");
        }

        // 최소 수용인원 조건
        if (minCapacity > 0) {
            sqlBuilder.append(" AND capacity >= ?");
            parameters.add(minCapacity);
        }

        sqlBuilder.append(" ORDER BY capacity ASC, name ASC");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {

            // 파라미터 설정
            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Room r = new Room();
                r.setId(rs.getInt("id"));
                r.setName(rs.getString("name"));
                r.setCapacity(rs.getInt("capacity"));
                list.add(r);
            }

        } catch (SQLException e) {
            System.err.println("회의실 검색 중 오류: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }
}