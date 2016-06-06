package system;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.String;
import java.time.LocalDate;
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
			// 出席ディレクトリの作成
			String dirName = "./file/" + getId() + "/attendance/";
			if (!Controller.mkdirs(dirName)) {
				return 1;
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

	// 報告書提出
	public boolean submitReport(String file) {
		// 報告書ディレクトリの作成
		if (!Controller.mkdirs("./file/" + getId() + "/report/")) {
			return false;
		}

		// コピー先ファイル名の決定
		String outFileName = "file/" + getId() + "/report/" + LocalDate.now().toString();
		String suffix = Controller.getSuffixWithDot(file);
		if (suffix != null) {
			// 元ファイルに拡張子がついていれば，コピーファイルにもつける
			outFileName += suffix;
		}

		// ファイルのコピー
		File inFile = new File(file);
		File outFile = new File(outFileName);
		try {
			Controller.copyFile(inFile, outFile);
		} catch (IOException e) {
			Log.error(e);
		}

		return false;
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
