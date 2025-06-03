package member;

import java.util.Scanner;

public class MemberView {
    private MemberController controller;

    public MemberView() {
        this.controller = new MemberController();
    }

    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n[Member 기능 선택]");
            System.out.println("1. 회의 가능 시간 등록");
            System.out.println("0. 뒤로가기 / 종료");
            System.out.print("선택: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // 개행 제거

            switch (choice) {
                case 1:
                    controller.registerTimePreference();
                    break;
                case 0:
                    System.out.println("돌아갑니다.");
                    break;
                default:
                    System.out.println("올바른 번호를 선택해주세요.");
            }

        } while (choice != 0);
    }
}
