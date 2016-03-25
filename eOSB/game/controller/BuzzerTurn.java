package eOSB.game.controller;

import eOSB.score.controller.ScoreResult;

public class BuzzerTurn implements Turn {

	private final ScoreResult teamAResult;
	private final ScoreResult teamBResult;

	public BuzzerTurn(ScoreResult teamAResult, ScoreResult teamBResult) {
		this.teamAResult = teamAResult;
		this.teamBResult = teamBResult;
	}

	public ScoreResult getTeamAScore() {
		return this.teamAResult;
	}

	public ScoreResult getTeamBScore() {
		return this.teamBResult;
	}
}
