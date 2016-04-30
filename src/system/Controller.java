package system;

import java.lang.Integer;
import java.lang.String;
import java.lang.System;
import java.util.ArrayList;

public class Controller{
    private ArrayList<User> users; // 教員学生を含めた全ユーザ
    private int numOfMasters;      // 教員総数
    private int numOfSlaves;       // 学生総数
    private Integer authenticated = null; // ログオンされているユーザ番号

    public Controller(){
        // 全ユーザを配列管理
        // 教員，学生の順で格納
        users = new ArrayList<User>();
        numOfMasters = 1;
        // for(int n = 0; n < numOfMasters; n++)
        //     users.add(new Master());
        // numOfSlaves = 0;
        // for(int n = 0; n < numOfSlaves; n++)
        //     users.add(new Slave());
        users.add(new Master("root", "teacher", "root"));
        numOfSlaves = 3;
        users.add(new Slave("s12507", "student", "s12507"));
        users.add(new Slave("s12547", "student", "s12547"));
        users.add(new Slave("s12548", "student", "s12548"));
    }

    // ユーザ総数
    private int numOfUsers(){
        return numOfMasters + numOfSlaves;
    }

    // ログイン
    public int login(String id, String passwd){
        // ログインできたらauthenticatedにユーザ番号が入る
        // 失敗したらnull
        authenticated = null;
        for(int n = 0; n < numOfUsers(); n++){
            if(id.equals(users.get(n).getId())){
                if(users.get(n).authenticate(passwd) == 0){
                    authenticated = n;
                    return 0; // 認証が通った
                }else{
                    return 1; // passwdが不正
                }
            }
        }

        return 2; // IDが不正
    }

    // ログアウト
    public int logout(){
        authenticated = null;
        return 0;
    }

    // TODO:報告書の提出
    public int submitReport(){
        return 0;
    }

    // TODO:報告書の閲覧
    public int showReport(String id/*, 日付*/){
        return 0;
    }

    // TODO:出席の登録
    public int attend(){
        return 0;
    }

    // TODO:出席の表示
    public int showAttendance(){
        return 0;
    }

    // TODO:アカウント作成
    public int setAccount(/*アカウントに必要なデータ*/){
        return 0;
    }
}
