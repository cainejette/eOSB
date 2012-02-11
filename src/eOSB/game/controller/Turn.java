package eOSB.game.controller;

import eOSB.score.controller.ScoreResult;

/**
 * Object encapsulating both {@link Team}'s responses to a particular question,
 * whether it was a buzzer-type of a {@link Tcq}.
 * @author cjette
 * 
 */
public interface Turn {

  /**
   * @return how many points {@link Team} A's answer was worth
   */
  public ScoreResult getTeamAScore();

  /**
   * @return how many points {@link Team} B's answer was worth
   */
  public ScoreResult getTeamBScore();
  
}
