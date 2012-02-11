package eOSB.binder.ui.actions;

import org.bushe.swing.event.EventServiceEvent;

import eOSB.game.controller.Turn;

/**
 * Event fired to update subscribers of a change 
 * @author cjette
 *
 */
public class AddTurnEvent implements EventServiceEvent {
  
  private Turn turn;
  
  /**
   * @param source the source firing this event
   * @param turn the {@link Turn} wrapped by this event
   */
  public AddTurnEvent(Turn turn) {
    this.turn = turn;
  }
  
  /**
   * @return the source firing this event
   */
//  @Override
  public Object getSource() {
    return null;
  }
  
  /**
   * @return the {@link Turn} wrapped by this event
   */
  public Turn getTurn() {
    return this.turn;
  }
}
