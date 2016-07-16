package system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
		String attribute = null; // 属性

		// パスワードが一致するかどうか検証
		if (isCorrectPasswd(id, passwd) /*&& hasCertifiedMacAddress(id)*/) {
			// ファイルから属性の読み込み
			try {
				File file = new File("./file/" + id + "/attribute");
				BufferedReader br = new BufferedReader(new FileReader(file));
				attribute = br.readLine(); // Fileから読み取った行

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
			if (attribute == null) {
				// 属性読み込みエラー
				Log.error(new Throwable(), "属性読み込みエラー: attribute = null");
				return null;
			} else if (attribute.equals(Master.class.getSimpleName())) {
				// 学生のIDで一致したとき
				return new Master(id, passwd);
			} else if (attribute.equals(Slave.class.getSimpleName())) {
				// 教員のIDで一致したとき
				return new Slave(id, passwd);
			} else {
				// 登録されていない属性が見つかったとき
				Log.error(new Throwable(), "属性読み込みエラー: attribute = " + attribute);
				return null;
			}
		} else {
			// 不正なパスワードの入力
		}

		return null; // 認証失敗
	}

	// 入力されたパスワードがIDに対して正しいか
	protected static boolean isCorrectPasswd(String id, String passwd) {
		String pw = null; // パスワード

		// ファイルからパスワードの読み込み
		try {
			File file = new File("./file/" + id + "/passwd");
			BufferedReader br = new BufferedReader(new FileReader(file));
			pw = br.readLine();
			br.close();
		} catch (FileNotFoundException e) {
			// 入力されたidを持つユーザがいない
			return false;
		} catch (IOException e) {
			Log.error(e);
			return false;
		} catch (NullPointerException e) {
			Log.error(e);
			return false;
		}

		return pw.equals(passwd);
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
			Pattern p = Pattern.compile("(.*):\\[([0-9A-F:]+)");
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
			String fileName = year + "-" + month + "-" + day;
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
					book.setData(d, Integer.parseInt(m.group(3)), lt);
				}

				br.close();
			} catch (FileNotFoundException e) {
				// 欠席のとき
				book.setData(d, AttendanceBook.ABSENCE);
			} catch (IOException e) {
				book.setData(d, AttendanceBook.ERROR);
			} catch (NullPointerException e) {
				book.setData(d, AttendanceBook.ERROR);
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

	// アカウント管理
	public abstract boolean setAccount(AccountInformation oldAccount, AccountInformation newAccount);

	public abstract boolean createUser(AccountInformation account); // ユーザの作成

	public abstract boolean deleteUser(String id); // ユーザの削除

	public abstract boolean setEvent(); // 情報配信

	// setter
	public void setId(String id) {
		this.id = id;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	// gettter
	public String getId() {
		return id;
	}

	public String getAttribute() {
		return attribute;
	}
}
