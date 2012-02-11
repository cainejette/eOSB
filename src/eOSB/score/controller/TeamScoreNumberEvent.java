package eOSB.score.controller;

import org.bushe.swing.event.EventServiceEvent;

/**
 * Event used to fire to view components the scores of both {@link Team}s
 * @author cjette
 *
 */
public class TeamScoreNumberEvent implements EventServiceEvent {

  private Object source;
  private int teamAScore;
  private int teamBScore;
  
  /**
   * @param source the source object firing this event
   * @param teamAScore {@link Team} A's score
   * @param teamBScore {@link Team} B's score
   */
  public TeamScoreNumberEvent(Object source, int teamAScore, int teamBScore) {
    this.source = source;
    this.teamAScore = teamAScore;
    this.teamBScore = teamBScore;
  }
  
  /**
   * @return the score for {@link Team} A
   */
  public int getTeamAScore() {
    return this.teamAScore;
  }
  
  /**
   * @return the score for {@link Team} B
   */
  public int getTeamBScore() {
    return this.teamBScore;
  }

  /** {@inheritDoc} */
//  @Override
  public Object getSource() {
    return this.source;
  }
}
