package system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.NullPointerException;
import java.lang.String;
import java.lang.System;
import java.net.NetworkInterface;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class User {
	protected String id = null; // ID
	private String passwd = null; // パスワード
	protected String attribute = null; // 属性

	public User(String id, String passwd) {
		this.id = id;
		this.passwd = passwd;
	}

	// パスワードによる認証
	public static User login(String id, String passwd) {
		String pw = null; // パスワード
		String attribute = null; // 属性

		// ファイルからパスワードの読み込み
		try {
			File file = new File("./file/" + id + "/passwd");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			Pattern p = Pattern.compile(id + ":(.*)$");
			Matcher m = p.matcher(line);
			if (m.find())
				pw = m.group(1);

			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("このIDは登録されていません");
			return null;
		} catch (IOException e) {
			System.out.println("パスワード読み込みエラー: " + e);
			return null;
		} catch (NullPointerException e) {
			System.out.println("パスワード読み込みエラー: " + e);
			return null;
		}

		// パスワードが一致するかどうか検証
		if (pw.equals(passwd) && true/*hasCertifiedMacAddress(id)*/) {
			// ファイルから属性の読み込み
			try {
				File file = new File("./file/user");
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line; // Fileから読み取った行
				Pattern p = Pattern.compile(id + ":(.*)$");
				Matcher m = null;
				boolean finded = false; // マッチしたかどうか

				// 各行を読み込んでマッチするか検証
				while (attribute == null) {
					line = br.readLine();
					m = p.matcher(line);
					if (m.find())
						attribute = m.group(1);
				}

				br.close();
			} catch (FileNotFoundException e) {
				System.out.println("ユーザファイル読み込みエラー: " + e);
				return null;
			} catch (IOException e) {
				System.out.println("属性読み込みエラー: " + e);
				return null;
			} catch (NullPointerException e) {
				System.out.println("属性読み込みエラー: " + e);
				return null;
			}

			// 属性に合わせてユーザの作成
			if (attribute.equals("master")) {
				return new Master(id, passwd);
			} else if (attribute.equals("slave")) {
				return new Slave(id, passwd);
			} else {
				System.out.println("属性読み込みエラー: attribute = " + attribute);
				return null;
			}
		} else {
			System.out.println("パスワードが不正です");
		}

		return null; // 認証失敗
	}

	// 認証されたMacAddressを保持しているか
	private static boolean hasCertifiedMacAddress(String id) {
		String registeredNicName = null; // 登録されているNICの表示名
		String registeredMacAddress = null; // 登録されているMACアドレス
		String gotNicName = null; // 現パソコンのNICの表示名
		String gotMacAddress = ""; // 現パソコンのMACアドレス

		// ファイルからMACアドレスNIC表示名の読み込み
		try {
			File file = new File("./file/" + id + "/nics");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			Pattern p = Pattern.compile(id + ":(.*):\\[([0-9A-F:]+)");
			Matcher m = p.matcher(line);
			if (m.find()) {
				registeredNicName = m.group(1);
				registeredMacAddress = m.group(2);
			}

			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("このIDは登録されていません");
			return false;
		} catch (IOException e) {
			System.out.println("パスワード読み込みエラー: " + e);
			return false;
		} catch (NullPointerException e) {
			System.out.println("パスワード読み込みエラー: " + e);
			return false;
		}

		// MACアドレスの取得
		try {
			// 全NICを取得
			Enumeration<NetworkInterface> gotNics = NetworkInterface.getNetworkInterfaces();
			// 登録MACアドレスと同じものを探す
			while (gotNics.hasMoreElements() && !registeredNicName.equals(gotNicName)) {
				NetworkInterface gotNic = gotNics.nextElement();
				byte[] hardwareAddress = gotNic.getHardwareAddress();
				if (hardwareAddress != null) {
					for (byte b : hardwareAddress) {
						gotMacAddress += String.format("%02X:", b);
					}
					gotMacAddress = gotMacAddress.substring(0, gotMacAddress.length() - 1);
				}
				gotNicName = gotNic.getName();
				// System.out.println(gotNic.getName() + ":" + gotNic.getDisplayName() + " - " + gotMacAddress);
			}
		} catch (IOException e) {
			System.out.println("MACアドレスの取得不可: " + e);
			return false;
		}

		// 登録されているMACアドレスが見つかったか
		if (registeredMacAddress.equals(gotMacAddress) && registeredNicName.equals(gotNicName)) {
			return true;
		} else {
			return false;
		}
	}

	public abstract int setAttendance(); // 出席

	public abstract String[][] getAttendance(Calendar calendar); // 出席表示(idから取得すべき情報を自動判断)

	// 出席表示(id指定)
	protected static String[] getAttendance(String id ,Calendar calendar) {
		int maxDay = calendar.getActualMaximum(Calendar.DATE);
		String attendanceBook[] = new String[maxDay]; // 出席簿
		String year = String.format("%02d", calendar.get(Calendar.YEAR)); // 取得する年
		String month = String.format("%02d", calendar.get(Calendar.MONTH) + 1); // 取得する月
		for (int d = 0; d < maxDay; d++) {
			// ファイルから出席情報の読み込み
			String day = String.format("%02d", d + 1);
			String fileName = year + month + day;
			try {
				// 出席のとき
				File file = new File("./file/" + id + "/attendance/" + fileName);
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = br.readLine();
				Pattern p = Pattern.compile("([0-9]{2})([0-9]{2}):([a-z]+)$");
				Matcher m = p.matcher(line);
				if (m.find()){
					attendanceBook[d] = m.group(1) + m.group(2) + ":" + m.group(3);
				}

				br.close();
			} catch (FileNotFoundException e) {
				// 欠席のとき
				attendanceBook[d] = "absent";
			} catch (IOException e) {
				attendanceBook[d] = "error";
			} catch (NullPointerException e) {
				attendanceBook[d] = "error";
			}
		}

		return attendanceBook;
	}

	public abstract int submitReport(); // 報告書提出

	public abstract int showReport(); // 報告書閲覧

	public abstract int setEvent(); // 情報配信

	public abstract int createUser(); // ユーザの作成

	// setter getter
	public int setId(String id) {
		this.id = id;
		return 0;
	}

	public String getId() {
		return id;
	}

	public int setPasswd(String passwd) {
		this.passwd = passwd;
		return 0;
	}

	public String getAttribute() {
		return attribute;
	}
}
