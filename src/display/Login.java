
package display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends KeyAdapter implements ActionListener{/*ログインクラス*/
	private system.Controller controller; // 内部動作用
	private display.Message message; //メッセージ表示用
	static JFrame loginFrame;
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
		// TODO: Dialogクラスに変更
		message = new display.Message();

		/* 各種設定 */
		loginFrame = new JFrame("ログインフォーム");
		loginFrame.setBounds(0, 0, 400, 240);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.setLocationRelativeTo(null); // 画面の中央に表示
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
		loginButton.setBackground(Color.white);
		endButton = new JButton("終了");
		endButton.setBounds(205, 120, 100, 30);
		endButton.setBackground(Color.white);

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

		/*アイコンの設定*/
		try{
			ClassLoader cl = this.getClass().getClassLoader();
			ImageIcon icon = new ImageIcon(cl.getResource("src/icon/icon.png"));
			loginFrame.setIconImage(icon.getImage());
		}catch(Exception e){
			ImageIcon icorn = new ImageIcon("src/icon/icon.png");
			loginFrame.setIconImage(icorn.getImage());
		}
	}

	/**
	 * 入力とファイルの中身を比較してログイン可能か判定
	 */
	public void ToF(){
		String ID = new String(idField.getText());//ID
		String PA = new String(passField.getPassword());//パスワード
		if(controller.login(ID, PA)){//IDとPassがそれぞれ一致したら
			loginFrame.setVisible(false);
			if(ID.equals("root")) // TODO: 教員かどうかの判定をcontrollerでできるように
				new Teacher(controller, message);
			else
				new Method(controller, idField.getText());
		}else{
			annouceLabel.setText("ログインできません\n");
		}
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
}
