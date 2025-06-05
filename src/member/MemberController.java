package member;

import common.model.User;
import login.AuthService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MemberController {

    private final TimePreferenceManager preferenceManager = new TimePreferenceManager();

    /**
     * 회의 가능 시간 등록 기능
     */
    public void registerTimePreference() {
        Scanner scanner = new Scanner(System.in);

        User user = AuthService.getCurrentUser();
        if (user == null) {
            System.out.println("로그인된 사용자가 없습니다.");
            return;
        }

        int userId = user.getId();
        int teamId = Integer.parseInt(user.getTeam()); // team이 int인 경우

        System.out.println("\n🗓 회의 가능 시간 등록");
        System.out.print("회의 날짜 입력 (예: 2025-06-10): ");
        String date = scanner.nextLine().trim(); // "2025-06-10"

        List<TimePreference> preferences = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            System.out.print(i + "순위 시작 시간 입력 (예: 09:00): ");
            String start = scanner.nextLine().trim();
            System.out.print(i + "순위 종료 시간 입력 (예: 10:00): ");
            String end = scanner.nextLine().trim();

            // LocalDateTime으로 변환
            LocalDateTime startTime = LocalDateTime.parse(date + "T" + start);
            LocalDateTime endTime = LocalDateTime.parse(date + "T" + end);

            preferences.add(new TimePreference(userId, teamId, startTime, endTime, i));
        }

        boolean allSaved = true;
        for (TimePreference tp : preferences) {
            boolean saved = preferenceManager.save(tp); // 하나씩 저장
            if (!saved) {
                allSaved = false;
            }
        }

        if (allSaved) {
            System.out.println("회의 가능 시간이 성공적으로 등록되었습니다.");
        } else {
            System.out.println("일부 시간 등록 중 오류가 발생했습니다.");
        }
    }


    /**
     * (선택 기능) 사용자가 등록한 시간 목록 전체 출력
     */
    public void showMyTimePreferences() {
        User user = AuthService.getCurrentUser();
        if (user == null) {
            System.out.println("로그인된 사용자가 없습니다.");
            return;
        }

        List<TimePreference> preferences = preferenceManager.getAllByUser(user.getId());

        if (preferences.isEmpty()) {
            System.out.println("등록된 회의 가능 시간이 없습니다.");
        } else {
            System.out.println("\n등록된 회의 가능 시간 목록:");
            for (TimePreference tp : preferences) {
                System.out.println(tp);
            }
        }
    }
}