package eOSB.time.controller;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.bushe.swing.event.EventBus;

import eOSB.game.controller.Handler;
import eOSB.game.controller.QuestionClockTimeUpdateEvent;
import eOSB.game.controller.RoundClockTimeUpdateEvent;
import eOSB.time.actions.OpenEditTimeDialogAction;
import eOSB.time.actions.ResetAction;
import eOSB.time.actions.StartAction;

/**
 * Creates a countdown timer
 * 
 * @author mikey
 * 
 */
public class CountDownTimer {

  private long startTime = 0;
  private long currentTime = 0;

  private JPanel timePanel;

  private JLabel smallMinutesLabel;
  private JLabel smallColonLabel;
  private JLabel smallSecondsLabel;

  private final Font smallFont = new Font("Courier", Font.BOLD, 36);

  private Timer timer;
  private TimerTask countdownTimerTask;
  private TimerTask threshholdTimerTask;

  private boolean paused = true;
  private List<Long> thresholds = new ArrayList<Long>();

  private Color normalColor = Color.BLACK;
  private Color thresholdColor = Color.RED;

  private JButton editButton;
  private JButton resetButton;
  private JButton startButton;
  
  private boolean isRoundClock = false;

  /**
   * @param handler the {@link Handler}
   * @param timeInMilliseconds current time in millis
   * @param ownFrame whether the clock should have its own frame
   */
  public CountDownTimer(long timeInMilliseconds, boolean isRoundClock) {
    this.startTime = timeInMilliseconds;
    this.currentTime = timeInMilliseconds;
    this.isRoundClock = isRoundClock;

    this.setup();
    this.setToNormalColors();
  }

  /**
   * Gets the current time on the clock
   * 
   * @return the current time on the clock
   */
  public long getCurrentTime() {
    return this.currentTime;
  }

  /**
   * initialize the countdown display and countdown thread
   */
  public void setup() {
    this.smallMinutesLabel = new JLabel("00", JLabel.CENTER);
    this.smallMinutesLabel.setForeground(this.normalColor);
    this.smallMinutesLabel.setFont(this.smallFont);

    this.smallSecondsLabel = new JLabel("00", JLabel.CENTER);
    this.smallSecondsLabel.setForeground(this.normalColor);
    this.smallSecondsLabel.setFont(this.smallFont);

    this.smallColonLabel = new JLabel(" : ", JLabel.CENTER);
    this.smallColonLabel.setForeground(this.normalColor);
    this.smallColonLabel.setFont(this.smallFont);

    this.timePanel = new JPanel();
    this.timePanel.setLayout(new BoxLayout(this.timePanel, BoxLayout.LINE_AXIS));
    this.timePanel.setAlignmentX(JFrame.CENTER_ALIGNMENT);
    this.timePanel.setAlignmentY(JFrame.CENTER_ALIGNMENT);

    this.timePanel.add(this.smallMinutesLabel);
    this.timePanel.add(this.smallColonLabel);
    this.timePanel.add(this.smallSecondsLabel);

    this.thresholds = new ArrayList<Long>();
    long threshhold = 5000;
    this.thresholds.add(threshhold);

    this.timer = new Timer();

    // create the TimerTasks
    this.countdownTimerTask = new CountDownTimerTask(this, this.currentTime, !this.isRoundClock);
    this.threshholdTimerTask = new ThreshholdCountDownTimerTask(this, this.thresholds);

    // set the current time
    this.currentTime = this.startTime;
    this.setTime(this.startTime, true);
  }

  /**
   * Closes the frame before program closure.
   */
  public void close() {
    this.timer.cancel();
  }

  /**
   * gets the jpanel that the counter is actually in, just incase you dont want
   * to just display it in its own frame
   * 
   * @return panel containing the {@link CountDownTimer} and controlling buttons
   */
  public JPanel getStandaloneTimer() {
    this.editButton = new JButton("Edit");
    this.resetButton = new JButton("Reset");
    this.startButton = new JButton("Start");

    this.editButton.setAction(new OpenEditTimeDialogAction(this));
    this.resetButton.setAction(new ResetAction(this, this.startButton));
    this.startButton.setAction(new StartAction(this, this.startButton));

    final JPanel actionButtonPanel = new JPanel();
    actionButtonPanel.setLayout(new MigLayout("wrap 2, insets 0, fill"));
    actionButtonPanel.add(this.startButton, "span, growx");
    actionButtonPanel.add(this.editButton, "growx, sizegroupx group1");
    actionButtonPanel.add(this.resetButton, "growx, sizegroupx group1");

    final JPanel panel = new JPanel();
    panel.setLayout(new MigLayout("wrap 1, fill, insets 0"));
    panel.add(this.timePanel, "grow, sizegroupx group2");
    panel.add(actionButtonPanel, "growx, sizegroupx group2");

    return panel;
  }

  /**
   * Sets the action buttons Edit, Reset, and Start
   * 
   * @param enabled the state to set the buttons to
   */
  public void setActionButtonsEnabled(boolean enabled) {
    this.editButton.setEnabled(enabled);
//    this.resetButton.setEnabled(enabled);
    this.startButton.setEnabled(enabled);
  }

  /**
   * Adds a threshold time to the list
   * @param time the time to add
   */
  public void addThreshold(long time) {
    this.thresholds.add(time);
  }

  /**
   * Clears the list of thresholds
   */
  public void clearThresholds() {
    this.thresholds.clear();
  }

  /**
   * starts the countdown from where it was last, so you can pause, and press
   * start to continue
   */
  public void start() {
    // return if time has already expired
    if (this.currentTime <= 0) {
      return;
    }

    this.paused = false;

    // set up the Timer
    this.timer.cancel();
    this.timer.purge();
    this.timer = new Timer();

    // create the TimerTasks
    this.countdownTimerTask = new CountDownTimerTask(this, this.currentTime, !this.isRoundClock);
    this.threshholdTimerTask = new ThreshholdCountDownTimerTask(this, this.thresholds);

    // schedule the TimerTasks
    this.timer.schedule(this.countdownTimerTask, 1000, 1000);
    this.timer.schedule(this.threshholdTimerTask, 0, 500);
  }

  /**
   * Pauses the clock
   */
  public void pause() {
    this.paused = true;
    this.timer.cancel();
    this.timer.purge();
  }

  /**
   * Resets the time on the clock to be the time the clock has most recently
   * been set to.
   */
  public void reset() {
    this.currentTime = this.lastSetTime;
    this.paused = true;

    // creates a new Timer
    this.timer.cancel();
    this.timer.purge();
    this.timer = new Timer();

    // creates the TimerTasks
    this.countdownTimerTask = new CountDownTimerTask(this, this.currentTime, !this.isRoundClock);
    this.threshholdTimerTask = new ThreshholdCountDownTimerTask(this, this.thresholds);

    // sets the time on the clock
    this.setTime(this.currentTime, false);
  }

  private long lastSetTime = this.startTime;

  /**
   * Sets the time on the clock
   * 
   * @param time in milliseconds to set the clock
   * @param shouldResetLastSetTime whether to store this time as a "reset" point
   */
  public void setTime(long time, boolean shouldResetLastSetTime) {
    if (shouldResetLastSetTime) {
      this.lastSetTime = time;
    }
    
    this.currentTime = time;
    long minutes = time / 60000l;
    long seconds = (time % 60000l) / 1000;
    
    if (this.isRoundClock) {
      EventBus.publish(new RoundClockTimeUpdateEvent(this, minutes, seconds));
    }
    else {
      EventBus.publish(new QuestionClockTimeUpdateEvent(this, minutes, seconds));
    }

    this.smallMinutesLabel.setText(this.addZeroes(String.valueOf(minutes), 2));
    this.smallSecondsLabel.setText(this.addZeroes(String.valueOf(seconds), 2));

    this.setToNormalColors();
  }

  /**
   * Returns whether the clock is running or not
   * 
   * @return false if the clock is paused, true if the clock is running
   */
  public boolean isTimeRunning() {
    return !(this.paused);
  }

  /**
   * used to set the time of the countdown,should do a pause first..or craaazy
   * things can happen
   * 
   * @param minutes the number of minutes
   * @param seconds the number of seconds
   */
  public void setTime(long minutes, long seconds) {
    long time = minutes * 60000 + seconds * 1000;
    this.setTime(time, true);
  }

  /**
   * Adds zeroes to the front of a string
   * @param string the string to add zeroes to
   * @param size the desired final length of the string
   * @return the provided string, with as many zeroes as indicated
   */
  private String addZeroes(String string, int size) {
    while (string.length() < size) {
      string = "0" + string;
    }
    return string;
  }

  /**
   * Swaps the colors of the clock labels. That is, if color is currently
   * normal, switch to threshold color Likewise, if currently threshold, set
   * to normal
   */
  public void swapTimeLabelColors() {
    Color color = this.smallMinutesLabel.getForeground();

    if (color.equals(this.normalColor)) {
      this.setToThresholdColors();
    }
    else {
      this.setToNormalColors();
    }
  }

  /**
   * Sets the foreground color for the time labels to the normal color
   */
  public void setToNormalColors() {
    this.smallMinutesLabel.setForeground(this.normalColor);
    this.smallColonLabel.setForeground(this.normalColor);
    this.smallSecondsLabel.setForeground(this.normalColor);
  }

  /**
   * Sets the foreground color for the time labels to the threshold color
   */
  public void setToThresholdColors() {
    this.smallMinutesLabel.setForeground(this.thresholdColor);
    this.smallColonLabel.setForeground(this.thresholdColor);
    this.smallSecondsLabel.setForeground(this.thresholdColor);
  }
}
