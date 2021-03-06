package system;

/**
 * アカウントに必要な情報一式を保持するクラス
 * @author Shinichi Yanagido
 * @version 1.1
 */
public class AccountInformation {
	/**
	 * ID
	 */
	private String id;
	/**
	 * 名前
	 */
	private String name;
	/**
	 * パスワード
	 */
	private String passwd;

	/**
	 * コンストラクタ
	 * @param id 対象者のID
	 * @param name 対象者の名前
	 * @param passwd 対象者のパスワード
	 */
	private AccountInformation(String id, String name, String passwd) {
		this.id = id;
		this.name = name;
		this.passwd = passwd;
	}

	/**
	 * パスワードが登録されたAccountInformationのインスタンスを取得する
	 * @param passwd 対象者のパスワード
	 * @return パスワードが設定されたインスタンス
	 */
	public static AccountInformation ofPasswd(String passwd) {
		return new AccountInformation(null, null, passwd);
	}

	/**
	 * idが登録されたAccountInformationのインスタンスを取得する
	 * @param id 対象者のID
	 * @return IDが設定されたインスタンス
	 */
	public static AccountInformation ofId(String id) {
		return new AccountInformation(id, null, null);
	}

	/**
	 * IDとパスワードが登録されたAccountInformationのインスタンスを取得する
	 * @param id 対象者のID
	 * @param passwd 対象者のパスワード
	 * @return IDとパスワードが設定されたインスタンス
	 */
	public static AccountInformation ofIdPasswd(String id, String passwd) {
		return new AccountInformation(id, null, passwd);
	}

	/**
	 * IDと名前が登録されたAccountInformationのインスタンス
	 * @param id 対象者のID
	 * @param name 対象者の名前
	 * @return IDと名前が設定されたインスタンス
	 */
	public static AccountInformation ofIdName(String id, String name) {
		return new AccountInformation(id, name, null);
	}

	/**
	 * IDと名前とパスワードが登録されたAccountInformationのインスタンスを取得する
	 * @param id 対象者のID
	 * @param name 対象者の名前
	 * @param passwd 対象者のパスワード
	 * @return 全てのフィールドが設定されたインスタンス
	 */
	public static AccountInformation ofAll(String id, String name, String passwd) {
		return new AccountInformation(id, name, passwd);
	}

	/**
	 * idフィールドの値を取得する
	 * @return idフィールド
	 */
	public String getId() {
		return id;
	}

	/**
	 * nameフィールドの値を取得する
	 * @return nameフィールド
	 */
	public String getName() {
		return name;
	}

	/**
	 * passwdフィールドの値を取得する
	 * @return passwdフィールド
	 */
	public String getPasswd() {
		return passwd;
	}

	/**
	 * 設定されたアカウント情報が有効なものかどうか
	 * 半角全角チェックを行う
     * @return 有効であればtrue，無効ならfalse
	 */
	public boolean isValid() {
		if (id != null && !isHalfWidth(id) ||
			passwd != null && !isHalfWidth(passwd)) {
			return false;
		}
		return true;
	}

	/**
	 * 含まれる文字が全部半角で構成されているかチェック
	 * @param str チェックしたい文字列
	 * @return 文字列がすべて半角で構成されていたらtrue，それ以外ならfalse
	 */
	public static boolean isHalfWidth(String str) {
		char[] chars = str.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (String.valueOf(chars[i]).getBytes().length >= 2) {
				return false;
			}
		}
		return true;
	}
}
