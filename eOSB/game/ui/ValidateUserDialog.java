package eOSB.game.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.ui.actions.CancelButtonAction;
import eOSB.game.actions.AuthenticateUserAction;
import eOSB.game.controller.Handler;
import eOSB.game.data.IconFactory;
import net.miginfocom.swing.MigLayout;

public class ValidateUserDialog extends StandardDialog {
	private Handler handler;
	private JPasswordField passwordField;
	private JButton okButton;
	private JLabel tryAgainLabel;

	public ValidateUserDialog(Handler handler) {
		super(handler.getFrame(), true);
		this.handler = handler;
		this.passwordField = new JPasswordField(25);
		tryAgainLabel = new JLabel("failed. Try again.");
		tryAgainLabel.setForeground(Color.RED);
		tryAgainLabel.setFont(new Font(tryAgainLabel.getFont().getName(), Font.PLAIN, tryAgainLabel.getFont().getSize() + 2));

		okButton = new JButton();
		this.passwordField.addKeyListener(new PasswordKeyListener(this, passwordField, okButton, tryAgainLabel));

		this.init();
	}

	private void init() {
		this.pack();
		this.setTitle("User Validation");

		this.setLocationRelativeTo(this.handler.getFrame());
		this.setVisible(true);
		this.setResizable(false);
//		this.setMaximumSize(new Dimension(306, 200));
		this.setPreferredSize(new Dimension(506, 500));
//		this.setMinimumSize(new Dimension(306, 200));
		this.setSize(new Dimension(506, 500));
	}

	public JComponent createBannerPanel() {
		JLabel message = new JLabel("Enter passphrase:");
		message.setFont(new Font(message.getFont().getName(), Font.PLAIN, message.getFont().getSize() + 2));

		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(6, 15, 0, 15));
		panel.add(message);
		panel.add(tryAgainLabel);

		return panel;
	}

	public ButtonPanel createButtonPanel() {
		ButtonPanel panel = new ButtonPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
		panel.setLayout(new MigLayout());

		JButton cancelButton = new JButton();
		CancelButtonAction cancelAction = new CancelButtonAction(this);
		this.setDefaultCancelAction(cancelAction);
		cancelButton.setAction(cancelAction);

		ImageIcon cancelIcon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource(IconFactory.INCORRECT));
		cancelButton.setIcon(cancelIcon);

		panel.add(cancelButton, "w 150!, h 65!");

		AuthenticateUserAction okAction = new AuthenticateUserAction(this, this.passwordField, this.tryAgainLabel);
		this.setDefaultAction(okAction);
		okButton.setAction(okAction);

		ImageIcon okIcon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource(IconFactory.NEXT));
		okButton.setIcon(okIcon);

		panel.add(okButton, "gapleft 10, w 150!, h 65!");

		return panel;
	}

	public JComponent createContentPanel() {
		JPanel panel = new JPanel();

		panel.setLayout(new MigLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

		panel.add(this.passwordField, "h 45!");

		return panel;
	}
}