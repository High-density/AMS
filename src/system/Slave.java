package system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 学生用クラス
 * @author Shinichi Yanagido
 * @version 1.0
 */
public class Slave extends User {
	public Slave(String id, String passwd) {
		super(id, passwd);
		attribute = this.getClass().getSimpleName();
	}

	// 出席
	public boolean setAttendance() {
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		// 各自のフォルダに日付ごとにファイルを作り，出席状況を格納する
		try {
			// 出席ディレクトリの作成
			String dirName = "./file/" + getId() + "/attendance/";
			if (!Controller.mkdirs(dirName)) {
				return false;
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
			return false;
		}
		return true;
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

	// アカウント管理
	public boolean setAccount(AccountInformation oldAccount, AccountInformation newAccount){
		// パスワード認証
		if (!isCorrectPasswd(oldAccount.getId(), oldAccount.getPasswd())) {
			return false;
		}

		// 必要な要素が抜けてたらエラー
		if (newAccount.getPasswd() == null) {
			Log.error(new Throwable(), "要素がnull");
			return false;
		}

		// ファイルを更新する
		// 更新対象はパスワードのみ
		try {
			File file = new File("file/" + id + "/passwd");
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			pw.println(newAccount.getPasswd());
			pw.close();
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

	// Slaveはユーザの作成不可
	public boolean createUser(AccountInformation account) {
		return false;
	}

	// Slaveはユーザの削除不可
	public boolean deleteUser(String id) {
		return false;
	}

	// Slaveは情報配信不可
	public boolean setEvent() {
		return false;
	}

	/**
	 * Slaveの一覧を取得する
	 * @return ユーザのIDをListで返す
	 */
	public static ArrayList<String> getSlaves() {
		// Slaveのid格納
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

		return slaves;
	}
}
