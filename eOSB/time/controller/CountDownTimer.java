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

import org.bushe.swing.event.EventBus;

import eOSB.game.controller.RoundClockTimeUpdateEvent;
import eOSB.time.actions.OpenEditTimeDialogAction;
import eOSB.time.actions.ResetAction;
import eOSB.time.actions.StartAction;
import net.miginfocom.swing.MigLayout;

/**
 * Creates a countdown timer
 * 
 * @author mikey
 * 
 */
public class CountDownTimer {

	private long startTime = 0;
	private long currentTime = 0;
	
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
	
	private JPanel timePanel;

	public CountDownTimer(long timeInMilliseconds, boolean isRoundClock) {
		this.startTime = timeInMilliseconds;
		this.currentTime = timeInMilliseconds;
		this.isRoundClock = isRoundClock;

		this.makePanel();
		this.setup();
		this.setToNormalColors();
		
		if (!this.isRoundClock)
			System.out.println("new CountDownTimer made (current time: " + this.currentTime + ")");
	}

	public long getCurrentTime() {
		return this.currentTime;
	}

	public void setup() {
		this.thresholds = new ArrayList<Long>();
		long threshhold = 5000;
		this.thresholds.add(threshhold);

		this.timer = new Timer();

		if (!this.isRoundClock) {
			System.out.println("setup: create CountDownTimerTask with time: " + currentTime);
		}
		
		this.countdownTimerTask = new CountDownTimerTask(this, this.currentTime);
		this.threshholdTimerTask = new ThreshholdCountDownTimerTask(this, this.thresholds);

		this.currentTime = this.startTime;
		if (!this.isRoundClock)
			System.out.println("\nCountDownTimer/setup calling setTime");
		
		this.setTime(this.startTime, true);
	}

	public void close() {
		this.timer.cancel();
	}

	public JPanel getPanel() {
		return this.timePanel;
	}
	
	public void makePanel() {
		System.out.println("\nget standalone timer for " + (isRoundClock ? " round " : " question ") + "clock");
		
		JPanel displayPanel = new JPanel();
		displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.LINE_AXIS));
		displayPanel.setAlignmentX(JFrame.CENTER_ALIGNMENT);
		displayPanel.setAlignmentY(JFrame.CENTER_ALIGNMENT);

		this.smallMinutesLabel = new JLabel("00", JLabel.CENTER);
		this.smallMinutesLabel.setForeground(this.normalColor);
		this.smallMinutesLabel.setFont(this.smallFont);
		
		this.smallSecondsLabel = new JLabel("00", JLabel.CENTER);
		this.smallSecondsLabel.setForeground(this.normalColor);
		this.smallSecondsLabel.setFont(this.smallFont);
		
		this.smallColonLabel = new JLabel(" : ", JLabel.CENTER);
		this.smallColonLabel.setForeground(this.normalColor);
		this.smallColonLabel.setFont(this.smallFont);

		displayPanel.add(this.smallMinutesLabel);
		displayPanel.add(this.smallColonLabel);
		displayPanel.add(this.smallSecondsLabel);

		JPanel actionButtonPanel = new JPanel();
		actionButtonPanel.setLayout(new MigLayout("insets 0, fill"));

		this.editButton = new JButton("Edit");
		this.resetButton = new JButton("Reset");
		this.startButton = new JButton("Start");
		
		this.editButton.setAction(new OpenEditTimeDialogAction(this));
		this.resetButton.setAction(new ResetAction(this, this.startButton));
		this.startButton.setAction(new StartAction(this, this.startButton));
		
		actionButtonPanel.add(this.startButton, "span, growx, h 50!");
		actionButtonPanel.add(this.editButton, "growx, sizegroupx group1, h 50!");
		actionButtonPanel.add(this.resetButton, "growx, sizegroupx group1, h 50!");

		this.timePanel = new JPanel();
		this.timePanel.setLayout(new MigLayout("wrap 1, fill, insets 0"));
		this.timePanel.add(displayPanel, "grow, sizegroupx group2");
		this.timePanel.add(actionButtonPanel, "growx, sizegroupx group2");
	}

	public void setActionButtonsEnabled(boolean enabled) {
		this.editButton.setEnabled(enabled);
		this.startButton.setEnabled(enabled);
	}

	public void addThreshold(long time) {
		this.thresholds.add(time);
	}

	public void clearThresholds() {
		this.thresholds.clear();
	}

	public void start() {
		if (!this.isRoundClock)
			System.out.println("CountDownTimer/start says currentTime is : " + currentTime);
	
		if (this.currentTime <= 0) {
			return;
		}

		this.paused = false;

		this.timer.cancel();
		this.timer.purge();
		this.timer = new Timer();

		if (!this.isRoundClock)
			System.out.println("start: create CountDownTimerTask with time: " + currentTime);
		this.countdownTimerTask = new CountDownTimerTask(this, this.currentTime);
		this.threshholdTimerTask = new ThreshholdCountDownTimerTask(this, this.thresholds);

		this.timer.schedule(this.countdownTimerTask, 1000, 1000);
		this.timer.schedule(this.threshholdTimerTask, 0, 500);
	}

	public void pause() {
		this.paused = true;
		this.timer.cancel();
		this.timer.purge();
	}

	public void reset() {
		this.currentTime = this.lastSetTime;
		this.paused = true;

		this.timer.cancel();
		this.timer.purge();
		this.timer = new Timer();

		if (!this.isRoundClock) {
			System.out.println("reset: create CountDownTimerTask with time: " + currentTime);
		}
		this.countdownTimerTask = new CountDownTimerTask(this, this.currentTime);
		this.threshholdTimerTask = new ThreshholdCountDownTimerTask(this, this.thresholds);

		if (!this.isRoundClock) {
			System.out.println("\nCountDownTimer/reset calling setTime");
		}
		this.setTime(this.currentTime, false);
	}

	private long lastSetTime = this.startTime;

	public void setTime(long time, boolean shouldResetLastSetTime) {
		if (!this.isRoundClock) {
			System.out.println("setting time to: " + time);
		}
		if (shouldResetLastSetTime) {
			this.lastSetTime = time;
		}

		this.currentTime = time;
		long minutes = time / 60000l;
		long seconds = (time % 60000l) / 1000;

		if (this.isRoundClock) {
			EventBus.publish(new RoundClockTimeUpdateEvent(this, minutes, seconds));
		}

		this.smallMinutesLabel.setText(this.addZeroes(String.valueOf(minutes), 2));
		this.smallSecondsLabel.setText(this.addZeroes(String.valueOf(seconds), 2));

		this.setToNormalColors();
		
		if (!this.isRoundClock) {
			System.out.println("at least, for : " + this);
		}
	}

	public boolean isTimeRunning() {
		return !(this.paused);
	}

	public void swapTimeLabelColors() {
		Color color = this.smallMinutesLabel.getForeground();

		if (color.equals(this.normalColor)) {
			this.setToThresholdColors();
		} else {
			this.setToNormalColors();
		}
	}

	public void setToNormalColors() {
		this.smallMinutesLabel.setForeground(this.normalColor);
		this.smallColonLabel.setForeground(this.normalColor);
		this.smallSecondsLabel.setForeground(this.normalColor);
	}

	public void setToThresholdColors() {
		this.smallMinutesLabel.setForeground(this.thresholdColor);
		this.smallColonLabel.setForeground(this.thresholdColor);
		this.smallSecondsLabel.setForeground(this.thresholdColor);
	}
	
	private String addZeroes(String string, int size) {
		while (string.length() < size) {
			string = "0" + string;
		}
		return string;
	}
}