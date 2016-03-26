package eOSB.time.controller;

import java.util.List;
import java.util.TimerTask;

public class ThreshholdCountDownTimerTask extends TimerTask {

	private CountDownTimer timer;
	private List<Long> thresholdTimes;

	public ThreshholdCountDownTimerTask(CountDownTimer timer, List<Long> thresholdTimes) {
		this.timer = timer;
		this.thresholdTimes = thresholdTimes;
	}

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
