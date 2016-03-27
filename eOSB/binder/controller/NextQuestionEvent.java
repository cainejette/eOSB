package eOSB.binder.controller;

import eOSB.game.controller.Turn;

public class NextQuestionEvent implements QuestionEvent {

	private Object source;
	private boolean enableTeamA;
	private boolean enableTeamB;
	private Turn turn;
	
	public NextQuestionEvent(Object source, boolean enableTeamA, boolean enableTeamB, Turn turn) {
		this.source = source;
		System.out.println("next question event: " + enableTeamA + " / " + enableTeamB);
		this.enableTeamA = enableTeamA;
		this.enableTeamB = enableTeamB;
		this.turn = turn;
	}
	
	@Override
	public Object getSource() {
		return this.source;
	}
	
	public boolean getEnableTeamA() {
		return this.enableTeamA;
	}
	
	public boolean getEnableTeamB() {
		return this.enableTeamB;
	}
	
	public Turn getTurn() {
		return this.turn;
	}
}
