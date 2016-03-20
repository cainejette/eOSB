package eOSB.binder.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import eOSB.game.controller.Handler;
import eOSB.game.ui.ConfirmExitDialog;

/**
 * The action associated with confirming the user wanted to close the program.
 * 
 * @author Caine Jette
 * 
 */
public class OpenConfirmExitDialogAction extends AbstractAction {
	private Handler handler;

	/**
	 * Constructs the action with the specified controller object and the
	 * command name
	 * 
	 * @param handler
	 *            the handler controller object
	 */
	public OpenConfirmExitDialogAction(Handler handler) {
		super("Exit");
		this.handler = handler;
	}

	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent event) {
		final ConfirmExitDialog dialog = new ConfirmExitDialog(this.handler);
		dialog.setVisible(true);
	}
}
