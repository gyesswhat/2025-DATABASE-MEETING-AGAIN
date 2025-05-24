package system;

import admin.AdminController;
import admin.AdminView;

public class Main {
    public static void main(String[] args) {
        AdminController controller = new AdminController();

        // 회의실 등록
        String name = AdminView.inputRoomName();
        int capacity = AdminView.inputRoomCapacity();
        controller.registerRoom(name, capacity);

        // 전체 회의실 목록 출력
        controller.showAllRooms();
    }
}