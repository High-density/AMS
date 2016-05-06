package system;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.String;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.lang.System;

class Slave extends User{
    public Slave(String id, String passwd){
        super(id, passwd);
        attribute = "slave";
    }

    // 出席
    public int setAttendance(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        // 各自のフォルダに日付ごとにファイルを作り，出席状況を格納する
        try{
            File file = new File("./file/" + getId() + "/attendance/" + sdf.format(c.getTime()));
            // 未出席時のみ記録
            if(!file.exists()){
                file.createNewFile();
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
                sdf.applyPattern("HHmm");
                pw.println(sdf.format(c.getTime()) + ":attended");
                pw.close();
            }
        }catch(IOException e){
            System.out.println("ファイル書き込みエラー: " + e);
            return 1;
        }
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
