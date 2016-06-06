package system;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Runtime;
import java.lang.String;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		String dirName = "file/" + getId() + "/report/"; // 報告書ディレクトリの名前
		File dir = new File(dirName); // 報告書ディレクトリ
		String today = LocalDate.now().toString(); // 今日の日付

		// 報告書ディレクトリの作成
		if (!Controller.mkdirs(dirName)) {
			return false;
		}

		// ディレクトリ内を検索し，その日に報告書が出てたら再提出不可
		Pattern p = Pattern.compile("^" + today);
		for (String fileName: dir.list()) {
			Matcher m = p.matcher(fileName);
			if (m.find()) {
				Log.popup("本日分の報告書は提出済みです．\n更新したい場合は管理者に問い合わせてください．");
				return false;
			}
		}

		// コピー先ファイル名の決定
		String outFileName = dirName + today;
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

	// 報告書閲覧
	public boolean showReport() {
		return showReport(getId());
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
