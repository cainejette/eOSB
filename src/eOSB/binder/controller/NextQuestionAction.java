package eOSB.binder.controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import eOSB.game.controller.Handler;

public class NextQuestionAction extends AbstractAction {

	private Handler handler;
	
	public NextQuestionAction(Handler handler) {
		this.handler = handler;
	}

	public void actionPerformed(ActionEvent arg0) {
		this.handler.nextQuestion(true, true, null);
	}
}
