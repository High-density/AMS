package system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.lang.Throwable;
import java.lang.String;
import java.time.YearMonth;
import java.util.ArrayList;

class Master extends User {
	public Master(String id, String passwd) {
		super(id, passwd);
		attribute = this.getClass().getSimpleName();
	}

	// Masterは出席不可
	public boolean setAttendance() {
		return false;
	}

	// 全Slaveから出席取得
	public AttendanceBook[] getAttendance(YearMonth ym) {
		AttendanceBook book[]; // 出席情報

		// Slaveのidを一旦格納
		ArrayList<String> slaves = new ArrayList<String>();
		String fileDirName = "file/";
		File fileDir = new File(fileDirName);
		for (String userDirName: fileDir.list()) {
			File file = new File(fileDirName + userDirName + "/attribute");
			if (file.exists()) {
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					if (br.readLine().equals(Slave.class.getSimpleName())) {
						slaves.add(userDirName);
					}
				} catch (FileNotFoundException e) {
					Log.error(e);
				} catch (IOException e) {
					Log.error(e);
				} catch (NullPointerException e) {
					Log.error(e);
				}
			}
		}

		// 取得したidのそれぞれの出席を取得
		book = new AttendanceBook[slaves.size()];
		for (int s = 0; s < slaves.size(); s++) {
			book[s] = User.getAttendance(slaves.get(s), ym);
		}

		return book;
	}

	// Masterは報告書提出不可
	public boolean submitReport(String file) {
		return false;
	}

	// 教員はIDを指定しないと報告書が閲覧できない
	public boolean showReport() {
		return false;
	}

	// アカウント管理
	public boolean setAccount(AccountInformation oldAccount, AccountInformation newAccount) {
		String target = oldAccount.getId(); // 変更対象のユーザID

		// 必要な要素が抜けてたらエラー
		if (newAccount.getId() == null ||
			newAccount.getName() == null ||
			newAccount.getPasswd() == null) {
			Log.error(new Throwable(), "要素がnull");
			return false;
		}

		// ユーザ情報を更新する
		try {
			// 名前の更新
			File file = new File("file/" + target + "/name");
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			pw.println(newAccount.getName());
			pw.close();

			// パスワードの更新
			file = new File("file/" + target + "/passwd");
			pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			pw.println(newAccount.getPasswd());
			pw.close();

			// ディレクトリの変更
			File oldFile = new File("file/" + target);
			File newFile = new File("file/" + newAccount.getId());
			oldFile.renameTo(newFile);
		} catch (FileNotFoundException e) {
			Log.error(e);
			return false;
		} catch (IOException e) {
			Log.error(e);
			return false;
		} catch (NullPointerException e) {
			Log.error(e);
			return false;
		}

		return true;
	}

	// TODO:マスターからの情報発信
	public boolean setEvent() {
		return false;
	}

	// TODO:ユーザの作成
	public boolean createUser() {
		return false;
	}
}
