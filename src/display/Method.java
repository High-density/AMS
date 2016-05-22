package display;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;





//import javax.crypto.spec.IvParameterSpec;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class Method extends KeyAdapter implements ActionListener{/*機能選択クラス*/
	//private system.Controller controller; // 内部動作用
	private JFrame mainFrame;
	private Container contentPane;
	private JPanel panelButton;
	private JPanel cardPanel;
	private JPanel panelNum[] = new JPanel[4];
	private CardLayout cLayout;
	private JButton numButton[] = new JButton[5];
	private JButton referButton;
	private JButton upButton;
	private JLabel labelNum[] = new JLabel[4];
	private JTextField pathTextField;

	Method(system.Controller controller){
		// システム引き継ぎ
		//this.controller = controller;

		//各種設定
		mainFrame = new JFrame("機能選択");
		mainFrame.setBounds(0, 0, 800, 600);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);
		contentPane = mainFrame.getContentPane();
		panelButton = new JPanel(new GridLayout(1,5));
		cardPanel = new JPanel();
		cLayout = new CardLayout();
		cardPanel.setLayout(cLayout);
		for (int i=0;i<4;i++){
			panelNum[i] = new JPanel();
			panelNum[i].setLayout(null);
		}
		
		numButton[0] = new JButton("出席管理");
		numButton[1] = new JButton("週報アップロード");
		numButton[2] = new JButton("予定確認");
		numButton[3] = new JButton("アカウント情報");
		numButton[4] = new JButton("終了");
		referButton  = new JButton("参照");
		referButton.setBounds(450,200,100,30);
		upButton = new JButton("アップロード");
		upButton.setBounds(300,300,200,30);
		
		labelNum[0] = new JLabel("出席");
		labelNum[1] = new JLabel("週報");
		labelNum[2] = new JLabel("予定");
		labelNum[3] = new JLabel("アカウント");
		for(int i=0;i<4;i++)
			labelNum[i].setBounds(380,10,200,40);
		pathTextField = new JTextField("ファイルを参照してください");
		pathTextField.setBounds(200,200,250,31);

		//ボタンのアクション用
		for(int i=0;i<5;i++){
			numButton[i].addActionListener(this);
			numButton[i].addKeyListener(this);
		}
		referButton.addActionListener(this);
		referButton.addKeyListener(this);
		
		//コンテンツの追加
		for(int i=0;i<5;i++)
			panelButton.add(numButton[i]);
		for(int i=0;i<4;i++){
			String str = "Meth" + (i+1);
			panelNum[i].add(labelNum[i]);
			cardPanel.add(panelNum[i], str);
		}
		panelNum[3].add(pathTextField);
		panelNum[3].add(referButton);
		panelNum[3].add(upButton);

		//フレームに追加
		contentPane.add(panelButton, BorderLayout.NORTH);
		contentPane.add(cardPanel, BorderLayout.CENTER);
		mainFrame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e){
		//機能1
		if(e.getSource() == numButton[0]){
			cLayout.show(cardPanel, "Meth1");
		}
		//機能2
		if(e.getSource() == numButton[1]){
			cLayout.show(cardPanel, "Meth2");
		}
		//機能3
		if(e.getSource() == numButton[2]){
			cLayout.show(cardPanel, "Meth3");
		}
		//機能4
		if(e.getSource() == numButton[3]){
			cLayout.show(cardPanel, "Meth4");
		}
		//終了
		if(e.getSource() == numButton[4]){
			System.exit(0);
		}
	}
	public void keyPressed(KeyEvent e){
		if(KeyEvent.VK_ENTER == e.getKeyCode()){
			//機能1
			if(e.getSource() == numButton[0]){
				cLayout.show(cardPanel, "Meth1");
			}
			//機能2
			if(e.getSource() == numButton[1]){
				cLayout.show(cardPanel, "Meth2");
			}
			//機能3
			if(e.getSource() == numButton[2]){
				cLayout.show(cardPanel, "Meth3");
			}
			//機能4
			if(e.getSource() == numButton[3]){
				cLayout.show(cardPanel, "Meth4");
			}
			//終了
			if(e.getSource() == numButton[4]){
				System.exit(0);
			}
		}
	}
}
