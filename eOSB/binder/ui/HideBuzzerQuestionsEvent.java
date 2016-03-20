package eOSB.binder.ui;

import org.bushe.swing.event.EventServiceEvent;

public class HideBuzzerQuestionsEvent implements EventServiceEvent {

	private Object source;
	
	public HideBuzzerQuestionsEvent(Object source) {
		this.source = source;
	}
	
	public Object getSource() {
		return this.source;
	}
}
