package eOSB.time.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;

/**
 * The action associated with clicking clock Edit --> Cancel button
 * @author Caine Jette
 *
 */
public class CancelEditTimeAction extends AbstractAction {
	
	private JDialog dialog;
	
	/**
	 * Initializes the action with parent dialog
	 * @param dialog the parent dialog
	 */
	public CancelEditTimeAction( JDialog dialog ) {
		super( "Cancel" );
		this.dialog = dialog;
	}
	
	/**
	 * {@inheritDoc}
	 */
//	@Override
	public void actionPerformed( ActionEvent ae ) {
		this.dialog.setVisible( false );
		this.dialog.dispose();
	}
}
