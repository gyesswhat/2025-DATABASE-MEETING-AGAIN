package admin;

import java.util.List;
import java.util.Scanner;

public class AdminView {
    private static Scanner sc = new Scanner(System.in);

    public static void displayRoomList(List<Room> rooms) {
        System.out.println("\n📋 회의실 목록:");
        for (Room r : rooms) {
            System.out.println(r.toString());
        }
    }

    public static String inputRoomName() {
        System.out.print("회의실 이름 입력: ");
        return sc.nextLine();
    }

    public static int inputRoomCapacity() {
        System.out.print("수용 인원 입력: ");
        return Integer.parseInt(sc.nextLine());
    }
}
