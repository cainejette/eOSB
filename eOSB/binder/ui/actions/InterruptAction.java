package eOSB.binder.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import eOSB.binder.controller.ButtonState;
import eOSB.game.controller.Handler;
import eOSB.game.controller.Question;

public class InterruptAction extends AbstractAction {

	private Handler handler;
	private ButtonState teamA;
	private ButtonState teamB;

	public InterruptAction(Handler handler, ButtonState teamA, ButtonState teamB) {
		this.handler = handler;
		this.teamA = teamA;
		this.teamB = teamB;
	}

	public void actionPerformed(ActionEvent ae) {
		Object source = ae.getSource();
		if (source instanceof JButton) {
			JButton button = (JButton) source;
			String actionCommand = button.getActionCommand();

			boolean isInterrupt;
			if (actionCommand.equals("TEAM_A")) {
				isInterrupt = !this.teamA.isInterrupt();
				this.teamA.setInterrupt(isInterrupt);
				if (isInterrupt) {
					teamA.setIncorrect(false);
				}
			} else if (actionCommand.equals("TEAM_B")) {
				isInterrupt = !this.teamB.isInterrupt();
				this.teamB.setInterrupt(isInterrupt);
				if (isInterrupt) {
					teamB.setIncorrect(false);
				}
			}
		}

		Question question = this.handler.getCurrentRound().getCurrentQuestion();

		if ((teamA.isIncorrect() || teamA.isInterrupt()) && (teamB.isIncorrect() || teamB.isInterrupt())) {
			AnswerUtils.processSubmission(question, this.teamA, this.teamB);
		}
	}
}
