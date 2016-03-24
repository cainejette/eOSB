package eOSB.time.controller;

import javax.swing.JPanel;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventServiceEvent;
import org.bushe.swing.event.EventSubscriber;

import eOSB.binder.controller.QuestionEvent;
import eOSB.game.controller.Handler;

/**
 * The class encapsulating the time keeping functionality.
 * 
 * @author Caine Jette
 */
public class Timekeeper implements EventSubscriber<EventServiceEvent> {

  /** the clocks this timekeeper is in charge of */
  private CountDownTimer roundClock;
  private CountDownTimer questionClock;

  public Timekeeper() {
    this.roundClock = new CountDownTimer(360000, true);
    this.questionClock = new CountDownTimer(5000, false);
    
    EventBus.subscribe(QuestionEvent.class, this);
  }

  /**
   * Cleans up before program closure.
   */
  public void close() {
    this.roundClock.close();
    this.questionClock.close();
  }

  /**
   * @param clock the {@link CountDownTimer} for which the containing panel will be returned
   * @return a panel containing the specified clock 
   */
  public JPanel getClockPanel(CountDownTimer clock) {
      return clock.getStandaloneTimer();
  }

  /**
   * @return the clock keeping time of the current {@link Question}
   */
  public CountDownTimer getQuestionClock() {
    return this.questionClock;
  }

  /**
   * @return the clock keeping time of the {@link Round}
   */
  public CountDownTimer getRoundClock() {
    return this.roundClock;
  }

  /** {@inheritDoc} */
//  @Override
  public void onEvent(EventServiceEvent ese) {
    if (ese instanceof QuestionEvent) {
      this.questionClock.pause();
    }
  }
}
