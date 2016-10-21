package system;

import java.time.YearMonth;
import java.util.ArrayList;

/**
 * 一月分のデータを扱うクラス
 * @author Shinichi Yanagido
 * @version 1.2
 */
abstract public class DataForAMonth<T> {
	/**
	 * いつの情報を取得しているのか
	 */
	protected YearMonth yearMonth;
	/**
	 * その日のデータを格納
	 */
	protected ArrayList<T> data;

	/**
	 * 使用する日付の指定されたコンストラクタ
	 * @param ym 保持しておきたい年月
	 */
	public DataForAMonth(YearMonth ym) {
		yearMonth = ym;
		data = new ArrayList<T>(getMaxDate());
		for (int d = 0; d < getMaxDate(); d++) {
			data.add(d, null);
		}
	}

	/**
	 * 指定された日付にデータがあるかどうか
	 * @param day 確認したい日付-1
	 * @return データが入っていればtrue，入っていなければfalse
	 */
	public boolean hasData(int day) {
		if (0 <= day && day < getMaxDate() && data.get(day) != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 特定の日付のデータを取得する
	 * @param day 取得したい日付-1
	 * @return その日のデータ，データが入ってなければnull
	 */
	public T getData(int day) {
		if (0 <= day && day < getMaxDate()) {
			return data.get(day);
		} else {
			return null;
		}
	}

	/**
	 * 扱っている年を取得する
	 * @return 年
	 */
	public int getYear() {
		return yearMonth.getYear();
	}

	/**
	 * 扱っている月を取得する
	 * @return 月
	 */
	public int getMonth() {
		return yearMonth.getMonthValue();
	}

	/**
	 * 扱っている月の日数の最大値を取得する
	 * @return 日付の最大値
	 */
	public int getMaxDate() {
		return yearMonth.lengthOfMonth();
	}

	/**
	 * データの書き込みを行う
	 * @param day 日付-1
	 * @param d 書き込みたいデータ
	 */
	public void setData(int day, T d) {
		data.set(day, d);
	}
}
