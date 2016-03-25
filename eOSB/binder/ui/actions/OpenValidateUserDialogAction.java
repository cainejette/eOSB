package eOSB.binder.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import eOSB.game.controller.Handler;
import eOSB.game.ui.ValidateUserDialog;

public class OpenValidateUserDialogAction extends AbstractAction {

	private Handler handler;

	public OpenValidateUserDialogAction(Handler handler) {
		this.handler = handler;
	}

	public void actionPerformed(ActionEvent ae) {
		new ValidateUserDialog(this.handler);
	}
}
