package common.service;

import common.model.Room;
import common.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 회의실 검색을 위한 공통 서비스
 * 모든 사용자가 사용할 수 있는 회의실 조회 기능 제공
 */
public class RoomSearchService {

    /**
     * 모든 회의실 조회
     */
    public List<Room> getAllRooms() {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT * FROM db2025_room ORDER BY capacity ASC, name ASC";

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
            System.err.println("회의실 조회 중 오류: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 조건에 따른 회의실 검색
     * @param searchKeyword 회의실 이름 검색어 (null 또는 빈 문자열이면 무시)
     * @param minCapacity 최소 수용인원 (0 이하면 무시)
     * @return 검색 조건에 맞는 회의실 목록
     */
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

    /**
     * 예약 가능한 회의실 조회 (예약되지 않은 회의실만)
     */
    public List<Room> getAvailableRooms() {
        List<Room> list = new ArrayList<>();

        // 예약 테이블 존재 여부 확인
        if (!tableExists("db2025_reservation")) {
            // 예약 테이블이 없으면 모든 회의실이 예약 가능
            return getAllRooms();
        }

        String sql = "SELECT * FROM db2025_room WHERE id NOT IN (SELECT room_id FROM db2025_reservation) ORDER BY capacity ASC, name ASC";

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
            // 오류 발생 시 모든 회의실 반환
            return getAllRooms();
        }

        return list;
    }

    /**
     * 예약된 회의실 조회
     */
    public List<Room> getReservedRooms() {
        List<Room> list = new ArrayList<>();

        if (!tableExists("db2025_reservation")) {
            return list; // 빈 리스트 반환
        }

        String sql = "SELECT r.* FROM db2025_room r JOIN db2025_reservation res ON r.id = res.room_id ORDER BY r.capacity ASC, r.name ASC";

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

    /**
     * 특정 ID의 회의실 조회
     */
    public Room getRoomById(int roomId) {
        String sql = "SELECT * FROM db2025_room WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Room r = new Room();
                r.setId(rs.getInt("id"));
                r.setName(rs.getString("name"));
                r.setCapacity(rs.getInt("capacity"));
                return r;
            }

        } catch (SQLException e) {
            System.err.println("회의실 조회 중 오류: " + e.getMessage());
        }

        return null;
    }

    /**
     * 테이블 존재 여부 확인
     */
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
}