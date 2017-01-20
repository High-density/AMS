package system;

public class CheckRepaint {
	public static Boolean repaintFlag = false;
	public static void beTrue(){
		repaintFlag = true;
		// System.out.println("true");
	}
	public static void beFalse(){
		repaintFlag = false;
		// System.out.println("");
	}
	public static boolean checkFlag(){
		return repaintFlag;
	}
}
