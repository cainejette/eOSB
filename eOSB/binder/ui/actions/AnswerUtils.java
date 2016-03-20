package eOSB.binder.ui.actions;

import org.bushe.swing.event.EventBus;

import eOSB.binder.controller.ButtonState;
import eOSB.game.controller.BuzzerTurn;
import eOSB.game.controller.Handler;
import eOSB.game.controller.Question;
import eOSB.score.controller.BuzzerScoreResult;

/**
 * Class to help with shit. Consolidates how submissions are processed.
 * @author cjette
 * 
 */
public class AnswerUtils {

  /**
   * Processes an answer submission.
   * @param handler the {@link Handler}
   * @param teamA {@link ButtonState} for team A
   * @param teamB {@link ButtonState} for team B
   */
  public static void processSubmission(Handler handler, ButtonState teamA, ButtonState teamB) {
    BuzzerTurn turn;
    boolean enableTeamA = false;
    boolean enableTeamB = false;

    if (handler.getCurrentRound().getCurrentQuestion().getType() == Question.Type.TOSSUP) {
      if (teamA.isCorrect()) {
        if (teamB.isInterrupt()) {
          turn = new BuzzerTurn(BuzzerScoreResult.CORRECT_TOSSUP, BuzzerScoreResult.INTERRUPT);
        }
        else {
          turn = new BuzzerTurn(BuzzerScoreResult.CORRECT_TOSSUP, BuzzerScoreResult.INCORRECT);
        }

        enableTeamA = true;
      }
      else if (teamB.isCorrect()) {
        if (teamA.isInterrupt()) {
          turn = new BuzzerTurn(BuzzerScoreResult.INTERRUPT, BuzzerScoreResult.CORRECT_TOSSUP);
        }
        else {
          turn = new BuzzerTurn(BuzzerScoreResult.INCORRECT, BuzzerScoreResult.CORRECT_TOSSUP);
        }

        enableTeamB = true;
      }
      else {
        if (teamA.isInterrupt()) {
          if (teamB.isInterrupt()) {
            turn = new BuzzerTurn(BuzzerScoreResult.INTERRUPT, BuzzerScoreResult.INTERRUPT);
          }
          else {
            turn = new BuzzerTurn(BuzzerScoreResult.INTERRUPT, BuzzerScoreResult.INCORRECT);
          }
        }
        else if (teamB.isInterrupt()) {
          turn = new BuzzerTurn(BuzzerScoreResult.INCORRECT, BuzzerScoreResult.INTERRUPT);
        }
        else {
          turn = new BuzzerTurn(BuzzerScoreResult.INCORRECT, BuzzerScoreResult.INCORRECT);
        }
      }
    }

    // question is a bonus
    else {
      if (teamA.isCorrect()) {
        turn = new BuzzerTurn(BuzzerScoreResult.CORRECT_BONUS, BuzzerScoreResult.INCORRECT);
      }
      else if (teamB.isCorrect()) {
        turn = new BuzzerTurn(BuzzerScoreResult.INCORRECT, BuzzerScoreResult.CORRECT_BONUS);
      }
      else {
        turn = new BuzzerTurn(BuzzerScoreResult.INCORRECT, BuzzerScoreResult.INCORRECT);
      }

      enableTeamA = true;
      enableTeamB = true;
    }

    turn.setQuestionNumber(handler.getCurrentRound().getCurrentQuestion().getNumber());
//    System.out.println("add turn event from answerUtils");
//    EventBus.publish(new AddTurnEvent(turn));

    if (handler.getCurrentRound().getCurrentQuestion().getType() == Question.Type.TOSSUP) {
      // if both teams are incorrect on a tossup, move on to next tossup
      if (!teamA.isCorrect() && !teamB.isCorrect()) {
        handler.nextQuestion(false, false, turn);

        // add a dummy turn to the match's history so "Back" button can just
        // remove most recent turn
        BuzzerTurn spacerTurn = new BuzzerTurn(BuzzerScoreResult.INCORRECT,
            BuzzerScoreResult.INCORRECT);
        spacerTurn.setQuestionNumber(handler.getCurrentRound().getCurrentQuestion().getNumber());
//        System.out.println("add turn event from answerUtils");
//        EventBus.publish(new AddTurnEvent(spacerTurn));
        handler.nextQuestion(true, true, spacerTurn);
      }
      // one team answered the tossup correctly, so move on to bonus and set
      // buttons appropriately
      else {
        handler.nextQuestion(enableTeamA, enableTeamB, turn);
      }
    }

    // move on to next tossup
    else {
      handler.nextQuestion(true, true, turn);
    }
  }
}
