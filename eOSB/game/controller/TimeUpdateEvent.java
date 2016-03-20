package eOSB.game.controller;

import org.bushe.swing.event.EventServiceEvent;

import eOSB.time.controller.CountDownTimer;

/**
 * Event fired to update a {@link CountDownTimer} with a new time.
 * @author cjette
 * 
 */
public class TimeUpdateEvent implements EventServiceEvent {

  private Object source;
  private long minutes;
  private long seconds;

  /**
   * @param source the source firing this event
   * @param minutes the number of minutes to display
   * @param seconds the number of seconds to display
   */
  public TimeUpdateEvent(Object source, long minutes, long seconds) {
    this.source = source;
    this.minutes = minutes;
    this.seconds = seconds;
  }

  /**
   * @return the source firing this event
   */
//  @Override
  public Object getSource() {
    return this.source;
  }

  /**
   * @return the number of minutes wrapped by this event
   */
  public long getMinutes() {
    return this.minutes;
  }

  /**
   * @return the number of seconds wrapped by this event
   */
  public long getSeconds() {
    return this.seconds;
  }
}
