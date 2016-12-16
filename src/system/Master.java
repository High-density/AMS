package system;

import java.io.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
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
		String temporaryTime = "0000"; // 書き込む時間
		String content = ""; // 書き込む内容
		// 対象ディレクトリ
		File dir = new File(Controller.homeDirName + "/" + id + "/" + attendanceDirName);
		// 対象ファイル
		File file = new File(dir.toString() + "/" + ld.toString());

		if (file.exists()) {
			// ファイルがあるときは中のデータのみを更新
			try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
				String line = br.readLine();
				Pattern p = Pattern.compile("^([0-9]{4}:)");
				Matcher m = p.matcher(line);
				if (m.find()) {
					content = m.group(1) + String.valueOf(status);
				} else {
					Log.error(new Throwable(), file.toString() + "の内容が不正です");
					return false;
				}
			} catch (IOException e) {
				Log.error(e);
				return false;
			}
			try (PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8")))){
				pw.print(content + "\n");
			} catch (IOException | NullPointerException e) {
				Log.error(e);
				return false;
			}

		} else {
			// ファイルがないときは新たに作成する
			Controller.mkdirs(dir.toString());
			try (PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8")))) {
				pw.print(temporaryTime + ":" + String.valueOf(status) + "\n");
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
		// 指定されたAccountInformationが有効なものか
		if (!oldAccount.isValid() || !newAccount.isValid()) return false;

		String target = oldAccount.getId(); // 変更対象のユーザID

		// 必要な要素が抜けてたらエラー
		if (newAccount.getId() == null ||
			newAccount.getName() == null) {
			Log.error(new Throwable(), "要素がnull");
			return false;
		}

		// ユーザ情報を更新する
		File file = new File(Controller.homeDirName + "/" + target + "/" + nameFileName);
		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8")))) {
			// 名前の更新
			pw.print(newAccount.getName() + "\n");
		} catch (NullPointerException | IOException e) {
			Log.error(e);
			return false;
		}

		// パスワードの更新
		if (newAccount.getPasswd() != null) {
			file = new File(Controller.homeDirName + "/" + target + "/" + passwdFileName);
			if (!Log.writeInCipher(file, newAccount.getPasswd(), false)) return false;
		}

		// ディレクトリの変更
		if (!target.equals(newAccount.getId())) {
			try {
				File oldFile = new File(Controller.homeDirName + "/" + target);
				File newFile = new File(Controller.homeDirName + "/" + newAccount.getId());
				oldFile.renameTo(newFile);
			} catch (NullPointerException e) {
				Log.error(e);
				return false;
			}
		}
		return true;
	}

	// ユーザの作成
	public boolean createUser(AccountInformation account) {
		File file; // 作成するファイル用

		// 指定されたAccountInformationが有効なものか
		if (!account.isValid()) return false;

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
		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8")))) {
			pw.print(Slave.class.getSimpleName() + "\n");
		} catch(IOException | NoSuchElementException e) {
			Log.error(e);
			return false;
		}

		// 名前ファイル作成
		file = new File(userDirName + "/" + nameFileName);
		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8")))) {
			pw.print(account.getName() + "\n");
		} catch(IOException | NoSuchElementException e) {
			Log.error(e);
			return false;
		}

		// パスワードファイル作成
		file = new File(userDirName + "/" + passwdFileName);
		if (!Log.writeInCipher(file, account.getPasswd(), false)) return false;

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
			try (PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8")))) {
				pw.print(props.getProperty("ttl.agenda") + "\n");
				pw.print(content + "\n");
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

	// Masterの一覧を取得する
	public static ArrayList<String> getMasters() {
		// Masterのid格納
		ArrayList<String> masters = new ArrayList<String>();

		File fileDir = new File(Controller.homeDirName);
		if (fileDir.list() == null) return null;
		for (String userDirName: fileDir.list()) {
			File file = new File(Controller.homeDirName + "/" + userDirName + "/" + attributeFileName);
			if (file.exists()) {
				try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
					if (br.readLine().equals(Master.class.getSimpleName())) {
						masters.add(userDirName);
					}
				} catch (IOException | NullPointerException e) {
					Log.error(e);
				}
			}
		}
		Collections.sort(masters);

		return masters;
	}
}
