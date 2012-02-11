package eOSB.binder.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.bushe.swing.event.EventBus;

import com.jidesoft.dialog.StandardDialog;

public class OpenRoundAction extends AbstractAction {

	private StandardDialog dialog;

	public OpenRoundAction(StandardDialog dialog) {
		super("OK");
		this.dialog = dialog;
	}

	public void actionPerformed(ActionEvent e) {

		this.dialog.setVisible(false);

		System.out.println("[OpenRoundAction/actionPerformed] sending RoundSelectedEvent");
		EventBus.publish(new RoundSelectedEvent(this));

		this.dialog.dispose();
	}
}
