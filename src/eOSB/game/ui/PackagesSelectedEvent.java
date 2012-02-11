package eOSB.game.ui;

import org.bushe.swing.event.EventServiceEvent;

public class PackagesSelectedEvent implements EventServiceEvent {

	private Object source;

	public PackagesSelectedEvent(Object source) {
		this.source = source;
	}
	
	public Object getSource() {
		return this.source;
	}
}
