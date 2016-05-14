import java.lang.String;
import java.util.Calendar;

// 各Slaveの出席を1月分記録
public class AttendanceBook {
    public static final int ATTENDED = 0; // 出席
    public static final int ABSENCE = 1; // 欠席
    public static final int TARDY = 2; // 遅刻
    public static final int LEAVE_EARLY = 3; // 早退
    private Calendar calendar; // いつの情報かを保持
    private String id; // Slaveのid
    private int[] book; // 出席状況

    // 引数：Slaveのid，取得したい月を設定したカレンダー
    public AttendanceBook(String id, Calendar calendar){
        this.id = id;
        this.calendar = calendar;
        book = new int[calendar.getActualMaximum(Calendar.DATE)];
        setBook();
    }

    // TODO:出席情報の読み込み
    private void setBook() {
    }

    // 日ごとの出席状況の取得
    public int getAttendance(int day) {
        return book[day];
    }

    // TODO:出席時間の取得
    public Calendar getAttendedTime(int day) {
        return Calendar.getInstance();
    }

    // TODO:帰宅時間の取得
    public Calendar getReturnTime(int day) {
        return Calendar.getInstance();
    }

    // 取得している年
    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    // 取得している月
    public int getMongh() {
        return calendar.get(Calendar.MONTH);
    }

    // 取得しているSlaveのid
    public String getId() {
        return id;
    }
}
