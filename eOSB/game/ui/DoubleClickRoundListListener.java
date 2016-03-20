package eOSB.game.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.bushe.swing.event.EventBus;

import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.ui.actions.RoundSelectedEvent;

public class DoubleClickRoundListListener extends MouseAdapter {

	private StandardDialog dialog;

	public DoubleClickRoundListListener(StandardDialog dialog) {
		this.dialog = dialog;
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			this.dialog.setVisible(false);
			EventBus.publish(new RoundSelectedEvent(this));
			this.dialog.dispose();
		}
	}
}
