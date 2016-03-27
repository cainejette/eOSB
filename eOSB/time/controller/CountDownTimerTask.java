package eOSB.time.controller;

import java.util.TimerTask;

public class CountDownTimerTask extends TimerTask {

	private CountDownTimer timer;

	public CountDownTimerTask(CountDownTimer timer, long currentTime) {
		this.timer = timer;
	}

	@Override
	public void run() {
		long temp = this.timer.getCurrentTime();
		temp -= 1000;

		this.timer.setTime(temp, false);
		if (temp <= 0) {
			this.timer.pause();
			return;
		}
	}
}
