package system;

import java.lang.String;
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

	// TODO:報告書の提出
	public int submitReport(/*id, date*/) {
		return 0;
	}

	// TODO:報告書の閲覧
	public int showReport(/* date */) {
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
}
