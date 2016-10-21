package display;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import system.AccountInformation;
import system.Controller;

public class ChangePassword extends KeyAdapter implements ActionListener{
	private system.Controller controller;

	private JFrame accountFrame;
	private JPanel changePanel;
	private JLabel changeLabel;
	private JLabel IDLabel;
	private JLabel ID_Mine;
	private JLabel usrLabel;
	private JLabel usr_Mine;
	private JLabel newPassLabel;
	private JTextField newTextField;
	private JButton changeButton;

	private String oldID;
	private String oldName;

	public ChangePassword(system.Controller controller){
		// システム引き継ぎ
		this.controller = controller;

		accountFrame = new JFrame("アカウント情報の変更");
		accountFrame.setBounds(650, 300, 600, 500);
		changePanel = new JPanel();
		changePanel.setLayout(null);
		changeLabel = new JLabel("アカウント情報の変更");
		changeLabel.setFont(new Font(null, Font.PLAIN, 18));
		changeLabel.setBounds(200,10,200,40);
		//IDの表示
		IDLabel = new JLabel("ID");
		IDLabel.setBounds(170,80,200,40);
		IDLabel.setFont(new Font(null, Font.PLAIN, 18));
		ID_Mine = new JLabel("ID_Null");
		ID_Mine.setBounds(170,120,200,40);
		ID_Mine.setFont(new Font(null, Font.PLAIN, 24));
		//ユーザ名の表示
		usrLabel = new JLabel("ユーザ名");
		usrLabel.setBounds(300,80,200,40);
		usrLabel.setFont(new Font(null, Font.PLAIN, 18));
		usr_Mine = new JLabel("Name_Null");
		usr_Mine.setBounds(300,120,200,40);
		usr_Mine.setFont(new Font(null, Font.PLAIN, 24));
		//pwの表示
		newPassLabel = new JLabel("新しいパスワード");
		newPassLabel.setBounds(30,200,200,40);
		newPassLabel.setFont(new Font(null, Font.PLAIN, 18));
		newTextField = new JTextField();
		newTextField.setColumns(100);
		newTextField.setBounds(200,200,200,40);
		//変更
		changeButton = new JButton("変更");
		changeButton.setBounds(200,250,200,40);
		changeButton.setFont(new Font(null, Font.PLAIN, 18));
		changeButton.setBackground(Color.WHITE);

		changePanel.add(changeLabel);
		changePanel.add(IDLabel);
		changePanel.add(usrLabel);
		changePanel.add(ID_Mine);
		changePanel.add(usr_Mine);
		changePanel.add(newPassLabel);
		changePanel.add(newTextField);
		changePanel.add(changeButton);
		accountFrame.getContentPane().add(changePanel);

		accountFrame.setVisible(false);

		/*イベントの追加*/
		changeButton.addActionListener(this);
		changeButton.addKeyListener(this);

		/*アイコンの設定*/
		ImageIcon icon = new ImageIcon("src/icon/icon.png");
		accountFrame.setIconImage(icon.getImage());
	}

	/**
	 * 学生自身がパスワードを変更したい場合に使用
	 * @param myID 対象者のID
	 */
	public void showChangePassword(String myID){
		oldID = myID;
		oldName = Controller.getName(oldID);
		ID_Mine.setText(oldID);
		usr_Mine.setText(oldName);
		accountFrame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == changeButton){
			AccountInformation oldAccount = AccountInformation.ofId(oldID);
			String newPass = newTextField.getText();
			AccountInformation newAccount = AccountInformation.ofAll(oldID, oldName, newPass);
			controller.setAccount(oldAccount, newAccount);
			controller.logout();
			accountFrame.setVisible(false);
			Method.mainFrame.setVisible(false);
			Login.loginFrame.setVisible(true);
		}
	}

	public void keyPressed(KeyEvent e){
		if(KeyEvent.VK_ENTER == e.getKeyCode()){/*actionevebtを呼び出し*/
			ActionEvent actionEvents = new ActionEvent(e.getComponent(),ActionEvent.ACTION_PERFORMED,"");
			actionPerformed(actionEvents);
		}
	}
}
