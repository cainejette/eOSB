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

			if (actionCommand.equals("TEAM_A")) {
				this.teamA.setIncorrect(!this.teamA.isIncorrect());
			} else if (actionCommand.equals("TEAM_B")) {
				this.teamB.setIncorrect(!this.teamB.isIncorrect());
			}
		}

		Question question = this.handler.getCurrentRound().getCurrentQuestion();
		// "one-click" incorrect if question is a bonus question
		if (question.getType() == Question.Type.BONUS) {
			AnswerUtils.processSubmission(question, this.teamA, this.teamB);
		}

		// "one-click" incorrect if both teams have their correct buttons selected
		if (this.teamA.isIncorrect() && this.teamB.isIncorrect()) {
			AnswerUtils.processSubmission(question, this.teamA, this.teamB);
		}
	}
}
