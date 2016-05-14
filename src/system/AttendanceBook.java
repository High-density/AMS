import java.lang.String;
import java.time.LocalTime;
import java.time.YearMonth;

// 各Slaveの出席を1月分記録
public class AttendanceBook {
	public static final int ERROR = -1; // エラー
	public static final int ATTENDED = 0; // 出席
	public static final int ABSENCE = 1; // 欠席
	public static final int TARDY = 2; // 遅刻
	public static final int LEAVE_EARLY = 3; // 早退

	private YearMonth yearMonth; // いつの情報を取得しているのか
	private LocalTime attendedTime[]; // 出席時刻
	private LocalTime returnTime[]; // 下校時刻
	private String id; // Slaveのid
	private int[] book; // 出席状況

	// 引数：Slaveのid，取得したい月を設定したカレンダー
	public AttendanceBook(String id, YearMonth ym){
		yearMonth = ym;
		this.id = id;
		int maxDate = ym.lengthOfMonth();
		book = new int[maxDate];
		attendedTime = new LocalTime[maxDate];
		returnTime = new LocalTime[maxDate];
	}

	// 出席情報の書き込み
	public void setBook(int day, int status) {
		book[day] = status;
	}

	public void setBook(int day, int status, LocalTime attendedTime, LocalTime returnTime) {
		setBook(day, status);
		this.attendedTime[day] = attendedTime;
		this.returnTime[day] = returnTime;
	}

	// 日ごとの出席状況の取得
	public int getStatus(int day) {
		return book[day];
	}

	// 出席時間の取得
	public LocalTime getAttendedTime(int day) {
		return attendedTime[day];
	}

	// 帰宅時間の取得
	public LocalTime getReturnTime(int day) {
		return returnTime[day];
	}

	// 取得している年
	public int getYear() {
		return yearMonth.getYear();
	}

	// 取得している月
	public int getMonth() {
		return yearMonth.getMonthValue();
	}

	// 取得しているSlaveのid
	public String getId() {
		return id;
	}
}
