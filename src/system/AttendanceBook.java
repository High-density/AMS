package system;

import java.lang.String;
import java.time.LocalTime;
import java.time.YearMonth;

/**
 * 出席簿クラス<br>
 * 各Slaveの出欠を一月分保持する
 * @author Shinichi Yanagido
 * @version 1.0
*/
public class AttendanceBook {
	/**
	 * 出席状況の取得エラーコード
	 */
	public static final int ERROR = -1;
	/**
	 * 出席コード
	 */
	public static final int ATTENDED = 0;
	/**
	 * 欠席コード
	 */
	public static final int ABSENCE = 1;
	/**
	 * 遅刻コード
	 */
	public static final int TARDY = 2;
	/**
	 * 早退コード
	 */
	public static final int LEAVE_EARLY = 3;

	private YearMonth yearMonth; // いつの情報を取得しているのか
	private LocalTime attendedTime[]; // 出席時刻
	private LocalTime returnTime[]; // 下校時刻
	private String id; // Slaveのid
	private int[] book; // 出席状況

	/**
	 * コンストラクタ
	 * @param id SlaveのID
	 * @param ym 取得したい年月
	 */
	public AttendanceBook(String id, YearMonth ym){
		yearMonth = ym;
		this.id = id;
		int maxDate = ym.lengthOfMonth();
		book = new int[maxDate];
		attendedTime = new LocalTime[maxDate];
		returnTime = new LocalTime[maxDate];
	}

	/**
	 * 出席簿に出欠情報の書き込みを行う
	 * @param day 日付
	 * @param status 出欠コード
	 */
	public void setBook(int day, int status) {
		book[day] = status;
	}

	/**
	 * 出席簿に出欠情報の書き込みを行う
	 * @param day 日付
	 * @param status 出欠コード
	 * @param attendedTime 出席時刻
	 */
	public void setBook(int day, int status, LocalTime attendedTime) {
		setBook(day, status);
		this.attendedTime[day] = attendedTime;
	}

	/**
	 * 出席簿に出欠情報の書き込みを行う
	 * @param day 日付
	 * @param status 出欠コード
	 * @param attendedTime 出席時刻
	 * @param returnTime 帰宅時刻
	 */
	public void setBook(int day, int status, LocalTime attendedTime, LocalTime returnTime) {
		setBook(day, status, attendedTime);
		this.returnTime[day] = returnTime;
	}

	/**
	 * 特定の日付の出席状況を取得する
	 * @param day 日付
	 * @return 出欠コード
	 */
	public int getStatus(int day) {
		int maxDate = yearMonth.lengthOfMonth();
		if (0 <= day && day < maxDate) {
			return book[day];
		} else {
			return AttendanceBook.ERROR;
		}
	}

	/**
	 * 特定の日付の出席時間を取得する
	 * @param day 日付
	 * @return 出席時刻
	 */
	public LocalTime getAttendedTime(int day) {
		return attendedTime[day];
	}

	/**
	 * 特定の日付の帰宅時間を取得する
	 */
	public LocalTime getReturnTime(int day) {
		return returnTime[day];
	}

	/**
	 * 出席簿が扱っている年を取得する
	 * @return 年
	 */
	public int getYear() {
		return yearMonth.getYear();
	}

	/**
	 * 出席簿が扱っている月を取得する
	 * @return 月
	 */
	public int getMonth() {
		return yearMonth.getMonthValue();
	}

	/**
	 * 出席簿が扱っている月の日数の最大値を取得する
	 * @return 日付の最大値
	 */
	public int getMaxDate() {
		return yearMonth.lengthOfMonth();
	}

	/**
	 * 出席簿が扱っているSlaveのIDを取得する
	 * @return 対象者のID
	 */
	public String getId() {
		return id;
	}
}
