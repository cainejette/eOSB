package eOSB.binder.controller;

import org.bushe.swing.event.EventServiceEvent;

public class RemindTcqEvent implements EventServiceEvent {
	
	private Object source;
	
	public RemindTcqEvent(Object source) {
		this.source = source;
	}

	public Object getSource() {
		return this.source;
	}
}
