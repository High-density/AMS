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
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import system.Agenda;
import system.AttendanceBook;
import system.Controller;
import system.Slave;

class Teacher extends KeyAdapter implements ActionListener{/*機能選択クラス*/
	/*main*/
	private Controller controller;	//内部動作用
	private Message message;		//エラー呼び出し用
	private NewAccount newAccount;
	private JFrame mainFrame;
	private Container contentPane;
	private JPanel panelButton;
	private JPanel cardPanel;private CardLayout cLayout;
	private JPanel panelNum[] = new JPanel[4];
	private JButton numButton[] = new JButton[5];
	private JLabel labelNum[] = new JLabel[4];

	/*attend*/
	private JPanel calPanel;
	private JPanel IDPanel;
	private JScrollPane scrollPane;
	private JLabel idLabel[] = new JLabel[100];
	private JLabel dayLabel[] = new JLabel[32];
	private JButton attButton[][];
	private JButton aNextButton;
	private JButton aBackButton;
	private JLabel aMonthLabel;
	private JLabel pMonthLabel;

	/*report*/
	private JPanel repoPanel;
	private JScrollPane repoScrollPanel;
	private JButton stuButton[] = new JButton[100];
	private JLabel updateLabel[] = new JLabel[100];

	/*plan*/
	private JPanel planPanel;
	private JLabel ymd;//year month day
	private JLabel weekLabel[] = new JLabel[7];
	private JButton dayButton_clone[] = new JButton[42];
	private JButton addPlanButton;
	private JButton pNextButton;
	private JButton pBackButton;
	private JTextArea pTextArea;
	private final String weekName[] = {"日","月","火","水","木","金","土"};

	/*account*/
	private JPanel accPanel;
	private JScrollPane accScrollPanel;
	private JButton stuButton_clone[]= new JButton[100];
	private JButton addAccButton;/*追加*/
	private JButton cheAccButton;/*変更*/
	private JButton delAccButton;/*削除*/
	private JLabel stuNumLabel;

	/*someOne*/
	private YearMonth yearMonth;
	private Calendar calendar = Calendar.getInstance();
	private Calendar Cal = Calendar.getInstance();
	private int year[] = {calendar.get(Calendar.YEAR),calendar.get(Calendar.YEAR)};
	private int month[] = {calendar.get(Calendar.MONTH),calendar.get(Calendar.MONTH)};
	private int day = 0;//ボタンから取得した日
	private int numSize;//アカウント数

	Teacher(system.Controller controller, display.Message message) {
		/* システム引き継ぎ */
		this.controller = controller;
		this.message = message;
		newAccount = new NewAccount(this.controller);

		/* メインフレーム設定 */
		mainFrame = new JFrame("機能選択");
		mainFrame.setSize(800, 600);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);
		contentPane = mainFrame.getContentPane();

		/*アイコンの設定*/
		ImageIcon icon = new ImageIcon("src/icon/icon.png");
		mainFrame.setIconImage(icon.getImage());

		/* 各種設定*/
		PanelButton();	//機能選択ボタンの追加
		Attendance();	//出席管理パネル設定
		Report();		//報告書管理パネル設定
		Plan();			//予定管理パネルの設定
		Account();		//アカウント管理
		CardPanel();//機能パネル

		/* ボタンのアクション用 */
		actionButton();

		/* フレームに追加 */
		contentPane.add(panelButton, BorderLayout.NORTH);	//機能選択ボタンの追加
		contentPane.add(cardPanel, BorderLayout.CENTER);	//パネルの追加
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
		for(int i=0;i<5;i++)//ボタンの背景を白に
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
		IDPanel = new JPanel(new GridLayout((numSize+1), 31));
		calPanel = new JPanel(new GridLayout((numSize+1), 31));
		calPanel.setBounds(0,130,32*45,(numSize+1)*30);
		aNextButton = new JButton();
		aNextButton.setBounds(550,60,200,40);
		aNextButton.setContentAreaFilled(false);
		aNextButton.setBorderPainted(false);
		aBackButton = new JButton();
		aBackButton.setBounds(050,60,200,40);
		aBackButton.setContentAreaFilled(false);
		aBackButton.setBorderPainted(false);
		//ボタンへのiconの設置
		ImageIcon left = new ImageIcon("src/icon/left.png");
		ImageIcon right = new ImageIcon("src/icon/right.png");
		aBackButton.setIcon(left);
		aNextButton.setIcon(right);
		aBackButton.setHorizontalTextPosition(SwingConstants.CENTER);
		aNextButton.setHorizontalTextPosition(SwingConstants.CENTER);
		labelNum[0] = new JLabel("出席");
		labelNum[0].setBounds(0,10,800,40);
		labelNum[0].setFont(new Font(null, Font.PLAIN, 18));
		labelNum[0].setHorizontalAlignment(JLabel.CENTER);
		aMonthLabel = new JLabel(year+"年"+month+"月");
		aMonthLabel.setBounds(340,60,200,40);
		aMonthLabel.setFont(new Font(null, Font.PLAIN, 24));
		attButton = new JButton[numSize][31];

		for(int i=0;i<dayLabel.length;i++){/*日付表示*/
			dayLabel[i] = new JLabel((String.format("%1$02d", i)));
			dayLabel[i].setBounds(0,0,50,40);
			dayLabel[i].setHorizontalAlignment(JLabel.CENTER);
			dayLabel[i].setOpaque(true);
			dayLabel[i].setBackground(Color.YELLOW);
			dayLabel[i].setBorder(new LineBorder(Color.GRAY, 1, true));
		}

		for(int i=0;i<numSize;i++){/*出欠席ボタン*/
			idLabel[i] = new JLabel();
			idLabel[i].setHorizontalAlignment(JLabel.CENTER);
			idLabel[i].setForeground(Color.WHITE);
			idLabel[i].setOpaque(true);
			idLabel[i].setBackground(Color.GRAY);
			idLabel[i].setBorder(new LineBorder(Color.DARK_GRAY, 1, true));
			for(int j=0;j<31;j++){
				attButton[i][j] = new JButton();
				attButton[i][j].setBounds(0,0,50,40);
				attButton[i][j].setBackground(Color.WHITE);
			}
		}

		calr(numSize);/*カレンダーのボタン作成用*/

		for(int i=0;i<dayLabel.length;i++){
			calPanel.add(dayLabel[i]);
		}
		for(int i=0;i<numSize;i++){
			calPanel.add(idLabel[i]);
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

	private void calr(int size){
		year[0] = calendar.get(Calendar.YEAR);
		month[0] = calendar.get(Calendar.MONTH);
		aMonthLabel.setText(year[0]+"年"+(month[0]+1)+"月");

		calendar.set(year[0], month[0], 1);
		yearMonth = YearMonth.of(year[0], month[0]+1);
		//int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int maxDate = yearMonth.lengthOfMonth();

		AttendanceBook[] Book = controller.getAttendance(yearMonth);
		int status[][] = new int [size][maxDate];

		for(int i=0;i<size;i++){/*IDを取得からの表示*/
			idLabel[i].setText(Book[i].getId());
		}

		for(int i=0;i<size;i++){
			for(int j=0;j<maxDate;j++){
				status[i][j] = Book[i].getData(j);
			}
		}

		for(int i=0;i<size;i++){
			for(int j=0;j<maxDate;j++){
				dayLabel[j+1].setText(""+(j+1));
				if(status[i][j] == AttendanceBook.ATTENDED){
					attButton[i][j].setText("出");
					attButton[i][j].setBackground(Color.CYAN);
					attButton[i][j].setForeground(Color.DARK_GRAY);
				}else if(status[i][j] == AttendanceBook.ABSENCE){
					attButton[i][j].setText("欠");
					attButton[i][j].setBackground(Color.PINK);;
					attButton[i][j].setForeground(Color.DARK_GRAY);
				}else if(status[i][j] == AttendanceBook.AUTHORIZED_ABSENCE){
					attButton[i][j].setText("公");
					attButton[i][j].setBackground(Color.GREEN);
					attButton[i][j].setForeground(Color.DARK_GRAY);
				}
			}
		}

		dayLabel[0].setText("ID");

		if(maxDate < 31){
			for (int i=0;i<numSize;i++){
				for(int j=maxDate+1;j<32;j++){
					dayLabel[j].setText("/");
					attButton[i][j-1].setText("/");
					attButton[i][j-1].setBackground(Color.WHITE);
				}
			}
		}
	}

	private void Report(){
		numSize = Slave.getSlaves().size(); /*アカウント数*/
		ArrayList<String> slaves = Slave.getSlaves();
		panelNum[1] = new JPanel();
		panelNum[1].setLayout(null);
		repoPanel = new JPanel(new GridLayout(numSize,2));
		repoPanel.setPreferredSize(new Dimension(600, (numSize*30)));
		repoScrollPanel = new JScrollPane();
		repoScrollPanel.setBounds(80, 100, 650, 300);
		repoScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		repoScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		labelNum[1] = new JLabel("報告書確認");
		labelNum[1].setBounds(0,10,800,40);
		labelNum[1].setFont(new Font(null, Font.PLAIN, 18));
		labelNum[1].setHorizontalAlignment(JLabel.CENTER);

		for(int i=0;i<numSize;i++){
			updateLabel[i] = new JLabel("");/*更新日をここに入れる*/
			updateLabel[i].setHorizontalAlignment(JLabel.CENTER);
			updateLabel[i].setFont(new Font(null, Font.PLAIN, 16));
			updateLabel[i].setBackground(Color.WHITE);
			updateLabel[i].setBorder(new LineBorder(Color.LIGHT_GRAY,1,true));
			updateLabel[i].setOpaque(true);
		}
		for(int i=0;i<numSize;i++){
			stuButton[i] = new JButton();
			stuButton[i].setPreferredSize(new Dimension(300, 30));
			stuButton[i].setBackground(Color.WHITE);
			stuButton[i].setText(slaves.get(i));
			repoPanel.add(stuButton[i]);
			repoPanel.add(updateLabel[i]);
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
		labelNum[2].setBounds(0,10,400,40);
		labelNum[2].setFont(new Font(null, Font.PLAIN, 18));
		labelNum[2].setHorizontalAlignment(JLabel.CENTER);
		ymd = new JLabel("日付を選択");
		ymd.setBounds(500,10,200,40);
		ymd.setFont(new Font(null, Font.PLAIN, 18));
		ymd.setHorizontalAlignment(JLabel.CENTER);
		pMonthLabel = new JLabel(year[1]+"年"+(month[1]+1)+"月");
		pMonthLabel.setBounds(150,70,200,40);
		pMonthLabel.setFont(new Font(null, Font.PLAIN, 24));
		pTextArea = new JTextArea(20,24);
		pTextArea.setBounds(450, 50, 300, 400);
		pTextArea.setLineWrap(true);
		addPlanButton = new JButton("予定追加");
		addPlanButton.setBounds(500,470,200,40);
		addPlanButton.setBackground(Color.WHITE);
		pNextButton = new JButton();
		pNextButton.setBounds(300,70,100,40);
		pNextButton.setContentAreaFilled(false);
		pNextButton.setBorderPainted(false);
		pBackButton = new JButton();
		pBackButton.setBounds(030,70,100,40);
		pBackButton.setContentAreaFilled(false);
		pBackButton.setBorderPainted(false);
		//ボタンへのiconの設置
		ImageIcon left = new ImageIcon("src/icon/left_mini.png");
		ImageIcon right = new ImageIcon("src/icon/right_mini.png");
		pNextButton.setIcon(right);
		pBackButton.setIcon(left);
		pNextButton.setHorizontalTextPosition(SwingConstants.CENTER);
		pNextButton.setHorizontalTextPosition(SwingConstants.CENTER);
		for(int i=0;i<7;i++){
			weekLabel[i] = new JLabel(weekName[i]);
			weekLabel[i].setHorizontalAlignment(JLabel.CENTER);
			weekLabel[i].setFont(new Font(null, Font.PLAIN, 16));
			weekLabel[i].setBackground(Color.WHITE);
			weekLabel[i].setBorder(new LineBorder(Color.DARK_GRAY,1,true));
			weekLabel[i].setOpaque(true);
			if(i==0){
				weekLabel[i].setForeground(Color.RED);
				weekLabel[i].setBorder(new LineBorder(Color.RED,1,true));
			}else if(i==6){
				weekLabel[i].setForeground(Color.BLUE);
				weekLabel[i].setBorder(new LineBorder(Color.BLUE,1,true));
			}
		}
		for(int i=0;i<dayButton_clone.length;i++)
			dayButton_clone[i] = new JButton();

		calr_clone();/*カレンダーの表示*/

		for(int i=0;i<7;i++)
			planPanel.add(weekLabel[i]);
		for(int i=0;i<dayButton_clone.length;i++){
			dayButton_clone[i].setBackground(Color.WHITE);
			planPanel.add(dayButton_clone[i]);//カレンダーボタン追加
		}

		panelNum[2].add(labelNum[2]);
		panelNum[2].add(addPlanButton);
		panelNum[2].add(ymd);
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
		//System.out.println(dayOfWeek);
		int maxDate = yearMonth.lengthOfMonth();
		for(int i=0;i<dayOfWeek;i++){
			dayButton_clone[i].setText("");
		}
		for(int i=dayOfWeek;i<dayOfWeek+maxDate;i++){
			dayButton_clone[i].setText(""+(1+i-dayOfWeek));
			Cal.set(year[1], month[1], (i+1-dayOfWeek));
			if(Cal.get(Calendar.DAY_OF_WEEK) == 7)
				dayButton_clone[i].setForeground(Color.BLUE);
			else if(Cal.get(Calendar.DAY_OF_WEEK) == 1)
				dayButton_clone[i].setForeground(Color.RED);
			else
				dayButton_clone[i].setForeground(Color.BLACK);
		}
		for (int i=dayOfWeek+maxDate;i<dayButton_clone.length;i++){
			dayButton_clone[i].setText("");
		}
	}

	private void Account(){
		numSize = Slave.getSlaves().size(); /*アカウント数*/
		year[0] = calendar.get(Calendar.YEAR);
		month[0] = calendar.get(Calendar.MONTH);
		yearMonth = YearMonth.of(year[0], month[0]+1);
		ArrayList<String> slaves = Slave.getSlaves();
		accPanel = new JPanel(new GridLayout(numSize,2));
		accPanel.setPreferredSize(new Dimension(300, (numSize*30)));
		accScrollPanel = new JScrollPane();
		accScrollPanel.setBounds(100,  80, 320, 400);
		accScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		accScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panelNum[3] = new JPanel();
		panelNum[3].setLayout(null);
		for(int i=0;i<numSize;i++){
			stuButton_clone[i] = new JButton();
			stuButton_clone[i].setPreferredSize(new Dimension(300, 30));
			stuButton_clone[i].setBackground(Color.WHITE);
			accPanel.add(stuButton_clone[i]);
			stuButton_clone[i].setText(slaves.get(i));
		}
		int wid=100, hig=60;
		addAccButton = new JButton("新規作成");
		addAccButton.setBounds(500, 80,200,60);
		addAccButton.setFont(new Font(null, Font.PLAIN, 14));
		addAccButton.setBackground(Color.WHITE);
		cheAccButton = new JButton("変更");
		cheAccButton.setBounds(500,230,wid,hig);
		cheAccButton.setFont(new Font(null, Font.PLAIN, 14));
		cheAccButton.setBackground(Color.WHITE);
		delAccButton = new JButton("削除");
		delAccButton.setBounds(600,230,wid,hig);
		delAccButton.setFont(new Font(null, Font.PLAIN, 14));
		delAccButton.setBackground(Color.WHITE);

		labelNum[3] = new JLabel("アカウント管理");
		labelNum[3].setBounds(0,10,800,40);
		labelNum[3].setFont(new Font(null, Font.PLAIN, 18));
		labelNum[3].setHorizontalAlignment(JLabel.CENTER);
		stuNumLabel = new JLabel("編集したいIDを選択");
		stuNumLabel.setBounds(500,180,200,60);
		stuNumLabel.setFont(new Font(null, Font.PLAIN, 18));
		stuNumLabel.setHorizontalAlignment(JLabel.CENTER);
		stuNumLabel.setBackground(Color.LIGHT_GRAY);

		accScrollPanel.setViewportView(accPanel);

		panelNum[3].add(labelNum[3]);
		panelNum[3].add(accScrollPanel);
		panelNum[3].add(stuNumLabel);
		panelNum[3].add(addAccButton);
		panelNum[3].add(cheAccButton);
		panelNum[3].add(delAccButton);
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

	private void message(String mess){/*message("")でメッセージを表示*/
		message.showMessage(mess);
	}

	private void member(int i){
		if(i == -1){
			newAccount.showNewAccount();
		}else{
			ArrayList<String> slaves = Slave.getSlaves();
			String slave = slaves.get(i);
			newAccount.showCheAccount(slave, "サレジオ太郎", "");
		}
	}

	private void actionButton(){
		numSize = Slave.getSlaves().size(); /*アカウント数*/
		/*main*/
		for(int i=0;i<5;i++){
			numButton[i].addActionListener(this);
			numButton[i].addKeyListener(this);
		}
		/*attend*/
		aNextButton.addActionListener(this);
		aNextButton.addKeyListener(this);
		aBackButton.addActionListener(this);
		aBackButton.addKeyListener(this);
		for(int i=0;i<numSize;i++){
			for(int j=0;j<31;j++){
				attButton[i][j].addActionListener(this);
				attButton[i][j].addKeyListener(this);
				attButton[i][j].setActionCommand("attButton"+i+j);
			}
		}
		/*repo*/
		for(int i=0;i<numSize;i++){
			stuButton[i].addActionListener(this);
			stuButton[i].addKeyListener(this);
			stuButton[i].setActionCommand("stuButton_real"+i);
		}
		/*plan*/
		addPlanButton.addActionListener(this);
		addPlanButton.addKeyListener(this);
		pNextButton.addActionListener(this);
		pNextButton.addKeyListener(this);
		pBackButton.addActionListener(this);
		pBackButton.addKeyListener(this);
		for(int i=0;i<dayButton_clone.length;i++){
			dayButton_clone[i].addActionListener(this);
			dayButton_clone[i].addKeyListener(this);
			dayButton_clone[i].setActionCommand("dayButton_clone"+i);
		}
		/*account*/
		for(int i=0;i<numSize;i++){
			stuButton_clone[i].addActionListener(this);
			stuButton_clone[i].addKeyListener(this);
			stuButton_clone[i].setActionCommand("stuButton_clone"+i);
		}
		addAccButton.addActionListener(this);
		addAccButton.addKeyListener(this);
		cheAccButton.addActionListener(this);
		cheAccButton.addKeyListener(this);
		delAccButton.addActionListener(this);
		delAccButton.addKeyListener(this);
	}

	private int memNum = -1;
	private String stuid = "";
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == numButton[0]){//機能1
			calendar.set(Calendar.YEAR, year[0]);
			calendar.set(Calendar.MONTH, month[0]);
			cLayout.show(cardPanel, "Meth1");
		}else if(e.getSource() == numButton[1]){//機能2
			cLayout.show(cardPanel, "Meth2");
		}else if(e.getSource() == numButton[2]){//機能3
			calendar.set(Calendar.YEAR, year[1]);
			calendar.set(Calendar.MONTH, month[1]);
			cLayout.show(cardPanel, "Meth3");
		}else if(e.getSource() == numButton[3]){//機能4
			cLayout.show(cardPanel, "Meth4");
		}else if(e.getSource() == numButton[4]){//ログアウト
			controller.logout();
			mainFrame.setVisible(false);
			Login.loginFrame.setVisible(true);
		}else if(e.getSource() == aNextButton){
			calendar.set(Calendar.MONTH, month[0] +1);	//attendで1ヶ月増やす
			calr(numSize);
			panelNum[0].repaint();
		}else if(e.getSource() == aBackButton){
			calendar.set(Calendar.MONTH, month[0] -1);	//attendで1ヶ月減らす
			calr(numSize);
			panelNum[0].repaint();
		}else if(e.getActionCommand().matches("attButton" + ".*")){	//出欠情報の変更機能
			for(int i=0;i<numSize;i++){	//ID用の i
				for(int j=0;j<31;j++){	//日付ようの j
					if(e.getSource()==attButton[i][j])
						System.out.println("i="+ (i+1) +" j="+ (j+1));
				}
			}
		}else if(e.getActionCommand().matches("stuButton_real" + ".*")){//報告書管理
			String user = null;
			for(int i=0;i<numSize;i++){
				if(e.getSource() == stuButton[i])
					user= stuButton[i].getText();
			}
			controller.showReport(user);
			/*
			File dir = new File("./file/" + path + "/report");
			JFileChooser filechooser = new JFileChooser(dir);
			int selected = filechooser.showOpenDialog(contentPane);
			if (selected == JFileChooser.APPROVE_OPTION){
				File file = filechooser.getSelectedFile();
				File openFile = new File("./file/" + path + "/" + filechooser.getName(file));
				Desktop desktop = Desktop.getDesktop();
				try{
					desktop.open(openFile);
				}catch(IOException e1){}

			}
			*/
		}else if(e.getSource() == addPlanButton){//ここに予定を追加機能を実装する
			//controller(agenda, day, plan);
		}else if(e.getSource() == pNextButton){
			calendar.set(Calendar.MONTH, month[1] +1);	//planで1ヶ月増やす
			calr_clone();
			panelNum[2].repaint();
		}else if(e.getSource() == pBackButton){
			calendar.set(Calendar.MONTH, month[1] -1);	//planで1ヶ月減らす
			calr_clone();
			panelNum[2].repaint();
		}else if(e.getActionCommand().matches("dayButton_clone" + ".*")){/*planで日付を取得するとき*/
			YearMonth ym = YearMonth.now();
			Agenda agenda = controller.getAgenda(ym);
			for(int i=0;i<dayButton_clone.length;i++){
				if(e.getSource() == dayButton_clone[i]){
					String dayName = dayButton_clone[i].getText();
					if(!dayName.equals("")){
						day = Integer.parseInt(dayName);
						String plan = agenda.getData(day);
						ymd.setText(year[1]+"年"+(month[1]+1)+"月"+day+"日");
						pTextArea.setText(plan);
					}
					else{
						day = 0;
						ymd.setText("日付を選択");
						pTextArea.setText("");
					}
				}
			}
		}else if(e.getActionCommand().matches("stuButton_clone" + ".*")){//account
			for(int i=0;i<numSize;i++){
				if(e.getSource() == stuButton_clone[i]){
					memNum = i;
					stuNumLabel.setText(stuButton_clone[i].getText() + "を");
				}
			}
		}else if(e.getSource() == addAccButton){//新規作成
			member(-1);
			stuNumLabel.setText("編集したいIDを選択");
			memNum = -1;
		}else if(e.getSource() == cheAccButton){//変更
			member(memNum);
			stuNumLabel.setText("編集したいIDを選択");
			memNum = -1;
		}else if(e.getSource() == delAccButton){//削除
			stuid = stuButton_clone[memNum].getText();
			message(stuid + "を削除します");
			controller.deleteUser(stuid);
			stuNumLabel.setText("編集したいIDを選択");
			memNum = -1;
			//panelNum[3].repaint();
		}
	}

	public void keyPressed(KeyEvent e){
		if(KeyEvent.VK_ENTER == e.getKeyCode()){/*actionevebtを呼び出し*/
			ActionEvent actionEvents = new ActionEvent(e.getComponent(),ActionEvent.ACTION_PERFORMED,"");
			actionPerformed(actionEvents);
		}
	}
}
