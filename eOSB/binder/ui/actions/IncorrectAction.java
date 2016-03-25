package eOSB.binder.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import eOSB.binder.controller.ButtonState;
import eOSB.game.controller.Handler;
import eOSB.game.controller.Question;

/**
 * The action associated with clicking "Incorrect"
 * @author Caine Jette
 * 
 */
public class IncorrectAction extends AbstractAction {

  private Handler handler;
  private ButtonState teamA;
  private ButtonState teamB;

  /**
   * @param handler the {@link Handler}
   * @param teamA {@link ButtonState} for team A
   * @param teamB {@link ButtonState} for team B
   */
  public IncorrectAction(Handler handler, ButtonState teamA, ButtonState teamB) {
    this.handler = handler;
    this.teamA = teamA;
    this.teamB = teamB;
  }

  /** {@inheritDoc} */
//  @Override
  public void actionPerformed(ActionEvent ae) {
    
    Object source = ae.getSource();
    if (source instanceof JButton) {
      JButton button = (JButton) source;
      String actionCommand = button.getActionCommand();
      
      if (actionCommand.equals("TEAM_A")) {
        if (this.teamA.isIncorrect()) {
          this.teamA.setIncorrect(false);
        }
        else {
          this.teamA.setIncorrect(true);
        }
      }
      else if (actionCommand.equals("TEAM_B")) {
        if (this.teamB.isIncorrect()) {
          this.teamB.setIncorrect(false);
        }
        else {
            this.teamB.setIncorrect(true);
        }
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
