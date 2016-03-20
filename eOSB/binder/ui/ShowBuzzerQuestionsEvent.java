package eOSB.binder.ui;

import org.bushe.swing.event.EventServiceEvent;

public class ShowBuzzerQuestionsEvent implements EventServiceEvent {

	private Object source;
	
	public ShowBuzzerQuestionsEvent(Object source) {
		this.source = source;
	}
	
	public Object getSource() {
		return this.source;
	}
}
