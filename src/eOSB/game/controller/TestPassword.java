package eOSB.game.controller;

public class TestPassword {

	public static void main (String[] args) {
		String password = "mop123";
		
		try {
			String encryptPass = Password.getSaltedHash(password);
			System.out.println("salt+hash: " + encryptPass);
			System.out.println("same? " + Password.check(password, encryptPass));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
