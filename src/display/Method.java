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
import java.io.File;
import java.time.YearMonth;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import system.Agenda;
import system.AttendanceBook;
import system.CheckOS;
import system.Controller;

class Method extends KeyAdapter implements ActionListener{// 機能選択クラス
	// main
	private Controller controller; // 内部動作用
	private ChangePassword changePassword; // パスワード変更用
	static JFrame mainFrame;
	private Container contentPane;
	private JPanel panelButton;
	private JButton numButton[] = new JButton[5];
	private JPanel cardPanel;
	private CardLayout cLayout;
	private JPanel panelNum[] = new JPanel[4];
	private JLabel labelNum[] = new JLabel[4];

	// attend
	private JPanel calPanel;
	private JButton aNextButton;
	private JButton aBackButton;
	private JLabel aMonthLabel;
	private JLabel[] weekLabel_att = new JLabel[7];
	private JLabel[] attDayLabel = new JLabel[42];

	// report
	private JButton referButton;
	private JButton upButton;
	private JTextField pathTextField;
	private JFileChooser filechooser;

	// plan
	private JPanel planPanel;
	private JLabel pMonthLabel;
	private JButton pNextButton;
	private JButton pBackButton;
	private JLabel weekLabel_plan[] = new JLabel[7];
	private JButton dayButton_plan[] = new JButton[42];
	private JLabel planDate; 		// 予定取得日
	private JTextArea pTextArea; 	// 予定表示
	private Agenda agenda;
	private int pday = -1; 		// ボタンを押した時の数字から日付

	// account
	private JLabel IDLabel;
	private JLabel ID_Mine;
	private JLabel usrLabel;
	private JLabel usr_Mine;
	private JButton ChangeButton;

	// some
	private final String weekName[] = {"日","月","火","水","木","金","土"};
	private YearMonth[] yearMonth = new YearMonth[2];
	private Calendar calendar = Calendar.getInstance();
	private int year[] = {calendar.get(Calendar.YEAR),calendar.get(Calendar.YEAR)}; // 年
	private int month[] = {calendar.get(Calendar.MONTH),calendar.get(Calendar.MONTH)}; // 月
	private String myID; // ログインしたIDを引き継ぎ

	Method(system.Controller controller){
		/* システム引き継ぎ */
		this.controller = controller;
		myID = controller.getLoginUserId();
		changePassword = new display.ChangePassword(controller);

		/* メインフレーム設定 */
		mainFrame = new JFrame("-学生- 機能選択");
		mainFrame.setBounds(0, 0, 800, 600);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);
		contentPane = mainFrame.getContentPane();

		/* 各種設定*/
		PanelButton();	// 機能選択ボタンの追加
		Attendance();	// 出欠席用パネル設定
		Report();		// 報告書用パネル設定
		Plan();			// 予定用
		Account();		// アカウント用
		CardPanel();	// パネル

		/* ボタンのアクション用 */
		actionButton();

		/* フレームに追加 */
		contentPane.add(panelButton, BorderLayout.NORTH);
		contentPane.add(cardPanel, BorderLayout.CENTER);
		mainFrame.setVisible(true);

		/*アイコンの設定*/
		try{
		java.net.URL url = getClass().getClassLoader().getResource("src/icon/icon.png");
		ImageIcon icon = new ImageIcon(url);
		mainFrame.setIconImage(icon.getImage());
		}catch(Exception e){
			ImageIcon icorn = new ImageIcon("src/icon/icon.png");
			mainFrame.setIconImage(icorn.getImage());
		}

	}

	private void PanelButton(){
		panelButton = new JPanel(new GridLayout(1,5));
		panelButton.setPreferredSize(new Dimension(800, 40));
		numButton[0] = new JButton("出席確認");
		numButton[1] = new JButton("報告書アップローダ");
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
		calPanel.setBounds(200,110,400,400);
		aNextButton = new JButton();
		aNextButton.setBounds(510,50,140,60);
		aNextButton.setContentAreaFilled(false);
		aNextButton.setBorderPainted(false);
		aBackButton = new JButton();
		aBackButton.setBounds(150,50,140,60);
		aBackButton.setContentAreaFilled(false);
		aBackButton.setBorderPainted(false);
		try{
			java.net.URL url = getClass().getClassLoader().getResource("src/icon/right.png");
			ImageIcon right = new ImageIcon(url);
		aNextButton.setIcon(right);
		}catch(Exception e){
			ImageIcon right = new ImageIcon("src/icon/right.png");
			aNextButton.setIcon(right);
		}
		try{
			java.net.URL url = getClass().getClassLoader().getResource("src/icon/left.png");
			ImageIcon left = new ImageIcon(url);
			aBackButton.setIcon(left);
		}catch(Exception e){
			ImageIcon left = new ImageIcon("src/icon/left.png");
			aBackButton.setIcon(left);
		}
		aBackButton.setHorizontalTextPosition(SwingConstants.CENTER);
		aNextButton.setHorizontalTextPosition(SwingConstants.CENTER);
		labelNum[0] = new JLabel("出席確認");
		labelNum[0].setFont(new Font(null, Font.BOLD, 20));
		labelNum[0].setHorizontalAlignment(JLabel.CENTER);
		labelNum[0].setBounds(0,10,800,40);

		aMonthLabel = new JLabel(year+"年"+month+"月");
		aMonthLabel.setBounds(0,60,800,40);
		aMonthLabel.setHorizontalAlignment(JLabel.CENTER);
		aMonthLabel.setFont(new Font(null, Font.BOLD, 24));
		for(int i=0;i<7;i++){
			weekLabel_att[i] = new JLabel(weekName[i]);
			weekLabel_att[i].setFont(new Font(null, Font.BOLD, 20));
			weekLabel_att[i].setHorizontalAlignment(JLabel.CENTER);
			weekLabel_att[i].setBackground(Color.WHITE);
			weekLabel_att[i].setBorder(new LineBorder(Color.BLACK,1,true));
			weekLabel_att[i].setOpaque(true);
			if( i == 0)
				weekLabel_att[i].setForeground(Color.RED);
			if( i == 6)
				weekLabel_att[i].setForeground(Color.BLUE);
		}

		for(int i=0;i<42;i++){
			attDayLabel[i] = new JLabel();
			attDayLabel[i].setOpaque(true);
			attDayLabel[i].setBorder(new LineBorder(Color.GRAY, 1, true));
			attDayLabel[i].setFont(new Font(null, Font.PLAIN, 14));
			attDayLabel[i].setHorizontalAlignment(JLabel.CENTER);
		}

		attendCalendar();

		for(int i=0;i<7;i++){
			calPanel.add(weekLabel_att[i]);
		}
		for(int i=0;i<42;i++){
			calPanel.add(attDayLabel[i]);//カレンダーボタン追加
		}

		panelNum[0].add(labelNum[0]);
		panelNum[0].add(calPanel);
		panelNum[0].add(aNextButton);
		panelNum[0].add(aBackButton);
		panelNum[0].add(aMonthLabel);
	}

	private void attendCalendar(){
		year[0] = calendar.get(Calendar.YEAR);
		month[0] = calendar.get(Calendar.MONTH);
		aMonthLabel.setText(year[0]+"年"+(month[0]+1)+"月");
		aMonthLabel.setFont(new Font(null, Font.BOLD, 20));
		calendar.set(year[0], month[0], 1);
		yearMonth[0] = YearMonth.of(year[0], month[0]+1);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int maxDate = yearMonth[0].lengthOfMonth();
		AttendanceBook[] Book = controller.getAttendance(yearMonth[0]);
		int status[] = new int [maxDate];
		for(int i=0;i<maxDate;i++){
			status[i] = Book[0].getData(i);
		}
		for(int i=0;i<dayOfWeek;i++){
			attDayLabel[i].setText("");
			attDayLabel[i].setBackground(Color.WHITE);
		}
		for(int i=dayOfWeek;i<dayOfWeek+maxDate;i++){
			int j = i - dayOfWeek;
			attDayLabel[i].setText(""+(1+j));
			if(status[j] == AttendanceBook.ATTENDED){
				attDayLabel[i].setBackground(new Color(198 ,237 ,254));
				attDayLabel[i].setForeground(Color.DARK_GRAY);
				attDayLabel[i].setFont(new Font(null, Font.BOLD, 20));
			}else if(status[j] == AttendanceBook.ABSENCE){
				attDayLabel[i].setBackground(new Color(254 ,200 ,199));;
				attDayLabel[i].setForeground(Color.DARK_GRAY);
				attDayLabel[i].setFont(new Font(null, Font.BOLD, 20));
			}else if(status[j] == AttendanceBook.AUTHORIZED_ABSENCE){
				attDayLabel[i].setBackground(Color.GREEN);
				attDayLabel[i].setForeground(Color.DARK_GRAY);
				attDayLabel[i].setFont(new Font(null, Font.BOLD, 20));
			}else if(status[j] == AttendanceBook.NO_MARK){ // TODO: status[j]でNO_MARKが返ってきていない
			    attDayLabel[i].setBackground(Color.WHITE);
			    attDayLabel[i].setForeground(Color.DARK_GRAY);
			    attDayLabel[i].setFont(new Font(null, Font.BOLD, 20));
			}
			if(i % 7 == 0)
				attDayLabel[i].setForeground(Color.RED);
			else if(i % 7 == 6)
				attDayLabel[i].setForeground(Color.BLUE);
			else
				attDayLabel[i].setForeground(Color.BLACK);
		}
		for (int i=dayOfWeek+maxDate;i<42;i++){
			attDayLabel[i].setText("");
			attDayLabel[i].setBackground(Color.WHITE);
		}
	}

	private void Report(){
		panelNum[1] = new JPanel();
		panelNum[1].setLayout(null);
		labelNum[1] = new JLabel("報告書アップローダ");
		labelNum[1].setBounds(0,10,800,40);
		labelNum[1].setFont(new Font(null, Font.BOLD, 20));
		labelNum[1].setHorizontalAlignment(JLabel.CENTER);
		referButton  = new JButton("参照");
		referButton.setBounds(500,100,150,50);
		referButton.setBackground(Color.WHITE);
		upButton = new JButton("アップロード");
		upButton.setBounds(300,200,200,60);
		upButton.setBackground(Color.WHITE);
		try{
			java.net.URL url = getClass().getClassLoader().getResource("src/icon/upload.png");
			ImageIcon upload = new ImageIcon(url);
			upButton.setIcon(upload);
			}catch(Exception e){
				ImageIcon upload = new ImageIcon("src/icon/upload.png");
				upButton.setIcon(upload);
			}
		pathTextField = new JTextField("ファイルを参照してください");
		pathTextField.setBounds(200,100,300,51);
		pathTextField.setFont(new Font(null, Font.PLAIN, 14));

		File dir;
		dir = new File(System.getProperty("user.home") + "/Documents");
		filechooser = new JFileChooser(dir);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("ドキュメントファイル(pdf,docx,doc)", "pdf", "docx", "doc");
		filechooser.addChoosableFileFilter(filter); // ファイルのフィルタを設定
		filechooser.setAcceptAllFileFilterUsed(true); // すべてのファイルを使うか

		panelNum[1].add(labelNum[1]);
		panelNum[1].add(pathTextField);
		panelNum[1].add(referButton);
		panelNum[1].add(upButton);
	}

	private void Plan(){
		panelNum[2] = new JPanel();
		panelNum[2].setLayout(null);
		planPanel = new JPanel();
		planPanel.setLayout(new GridLayout(7, 7));
		planPanel.setBounds(20, 100, 400, 400);
		labelNum[2] = new JLabel("予定確認");
		labelNum[2].setBounds(0,10,800,40);
		labelNum[2].setFont(new Font(null, Font.BOLD, 20));
		labelNum[2].setHorizontalAlignment(JLabel.CENTER);
		pMonthLabel = new JLabel(year[1]+"年"+(month[1]+1)+"月");
		pMonthLabel.setBounds(160,50,200,40);
		pMonthLabel.setFont(new Font(null, Font.PLAIN, 24));
		planDate = new JLabel("指定された日付");
		planDate.setBounds(520,50,200,40);
		planDate.setFont(new Font(null, Font.PLAIN, 24));
		planDate.setBackground(Color.WHITE);
		pTextArea = new JTextArea(20,24);
		pTextArea.setBounds(450, 100, 300, 400);
		pTextArea.setLineWrap(true);
		pTextArea.setEditable(false);
		pNextButton = new JButton();
		pNextButton.setBounds(300,50,100,40);
		pNextButton.setContentAreaFilled(false);
		pNextButton.setBorderPainted(false);
		pBackButton = new JButton();
		pBackButton.setBounds(040,50,100,40);
		pBackButton.setContentAreaFilled(false);
		pBackButton.setBorderPainted(false);
		//ボタンへのiconの設置
		try{
			java.net.URL url = getClass().getClassLoader().getResource("src/icon/right_mini.png");
			ImageIcon right = new ImageIcon(url);
			pNextButton.setIcon(right);
		}catch(Exception e){
			ImageIcon right = new ImageIcon("src/icon/right_mini.png");
			pNextButton.setIcon(right);
		}
		try{
			java.net.URL url = getClass().getClassLoader().getResource("src/icon/left_mini.png");
			ImageIcon left = new ImageIcon(url);
			pBackButton.setIcon(left);
		}catch(Exception e){
			ImageIcon left = new ImageIcon("src/icon/left_mini.png");
			pBackButton.setIcon(left);
		}
		pNextButton.setHorizontalTextPosition(SwingConstants.CENTER);
		pBackButton.setHorizontalTextPosition(SwingConstants.CENTER);



		for(int i=0;i<7;i++){
			weekLabel_plan[i] = new JLabel(weekName[i]);
			weekLabel_plan[i].setFont(new Font(null, Font.BOLD, 20));
			weekLabel_plan[i].setHorizontalAlignment(JLabel.CENTER);
			weekLabel_plan[i].setBackground(Color.ORANGE);
			weekLabel_plan[i].setBorder(new LineBorder(Color.BLACK,1,true));
			weekLabel_plan[i].setOpaque(true);
			weekLabel_plan[i].setBackground(Color.WHITE);
		}
		weekLabel_plan[0].setForeground(Color.RED);
		weekLabel_plan[0].setBorder(new LineBorder(Color.RED,1,true));
		weekLabel_plan[6].setForeground(Color.BLUE);
		weekLabel_plan[6].setBorder(new LineBorder(Color.BLUE,1,true));
		for(int i=0;i<42;i++){
			dayButton_plan[i] = new JButton();
		}

		planCalendar();/*カレンダーの表示*/

		for(int i=0;i<7;i++)
			planPanel.add(weekLabel_plan[i]);
		for(int i=0;i<42;i++){
			planPanel.add(dayButton_plan[i]);//カレンダーボタン追加
		}

		panelNum[2].add(planDate);
		panelNum[2].add(labelNum[2]);
		panelNum[2].add(pMonthLabel);
		panelNum[2].add(pNextButton);
		panelNum[2].add(pBackButton);
		panelNum[2].add(planPanel);
		panelNum[2].add(pTextArea);
	}

	private void planCalendar(){
		year[1] = calendar.get(Calendar.YEAR);
		month[1] = calendar.get(Calendar.MONTH);
		pMonthLabel.setText(year[1]+"年"+(month[1]+1)+"月");
		calendar.set(year[1], month[1], 1);
		yearMonth[1] = YearMonth.of(year[1], month[1]+1);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int maxDate = yearMonth[0].lengthOfMonth();

		agenda  = controller.getAgenda(yearMonth[1]);
		for(int i=0;i<dayOfWeek;i++){
			dayButton_plan[i].setText("");
			dayButton_plan[i].setBackground(Color.WHITE);
		}

		int fontSize = 16;
		if(CheckOS.isWindows()){
			fontSize = 20;
		}
		for(int i=dayOfWeek;i<dayOfWeek+maxDate;i++){
			int day = 1 + i - dayOfWeek;
			dayButton_plan[i].setText(""+day);
			dayButton_plan[i].setBackground(Color.WHITE);
			dayButton_plan[i].setFont(new Font(null, Font.BOLD, fontSize));
			if(i % 7 == 0)
				dayButton_plan[i].setForeground(Color.RED);
			else if(i % 7 == 6)
				dayButton_plan[i].setForeground(Color.BLUE);
			else
				dayButton_plan[i].setForeground(Color.BLACK);
		}
		for (int i=dayOfWeek+maxDate;i<42;i++){
			dayButton_plan[i].setText("");
			dayButton_plan[i].setBackground(Color.WHITE);
		}
	}

	private void Account(){
		panelNum[3] = new JPanel();
		panelNum[3].setLayout(null);
		labelNum[3] = new JLabel("アカウント情報");
		labelNum[3].setBounds(0,10,800,40);
		labelNum[3].setFont(new Font(null, Font.BOLD, 20));
		labelNum[3].setHorizontalAlignment(JLabel.CENTER);
		//IDの表示
		IDLabel = new JLabel("ID");
		IDLabel.setBounds(280,130,200,40);
		IDLabel.setFont(new Font(null, Font.PLAIN, 24));
		ID_Mine = new JLabel(myID);
		ID_Mine.setBounds(380,130,200,40);
		ID_Mine.setFont(new Font(null, Font.PLAIN, 24));
		//ユーザ名の表示
		usrLabel = new JLabel("ユーザ名");
		usrLabel.setBounds(250,200,200,40);
		usrLabel.setFont(new Font(null, Font.PLAIN, 24));
		usr_Mine = new JLabel(Controller.getName(myID));
		usr_Mine.setBounds(380,200,200,40);
		usr_Mine.setFont(new Font(null, Font.PLAIN, 24));
		//アカウント情報変更ボタン
		ChangeButton = new JButton("アカウント情報の変更");
		ChangeButton.setFont(new Font(null, Font.PLAIN, 24));
		ChangeButton.setBounds(380,270,300,40);
		ChangeButton.setBackground(Color.WHITE);

		panelNum[3].add(labelNum[3]);
		panelNum[3].add(IDLabel);
		panelNum[3].add(ID_Mine);
		panelNum[3].add(usrLabel);
		panelNum[3].add(usr_Mine);
		panelNum[3].add(ChangeButton);
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
		aNextButton.addActionListener(this);
		aNextButton.addKeyListener(this);
		aBackButton.addActionListener(this);
		aBackButton.addKeyListener(this);
		pNextButton.addActionListener(this);
		pNextButton.addKeyListener(this);
		pBackButton.addActionListener(this);
		pBackButton.addKeyListener(this);
		ChangeButton.addActionListener(this);
		ChangeButton.addKeyListener(this);
		for(int i = 0;i < 42;i++){
			dayButton_plan[i].addActionListener(this);
			dayButton_plan[i].addKeyListener(this);
			dayButton_plan[i].setActionCommand("dayButton_plan"+i);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == numButton[0]){			// 機能1
			calendar.set(Calendar.YEAR, year[0]);
			calendar.set(Calendar.MONTH, month[0]);
			cLayout.show(cardPanel, "Meth1");
		}else if(e.getSource() == numButton[1]){	// 機能2
			cLayout.show(cardPanel, "Meth2");
		}else if(e.getSource() == numButton[2]){	// 機能3
			calendar.set(Calendar.YEAR, year[1]);
			calendar.set(Calendar.MONTH, month[1]);
			cLayout.show(cardPanel, "Meth3");
		}else if(e.getSource() == numButton[3]){	// 機能4
			cLayout.show(cardPanel, "Meth4");
		}else if(e.getSource() == numButton[4]){	// ログアウト
			controller.logout();
			mainFrame.setVisible(false);
			Login.loginFrame.setVisible(true);
		}else if(e.getSource() == referButton){	// ファイル参照用
			int selected = filechooser.showOpenDialog(null);//ダイアログ表示
			if (selected == JFileChooser.APPROVE_OPTION){
				File file = filechooser.getSelectedFile();
				pathTextField.setText(file.getPath());//ファイルが選ばれたらパスを表示
			}else if (selected == JFileChooser.CANCEL_OPTION){
				pathTextField.setText("キャンセルされました");
			}else if (selected == JFileChooser.ERROR_OPTION){
				pathTextField.setText("エラー又は取消しがありました");
			}
		}else if(e.getSource() == upButton){	// アップロード
			if(pathTextField.getText().equals("ファイルを参照してください") || pathTextField.getText().equals("キャンセルされました"))
				JOptionPane.showMessageDialog(mainFrame, "ファイルを選択してください");
			else{
				String mess = "";
				int opt = JOptionPane.showConfirmDialog(mainFrame, "アップロードしますか？");
				if(opt == 0){
					boolean bool = controller.submitReport(pathTextField.getText());
					if(bool){
						mess = "アップロードしました";
					}else{
						mess = "本日分の報告書は提出済みです．\n更新したい場合は管理者に問い合わせてください．";
					}
				}else{
					mess = "操作を取り消しました";
				}
				JOptionPane.showMessageDialog(mainFrame, mess);
			}
		}else if(e.getSource() == aNextButton){
			calendar.set(Calendar.MONTH, month[0] +1);	//1ヶ月増やす
			attendCalendar();
			panelNum[0].repaint();
		}else if(e.getSource() == aBackButton){
			calendar.set(Calendar.MONTH, month[0] -1);	//1ヶ月減らす
			attendCalendar();
			panelNum[0].repaint();
		}else if(e.getSource() == pNextButton){
			calendar.set(Calendar.MONTH, month[1] +1);	//1ヶ月増やす
			planCalendar();
			panelNum[2].repaint();
		}else if(e.getSource() == pBackButton){
			calendar.set(Calendar.MONTH, month[1] -1);	//1ヶ月減らす
			planCalendar();
			panelNum[2].repaint();
		}else if(e.getActionCommand().matches("dayButton_plan" + ".*")){ // 予定の取得
			for(int i=0;i<42;i++){
				if(e.getSource() == dayButton_plan[i]){
					String dayName = dayButton_plan[i].getText();
					if(!dayName.equals("")){
						pday = Integer.parseInt(dayName) -1;
						planDate.setText(year[1]+"年"+(month[1]+1)+"月"+(pday+1)+"日");
						pTextArea.setEditable(true);
						String plan = agenda.getData(pday);
						pTextArea.setText(plan);
						pTextArea.setEditable(false);
					}else{
						pday = -1;
						planDate.setText("日付なし");
					}
				}
			}
		}else if(e.getSource() == ChangeButton){ // アカウント情報の変更
			changePassword.showChangePassword(myID);
		}
	}

	public void keyPressed(KeyEvent e){
		if(KeyEvent.VK_ENTER == e.getKeyCode()){/*actionevebtを呼び出し*/
			ActionEvent actionEvents = new ActionEvent(e.getComponent(),ActionEvent.ACTION_PERFORMED,"");
			actionPerformed(actionEvents);
		}
	}
}
