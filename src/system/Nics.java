package system;

import java.io.IOException;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.NoSuchElementException;

// NICの情報を一通り保持するクラス
// コンピュータのMACアドレスの情報を一通り取得
public class Nics {
	private ArrayList<String> names = new ArrayList<String>();
	private ArrayList<MacAddress> macs = new ArrayList<MacAddress>();

	public Nics() {
		// MACアドレスファイル作成
		try {
			// 全NICを取得
			Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces();
			while(nics.hasMoreElements()) {
				NetworkInterface nic = nics.nextElement();
				MacAddress mac = new MacAddress();
				mac.data = nic.getHardwareAddress();
				macs.add(mac);
				names.add(nic.getName());
			}
		} catch(IOException | NoSuchElementException e) {
			Log.error(e);
		}
	}

	// NICの名前を取得する
	public String getName(int i) {
		return names.get(i);
	}

	// MACアドレスを取得する
	public MacAddress getMacAddress(int i) {
		return macs.get(i);
	}

	// 保持しているMACアドレスの総数
	public int length() {
		return names.size();
	}

	// MACアドレスのみを管理するインナークラス
	public class MacAddress {
		public byte data[];

		@Override
		public String toString() {
			String address = "";
			if (data != null) {
				for (byte b : data) {
					address += String.format("%02X:", b);
				}
				address = address.substring(0, address.length() - 1);
			}
			return address;
		}
	}
}
