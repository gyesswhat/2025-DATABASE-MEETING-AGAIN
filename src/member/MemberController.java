package member;

import common.model.User;
import java.time.LocalDateTime;
import java.util.List;

public class MemberController {
    private TimePreferenceManager preferenceManager = new TimePreferenceManager();

    public boolean saveTimePreference(User user, String date, String startStr, String endStr, int priority) {
        try {
            LocalDateTime start = LocalDateTime.parse(date + "T" + startStr);
            LocalDateTime end = LocalDateTime.parse(date + "T" + endStr);

            TimePreference tp = new TimePreference(user.getId(), user.getTeamId(), start, end, priority);
            return preferenceManager.save(tp);

        } catch (Exception e) {
            System.err.println("[MemberController] 시간 등록 오류: " + e.getMessage());
            return false;
        }
    }

	public List<TimePreference> getAllByUser(int userId) {
		// TODO Auto-generated method stub
		return preferenceManager.getAllByUser(userId);
	}
}
