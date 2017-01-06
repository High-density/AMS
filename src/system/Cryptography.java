package system;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.xml.bind.DatatypeConverter;

public class Cryptography {
	// ソルト値
	private static final byte[] salt = {(byte)0xc7, (byte)0x73, (byte)0x21,
								  (byte)0x8c, (byte)0x7e, (byte)0xc8,
								  (byte)0xee, (byte)0x99};
	private static final int count = 2048; // イテレーションカウント値
	private static final PBEParameterSpec pps = new PBEParameterSpec(salt, count);
	private static final String passwd = "LA-zmr!)NqCf+J.i~B/OcVOOo/*m)S!n,KZr4VpL";

	public static String encrypt(String text, String passwd) {
		byte[] encrypted; // 暗号化した数字列

		// 暗号化してbyte配列に
		try {
			PBEKeySpec pks = new PBEKeySpec(passwd.toCharArray());
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey key = skf.generateSecret(pks);
			Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
			cipher.init(Cipher.ENCRYPT_MODE, key, pps);
			encrypted = cipher.doFinal(text.getBytes());
		} catch (NoSuchAlgorithmException | InvalidKeySpecException |
				 InvalidKeyException | IllegalBlockSizeException |
				 NoSuchPaddingException | InvalidAlgorithmParameterException |
				 BadPaddingException e) {
			Log.error(e);
			return null;
		}

		// byte配列を文字列にして返す
		String result = "";
		for (byte b : encrypted) {
			result += String.format("%02x", b & 0xff);
		}
		return result.trim();
	}

	public static String encrypt(String text) {
		return encrypt(text, passwd);
	}

	public static String decrypt(String ciphertext, String passwd) {
		// 暗号化したbyte配列
		byte[] encrypted = DatatypeConverter.parseHexBinary(ciphertext);
		String text = null; // 復号後文字列

		// 文字列を復号
		try {
			PBEKeySpec pks = new PBEKeySpec(passwd.toCharArray());
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey key = skf.generateSecret(pks);
			Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
			cipher.init(Cipher.DECRYPT_MODE, key, pps);
			text = new String(cipher.doFinal(encrypted));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException |
				 InvalidKeyException | NoSuchPaddingException |
				 InvalidAlgorithmParameterException | IllegalBlockSizeException |
				 BadPaddingException e) {
			Log.error(e);
			return null;
		}

		return text;
	}

	public static String decrypt(String ciphertext) {
		return decrypt(ciphertext, passwd);
	}
}
