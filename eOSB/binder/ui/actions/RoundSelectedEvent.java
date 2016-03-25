package eOSB.binder.ui.actions;

import org.bushe.swing.event.EventServiceEvent;

public class RoundSelectedEvent implements EventServiceEvent {

	private Object source;
	private String roundName;
	private boolean useTimekeeper;
	private boolean useScorekeeper;
	
	public RoundSelectedEvent(Object source, String roundName, boolean useTimekeeper, boolean useScorekeeper) {
		this.source = source;
		this.roundName = roundName;
		this.useTimekeeper = useTimekeeper;
		this.useScorekeeper = useScorekeeper;
	}
	
	public Object getSource() {
		return this.source;
	}
	
	public String getRoundName() {
		return this.roundName;
	}
	
	public boolean useTimekeeper() {
		return this.useTimekeeper;
	}
	
	public boolean useScorekeeper() {
		return this.useScorekeeper;
	}
}
