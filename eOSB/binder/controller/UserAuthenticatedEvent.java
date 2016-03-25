package eOSB.binder.controller;

import org.bushe.swing.event.EventServiceEvent;

public class UserAuthenticatedEvent implements EventServiceEvent {

	private Object source;
	
	public UserAuthenticatedEvent(Object source) {
		this.source = source;
	}
	
	public Object getSource() {
		return this.source;
	}
}
