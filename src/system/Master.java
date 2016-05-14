package system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.String;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.lang.String;
import java.lang.System;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Master extends User {
	public Master(String id, String passwd) {
		super(id, passwd);
		attribute = "master";
	}

	// Masterは出席不可
	public int setAttendance() {
		return 1;
	}

	// 全Slaveから出席取得
	public String[][] getAttendance(Calendar calendar) {
		String attendanceBook[][]; // 出席情報

		// Slaveのidを一旦格納
		ArrayList<String> slaves = new ArrayList<String>();
		try {
			File file = new File("./file/user");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null){
				Pattern p = Pattern.compile("([0-9a-z]+):([a-z]+)$");
				Matcher m = p.matcher(line);
				if (m.find()){
                    // 属性がSlaveのものだけを格納
					if (m.group(2).equals("slave")) {
						slaves.add(m.group(1));
					}
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		} catch (NullPointerException e) {
			System.out.println(e);
		}

		// 取得したidのそれぞれの出席を取得
		attendanceBook = new String[slaves.size()][];
		for (int s = 0; s < slaves.size(); s++) {
			attendanceBook[s] = User.getAttendance(slaves.get(s), calendar);
		}

		return attendanceBook;
	}

	// Masterは報告書提出不可
	public int submitReport() {
		return 1;
	}

	// TODO:報告書閲覧
	public int showReport() {
		return 0;
	}

	// TODO:マスターからの情報発信
	public int setEvent() {
		return 0;
	}

	// TODO:ユーザの作成
	public int createUser() {
		return 0;
	}
}
