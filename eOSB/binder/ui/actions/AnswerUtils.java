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
			processTossup(teamA, teamB);
		} else {
			processBonus(teamA, teamB);
		}
	}

	private static void processTossup(ButtonState teamA, ButtonState teamB) {
		System.out.println("processing tossup: ");
		System.out.println(teamA.isCorrect());
		System.out.println(teamB.isCorrect());
		System.out.println(!teamA.isCorrect() && !teamB.isCorrect());
		
		System.out.println("is team A correct? " + teamA.isCorrect());
		BuzzerScoreResult teamAResult = teamA.isCorrect() ? 
				BuzzerScoreResult.CORRECT_TOSSUP : teamA.isInterrupt() ? 
							BuzzerScoreResult.INTERRUPT : BuzzerScoreResult.INCORRECT;
		
		System.out.println("is team B correct? " + teamB.isCorrect());
		BuzzerScoreResult teamBResult = teamB.isCorrect() ? 
				BuzzerScoreResult.CORRECT_TOSSUP : teamB.isInterrupt() ? 
							BuzzerScoreResult.INTERRUPT : BuzzerScoreResult.INCORRECT;
	
		BuzzerTurn turn = new BuzzerTurn(teamAResult, teamBResult);

		// if both teams were incorrect on a tossup, they don't get the bonus question
		if (!teamA.isCorrect() && !teamB.isCorrect()) {
			EventBus.publish(new NextQuestionEvent("AnswerUtils", teamA.isCorrect(), teamB.isCorrect(), turn));
			System.out.println("AnswerUtils/ spacer round created");
			System.out.println(teamA.isCorrect());
			System.out.println(teamB.isCorrect());
			System.out.println(!teamA.isCorrect() && !teamB.isCorrect());
			BuzzerTurn spacerTurn = new BuzzerTurn(BuzzerScoreResult.INCORRECT, BuzzerScoreResult.INCORRECT);
			EventBus.publish(new NextQuestionEvent("AnswerUtils", true, true, spacerTurn));
		} else {
			EventBus.publish(new NextQuestionEvent("AnswerUtils", teamA.isCorrect(), teamB.isCorrect(), turn));
		}
	}

	private static void processBonus(ButtonState teamA, ButtonState teamB) {
		BuzzerScoreResult teamAResult = teamA.isCorrect() ? 
				BuzzerScoreResult.CORRECT_BONUS : BuzzerScoreResult.INCORRECT;
		
		BuzzerScoreResult teamBResult = teamB.isCorrect() ? 
				BuzzerScoreResult.CORRECT_BONUS : BuzzerScoreResult.INCORRECT;

		BuzzerTurn turn = new BuzzerTurn(teamAResult, teamBResult);

		EventBus.publish(new NextQuestionEvent("AnswerUtils", true, true, turn));
	}
}
