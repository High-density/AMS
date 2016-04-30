package system;

import java.lang.String;

public class User{
    private String name   = null; // 表示名
    private String id     = null; // 名前(ID)
    private String passwd = null; // パスワード

    // 設定の読み込み
    public User(String id, String name, String passwd){
        setId(id);
        setName(name);
        setPasswd(passwd);
    }

    // パスワードによる認証
    public int authenticate(String passwd){
        if(passwd.equals(this.passwd)){
            return 0;
        }else{
            return 1;
        }
    }

    // setter getter
    public int setName(String name){
        this.name = name;
        return 0;
    }

    public String getName(){
        return name;
    }

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
}
