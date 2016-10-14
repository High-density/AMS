package main;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

class Main{ /* スタータークラス */
	public static void main(String args[]){
		// TODO : 削除
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Metal".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look and feel.
		}
		new display.Login();
	}
}
