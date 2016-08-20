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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import system.AccountInformation;
import system.Controller;

public class NewAccount extends KeyAdapter implements ActionListener{
	private Controller controller;
	private JFrame newAccFrame;
	private JLabel subLabel;
	private JTextField idField;
	private JTextField nameField;
	private JTextField passField;
	private JButton saveButton;
	private JButton returnButton;
	private String oldId;

	public NewAccount(Controller controller){
		Container contentPane;
		JPanel panelMaster;
		JLabel idLabel;
		JLabel nameLabel;
		JLabel passLabel;

		this.controller = controller;

		/* 各種設定 */
		newAccFrame = new JFrame("アカウント管理");
		newAccFrame.setBounds(0, 0, 400, 320);
		newAccFrame.setLocationRelativeTo(null);
		contentPane = newAccFrame.getContentPane();
		panelMaster = new JPanel();
		panelMaster.setLayout(null);

		int wid = 120, hei = 40;
		subLabel = new JLabel();
		subLabel.setBounds(0,0, 390, 50);
		subLabel.setFont(new Font(null, Font.PLAIN, 18));
		subLabel.setHorizontalAlignment(JLabel.CENTER);

		idLabel = new JLabel("新しいID");
		idLabel.setBounds(50,60,wid,hei);
		idLabel.setFont(new Font(null, Font.PLAIN, 12));
		idLabel.setHorizontalAlignment(JLabel.RIGHT);
		nameLabel = new JLabel("新しいユーザ名");
		nameLabel.setBounds(50,110,wid,hei);
		nameLabel.setFont(new Font(null, Font.PLAIN, 12));
		nameLabel.setHorizontalAlignment(JLabel.RIGHT);
		passLabel = new JLabel("新しいパスワード");
		passLabel.setBounds(50,160,wid,hei);
		passLabel.setFont(new Font(null, Font.PLAIN, 12));
		passLabel.setHorizontalAlignment(JLabel.RIGHT);

		idField = new JTextField();
		idField.setBounds(180,65,wid,30);
		nameField = new JTextField();
		nameField.setBounds(180,115,wid,30);
		passField = new JTextField();
		passField.setBounds(180,165,wid,30);

		saveButton = new JButton("保存");
		saveButton.setBounds(20,220,150,35);
		saveButton.setBackground(Color.WHITE);
		returnButton = new JButton("戻る");
		returnButton.setBounds(220,220,150,35);
		returnButton.setBackground(Color.WHITE);

		saveButton.addActionListener(this);
		saveButton.addKeyListener(this);
		returnButton.addActionListener(this);
		returnButton.addKeyListener(this);

		/*アイコンの設定*/
		ImageIcon icon = new ImageIcon("file/icon5.png");
		newAccFrame.setIconImage(icon.getImage());

		panelMaster.add(subLabel);
		panelMaster.add(idLabel);
		panelMaster.add(idField);
		panelMaster.add(nameLabel);
		panelMaster.add(nameField);
		panelMaster.add(passLabel);
		panelMaster.add(passField);
		panelMaster.add(saveButton);
		panelMaster.add(returnButton);

		contentPane.add(panelMaster, BorderLayout.CENTER);
		newAccFrame.setVisible(false);
	}

	public void showNewAccount(){
		subLabel.setText("新規作成");
		idField.setText("");
		nameField.setText("");
		passField.setText("");
		newAccFrame.setVisible(true);
	}

	public void showCheAccount(String Id, String Name, String Pass){
		oldId = Id;
		subLabel.setText("変更");
		idField.setText(Id);
		nameField.setText(Name);
		passField.setText(Pass);
		newAccFrame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == saveButton){
			AccountInformation oldAccount = AccountInformation.ofId(oldId);
			String newId = idField.getText();
			String newName = nameField.getText();
			String newPass = passField.getText();
			AccountInformation newAccount = AccountInformation.ofAll(newId, newName, newPass);
			controller.setAccount(oldAccount, newAccount);
			newAccFrame.setVisible(false);
			newAccFrame.dispose();
		}else if(e.getSource() == returnButton){
			newAccFrame.setVisible(false);
			newAccFrame.dispose();
		}
	}

	public void keyPressed(KeyEvent e){
		if(KeyEvent.VK_ENTER == e.getKeyCode()){/*actionevebtを呼び出し*/
			ActionEvent actionEvents = new ActionEvent(e.getComponent(),ActionEvent.ACTION_PERFORMED,"");
			actionPerformed(actionEvents);
		}
	}
}