package display;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
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
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import system.Agenda;
import system.AttendanceBook;
import system.CheckOS;
import system.Controller;
import system.Slave;

class Teacher extends KeyAdapter implements ActionListener, WindowListener{/*機能選択クラス*/
	/*main*/
	private Controller controller;	// 内部動作用
	private Message message;		// エラー呼び出し用
	private NewAccount newAccount;	// アカウント用
	private JFrame mainFrame;
	private Container contentPane;
	private JPanel panelButton;
	private JPanel cardPanel;private CardLayout cLayout;
	private JPanel panelNum[] = new JPanel[4];
	private JButton numButton[] = new JButton[5];
	private JLabel labelNum[] = new JLabel[4];
	private final String[] funcName = {"出席管理","報告書管理","予定管理",
										"アカウント管理", "ログアウト"};

	/*attend*/
	private JPanel IDPanel;
	private JPanel aDayPanel;
	private JPanel attPanel;
	private GridBagLayout attgLayout[] = new GridBagLayout[3];
	private GridBagConstraints attgbc[] = new GridBagConstraints[3];
	private JScrollPane IDScrollPanel;
	private JScrollPane dayScrollPanel;
	private JScrollPane attScrollPanel;
	private JLabel ID;
	private JLabel idLabel[];
	private JLabel dayLabel[] = new JLabel[31];
	private JButton attButton[][];
	private JButton aNextButton;
	private JButton aBackButton;
	private JLabel aMonthLabel;
	private JLabel pMonthLabel;

	/*report*/
	private JPanel repoPanel;
	private JScrollPane repoScrollPanel;
	private JButton rStudentsButton[];
	private JLabel updateLabel[];

	/*plan*/
	private JPanel planPanel;
	private JLabel ymd;// year month day
	private JLabel weekLabel[] = new JLabel[7];
	private JButton pDayButton[] = new JButton[42];
	private JButton addPlanButton;
	private JButton pNextButton;
	private JButton pBackButton;
	private JTextArea pTextArea;
	private Agenda agenda; // 予定
	private int planday = -1; // ボタンから取得した日
	private final String weekName[] = {"日","月","火","水","木","金","土"};

	/*account*/
	private JPanel accPanel;
	private JScrollPane accScrollPanel;
	private JButton aStudentsButton[];
	private JButton addAccButton;	// 追加
	private JButton cheAccButton;	// 変更
	private JButton delAccButton;	// 削除
	private JButton rootButton;		// 教員用
	private JLabel stuNumLabel;

	/*someOne*/
	private YearMonth[] yearMonth = new YearMonth[2];
	private Calendar calendar = Calendar.getInstance();
	private int year[] = {calendar.get(Calendar.YEAR),calendar.get(Calendar.YEAR)};
	private int month[] = {calendar.get(Calendar.MONTH),calendar.get(Calendar.MONTH)};
	private int numSize = Slave.getSlaves().size();//アカウント数
	private ArrayList<String> slaves = Slave.getSlaves(); //アカウントのID


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
		for(int i=0;i<5;i++){//ボタンの背景を白に
			numButton[i] = new JButton();
			numButton[i].setText(funcName[i]);
			numButton[i].setBackground(Color.WHITE);
			panelButton.add(numButton[i]);
		}
	}

	private void Attendance(){
		panelNum[0] = new JPanel();
		panelNum[0].setLayout(null);
		labelNum[0] = new JLabel("出席管理");
		labelNum[0].setBounds(0,10,800,40);
		labelNum[0].setFont(new Font(null, Font.PLAIN, 20));
		labelNum[0].setHorizontalAlignment(JLabel.CENTER);

		//ボタンへのiconの設置
		ImageIcon right = new ImageIcon("src/icon/right.png");
		aNextButton = new JButton();
		aNextButton.setBounds(550,60,200,50);
		aNextButton.setContentAreaFilled(false);
		aNextButton.setBorderPainted(false);
		aNextButton.setIcon(right);
		aNextButton.setHorizontalTextPosition(SwingConstants.CENTER);
		ImageIcon left = new ImageIcon("src/icon/left.png");
		aBackButton = new JButton();
		aBackButton.setBounds(050,60,200,50);
		aBackButton.setContentAreaFilled(false);
		aBackButton.setBorderPainted(false);
		aBackButton.setIcon(left);
		aBackButton.setHorizontalTextPosition(SwingConstants.CENTER);
		//
		aMonthLabel = new JLabel(year+"年"+month+"月");
		aMonthLabel.setBounds(340,60,200,40);
		aMonthLabel.setFont(new Font(null, Font.PLAIN, 24));

		Border border = new EmptyBorder(0,0,0,0);
		dayScrollPanel = new JScrollPane();
		dayScrollPanel.setBounds(70,130,693,31);
		dayScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		dayScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		dayScrollPanel.setBorder(border);
		IDScrollPanel = new JScrollPane();
		IDScrollPanel.setBounds(10,160,60,285);
		IDScrollPanel.setBorder(border);
		IDScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		IDScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		attScrollPanel = new JScrollPane();
		attScrollPanel.setBounds(70,161,710,300);
		attScrollPanel.setBorder(border);
		attScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		attScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		IDScrollPanel.getVerticalScrollBar().setModel(attScrollPanel.getVerticalScrollBar().getModel());
		dayScrollPanel.getHorizontalScrollBar().setModel(attScrollPanel.getHorizontalScrollBar().getModel());

		ID = new JLabel("ID");
		ID.setBounds(10,130,60,30);
		ID.setHorizontalAlignment(JLabel.CENTER);
		ID.setOpaque(true);
		ID.setBackground(Color.YELLOW);
		ID.setBorder(new LineBorder(Color.GRAY, 1, true));

		attReset();

		panelNum[0].add(labelNum[0]);
		panelNum[0].add(aNextButton);
		panelNum[0].add(aBackButton);
		panelNum[0].add(aMonthLabel);
		panelNum[0].add(ID);
		panelNum[0].add(dayScrollPanel);
		panelNum[0].add(IDScrollPanel);
		panelNum[0].add(attScrollPanel);
	}

	private void attendCalendar(){
		year[0] = calendar.get(Calendar.YEAR);
		month[0] = calendar.get(Calendar.MONTH);
		aMonthLabel.setText(year[0]+"年"+(month[0]+1)+"月");
		calendar.set(year[0], month[0], 1);
		yearMonth[0] = YearMonth.of(year[0], month[0]+1);
		int maxDate = yearMonth[0].lengthOfMonth();

		AttendanceBook[] Book = controller.getAttendance(yearMonth[0]);
		int status[][] = new int [numSize][maxDate];
		for(int i=0;i<numSize;i++){
			for(int j=0;j<maxDate;j++){
				status[i][j] = Book[i].getData(j);
			}
		}

		attgbc[2].gridy = 0;
		attgbc[2].ipadx = 32;	//+31ピクセル これで最小のXをでかくできる
		if(CheckOS.isWindows()){
			attgbc[2].ipady = 12;	//+12ピクセル これで最小のYをでかくできる
		}else{// if(CheckOS.isLinux()){
			attgbc[2].ipady = 15;
		}
		for(int i=0;i<maxDate;i++){// 日付表示
			dayLabel[i].setText(String.format("%1$02d", i+1));
			attgbc[2].gridx = i;
			attgLayout[2].setConstraints(dayLabel[i], attgbc[2]);
		}

		if(CheckOS.isWindows()){
			attgbc[1].ipadx = 1;	//+0ピクセル これで最小のXをでかくできる
		}else{// if(CheckOS.isLinux()){
			attgbc[1].ipadx = 5;
		}
		attgbc[1].ipady = 6;	//+6ピクセル これで最小のYをでかくできる
		for(int i=0;i<numSize;i++){
			for(int j=0;j<maxDate;j++){
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
				attgbc[1].gridx = j;
				attgbc[1].gridy = i;
				attgLayout[1].setConstraints(attButton[i][j], attgbc[1]);
			}
		}

		if(maxDate < 31){
			if(CheckOS.isWindows()){
				attgbc[2].ipadx = 40;
				attgbc[2].ipady = 12;
			}else{// if(CheckOS.isLinux()){
				attgbc[2].ipadx = 38;
				attgbc[2].ipady = 15;
			}
			for(int i=maxDate;i<31;i++){
				dayLabel[i].setText("/");
				attgbc[2].gridx = i;
				attgLayout[2].setConstraints(dayLabel[i], attgbc[2]);
			}

			if(CheckOS.isWindows()){
				attgbc[1].ipadx = 8;
			}else{// if(CheckOS.isLinux()){
				attgbc[1].ipadx = 6;
			}
			attgbc[1].ipady = 6;
			for (int i=0;i<numSize;i++){
				for(int j=maxDate;j<31;j++){
					attButton[i][j].setText("/");
					attButton[i][j].setForeground(Color.DARK_GRAY);
					attButton[i][j].setBackground(Color.WHITE);
					attgbc[1].gridx = j;
					attgbc[1].gridy = i;
					attgLayout[1].setConstraints(attButton[i][j], attgbc[1]);
				}
			}
		}
	}

	private void attReset(){
		// アカウントの増減の時はattUpdate();を実行する
		for(int i=0;i<3;i++){
			attgLayout[i] = new GridBagLayout();
			attgbc[i] = new GridBagConstraints();
		}
		IDPanel = new JPanel(attgLayout[0]);
		attPanel = new JPanel(attgLayout[1]);
		aDayPanel = new JPanel(attgLayout[2]);

		idLabel = new JLabel[numSize];
		attButton = new JButton[numSize][31];
		attgbc[0].gridx = 0;
		int ix=0;
		if(CheckOS.isWindows()){
			ix = 13;
		}else{// if(CheckOS.isLinux()){
			ix = 3;
		}
		attgbc[0].ipadx = ix;
		attgbc[0].ipady = 14;
		for(int i=0;i<numSize;i++){	// s12500
			idLabel[i] = new JLabel(slaves.get(i));
			idLabel[i].setHorizontalAlignment(JLabel.CENTER);
			idLabel[i].setForeground(Color.WHITE);
			idLabel[i].setOpaque(true);
			idLabel[i].setBackground(Color.GRAY);
			idLabel[i].setBorder(new LineBorder(Color.DARK_GRAY, 1, true));
			attgbc[0].gridy = i;
			attgLayout[0].setConstraints(idLabel[i], attgbc[0]);
			for(int j=0;j<31;j++){
				attButton[i][j] = new JButton();
				attButton[i][j].setBounds(0,0,50,40);
				attButton[i][j].setBackground(Color.WHITE);
				attButton[i][j].addActionListener(this);
				attButton[i][j].addKeyListener(this);
				attButton[i][j].setActionCommand("attButton"+i+j);
			}
		}

		for(int i=0;i<dayLabel.length;i++){// 1 - 31
			dayLabel[i] = new JLabel("00");
			dayLabel[i].setBounds(0,0,50,40);
			dayLabel[i].setHorizontalAlignment(JLabel.CENTER);
			dayLabel[i].setOpaque(true);
			dayLabel[i].setBackground(Color.YELLOW);
			dayLabel[i].setBorder(new LineBorder(Color.GRAY, 1, true));
		}

		attendCalendar();// カレンダー作成用

		for(int i=0;i<numSize;i++){
			IDPanel.add(idLabel[i]);
			for(int j=0;j<31;j++){
				attPanel.add(attButton[i][j]);/*カレンダーボタン追加*/
			}
		}
		for(int i=0;i<dayLabel.length;i++){
			aDayPanel.add(dayLabel[i]);
		}

		IDScrollPanel.setViewportView(IDPanel);
		dayScrollPanel.setViewportView(aDayPanel);
		attScrollPanel.setViewportView(attPanel);
	}

/*	private ChangeListener cl = new ChangeListener() {
		private boolean adjflg;
		public void stateChanged(ChangeEvent e) {
			JViewport src = null;
			JViewport tgt = null;
			if (e.getSource() == attScrollPanel.getViewport()) {
				src = attScrollPanel.getViewport();
				tgt = IDScrollPanel.getViewport();
			} else if (e.getSource() == IDScrollPanel.getViewport()) {
				src = IDScrollPanel.getViewport();
				tgt = attScrollPanel.getViewport();
			}
			if (adjflg || tgt == null || src == null) {
				return;
			}
			adjflg = true;
			Dimension dim1 = src.getViewSize();
			Dimension siz1 = src.getSize();
			Point pnt1 = src.getViewPosition();
			Dimension dim2 = tgt.getViewSize();
			Dimension siz2 = tgt.getSize();
			//Point pnt2 = tgt.getViewPosition();
			double d;
			d = pnt1.getY() / (dim1.getHeight() - siz1.getHeight())
					* (dim2.getHeight() - siz2.getHeight());
			pnt1.y = (int) d;
			d = pnt1.getX() / (dim1.getWidth() - siz1.getWidth())
					* (dim2.getWidth() - siz2.getWidth());
			pnt1.x = (int) d;
			tgt.setViewPosition(pnt1);
			adjflg = false;
		}
	};*/

	private void Report(){
		panelNum[1] = new JPanel();
		panelNum[1].setLayout(null);
		labelNum[1] = new JLabel("報告書管理");
		labelNum[1].setBounds(0,10,800,40);
		labelNum[1].setFont(new Font(null, Font.PLAIN, 20));
		labelNum[1].setHorizontalAlignment(JLabel.CENTER);
		Border border = new EmptyBorder(0,0,0,0);
		repoScrollPanel = new JScrollPane();
		repoScrollPanel.setBounds(80, 100, 650, 300);
		repoScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		repoScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		repoScrollPanel.setBorder(border);

		repoReset();

		panelNum[1].add(labelNum[1]);
		panelNum[1].add(repoScrollPanel);
	}

	private void repoReset() {
		GridBagLayout gLayout = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		repoPanel = new JPanel(gLayout);
		gbc.gridx = 0;
		gbc.ipadx = 0;
		if(CheckOS.isWindows()){
			gbc.ipady = 8;
		}else{// if(CheckOS.isLinux()){
			gbc.ipady = 13;
		}
		rStudentsButton = new JButton[numSize];
		for(int i=0;i<numSize;i++){
			gbc.gridy = i;
			rStudentsButton[i] = new JButton();
			rStudentsButton[i].setPreferredSize(new Dimension(300, 30));
			rStudentsButton[i].setBackground(Color.WHITE);
			rStudentsButton[i].setText(slaves.get(i));
			rStudentsButton[i].setFont(new Font(null, Font.PLAIN, 14));
			gLayout.setConstraints(rStudentsButton[i], gbc);
			rStudentsButton[i].addActionListener(this);
			rStudentsButton[i].addKeyListener(this);
			rStudentsButton[i].setActionCommand("rStudents"+i);
		}

		int nx=128, yx=149;
		if(CheckOS.isWindows()){
			nx = 128;
			yx = 149;
		}
		gbc.gridx = 1;
		if(CheckOS.isWindows()){
			gbc.ipady = 15;
		}else{// if(CheckOS.isLinux()){
			gbc.ipady = 24;
		}
		updateLabel  = new JLabel[numSize];
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年 MM月 dd日");
		for(int i=0;i<numSize;i++){
			gbc.gridy = i;
			gbc.ipadx = nx;
			updateLabel[i] = new JLabel("ファイルが存在しません");
			updateLabel[i].setHorizontalAlignment(JLabel.CENTER);
			updateLabel[i].setFont(new Font(null, Font.PLAIN, 16));
			updateLabel[i].setBackground(Color.WHITE);
			updateLabel[i].setBorder(new LineBorder(Color.LIGHT_GRAY,1,true));
			updateLabel[i].setOpaque(true);
			if(controller.getLastUploadDate(slaves.get(i)) != null){
				LocalDate LastUpdate = controller.getLastUploadDate(slaves.get(i));
				String update = LastUpdate.format(formatter);
				updateLabel[i].setText(update);
				gbc.ipadx = yx;
			}
			gLayout.setConstraints(updateLabel[i], gbc);
		}

		for(int i=0;i<numSize;i++){
			repoPanel.add(rStudentsButton[i]);
			repoPanel.add(updateLabel[i]);
		}
		repoScrollPanel.setViewportView(repoPanel);
	}

	private void Plan(){
		panelNum[2] = new JPanel();
		panelNum[2].setLayout(null);
		labelNum[2] = new JLabel("予定確認");
		labelNum[2].setBounds(0,10,800,40);
		labelNum[2].setFont(new Font(null, Font.PLAIN, 20));
		labelNum[2].setHorizontalAlignment(JLabel.CENTER);

		ymd = new JLabel("日付を選択");
		ymd.setBounds(500,10,200,40);
		ymd.setFont(new Font(null, Font.PLAIN, 18));
		ymd.setHorizontalAlignment(JLabel.CENTER);
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
		planPanel = new JPanel();
		planPanel.setLayout(new GridLayout(7, 7));
		planPanel.setBounds(10, 120, 400, 400);

		pNextButton.setIcon(right);
		pBackButton.setIcon(left);
		pNextButton.setHorizontalTextPosition(SwingConstants.CENTER);
		pBackButton.setHorizontalTextPosition(SwingConstants.CENTER);

		pMonthLabel = new JLabel(year[1]+"年"+(month[1]+1)+"月");
		pMonthLabel.setBounds(150,70,200,40);
		pMonthLabel.setFont(new Font(null, Font.PLAIN, 24));
		pTextArea = new JTextArea(20,24);
		pTextArea.setBounds(450, 50, 300, 400);
		pTextArea.setLineWrap(true);
		addPlanButton = new JButton("予定追加");
		addPlanButton.setBounds(500,470,200,40);
		addPlanButton.setBackground(Color.WHITE);
		for(int i=0;i<7;i++){
			weekLabel[i] = new JLabel(weekName[i]);
			weekLabel[i].setHorizontalAlignment(JLabel.CENTER);
			weekLabel[i].setFont(new Font(null, Font.PLAIN, 16));
			weekLabel[i].setBackground(Color.WHITE);
			weekLabel[i].setBorder(new LineBorder(Color.DARK_GRAY,1,true));
			weekLabel[i].setOpaque(true);
		}
		weekLabel[0].setForeground(Color.RED);
		weekLabel[0].setBorder(new LineBorder(Color.RED,1,true));
		weekLabel[6].setForeground(Color.BLUE);
		weekLabel[6].setBorder(new LineBorder(Color.BLUE,1,true));

		for(int i=0;i<pDayButton.length;i++)
			pDayButton[i] = new JButton();

		planCalendar();/*カレンダーの表示*/

		for(int i=0;i<7;i++)
			planPanel.add(weekLabel[i]);
		for(int i=0;i<pDayButton.length;i++){
			planPanel.add(pDayButton[i]);//カレンダーボタン追加
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

	private void planCalendar(){
		year[1] = calendar.get(Calendar.YEAR);
		month[1] = calendar.get(Calendar.MONTH);
		calendar.set(year[1], month[1], 1);
		pMonthLabel.setText(year[1]+"年"+(month[1]+1)+"月");
		yearMonth[1] = YearMonth.of(year[1], month[1]+1);
		agenda  = controller.getAgenda(yearMonth[1]);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int maxDate = yearMonth[1].lengthOfMonth();

		for(int i=0;i<dayOfWeek;i++){
			pDayButton[i].setText("");
			pDayButton[i].setBackground(Color.WHITE);
		}
		for(int i=dayOfWeek;i<dayOfWeek+maxDate;i++){
			int day = 1+i-dayOfWeek;
			pDayButton[i].setText(""+day);
			if(i % 7 == 0){
				pDayButton[i].setForeground(Color.RED);
			}else if(i % 7 == 6){
				pDayButton[i].setForeground(Color.BLUE);
			}else{
				pDayButton[i].setForeground(Color.BLACK);
			}
			if(agenda.hasData(day-1)){
				pDayButton[i].setBackground(Color.ORANGE);
			}else
				pDayButton[i].setBackground(Color.WHITE);
		}
		for (int i=dayOfWeek+maxDate;i<pDayButton.length;i++){
			pDayButton[i].setText("");
			pDayButton[i].setBackground(Color.WHITE);
		}
	}

	private void Account(){
		panelNum[3] = new JPanel();
		panelNum[3].setLayout(null);
		labelNum[3] = new JLabel("アカウント管理");
		labelNum[3].setBounds(0,10,800,40);
		labelNum[3].setFont(new Font(null, Font.PLAIN, 20));
		labelNum[3].setHorizontalAlignment(JLabel.CENTER);

		stuNumLabel = new JLabel("編集したいIDを選択");
		stuNumLabel.setBounds(500,180,200,60);
		stuNumLabel.setFont(new Font(null, Font.PLAIN, 18));
		stuNumLabel.setHorizontalAlignment(JLabel.CENTER);
		stuNumLabel.setBackground(Color.LIGHT_GRAY);

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
		rootButton = new JButton("教員用アカウントの編集");
		rootButton.setBounds(500,350,200,80);
		rootButton.setBackground(Color.WHITE);
		rootButton.setFont(new Font(null, Font.PLAIN, 14));
		Border border = new EmptyBorder(0,0,0,0);
		accScrollPanel = new JScrollPane();
		accScrollPanel.setBounds(100,  80, 320, 400);
		accScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		accScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		accScrollPanel.setBorder(border);

		accReset();

		panelNum[3].add(labelNum[3]);
		panelNum[3].add(accScrollPanel);
		panelNum[3].add(stuNumLabel);
		panelNum[3].add(addAccButton);
		panelNum[3].add(cheAccButton);
		panelNum[3].add(delAccButton);
		panelNum[3].add(rootButton);
	}

	private void accReset(){
		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		accPanel = new JPanel(gridBagLayout);

		gbc.gridx = 0;
		gbc.gridy = 0;
		if(CheckOS.isWindows()){
			gbc.ipadx = 12;
			gbc.ipady = 12;
		}else{
			gbc.ipadx = 4;
			gbc.ipady = 16;
		}
		aStudentsButton  = new JButton[numSize];
		for(int i=0;i<numSize;i++){
			aStudentsButton[i] = new JButton();
			aStudentsButton[i].setPreferredSize(new Dimension(300, 30));
			aStudentsButton[i].setBackground(Color.WHITE);
			aStudentsButton[i].setFont(new Font(null, Font.PLAIN, 14));
			aStudentsButton[i].setText(slaves.get(i));
			gbc.gridy = i;
			gridBagLayout.setConstraints(aStudentsButton[i], gbc);
			accPanel.add(aStudentsButton[i]);
			// set listener
			aStudentsButton[i].addActionListener(this);
			aStudentsButton[i].addKeyListener(this);
			aStudentsButton[i].setActionCommand("aStudentsButton"+i);
		}

		accScrollPanel.setViewportView(accPanel);
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

	private void member(int i){
		if(i == -1){
			newAccount.showNewAccount();
		}else{
			String slaveID = slaves.get(i);
			String slaveName = controller.getName(slaveID);
			newAccount.showCheAccount(slaveID, slaveName);
		}
	}

	/**
	 * slaveとnumSizeを更新する
	 */
	private void resetSlave(){
		slaves = Slave.getSlaves();
		numSize = Slave.getSlaves().size();
	}

	public void UpdateAccount() {
		resetSlave();
		attendUpdate();
		reportUpdate();
		accountUpdate();
	}

	private void attendUpdate() {
		IDPanel.removeAll();
		attPanel.removeAll();
		IDScrollPanel.remove(IDPanel);
		attScrollPanel.remove(attPanel);
		attReset();
		panelNum[0].repaint();
	}

	private void reportUpdate(){
		repoPanel.removeAll();
		repoScrollPanel.remove(repoPanel);
		repoReset();
		panelNum[1].repaint();
	}

	private void accountUpdate(){
		accPanel.removeAll();
		accScrollPanel.remove(accPanel);
		accReset();
		panelNum[3].repaint();
	}

	private void actionButton(){
		// main
		mainFrame.addWindowListener(this);
		for(int i=0;i<5;i++){
			numButton[i].addActionListener(this);
			numButton[i].addKeyListener(this);
		}

		// attend
		aNextButton.addActionListener(this);
		aNextButton.addKeyListener(this);
		aBackButton.addActionListener(this);
		aBackButton.addKeyListener(this);

		//スクロールを同期する
		//IDScrollPanel.getViewport().addChangeListener(cl);
		//attScrollPanel.getViewport().addChangeListener(cl);

		// report
		/*for(int i=0;i<numSize;i++){
			rStudentsButton[i].addActionListener(this);
			rStudentsButton[i].addKeyListener(this);
			rStudentsButton[i].setActionCommand("rStudents"+i);
		}*/

		// plan
		addPlanButton.addActionListener(this);
		addPlanButton.addKeyListener(this);
		pNextButton.addActionListener(this);
		pNextButton.addKeyListener(this);
		pBackButton.addActionListener(this);
		pBackButton.addKeyListener(this);
		for(int i=0;i<pDayButton.length;i++){
			pDayButton[i].addActionListener(this);
			pDayButton[i].addKeyListener(this);
			pDayButton[i].setActionCommand("pDayButton"+i);
		}

		// account
		addAccButton.addActionListener(this);
		addAccButton.addKeyListener(this);
		cheAccButton.addActionListener(this);
		cheAccButton.addKeyListener(this);
		delAccButton.addActionListener(this);
		delAccButton.addKeyListener(this);
		rootButton.addActionListener(this);
		rootButton.addKeyListener(this);

	}

	private int memNum = -1;
	private String stuid = "";
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == numButton[0]){//出席情報
			calendar.set(Calendar.YEAR, year[0]);
			calendar.set(Calendar.MONTH, month[0]);
			cLayout.show(cardPanel, "Meth1");
		}else if(e.getSource() == numButton[1]){//報告書管理
			cLayout.show(cardPanel, "Meth2");
		}else if(e.getSource() == numButton[2]){//予定確認
			calendar.set(Calendar.YEAR, year[1]);
			calendar.set(Calendar.MONTH, month[1]);
			cLayout.show(cardPanel, "Meth3");
		}else if(e.getSource() == numButton[3]){//アカウント管理
			cLayout.show(cardPanel, "Meth4");
		}else if(e.getSource() == numButton[4]){//ログアウト
			controller.logout();
			mainFrame.setVisible(false);
			Login.loginFrame.setVisible(true);
		}else if(e.getSource() == aNextButton){
			calendar.set(Calendar.MONTH, month[0] +1);	//attendで1ヶ月増やす
			attendCalendar();
			panelNum[0].repaint();
		}else if(e.getSource() == aBackButton){
			calendar.set(Calendar.MONTH, month[0] -1);	//attendで1ヶ月減らす
			attendCalendar();
			panelNum[0].repaint();
		}else if(e.getActionCommand().matches("attButton" + ".*")){	//出欠情報の変更機能
			for(int i=0;i<numSize;i++){	//ID用の i
				for(int j=0;j<31;j++){	//日付用の j
					if(e.getSource()==attButton[i][j]){
						String mon = String.format("%1$02d", month[0]+1);
						String day = String.format("%1$02d", j+1);
						String chengeDay = year[0]+"-" +mon+"-"+day;
						if(attButton[i][j].getText().equals("出")){
							controller.changeAttendance(LocalDate.parse(chengeDay)
									, slaves.get(i), AttendanceBook.ABSENCE);
						}else if(attButton[i][j].getText().equals("欠")){
							controller.changeAttendance(LocalDate.parse(chengeDay)
									, slaves.get(i), AttendanceBook.AUTHORIZED_ABSENCE);
						}else if(attButton[i][j].getText().equals("公")){
							controller.changeAttendance(LocalDate.parse(chengeDay)
									, slaves.get(i), AttendanceBook.ATTENDED);
						}
						attendCalendar();
						panelNum[0].repaint();
					}
				}
			}
		}else if(e.getActionCommand().matches("rStudents" + ".*")){//報告書管理
			String user = null;
			for(int i=0;i<numSize;i++){
				if(e.getSource() == rStudentsButton[i]){
					user =  slaves.get(i);
				}
			}
			controller.showReport(user);
		}else if(e.getSource() == pNextButton){
			calendar.set(Calendar.MONTH, month[1]+1);	//planで1ヶ月増やす
			planCalendar();
			panelNum[2].repaint();
		}else if(e.getSource() == pBackButton){
			calendar.set(Calendar.MONTH, month[1]-1);	//planで1ヶ月減らす
			planCalendar();
			panelNum[2].repaint();
		}else if(e.getActionCommand().matches("pDayButton" + ".*")){/*planで日付を取得するとき*/
			for(int i=0;i<pDayButton.length;i++){
				if(e.getSource() == pDayButton[i]){
					String dayName = pDayButton[i].getText();
					if(!dayName.equals("")){
						planday = Integer.parseInt(dayName) -1;
						String plan = agenda.getData(planday);
						ymd.setText(year[1]+"年"+(month[1]+1)+"月"+(planday+1)+"日");
						pTextArea.setText(plan);
					}
					else{
						planday = -1;
						ymd.setText("日付を選択");
						pTextArea.setText("");
					}
				}
			}
		}else if(e.getSource() == addPlanButton){//予定追加機能
			if(!(planday < 0)){
				String plan = pTextArea.getText();
				controller.setAgenda(agenda, planday, plan);
				message.showMessage(ymd.getText() + "の予定を更新しました");
			}
		}else if(e.getActionCommand().matches("aStudentsButton" + ".*")){//アカウント選択
			for(int i=0;i<numSize;i++){
				if(e.getSource() == aStudentsButton[i]){
					memNum = i;
					stuNumLabel.setText(aStudentsButton[memNum].getText());// + "を");
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
			stuid = slaves.get(memNum);
			controller.deleteUser(stuid);
			message.showMessage(stuid + "を削除しました");
			stuNumLabel.setText("編集したいIDを選択");
			UpdateAccount();
		}
	}

	public void keyPressed(KeyEvent e){
		if(KeyEvent.VK_ENTER == e.getKeyCode()){/*actionevebtを呼び出し*/
			ActionEvent actionEvents = new ActionEvent(e.getComponent(),ActionEvent.ACTION_PERFORMED,"");
			actionPerformed(actionEvents);
		}
	}

	public void windowOpened(WindowEvent e){		// ウィンドウが可視化したとき
		// TODO 自動生成されたメソッド・スタブ
	}

	public void windowClosing(WindowEvent e){		// ウィンドウを閉じようとしたとき
		// TODO 自動生成されたメソッド・スタブ
	}

	public void windowClosed(WindowEvent e){		// ウィンドウをdisposeの結果閉じたとき
		// TODO 自動生成されたメソッド・スタブ
	}

	public void windowIconified(WindowEvent e){	// ウィンドウを最小化したとき
		// TODO 自動生成されたメソッド・スタブ
	}

	public void windowDeiconified(WindowEvent e){	// ウィンドウを最小化から復帰したとき
		// TODO 自動生成されたメソッド・スタブ
	}

	public void windowActivated(WindowEvent e) {	// ウィンドウがアクティブになったとき
		UpdateAccount();
	}

	public void windowDeactivated(WindowEvent e) {	// ウィンドウをアクティブでなくしたとき
		// TODO 自動生成されたメソッド・スタブ
	}
}
