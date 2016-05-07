package eOSB.binder.ui.actions;

import org.bushe.swing.event.EventServiceEvent;

public class SetScoreEvent implements EventServiceEvent {
	
	private int teamAScore;
	private int teamBScore;

	public SetScoreEvent(int teamAScore, int teamBScore) {
		this.teamAScore = teamAScore;
		this.teamBScore = teamBScore;
	}
	
	public Object getSource() {
		return null;
	}
	
	public int getTeamAScore() {
		return this.teamAScore;
	}
	
	public int getTeamBScore() {
		return this.teamBScore;
	}
}
