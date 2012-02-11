package eOSB.score.controller;

/**
 * Enumeration representing how many points each type of response is worth.
 * @author cjette
 * 
 */
public enum BuzzerScoreResult implements ScoreResult {

  /** correct answer for a {@link Question.Type.TOSSUP} */
  CORRECT_TOSSUP(4),

  /** incorrect answer */
  INCORRECT(0),

  /** incorrect interrupt for a {@link Question.Type.TOSSUP} */
  INTERRUPT(-4),

  /** correct answer for a {@link Question.Type.BONUS} */
  CORRECT_BONUS(6);

  private final int worth;

  /**
   * @param worth the value of this {@link BuzzerScoreResult}
   */
  BuzzerScoreResult(int worth) {
    this.worth = worth;
  }

  /** {@inheritDoc} */
//  @Override
  public int getWorth() {
    return this.worth;
  }
}
