package eOSB.binder.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import eOSB.binder.controller.Binder;

/**
 * The action associated with setting the font size
 * 
 * @author Caine Jette
 * 
 */
public class SetFontSizeAction extends AbstractAction {
	private Binder binder;
	private int size;

	/**
	 * Populates the action with the necessary data
	 * 
	 * @param binder
	 *            the handle on the gui
	 * @param commandName
	 *            the name of the button
	 * @param size
	 *            the size to set the font to
	 */
	public SetFontSizeAction(Binder binder, String commandName, int size) {
		super(commandName);
		this.binder = binder;
		this.size = size;
	}

	/**
	 * {@inheritDoc}
	 */
//	@Override
	public void actionPerformed(ActionEvent ae) {
		this.binder.setFontSize(this.size);
	}
}
