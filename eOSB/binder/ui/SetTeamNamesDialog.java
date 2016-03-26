package eOSB.binder.ui;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.ui.actions.CancelButtonAction;
import eOSB.binder.ui.actions.SetTeamNamesAction;
import eOSB.game.controller.Handler;
import eOSB.game.data.IconFactory;
import net.miginfocom.swing.MigLayout;

public class SetTeamNamesDialog extends StandardDialog {

	private Handler handler;

	private JTextField teamAField = new JTextField(15);
	private JTextField teamBField = new JTextField(15);

	public SetTeamNamesDialog(Handler handler) {
		this.handler = handler;
		this.init();
	}

	private void init() {
		this.pack();
		this.setTitle("Set Team Names");
		this.setModal(true);
		
		this.setLocationRelativeTo(this.handler.getFrame());
		this.setResizable(false);
		this.setVisible(true);
	}

	public JComponent createBannerPanel() {
		JLabel message = new JLabel("Set team names:");
		message.setFont(new Font(message.getFont().getName(), Font.PLAIN, message.getFont().getSize() + 2));

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
		panel.setLayout(new MigLayout());
		panel.add(message);
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
		
		panel.add(cancelButton, "w 150!, h 75!");

		JButton okButton = new JButton();
		SetTeamNamesAction okAction = new SetTeamNamesAction(this, this.handler, this.teamAField,
				this.teamBField);
		this.setDefaultAction(okAction);
		okButton.setAction(okAction);
		okButton.requestFocus();
		
		ImageIcon okIcon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource(IconFactory.CORRECT));
		okButton.setIcon(okIcon);

		panel.add(okButton, "w 150!, h 75!");

		return panel;
	}

	public JComponent createContentPanel() {
		final JLabel teamALabel = new JLabel(this.handler.getTeamA().getName());
		final JLabel teamBLabel = new JLabel(this.handler.getTeamB().getName());

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
		panel.setLayout(new MigLayout("wrap, fill"));
		panel.add(teamALabel);
		panel.add(this.teamAField, "h 45!, growx, span");
		panel.add(teamBLabel, "gapy 20");
		panel.add(this.teamBField, "gapy 5, h 45!, growx, span");

		return panel;
	}
}
