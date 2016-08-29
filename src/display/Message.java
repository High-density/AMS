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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Message extends KeyAdapter implements ActionListener{
	private JFrame messFrame;
	private Container contentPane;
	private JPanel panelMaster;
	private JTextArea messageArea;
	private JScrollPane messageScroll;
	private JButton returnButton;

	public Message(){
		/* 各種設定 */
		messFrame = new JFrame("メッセージ");
		messFrame.setBounds(0, 0, 400, 240);
		messFrame.setLocationRelativeTo(null);
		messFrame.setAlwaysOnTop(true);
		contentPane = messFrame.getContentPane();
		panelMaster = new JPanel();
		panelMaster.setLayout(null);
		messageArea = new JTextArea(5, 26);
		messageArea.setBounds(0,0,300,100);
		messageArea.setFont(new Font(null, Font.PLAIN, 16));
		messageArea.setLineWrap(true);
		messageArea.setOpaque(false);
		messageArea.setEditable(false);
		messageScroll = new JScrollPane(messageArea);
		messageScroll.setBorder(null);
		messageScroll.setBounds(10,10,365,140);
		returnButton = new JButton("戻る");
		returnButton.setBounds(50,160,100,35);
		returnButton.setBackground(Color.WHITE);
		returnButton.addActionListener(this);
		returnButton.addKeyListener(this);

		/*アイコンの設定*/
		ImageIcon icon = new ImageIcon("src/icon/icon.png");
		messFrame.setIconImage(icon.getImage());

		//panelMaster.add(messageArea);
		panelMaster.add(messageScroll);
		panelMaster.add(returnButton);

		contentPane.add(panelMaster, BorderLayout.CENTER);

		messFrame.setVisible(false);
	}

	/**
	 * ユーザに向けてメッセージを表示
	 * @param メッセージ内容
	 */
	public void showMessage(String textMessage){
		messageArea.setText(textMessage);//ここでメッセージを入れるための関数を実行
		messFrame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == returnButton){
			messFrame.setVisible(false);
			messFrame.dispose();
		}
	}
	public void keyPressed(KeyEvent e){
		if(KeyEvent.VK_ENTER == e.getKeyCode()){/*actionevebtを呼び出し*/
			ActionEvent actionEvents = new ActionEvent(e.getComponent(),ActionEvent.ACTION_PERFORMED,"");
			actionPerformed(actionEvents);
		}
	}
}
