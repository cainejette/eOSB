package eOSB.game.controller;

public class NoRoundFoundException extends Exception {

	private String name;
	
	public NoRoundFoundException(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
