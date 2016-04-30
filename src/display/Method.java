package display;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

class Method implements ActionListener{/*機能選択クラス*/
    private system.Controller controller; // 内部動作用
	private JFrame mainFrame;
	private Container contentPane;
	private JPanel panelButton;
	private JPanel panelOne;
	private JButton oneButton;
	private JButton twoButton;
	private JButton thrButton;
	private JButton forButton;
	private JButton endButton;
	private JButton[] day = new JButton[31];

	Method(system.Controller controller){
        // システム引き継ぎ
        this.controller = controller;

		//各種設定
		mainFrame = new JFrame("機能選択");
		mainFrame.setBounds(0, 0, 800, 600);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);
		contentPane = mainFrame.getContentPane();
		panelButton = new JPanel();
		panelOne = new JPanel();
		panelOne.setLayout(new GridLayout(7,5));
		oneButton = new JButton("機能1");
		twoButton = new JButton("機能2");
		thrButton = new JButton("機能3");
		forButton = new JButton("機能4");
		endButton = new JButton("終了");

		//ボタンのアクション用
		oneButton.addActionListener(this);
		twoButton.addActionListener(this);
		thrButton.addActionListener(this);
		forButton.addActionListener(this);
		endButton.addActionListener(this);

		//コンテンツの追加
		panelButton.add(oneButton);
		panelButton.add(twoButton);
		panelButton.add(thrButton);
		panelButton.add(forButton);
		panelButton.add(endButton);
		for(int i=0;i<31;i++){

		}

		//フレームに追加
		contentPane.add(panelButton, BorderLayout.NORTH);
		mainFrame.setVisible(true);
	}

	public void actionPerformed(ActionEvent event){
		//機能1
		if(event.getSource() == oneButton){
			//	mainFrame.setVisible(false);
		}
		//機能2
		if(event.getSource() == twoButton){
			//	panelButtonton.setVisible(false);
		}
		//機能3
		if(event.getSource() == thrButton){
			//	mainFrame.setVisible(false);
		}
		//機能4
		if(event.getSource() == forButton){
			//	mainFrame.setVisible(false);
		}
		//終了
		if(event.getSource() == endButton){
			System.exit(0);
		}
	}
}
