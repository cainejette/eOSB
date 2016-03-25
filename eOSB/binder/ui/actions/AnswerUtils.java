package eOSB.binder.ui.actions;

import org.bushe.swing.event.EventBus;

import eOSB.binder.controller.ButtonState;
import eOSB.binder.controller.NextQuestionEvent;
import eOSB.game.controller.BuzzerTurn;
import eOSB.game.controller.Question;
import eOSB.score.controller.BuzzerScoreResult;

public class AnswerUtils {

	public static void processSubmission(Question question, ButtonState teamA, ButtonState teamB) {

		if (question.getType() == Question.Type.TOSSUP) {
			processTossup(question, teamA, teamB);
		} else {
			processBonus(question, teamA, teamB);

		}
	}

	private static void processTossup(Question question, ButtonState teamA, ButtonState teamB) {
		BuzzerTurn turn;
		boolean enableTeamA = false;
		boolean enableTeamB = false;

		if (teamA.isCorrect()) {
			if (teamB.isInterrupt()) {
				turn = new BuzzerTurn(BuzzerScoreResult.CORRECT_TOSSUP, BuzzerScoreResult.INTERRUPT);
			} else {
				turn = new BuzzerTurn(BuzzerScoreResult.CORRECT_TOSSUP, BuzzerScoreResult.INCORRECT);
			}

			enableTeamA = true;
		} else if (teamB.isCorrect()) {
			if (teamA.isInterrupt()) {
				turn = new BuzzerTurn(BuzzerScoreResult.INTERRUPT, BuzzerScoreResult.CORRECT_TOSSUP);
			} else {
				turn = new BuzzerTurn(BuzzerScoreResult.INCORRECT, BuzzerScoreResult.CORRECT_TOSSUP);
			}

			enableTeamB = true;
		} else {
			if (teamA.isInterrupt()) {
				if (teamB.isInterrupt()) {
					turn = new BuzzerTurn(BuzzerScoreResult.INTERRUPT, BuzzerScoreResult.INTERRUPT);
				} else {
					turn = new BuzzerTurn(BuzzerScoreResult.INTERRUPT, BuzzerScoreResult.INCORRECT);
				}
			} else if (teamB.isInterrupt()) {
				turn = new BuzzerTurn(BuzzerScoreResult.INCORRECT, BuzzerScoreResult.INTERRUPT);
			} else {
				turn = new BuzzerTurn(BuzzerScoreResult.INCORRECT, BuzzerScoreResult.INCORRECT);
			}
		}

		turn.setQuestionNumber(question.getNumber());

		// if both teams are incorrect on a tossup, move on to next tossup
		if (!teamA.isCorrect() && !teamB.isCorrect()) {
			EventBus.publish(new NextQuestionEvent("AnswerUtils", false, false, turn));

			// add a dummy turn to the match's history so "Back" button can
			// just remove most recent turn
			BuzzerTurn spacerTurn = new BuzzerTurn(BuzzerScoreResult.INCORRECT, BuzzerScoreResult.INCORRECT);
			spacerTurn.setQuestionNumber(question.getNumber());
			// System.out.println("add turn event from answerUtils");
			// EventBus.publish(new AddTurnEvent(spacerTurn));

			EventBus.publish(new NextQuestionEvent("AnswerUtils", true, true, spacerTurn));
		}
		// one team answered the tossup correctly, so move on to bonus and set
		// buttons appropriately
		else {
			EventBus.publish(new NextQuestionEvent("AnswerUtils", enableTeamA, enableTeamB, turn));
		}
	}

	private static void processBonus(Question question, ButtonState teamA, ButtonState teamB) {
		BuzzerTurn turn;

		if (teamA.isCorrect()) {
			turn = new BuzzerTurn(BuzzerScoreResult.CORRECT_BONUS, BuzzerScoreResult.INCORRECT);
		} else if (teamB.isCorrect()) {
			turn = new BuzzerTurn(BuzzerScoreResult.INCORRECT, BuzzerScoreResult.CORRECT_BONUS);
		} else {
			turn = new BuzzerTurn(BuzzerScoreResult.INCORRECT, BuzzerScoreResult.INCORRECT);
		}

		turn.setQuestionNumber(question.getNumber());

		EventBus.publish(new NextQuestionEvent("AnswerUtils", true, true, turn));
	}
}
