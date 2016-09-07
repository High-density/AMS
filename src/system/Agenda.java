package system;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.YearMonth;

/**
 * 各月の予定を一月分保持するクラス
 * @author Shinichi Yanagido
 * @version 1.1
 */
public class Agenda extends DataForAMonth<String> {
	private File dir;

	/**
	 * コンストラクタ
	 * @param ym 予定を取得したい年月
	 * @param dir 予定の保存先
	 */
	public Agenda(YearMonth ym, File dir) {
		super(ym);

		// ディレクトリの作成
		this.dir = dir;
		if (!Controller.mkdirs(dir.toString())) return;

		// 格納してある予定データの取得
		String fileName = dir + "/" + ym.toString() + "-";
		for (int day = 0; day < ym.lengthOfMonth(); day++) {
			File file = new File(fileName + String.format("%02d", day));
			if (file.exists()) {
				// ファイルが存在したら，ファイル内のデータを全て読み込む
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					String line, content = null;
					if ((line = br.readLine()) != null) {
						content = line;
						while ((line = br.readLine()) != null) {
							content = System.getProperty("line.separator") + line;
						}
					}
					set(day, content);
				} catch (IOException e) {
					Log.error(e);
				}
			} else {
				// ファイルが存在しなかったら，nullをセット
				set(day, null);
			}
		}
	}

	/**
	 * 予定を追加して，予定を保存したファイルを更新する
	 * @param day 対象の日付-1
	 * @param d 格納したい文字列
	 */
	public void setData(int day, String d) {
		super.setData(day, d);

		try {
			String fileName = dir + "/" + yearMonth.toString() + "-" + String.format("%02d", day);
			File file = new File(fileName);
			if (!file.exists()) file.createNewFile();

			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			pw.println(d);
			pw.close();
		} catch (IOException e) {
			Log.error(e);
		}
	}

	/**
	 * 予定の追加をするが，ファイルの更新はしない
	 * @param day 対象の日付-1
	 * @param d 格納したい文字列
	 */
	public void set(int day, String d) {
		super.setData(day, d);
	}
}
