package eOSB.time.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;

import eOSB.time.controller.CountDownTimer;
import eOSB.time.ui.EditTimeDialog;

public class OpenEditTimeDialogAction extends AbstractAction {
		
	private CountDownTimer timer; 

	public OpenEditTimeDialogAction(CountDownTimer timer) {
		super( "Edit" );
		this.timer = timer;
	}
	
	public void actionPerformed(ActionEvent ae) {
		EditTimeDialog dialog = new EditTimeDialog(this.timer);
		dialog.setLocationRelativeTo( null );
		dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		dialog.setVisible( true );
	}
}