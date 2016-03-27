package eOSB.binder.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import eOSB.binder.controller.ButtonState;
import eOSB.game.controller.Handler;
import eOSB.game.controller.Question;

public class IncorrectAction extends AbstractAction {

	private Handler handler;
	private ButtonState teamA;
	private ButtonState teamB;

	public IncorrectAction(Handler handler, ButtonState teamA, ButtonState teamB) {
		this.handler = handler;
		this.teamA = teamA;
		this.teamB = teamB;
	}

	public void actionPerformed(ActionEvent ae) {
		Object source = ae.getSource();
		if (source instanceof JButton) {
			JButton button = (JButton) source;
			String actionCommand = button.getActionCommand();

			boolean isIncorrect;
			if (actionCommand.equals("TEAM_A")) {
				isIncorrect = !this.teamA.isIncorrect();
				this.teamA.setIncorrect(isIncorrect);
				if (isIncorrect) {
					teamA.setInterrupt(false);
				}
			} else if (actionCommand.equals("TEAM_B")) {
				isIncorrect = !this.teamB.isIncorrect();
				this.teamB.setIncorrect(isIncorrect);
				if (isIncorrect) {
					teamB.setInterrupt(false);
				}
			}
		}

		Question question = this.handler.getCurrentRound().getCurrentQuestion();
		if (question.getType() == Question.Type.BONUS) {
			AnswerUtils.processSubmission(question, this.teamA, this.teamB);
		}

		if ((teamA.isIncorrect() || teamA.isInterrupt()) && (teamB.isIncorrect() || teamB.isInterrupt())) {
			AnswerUtils.processSubmission(question, this.teamA, this.teamB);
		}
	}
}
