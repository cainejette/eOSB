package eOSB.time.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import eOSB.time.controller.CountDownTimer;

public class ResetAction extends AbstractAction {

	private CountDownTimer timer;
	private JButton startButton;

	public ResetAction(CountDownTimer timer, JButton startButton) {
		super("Reset");
		this.timer = timer;
		this.startButton = startButton;
	}

	/** {@inheritDoc} */
	public void actionPerformed(ActionEvent ae) {
		boolean isRunning = this.timer.isTimeRunning();
		this.startButton.setEnabled(false);
		this.timer.pause();
		this.timer.reset();

		if (isRunning) {
			this.timer.start();
			this.startButton.setText("Pause");
			this.startButton.setEnabled(true);
		} else {
			this.startButton.setText("Start");
			this.startButton.setEnabled(true);
		}
	}
}
