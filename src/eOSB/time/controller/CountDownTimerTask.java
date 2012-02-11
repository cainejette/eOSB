package eOSB.time.controller;

import java.util.TimerTask;

/**
 * Creates a TimerTask that acts as a count down timer.
 * @author Caine Jette
 * 
 */
public class CountDownTimerTask extends TimerTask {

  private CountDownTimer timer;
  private long currentTime;
  boolean shouldPlayBuzzer = false;

  /**
   * Initializes the count down timer.
   * @param timer the CountDownTimer object
   * @param currentTime the time to initialize the clock to
   */
  public CountDownTimerTask(CountDownTimer timer, long currentTime, boolean shouldPlayBuzzer) {
    this.timer = timer;
    this.currentTime = currentTime;
    this.shouldPlayBuzzer = shouldPlayBuzzer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void run() {
    long temp = this.timer.getCurrentTime();
    temp -= 1000;

    this.timer.setTime(temp, false);
    if (temp <= 0) {
      if (this.shouldPlayBuzzer) {
        SoundPlayer sp = new SoundPlayer(
            "eOSB/src/eOSB/game/data/sounds/windows_stop.wav");
        sp.start();
      }
      this.timer.pause();
      return;
    }
  }
}
