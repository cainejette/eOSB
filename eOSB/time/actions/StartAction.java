package eOSB.time.actions;

import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import eOSB.time.controller.CountDownTimer;

/**
 * The action associated with clicking clock Start/Pause button
 * @author Caine Jette
 * 
 */
public class StartAction extends AbstractAction {

  private CountDownTimer timer;
  private JButton startButton;
  private Timer t;

  /**
   * Initializes the action with the timer object and a handle on the start
   * button, as it needs to be modified.
   * @param timer the timer object
   * @param startButton the start button
   */
  public StartAction(CountDownTimer timer, JButton startButton) {
    super("Start");
    this.timer = timer;
    this.startButton = startButton;
    this.t = new Timer();
  }

  /**
   * {@inheritDoc}
   */
//  @Override
  public void actionPerformed(ActionEvent ae) {
    if (this.timer.isTimeRunning()) {
      this.startButton.setEnabled(false);
      this.timer.pause();
      this.timer.setActionButtonsEnabled(true);
      this.startButton.setText("Start");
    }
    else {
      this.timer.setActionButtonsEnabled(false);
      this.timer.start();
      this.startButton.setEnabled(true);
      this.startButton.setText("Pause");

      // sets a timertask to set buttons to enabled upon time expiration
      t = new Timer();
      t.schedule(new TimerTask() {
        @Override
        public void run() {
          if (StartAction.this.timer.isTimeRunning()) {
            return;
          }
          else {
            t.cancel();
            StartAction.this.timer.setActionButtonsEnabled(true);
            StartAction.this.startButton.setText("Start");
          }
        }
      }, 0, 200);
    }
  }
}
