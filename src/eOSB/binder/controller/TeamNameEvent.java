package eOSB.binder.controller;

import org.bushe.swing.event.EventServiceEvent;

/**
 * Event fired to updated team names.
 * @author cjette
 *
 */
public class TeamNameEvent implements EventServiceEvent {

  private Object source;
  private String teamAName;
  private String teamBName;
  
  /**
   * @param source the object firing this event
   * @param teamAName {@link Team} A's name
   * @param teamBName {@link Team} B's name
   */
  public TeamNameEvent(Object source, String teamAName, String teamBName) {
    this.source = source;
    this.teamAName = teamAName;
    this.teamBName = teamBName;
  }
  
  /**
   * @return team A's name
   */
  public String getTeamAName() {
    return this.teamAName;
  }
  
  /**
   * @return team B's name
   */
  public String getTeamBName() {
    return this.teamBName;
  }

  /** {@inheritDoc} */
//  @Override
  public Object getSource() {
    return this.source;
  }
}
