package eOSB.game.controller;

import eOSB.score.controller.ScoreResult;

/**
 * Object representing the first of the two {@link Tcq} questions in a {@link Round}
 * @author cjette
 *
 */
public class TcqTurnB extends TcqTurn {

  /**
   * @param teamAResult {@link Team} A's {@link ScoreResult}
   * @param teamBResult {@link Team} B's {@link ScoreResult}
   */
  public TcqTurnB(ScoreResult teamAResult, ScoreResult teamBResult) {
    super(teamAResult, teamBResult);
  }
}
