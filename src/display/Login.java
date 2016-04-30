package display;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login implements ActionListener{/*ログインクラス*/
	private JFrame loginFrame;
	private Container contentPane;
	private JLabel idLabel;
	private JLabel passLabel;
	private JLabel annouceLabel;
	private JPanel panelText;
	private JPanel panelButton;
	private JPanel panelError;
	private JTextField text;
	private JPasswordField password;
	private JButton loginButton;
	private JButton endButton;
	public Login(){
		//各種設定
		loginFrame = new JFrame("ログインフォーム");
		loginFrame.setBounds(0, 0, 225, 400);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.setLocationRelativeTo(null);
		contentPane = loginFrame.getContentPane();
		idLabel = new JLabel("ID");
		passLabel = new JLabel("Pass");
		annouceLabel = new JLabel("ログインしてください");
		panelText = new JPanel();
		panelText.setLayout(new GridLayout(4,1));
		panelButton = new JPanel();
		panelError = new JPanel();
		text = new JTextField();
		password = new JPasswordField();
		text.setColumns(10);
		loginButton = new JButton("ログイン");
		endButton = new JButton("終了");

		//アクションの設定用
		loginButton.addActionListener(this);
		endButton.addActionListener(this);

		//パネルにいろいろ追加
		panelText.add(idLabel);
		panelText.add(text);
		panelText.add(passLabel);
		panelText.add(password);
		panelButton.add(loginButton);
		panelButton.add(endButton);
		panelError.add(annouceLabel);

		//フレームにパネルを追加
		contentPane.add(panelText, BorderLayout.NORTH);
		contentPane.add(panelButton, BorderLayout.CENTER);
		contentPane.add(panelError, BorderLayout.SOUTH);
		loginFrame.setVisible(true);
	}

	public void actionPerformed(ActionEvent event){
		String ID = new String(text.getText());
		String PA = new String(password.getPassword());
		//ログインボタンのアクション
		if(event.getSource() == loginButton){
			if(ID.equals("i") && PA.equals("p")){
					loginFrame.setVisible(false);
					new Method();
			}else
				annouceLabel.setText("ログインできません\n");
		}
		//終了ボタンのアクション
		if(event.getSource() == endButton){
			System.exit(0);
		}
	}
}