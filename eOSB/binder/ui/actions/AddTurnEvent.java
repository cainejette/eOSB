package eOSB.binder.ui.actions;

import org.bushe.swing.event.EventServiceEvent;

import eOSB.game.controller.Turn;

public class AddTurnEvent implements EventServiceEvent {

	private Turn turn;

	public AddTurnEvent(Turn turn) {
		this.turn = turn;
	}

	public Object getSource() {
		return null;
	}

	public Turn getTurn() {
		return this.turn;
	}
}
