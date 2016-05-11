package system;

import java.lang.String;
import java.util.Calendar;

public class Controller {
	private User user; // ログインしているユーザ

	public Controller() {
	}

	// ログイン
	public int login(String id, String passwd) {
		user = User.login(id, passwd);
		if (user == null) {
			return 1;
		} else {
			attend();
			return 0;
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
		// 属性がslaveなら出席
		if (user.getAttribute().equals("slave")) {
			return user.setAttendance();
		}
		return 1;
	}

	// 出席の取得
	public String[][] getAttendance(Calendar calendar) {
		return user.getAttendance(calendar);
	}

	// TODO:アカウント設定
	public int setAccount(String id, String passwd) {
		return 0;
	}
}
