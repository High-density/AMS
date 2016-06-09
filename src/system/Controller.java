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
	public boolean logout() {
		user = null;
		return true;
	}

	// 報告書の提出
	public boolean submitReport(String file) {
		return user.submitReport(file);
	}

	// 報告書の閲覧
	public boolean showReport() {
		return user.showReport();
	}
	public boolean showReport(String id) {
		return user.showReport(id);
	}

	// 出席の登録
	private boolean attend() {
		// 属性がSlaveなら出席
		if (user.getAttribute().equals(Slave.class.getSimpleName())) {
			return user.setAttendance();
		}
		return false;
	}

	// 出席の取得
	public AttendanceBook[] getAttendance(YearMonth ym) {
		return user.getAttendance(ym);
	}

	// アカウント設定
	public boolean setAccount(AccountInformation oldAccount, AccountInformation newAccount) {
		return user.setAccount(oldAccount, newAccount);
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

    // ファイルまたはディレクトリの削除
    public static void deleteFile(File f) {
        if (f.exists() == false) {
            return;
        }
        if(f.isFile()) {
            f.delete();
        } else if(f.isDirectory()) {
            File files[] = f.listFiles();
            for(int i = 0; i < files.length; i++) {
                deleteFile(files[i]);
            }
            f.delete();
        }
    }
}
