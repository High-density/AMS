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
	private display.Message message; //エラー呼び出し用
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
	private JButton weekButton[] = new JButton[7];
	private JButton aNextButton;
	private JButton aBackButton;
	private JButton referButton;
	private JButton upButton;
	private JButton dayButton_clone[] = new JButton[42];
	private JButton weekButton_clone[] = new JButton[7];
	private JButton pNextButton;
	private JButton pBackButton;
	private JLabel labelNum[] = new JLabel[4];
	private JLabel testPathLabel;//ファイルパス取得テスト
	private JLabel aMonthLabel;
	private JLabel pMonthLabel;
	private JLabel planDate;
	private JLabel ID;
	private JLabel UserName;
	private JTextField pathTextField;

	private YearMonth yearMonth;
	private Calendar calendar = Calendar.getInstance();
	private final String weekName[] = {"日","月","火","水","木","金","土"};
	private int year[] = {calendar.get(Calendar.YEAR),calendar.get(Calendar.YEAR)};
	private int month[] = {calendar.get(Calendar.MONTH),calendar.get(Calendar.MONTH)};
	private int button;

	Method(system.Controller controller, display.Message message){
		/* システム引き継ぎ */
		this.controller = controller;
		this.message = message;

		/* メインフレーム設定 */
		mainFrame = new JFrame("機能選択");
		mainFrame.setBounds(0, 0, 800, 600);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);
		contentPane = mainFrame.getContentPane();

		/* 各種設定*/
		PanelButton();//機能選択ボタンの追加
		Attendance();//機能1用パネル設定
		Report();//機能2用パネル設定
		Plan();
		Account();
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

	private void Attendance(){
		panelNum[0] = new JPanel();
		panelNum[0].setLayout(null);
		calPanel = new JPanel(new GridLayout(7,7));
		calPanel.setBounds(200,130,400,400);
		aNextButton = new JButton("next");
		aNextButton.setBounds(550,60,200,40);
		aNextButton.setBackground(Color.WHITE);
		aBackButton = new JButton("back");
		aBackButton.setBounds(050,60,200,40);
		aBackButton.setBackground(Color.WHITE);
		labelNum[0] = new JLabel("出席");
		labelNum[0].setBounds(380,10,200,40);
		labelNum[0].setFont(new Font(null, Font.PLAIN, 18));

		aMonthLabel = new JLabel(year+"年"+month+"月");
		aMonthLabel.setBounds(340,60,200,40);
		aMonthLabel.setFont(new Font(null, Font.PLAIN, 24));
		for(int i=0;i<7;i++){
			weekButton[i] = new JButton(weekName[i]);
			weekButton[i].setFont(new Font(null, Font.PLAIN, 16));
			weekButton[i].setBackground(Color.YELLOW);
			weekButton[i].setBorder(new LineBorder(Color.BLACK,1,true));
		}

		for(int i=0;i<42;i++)
			dayButton[i] = new JButton();
		calr();
		for(int i=0;i<7;i++)
			calPanel.add(weekButton[i]);
		for(int i=0;i<42;i++){
			dayButton[i].setBackground(Color.WHITE);
			calPanel.add(dayButton[i]);//カレンダーボタン追加
		}

		panelNum[0].add(labelNum[0]);
		panelNum[0].add(calPanel);
		panelNum[0].add(aNextButton);
		panelNum[0].add(aBackButton);
		panelNum[0].add(aMonthLabel);
	}

	private void calr(){
		month[0] = calendar.get(Calendar.MONTH);
		aMonthLabel.setText(year[0]+"年"+(month[0]+1)+"月");

		calendar.set(year[0], month[0], 1);
		yearMonth = YearMonth.of(year[0], month[0]+1);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int maxDate = yearMonth.lengthOfMonth();
		for(int i=0;i<dayOfWeek;i++)
			dayButton[i].setText("");
		for(int i=dayOfWeek;i<dayOfWeek+maxDate;i++)
			dayButton[i].setText(""+(1+i-dayOfWeek));
		for (int i=dayOfWeek+maxDate;i<42;i++)
			dayButton[i].setText("");
	}

	private void Report(){
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

	private void Plan(){
		panelNum[2] = new JPanel();
		panelNum[2].setLayout(null);
		planPanel = new JPanel();
		planPanel.setLayout(new GridLayout(7, 7));
		planPanel.setBounds(10, 120, 400, 400);
		labelNum[2] = new JLabel("予定");
		labelNum[2].setBounds(180,10,200,40);
		labelNum[2].setFont(new Font(null, Font.PLAIN, 18));
		pMonthLabel = new JLabel(year[1]+"年"+(month[1]+1)+"月");
		pMonthLabel.setBounds(150,70,200,40);
		pMonthLabel.setFont(new Font(null, Font.PLAIN, 24));
		planDate = new JLabel("指定された日付け");
		planDate.setBounds(550,20,200,40);
		planDate.setBackground(Color.WHITE);
		pNextButton = new JButton("next");
		pNextButton.setBounds(300,70,100,40);
		pNextButton.setBackground(Color.WHITE);
		pBackButton = new JButton("back");
		pBackButton.setBounds(030,70,100,40);
		pBackButton.setBackground(Color.WHITE);
		for(int i=0;i<7;i++){
			weekButton_clone[i] = new JButton(weekName[i]);
			weekButton_clone[i].setFont(new Font(null, Font.PLAIN, 16));
			weekButton_clone[i].setBackground(Color.ORANGE);
			weekButton_clone[i].setBorder(new LineBorder(Color.BLACK,1,true));
		}
		for(int i=0;i<42;i++)
			dayButton_clone[i] = new JButton();

		calr_clone();/*カレンダーの表示*/

		for(int i=0;i<7;i++)
			planPanel.add(weekButton_clone[i]);
		for(int i=0;i<42;i++){
			dayButton_clone[i].setBackground(Color.WHITE);
			planPanel.add(dayButton_clone[i]);//カレンダーボタン追加
		}

		panelNum[2].add(planDate);
		panelNum[2].add(labelNum[2]);
		panelNum[2].add(pMonthLabel);
		panelNum[2].add(pNextButton);
		panelNum[2].add(pBackButton);
		panelNum[2].add(planPanel);
	}

	private void calr_clone(){
		year[1] = calendar.get(Calendar.YEAR);
		month[1] = calendar.get(Calendar.MONTH);
		pMonthLabel.setText(year[1]+"年"+(month[1]+1)+"月");
		calendar.set(year[1], month[1], 1);
		yearMonth = YearMonth.of(year[1], month[1]+1);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int maxDate = yearMonth.lengthOfMonth();
		button = dayOfWeek;
		for(int i=0;i<dayOfWeek;i++)
			dayButton_clone[i].setText("");
		for(int i=dayOfWeek;i<dayOfWeek+maxDate;i++)
			dayButton_clone[i].setText(""+(1+i-dayOfWeek));
		for (int i=dayOfWeek+maxDate;i<42;i++)
			dayButton_clone[i].setText("");
	}

	private void Account(){
		panelNum[3] = new JPanel();
		panelNum[3].setLayout(null);
		labelNum[3] = new JLabel("アカウント");
		labelNum[3].setBounds(380,10,200,40);
		labelNum[3].setFont(new Font(null, Font.PLAIN, 18));
		ID = new JLabel("ID");
		ID.setBounds(380,30,200,40);
		ID.setFont(new Font(null, Font.PLAIN, 18));
		UserName = new JLabel("ユーザ名");
		UserName.setBounds(380,50,200,40);
		UserName.setFont(new Font(null, Font.PLAIN, 18));

		panelNum[3].add(labelNum[3]);
		panelNum[3].add(ID);
		panelNum[3].add(UserName);
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

	private void message(String mess){/*message()でメッセージを表示*/
		message.showMessage(mess);
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
		aNextButton.addActionListener(this);
		aNextButton.addKeyListener(this);
		aBackButton.addActionListener(this);
		aBackButton.addKeyListener(this);
		pNextButton.addActionListener(this);
		pNextButton.addKeyListener(this);
		pBackButton.addActionListener(this);
		pBackButton.addKeyListener(this);
		for(int i = button;i < 42;i++){
			dayButton_clone[i].addActionListener(this);
			dayButton_clone[i].addKeyListener(this);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == numButton[0]){/*機能1*/
			calendar.set(Calendar.YEAR, year[0]);
			calendar.set(Calendar.MONTH, month[0]);
			cLayout.show(cardPanel, "Meth1");
		}else if(e.getSource() == numButton[1]){/*機能2*/
			cLayout.show(cardPanel, "Meth2");
		}else if(e.getSource() == numButton[2]){/*機能3*/
			calendar.set(Calendar.YEAR, year[1]);
			calendar.set(Calendar.MONTH, month[1]);
			cLayout.show(cardPanel, "Meth3");
		}else if(e.getSource() == numButton[3]){/*機能4*/
			cLayout.show(cardPanel, "Meth4");
		}else if(e.getSource() == referButton){/*ファイル参照用*/
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
		}else if(e.getSource() == upButton){/*アップロード*/
			message("アップロードしてもよろしいですか?");
			testPathLabel.setText(pathTextField.getText());//ファイルパス取得テスト
			controller.submitReport(pathTextField.getText());
		}else if(e.getSource() == numButton[4]){/*ログアウト*/
			controller.logout();
			mainFrame.setVisible(false);
			Login.loginFrame.setVisible(true);
		}else if(e.getSource() == aNextButton){
			calendar.set(Calendar.MONTH, month[0] +1);	//1ヶ月増やす
			calr();
			panelNum[0].repaint();
		}else if(e.getSource() == aBackButton){
			calendar.set(Calendar.MONTH, month[0] -1);	//1ヶ月減らす
			calr();
			panelNum[0].repaint();
		}else if(e.getSource() == pNextButton){
			calendar.set(Calendar.MONTH, month[1] +1);	//1ヶ月増やす
			if(month[1]+1 == 13)
				calendar.set(Calendar.YEAR, year[1]+1);
			calr_clone();
			panelNum[2].repaint();
		}else if(e.getSource() == pBackButton){
			calendar.set(Calendar.MONTH, month[1] -1);	//1ヶ月減らす
			if(month[1]-1 == -1)
				calendar.set(Calendar.YEAR, year[1]-1);
			calr_clone();
			panelNum[2].repaint();
		}
		for(int i=button;i < 42;i++){
			if(e.getSource() == dayButton_clone[i]){
			    String str = dayButton_clone[i].getText();
			    planDate.setText(str);
			    panelNum[2].repaint();
			}
		}
	}

	public void keyPressed(KeyEvent e){
		if(KeyEvent.VK_ENTER == e.getKeyCode()){
			if(e.getSource() == numButton[0]){/*機能1*/
				calendar.set(Calendar.YEAR, year[0]);
				calendar.set(Calendar.MONTH, month[0]);
				cLayout.show(cardPanel, "Meth1");
			}else if(e.getSource() == numButton[1]){/*機能2*/
				cLayout.show(cardPanel, "Meth2");
			}else if(e.getSource() == numButton[2]){/*機能3*/
				calendar.set(Calendar.YEAR, year[1]);
				calendar.set(Calendar.MONTH, month[1]);
				cLayout.show(cardPanel, "Meth3");
			}else if(e.getSource() == numButton[3]){/*機能4*/
				cLayout.show(cardPanel, "Meth4");
			}else if(e.getSource() == referButton){/*ファイル参照用*/
				JFileChooser filechooser = new JFileChooser();
				int selected = filechooser.showOpenDialog(null);//ダイアログ表示
				if(selected == JFileChooser.APPROVE_OPTION){
					File file = filechooser.getSelectedFile();
					pathTextField.setText(file.getPath());	//ファイルが選ばれたらパスを表示
				}else if (selected == JFileChooser.CANCEL_OPTION){
					pathTextField.setText("キャンセルされました");
				}else if (selected == JFileChooser.ERROR_OPTION){
					pathTextField.setText("エラー又は取消しがありました");
				}
			}else if(e.getSource() == upButton){/*アップロード*/
				testPathLabel.setText(pathTextField.getText());//ファイルパス取得テスト
			}else if(e.getSource() == numButton[4]){/*ログアウト*/
				controller.logout();
				mainFrame.setVisible(false);
				Login.loginFrame.setVisible(true);
			}else if(e.getSource() == aNextButton){
				calendar.set(Calendar.MONTH, month[0] +1);	//1ヶ月増やす
				calr();
				panelNum[0].repaint();
			}else if(e.getSource() == aBackButton){
				calendar.set(Calendar.MONTH, month[0] -1);	//1ヶ月減らす
				calr();
				panelNum[0].repaint();
			}else if(e.getSource() == pNextButton){
				calendar.set(Calendar.MONTH, month[1] +1);	//1ヶ月増やす
				calr_clone();
				panelNum[2].repaint();
			}else if(e.getSource() == pBackButton){
				calendar.set(Calendar.MONTH, month[1] -1);	//1ヶ月減らす
				calr_clone();
				panelNum[2].repaint();
			}
		}
	}
}
