package eOSB.score.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import eOSB.game.controller.Handler;
import eOSB.score.ui.TcqResultsDialog;

public class OpenTcqResultsDialogAction extends AbstractAction {

	private Handler handler;

	public OpenTcqResultsDialogAction(Handler handler) {
		super("Add Results");
		this.handler = handler;
	}

	public void actionPerformed(ActionEvent ae) {
		TcqResultsDialog dialog = new TcqResultsDialog(this.handler);
		dialog.setVisible(true);
	}
}
