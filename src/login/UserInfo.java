package login;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import app.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import common.model.User;
import common.model.UserView;

public class UserInfo extends UserView {
	private final BaseFrame baseframe;
	JLabel nameInfo;
	JLabel teamInfo;
	JLabel roleInfo;
	JTextArea membersInfoArea;
	JButton deleteMemberBtn;
	JButton addMemberBtn;

	public UserInfo(BaseFrame baseframe) {
		super(baseframe);
		this.baseframe=baseframe;

		try {
			// user 객체가 정상적으로 초기화되었는지 확인
			if (user == null) {
				throw new IllegalStateException("사용자 정보가 없습니다.");
			}

			//안내 라벨
			infoLabel.setText("사용자 정보");

			//사용자명 라벨
			nameInfo = new JLabel(user.getUsername() != null ? user.getUsername() : "Unknown");
			nameInfo.setBounds(500, 100, 300, 40);
			TitledBorder nameborder = new TitledBorder("사용자명");
			nameborder.setTitlePosition(TitledBorder.ABOVE_TOP);
			nameborder.setTitleJustification(TitledBorder.LEADING);
			nameInfo.setBorder(nameborder);

			//소속 팀 라벨
			teamInfo = new JLabel(String.valueOf(user.getTeamId()));
			teamInfo.setBounds(500, 160, 300, 40);
			TitledBorder teamborder = new TitledBorder("소속 팀 ID");
			teamborder.setTitlePosition(TitledBorder.ABOVE_TOP);
			teamborder.setTitleJustification(TitledBorder.LEADING);
			teamInfo.setBorder(teamborder);

			//역할 라벨
			roleInfo = new JLabel(user.getRole() != null ? user.getRole() : "Unknown");
			roleInfo.setBounds(500, 220, 300, 40);
			TitledBorder roleborder = new TitledBorder("역할");
			roleborder.setTitlePosition(TitledBorder.ABOVE_TOP);
			roleborder.setTitleJustification(TitledBorder.LEADING);
			roleInfo.setBorder(roleborder);

			//비밀번호 변경 버튼
			JButton pwChangeBtn=new JButton("비밀번호 변경하기");
			pwChangeBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					handlePasswordChange();
				}
			});
			pwChangeBtn.setBounds(500, 280, 300, 40);

			//컴포넌트 추가
			userPanel.add(nameInfo);
			userPanel.add(teamInfo);
			userPanel.add(roleInfo);
			userPanel.add(pwChangeBtn);

			//여기부터는 리더에게만 보이는 컴포넌트
			if(user.getRole() != null && user.getRole().equals("leader")) {
				setLeaderOnlyInfo();
			}

		} catch (Exception e) {
			System.err.println("UserInfo 초기화 중 오류: " + e.getMessage());
			e.printStackTrace();

			// 오류 발생 시 기본 정보 표시
			JLabel errorLabel = new JLabel("사용자 정보를 불러올 수 없습니다.");
			errorLabel.setBounds(500, 100, 300, 40);
			errorLabel.setForeground(Color.RED);
			userPanel.add(errorLabel);

			JButton backBtn = new JButton("로그인 화면으로");
			backBtn.setBounds(500, 150, 150, 30);
			backBtn.addActionListener(e2 -> {
				baseframe.change(baseframe.panel, baseframe.lgv);
			});
			userPanel.add(backBtn);
		}
	}

	private void handlePasswordChange() {
		try {
			int confirmation = JOptionPane.showConfirmDialog(this,
					"비밀번호를 변경하시겠습니까?",
					"비밀번호 변경", JOptionPane.YES_NO_OPTION);

			if (confirmation != JOptionPane.YES_OPTION) {
				JOptionPane.showMessageDialog(this, "비밀번호 변경이 취소되었습니다.",
						"취소", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			String currentPassword = JOptionPane.showInputDialog(this,
					"현재 비밀번호를 입력하세요:", "현재 비밀번호", JOptionPane.QUESTION_MESSAGE);

			if (currentPassword == null || currentPassword.trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "현재 비밀번호를 입력해야 합니다.",
						"입력 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}

			// 현재 비밀번호 확인
			String userPassword = user.getPassword();
			if (userPassword == null || !userPassword.equals(currentPassword.trim())) {
				JOptionPane.showMessageDialog(this, "현재 비밀번호가 일치하지 않습니다.",
						"인증 실패", JOptionPane.ERROR_MESSAGE);
				return;
			}

			String newPassword = JOptionPane.showInputDialog(this,
					"새로운 비밀번호를 입력하세요:", "새 비밀번호", JOptionPane.QUESTION_MESSAGE);

			if (newPassword == null || newPassword.trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "비밀번호 변경이 취소되었습니다.",
						"취소", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			if (newPassword.trim().length() < 3) {
				JOptionPane.showMessageDialog(this, "비밀번호는 최소 3자 이상이어야 합니다.",
						"입력 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}

			// 비밀번호 업데이트 (실제 DB 업데이트는 추후 구현)
			user.setPassword(newPassword.trim());
			JOptionPane.showMessageDialog(this, "비밀번호가 변경되었습니다.\n(주의: DB에는 아직 반영되지 않았습니다)");

		} catch (Exception e) {
			System.err.println("비밀번호 변경 중 오류: " + e.getMessage());
			JOptionPane.showMessageDialog(this,
					"비밀번호 변경 중 오류가 발생했습니다: " + e.getMessage(),
					"오류", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void setLeaderOnlyInfo() {
		try {
			//팀원 정보 출력 필드
			membersInfoArea = new JTextArea();
			membersInfoArea.setEditable(false);
			TitledBorder membersborder = new TitledBorder("팀원 정보");
			membersborder.setTitlePosition(TitledBorder.ABOVE_TOP);
			membersborder.setTitleJustification(TitledBorder.LEADING);
			membersInfoArea.setBorder(membersborder);
			membersInfoArea.setBounds(500, 340, 300, 100);

			//JTextArea에 정보 추가 (임시 데이터)
			String memberInfo = findTeamMemberInfo(user.getTeamId());
			if (memberInfo != null && !memberInfo.trim().isEmpty()) {
				membersInfoArea.append(memberInfo);
			} else {
				membersInfoArea.append("팀원 정보를 불러오는 중...");
			}

			//팀원 관리 버튼
			addMemberBtn = new JButton("팀원 추가");
			addMemberBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					handleAddMember();
				}
			});
			addMemberBtn.setBounds(500, 445, 300, 20);

			deleteMemberBtn = new JButton("팀원 삭제");
			deleteMemberBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					handleDeleteMember();
				}
			});
			deleteMemberBtn.setBounds(500, 470, 300, 20);

			//컴포넌트 추가
			userPanel.add(membersInfoArea);
			userPanel.add(addMemberBtn);
			userPanel.add(deleteMemberBtn);

		} catch (Exception e) {
			System.err.println("리더 전용 정보 설정 중 오류: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void handleAddMember() {
		try {
			String memberName = JOptionPane.showInputDialog(this,
					"추가할 팀원의 사용자명을 입력하세요:",
					"팀원 추가", JOptionPane.QUESTION_MESSAGE);

			if (memberName == null || memberName.trim().isEmpty()) {
				return;
			}

			if (isUserExists(memberName.trim())) {
				addUser(memberName.trim(), user.getTeamId());
				JOptionPane.showMessageDialog(this, "팀원이 추가되었습니다.");
				// UI 새로고침은 추후 구현
			} else {
				JOptionPane.showMessageDialog(this, "존재하지 않는 사용자입니다",
						"사용자 없음", JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception e) {
			System.err.println("팀원 추가 중 오류: " + e.getMessage());
			JOptionPane.showMessageDialog(this, "팀원 추가 중 오류가 발생했습니다.",
					"오류", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void handleDeleteMember() {
		try {
			String memberName = JOptionPane.showInputDialog(this,
					"삭제할 팀원의 사용자명을 입력하세요:",
					"팀원 삭제", JOptionPane.QUESTION_MESSAGE);

			if (memberName == null || memberName.trim().isEmpty()) {
				return;
			}

			if (isUserExists(memberName.trim())) {
				deleteuser(memberName.trim(), user.getTeamId());
				JOptionPane.showMessageDialog(this, "팀원이 삭제되었습니다.");
				// UI 새로고침은 추후 구현
			} else {
				JOptionPane.showMessageDialog(this, "존재하지 않는 사용자입니다",
						"사용자 없음", JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception e) {
			System.err.println("팀원 삭제 중 오류: " + e.getMessage());
			JOptionPane.showMessageDialog(this, "팀원 삭제 중 오류가 발생했습니다.",
					"오류", JOptionPane.ERROR_MESSAGE);
		}
	}

	/* TODO: 실제 DB 연동 메서드들 - 추후 구현 필요
	 * 1. 같은 팀에 소속된 팀원 정보 확인할 수 있는 sql문 필요
	 * 2. 팀원 삭제할 수 있는 sql문 필요
	 * 3. 팀원 추가할 수 있는 sql문 필요 (존재하는 유저인지, role이 member인지 확인 필요함)
	 */
	public String findTeamMemberInfo(int teamid) {
		//사용자의 정보를 하나의 string으로 만들어서 리턴
		return "팀원 정보 기능은 아직 구현되지 않았습니다.";
	}

	public boolean isUserExists(String username) {
		// TODO: 실제 DB 조회 구현
		return true; // 임시로 true 반환
	}

	public void addUser(String username, int teamid) {
		// TODO: 실제 DB 업데이트 구현
		System.out.println("팀원 추가: " + username + " to team " + teamid);
	}

	public void deleteuser(String username, int teamid) {
		// TODO: 실제 DB 업데이트 구현
		System.out.println("팀원 삭제: " + username + " from team " + teamid);
	}
}