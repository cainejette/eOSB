package eOSB.game.controller;

import eOSB.score.controller.ScoreResult;

/**
 * Object representing the {@link Turn} comprised of a buzzer question.
 * @author cjette
 * 
 */
public class BuzzerTurn implements Turn {

  private final ScoreResult teamAResult;
  private final ScoreResult teamBResult;
  
  private String questionNumber;

  /**
   * @param teamAResult how many points were awarded to {@link Team} A
   * @param teamBResult how many points were awarded to {@link Team} B
   */
  public BuzzerTurn(ScoreResult teamAResult, ScoreResult teamBResult) {
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
  
  public void setQuestionNumber(String number) {
    this.questionNumber = number;
  }
  
  public String getNumber() {
    return this.questionNumber;
  }
}
