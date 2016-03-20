package eOSB.score.controller;

/**
 * Object encapsulating the result of a {@link Tcq}.
 * @author cjette
 *
 */
public class TcqScoreResult implements ScoreResult {

  private int worth;
  
  /**
   * @param worth the number of points this result is worth
   */
  public TcqScoreResult(int worth) {
    this.worth = worth;
  }
  
  /** {@inheritDoc} */
//  @Override
  public int getWorth() {
    return this.worth;
  }
}
