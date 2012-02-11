package eOSB.binder.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import eOSB.binder.controller.ButtonState;
import eOSB.game.controller.Handler;

/**
 * The action associated with clicking "Submit", or either of the team's
 * "Correct" buttons
 * @author Caine Jette
 * 
 */
public class SubmitAction extends AbstractAction {

  private Handler handler;
  private ButtonState teamA;
  private ButtonState teamB;

  public SubmitAction(Handler handler, ButtonState teamA, ButtonState teamB) {
    this.handler = handler;
    this.teamA = teamA;
    this.teamB = teamB;
  }

  /** {@inheritDoc} */
  public void actionPerformed(ActionEvent ae) {

    Object source = ae.getSource();
    if (source instanceof JButton) {
      JButton button = (JButton) source;
      String actionCommand = button.getActionCommand();

      if (actionCommand.equals("TEAM_A")) {
        this.teamA.setCorrect(true);
      }
      else if (actionCommand.equals("TEAM_B")) {
        this.teamB.setCorrect(true);
      }
    }
    AnswerUtils.processSubmission(this.handler, this.teamA, this.teamB);
  }
}
