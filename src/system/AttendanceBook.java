package system;

import java.time.LocalTime;
import java.time.YearMonth;

/**
 * 出席簿クラス<br>
 * 各Slaveの出欠を一月分保持する
 * @author Shinichi Yanagido
 * @version 1.1
*/
public class AttendanceBook extends DataForAMonth<Integer> {
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
	 * 公欠コード
	 */
	public static final int AUTHORIZED_ABSENCE = 2;
	/**
	 * 無印
	 */
	public static final int NO_MARK = 3;

	private LocalTime attendedTime[]; // 出席時刻
	private LocalTime returnTime[]; // 下校時刻
	private String id; // Slaveのid

	/**
	 * コンストラクタ
	 * @param id SlaveのID
	 * @param ym 取得したい年月
	 */
	public AttendanceBook(String id, YearMonth ym) {
		super(ym);
		this.id = id;
		attendedTime = new LocalTime[getMaxDate()];
		returnTime = new LocalTime[getMaxDate()];
	}

	/**
	 * 出席簿に出欠情報の書き込みを行う
	 * @param day 日付
	 * @param status 出欠コード
	 * @param attendedTime 出席時刻
	 */
	public void setData(int day, int status, LocalTime attendedTime) {
		setData(day, status);
		this.attendedTime[day] = attendedTime;
	}

	/**
	 * 出席簿に出欠情報の書き込みを行う
	 * @param day 日付
	 * @param status 出欠コード
	 * @param attendedTime 出席時刻
	 * @param returnTime 帰宅時刻
	 */
	public void setData(int day, int status, LocalTime attendedTime, LocalTime returnTime) {
		setData(day, status, attendedTime);
		this.returnTime[day] = returnTime;
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
	 * 出席簿が扱っているSlaveのIDを取得する
	 * @return 対象者のID
	 */
	public String getId() {
		return id;
	}
}
