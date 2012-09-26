package eOSB.game.controller;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.controller.FireTcqPreambleEventAction;
import eOSB.binder.ui.actions.CancelButtonAction;

public class HalfwayDoneDialog extends StandardDialog {

	private JFrame parent;
	
	public HalfwayDoneDialog(JFrame parent) {
		this.parent = parent;
		this.init();
	}
	
	private void init() {
		this.pack();
		this.setTitle("TCQ Reminder");

//		this.setLocationRelativeTo(this.parent);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
	}

	public JComponent createBannerPanel() {
		JLabel message = new JLabel("Please administer the TCQs if you haven't already.");
	  message.setFont(new Font(message.getFont().getName(), Font.PLAIN, message.getFont().getSize() + 2));

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 6));
		panel.setLayout(new MigLayout("wrap 1"));
		panel.add(message);
		return panel;
	}

	public ButtonPanel createButtonPanel() {
		ButtonPanel panel = new ButtonPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

		JButton button = new JButton();
		FireTcqPreambleEventAction okAction = new FireTcqPreambleEventAction(this);
		button.setAction(okAction);
		button.setText("Launch TCQs");
		panel.add(button);
		
		button = new JButton();
		CancelAndShowAction action = new CancelAndShowAction(this);
		button.setAction(action);
		button.setText("Dismiss");
		panel.add(button);
		
		this.setDefaultAction(okAction);
		this.setDefaultCancelAction(action);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		return panel;
 	}
	
	@Override
	public JComponent createContentPanel() {
		return null;
	}
}
