package admin;

import java.util.List;

public class AdminController {
    private RoomManager roomManager = new RoomManager();

    public void showAllRooms() {
        List<Room> rooms = roomManager.getAllRooms();
        AdminView.displayRoomList(rooms);
    }

    public void registerRoom(String name, int capacity) {
        Room room = new Room(name, capacity);
        roomManager.addRoom(room);
    }
}
