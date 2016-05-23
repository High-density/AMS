package display;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
//import javax.swing.LayoutFocusTraversalPolicy;

public class Login extends KeyAdapter implements ActionListener{/*ログインクラス*/
	private system.Controller controller; // 内部動作用
	private JFrame loginFrame;
	private Container contentPane;
	private JLabel idLabel;
	private JLabel passLabel;
	private JLabel annouceLabel;
	private JPanel panelMaster;
	private JTextField idField;
	private JPasswordField passField;
	private JButton loginButton;
	private JButton endButton;

	public Login(){
		/* システム呼び出し */
		controller = new system.Controller();

		/* 各種設定 */
		loginFrame = new JFrame("ログインフォーム");
		loginFrame.setBounds(0, 0, 400, 240);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.setLocationRelativeTo(null);
		contentPane = loginFrame.getContentPane();
		panelMaster = new JPanel();
		panelMaster.setLayout(null);

		idLabel = new JLabel  ("ID");
		idLabel.setBounds(120, 10, 50, 20);
		idField = new JTextField();
		idField.setBounds(120, 35, 150, 20);
		
		passLabel = new JLabel("Pass");
		passLabel.setBounds(120, 60, 50, 20);
		passField = new JPasswordField();
		passField.setBounds(120, 85, 150, 20);
		
		annouceLabel = new JLabel("ログインしてください");
		annouceLabel.setBounds(140,180,150,20);
		
		loginButton = new JButton("ログイン");
		loginButton.setBounds(95, 120, 100, 30);
		endButton = new JButton("終了");
		endButton.setBounds(205, 120, 100, 30);

		/* アクションの設定用 */
		passField.addKeyListener(this);
		loginButton.addActionListener(this);
		loginButton.addKeyListener(this);
		endButton.addActionListener(this);
		endButton.addKeyListener(this);

		/* パネルにいろいろ追加 */
		panelMaster.add(idLabel);
		panelMaster.add(idField);
		panelMaster.add(passLabel);
		panelMaster.add(passField);
		panelMaster.add(loginButton);
		panelMaster.add(endButton);
		panelMaster.add(annouceLabel);

		/* フレームにパネルを追加 */
		contentPane.add(panelMaster, BorderLayout.CENTER);
		loginFrame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == loginButton)/*ログインボタン*/
			ToF();
		if(e.getSource() == endButton)/*終了ボタン*/
			System.exit(0);
	}
	public void keyPressed(KeyEvent e){
		if(KeyEvent.VK_ENTER == e.getKeyCode()){
			if(e.getSource() == loginButton || e.getSource() == passField)/*ログインボタン・パスフィールド*/
				ToF();
			if(e.getSource() == endButton)/*終了ボタン*/
				System.exit(0);
		}
	}
	public void ToF(){
		String ID = new String(idField.getText());//ID
		String PA = new String(passField.getPassword());//パスワード
		if(controller.login(ID, PA)){//IDとPassがそれぞれ一致したら
			loginFrame.setVisible(false);
			new Method(controller);
		}else
			annouceLabel.setText("ログインできません\n");
	}
}
