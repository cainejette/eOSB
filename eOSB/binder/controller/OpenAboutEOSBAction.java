package eOSB.binder.controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

public class OpenAboutEOSBAction extends AbstractAction {
	
	private JFrame parent;
	
	public OpenAboutEOSBAction(JFrame frame) {
		this.parent = frame;
	}
	
	public void actionPerformed(ActionEvent e) {
		AboutEOSBDialog dialog = new AboutEOSBDialog(this.parent);
	}
}
