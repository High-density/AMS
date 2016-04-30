package system;

import java.lang.String;

class Slave extends User{
    public Slave(String id, String name, String passwd){
        super(id, name, passwd);
    }

    // TODO:出席
    public int setAttendance(){
        return 0;
    }

    // TODO:報告書提出
    public int submitReports(){
        return 0;
    }
}
