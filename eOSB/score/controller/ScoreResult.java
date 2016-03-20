package eOSB.score.controller;

/**
 * Interface tying together the two ways of earning points - via buzzer
 * questions and via {@link Tcq}s.
 * @author cjette
 * 
 */
public interface ScoreResult {

  /**
   * @return the worth of the result
   */
  public int getWorth();
}
