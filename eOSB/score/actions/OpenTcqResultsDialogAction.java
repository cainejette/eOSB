package eOSB.score.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.jidesoft.dialog.StandardDialog;

import eOSB.game.controller.Handler;
import eOSB.score.ui.TcqResultsDialog;

public class OpenTcqResultsDialogAction extends AbstractAction {

	private StandardDialog parent;
	private Handler handler;

	public OpenTcqResultsDialogAction(StandardDialog parent, Handler handler) {
		super("Add Results");
		this.parent = parent;
		this.handler = handler;
	}

	public void actionPerformed(ActionEvent ae) {
		TcqResultsDialog dialog = new TcqResultsDialog(this.handler);
		dialog.setVisible(true);
	}
}
