package eOSB.game.controller;

/**
 * Event fired to update listeners that the Round clock time has changed.
 * @author cjette
 *
 */
public class RoundClockTimeUpdateEvent extends TimeUpdateEvent {

  /**
   * @param source the source firing this event
   * @param minutes the number of minutes wrapped by this event
   * @param seconds the number of seconds wrapped by this event
   */
  public RoundClockTimeUpdateEvent(Object source, long minutes, long seconds) {
    super(source, minutes, seconds);
  }
}
