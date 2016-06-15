package display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Message extends KeyAdapter implements ActionListener{
	static JFrame messFrame;
	private Container contentPane;
	private JPanel panelMaster;
	private JLabel messageLabel;
	private JButton returnButton;

	public Message(String textMessage){
		/* 各種設定 */
		messFrame = new JFrame("メッセージ");
		messFrame.setBounds(0, 0, 400, 240);
		messFrame.setLocationRelativeTo(null);
		contentPane = messFrame.getContentPane();
		panelMaster = new JPanel();
		panelMaster.setLayout(null);
		messageLabel = new JLabel();
		messageLabel.setBounds(0,0,200,150);
		messageLabel.setFont(new Font(null, Font.PLAIN, 16));
		returnButton = new JButton("戻る");
		returnButton.setBounds(50,160,100,35);
		returnButton.setBackground(Color.WHITE);

		messageLabel.setText(textMessage);//ここでメッセージを入れるための関数を実行

		returnButton.addActionListener(this);
		returnButton.addKeyListener(this);

		panelMaster.add(messageLabel);
		panelMaster.add(returnButton);

		contentPane.add(panelMaster, BorderLayout.CENTER);
		messFrame.setVisible(true);
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == returnButton)
			messFrame.setVisible(false);
	}
	public void keyPressed(KeyEvent e){
		if(KeyEvent.VK_ENTER == e.getKeyCode()){
			if(e.getSource() == returnButton)
				messFrame.setVisible(false);
		}
	}
}