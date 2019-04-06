package eOSB.game.controller;

import java.awt.Font;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.controller.FireTcqPreambleEventAction;
import eOSB.game.data.IconFactory;
import net.miginfocom.swing.MigLayout;

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
		JLabel message = new JLabel("It's time for the 2 minute break, if you haven't already taken it!");
	  message.setFont(new Font(message.getFont().getName(), Font.PLAIN, message.getFont().getSize() + 2));

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 6));
		panel.setLayout(new MigLayout("wrap 1"));
		panel.add(message);
		return panel;
	}

	public ButtonPanel createButtonPanel() {
		ButtonPanel panel = new ButtonPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
		panel.setLayout(new MigLayout("fill"));

		JButton okButton = new JButton();
		AbstractAction okAction = new CancelAndShowAction(this);
		okButton.setAction(okAction);
		this.setDefaultAction(okAction);
		okButton.requestFocus();
		okButton.setToolTipText("OK");
		
		ImageIcon okIcon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource(IconFactory.NEXT));
		okButton.setIcon(okIcon);

		panel.add(okButton, "gapleft 10, growx, h 75!");

		return panel;
 	}
	
	@Override
	public JComponent createContentPanel() {
		return null;
	}
}
