package eOSB.binder.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.ui.SetScoresDialog;
import eOSB.game.controller.Handler;

public class OpenSetScoresDialogAction extends AbstractAction {
	
	private Handler handler;
	
	public OpenSetScoresDialogAction(Handler handler) {
		super("Manual score override");
		this.handler = handler;
	}

	public void actionPerformed(ActionEvent e) {
		StandardDialog dialog = new SetScoresDialog(this.handler);
		dialog.setVisible(true);
	}
}
