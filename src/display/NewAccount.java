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
import system.CheckRepaint;
import system.Controller;

public class NewAccount extends KeyAdapter implements ActionListener{
	private Controller controller;
	private JFrame newAccFrame;
	private JLabel subLabel;
	private JTextField idField;
	private JTextField nameField;
	private JButton saveButton;
	private JButton returnButton;
	private String oldId;
	private JCheckBox checkPass;

	private int na=0;//新規作成か変更かのやつ

	public NewAccount(Controller controller){
		Container contentPane;
		JPanel panelMaster;
		JLabel idLabel;
		JLabel nameLabel;

		this.controller = controller;

		/* 各種設定 */
		newAccFrame = new JFrame("アカウント管理");
		newAccFrame.setSize(400, 320);
		newAccFrame.setLocationRelativeTo(null);
		newAccFrame.setResizable(false);
		contentPane = newAccFrame.getContentPane();
		panelMaster = new JPanel();
		panelMaster.setLayout(null);

		int wid = 120, hei = 40;
		subLabel = new JLabel();
		subLabel.setBounds(0,0, 390, 50);
		subLabel.setFont(new Font(null, Font.PLAIN, 18));
		subLabel.setHorizontalAlignment(JLabel.CENTER);

		idLabel = new JLabel("ID");
		idLabel.setBounds(50,60,wid,hei);
		idLabel.setFont(new Font(null, Font.PLAIN, 12));
		idLabel.setHorizontalAlignment(JLabel.RIGHT);
		nameLabel = new JLabel("新しいユーザ名");
		nameLabel.setBounds(50,110,wid,hei);
		nameLabel.setFont(new Font(null, Font.PLAIN, 12));
		nameLabel.setHorizontalAlignment(JLabel.RIGHT);

		idField = new JTextField();
		idField.setBounds(180,65,wid,30);
		nameField = new JTextField();
		nameField.setBounds(180,115,wid,30);

		checkPass = new JCheckBox("パスワードのリセット");
		checkPass.setBounds(120, 160, 200, 40);
		checkPass.setOpaque(false);
		checkPass.setMnemonic(KeyEvent.VK_P);

		saveButton = new JButton("保存");
		saveButton.setBounds(50,200,150,35);
		saveButton.setBackground(Color.WHITE);
		returnButton = new JButton("戻る");
		returnButton.setBounds(210,200,150,35);
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
		panelMaster.add(checkPass);
		panelMaster.add(saveButton);
		panelMaster.add(returnButton);

		contentPane.add(panelMaster, BorderLayout.CENTER);
		newAccFrame.setVisible(false);
	}

	public void showNewAccount(){
		na = 0;
		subLabel.setText("新規作成");
		idField.setEditable(true);
		idField.setText("");
		nameField.setText("");
		checkPass.setSelected(true);
		checkPass.setEnabled(false);
		newAccFrame.setVisible(true);
	}

	public void showCheAccount(String Id, String Name){
		na = 1;
		oldId = Id;
		subLabel.setText("変更");
		idField.setText(Id);
		idField.setEditable(false);
		nameField.setText(Name);
		checkPass.setSelected(false);
		checkPass.setEnabled(true);
		newAccFrame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == saveButton){
			if(na == 1){
				AccountInformation oldAccount = AccountInformation.ofId(oldId);
				String newId = idField.getText();
				String newName = nameField.getText();
				AccountInformation newAccount;
				if(checkPass.isSelected()){
					newAccount = AccountInformation.ofAll(newId, newName, newId);
					//System.out.println("チェック有り");
				}else{
					newAccount = AccountInformation.ofIdName(newId, newName);
					//System.out.println("チェック無し");
				}
				controller.setAccount(oldAccount, newAccount);
				CheckRepaint.beTrue();
			}else if(na == 0){
				String newId = idField.getText();
				String newName = nameField.getText();
				AccountInformation newAccount = AccountInformation.ofAll(newId, newName, newId);
				controller.createUser(newAccount);
				CheckRepaint.beTrue();
			}
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
