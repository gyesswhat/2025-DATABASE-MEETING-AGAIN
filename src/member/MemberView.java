package member;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.JOptionPane;

import app.*;

public class MemberView extends UserView {
	String date, time;
	JPanel addTimePanel = new JPanel();
	
    public MemberView(BaseFrame baseframe){
    	super(baseframe);
    	setLayout(null);
    	
    	infoLabel.setText("회의 가능 시간 등록");
    	
    	JButton logoutBtn = new JButton("로그아웃");
    	
    	JLabel addTimeLabel = new JLabel("가능 시간 등록");
    	addTimeLabel.setFont(new Font("굴림", Font.PLAIN, 30));
    	addTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
    	addTimeLabel.setBounds(470, 135, 396, 47);
    	userPanel.add(addTimeLabel);
    	
    	JButton dateChoiceBtn = new JButton("날짜 선택");
    	dateChoiceBtn.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			date=JOptionPane.showInputDialog("2000-01-01 형식으로 날짜를 입력하세요",null);
    			addTime(new JLabel(), date);
    		}
    	});
    	dateChoiceBtn.setBounds(470, 192, 192, 23);
    	userPanel.add(dateChoiceBtn);
    	
    	JButton timeChoiceBtn = new JButton("시간 선택");
    	timeChoiceBtn.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if(date==null) {
    				JOptionPane.showMessageDialog(null, "날짜를 먼저 입력하세요","", JOptionPane.ERROR_MESSAGE);
    			}
    			else {
        			time=JOptionPane.showInputDialog("00:00-23:59 형식으로 가능한 시간을 입력하세요", null);
        			addTime(new JLabel(), time);
        			time=null;
        			date=null;
    			}
    		}
    	});
    	timeChoiceBtn.setBounds(674, 192, 192, 23);
    	userPanel.add(timeChoiceBtn);
  
    	addTimePanel.setBorder(new LineBorder(new Color(0, 0, 0), 5));
    	addTimePanel.setBounds(470, 225, 396, 267);
    	userPanel.add(addTimePanel);
    	addTimePanel.setLayout(new GridLayout(0, 2, 0, 4));
    }
    public void addTime(JLabel label, String time) {
    	label.setText(time);
    	addTimePanel.add(label);
    	addTimePanel.revalidate();
    }
}