package system;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 * 内部の動作を一括で管理するクラス<br>
 * GUIとのやりとりはここを通して行う
 * @author Shinichi Yanagido
 * @version 1.7
 */
public class Controller {
	/**
	 * ログインしているユーザを格納
	 */
	private User user;
	/**
	 * 全てのファイルを入れるディレクトリの名前
	 */
	public static final String homeDirName = "file";
	/**
	 * 教員のディレクトリ
	 */
	public static final File masterDir = new File(homeDirName + "/root");
	/**
	 * 予定を格納するディレクトリ
	 */
	public static final File agendaDir = new File(homeDirName + "/root/agenda/");
	/**
	 * ログを保存するディレクトリ
	 */
	public static final File logDir = new File(homeDirName + "/root/log/");
	/**
	 * 基本的なログを保存するファイル
	 */
	public static final File logFile = new File(logDir.toString() + "/log.txt");
	/**
	 * エラーログを保存するファイル
	 */
	public static final File errorFile = new File(logDir.toString() + "/error.txt");
	/**
	 * 不正ログインの情報を記入するディレクトリ
	 */
	public static final File incorrectLoginFile = new File(homeDirName + "/root/notification/不正ログイン");

	public Controller() {
		Log.logDir = logDir;
		Log.logFile = logFile;
		Log.errorFile = errorFile;
	}

	/**
	 * ログインを行う<br>
	 * IDに沿った権限を持つユーザを作成する
	 * @param id ユーザID
	 * @param passwd パスワード
	 * @return ログイン成功時true，失敗時false
	 */
	public boolean login(String id, String passwd) {
		user = User.login(id, passwd);
		if (user == null) {
			// ログイン失敗
			return false;
		} else {
			// ログイン成功したら出席チェック
			attend();
			user.popNotification();
			return true;
		}
	}

	/**
	 * ログアウトを行う
	 * @return ログアウト成功時true，失敗時false
	 */
	public boolean logout() {
		// TODO: falseを返す状況を作る
		user = null;
		return true;
	}

	/**
	 * 報告書の提出を行う
	 * @param file 提出対象へのファイルパス
	 * @return 提出成功時true，失敗時false，教員は常にfalse
	 */
	public boolean submitReport(String file) {
		return user.submitReport(file);
	}

	/**
	 * 報告書の入ったディレクトリをファイルマネージャで開く<br>
	 * どのディレクトリを開けばいいかをログイン中のIDから判断して開く
	 * @return ディレクトリが開けた時true，どのディレクトリを開いたらいいか判断がつかない等で開けない時false，教員は常にfalse
	 */
	public boolean showReport() {
		return user.showReport();
	}

	/**
	 * 報告書の入ったディレクトリをファイルマネージャで開く<br>
	 * どのディレクトリを開くかを引数に指定する
	 * @param id 開きたいディレクトリの所有者ID
	 * @return ディレクトリが開けた時true，開けなかった時false
	 */
	public boolean showReport(String id) {
		return user.showReport(id);
	}

	/**
	 * 最終更新日を取得する
	 * @param id 最終更新日を取得したい学生のID
	 * @return 最終更新日
	 */
	public LocalDate getLastUploadDate(String id) {
		File fileDir = new File(homeDirName + "/" + id + "/" + User.reportDirName);
		String reports[] = fileDir.list();
		if (reports == null || reports.length == 0) return null;

		Arrays.sort(reports, Comparator.reverseOrder());
		return LocalDate.parse(getFileNameWithoutSuffix(reports[0]));
	}

	/**
	 * 出席の登録を行う
	 * @return 登録が完了したらtrue，できなかったらfalse
	 */
	private boolean attend() {
		// 属性がSlaveなら出席
		if (user.getAttribute().equals(Slave.class.getSimpleName())) {
			return user.setAttendance();
		}
		return false;
	}

	/**
	 * 出席の手動変更を行う
	 * @param ld 出席情報を変えたい日付
	 * @param id 変更したい学生ID
	 * @param status 出席情報
	 * @return 変更が完了したらtrue，できなかったらfalse，学生は常にfalse
	 * @see AttendanceBook
	 */
	public boolean changeAttendance(LocalDate ld, String id, int status) {
		return user.changeAttendance(ld, id, status);
	}

	/**
	 * 出席状況の取得を行う
	 * @param ym 取得したい年月
	 * @return 取得した各学生の一月分の出席簿配列(教員は全学生分，学生は自分のみ)
	 * @see AttendanceBook
	 */
	public AttendanceBook[] getAttendance(YearMonth ym) {
		return user.getAttendance(ym);
	}

	/**
	 * アカウントの更新を行う
	 * @param oldAccount 現在のアカウント情報<br>
	 * 教員の場合: 必須項目:ID<br>
	 * 学生の場合: 必須項目:なし
	 * @param newAccount 更新したいアカウント情報<br>
	 * 教員の場合: 必須項目:ID，名前，任意項目:パスワード<br>
	 * 学生の場合: 必須項目:パスワード
	 * @return 更新成功時true，失敗時false
	 */
	public boolean setAccount(AccountInformation oldAccount, AccountInformation newAccount) {
		return user.setAccount(oldAccount, newAccount);
	}

	/**
	 * 学生アカウントの作成を行う
	 * @param account 作成するアカウントの情報(必須項目:ID，名前，パスワード)
	 * @return 作成成功時true，失敗時false，学生は常にfalse
	 */
	public boolean createUser(AccountInformation account) {
		return user.createUser(account);
	}

	/**
	 * アカウントの削除を行う．教員も消すことができる
	 * @param id
	 * @return 削除成功時true，失敗時false，学生は常にfalse
	 */
	public boolean deleteUser(String id) {
		return user.deleteUser(id);
	}

	/**
	 * 予定の取得を行う
	 * @param ym 予定を取得したい年月
	 * @return 予定が入ったAgendaクラス
	 */
	public Agenda getAgenda(YearMonth ym) {
		return new Agenda(ym, agendaDir);
	}

	/**
	 * 予定の更新を行う
	 * @param agenda 予定を更新したいAgendaクラス
	 * @param date 予定を更新したい日付
	 * @param content 予定内容
	 * @return 新しく作成した予定．更新できない場合は引数で渡したものと同じのを返す
	 */
	public Agenda setAgenda(Agenda agenda, int date, String content) {
		// 予定の設定
		Agenda newAgenda = user.setAgenda(agenda, date, content);
		return newAgenda;
	}

	/**
	 * 特定の予定を削除する
	 * @param agenda 削除したい予定の入ったAgendaクラス
	 * @param date 削除したい日付
	 * @return 予定削除後のAgendaクラス．削除できない場合は引数で渡したものと同じのを返す
	 */
	public Agenda deleteAgenda(Agenda agenda, int date) {
		return user.deleteAgenda(agenda, date);
	}

	/**
	 * idからユーザの名前を取得する
	 * @param id 名前を取得したいID
	 * @return idに対応する名前
	 */
	public static String getName(String id) {
		return User.getName(id);
	}

	/**
	 * ディレクトリの作成を行う
	 * @param dir 作成したいディレクトリのパス
	 * @return 作成成功時true，失敗時false
	 */
	public static boolean mkdirs(String dir) {
		File directory = new File(dir);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		return true;
	}

	/**
	 * ファイルの拡張子をドット付きで取得する
	 * @param fileName ファイルの名前
	 * @return 拡張子を返す．fileNameがnullのときや拡張子が存在しない時はnull
	 */
	public static String getSuffixWithDot(String fileName) {
		if (fileName == null) {
			return null;
		}
		int point = fileName.lastIndexOf(".");
		if (point != -1) {
			return fileName.substring(point);
		}
		return null;
	}

	/**
	 * ディレクトリと拡張子を除いたファイル名を返す
	 * @param fileName ファイルのパス
	 * @return ディレクトリと拡張子を除いたファイル名
	 */
	public static String getFileNameWithoutSuffix(String fileName) {
		if (fileName == null) return null;
		int point = fileName.lastIndexOf(".");
		if (point != -1) {
			return fileName.substring(fileName.lastIndexOf("/") + 1, point);
		}
		return fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
	}

	/**
	 * MACアドレスからその所有者を特定する
	 * @return MACアドレスに対応した登録されているユーザ
	 */
	public static String getComputerHolder() {
		String nicName = null; // 登録されているNICの表示名
		String macAddress = null; // 登録されているMACアドレス
		ArrayList<String> slaves = Slave.getSlaves();
		Nics nics = new Nics();

		// ファイルからMACアドレスNIC表示名の読み込み
		for(String slave : slaves) {
			try {
				File file = new File(Controller.homeDirName + "/" + slave + "/nics");
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = br.readLine();
				Pattern p = Pattern.compile("(.*):\\[([0-9A-F:]+)");
				Matcher m = p.matcher(line);
				if (m.find()) {
					nicName = m.group(1);
					macAddress = m.group(2);
					for (int i = 0; i < nics.length(); i++) {
						// 一致の検証
						if (nicName.equals(nics.getName(i))
							&& macAddress.equals(nics.getMacAddress(i).toString())) {
							return slave;
						}
					}
				}

				br.close();
			} catch (FileNotFoundException e) {
				// ID未登録時
				return null;
			} catch (IOException | NullPointerException e) {
				Log.error(e);
				return null;
			}
		}

		return null;
	}

	/**
	 * ファイルのコピーを行う
	 * @param in コピー元ファイル
	 * @param out コピー先ファイル
	 * @return ファイルのコピー成功時true，失敗時false
	 * @throws IOException コピー失敗
	 */
	public static boolean copyFile(File in, File out) throws IOException {
		try {
			FileChannel inChannel = new FileInputStream(in).getChannel();
			FileChannel outChannel = new FileOutputStream(out).getChannel();
			inChannel.transferTo(0, inChannel.size(), outChannel);
			inChannel.close();
			outChannel.close();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * ファイルまたはディレクトリの削除を行う
	 * @param f 削除対象
	 */
	public static void deleteFile(File f) {
		if (f.exists() == false) {
			return;
		}
		if(f.isFile()) {
			f.delete();
		} else if(f.isDirectory()) {
			File files[] = f.listFiles();
			for(int i = 0; i < files.length; i++) {
				deleteFile(files[i]);
			}
			f.delete();
		}
	}
}
