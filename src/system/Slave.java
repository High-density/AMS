package system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.NetworkInterface;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 * 学生用クラス
 * @author Shinichi Yanagido
 * @version 1.2
 */
public class Slave extends User {
	public Slave(String id, String passwd) {
		super(id, passwd);
		attribute = this.getClass().getSimpleName();
	}

	public boolean register() {
		// 作成後初めてのログインであれば，MACアドレスを登録する
		File file = new File(Controller.homeDirName + "/" + id + "/" + nicFileName);

		if (!file.exists()) {
			int option = JOptionPane.showConfirmDialog(null, "このコンピュータを登録しますか？");
			if (option != JOptionPane.YES_OPTION) return false;

			try {
				// MACアドレスファイル作成
				String gotNicName = null;
				String gotMacAddress = "";
				// 全NICを取得
				Enumeration<NetworkInterface> gotNics = NetworkInterface.getNetworkInterfaces();

				// 一番最初に取得したNICを登録する
				NetworkInterface gotNic = gotNics.nextElement();
				byte[] hardwareAddress = gotNic.getHardwareAddress();
				if (hardwareAddress != null) {
					for (byte b : hardwareAddress) {
						gotMacAddress += String.format("%02X:", b);
					}
					gotMacAddress = gotMacAddress.substring(0, gotMacAddress.length() - 1);
				}
				gotNicName = gotNic.getName();

				// ファイルへの書き込み
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
				pw.println(gotNicName + ":[" + gotMacAddress + "]");
				pw.close();
			} catch(IOException | NoSuchElementException e) {
				Log.error(e);
				return false;
			}
		}

		return true;
	}

	// 出席
	public boolean setAttendance() {
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		// 各自のフォルダに日付ごとにファイルを作り，出席状況を格納する
		try {
			// 出席ディレクトリの作成
			String dirName = Controller.homeDirName + "/" + getId() + "/" + attendanceDirName;
			if (!Controller.mkdirs(dirName)) return false;

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

	// 学生は出席変更不可
	public boolean changeAttendance(LocalDate ld, String id, int status) {
		return false;
	}


	// 出席取得
	public AttendanceBook[] getAttendance(YearMonth ym) {
		AttendanceBook book[] = new AttendanceBook[1];
		book[0] = User.getAttendance(id, ym);
		return book;
	}

	// 報告書提出
	public boolean submitReport(String file) {
		String dirName = Controller.homeDirName + "/" + getId() + "/" + reportDirName; // 報告書ディレクトリの名前
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

		return true;
	}

	// 報告書閲覧
	public boolean showReport() {
		return showReport(getId());
	}

	// アカウント管理
	public boolean setAccount(AccountInformation oldAccount, AccountInformation newAccount){
		// 必要な要素が抜けてたらエラー
		if (newAccount.getPasswd() == null) {
			Log.error(new Throwable(), "要素がnull");
			return false;
		}

		// ファイルを更新する
		// 更新対象はパスワードのみ
		try {
			File file = new File(Controller.homeDirName + "/" + id + "/" + passwdFileName);
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			pw.println(newAccount.getPasswd());
			pw.close();
		} catch (IOException | NullPointerException e) {
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

	// Slaveは予定の更新不可
	public Agenda setAgenda(Agenda agenda, int date, String content) {
		return agenda;
	}

	// Slateは予定の削除不可
	public Agenda deleteAgenda(Agenda agenda, int date) {
		return agenda;
	}

	/**
	 * Slaveの一覧を取得する
	 * @return ユーザのIDをListで返す
	 */
	public static ArrayList<String> getSlaves() {
		// Slaveのid格納
		ArrayList<String> slaves = new ArrayList<String>();

		File fileDir = new File(Controller.homeDirName);
		for (String userDirName: fileDir.list()) {
			File file = new File(Controller.homeDirName + "/" + userDirName + "/" + attributeFileName);
			if (file.exists()) {
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					if (br.readLine().equals(Slave.class.getSimpleName())) {
						slaves.add(userDirName);
					}
				} catch (IOException | NullPointerException e) {
					Log.error(e);
				}
			}
		}
		Collections.sort(slaves);

		return slaves;
	}
}
