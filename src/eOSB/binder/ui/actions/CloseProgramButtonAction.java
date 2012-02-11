package eOSB.binder.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.jidesoft.dialog.StandardDialog;

import eOSB.game.controller.Handler;

/**
 * The action associated with closing eOSB.
 * @author cjette
 *
 */
public class CloseProgramButtonAction extends AbstractAction {
	
	private StandardDialog dialog;
	private Handler handler;
	
	/**
	 * @param dialog the parent dialog
	 * @param handler the {@link Handler}
	 */
	public CloseProgramButtonAction( StandardDialog dialog, Handler handler ) {
		super( "Yes, Close" );
		this.dialog = dialog;
		this.handler = handler;
	}

	/** {@inheritDoc} */
//	@Override
	public void actionPerformed(ActionEvent ae) {
		this.dialog.setVisible(false);
		this.dialog.dispose();
		
		try {
			this.handler.cleanUp();
		}
		catch ( Exception e ) {
		  // do nothing
		}
		
		System.exit(0);
	}
}
