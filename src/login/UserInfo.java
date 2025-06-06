package login;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import admin.RoomManager;
import app.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;

import common.model.Room;
import common.model.User;
import common.model.UserView;
import common.util.DBUtil;

public class UserInfo extends UserView {
	private final BaseFrame baseframe;
	final AuthService authService;
	JLabel nameInfo;
	JLabel teamInfo;
	JLabel roleInfo;
	JTextArea membersInfoArea;
	JButton deleteMemberBtn;
	JButton addMemberBtn;
	JScrollPane scrollPane;

	public UserInfo(BaseFrame baseframe) {
		super(baseframe);

		this.baseframe = baseframe;
		authService=new AuthService();
		// 안내 라벨
		infoLabel.setText("사용자 정보");

		// 사용자명 라벨
		nameInfo = new JLabel(user.getUsername());
		nameInfo.setBounds(500, 100, 300, 40);
		TitledBorder nameborder = new TitledBorder("사용자명");
		nameborder.setTitlePosition(TitledBorder.ABOVE_TOP);
		nameborder.setTitleJustification(TitledBorder.LEADING);
		nameInfo.setBorder(nameborder);

		// 소속 팀 라벨
		teamInfo = new JLabel(Integer.toString(user.getTeamId()));
		teamInfo.setBounds(500, 160, 300, 40);
		TitledBorder teamborder = new TitledBorder("소속 팀");
		teamborder.setTitlePosition(TitledBorder.ABOVE_TOP);
		teamborder.setTitleJustification(TitledBorder.LEADING);
		teamInfo.setBorder(teamborder);

		// 역할 라벨
		roleInfo = new JLabel(user.getRole());
		roleInfo.setBounds(500, 220, 300, 40);
		TitledBorder roleborder = new TitledBorder("역할");
		roleborder.setTitlePosition(TitledBorder.ABOVE_TOP);
		roleborder.setTitleJustification(TitledBorder.LEADING);
		roleInfo.setBorder(roleborder);

		// 비밀번호 변경 버튼
		JButton pwChangeBtn = new JButton("비밀번호 변경하기");
		pwChangeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int yes = JOptionPane.showConfirmDialog(null, "비밀번호를 변경하시겠습니까?", "confirm", JOptionPane.YES_NO_OPTION);
				if (yes == 0) {
					String pwNow = JOptionPane.showInputDialog("현재 비밀번호를 입력하세요");
					if (pwNow.equals(user.getPassword())) {
						String pwNew = JOptionPane.showInputDialog("새로운 비밀번호를 입력하세요");
						if (pwNew != null) {
							user.setPassword(pwNew);
							if(authService.updatePassword(user)) JOptionPane.showMessageDialog(null, "비밀번호가 변경되었습니다");
							else JOptionPane.showMessageDialog(null, "비밀번호 변경에 실패했습니다", "Message",
									JOptionPane.WARNING_MESSAGE);
						} else
							JOptionPane.showMessageDialog(null, "비밀번호 변경이 취소되었습니다", "Message",
									JOptionPane.WARNING_MESSAGE);
					} else
						JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다", "Message", JOptionPane.WARNING_MESSAGE);
				} else
					JOptionPane.showMessageDialog(null, "비밀번호 변경이 취소되었습니다", "Message", JOptionPane.WARNING_MESSAGE);
			}
		});
		pwChangeBtn.setBounds(500, 280, 300, 40);

		// 컴포넌트 추가
		userPanel.add(nameInfo);
		userPanel.add(teamInfo);
		userPanel.add(roleInfo);
		userPanel.add(pwChangeBtn);

		// 여기부터는 리더에게만 보이는 컴포넌트
		if (user.getRole().equals("leader"))
			setLeaderOnlyInfo();
	}

	public void setLeaderOnlyInfo() {
		// 팀원 정보 출력 필드
		membersInfoArea = new JTextArea();
		membersInfoArea.setEditable(false);
		TitledBorder membersborder = new TitledBorder("팀원 정보");
		membersborder.setTitlePosition(TitledBorder.ABOVE_TOP);
		membersborder.setTitleJustification(TitledBorder.LEADING);
		membersInfoArea.setBorder(membersborder);
		//membersInfoArea.setBounds(500, 340, 300, 100);
		
		scrollPane=new JScrollPane(membersInfoArea);
		scrollPane.setBounds(500, 340, 300, 100);
		loadTeamMemberInfo(user.getTeamId());

		// 팀원 관리 버튼
		addMemberBtn = new JButton("팀원 추가");
		addMemberBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String addedMember = JOptionPane.showInputDialog("추가할 팀원의 사용자명을 입력하세요");
				/*
				 * 1. 추가할 팀원이 존재하는 member인지 확인하고 2. member의 team을 리더의 team으로 바꾸는 함수가 필요하다
				 */
				if (isUserExists(addedMember)) {
					if(addUser(addedMember, user.getTeamId())) {
						JOptionPane.showMessageDialog(null, "팀원이 추가되었습니다");
						loadTeamMemberInfo(user.getTeamId());
					}
					else JOptionPane.showMessageDialog(null, "팀원 추가에 실패했습니다", "Message", JOptionPane.WARNING_MESSAGE);
					
				} else
					JOptionPane.showMessageDialog(null, "존재하지 않는 사용자입니다", "Message", JOptionPane.WARNING_MESSAGE);
			}
		});
		addMemberBtn.setBounds(500, 445, 300, 20);

		deleteMemberBtn = new JButton("팀원 삭제");
		deleteMemberBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String deletedMember = JOptionPane.showInputDialog("삭제할 팀원의 사용자명을 입력하세요");
				/*
				 * 1. 삭제할 팀원이 존재하는 member인지 확인하고 2. member의 team을 무소속으로 바꾸는 함수가 필요하다
				 */
				if (isUserExists(deletedMember)) {
					if(deleteUser(deletedMember, user.getTeamId())) {
						JOptionPane.showMessageDialog(null, "팀원이 삭제되었습니다.");
						loadTeamMemberInfo(user.getTeamId());
					}
					else JOptionPane.showMessageDialog(null, "팀원 삭제에 실패했습니다", "Message", JOptionPane.WARNING_MESSAGE);
					
				} else
					JOptionPane.showMessageDialog(null, "존재하지 않는 사용자입니다", "Message", JOptionPane.WARNING_MESSAGE);
			}
		});
		deleteMemberBtn.setBounds(500, 470, 300, 20);

		// 컴포넌트 추가
		userPanel.add(scrollPane);
		userPanel.add(addMemberBtn);
		userPanel.add(deleteMemberBtn);
	}

	/*
	 * 1. 같은 팀에 소속된 팀원 정보 확인할 수 있는 sql문 필요 2. 팀원 삭제할 수 있는 sql문 필요 3. 팀원 추가할 수 있는
	 * sql문 필요 (존재하는 유저인지, role이 member인지 확인 필요함)
	 */
	public void loadTeamMemberInfo(int teamid) {
		// 사용자의 정보를 하나의 string으로 만들어서 리턴
		List<User> users = getAllMembers();
		StringBuilder sb = new StringBuilder();
		for (User u : users) {
			sb.append("ID: ").append(u.getId()).append(" | 이름: ").append(u.getUsername()).append(" | 역할: ")
					.append(u.getRole()).append(" | 팀ID: ").append(u.getTeamId()).append("\n");
		}
		membersInfoArea.setText(sb.toString());
	}

	public List<User> getAllMembers() {
		List<User> list = new ArrayList<>();
		String sql = "SELECT * FROM db2025_user WHERE team_id =?;";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);) {
			stmt.setInt(1, user.getTeamId());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				User u = new User();
				u.setId(rs.getInt("id"));
				u.setUsername(rs.getString("username"));
				u.setPassword(rs.getString("password"));
				u.setRole(rs.getString("role"));
				u.setTeamId(rs.getInt("team_id"));
				list.add(u);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public boolean isUserExists(String username) {
		return authService.isUsernameExists(username);
	}

	public boolean addUser(String username, int teamid) {
		String sql="UPDATE db2025_user SET team_id=? WHERE username=?;";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);){
			stmt.setInt(1, teamid);
			stmt.setString(2, username);
			return stmt.executeUpdate()>0;
			
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteUser(String username, int teamid) {
		String sql="UPDATE db2025_user SET team_id='1' WHERE username=?;";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);){
			stmt.setString(1, username);
			return stmt.executeUpdate()>0;
			
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}
}