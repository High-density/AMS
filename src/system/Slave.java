package system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.String;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.System;

class Slave extends User {
	public Slave(String id, String passwd) {
		super(id, passwd);
		attribute = this.getClass().getSimpleName();
	}

	// 出席
	public int setAttendance() {
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

		// 各自のフォルダに日付ごとにファイルを作り，出席状況を格納する
		try {
			File file = new File("./file/" + getId() + "/attendance/" + ldt.format(formatter));
			// 未出席時のみ記録
			if (!file.exists()) {
				file.createNewFile();
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
				formatter = DateTimeFormatter.ofPattern("HHmm");
				pw.println(ldt.format(formatter) + ":attended");
				pw.close();
			}
		} catch (IOException e) {
			System.out.println("Slave.java:ファイル書き込みエラー: " + e);
			return 1;
		}
		return 0;
	}

	// 出席取得
	public String[][] getAttendance(YearMonth ym) {
		String attendanceBook[][] = new String[1][];
		attendanceBook[0] = User.getAttendance(id, ym);
		return attendanceBook;
	}

	// TODO:報告書提出
	public int submitReport() {
		return 0;
	}

	// TODO:報告書閲覧
	public int showReport() {
		return 0;
	}

	// Slaveは情報配信不可
	public int setEvent() {
		return 1;
	}

	// Slaveはユーザの作成不可
	public int createUser() {
		return 1;
	}
}
