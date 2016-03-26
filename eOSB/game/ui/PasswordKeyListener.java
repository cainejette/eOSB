package eOSB.game.ui;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JPasswordField;

import com.jidesoft.dialog.StandardDialog;

public class PasswordKeyListener implements KeyListener {

	private StandardDialog dialog;
	private JPasswordField passField;
	private JButton button;

	public PasswordKeyListener(StandardDialog dialog, JPasswordField passField, JButton button) {
		this.dialog = dialog;
		this.passField = passField;
		this.button = button;
		this.validateDialog();
	}
	
	public void keyPressed(KeyEvent arg0) {
		this.validateDialog();
	}

	public void keyReleased(KeyEvent arg0) {
		this.validateDialog();
	}

	public void keyTyped(KeyEvent arg0) {
		this.validateDialog();
	}

	private void validateDialog() {
		char[] pass = this.passField.getPassword();
		if (pass.length == 0) {
			this.button.setEnabled(false);
			this.dialog.setDefaultAction(null);
			this.passField.setBackground(Color.WHITE);
		}
		else {
			this.button.setEnabled(true);
			this.dialog.setDefaultAction(this.button.getAction());
		}
	}
}
