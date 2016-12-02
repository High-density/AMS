package system;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.lang.Exception;
import java.lang.StackTraceElement;
import java.lang.String;
import java.lang.Throwable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDateTime;
import javax.swing.JOptionPane;

class Log {
	// 改行コード
	public static final String br = System.getProperty("line.separator");

	// ログファイル
	static File logDir;
	static File logFile;
	static File errorFile;

	// 出力時の接頭語・接尾語
	private static final String placePrefix = "  at ";
	private static final String suffix = "エラーが発生しました．この事象はシステム改善のために記録されます．";
	// 暗号化用パスワード
	private static final String passwd = "LA-zmr!)NqCf+J.i~B/OcVOOo/*m)S!n,KZr4VpL";

	// エラー処理（例外が投げられるとき）
	public static void error(Exception e) {
		// ログの書き出し処理
		writeLog(errorFile, "[" + LocalDateTime.now() + "]");
		writeLog(errorFile, e.toString());

		// ライブラリ以外のエラーのみを出力
		Pattern p = Pattern.compile("^([^j][^a][^v][^a].*)");
		for (StackTraceElement ste : e.getStackTrace()) {
			Matcher m = p.matcher(ste.toString());
			if (m.find()) {
				writeLog(errorFile, placePrefix + m.group(1));
			}
		}

		// 区切り
		writeLog(errorFile, "");

		// ポップアップによるエラー表示
		String message = "(" + e.getClass().getName() + ")";
		popup(suffix + br + message);
	}

	// エラー処理（内容を直接受け取る）
	public static void error(Throwable t, String message) {
		// ログの書き出し処理
		StackTraceElement ste = t.getStackTrace()[0];
		String methodName = ste.getClassName() + "." + ste.getMethodName(); // クラスとメソッド名
		String fileLine = ste.getFileName() + ":" + ste.getLineNumber(); // ファイル名と行番号
		writeLog(errorFile, message);
		writeLog(errorFile, placePrefix + methodName + "(" + fileLine + ")");

		// ポップアップによるエラー表示
		popup(suffix + br + "(" + message + ")");
	}

	// ログの書き出し
	public static void writeLog(File file, String message) {
		// ファイルが存在しないときに作成する
		Controller.mkdirs(logDir.toString());
		try {
			file.createNewFile();
		} catch(IOException e) {
			popup("ファイル作成エラー: " + file.toString());
		}

		// ファイルに書き込み
		if (file.canWrite() == false) {
			popup("ファイル書き込みエラー: file.canWrite() = false");
		}
		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));) {
			pw.println(message);
		} catch(IOException e) {
			popup("ファイル書き込みエラー: " + file.toString());
		}
	}

	// 指定したファイルに指定された文字列を暗号化して記述する
	public static boolean writeInCipher(File file, String message, boolean add) {
		(new File(file.getParent())).mkdirs(); // ディレクトリがない場合に作成
		try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, add)))) {
			pw.println(Cryptography.encrypt(message, passwd));
			return true;
		} catch(IOException e) {
			error(e);
			return false;
		}
	}

	// 暗号化した文字列をファイルから読み込む
	public static String readInPlainText(File file) {
		String text = ""; // ファイルに記述されているテキストの平文

		// 各行をデコードして格納
		String line;
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			while ((line = reader.readLine()) != null) {
				text += Cryptography.decrypt(line, passwd) + System.getProperty("line.separator");
			}
		} catch(IOException e) {
			Log.error(e);
			return null;
		}

		return text.trim();
	}

	// ポップアップ表示
	public static void popup(String message) {
		JOptionPane.showMessageDialog(null, message, "ERROR", JOptionPane.ERROR_MESSAGE);
	}
}
