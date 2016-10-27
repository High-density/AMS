package system;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
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

	// ポップアップ表示
	public static void popup(String message) {
		JOptionPane.showMessageDialog(null, message, "ERROR", JOptionPane.ERROR_MESSAGE);
	}
}
