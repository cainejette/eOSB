package eOSB.binder.ui.actions;

import org.bushe.swing.event.EventServiceEvent;

public class RoundSelectedEvent implements EventServiceEvent {

	private Object source;
	
	public RoundSelectedEvent(Object source) {
		this.source = source;
	}
	
	public Object getSource() {
		return this.source;
	}
}
