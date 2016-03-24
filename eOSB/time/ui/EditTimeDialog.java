package eOSB.time.ui;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.ui.actions.CancelButtonAction;
import eOSB.game.controller.Handler;
import eOSB.game.data.IconFactory;
import eOSB.time.actions.SetTimeAction;
import eOSB.time.controller.CountDownTimer;

/**
 * The dialog associated with a user editing a clock's time
 * 
 * @author Caine Jette
 * 
 */
public class EditTimeDialog extends StandardDialog {
	
	private CountDownTimer timer;
	private JTextField minutesField = new JTextField(2);
	private JTextField secondsField = new JTextField(2);
	
	/**
	 * @param timer the {@link CountDownTimer}
	 * @param handler the {@link Handler}
	 */
	public EditTimeDialog(CountDownTimer timer) {
		this.timer = timer;
		
		this.init();
	}

	/**
	 * Basic initializations for the dialog.
	 */
	private void init() {
		this.pack();
		this.setTitle("Set Time");
		this.setResizable(false);
	}	

	/** {@inheritDoc} */
	@Override
	public JComponent createBannerPanel() {
		JLabel message = new JLabel("Set the desired time:");
    message.setFont(new Font(message.getFont().getName(), Font.PLAIN, message.getFont().getSize() + 2));

    JPanel panel = new JPanel();
    panel.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 6));
    panel.setLayout(new MigLayout());
    panel.add(message, "wrap");
    return panel;
	}

	/** {@inheritDoc} */
	@Override
	public ButtonPanel createButtonPanel() {
		ButtonPanel panel = new ButtonPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 6, 6));

		SetTimeAction setTimeAction = new SetTimeAction(this.timer, this, this.minutesField, this.secondsField);
		CancelButtonAction cancelAction = new CancelButtonAction(this);
		
		JButton button = new JButton();
		button.setAction(setTimeAction);
		this.setDefaultAction(setTimeAction);
		panel.add(button);

		button = new JButton();
		button.setAction(cancelAction);
		this.setDefaultCancelAction(cancelAction);
		panel.add(button);

		return panel;
	}

	/** {@inheritDoc} */
	@Override
	public JComponent createContentPanel() {
		final JPanel panel = new JPanel();
		final JLabel colonLabel = new JLabel(" : ");

		panel.setLayout(new MigLayout("wrap 3, insets 0, fill"));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 15, 6, 15));
		panel.add(this.minutesField, "alignx center, growx, sizegroupx 1, pushx");
		panel.add(colonLabel);
		panel.add(this.secondsField, "alignx center, growx, sizegroupx 1, pushx");
		return panel;
	}
}
