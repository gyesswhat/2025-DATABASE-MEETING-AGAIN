package admin;

import common.model.Room;

import java.util.List;

public class AdminController {
    private final RoomManager roomManager = new RoomManager();

    public List<Room> getAllRooms() {
        return roomManager.getAllRooms();
    }

    public boolean registerRoom(String name, int capacity) {
        if (name == null || name.isBlank() || capacity <= 0) return false;

        Room room = new Room(name, capacity);
        roomManager.addRoom(room);
        return true;
    }

    public boolean deleteRoom(int id) {
        return roomManager.deleteRoomById(id);
    }

    public boolean updateRoomName(int id, String newName) {
        return roomManager.updateRoomName(id, newName);
    }

    public boolean updateRoomCapacity(int id, int newCapacity) {
        return roomManager.updateRoomCapacity(id, newCapacity);
    }
}