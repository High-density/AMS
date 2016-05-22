package display;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
	private JPanel panelText;
	private JPanel panelButton;
	private JPanel panelError;
	private GridBagLayout gLayout;
	private GridBagConstraints gbc;
	private JTextField idField;
	private JPasswordField passField;
	private JButton loginButton;
	private JButton endButton;

	public Login(){
		// システム呼び出し
		controller = new system.Controller();

		//各種設定
		loginFrame = new JFrame("ログインフォーム");
		loginFrame.setBounds(0, 0, 400, 225);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.setLocationRelativeTo(null);
		contentPane = loginFrame.getContentPane();
		idLabel = new JLabel  ("ID");
		passLabel = new JLabel("Pass");
		annouceLabel = new JLabel("ログインしてください");
		panelText = new JPanel();
		gLayout = new GridBagLayout();
		panelText.setLayout(gLayout);
		panelButton = new JPanel();
		panelError = new JPanel();
		gbc = new GridBagConstraints();
		idField = new JTextField();
		passField = new JPasswordField();
		idField.setColumns(15);
		passField.setColumns(15);
		loginButton = new JButton("ログイン");
		endButton = new JButton("終了");

		//アクションの設定用
		passField.addKeyListener(this);
		loginButton.addActionListener(this);
		loginButton.addKeyListener(this);
		endButton.addActionListener(this);
		endButton.addKeyListener(this);

		//パネルにいろいろ追加
		gbc.gridx = 0;
		gbc.gridy = 0;
		gLayout.setConstraints(idLabel, gbc);
		gbc.gridx = 1;
		gLayout.setConstraints(idField, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gLayout.setConstraints(passLabel, gbc);
		gbc.gridx = 1;
		gLayout.setConstraints(passField, gbc);
		panelText.add(idLabel);
		panelText.add(idField);
		panelText.add(passLabel);
		panelText.add(passField);
		panelButton.add(loginButton);
		panelButton.add(endButton);
		panelError.add(annouceLabel);
		
		//フレームにパネルを追加
		contentPane.add(panelText, BorderLayout.NORTH);
		contentPane.add(panelButton, BorderLayout.CENTER);
		contentPane.add(panelError, BorderLayout.SOUTH);
		loginFrame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == loginButton)
			ToF();
		if(e.getSource() == endButton){
			System.exit(0);
		}
	}
	public void keyPressed(KeyEvent e){
		if(KeyEvent.VK_ENTER == e.getKeyCode()){
			if(e.getSource() == loginButton || e.getSource() == passField)
				ToF();
			if(e.getSource() == endButton){
				System.exit(0);
			}
		}
	}
	public void ToF(){
		String ID = new String(idField.getText());
		String PA = new String(passField.getPassword());
		if(controller.login(ID, PA) == 0){
			loginFrame.setVisible(false);
			new Method(controller);
		}else
			annouceLabel.setText("ログインできません\n");
	}
}
