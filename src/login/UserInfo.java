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
		
		//안내 라벨
		infoLabel.setText("사용자 정보");
		
		//사용자명 라벨
		nameInfo = new JLabel(user.getUsername());
		nameInfo.setBounds(500, 100, 300, 40);
		TitledBorder nameborder = new TitledBorder("사용자명");
		nameborder.setTitlePosition(TitledBorder.ABOVE_TOP);
		nameborder.setTitleJustification(TitledBorder.LEADING);
		nameInfo.setBorder(nameborder);
		
		//소속 팀 라벨
		teamInfo = new JLabel(Integer.toString(user.getTeamId()));
		teamInfo.setBounds(500, 160, 300, 40);
		TitledBorder teamborder = new TitledBorder("소속 팀");
		teamborder.setTitlePosition(TitledBorder.ABOVE_TOP);
		teamborder.setTitleJustification(TitledBorder.LEADING);
		teamInfo.setBorder(teamborder);
		
		//역할 라벨
		roleInfo = new JLabel(user.getRole());
		roleInfo.setBounds(500, 220, 300, 40);
		TitledBorder roleborder = new TitledBorder("역할");
		roleborder.setTitlePosition(TitledBorder.ABOVE_TOP);
		roleborder.setTitleJustification(TitledBorder.LEADING);
		roleInfo.setBorder(roleborder);
		
		//비밀번호 변경 버튼
		JButton pwChangeBtn=new JButton("비밀번호 변경하기");
		pwChangeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int yes=JOptionPane.showConfirmDialog(null,"비밀번호를 변경하시겠습니까?", "confirm",JOptionPane.YES_NO_OPTION);
				if(yes==0) {
					String pwNow=JOptionPane.showInputDialog("현재 비밀번호를 입력하세요");
					if(pwNow.equals(user.getPassword())) {
						String pwNew=JOptionPane.showInputDialog("새로운 비밀번호를 입력하세요");
						if (pwNew!=null) {
							user.setPassword(pwNew);
							/*db까지 변경하는 코드 필요함
							 * 
							 * 
							 * 
							 * 
							 * 
							 * */
							JOptionPane.showMessageDialog(null, "비밀번호가 변경되었습니다");
						}
						else JOptionPane.showMessageDialog(null, "비밀번호 변경이 취소되었습니다", "Message", JOptionPane.WARNING_MESSAGE);
					}
					else JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다", "Message", JOptionPane.WARNING_MESSAGE);
				}
				else JOptionPane.showMessageDialog(null, "비밀번호 변경이 취소되었습니다", "Message", JOptionPane.WARNING_MESSAGE);
			}
		});
		pwChangeBtn.setBounds(500, 280, 300, 40);
		
		//컴포넌트 추가
		userPanel.add(nameInfo);
		userPanel.add(teamInfo);
		userPanel.add(roleInfo);
		userPanel.add(pwChangeBtn);
		
		//여기부터는 리더에게만 보이는 컴포넌트
		if(user.getRole().equals("leader")) setLeaderOnlyInfo();
	}
	public void setLeaderOnlyInfo() {
		//팀원 정보 출력 필드
		membersInfoArea=new JTextArea();
		membersInfoArea.setEditable(false);
		membersInfoArea.setEditable(false);
		TitledBorder membersborder = new TitledBorder("팀원 정보");
		membersborder.setTitlePosition(TitledBorder.ABOVE_TOP);
		membersborder.setTitleJustification(TitledBorder.LEADING);
		membersInfoArea.setBorder(membersborder);
		membersInfoArea.setBounds(500, 340, 300, 100);
		//JTextArea에 정보 추가
		membersInfoArea.append(findTeamMemberInfo(user.getTeamId()));
		
		//팀원 관리 버튼
		addMemberBtn=new JButton("팀원 추가");
		addMemberBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String addedMember=JOptionPane.showInputDialog("추가할 팀원의 사용자명을 입력하세요");
				/*1. 추가할 팀원이 존재하는 member인지 확인하고
				 *2. member의 team을 리더의 team으로 바꾸는 함수가 필요하다
				 * */
				if(isUserExists(addedMember)) {
					addUser(addedMember, user.getTeamId());
					JOptionPane.showMessageDialog(null, "팀원이 추가되었습니다.");
				}
				else JOptionPane.showMessageDialog(null, "존재하지 않는 사용자입니다", "Message", JOptionPane.WARNING_MESSAGE);
			}
		});
		addMemberBtn.setBounds(500,445, 300, 20);
		
		deleteMemberBtn=new JButton("팀원 삭제");
		deleteMemberBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String deletedMember=JOptionPane.showInputDialog("삭제할 팀원의 사용자명을 입력하세요");
				/*1. 삭제할 팀원이 존재하는 member인지 확인하고
				 *2. member의 team을 무소속으로 바꾸는 함수가 필요하다
				 * */
				if(isUserExists(deletedMember)) {
					addUser(deletedMember, user.getTeamId());
					JOptionPane.showMessageDialog(null, "팀원이 삭제되었습니다.");
				}
				else JOptionPane.showMessageDialog(null, "존재하지 않는 사용자입니다", "Message", JOptionPane.WARNING_MESSAGE);
			}
		});
		deleteMemberBtn.setBounds(500, 470, 300, 20);
		
		//컴포넌트 추가
		userPanel.add(membersInfoArea);
		userPanel.add(addMemberBtn);
		userPanel.add(deleteMemberBtn);
	}
	
	/*1. 같은 팀에 소속된 팀원 정보 확인할 수 있는 sql문 필요
	 *2. 팀원 삭제할 수 있는 sql문 필요
	 *3. 팀원 추가할 수 있는 sql문 필요 (존재하는 유저인지, role이 member인지 확인 필요함) 
	 * */
	public String findTeamMemberInfo(int teamid) {
		//사용자의 정보를 하나의 string으로 만들어서 리턴
		return null;
	}
	public boolean isUserExists(String username) {
		return true;
	}
	public void addUser(String username, int teamid) {
		
	}
	public void deleteuser(String username, int teamid) {
		
	}
}
