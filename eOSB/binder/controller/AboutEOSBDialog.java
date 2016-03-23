package eOSB.binder.controller;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.ui.actions.CancelButtonAction;
import net.miginfocom.swing.MigLayout;

public class AboutEOSBDialog extends StandardDialog {
	
	private JFrame parent;
	
	public AboutEOSBDialog(JFrame frame) {
		this.init();
		this.parent = frame;
	}
	
	private void init() {
		this.setModal(true);
		this.pack();
		this.setTitle("About eOSB");
		
		this.setLocationRelativeTo(this.parent);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	@Override
	public JComponent createBannerPanel() {
		JLabel label = new JLabel("eOSB is dedicated towards making NOSB greener and more sustainable.");
		JLabel label2 = new JLabel("eOSB is maintained by competition alumnus Caine Jette.");
		JLabel label3 = new JLabel("Please send any feedback about the project to jette@alum.mit.edu.");
		
		label.setFont(new Font(label.getFont().getName(), Font.PLAIN, label.getFont().getSize() + 2));
		
		JPanel panel = new JPanel();
	    panel.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 6));
	    panel.setLayout(new MigLayout("wrap 1"));
	    panel.add(label);
	    panel.add(label2);
	    panel.add(label3);
	    
		return panel;
	}

	@Override
	public ButtonPanel createButtonPanel() {
		CancelButtonAction action = new CancelButtonAction(this);
		this.setDefaultAction(action);
		this.setDefaultCancelAction(action);
		
		JButton button = new JButton();
		button.setAction(action);
		button.setText("OK");
		
		ButtonPanel panel = new ButtonPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		panel.add(button);
		return panel;
	}

	@Override
	public JComponent createContentPanel() {
		return null;
	}
}
