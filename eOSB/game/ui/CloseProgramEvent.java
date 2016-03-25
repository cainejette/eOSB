package eOSB.game.ui;

import org.bushe.swing.event.EventServiceEvent;

public class CloseProgramEvent implements EventServiceEvent  {

	private Object source;
	
	public CloseProgramEvent(Object source) {
		this.source = source;
	}
	
	public Object getSource() {
		return this.source;
	}
}
