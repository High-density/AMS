package system;

import java.lang.String;

class Slave extends User{
    public Slave(String id, String passwd){
        super(id, passwd);
    }

    // TODO:出席
    public int setAttendance(){
        return 0;
    }

    // TODO:出席表示
    public int showAttendance(){
        return 0;
    }

    // TODO:報告書提出
    public int submitReport(){
        return 0;
    }

    // TODO:報告書閲覧
    public int showReport(){
        return 0;
    }

    // Slaveは情報配信不可
    public int setEvent(){
        return 1;
    }

    // Slaveはユーザの作成不可
    public int createUser(){
        return 1;
    }
}
