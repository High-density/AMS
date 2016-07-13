package display;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.YearMonth;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import system.AttendanceBook;
import system.Slave;

class Teacher extends KeyAdapter implements ActionListener{/*機能選択クラス*/
	private system.Controller controller; // 内部動作用
	private display.Message message; //エラー呼び出し用
	private JFrame mainFrame;
	private Container contentPane;
	private JPanel panelButton;
	private JPanel cardPanel;
	private JPanel panelNum[] = new JPanel[4];
	private JPanel calPanel;
	private JPanel repoPanel;
	private JPanel planPanel;
	private JPanel accPanel;
	private JScrollPane scrollPane;
	private JScrollPane repoScrollPanel;
	private JScrollPane accScrollPanel;
	private CardLayout cLayout;
	private JButton numButton[] = new JButton[5];
	private JButton dayButton[] = new JButton[32];
	private JButton idButton[] = new JButton[100];
	private JButton attButton[][];
	private JButton aNextButton;
	private JButton aBackButton;
	private JButton stuButton[][] = new JButton[100][2];
	private JButton stuButton_clone[]= new JButton[100];
	private JButton dayButton_clone[] = new JButton[42];
	private JButton weekButton[] = new JButton[7];
	private JButton addPlanButton;
	private JButton pNextButton;
	private JButton pBackButton;
	private JLabel labelNum[] = new JLabel[4];
	private JLabel aMonthLabel;
	private JLabel pMonthLabel;
	private JTextArea pTextArea;

	private YearMonth yearMonth;
	private Calendar calendar = Calendar.getInstance();
	private final String weekName[] = {"日","月","火","水","木","金","土"};
	private int year[] = {calendar.get(Calendar.YEAR),calendar.get(Calendar.YEAR)};
	private int month[] = {calendar.get(Calendar.MONTH),calendar.get(Calendar.MONTH)};
	private int day = 0;
	private int numSize;

	Teacher(system.Controller controller, display.Message message) {
		/* システム引き継ぎ */
		this.controller = controller;
		this.message = message;

		/* メインフレーム設定 */
		mainFrame = new JFrame("機能選択");
		mainFrame.setSize(800, 600);
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
		panelButton.setPreferredSize(new Dimension(800, 40));
		numButton[0] = new JButton("出席管理");
		numButton[1] = new JButton("報告書管理");
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
		numSize = Slave.getSlaves().size(); /*アカウント数*/

		panelNum[0] = new JPanel();
		panelNum[0].setLayout(null);
		calPanel = new JPanel(new GridLayout((numSize+1), 35));
		calPanel.setBounds(0,130,32*45,(numSize+1)*30);
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

		attButton = new JButton[numSize][31];

		for(int i=0;i<32;i++){/*日付表示*/
			dayButton[i] = new JButton((String.format("%1$02d", i)));
			dayButton[i].setBounds(0,0,50,40);
			dayButton[i].setBackground(Color.YELLOW);
		}

		for(int i=0;i<numSize;i++){/*出欠席ボタン*/
			idButton[i] = new JButton();
			idButton[i].setBackground(Color.GRAY);
			idButton[i].setForeground(Color.WHITE);
			for(int j=0;j<31;j++){
				attButton[i][j] = new JButton();
				attButton[i][j].setBounds(0,0,50,40);
				attButton[i][j].setBackground(Color.WHITE);
			}
		}

		calr(numSize);/*カレンダーのボタン作成用*/

		for(int i=0;i<32;i++){
			calPanel.add(dayButton[i]);
		}
		for(int i=0;i<numSize;i++){
			calPanel.add(idButton[i]);
			for(int j=0;j<31;j++){
				calPanel.add(attButton[i][j]);/*カレンダーボタン追加*/
			}
		}

		scrollPane = new JScrollPane(calPanel);
		scrollPane.setBounds(10,130,760,300);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		panelNum[0].add(labelNum[0]);
		panelNum[0].add(scrollPane);
		panelNum[0].add(aNextButton);
		panelNum[0].add(aBackButton);
		panelNum[0].add(aMonthLabel);
	}

	private int calr(int size){
		year[0] = calendar.get(Calendar.YEAR);
		month[0] = calendar.get(Calendar.MONTH);
		aMonthLabel.setText(year[0]+"年"+(month[0]+1)+"月");

		calendar.set(year[0], month[0], 1);
		yearMonth = YearMonth.of(year[0], month[0]+1);
		//int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int maxDate = yearMonth.lengthOfMonth() + 1;


		AttendanceBook[] Book = controller.getAttendance(yearMonth);
		int status[][] = new int [size][maxDate];

		for(int i=0;i<size;i++){
			idButton[i].setText(Book[i].getId());
		}

		for(int i=0;i<size;i++){
			for(int j=0;j<maxDate;j++){
				status[i][j] = Book[i].getStatus(j);
			}

		}

		for(int i=0;i<size;i++){
			for (int j=0;j<maxDate;j++){
				dayButton[j].setText(""+(j));
				if(status[i][j] == AttendanceBook.ATTENDED)
					attButton[i][j].setText("出");
				else if(status[i][j] == AttendanceBook.ABSENCE)
					attButton[i][j].setText("欠");
				else if(status[i][j] == 2)
					attButton[i][j].setText("公");
			}
		}

		dayButton[0].setText("ID");

		if(maxDate < 32){
			for (int i=0;i<numSize;i++){
				for(int j=maxDate;j<32;j++){
					dayButton[j].setText("/");
					attButton[i][j-1].setText("/");
				}
			}
		}

		return maxDate;
	}

	private void Report(){
		numSize = Slave.getSlaves().size(); /*アカウント数*/
		year[0] = calendar.get(Calendar.YEAR);
		month[0] = calendar.get(Calendar.MONTH);
		yearMonth = YearMonth.of(year[0], month[0]+1);
		AttendanceBook[] Book = controller.getAttendance(yearMonth);
		panelNum[1] = new JPanel();
		panelNum[1].setLayout(null);
		repoPanel = new JPanel(new GridLayout(numSize,2));
		repoPanel.setPreferredSize(new Dimension(600, (numSize*30)));
		repoScrollPanel = new JScrollPane();
		repoScrollPanel.setBounds(80, 100, 650, 300);
		repoScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		repoScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		labelNum[1] = new JLabel("報告書確認");
		labelNum[1].setBounds(350,10,200,40);
		labelNum[1].setFont(new Font(null, Font.PLAIN, 18));
		for(int i=0;i<numSize;i++){
			for(int j=0;j<2;j++){
				stuButton[i][j] = new JButton();
				stuButton[i][j].setPreferredSize(new Dimension(300, 30));
				stuButton[i][j].setBackground(Color.WHITE);
				repoPanel.add(stuButton[i][j]);
			}
			stuButton[i][0].setText(Book[i].getId());
		}

		repoScrollPanel.setViewportView(repoPanel);

		panelNum[1].add(labelNum[1]);
		panelNum[1].add(repoScrollPanel);
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
		pTextArea = new JTextArea(20,24);
		pTextArea.setBounds(450, 100, 300, 400);
		pTextArea.setLineWrap(true);
		addPlanButton = new JButton("予定追加");
		addPlanButton.setBounds(550,20,200,40);
		addPlanButton.setBackground(Color.WHITE);
		pNextButton = new JButton("next");
		pNextButton.setBounds(300,70,100,40);
		pNextButton.setBackground(Color.WHITE);
		pBackButton = new JButton("back");
		pBackButton.setBounds(030,70,100,40);
		pBackButton.setBackground(Color.WHITE);
		for(int i=0;i<7;i++){
			weekButton[i] = new JButton(weekName[i]);
			weekButton[i].setFont(new Font(null, Font.PLAIN, 16));
			weekButton[i].setBackground(Color.ORANGE);
			weekButton[i].setBorder(new LineBorder(Color.BLACK,1,true));
		}
		for(int i=0;i<dayButton_clone.length;i++)
			dayButton_clone[i] = new JButton();

		calr_clone();/*カレンダーの表示*/

		for(int i=0;i<7;i++)
			planPanel.add(weekButton[i]);
		for(int i=0;i<dayButton_clone.length;i++){
			dayButton_clone[i].setBackground(Color.WHITE);
			planPanel.add(dayButton_clone[i]);//カレンダーボタン追加
		}

		panelNum[2].add(labelNum[2]);
		panelNum[2].add(addPlanButton);
		panelNum[2].add(pMonthLabel);
		panelNum[2].add(pNextButton);
		panelNum[2].add(pBackButton);
		panelNum[2].add(planPanel);
		panelNum[2].add(pTextArea);
	}

	private void calr_clone(){
		year[1] = calendar.get(Calendar.YEAR);
		month[1] = calendar.get(Calendar.MONTH);
		pMonthLabel.setText(year[1]+"年"+(month[1]+1)+"月");
		calendar.set(year[1], month[1], 1);
		yearMonth = YearMonth.of(year[1], month[1]+1);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int maxDate = yearMonth.lengthOfMonth();
		for(int i=0;i<dayOfWeek;i++)
			dayButton_clone[i].setText("");
		for(int i=dayOfWeek;i<dayOfWeek+maxDate;i++)
			dayButton_clone[i].setText(""+(1+i-dayOfWeek));
		for (int i=dayOfWeek+maxDate;i<dayButton_clone.length;i++)
			dayButton_clone[i].setText("");
	}

	private void Account(){
		numSize = Slave.getSlaves().size(); /*アカウント数*/
		year[0] = calendar.get(Calendar.YEAR);
		month[0] = calendar.get(Calendar.MONTH);
		yearMonth = YearMonth.of(year[0], month[0]+1);
		AttendanceBook[] Book = controller.getAttendance(yearMonth);
		accPanel = new JPanel(new GridLayout(numSize,2));
		accPanel.setPreferredSize(new Dimension(300, (numSize*30)));
		accScrollPanel = new JScrollPane();
		accScrollPanel.setBounds(50, 100, 320, 400);
		accScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		accScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panelNum[3] = new JPanel();
		panelNum[3].setLayout(null);
		labelNum[3] = new JLabel("アカウント管理");
		labelNum[3].setBounds(380,10,200,40);
		labelNum[3].setFont(new Font(null, Font.PLAIN, 18));
		for(int i=0;i<numSize;i++){
			stuButton_clone[i] = new JButton();
			stuButton_clone[i].setPreferredSize(new Dimension(300, 30));
			stuButton_clone[i].setBackground(Color.WHITE);
			accPanel.add(stuButton_clone[i]);
			stuButton_clone[i].setText(Book[i].getId());
		}

		accScrollPanel.setViewportView(accPanel);

		panelNum[3].add(labelNum[3]);
		panelNum[3].add(accScrollPanel);
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
		aNextButton.addActionListener(this);
		aNextButton.addKeyListener(this);
		aBackButton.addActionListener(this);
		aBackButton.addKeyListener(this);
		addPlanButton.addActionListener(this);
		addPlanButton.addKeyListener(this);
		pNextButton.addActionListener(this);
		pNextButton.addKeyListener(this);
		pBackButton.addActionListener(this);
		pBackButton.addKeyListener(this);
		for(int i=0;i<dayButton_clone.length;i++){
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
			//message("確認してもよろしいですか?");
		}else if(e.getSource() == numButton[4]){/*ログアウト*/
			controller.logout();
			mainFrame.setVisible(false);
			Login.loginFrame.setVisible(true);
		}else if(e.getSource() == aNextButton){
			calendar.set(Calendar.MONTH, month[0] +1);	//1ヶ月増やす
			calr(numSize);
			panelNum[0].repaint();
		}else if(e.getSource() == aBackButton){
			calendar.set(Calendar.MONTH, month[0] -1);	//1ヶ月減らす
			calr(numSize);
			panelNum[0].repaint();
		}else if(e.getSource() == addPlanButton){//ここに予定を追加機能を実装する
			//writePlan(year[1], month[1]+1, dayName, pTextArea.getText);
		}else if(e.getSource() == pNextButton){
			calendar.set(Calendar.MONTH, month[1] +1);	//1ヶ月増やす
			calr_clone();
			panelNum[2].repaint();
		}else if(e.getSource() == pBackButton){
			calendar.set(Calendar.MONTH, month[1] -1);	//1ヶ月減らす
			calr_clone();
			panelNum[2].repaint();
		}else{
			for(int i=0;i<42;i++){
				if(e.getSource() == dayButton_clone[i]){
					String dayName = dayButton_clone[i].getText();
					if(!dayName.equals("")){
						day = Integer.parseInt(dayName);
						pTextArea.setText(year[1]+"年"+(month[1]+1)+"月"+day+"日");
					}
					else{
						day = 0;
						pTextArea.setText("日付なし");
					}
				}
			}
		}
	}

	public void keyPressed(KeyEvent e){
		if(KeyEvent.VK_ENTER == e.getKeyCode()){
			if(e.getSource() == numButton[0]){				/*機能1*/
				calendar.set(Calendar.YEAR, year[0]);
				calendar.set(Calendar.MONTH, month[0]);
				cLayout.show(cardPanel, "Meth1");
			}else if(e.getSource() == numButton[1]){		/*機能2*/
				cLayout.show(cardPanel, "Meth2");
			}else if(e.getSource() == numButton[2]){		/*機能3*/
				calendar.set(Calendar.YEAR, year[1]);
				calendar.set(Calendar.MONTH, month[1]);
				cLayout.show(cardPanel, "Meth3");
			}else if(e.getSource() == numButton[3]){		/*機能4*/
				cLayout.show(cardPanel, "Meth4");
				//message("確認してもよろしいですか?");
			}else if(e.getSource() == numButton[4]){		/*ログアウト*/
				controller.logout();
				mainFrame.setVisible(false);
				Login.loginFrame.setVisible(true);
			}else if(e.getSource() == aNextButton){
				calendar.set(Calendar.MONTH, month[0] +1);	/*1ヶ月増やす*/
				calr(numSize);
				panelNum[0].repaint();
			}else if(e.getSource() == aBackButton){
				calendar.set(Calendar.MONTH, month[0] -1);	/*1ヶ月減らす*/
				calr(numSize);
				panelNum[0].repaint();
			}else if(e.getSource() == addPlanButton){		/*ここに予定を追加機能を実装する*/
				//writePlan(year[1], month[1]+1, dayName, pTextArea.getText);
			}else if(e.getSource() == pNextButton){
				calendar.set(Calendar.MONTH, month[1] +1);	/*1ヶ月増やす*/
				calr_clone();
				panelNum[2].repaint();
			}else if(e.getSource() == pBackButton){
				calendar.set(Calendar.MONTH, month[1] -1);	/*1ヶ月減らす*/
				calr_clone();
				panelNum[2].repaint();
			}else{											/*日付判定用*/
				for(int i=0;i<42;i++){
					if(e.getSource() == dayButton_clone[i]){
						String dayName = dayButton_clone[i].getText();
						if(!dayName.equals("")){			/*日付ありなら保存*/
							day = Integer.parseInt(dayName);
							pTextArea.setText(year[1]+"年"+(month[1]+1)+"月"+day+"日");
						}
						else{								/*無しなら初期化*/
							day = 0;
							pTextArea.setText("日付なし");
						}
					}
				}
			}
		}
	}
}
