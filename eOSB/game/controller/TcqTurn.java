package eOSB.game.controller;

import eOSB.score.controller.ScoreResult;

public class TcqTurn implements Turn {

	private final ScoreResult teamAResult;
	private final ScoreResult teamBResult;
	private final boolean isTcqA;

	public TcqTurn(boolean isTcqA, ScoreResult teamAResult, ScoreResult teamBResult) {
		if (teamAResult.getWorth() > 20 || teamAResult.getWorth() < 0) {
			throw new IllegalArgumentException("invalid score");
		}
		if (teamBResult.getWorth() > 20 || teamBResult.getWorth() < 0) {
			throw new IllegalArgumentException("invalid score");
		}

		this.teamAResult = teamAResult;
		this.teamBResult = teamBResult;
		this.isTcqA = isTcqA;
	}

	public ScoreResult getTeamAScore() {
		return this.teamAResult;
	}

	public ScoreResult getTeamBScore() {
		return this.teamBResult;
	}
	
	public boolean isTcqA() {
		return this.isTcqA;
	}
}
