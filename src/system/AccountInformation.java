package system;

public class AccountInformation {
	private String id; // ID
	private String name; // 名前
	private String passwd; // パスワード

	private AccountInformation(String id, String name, String passwd) {
		this.id = id;
		this.name = name;
		this.passwd = passwd;
	}

	public static AccountInformation ofPasswd(String passwd) {
		return new AccountInformation(null, null, passwd);
	}

	public static AccountInformation ofIdPasswd(String id, String passwd) {
		return new AccountInformation(id, null, passwd);
	}

	public static AccountInformation ofAll(String id, String name, String passwd) {
		return new AccountInformation(id, name, passwd);
	}

	// getter
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPasswd() {
		return passwd;
	}
}
