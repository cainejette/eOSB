package eOSB.game.controller;

import eOSB.score.controller.ScoreResult;


/**
 * Object encapsulating a {@link Turn} that was a {@link Tcq} question.
 * @author cjette
 * 
 */
public abstract class TcqTurn implements Turn {
  
  private final ScoreResult teamAResult;
  private final ScoreResult teamBResult;

  /**
   * @param teamAResult how many points were awarded to {@link Team} A
   * @param teamBResult how many points were awarded to {@link Team} B
   */
  public TcqTurn(ScoreResult teamAResult, ScoreResult teamBResult) {
    if (teamAResult.getWorth() > 20 || teamAResult.getWorth() < 0) {
      throw new IllegalArgumentException("invalid score");
    }
    if (teamBResult.getWorth() > 20 || teamBResult.getWorth() < 0) {
      throw new IllegalArgumentException("invalid score");
    }
    
    this.teamAResult = teamAResult;
    this.teamBResult = teamBResult;
  }

  /** {@inheritDoc} */
//  @Override
  public ScoreResult getTeamAScore() {
    return this.teamAResult;
  }

  /** {@inheritDoc} */
//  @Override
  public ScoreResult getTeamBScore() {
    return this.teamBResult;
  }
}
