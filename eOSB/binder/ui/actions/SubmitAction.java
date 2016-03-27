package eOSB.binder.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import org.bushe.swing.event.EventBus;

import eOSB.binder.controller.ButtonState;
import eOSB.binder.controller.NextQuestionEvent;
import eOSB.game.controller.Handler;

public class SubmitAction extends AbstractAction {
	private Handler handler;
	private ButtonState teamA;
	private ButtonState teamB;

	public SubmitAction(Handler handler, ButtonState teamA, ButtonState teamB) {
		this.handler = handler;
		this.teamA = teamA;
		this.teamB = teamB;
	}

	public void actionPerformed(ActionEvent ae) {
		System.out.println("submit action triggered");
		if (this.handler.getCurrentRound().getCurrentQuestionIndex() == -1) {
			EventBus.publish(new NextQuestionEvent(this, true, true, null));
		}
		else {
			Object source = ae.getSource();
			if ((source instanceof JButton)) {
				JButton button = (JButton) source;
				String actionCommand = button.getActionCommand();
	
				System.out.println("action command: " + actionCommand);
				if (actionCommand != null) {
					if (actionCommand.equals("TEAM_A")) {
						this.teamA.setCorrect(true);
					} else if (actionCommand.equals("TEAM_B")) {
						this.teamB.setCorrect(true);
					}
				} else {
					this.teamA.setCorrect(false);
					this.teamB.setCorrect(false);
				}
			}
	
			AnswerUtils.processSubmission(this.handler.getCurrentRound().getCurrentQuestion(), this.teamA, this.teamB);
		}
	}
}