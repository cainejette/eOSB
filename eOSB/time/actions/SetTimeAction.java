package eOSB.time.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTextField;

import eOSB.time.controller.CountDownTimer;
import eOSB.time.ui.EditTimeDialog;

public class SetTimeAction extends AbstractAction {

	private CountDownTimer timer;

	private EditTimeDialog dialog;

	private JTextField minutesField;
	private JTextField secondsField;

	public SetTimeAction(CountDownTimer timer, EditTimeDialog dialog, JTextField minutesField,
			JTextField secondsField) {
		this.timer = timer;
		this.dialog = dialog;
		this.minutesField = minutesField;
		this.secondsField = secondsField;
	}

	public void actionPerformed(ActionEvent e) {
		String minutesString = this.minutesField.getText().trim();
		String secondsString = this.secondsField.getText().trim();

		if (minutesString.equals("")) {
			minutesString = "00";
		}
		if (secondsString.equals("")) {
			secondsString = "00";
		}

		long minutesLong = Long.parseLong(minutesString);
		long secondsLong = Long.parseLong(secondsString);

		long time = minutesLong * 60000 + secondsLong * 1000;
		System.out.println("\nSetTimeAction calling setTime");
		this.timer.setTime(time, true);
		this.dialog.setVisible(false);
	}
}
