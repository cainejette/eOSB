package eOSB.binder.controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.bushe.swing.event.EventBus;

public class NextQuestionAction extends AbstractAction {

	public void actionPerformed(ActionEvent arg0) {
		EventBus.publish(new NextQuestionEvent(this, true, true, null));
	}
}
