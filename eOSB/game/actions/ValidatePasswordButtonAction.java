package eOSB.game.actions;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.swing.AbstractAction;
import javax.swing.JPasswordField;

import com.jidesoft.dialog.StandardDialog;

import eOSB.game.controller.Handler;
import eOSB.game.controller.Password;

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

			this.cutoffDate = new Date(cutoffString);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		boolean isCorrect;
		String providedPassword = new String(input);
		try {
			if (Password.check(providedPassword, correctPassword)) {
				isCorrect = true;
				String salt = correctPassword.split("\\$")[0];
				this.handler.setRoundToken(providedPassword + salt);
			}
			else {
				isCorrect = false;
			}
		} catch (Exception e) {
			isCorrect = false;
		}

		Date currentDate = new Date();

		this.parent.setVisible(false);
		this.parent.dispose();

		if (isCorrect && currentDate.before(this.cutoffDate)) {
			this.handler.displayEula();
		} 
		else {
			this.handler.failedValidation();
		}
	}
}
