package system;

import java.lang.String;
import java.time.YearMonth;
import java.util.ArrayList;

abstract public class DataForAMonth<T> {
	protected YearMonth yearMonth; // いつの情報を取得しているのか
	protected ArrayList<T> data; // その日のデータを格納

	public DataForAMonth(YearMonth ym) {
		yearMonth = ym;
		data = new ArrayList<T>(getMaxDate());
		for (int d = 0; d < getMaxDate(); d++) {
			data.add(d, null);
		}
	}

	/**
	 * 特定の日付のデータを取得する
	 * @param day 取得したい日付
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
	 * @param data 書き込みたいデータ
	 */
	public void setData(int day, T d) {
		data.set(day, d);
	}
}
