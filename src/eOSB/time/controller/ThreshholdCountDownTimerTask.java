package eOSB.time.controller;

import java.util.List;
import java.util.TimerTask;

/**
 * Creates a {@link TimerTask} to keep track of the threshold times for a
 * {@link CountDownTimer}.
 * @author Caine Jette
 * 
 */
public class ThreshholdCountDownTimerTask extends TimerTask {

  private CountDownTimer timer;
  private List<Long> thresholdTimes;

  /**
   * @param timer the {@link CountDownTimer}
   * @param thresholdTimes the list of threshold values
   */
  public ThreshholdCountDownTimerTask(CountDownTimer timer, List<Long> thresholdTimes) {
    this.timer = timer;
    this.thresholdTimes = thresholdTimes;
  }

  /** {@inheritDoc} */
  @Override
  public void run() {
    boolean isInThreshholdTime = false;
    for (Long threshhold : this.thresholdTimes) {
      if (this.timer.getCurrentTime() <= threshhold) {
        isInThreshholdTime = true;
        this.timer.swapTimeLabelColors();
      }
    }
    if (!isInThreshholdTime) {
      this.timer.setToNormalColors();
    }
  }
}
