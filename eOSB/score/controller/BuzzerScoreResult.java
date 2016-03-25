package eOSB.score.controller;

public enum BuzzerScoreResult implements ScoreResult {

	CORRECT_TOSSUP(4),
	INCORRECT(0),
	INTERRUPT(-4),
	CORRECT_BONUS(6);

	private final int worth;

	BuzzerScoreResult(int worth) {
		this.worth = worth;
	}

	public int getWorth() {
		return this.worth;
	}
}
