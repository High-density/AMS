package system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.net.NetworkInterface;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Master extends User {
	public Master(String id, String passwd) {
		super(id, passwd);
		attribute = this.getClass().getSimpleName();
	}

	// Masterは出席不可
	public boolean setAttendance() {
		return false;
	}

	// 出席状況の手動変更
	public boolean changeAttendance(LocalDate ld, String id, int status) {
		String temporaryTime = "0000";

		File dir = new File(Controller.homeDirName + "/" + id + "/" + attendanceDirName);
		File file = new File(dir.toString() + "/" + ld.toString());
		if (file.exists()) {
			// ファイルがあるときは中のデータのみを更新
			try (BufferedReader br = new BufferedReader(new FileReader(file));
				 PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)))){
				String line = br.readLine();
				Pattern p = Pattern.compile("^([0-9]{4}:)");
				Matcher m = p.matcher(line);
				if (m.find()) {
					pw.println(m.group(1) + String.valueOf(status));
				} else {
					Log.error(new Throwable(), file.toString() + "の内容が不正です");
					return false;
				}
			} catch (IOException e) {
				Log.error(e);
				return false;
			}

		} else {
			// ファイルがないときは新たに作成する
			try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
				Controller.mkdirs(dir.toString());
				file.createNewFile();
				pw.println(temporaryTime + ":" + String.valueOf(status));
			} catch (IOException e) {
				Log.error(e);
				return false;
			}
		}

		return true;
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
			newAccount.getName() == null) {
			Log.error(new Throwable(), "要素がnull");
			return false;
		}

		// ユーザ情報を更新する
		File file = new File(Controller.homeDirName + "/" + target + "/" + nameFileName);
		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
			// 名前の更新
			pw.println(newAccount.getName());
		} catch (NullPointerException | IOException e) {
			Log.error(e);
			return false;
		}

		file = new File(Controller.homeDirName + "/" + target + "/" + passwdFileName);
		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
			// パスワードの更新
			if (newAccount.getPasswd() != null) {
				pw.println(newAccount.getPasswd());
			}

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

		// 属性ファイル作成
		file = new File(userDirName + "/" + attributeFileName);
		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
			pw.println(Slave.class.getSimpleName());
		} catch(IOException | NoSuchElementException e) {
			Log.error(e);
			return false;
		}

		// 名前ファイル作成
		file = new File(userDirName + "/" + nameFileName);
		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
			pw.println(account.getName());
		} catch(IOException | NoSuchElementException e) {
			Log.error(e);
			return false;
		}

		// パスワードファイル作成
		file = new File(userDirName + "/" + passwdFileName);
		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
			pw.println(account.getPasswd());
		} catch(IOException | NoSuchElementException e) {
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

	// 予定の更新
	public Agenda setAgenda(Agenda agenda, int date, String content) {
		// 予定の設定
		agenda.setData(date, content);

		// 各学生について，更新情報の通知を残す
		ArrayList<String> slaves =  Slave.getSlaves();
		for (String slave : slaves) {
			File dir = new File(Controller.homeDirName + "/" + slave + "/" + notificationDirName);
			if (!Controller.mkdirs(dir.toString())) return null;
			LocalDate ld = LocalDate.of(agenda.getYear(), agenda.getMonth(), date + 1);
			File file = new File(dir.toString() + "/" + ld.toString());
			try {
				file.createNewFile();
			} catch(IOException e) {
				Log.error(e);
				return null;
			}
			try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
				pw.println(content);
			} catch(IOException e) {
				Log.error(e);
				return null;
			}
		}

		return agenda;
	}

	// 予定の削除
	public Agenda deleteAgenda(Agenda agenda, int date) {
		agenda.unsetData(date);
		return agenda;
	}
}
