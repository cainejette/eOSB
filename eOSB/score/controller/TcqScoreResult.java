package eOSB.score.controller;

public class TcqScoreResult implements ScoreResult {

	private int worth;

	public TcqScoreResult(int worth) {
		this.worth = worth;
	}

	public int getWorth() {
		return this.worth;
	}
}
