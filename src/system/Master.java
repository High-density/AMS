package system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.net.NetworkInterface;
import java.time.YearMonth;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Enumeration;

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
		ArrayList<String> slaves = Slave.getSlaves();

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
			File file = new File(Controller.homeDirName + "/" + target + "/" + nameFileName);
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			pw.println(newAccount.getName());
			pw.close();

			// パスワードの更新
			file = new File(Controller.homeDirName + "/" + target + "/" + passwdFileName);
			pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			pw.println(newAccount.getPasswd());
			pw.close();

			// ディレクトリの変更
			File oldFile = new File(Controller.homeDirName + "/" + target);
			File newFile = new File(Controller.homeDirName + "/" + newAccount.getId());
			oldFile.renameTo(newFile);
		} catch (NullPointerException | IOException e) {
			Log.error(e);
			return false;
		}

		return true;
	}

	// ユーザの作成
	public boolean createUser(AccountInformation account) {
		File file; // 作成するファイル用
		PrintWriter pw; // ファイルへの書き込み用

		// 必要な要素が抜けてたらエラー
		if (account.getId() == null ||
			account.getName() == null ||
			account.getPasswd() == null) {
			Log.error(new Throwable(), "要素がnull");
			return false;
		}

		// ディレクトリ作成
		String userDirName = Controller.homeDirName + "/" + account.getId() + "/";
		Controller.mkdirs(userDirName + attendanceDirName);
		Controller.mkdirs(userDirName + reportDirName);

		try {
			// 属性ファイル作成
			file = new File(userDirName + "/" + attributeFileName);
			pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			pw.println(Slave.class.getSimpleName());
			pw.close();

			// 名前ファイル作成
			file = new File(userDirName + "/" + nameFileName);
			pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			pw.println(account.getName());
			pw.close();

			// パスワードファイル作成
			file = new File(userDirName + "/" + passwdFileName);
			pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			pw.println(account.getPasswd());
			pw.close();

			// MACアドレスファイル作成
			String gotNicName = null;
			String gotMacAddress = "";
			// 全NICを取得
			Enumeration<NetworkInterface> gotNics = NetworkInterface.getNetworkInterfaces();
			// 登録MACアドレスと同じものを探す
			do {
				NetworkInterface gotNic = gotNics.nextElement();
				byte[] hardwareAddress = gotNic.getHardwareAddress();
				if (hardwareAddress != null) {
					for (byte b : hardwareAddress) {
						gotMacAddress += String.format("%02X:", b);
					}
					gotMacAddress = gotMacAddress.substring(0, gotMacAddress.length() - 1);
				}
				gotNicName = gotNic.getName();
			} while("lo".equals(gotNicName));
			// ファイルへの書き込み
			file = new File(userDirName + "/" + nicFileName);
			pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			pw.println(gotNicName + ":[" + gotMacAddress + "]");
			pw.close();

		} catch(IOException e) {
			Log.error(e);
			return false;
		}

		return true;
	}

	// ユーザの削除
	public boolean deleteUser(String id) {
		Controller.deleteFile(new File(Controller.homeDirName + "/" + id));
		return true;
	}
}
