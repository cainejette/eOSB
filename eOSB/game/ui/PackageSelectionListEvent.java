package eOSB.game.ui;

import org.bushe.swing.event.EventServiceEvent;

public class PackageSelectionListEvent implements EventServiceEvent {

	private Object source;
	private int selection;
	
	public PackageSelectionListEvent(Object source, int selection) {
		this.source = source;
		this.selection = selection;
	}
	
	public Object getSource() {
		return this.source;
	}
	
	public int getSelection() {
		return this.selection;
	}
}
