package system;

import java.io.File;
import java.lang.Exception;
import java.lang.StackTraceElement;
import java.lang.String;
import java.lang.Throwable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import display.Message;

class Log {
	// 改行コード
	public static final String br = System.getProperty("line.separator");
	private static final Message dialog = new Message();

	// ログファイル
	private static final File logDir = new File("file/root/log/");
	private static final File logFile = new File(logDir.toString() + "log.txt");
	private static final File errorFile = new File(logDir.toString() + "error.txt");

	// 出力時の接頭語・接尾語
	private static final String placePrefix = "  at ";
	private static final String suffix = "エラーが発生しました．この事象はシステム改善のために記録されます．";

	// エラー処理（例外が投げられるとき）
	public static void error(Exception e) {
		// ログの書き出し処理
		writeLog(errorFile, e.toString());

		// ライブラリ以外のエラーのみを出力
		Pattern p = Pattern.compile("^([^j][^a][^v][^a].*)");
		for (StackTraceElement ste : e.getStackTrace()) {
			Matcher m = p.matcher(ste.toString());
			if (m.find()) {
				writeLog(errorFile, placePrefix + m.group(1));
			}
		}

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

	// TODO: ログの書き出し
	public static void writeLog(File file, String message) {
		System.out.println(message);
	}

	// ポップアップ表示
	public static void popup(String message) {
		dialog.showMessage(message);
	}
}
