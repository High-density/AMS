package system;

import java.lang.Integer;
import java.lang.String;
import java.lang.System;

public class Controller{
    private User user; // ログインしているユーザ

    public Controller(){
    }

    // ログイン
    public int login(String id, String passwd){
        user = User.login(id, passwd);
        if(user == null)
            return 1;
        else
            return 0;
    }

    // ログアウト
    public int logout(){
        user = null;
        return 0;
    }

    // TODO:報告書の提出
    public int submitReport(/*id, date*/){
        return 0;
    }

    // TODO:報告書の閲覧
    public int showReport(/* date */){
        return 0;
    }

    // TODO:出席の登録
    public int attend(){
        //return users.get(no).setAttendance();
        return 0;
    }

    // TODO:出席の表示
    public int showAttendance(/* date */){
        return 0;
    }

    // TODO:アカウント設定
    public int setAccount(/*アカウントに必要なデータ*/){
        return 0;
    }
}
