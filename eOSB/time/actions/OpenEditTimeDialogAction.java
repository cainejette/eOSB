package eOSB.time.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;

import eOSB.game.controller.Handler;
import eOSB.time.controller.CountDownTimer;
import eOSB.time.ui.EditTimeDialog;

/**
 * The action associated with a clock's "Edit" button.
 * 
 * @author Caine Jette
 */
public class OpenEditTimeDialogAction extends AbstractAction {
	
	private Handler handler;
	
	/** the timer this action is associated with */
	private CountDownTimer timer; 
	
	/**
	 * Initializes the action with its associated timer
	 * @param timer the {@link CountDownTimer} 
	 * @param handler the {@link Handler}
	 */
	public OpenEditTimeDialogAction(CountDownTimer timer, Handler handler) {
		super( "Edit" );
		this.timer = timer;
		this.handler = handler;
	}
	
	/** {@inheritDoc} */
//	@Override
	public void actionPerformed(ActionEvent ae) {
		EditTimeDialog dialog = new EditTimeDialog(this.timer, this.handler);
		dialog.setLocationRelativeTo( null );
		dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		dialog.setVisible( true );
	}
}

