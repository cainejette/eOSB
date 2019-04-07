package eOSB.binder.controller;

import org.bushe.swing.event.EventServiceEvent;

public class ShowHalftimeWarningEvent implements EventServiceEvent {
	
	private Object source;
	
	public ShowHalftimeWarningEvent(Object source) {
		this.source = source;
	}

	public Object getSource() {
		return this.source;
	}
}
