package eOSB.binder.controller;

import eOSB.game.controller.Question;

public class UpdateQuestionEvent implements QuestionEvent {

	private Object source;
	private Question question;
	private boolean enableTeamA;
	private boolean enableTeamB;

	public UpdateQuestionEvent(Object source, Question question, boolean enableTeamA, boolean enableTeamB) {
		this.source = source;
		this.question = question;
		this.enableTeamA = enableTeamA;
		this.enableTeamB = enableTeamB;
	}

	public Object getSource() {
		return this.source;
	}

	public Question getQuestion() {
		return this.question;
	}

	public boolean shouldEnableTeamA() {
		return this.enableTeamA;
	}

	public boolean shouldEnableTeamB() {
		return this.enableTeamB;
	}
}
