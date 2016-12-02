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
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import system.Nics.MacAddress;

public abstract class User {
	// 改行コード
	public static final String br = System.getProperty("line.separator");

	// ファイルの場所管理
	public static final String attributeFileName = "attribute";
	public static final String nameFileName = "name";
	public static final String nicFileName = "nics";
	public static final String passwdFileName = "passwd";
	public static final String attendanceDirName = "attendance/";
	public static final String reportDirName = "report/";
	public static final String notificationDirName = "notification/";

	protected String id = null; // ID
	protected String attribute = null; // 属性
	protected static Properties props; // 設定ファイル

	public User(String id, String passwd) {
		this.id = id;
	}

	// 初期化
	public static boolean init() {
		File file;

		// 設定ファイル読み込み
		props = Controller.loadProperties();

		// 教員用ディレクトリの作成
		Controller.mkdirs(Controller.masterDir.toString());

		// 教員の初期名の登録
		file = new File(Controller.masterDir + "/" + nameFileName);
		if (!file.exists()) {
			try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
				pw.println(props.getProperty("root.name.default"));
			} catch (IOException e) {
				return false;
			}
		}

		// 属性情報の登録
		file = new File(Controller.masterDir + "/" + attributeFileName);
		if (!file.exists()) {
			try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
				pw.println(Master.class.getSimpleName());
			} catch (IOException e) {
				return false;
			}
		}

		// パスワードの登録
		file = new File(Controller.masterDir + "/" + passwdFileName);
		if (!file.exists()) {
			if (!Log.writeInCipher(file, props.getProperty("root.pw.default"), false)) return false;
		}

		return true;
	}

	// 初期登録が必要なら登録を行う
	public boolean register() {
		// 作成後初めてのログインであれば，MACアドレスを登録する
		File file = new File(Controller.homeDirName + "/" + id + "/" + nicFileName);

		if (!file.exists()) {
			String message = Controller.getName(id) + "(" + id + ")として" + br  +  "このコンピュータを登録しますか？";
			int option = JOptionPane.showConfirmDialog(null, message);
			if (option != JOptionPane.YES_OPTION) return false;

			try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
				Nics nics = new Nics();
				for (int n = 0; n < nics.length(); n++) {
					MacAddress mac = nics.getMacAddress(n);
					if (mac.get() != null && mac.get() > 0) {
						// ファイルへの書き込み
						if (!Log.writeInCipher(file,
											   nics.getName(n) + ":[" + nics.getMacAddress(n) + "]",
											   false)) {
							return false;
						}
						n = nics.length();
					}
				}
			} catch(IOException | NoSuchElementException e) {
				Log.error(e);
				return false;
			}
		}

		return true;
	}

	// パスワードによる認証
	public static User login(String id, String passwd) {
		String attribute = null; // 属性
		File file;

		// パスワードとMACアドレスの検証
		if (!isCorrectPasswd(id, passwd)) {
			return null;
		}
		if (!hasCertifiedMacAddress(id)) {
			// 不正ログインを試みた場合はMasterに報告
			File dir = new File(Controller.masterDir + "/" + notificationDirName);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日HH時mm分ss秒");
			file = new File(Controller.masterDir + "/notification/" + LocalDateTime.now().format(formatter));
			if(!Controller.mkdirs(dir.toString())) return null;
			try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
				pw.println(props.getProperty("ttl.illegal"));
				String operator = Controller.getComputerHolder();
				String content;

				if (operator == null) {
					content = "登録されていない";
				} else {
					content = Controller.getName(operator) + "(" + operator + ")の";
				}
				content += "コンピュータで";
				content += Controller.getName(id) + "(" + id + ")";
				content += "へのログインが試みられました．";
				pw.println(content + "\n");
			} catch(IOException e) {
				Log.error(e);
				return null;
			}

			return null;
		}

		// ファイルから属性の読み込み
		file = new File(Controller.homeDirName + "/" + id + "/" + attributeFileName);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			attribute = br.readLine(); // Fileから読み取った行
		} catch (IOException | NullPointerException e) {
			Log.error(e);
			return null;
		}

		// 属性に合わせてユーザの作成
		if (attribute == null) {
			// 属性読み込みエラー
			Log.error(new Throwable(), "属性読み込みエラー: attribute = null");
			return null;
		} else if (attribute.equals(Master.class.getSimpleName())) {
			// 教員のIDで一致したとき
			return new Master(id, passwd);
		} else if (attribute.equals(Slave.class.getSimpleName())) {
			// 学生のIDで一致したとき
			return new Slave(id, passwd);
		} else {
			// 登録されていない属性が見つかったとき
			Log.error(new Throwable(), "属性読み込みエラー: attribute = " + attribute);
			return null;
		}
	}

	// 通知を表示する
	public boolean popNotification() {
		// タイトルと内容を結ぶmap
		HashMap<String, String> ttlToContent = new HashMap<String, String>();
		String message = ""; // 通知内容

		// 更新された通知を全て取得
		File dir = new File(Controller.homeDirName + "/" + id + "/" + notificationDirName);
		String fileNames[] = dir.list();
		if (fileNames != null && fileNames.length != 0) {
			Arrays.sort(fileNames);
			for (String fileName : fileNames) {
				File file = new File(dir.toString() + "/" + fileName);
				try (BufferedReader br = new BufferedReader(new FileReader(file))) {
					message += "***** " + fileName + " *****\n";
					String ttl = br.readLine(); // 先頭の行はフレームタイトル
					if (ttlToContent.get(ttl) == null) ttlToContent.put(ttl, "");
					String line;
					while ((line = br.readLine()) != null) {
						message += line + "\n";
					}
					message += "\n";
					ttlToContent.put(ttl, ttlToContent.get(ttl) + message);
				} catch (IOException e) {
					Log.error(e);
					return false;
				}
				Controller.deleteFile(file); // 通知した情報の削除
			}
			// message = message.trim();

			// 実際の表示処理
			for (String ttl : ttlToContent.keySet()) {
				int style;
				if (ttl.equals(props.getProperty("ttl.agenda"))) {
					style = JOptionPane.INFORMATION_MESSAGE;
				} else if (ttl.equals(props.getProperty("ttl.illegal"))) {
					style = JOptionPane.WARNING_MESSAGE;
				} else {
					style = JOptionPane.WARNING_MESSAGE;
				}
				JOptionPane.showMessageDialog(null, ttlToContent.get(ttl).trim(), ttl, style);
			}
		}

		return true;
	}

	// 入力されたパスワードがIDに対して正しいか
	protected static boolean isCorrectPasswd(String id, String passwd) {
		String pw = null; // パスワード

		// ファイルからパスワードの読み込み
		File file = new File(Controller.homeDirName + "/" + id + "/" + passwdFileName);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			pw = Cryptography.decrypt(br.readLine());
		} catch (FileNotFoundException e) {
			// 入力されたidを持つユーザがいない
			return false;
		} catch (IOException | NullPointerException e) {
			Log.error(e);
			return false;
		}

		if (pw == null) pw = "";
		return pw.equals(passwd);
	}

	// 認証されたMacAddressを保持しているか
	private static boolean hasCertifiedMacAddress(String id) {
		String nicName = null; // 登録されているNICの表示名
		String macAddress = null; // 登録されているMACアドレス

		// ファイルからMACアドレスNIC表示名の読み込み
		File file = new File(Controller.homeDirName + "/" + id + "/" + nicFileName);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = Cryptography.decrypt(br.readLine());
			Pattern p = Pattern.compile("(.*):\\[([0-9A-F:]*)\\]");
			Matcher m = p.matcher(line);
			if (m.find()) {
				nicName = m.group(1);
				macAddress = m.group(2);
			} else {
				return false;
			}
		} catch (FileNotFoundException e) {
			// ID未登録時
			return true;
		} catch (IOException | NullPointerException e) {
			Log.error(e);
			return false;
		}

		// MACアドレスの取得
		Nics nics = new Nics();
		for (int i = 0; i < nics.length(); i++) {
			if (nicName.equals(nics.getName(i))) {
				if (macAddress.equals(nics.getMacAddress(i).toString())) {
					return true;
				}
				return false;
			}
		}
		return false;
	}

	// idから名前を取得する
	public static String getName(String id) {
		if (id == null) return null;

		String name;
		File file = new File(Controller.homeDirName + "/" + id + "/" + nameFileName);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			name = br.readLine(); // Fileから読み取った行
		} catch (IOException | NullPointerException e) {
			Log.error(e);
			return null;
		}

		return name;
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
			String dirName = Controller.homeDirName + "/" + id + "/" + attendanceDirName;
			File file = new File(dirName + fileName);
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				// 出席のとき
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
			} catch (FileNotFoundException e) {
				// 出席もしてなく，ファイルを作成してないとき
				book.setData(d, AttendanceBook.NO_MARK);
			} catch (IOException | NullPointerException e) {
				book.setData(d, AttendanceBook.ERROR);
			}
		}

		return book;
	}

	public abstract boolean changeAttendance(LocalDate ld, String id, int status); // 出席の手動変更

	public abstract boolean submitReport(String file); // 報告書提出

	public abstract boolean showReport(); // 報告書閲覧

	// 報告書閲覧（ID指定）
	public boolean showReport(String id){
		String dirName = Controller.homeDirName + "/" + id + "/" + reportDirName; // 報告書ディレクトリ

		// ディレクトリ作成
		if (!Controller.mkdirs(dirName)) return false;

		try {
			// コマンドとして実行して，ファイルを開く
			Runtime r = Runtime.getRuntime();
			String cmd; // 実行するコマンド
			String osname = System.getProperty("os.name");
			// OSごとに開くためのコマンドを変える
			if (osname.indexOf("Windows") >= 0) {
				cmd = "explorer";
				Pattern p = Pattern.compile("/");
				Matcher m = p.matcher(dirName);
				dirName = m.replaceAll("\\\\");
			} else if (osname.indexOf("Linux") >= 0) {
				cmd = "xdg-open";
			} else if (osname.indexOf("Mac") >= 0) {
				cmd = "open";
			} else {
				Log.popup("現在使用中のOSには対応していません");
				return false;
			}
			cmd += " " + dirName;
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

	// 予定の更新
	public abstract Agenda setAgenda(Agenda agenda, int date, String content);

	// 予定の削除
	public abstract Agenda deleteAgenda(Agenda agenda, int date);

	// setter
	public void setId(String id) {
		this.id = id;
	}

	// gettter
	public String getId() {
		return id;
	}

	public String getAttribute() {
		return attribute;
	}
}
