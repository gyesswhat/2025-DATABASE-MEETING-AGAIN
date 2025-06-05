import admin.AdminController;
import admin.AdminView;
import app.BaseFrame;
import member.MemberView;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n [메인 메뉴]");
            System.out.println("1. 관리자(Admin) 기능 실행");
            System.out.println("2. 회원(Member) 기능 실행");
            System.out.println("0. 프로그램 종료");
            System.out.print("선택: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // 개행 제거

            switch (choice) {
                case 1:
                    runAdminFeature();
                    break;
                case 2:
                    runMemberFeature();
                    break;
                case 0:
                    System.out.println("👋 프로그램을 종료합니다.");
                    break;
                default:
                    System.out.println("❗ 잘못된 입력입니다. 다시 선택해주세요.");
            }
        } while (choice != 0);
    }

    private static void runAdminFeature() {
        AdminController controller = new AdminController();

        String name = AdminView.inputRoomName();
        int capacity = AdminView.inputRoomCapacity();
        controller.registerRoom(name, capacity);

        controller.showAllRooms();
    }

    private static void runMemberFeature() {
        MemberView view = new MemberView(new BaseFrame());
        view.displayMenu();
    }
}
