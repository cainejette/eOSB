package eOSB.binder.controller;

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

import eOSB.binder.ui.actions.CancelButtonAction;
import eOSB.game.controller.Handler;

public class AboutEOSBDialog extends StandardDialog {

	private JFrame parent;
	
	public AboutEOSBDialog(JFrame parent) {
		this.parent = parent;
		this.init();
	}
	
	private void init() {
		this.setModal(true);
		this.pack();
		this.setTitle("About eOSB");
		
		this.setResizable(false);
		this.setLocationRelativeTo(this.parent);
		this.setVisible(true);
	}
	
	@Override
	public JComponent createBannerPanel() {
		JLabel label = new JLabel("eOSB is dedicated towards making NOSB greener and more sustainable.");
		JLabel label4 = new JLabel("A sister project, eNSB, serves a parallel function for the NSB competition.");
		JLabel label2 = new JLabel("Both are maintained by NSB + NOSB alumnus Caine Jette, reachable via jette@alum.mit.edu.");
    label.setFont(new Font(label.getFont().getName(), Font.PLAIN, label.getFont().getSize() + 2));

		
		JLabel label3 = new JLabel("<HTML>This particular question set is described by code: <b>" + Handler.QUESTION_CODE + "</b></HTML>" );
		
		JPanel panel = new JPanel();
    panel.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 6));
    panel.setLayout(new MigLayout());
    panel.add(label, "wrap");
    panel.add(label4, "wrap, gaptop 20");
    panel.add(label2, "wrap");
    panel.add(label3, "gaptop 20");
    
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
