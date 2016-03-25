package eOSB.game.ui;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.ui.actions.CancelButtonAction;
import eOSB.binder.ui.actions.CloseProgramButtonAction;
import eOSB.game.controller.Handler;
import eOSB.game.data.IconFactory;
import net.miginfocom.swing.MigLayout;

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

	public JComponent createBannerPanel() {
		final JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(16, 15, 0, 15));
		panel.add(new JLabel("Are you sure you'd like to exit eOSB?"));

		return panel;
	}

	public ButtonPanel createButtonPanel() {
		ButtonPanel panel = new ButtonPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
		panel.setLayout(new MigLayout());

		JButton cancelButton = new JButton();
		AbstractAction cancelAction = new CancelButtonAction(this);
		cancelButton.setAction(cancelAction);
		this.setDefaultCancelAction(cancelAction);

		ImageIcon cancelIcon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource(IconFactory.INCORRECT));
		cancelButton.setIcon(cancelIcon);
		cancelButton.setToolTipText("No, go back");
		panel.add(cancelButton, "w 150!, h 75!");

		JButton okButton = new JButton();
		AbstractAction okAction = new CloseProgramButtonAction();
		okButton.setAction(okAction);

		ImageIcon okIcon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource(IconFactory.CORRECT));
		okButton.setIcon(okIcon);
		okButton.setToolTipText("Yes, exit");

		panel.add(okButton, "w 150!, h 75!");

		return panel;
	}

	@Override
	public JComponent createContentPanel() {
		return null;
	}
}
