package display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import system.AccountInformation;
import system.Controller;

public class SetAdmin extends KeyAdapter implements ActionListener{
	private Controller controller;
	private JFrame setAdminFrame;
	private Container contentPane;
	private JLabel subLabel;
	private JTextField idField;
	private JTextField nameField;
	private JTextField passField;
	private JButton saveButton;
	private JButton returnButton;
	private String oldId;
	private JCheckBox checkPass;

	public SetAdmin(Controller controller){
		// 引き継ぎ
		this.controller = controller;

		// パネル
		JPanel panelMaster;

		/* 各種設定 */
		setAdminFrame = new JFrame("教員用アカウント管理");
		setAdminFrame.setSize(400, 370);
		setAdminFrame.setLocationRelativeTo(null);
		setAdminFrame.setResizable(false);
		contentPane = setAdminFrame.getContentPane();
		panelMaster = new JPanel();
		panelMaster.setLayout(null);

		int wid = 120, hei = 40;
		subLabel = new JLabel();
		subLabel.setBounds(0,0, 390, 50);
		subLabel.setFont(new Font(null, Font.PLAIN, 18));
		subLabel.setHorizontalAlignment(JLabel.CENTER);

		JLabel idLabel = new JLabel("新しいID");
		idLabel.setBounds(50,60,wid,hei);
		idLabel.setFont(new Font(null, Font.PLAIN, 12));
		idLabel.setHorizontalAlignment(JLabel.RIGHT);
		JLabel nameLabel = new JLabel("新しいユーザ名");
		nameLabel.setBounds(50,110,wid,hei);
		nameLabel.setFont(new Font(null, Font.PLAIN, 12));
		nameLabel.setHorizontalAlignment(JLabel.RIGHT);
		JLabel passLabel = new JLabel("新しいパスワード");
		passLabel.setBounds(50,210,wid,hei);
		passLabel.setFont(new Font(null, Font.PLAIN, 12));
		passLabel.setHorizontalAlignment(JLabel.RIGHT);

		idField = new JTextField();
		idField.setBounds(180,65,wid,30);
		nameField = new JTextField();
		nameField.setBounds(180,115,wid,30);
		passField = new JTextField();
		passField.setBounds(180,215,wid,30);

		checkPass = new JCheckBox("パスワードを設定");
		checkPass.setBounds(120, 160, 200, 40);
		checkPass.setOpaque(false);
		checkPass.setMnemonic(KeyEvent.VK_P);

		saveButton = new JButton("保存");
		saveButton.setBounds(50,270,150,35);
		saveButton.setBackground(Color.WHITE);
		returnButton = new JButton("戻る");
		returnButton.setBounds(210,270,150,35);
		returnButton.setBackground(Color.WHITE);
		saveButton.addActionListener(this);
		saveButton.addKeyListener(this);
		returnButton.addActionListener(this);
		returnButton.addKeyListener(this);

		/*アイコンの設定*/
		ImageIcon icon = new ImageIcon("file/icon5.png");
		setAdminFrame.setIconImage(icon.getImage());

		panelMaster.add(subLabel);
		panelMaster.add(idLabel);
		panelMaster.add(idField);
		panelMaster.add(nameLabel);
		panelMaster.add(nameField);
		panelMaster.add(checkPass);
		panelMaster.add(passLabel);
		panelMaster.add(passField);
		panelMaster.add(saveButton);
		panelMaster.add(returnButton);

		contentPane.add(panelMaster, BorderLayout.CENTER);
		setAdminFrame.setVisible(false);
	}

	public void showSetAdmin(String Id, String Name){
		oldId = Id;
		subLabel.setText("変更");
		idField.setText(Id);
		nameField.setText(Name);
		setAdminFrame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == saveButton){
			AccountInformation oldAccount = AccountInformation.ofId(oldId);
			String newId = idField.getText();
			String newName = nameField.getText();
			AccountInformation newAccount;
			if(checkPass.isSelected()){
				String newPass = passField.getText();
				newAccount = AccountInformation.ofAll(newId, newName, newPass);
				//System.out.println("チェック有り");
			}else{
				newAccount = AccountInformation.ofIdName(newId, newName);
				//System.out.println("チェック無し");
			}
			controller.setAccount(oldAccount, newAccount);
			controller.logout();
			setAdminFrame.setVisible(false);
			Teacher.mainFrame.setVisible(false);
			Login.loginFrame.setVisible(true);
		}else if(e.getSource() == returnButton){
			setAdminFrame.setVisible(false);
			setAdminFrame.dispose();
		}
	}

	public void keyPressed(KeyEvent e){
		if(KeyEvent.VK_ENTER == e.getKeyCode()){/*actionevebtを呼び出し*/
			ActionEvent actionEvents = new ActionEvent(e.getComponent(),ActionEvent.ACTION_PERFORMED,"");
			actionPerformed(actionEvents);
		}
	}
}
