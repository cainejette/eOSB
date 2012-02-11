package eOSB.binder.controller;

import org.bushe.swing.event.EventServiceEvent;

import eOSB.game.controller.Turn;

/**
 * Event used to notify subscribers that a {@link Team}'s score has changed.
 * @author cjette
 * 
 */
public class TeamScoreTurnEvent implements EventServiceEvent {

  private Object source;
  private Turn turn;

  /**
   * @param source the source firing this event
   * @param turn the {@link Turn} wrapped by this event
   */
  public TeamScoreTurnEvent(Object source, Turn turn) {
    this.source = source;
    this.turn = turn;
  }

  /**
   * @return true if this {@link TeamScoreTurnEvent} is wrapping a {@link Turn},
   * false otherwise
   */
  public boolean hasTurn() {
    return this.turn != null;
  }

  /**
   * Gets the {@link Turn} wrapped by this {@link TeamScoreTurnEvent}, or null if it
   * doesn't exist. Use {@link #hasTurn()} to first check.
   * @return the turn wrapped in this event, or null
   */
  public Turn getTurn() {
    return this.turn;
  }

  /** {@inheritDoc} */
//  @Override
  public Object getSource() {
    return this.source;
  }
}
