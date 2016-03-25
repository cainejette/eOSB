package eOSB.binder.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.bushe.swing.event.EventBus;

import eOSB.game.ui.CloseProgramEvent;

public class CloseProgramButtonAction extends AbstractAction {

	public void actionPerformed(ActionEvent ae) {
			EventBus.publish(new CloseProgramEvent(this));
	}
}
