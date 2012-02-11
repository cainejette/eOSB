package eOSB.binder.controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.bushe.swing.event.EventBus;

import com.jidesoft.dialog.StandardDialog;

public class FireTcqPreambleEventAction extends AbstractAction {
	
	private StandardDialog dialog;
	
	public FireTcqPreambleEventAction() {
		this.dialog = null;
	}
	
	public FireTcqPreambleEventAction(StandardDialog dialog) {
		this.dialog = dialog;
	}
		
	public void actionPerformed(ActionEvent e) {
		
		if (this.dialog != null) {
			this.dialog.setVisible(false);
			this.dialog.dispose();
		}
		
		OpenTcqPreambleEvent event = new OpenTcqPreambleEvent(this);
		EventBus.publish(event);
	}
}
