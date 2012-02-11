package eOSB.time.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTextField;

import eOSB.time.controller.CountDownTimer;
import eOSB.time.ui.EditTimeDialog;

/**
 * The action associated with clicking a clock's Edit --> Set button
 * @author Caine Jette
 * 
 */
public class SetTimeAction extends AbstractAction {

  /** the timer this dialog is associated with */
  private CountDownTimer timer;

  /** the parent dialog */
  private EditTimeDialog dialog;

  /** the input fields */
  private JTextField minutesField;
  private JTextField secondsField;

  /**
   * Initializes the action with the timer and time to set to
   * 
   * @param timer the timer object
   * @param dialog the parent dialog
   * @param minutesField the minutes to set the clock to
   * @param secondsField the seconds to set the clock to
   */
  public SetTimeAction(CountDownTimer timer, EditTimeDialog dialog, JTextField minutesField,
      JTextField secondsField) {
    super("Set");
    this.timer = timer;
    this.dialog = dialog;
    this.minutesField = minutesField;
    this.secondsField = secondsField;
  }

  /** {@inheritDoc} */
//  @Override
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

    this.timer.setTime(minutesLong, secondsLong);
    this.dialog.setVisible(false);
  }
}
