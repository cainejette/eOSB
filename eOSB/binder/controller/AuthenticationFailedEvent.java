package eOSB.binder.controller;

import org.bushe.swing.event.EventServiceEvent;

public class AuthenticationFailedEvent implements EventServiceEvent {

	private Object source;
	
	public AuthenticationFailedEvent(Object source) {
		this.source = source;
	}
	
	public Object getSource() {
		return this.source;
	}
}
