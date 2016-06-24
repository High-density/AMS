package system;

import java.lang.String;
import java.time.YearMonth;

abstract public class DataForAMonth<T> {
	protected YearMonth yearMonth; // いつの情報を取得しているのか
	protected T[] data; // その日のデータを格納

	/**
	 * 特定の日付のデータを取得する
	 * @param day 取得したい日付
	 */
	abstract public T getData(int day);

	/**
	 * 扱っている年を取得する
	 * @return 年
	 */
	public int getYear() {
		return yearMonth.getYear();
	}

	/**
	 * 扱っている月の日数の最大値を取得する
	 * @return 日付の最大値
	 */
	public int getMaxDate() {
		return yearMonth.lengthOfMonth();
	}
}
