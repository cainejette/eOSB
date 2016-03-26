package eOSB.time.ui;

import java.awt.Font;
import java.text.NumberFormat;

import javax.swing.AbstractAction;
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
import eOSB.game.data.IconFactory;
import eOSB.score.ui.ImprovedFormattedTextField;
import eOSB.time.actions.SetTimeAction;
import eOSB.time.controller.CountDownTimer;
import net.miginfocom.swing.MigLayout;

public class EditTimeDialog extends StandardDialog {

	private CountDownTimer timer;
	private ImprovedFormattedTextField minutesField;
	private ImprovedFormattedTextField secondsField;

	public EditTimeDialog(CountDownTimer timer) {
		this.timer = timer;

		NumberFormat integerNumberInstance = NumberFormat.getIntegerInstance();
        minutesField = new ImprovedFormattedTextField( integerNumberInstance );
        minutesField.setColumns( 2 );
        
        secondsField = new ImprovedFormattedTextField( integerNumberInstance );
        secondsField.setColumns( 2 );
        
		this.init();
	}

	private void init() {
		this.pack();
		this.setTitle("Set Time");
		this.setResizable(false);
	}

	@Override
	public JComponent createBannerPanel() {
		JLabel message = new JLabel("Set the desired time:");
		message.setFont(new Font(message.getFont().getName(), Font.PLAIN, message.getFont().getSize() + 2));

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 6));
		panel.setLayout(new MigLayout());
		panel.add(message, "wrap");
		return panel;
	}

	@Override
	public ButtonPanel createButtonPanel() {
		ButtonPanel panel = new ButtonPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
		panel.setLayout(new MigLayout("fillx"));

		JButton cancelButton = new JButton();
		CancelButtonAction cancelAction = new CancelButtonAction(this);
		this.setDefaultCancelAction(cancelAction);
		cancelButton.setAction(cancelAction);

		ImageIcon cancelIcon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource(IconFactory.INCORRECT));
		cancelButton.setIcon(cancelIcon);

		panel.add(cancelButton, "w 150!, h 75!");

		JButton okButton = new JButton();
		AbstractAction okAction = new SetTimeAction(this.timer, this, this.minutesField, this.secondsField);
		okButton.setAction(okAction);
		this.setDefaultAction(okAction);
		okButton.requestFocus();

		ImageIcon okIcon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource(IconFactory.CORRECT));
		okButton.setIcon(okIcon);

		panel.add(okButton, "gapleft 10, w 150!, h 75!");

		return panel;
	}

	@Override
	public JComponent createContentPanel() {
		final JPanel panel = new JPanel();
		final JLabel colonLabel = new JLabel(" : ");

		panel.setLayout(new MigLayout("fill"));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
		panel.add(this.minutesField, "align right, w 60!, h 45!");
		panel.add(colonLabel, "align center");
		panel.add(this.secondsField, "align left, w 60!, h 45!");
		return panel;
	}
}
