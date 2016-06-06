package system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.NullPointerException;
import java.lang.String;
import java.lang.Throwable;
import java.net.NetworkInterface;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class User {
	protected String id = null; // ID
	private String passwd = null; // パスワード
	protected String attribute = null; // 属性

	public User(String id, String passwd) {
		this.id = id;
		this.passwd = passwd;
	}

	// パスワードによる認証
	public static User login(String id, String passwd) {
		String pw = null; // パスワード
		String attribute = null; // 属性

		// ファイルからパスワードの読み込み
		try {
			File file = new File("./file/" + id + "/passwd");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			Pattern p = Pattern.compile(id + ":(.*)$");
			Matcher m = p.matcher(line);
			if (m.find()) {
				pw = m.group(1);
			}

			br.close();
		} catch (FileNotFoundException e) {
			Log.error(e);
			return null;
		} catch (IOException e) {
			Log.error(e);
			return null;
		} catch (NullPointerException e) {
			Log.error(e);
			return null;
		}

		// パスワードが一致するかどうか検証
		if (pw.equals(passwd) && true/*hasCertifiedMacAddress(id)*/) {
			// ファイルから属性の読み込み
			try {
				File file = new File("./file/user");
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line; // Fileから読み取った行
				Pattern p = Pattern.compile(id + ":(.*)$");
				Matcher m = null;
				boolean finded = false; // マッチしたかどうか

				// 各行を読み込んでマッチするか検証
				while (attribute == null) {
					line = br.readLine();
					m = p.matcher(line);
					if (m.find())
						attribute = m.group(1);
				}

				br.close();
			} catch (FileNotFoundException e) {
				Log.error(e);
				return null;
			} catch (IOException e) {
				Log.error(e);
				return null;
			} catch (NullPointerException e) {
				Log.error(e);
				return null;
			}

			// 属性に合わせてユーザの作成
			if (attribute.equals(Master.class.getSimpleName())) {
				return new Master(id, passwd);
			} else if (attribute.equals(Slave.class.getSimpleName())) {
				return new Slave(id, passwd);
			} else {
				Log.error(new Throwable(), "属性読み込みエラー: attribute = " + attribute);
				return null;
			}
		} else {
			// 不正なパスワードの入力
		}

		return null; // 認証失敗
	}

	// 認証されたMacAddressを保持しているか
	private static boolean hasCertifiedMacAddress(String id) {
		String registeredNicName = null; // 登録されているNICの表示名
		String registeredMacAddress = null; // 登録されているMACアドレス
		String gotNicName = null; // 現パソコンのNICの表示名
		String gotMacAddress = ""; // 現パソコンのMACアドレス

		// ファイルからMACアドレスNIC表示名の読み込み
		try {
			File file = new File("./file/" + id + "/nics");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			Pattern p = Pattern.compile(id + ":(.*):\\[([0-9A-F:]+)");
			Matcher m = p.matcher(line);
			if (m.find()) {
				registeredNicName = m.group(1);
				registeredMacAddress = m.group(2);
			}

			br.close();
		} catch (FileNotFoundException e) {
			// ID未登録時
			return false;
		} catch (IOException e) {
			Log.error(e);
			return false;
		} catch (NullPointerException e) {
			Log.error(e);
			return false;
		}

		// MACアドレスの取得
		try {
			// 全NICを取得
			Enumeration<NetworkInterface> gotNics = NetworkInterface.getNetworkInterfaces();
			// 登録MACアドレスと同じものを探す
			while (gotNics.hasMoreElements() && !registeredNicName.equals(gotNicName)) {
				NetworkInterface gotNic = gotNics.nextElement();
				byte[] hardwareAddress = gotNic.getHardwareAddress();
				if (hardwareAddress != null) {
					for (byte b : hardwareAddress) {
						gotMacAddress += String.format("%02X:", b);
					}
					gotMacAddress = gotMacAddress.substring(0, gotMacAddress.length() - 1);
				}
				gotNicName = gotNic.getName();
			}
		} catch (IOException e) {
			Log.error(e);
			return false;
		}

		// 登録されているMACアドレスが見つかったか
		if (registeredMacAddress.equals(gotMacAddress) && registeredNicName.equals(gotNicName)) {
			return true;
		} else {
			return false;
		}
	}

	public abstract boolean setAttendance(); // 出席

	public abstract AttendanceBook[] getAttendance(YearMonth ym); // 出席表示(idから取得すべき情報を自動判断)

	// 出席表示(id指定)
	protected static AttendanceBook getAttendance(String id ,YearMonth ym) {
		AttendanceBook book = new AttendanceBook(id, ym); // 1 Slave分の出席簿
		String year = String.format("%02d", ym.getYear()); // 取得する年
		String month = String.format("%02d", ym.getMonthValue()); // 取得する月
		int maxDay = ym.lengthOfMonth(); // 月の日数
		for (int d = 0; d < maxDay; d++) {
			// ファイルから出席情報の読み込み
			String day = String.format("%02d", d + 1);
			String fileName = year + month + day;
			try {
				// 出席のとき
				File file = new File("./file/" + id + "/attendance/" + fileName);
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = br.readLine();
				Pattern p = Pattern.compile("([0-9]{2})([0-9]{2}):([0-9]+)$");
				Matcher m = p.matcher(line);
				if (m.find()){
					// 出席時間の設定
					int hour = Integer.parseInt(m.group(1));
					int minute = Integer.parseInt(m.group(2));
					LocalTime lt = LocalTime.of(hour, minute);
					// 出席簿に記入
					book.setBook(d, Integer.parseInt(m.group(3)), lt);
				}

				br.close();
			} catch (FileNotFoundException e) {
				// 欠席のとき
				book.setBook(d, AttendanceBook.ABSENCE);
			} catch (IOException e) {
				book.setBook(d, AttendanceBook.ERROR);
			} catch (NullPointerException e) {
				book.setBook(d, AttendanceBook.ERROR);
			}
		}

		return book;
	}

	public abstract boolean submitReport(String file); // 報告書提出

	public abstract boolean showReport(); // 報告書閲覧

	// 報告書閲覧（ID指定）
	public boolean showReport(String id){
		String dirName = "file/" + getId() + "/report/"; // 報告書ディレクトリ

		// ディレクトリ作成
		if (!Controller.mkdirs(dirName)) {
			return false;
		}

		try {
			// コマンドとして実行して，ファイルを開く
			Runtime r = Runtime.getRuntime();
			String cmd; // 実行するコマンド
			String osname = System.getProperty("os.name");
			// OSごとに開くためのコマンドを変える
			if (osname.indexOf("Windows") >= 0) {
				cmd = "start";
			} else if (osname.indexOf("Linux") >= 0) {
				cmd = "xdg-open";
			} else if (osname.indexOf("Mac") >= 0) {
				cmd = "open";
			} else {
				Log.popup("現在使用中のOSには対応していません");
				return false;
			}
			cmd += " " + dirName;
			Log.popup(cmd);
			r.exec(cmd);
		} catch (IOException e) {
			Log.error(e);
			return false;
		}
		return true;
	}

	public abstract boolean setEvent(); // 情報配信

	public abstract boolean createUser(); // ユーザの作成

	// setter getter
	public int setId(String id) {
		this.id = id;
		return 0;
	}

	public String getId() {
		return id;
	}

	public int setPasswd(String passwd) {
		this.passwd = passwd;
		return 0;
	}

	public String getAttribute() {
		return attribute;
	}
}
