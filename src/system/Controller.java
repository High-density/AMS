package system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.channels.FileChannel;
import java.lang.String;
import java.time.LocalDate;
import java.time.YearMonth;

public class Controller {
	private User user; // ログインしているユーザ

	public Controller() {
	}

	// ログイン
	public boolean login(String id, String passwd) {
		user = User.login(id, passwd);
		if (user == null) {
			// ログイン失敗
			return false;
		} else {
			// ログイン成功したら出席チェック
			attend();
			return true;
		}
	}

	// ログアウト
	public int logout() {
		user = null;
		return 0;
	}

	// 報告書の提出
	public boolean submitReport(String file) {
		return user.submitReport(file);
	}

	// TODO:報告書の閲覧
	public int showReport(LocalDate ld) {
		return 0;
	}

	// 出席の登録
	private int attend() {
		// 属性がSlaveなら出席
		if (user.getAttribute().equals(Slave.class.getSimpleName())) {
			return user.setAttendance();
		}
		return 1;
	}

	// 出席の取得
	public AttendanceBook[] getAttendance(YearMonth ym) {
		return user.getAttendance(ym);
	}

	// TODO:アカウント設定
	public int setAccount(String id, String passwd) {
		return 0;
	}

	// ディレクトリがないときに作成
	public static boolean mkdirs(String dir) {
		File directory = new File(dir);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		return true;
	}

	// ファイルの拡張子取得
	public static String getSuffixWithDot(String fileName) {
		if (fileName == null) {
			return null;
		}
		int point = fileName.lastIndexOf(".");
		if (point != -1) {
			return fileName.substring(point);
		}
		return null;
	}

	// ファイルのコピー
	public static boolean copyFile(File in, File out) throws IOException {
		try {
			FileChannel inChannel = new FileInputStream(in).getChannel();
			FileChannel outChannel = new FileOutputStream(out).getChannel();
			inChannel.transferTo(0, inChannel.size(), outChannel);
			inChannel.close();
			outChannel.close();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			throw e;
		}
	}
}
