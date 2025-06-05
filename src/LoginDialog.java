import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginDialog extends JDialog{
		
		public LoginDialog(JFrame owner) {
			super(owner);
			
			getContentPane().setSize(415, 250);
			
			JButton loginBtn=new JButton("로그인");
			loginBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(null, "로그인 되었습니다");
				}
			});
			loginBtn.setBounds(56, 155, 90, 23);
			JButton cancelBtn=new JButton("취소");
			cancelBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			cancelBtn.setBounds(154, 155, 90, 23);
			JButton signupBtn=new JButton("회원가입");
			signupBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			signupBtn.setBounds(252, 155, 90, 23);
			JPanel p=new JPanel();
			p.setLayout(null);
			
			JLabel loginInfo=new JLabel("로그인 하시겠습니까?");
			loginInfo.setHorizontalAlignment(SwingConstants.CENTER);
			loginInfo.setBounds(56, 10, 286, 15);
			p.add(loginInfo);
			
			JTextField idtxt=new JTextField();
			idtxt.setBounds(56, 35, 286, 38);
			TitledBorder idBorder=new TitledBorder("ID");
			idBorder.setTitlePosition(TitledBorder.ABOVE_TOP);
			idBorder.setTitleJustification(TitledBorder.LEADING);
			idtxt.setBorder(idBorder);
			
			JTextField pwtxt=new JTextField();
			pwtxt.setBounds(56, 83, 286, 38);
			TitledBorder pwBorder=new TitledBorder("pw");
			pwBorder.setTitlePosition(TitledBorder.ABOVE_TOP);
			pwBorder.setTitleJustification(TitledBorder.LEADING);
			pwtxt.setBorder(pwBorder);
			
			p.add(idtxt);
			p.add(pwtxt);
			
			p.add(loginBtn);
			p.add(cancelBtn);
			p.add(signupBtn);
			
			getContentPane().add(p);
			setLocation(100, 100);
			setVisible(true);
		}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new LoginDialog(new JFrame());
	}

}
