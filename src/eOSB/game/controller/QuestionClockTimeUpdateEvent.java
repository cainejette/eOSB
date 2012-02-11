package eOSB.game.controller;

/**
 * Event fired to update subscribers that the Question clock time has changed.
 * @author cjette
 *
 */
public class QuestionClockTimeUpdateEvent extends TimeUpdateEvent {

  /**
   * @param source the source firing this event
   * @param minutes the number of minutes wrapped by this event
   * @param seconds the number of seconds wrapped by this event
   */
  public QuestionClockTimeUpdateEvent(Object source, long minutes, long seconds) {
    super(source, minutes, seconds);
  }
}
