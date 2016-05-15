package display;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//import javax.crypto.spec.IvParameterSpec;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class Method implements ActionListener{/*機能選択クラス*/
	//private system.Controller controller; // 内部動作用
	private JFrame mainFrame;
	private Container contentPane;
	private JPanel panelButton;
	private JPanel panelOne;
	private JPanel panelTwo;
	private JPanel panelThr;
	private JPanel panelFor;
	private JPanel cardPanel;
	private CardLayout cLayout;
	private JButton[] numButton = new JButton[4];
	private JButton endButton;
	private JLabel[] labelNum = new JLabel[4];
	

	Method(system.Controller controller){
		// システム引き継ぎ
		//this.controller = controller;

		//各種設定
		mainFrame = new JFrame("機能選択");
		mainFrame.setBounds(0, 0, 800, 600);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);
		contentPane = mainFrame.getContentPane();
		panelOne = new JPanel();
		panelTwo = new JPanel();
		panelThr = new JPanel();
		panelFor = new JPanel();
		panelButton = new JPanel(new GridLayout(1,5));
		cardPanel = new JPanel();
		cLayout = new CardLayout();
		cardPanel.setLayout(cLayout);
		numButton[0] = new JButton("出席管理");
		numButton[1] = new JButton("週報アップロード");
		numButton[2] = new JButton("予定確認");
		numButton[3] = new JButton("アカウント情報");
		endButton = new JButton("終了");
		labelNum[0] = new JLabel("機能1");
		labelNum[1] = new JLabel("機能2");
		labelNum[2] = new JLabel("機能3");
		labelNum[3] = new JLabel("機能4");

		//ボタンのアクション用
		for(int i=0;i<4;i++)
			numButton[i].addActionListener(this);
		endButton.addActionListener(this);

		//コンテンツの追加
		for(int i=0;i<4;i++)
			panelButton.add(numButton[i]);
		panelButton.add(endButton);
		panelOne.add(labelNum[0]);
		panelTwo.add(labelNum[1]);
		panelThr.add(labelNum[2]);
		panelFor.add(labelNum[3]);
		cardPanel.add(panelOne, "Meth1");
		cardPanel.add(panelTwo, "Meth2");
		cardPanel.add(panelThr, "Meth3");
		cardPanel.add(panelFor, "Meth4");

		//フレームに追加
		contentPane.add(panelButton, BorderLayout.NORTH);
		contentPane.add(cardPanel, BorderLayout.CENTER);
		mainFrame.setVisible(true);
	}

	public void actionPerformed(ActionEvent event){
		//機能1
		if(event.getSource() == numButton[0]){
			cLayout.show(cardPanel, "Meth1");
		}
		//機能2
		if(event.getSource() == numButton[1]){
			cLayout.show(cardPanel, "Meth2");
		}
		//機能3
		if(event.getSource() == numButton[2]){
			cLayout.show(cardPanel, "Meth3");
		}
		//機能4
		if(event.getSource() == numButton[3]){
			cLayout.show(cardPanel, "Meth4");
		}
		//終了
		if(event.getSource() == endButton){
			System.exit(0);
		}
	}
}
