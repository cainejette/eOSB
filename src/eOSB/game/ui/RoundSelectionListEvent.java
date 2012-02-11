package eOSB.game.ui;

import org.bushe.swing.event.EventServiceEvent;

public class RoundSelectionListEvent implements EventServiceEvent {

	private Object source;
	private String name;
	
	public RoundSelectionListEvent(Object source, String name) {
		this.source = source;
		this.name = name;
	}
	
	public Object getSource() {
		return this.source;
	}
	
	public String getName() {
		return this.name;
	}
}
