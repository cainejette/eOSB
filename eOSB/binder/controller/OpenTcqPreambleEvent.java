package eOSB.binder.controller;

import org.bushe.swing.event.EventServiceEvent;

public class OpenTcqPreambleEvent implements EventServiceEvent {

	private Object source;
	
	public OpenTcqPreambleEvent(Object source) {
		this.source = source;
	}

	public Object getSource() {
		return this.source;
	}
}
