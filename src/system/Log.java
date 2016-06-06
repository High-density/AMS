package system;

import java.lang.Exception;
import java.lang.StackTraceElement;
import java.lang.String;
import java.lang.Throwable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Log {
	// 改行コード
	public static final String br = System.getProperty("line.separator");
	private static final String placePrefix = "	 at ";
	// エラー時の接尾語
	private static final String suffix = "エラーが発生しました．この事象はシステム改善のために記録されます．";

	// エラー処理（例外が投げられるとき）
	public static void error(Exception e) {
		// ログの書き出し処理
		writeError(e.toString());
		// ライブラリ以外のエラーのみを出力
		Pattern p = Pattern.compile("^([^j][^a][^v][^a].*)");
		for (StackTraceElement ste : e.getStackTrace()) {
			Matcher m = p.matcher(ste.toString());
			if (m.find()) {
				writeError(placePrefix + m.group(1));
			}
		}

		// ポップアップによるエラー表示
		String message = e.getClass().getName();
		message += br + suffix;
		showError(message);
	}

	// エラー処理（内容を直接受け取る）
	public static void error(Throwable t, String message) {
		// ログの書き出し処理
		StackTraceElement ste = t.getStackTrace()[0];
		writeError(message);
		String methodName = ste.getClassName() + "." + ste.getMethodName(); // クラスとメソッド名
		String fileLine = ste.getFileName() + ":" + ste.getLineNumber(); // ファイル名と行番号
		writeError(placePrefix + methodName + "(" + fileLine + ")");

		// ポップアップによるエラー表示
		showError(message + br + suffix);
	}

	// TODO:ログの書き出し
	public static void writeLog() {
	}

	// TODO:エラーの書き出し
	private static void writeError(String message) {
		System.out.println(message);
	}

	// TODO:ポップアップ表示
	public static void popup(String message) {
		System.out.println(message);
	}

	// TODO:不正の書き出し
	public static void writeCorruption() {
	}

	// TODO:ポップアップによるエラー
	private static void showError(String message) {
		// System.out.println(message);
	}
}
