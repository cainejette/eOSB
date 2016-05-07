package eOSB.binder.ui;

import java.awt.Font;
import java.text.NumberFormat;

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
import eOSB.game.controller.Handler;
import eOSB.game.data.IconFactory;
import eOSB.score.ui.ImprovedFormattedTextField;
import net.miginfocom.swing.MigLayout;

public class SetScoresDialog extends StandardDialog {
	
	private final Handler handler;
	
	private ImprovedFormattedTextField teamATextField;
	private ImprovedFormattedTextField teamBTextField;
	
	private JButton okButton;

	public SetScoresDialog(Handler handler) {
		this.handler = handler;
		
		NumberFormat integerNumberInstance = NumberFormat.getIntegerInstance();
        teamATextField = new ImprovedFormattedTextField( integerNumberInstance, 20 );
        teamATextField.setColumns( 2 );
        teamBTextField = new ImprovedFormattedTextField( integerNumberInstance, 20 );
        teamBTextField.setColumns( 2 );
        
		teamATextField.setDocument(new LengthRestrictedDocument(2));
		teamBTextField.setDocument(new LengthRestrictedDocument(2));
		
		teamATextField.setText(Integer.toString(this.handler.getTeamA().getScore()));
		teamBTextField.setText(Integer.toString(this.handler.getTeamB().getScore()));
		
		this.init();
	}
	
	private void init() {
		this.pack();
		this.setModal(true);
		this.setTitle("Manual Score Override");
		this.setLocationRelativeTo(null);
		this.setResizable(false);
	}

	@Override
	public JComponent createBannerPanel() {
		JLabel message = new JLabel("Input manual score overrides:");
		message.setFont(new Font(message.getFont().getName(), Font.PLAIN, message.getFont().getSize() + 2));

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 6));
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

		this.okButton = new JButton();
		AbstractAction okAction = new SetScoresAction(this, this.teamATextField, this.teamBTextField);
		this.okButton.setAction(okAction);
		this.setDefaultAction(okAction);
		this.okButton.requestFocus();

		ImageIcon okIcon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource(IconFactory.CORRECT));
		okButton.setIcon(okIcon);

		panel.add(this.okButton, "gapleft 10, w 150!, h 75!");

		return panel;
	}

	@Override
	public JComponent createContentPanel() {
		final JLabel teamALabel = new JLabel(this.handler.getTeamA().getName());
		final JLabel teamBLabel = new JLabel(this.handler.getTeamB().getName());

		JPanel tcqAPanel = new JPanel();
		tcqAPanel.setLayout(new MigLayout("wrap 4, insets 0, fill"));
		tcqAPanel.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 6));
		tcqAPanel.add(teamALabel, "sizegroupx name, growx, pushx");
		tcqAPanel.add(this.teamATextField, "gapright 20, w 60!, h 45!, wrap");
		tcqAPanel.add(teamBLabel, "sizegroupx name, growx, pushx");
		tcqAPanel.add(this.teamBTextField, "w 60!, h 45!");

		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("wrap 1, insets 0, fill"));
		panel.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 6));
		panel.add(tcqAPanel, "growx, sizegroupx panel");

		return panel;
	}
}
