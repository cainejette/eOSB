package eOSB.game.actions;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.JPasswordField;

import com.jidesoft.dialog.StandardDialog;

import eOSB.game.controller.Handler;

/**
 * The action that validates a user's password.
 * 
 * @author cjette
 * 
 */
public class ValidatePasswordButtonAction extends AbstractAction {

	private StandardDialog parent;
	private Handler handler;

	private Date cutoffDate;
	private String cutoffString;

	private JPasswordField passwordField;

	/**
	 * @param parent
	 *          the parent dialog
	 * @param handler
	 *          the {@link Handler}
	 * @param passwordField
	 *          the {@link JPasswordField} from which to extract the entered
	 *          password
	 */
	public ValidatePasswordButtonAction(StandardDialog parent, Handler handler,
			JPasswordField passwordField) {
		this.parent = parent;
		this.handler = handler;
		this.passwordField = passwordField;
	}

	/** {@inheritDoc} */
	public void actionPerformed(ActionEvent ae) {
		final char[] input = this.passwordField.getPassword();
		String correctPassword = "";

		InputStreamReader isReader;
		try {
			isReader = new InputStreamReader(Handler.getResourceAsStream(
					"eOSB/game/data/password.txt"));
			BufferedReader reader = new BufferedReader(isReader);
			if (reader.ready()) {
				correctPassword = reader.readLine();
			}

			isReader = new InputStreamReader(Handler.getResourceAsStream(
					"eOSB/game/data/expiration.txt"));
			reader = new BufferedReader(isReader);
			if (reader.ready()) {
				cutoffString = reader.readLine();
			}

			this.cutoffDate = DateFormat.getDateInstance().parse(cutoffString);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		final boolean isCorrect;
		if (input.length != correctPassword.length()) {
			isCorrect = false;
		} else {
			isCorrect = correctPassword.equals(new String(input));
		}

		Date currentDate = new Date();

		this.parent.setVisible(false);
		this.parent.dispose();

		if (isCorrect && currentDate.before(this.cutoffDate)) {
			this.handler.displayEula();
		} else {
			this.handler.failedValidation(correctPassword, new String(input));
		}
	}
}
