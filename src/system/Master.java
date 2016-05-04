package system;

import java.lang.String;

class Master extends User{
    public Master(String id, String passwd){
        super(id, passwd);
    }

    // Masterは出席不可
    public int setAttendance(){
        return 1;
    }

    // TODO:出席表示
    public int showAttendance(){
        return 0;
    }

    // Masterは報告書提出不可
    public int submitReport(){
        return 1;
    }

    // TODO:報告書閲覧
    public int showReport(){
        return 0;
    }

    // TODO:マスターからの情報発信
    public int setEvent(){
        return 0;
    }

    // TODO:ユーザの作成
    public int createUser(){
        return 0;
    }
}
