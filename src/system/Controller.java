package system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 * 内部の動作を一括で管理するクラス<br>
 * GUIとのやりとりはここを通して行う
 * @author Shinichi Yanagido
 * @version 1.3
 */
public class Controller {
	private User user; // ログインしているユーザ
	// 予定ファイルの置き場所
	public static final String homeDirName = "file";
	public static final File agendaDir = new File(homeDirName + "/root/agenda/");
	public static final File logDir = new File(homeDirName + "/root/log/");
	public static final File logFile = new File(logDir.toString() + "/log.txt");
	public static final File errorFile = new File(logDir.toString() + "/error.txt");

	public Controller() {
		Log.logDir = logDir;
		Log.logFile = logFile;
		Log.errorFile = errorFile;
	}

	/**
	 * ログインを行う<br>
	 * IDに沿ったインスタンスを作成する
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
			return true;
		}
	}

	/**
	 * ログアウトを行う
	 * @return ログアウト成功時true，失敗時false
	 */
	public boolean logout() {
		user = null;
		return true;
	}

	/**
	 * 報告書の提出を行う
	 * @param file 提出対象へのファイルパス
	 * @return 提出成功時true，失敗時false
	 */
	public boolean submitReport(String file) {
		return user.submitReport(file);
	}

	/**
	 * 報告書の入ったディレクトリをファイルマネージャで開く<br>
	 * どのディレクトリを開けばいいかをログイン中のIDから判断して開く
	 * @return ディレクトリが開けた時true，どのディレクトリを開いたらいいか判断がつかない等で開けない時false
	 */
	public boolean showReport() {
		return user.showReport();
	}

	/**
	 * 報告書の入ったディレクトリをファイルマネージャで開く
	 * @param id 開きたいディレクトリの所有者ID
	 * @return ディレクトリが開けた時true，開けなかった時false
	 */
	public boolean showReport(String id) {
		return user.showReport(id);
	}

	// 最終更新日の取得
	public LocalDate getLastUploadDate(String id) {
		File fileDir = new File(homeDirName + "/" + id + "/" + User.reportDirName);
		String reports[] = fileDir.list();
		if (reports == null) return null;
		return LocalDate.parse(getFileNameWithoutSuffix(reports[reports.length - 1]));
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
	 */
	public boolean changeAttendance(LocalDate ld, String id, int status) {
		return user.changeAttendance(ld, id, status);
	}

	/**
	 * 出席状況の取得を行う
	 * @param ym 取得したい年月
	 * @return 取得した各学生の一月分の出席簿配列<br>
	 * @see AttendanceBook
	 */
	public AttendanceBook[] getAttendance(YearMonth ym) {
		return user.getAttendance(ym);
	}

	/**
	 * アカウントの更新を行う
	 * @param oldAccount 現在のアカウント情報
	 * @param newAccount 更新したいアカウント情報
	 * @return 更新成功時true，失敗時false
	 */
	public boolean setAccount(AccountInformation oldAccount, AccountInformation newAccount) {
		return user.setAccount(oldAccount, newAccount);
	}

	public boolean createUser(AccountInformation account) {
		return user.createUser(account);
	}

	public boolean deleteUser(String id) {
		return user.deleteUser(id);
	}

	/**
	 * 予定の取得を行う
	 * @param ym 予定を取得したい年月
	 * @return 予定が入ったAgendaクラス
	 */
	public Agenda getAgenda(YearMonth ym) {
		// 予定格納先
		return new Agenda(ym, agendaDir);
	}

	/**
	 * 予定の更新を行う
	 * @param agenda 予定を更新したいAgendaクラス
	 * @param date 予定を更新したい日付
	 * @param content 予定内容
	 */
	public Agenda setAgenda(Agenda agenda, int date, String content) {
		agenda.setData(date, content);
		return agenda;
	}

	/**
	 * idからユーザの名前を取得する
	 * @param id 名前を取得したいID
	 * @return idに対応する名前
	 */
	public String getName(String id) {
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

	// 拡張子とディレクトリ名を削除する
	public static String getFileNameWithoutSuffix(String fileName) {
		if (fileName == null) return null;
		int point = fileName.lastIndexOf(".");
		if (point != -1) {
			return fileName.substring(fileName.lastIndexOf("/") + 1, point);
		}
		return fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length() - 1);
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
