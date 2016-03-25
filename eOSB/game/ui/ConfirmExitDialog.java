package eOSB.game.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.ui.actions.CancelButtonAction;
import eOSB.binder.ui.actions.CloseProgramButtonAction;
import eOSB.game.controller.Handler;

public class ConfirmExitDialog extends StandardDialog {

	private Handler handler;

	public ConfirmExitDialog(Handler handler) {
		this.handler = handler;
		this.init();
	}

	private void init() {
		this.pack();
		this.setTitle("Confirm Exit");
		this.setLocationRelativeTo(this.handler.getFrame());
		this.setResizable(false);
	}

	@Override
	public JComponent createBannerPanel() {
		final JLabel message = new JLabel("Are you sure you'd like to exit eOSB?");

		final JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		panel.add(message);

		return panel;
	}

	@Override
	public ButtonPanel createButtonPanel() {
		ButtonPanel panel = new ButtonPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

		CloseProgramButtonAction closeAction = new CloseProgramButtonAction(this, this.handler);
		CancelButtonAction cancelAction = new CancelButtonAction(this);
		this.setDefaultCancelAction(cancelAction);

		JButton button = new JButton();
		button.setAction(cancelAction);
		panel.add(button);

		button = new JButton();
		button.setAction(closeAction);
		panel.add(button);

		return panel;
	}

	@Override
	public JComponent createContentPanel() {
		return null;
	}
}
