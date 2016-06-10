package display;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.time.YearMonth;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

class Method extends KeyAdapter implements ActionListener{/*機能選択クラス*/
	private system.Controller controller; // 内部動作用
	private JFrame mainFrame;
	private Container contentPane;
	private JPanel panelButton;
	private JPanel cardPanel;
	private JPanel panelNum[] = new JPanel[4];
	private JPanel calPanel;
	private JPanel planPanel;
	private CardLayout cLayout;
	private JButton numButton[] = new JButton[5];
	private JButton dayButton[] = new JButton[42];
	private JButton dayButton_clone[] = new JButton[42];
	private JButton weekButton[] = new JButton[7];
	private JButton weekButton_clone[] = new JButton[7];
	private JButton referButton;
	private JButton upButton;
	private JButton nextButton;
	private JButton backButton;
	private JLabel labelNum[] = new JLabel[4];
	private JLabel testPathLabel;//ファイルパス取得テスト
	private JLabel monthLabel;
	private JTextField pathTextField;

	private YearMonth yearMonth;
	private Calendar calendar = Calendar.getInstance();
	private final String weekName[] = {"日","月","火","水","木","金","土"};
	private int year = calendar.get(Calendar.YEAR);
	private int month = calendar.get(Calendar.MONTH)+1;

	Method(system.Controller controller){
		/* システム引き継ぎ */
		this.controller = controller;

		/* メインフレーム設定 */
		mainFrame = new JFrame("機能選択");
		mainFrame.setBounds(0, 0, 800, 600);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);
		contentPane = mainFrame.getContentPane();

		/* 各種設定*/
		PanelButton();//機能選択ボタンの追加
		Method0();//機能1用パネル設定
		Method1();//機能2用パネル設定
		Method2();
		Method3();
		CardPanel();//機能パネル

		/* ボタンのアクション用 */
		actionButton();

		/* フレームに追加 */
		contentPane.add(panelButton, BorderLayout.NORTH);
		contentPane.add(cardPanel, BorderLayout.CENTER);
		mainFrame.setVisible(true);
	}

	private void PanelButton(){
		panelButton = new JPanel(new GridLayout(1,5));
		numButton[0] = new JButton("出席管理");
		numButton[1] = new JButton("週報アップロード");
		numButton[2] = new JButton("予定確認");
		numButton[3] = new JButton("アカウント情報");
		numButton[4] = new JButton("ログアウト");
		for(int i=0;i<5;i++)
			numButton[i].setBackground(Color.WHITE);

		panelButton.add(numButton[0]);
		panelButton.add(numButton[1]);
		panelButton.add(numButton[2]);
		panelButton.add(numButton[3]);
		panelButton.add(numButton[4]);
	}

	private void Method0(){
		panelNum[0] = new JPanel();
		panelNum[0].setLayout(null);
		calPanel = new JPanel(new GridLayout(7,7));
		calPanel.setBounds(200,130,400,400);
		nextButton = new JButton("next");
		nextButton.setBounds(550,60,200,40);
		nextButton.setBackground(Color.WHITE);
		backButton = new JButton("back");
		backButton.setBounds(050,60,200,40);
		backButton.setBackground(Color.WHITE);
		labelNum[0] = new JLabel("出席");
		labelNum[0].setBounds(380,10,200,40);
		labelNum[0].setFont(new Font(null, Font.PLAIN, 18));
		monthLabel = new JLabel(year+"年"+month+"月");
		monthLabel.setBounds(340,60,200,40);
		monthLabel.setFont(new Font(null, Font.PLAIN, 24));
		for(int i=0;i<7;i++){
			weekButton[i] = new JButton(weekName[i]);
			weekButton[i].setFont(new Font(null, Font.PLAIN, 16));
			weekButton[i].setBackground(Color.YELLOW);
			weekButton[i].setBorder(new LineBorder(Color.BLACK,1,true));
		}

		calendar.set(year, month-1, 1);
		yearMonth = YearMonth.of(year, month);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int maxDate = yearMonth.lengthOfMonth();
		//System.out.println(year+"年"+month+"月");
		//System.out.println("今月は"+maxDate+"日あります");
		//System.out.println("今月の1日は" +weekName[dayOfWeek]+ "曜日です");
		for(int i=0;i<dayOfWeek;i++)
			dayButton[i] = new JButton("");

		for(int i=dayOfWeek;i<dayOfWeek+maxDate;i++)
			dayButton[i] = new JButton(""+(1+i-dayOfWeek));
		for (int i=dayOfWeek+maxDate;i<42;i++)
			dayButton[i] = new JButton("");
		
		for(int i=0;i<7;i++)
			calPanel.add(weekButton[i]);
		for(int i=0;i<42;i++){
			dayButton[i].setBackground(Color.WHITE);
			calPanel.add(dayButton[i]);//カレンダーボタン追加
		}

		panelNum[0].add(labelNum[0]);
		panelNum[0].add(calPanel);
		panelNum[0].add(nextButton);
		panelNum[0].add(backButton);
		panelNum[0].add(monthLabel);
	}

	private void Method1(){
		panelNum[1] = new JPanel();
		panelNum[1].setLayout(null);
		labelNum[1] = new JLabel("週報");
		labelNum[1].setBounds(380,10,200,40);
		labelNum[1].setFont(new Font(null, Font.PLAIN, 18));
		referButton  = new JButton("参照");
		referButton.setBounds(500,100,100,30);
		referButton.setBackground(Color.WHITE);
		upButton = new JButton("アップロード");
		upButton.setBounds(300,200,200,30);
		upButton.setBackground(Color.WHITE);
		pathTextField = new JTextField("ファイルを参照してください");
		pathTextField.setBounds(200,100,300,31);
		testPathLabel = new JLabel("ここにファイルパスを表示");//ファイルパス取得
		testPathLabel.setBounds(10,500,500,30);//ファイルパス取得テ

		panelNum[1].add(labelNum[1]);
		panelNum[1].add(pathTextField);
		panelNum[1].add(referButton);
		panelNum[1].add(upButton);
		panelNum[1].add(testPathLabel);
	}

	private void Method2(){
		panelNum[2] = new JPanel();
		panelNum[2].setLayout(null);
		planPanel = new JPanel();
		planPanel.setLayout(new GridLayout(7, 7));
		planPanel.setBounds(10, 80, 400, 400);
		labelNum[2] = new JLabel("予定");
		labelNum[2].setBounds(180,10,200,40);
		labelNum[2].setFont(new Font(null, Font.PLAIN, 18));
		for(int i=0;i<7;i++){
			weekButton_clone[i] = new JButton(weekName[i]);
			weekButton_clone[i].setFont(new Font(null, Font.PLAIN, 16));
			//weekButton[i].setForeground(Color.WHITE);
			weekButton_clone[i].setBackground(Color.ORANGE);
			weekButton_clone[i].setBorder(new LineBorder(Color.BLACK,1,true));
		}
		
		calendar.set(year, month-1, 1);
		yearMonth = YearMonth.of(year, month);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int maxDate = yearMonth.lengthOfMonth();
		for(int i=0;i<dayOfWeek;i++)
			dayButton_clone[i] = new JButton("");
		for(int i=dayOfWeek;i<dayOfWeek+maxDate;i++)
			dayButton_clone[i] = new JButton(""+(1+i-dayOfWeek));
		for (int i=dayOfWeek+maxDate;i<42;i++)
			dayButton_clone[i] = new JButton("");
		
		for(int i=0;i<7;i++)
			planPanel.add(weekButton_clone[i]);
		for(int i=0;i<42;i++){
			dayButton_clone[i].setBackground(Color.WHITE);
			planPanel.add(dayButton_clone[i]);//カレンダーボタン追加
		}
		
		panelNum[2].add(labelNum[2]);
		panelNum[2].add(planPanel);
	}

	private void Method3(){
		panelNum[3] = new JPanel();
		panelNum[3].setLayout(null);
		labelNum[3] = new JLabel("アカウント");
		labelNum[3].setBounds(380,10,200,40);
		labelNum[3].setFont(new Font(null, Font.PLAIN, 18));

		panelNum[3].add(labelNum[3]);
	}

	private void CardPanel(){
		cardPanel = new JPanel();
		cLayout = new CardLayout();
		cardPanel.setLayout(cLayout);

		for(int i=0;i<4;i++){//それぞれの機能名を入れる
			String str = "Meth" + (i+1);
			cardPanel.add(panelNum[i], str);
		}
	}

	private void actionButton(){
		numButton[0].addActionListener(this);
		numButton[0].addKeyListener(this);
		numButton[1].addActionListener(this);
		numButton[1].addKeyListener(this);
		numButton[2].addActionListener(this);
		numButton[2].addKeyListener(this);
		numButton[3].addActionListener(this);
		numButton[3].addKeyListener(this);
		numButton[4].addActionListener(this);
		numButton[4].addKeyListener(this);
		referButton.addActionListener(this);
		referButton.addKeyListener(this);
		upButton.addActionListener(this);
		upButton.addKeyListener(this);
		nextButton.addActionListener(this);
		nextButton.addKeyListener(this);
		backButton.addActionListener(this);
		backButton.addKeyListener(this);
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == numButton[0]){/*機能1*/
			cLayout.show(cardPanel, "Meth1");
		}

		if(e.getSource() == numButton[1]){/*機能2*/
			cLayout.show(cardPanel, "Meth2");
		}

		if(e.getSource() == numButton[2]){/*機能3*/
			cLayout.show(cardPanel, "Meth3");
			new Message("おおおおおおお");
		}

		if(e.getSource() == numButton[3]){/*機能4*/
			cLayout.show(cardPanel, "Meth4");
		}

		if(e.getSource() == referButton){/*ファイル参照用*/
			JFileChooser filechooser = new JFileChooser();
			int selected = filechooser.showOpenDialog(null);//ダイアログ表示
			if (selected == JFileChooser.APPROVE_OPTION){
				File file = filechooser.getSelectedFile();
				pathTextField.setText(file.getPath());//ファイルが選ばれたらパスを表示
			}else if (selected == JFileChooser.CANCEL_OPTION){
				pathTextField.setText("キャンセルされました");
			}else if (selected == JFileChooser.ERROR_OPTION){
				pathTextField.setText("エラー又は取消しがありました");
			}
		}

		if(e.getSource() == upButton){/*アップロード*/
			testPathLabel.setText(pathTextField.getText());//ファイルパス取得テスト
			controller.submitReport(pathTextField.getText());
		}

		if(e.getSource() == numButton[4]){/*ログアウト*/
			controller.logout();
			mainFrame.setVisible(false);
			Login.loginFrame.setVisible(true);
		}
	}
	public void keyPressed(KeyEvent e){
		if(KeyEvent.VK_ENTER == e.getKeyCode()){
			if(e.getSource() == numButton[0]){/*機能1*/
				cLayout.show(cardPanel, "Meth1");
			}
			if(e.getSource() == numButton[1]){/*機能2*/
				cLayout.show(cardPanel, "Meth2");
			}
			if(e.getSource() == numButton[2]){/*機能3*/
				cLayout.show(cardPanel, "Meth3");
			}
			if(e.getSource() == numButton[3]){/*機能4*/
				cLayout.show(cardPanel, "Meth4");
			}
			if(e.getSource() == referButton){/*ファイル参照用*/
				JFileChooser filechooser = new JFileChooser();
				int selected = filechooser.showOpenDialog(null);//ダイアログ表示
				if (selected == JFileChooser.APPROVE_OPTION){
					File file = filechooser.getSelectedFile();
					pathTextField.setText(file.getPath());	//ファイルが選ばれたらパスを表示
				}else if (selected == JFileChooser.CANCEL_OPTION){
					pathTextField.setText("キャンセルされました");
				}else if (selected == JFileChooser.ERROR_OPTION){
					pathTextField.setText("エラー又は取消しがありました");
				}
			}
			if(e.getSource() == upButton){/*アップロード*/
				testPathLabel.setText(pathTextField.getText());//ファイルパス取得テスト
			}
			if(e.getSource() == numButton[4]){/*ログアウト*/
				controller.logout();
				mainFrame.setVisible(false);
				Login.loginFrame.setVisible(true);
			}
		}
	}
}
