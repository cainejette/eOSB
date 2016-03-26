package eOSB.game.actions;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.JPasswordField;

import org.bushe.swing.event.EventBus;

import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.controller.UserAuthenticatedEvent;
import eOSB.game.controller.Constants;
import eOSB.game.controller.Handler;
import eOSB.game.controller.Password;

public class AuthenticateUserAction extends AbstractAction {

	private StandardDialog parent;

	private JPasswordField passwordField;

	public AuthenticateUserAction(StandardDialog parent, JPasswordField passwordField) {
		this.parent = parent;
		this.passwordField = passwordField;
	}

	public void actionPerformed(ActionEvent ae) {
		boolean validPassword;
		String providedPassword = new String(this.passwordField.getPassword());
		String actualPassword = this.readFile(Constants.PASSWORD_FILE);
		try {
			validPassword = providedPassword != null && Password.check(providedPassword, actualPassword);
		} catch (Exception e) {
			validPassword = false;
		}

		boolean validDate;
		Date currentDate = new Date();
		String date = this.readFile(Constants.EXPIRATION_FILE);
		validDate = date != null && currentDate.before(new Date(date));

		if (validPassword && validDate) {
			this.parent.setVisible(false);
			this.parent.dispose();

			EventBus.publish(new UserAuthenticatedEvent(this));
		} 
		else {
			this.passwordField.setBackground(Color.RED);
		}
	}
	
	private String readFile(String path) {
		InputStreamReader isReader = new InputStreamReader(Handler.getResourceAsStream(path));
		BufferedReader reader = new BufferedReader(isReader);
		try {
			if (reader.ready()) {
				return reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
