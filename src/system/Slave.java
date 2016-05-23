package system;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.String;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

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
			// ディレクトリがないときには先に作成
			String dirName = "./file/" + getId() + "/attendance/";
			File directory = new File(dirName);
			if (!directory.exists()) {
				directory.mkdirs();
			}

			File file = new File(dirName + ldt.format(formatter));
			// 未出席時のみ記録
			if (!file.exists()) {
				file.createNewFile();
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
				formatter = DateTimeFormatter.ofPattern("HHmm");
				pw.println(ldt.format(formatter) + ":" + AttendanceBook.ATTENDED);
				pw.close();
			}
		} catch (IOException e) {
			Log.error(e);
			return 1;
		}
		return 0;
	}

	// 出席取得
	public AttendanceBook[] getAttendance(YearMonth ym) {
		AttendanceBook book[] = new AttendanceBook[1];
		book[0] = User.getAttendance(id, ym);
		return book;
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
