package system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.NullPointerException;
import java.lang.String;
import java.lang.System;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class User{
    private String id     = null; // ID
    private String passwd = null; // パスワード
    protected String attribute = null; // 属性

    public User(String id, String passwd){
        this.id = id;
        this.passwd = passwd;
    }

    // パスワードによる認証
    public static User login(String id, String passwd){
        String pw = null;        // パスワード
        String attribute = null; // 属性

        // ファイルからパスワードの読み込み
        try{
            File file = new File("./file/" + id + "/passwd");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            Pattern p = Pattern.compile(id + ":(.*)$");
            Matcher m = p.matcher(line);
            if(m.find())
                pw = m.group(1);

            br.close();
        }catch(FileNotFoundException e){
            System.out.println("このIDは登録されていません");
            return null;
        }catch(IOException e){
            System.out.println("パスワード読み込みエラー: " + e);
            return null;
        }catch(NullPointerException e){
            System.out.println("パスワード読み込みエラー: " + e);
            return null;
        }

        // パスワードが一致するかどうか検証
        if(pw.equals(passwd)){
            // ファイルから属性の読み込み
            try{
                File file = new File("./file/user");
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;            // Fileから読み取った行
                Pattern p = Pattern.compile(id + ":(.*)$");
                Matcher m = null;
                boolean finded = false; // マッチしたかどうか

                // 各行を読み込んでマッチするか検証
                while(attribute == null){
                    line = br.readLine();
                    m = p.matcher(line);
                    if(m.find())
                        attribute = m.group(1);
                }

                br.close();
            }catch(FileNotFoundException e){
                System.out.println("ユーザファイル読み込みエラー: " + e);
                return null;
            }catch(IOException e){
                System.out.println("属性読み込みエラー: " + e);
                return null;
            }catch(NullPointerException e){
                System.out.println("属性読み込みエラー: " + e);
                return null;
            }

            if(attribute.equals("master")){
                return new Master(id, passwd);
            }else if(attribute.equals("slave")){
                return new Slave(id, passwd);
            }else{
                System.out.println("属性読み込みエラー: attribute = " + attribute);
                return null;
            }
        }else{
            System.out.println("パスワードが不正です");
        }

        return null; // 認証失敗
    }

    public abstract int setAttendance();  // 出席
    public abstract int showAttendance(); // 出席表示
    public abstract int submitReport();   // 報告書提出
    public abstract int showReport();     // 報告書閲覧
    public abstract int setEvent();       // 情報配信
    public abstract int createUser();     // ユーザの作成

    // setter getter
    public int setId(String id){
        this.id = id;
        return 0;
    }

    public String getId(){
        return id;
    }

    public int setPasswd(String passwd){
        this.passwd = passwd;
        return 0; // 成功時
    }

    public String getAttribute(){
        return attribute;
    }
}
