package eOSB.time.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import eOSB.time.controller.CountDownTimer;

/**
 * The action associated with the {@link CountDownTimer}'s reset button
 * @author Caine Jette
 * 
 */
public class ResetAction extends AbstractAction {

  private CountDownTimer timer;
  private JButton startButton;

  /**
   * @param timer the {@link CountDownTimer}
   * @param startButton the start/pause button for the {@link CountDownTimer}
   */
  public ResetAction(CountDownTimer timer, JButton startButton) {
    super("Reset");
    this.timer = timer;
    this.startButton = startButton;
  }

  /** {@inheritDoc} */
//  @Override
  public void actionPerformed(ActionEvent ae) {
    boolean isRunning = this.timer.isTimeRunning();
    this.startButton.setEnabled(false);
    this.timer.pause();
    this.timer.reset();
    
    if (isRunning) {
      this.timer.start();
      this.startButton.setText("Pause");
      this.startButton.setEnabled(true);
    }
    else {
      this.startButton.setText("Start");
      this.startButton.setEnabled(true);
    }
  }
}
