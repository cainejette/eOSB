package eOSB.game.controller;

import org.bushe.swing.event.EventServiceEvent;

public class ShowSelectPackagesDialogEvent implements EventServiceEvent {

	private Object source;
	
	public ShowSelectPackagesDialogEvent(Object source) {
		System.out.println("[ShowSelectPackagesDialogEvent] firing event");
		this.source = source;
	}

	public Object getSource() {
		return this.source;
	}
}
