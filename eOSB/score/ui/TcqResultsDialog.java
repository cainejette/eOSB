package eOSB.score.ui;

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

import eOSB.binder.controller.TcqResultsTabListener;
import eOSB.binder.ui.LengthRestrictedDocument;
import eOSB.binder.ui.actions.CancelButtonAction;
import eOSB.game.controller.Handler;
import eOSB.game.data.IconFactory;
import eOSB.score.actions.SetTcqResultsAction;
import net.miginfocom.swing.MigLayout;

public class TcqResultsDialog extends StandardDialog {

	private final Handler handler;
	private ImprovedFormattedTextField teamAFieldA;
	private ImprovedFormattedTextField teamAFieldB;
	private ImprovedFormattedTextField teamBFieldA;
	private ImprovedFormattedTextField teamBFieldB;

	private JButton okButton;
	private JButton cancelButton;

	public TcqResultsDialog(Handler handler) {
		this.handler = handler;
		
		NumberFormat integerNumberInstance = NumberFormat.getIntegerInstance();
        teamAFieldA = new ImprovedFormattedTextField( integerNumberInstance, 20 );
        teamAFieldA.setColumns( 2 );
        teamAFieldB = new ImprovedFormattedTextField( integerNumberInstance, 20 );
        teamAFieldB.setColumns( 2 );
        teamBFieldA = new ImprovedFormattedTextField( integerNumberInstance, 20 );
        teamBFieldA.setColumns( 2 );
        teamBFieldB = new ImprovedFormattedTextField( integerNumberInstance, 20 );
        teamBFieldB.setColumns( 2 );
        
		teamAFieldA.setDocument(new LengthRestrictedDocument(2));
		teamAFieldB.setDocument(new LengthRestrictedDocument(2));
		teamBFieldA.setDocument(new LengthRestrictedDocument(2));
		teamBFieldB.setDocument(new LengthRestrictedDocument(2));
		
		teamAFieldA.setText(Integer.toString(this.handler.getTeamA().getTcqA().getWorth()));
		this.teamAFieldB.setText(Integer.toString(this.handler.getTeamA().getTcqB().getWorth()));
		this.teamBFieldA.setText(Integer.toString(this.handler.getTeamB().getTcqA().getWorth()));
		this.teamBFieldB.setText(Integer.toString(this.handler.getTeamB().getTcqB().getWorth()));

		this.init();
	}

	private void init() {
		this.pack();
		this.setModal(true);
		this.setTitle("TCQ Results");
		this.setLocationRelativeTo(null);
//		this.setLocationRelativeTo(this.handler.getFrame());
		this.setResizable(false);
		TcqResultsTabListener keyListener = new TcqResultsTabListener(teamAFieldA, this.teamAFieldB,
				this.teamBFieldA, this.teamBFieldB, this.okButton, this.cancelButton);
		this.addKeyListener(keyListener);
	}

	@Override
	public JComponent createBannerPanel() {
		JLabel message = new JLabel("Input TCQ results:");
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
		AbstractAction okAction = new SetTcqResultsAction(this, this.teamAFieldA, this.teamAFieldB, this.teamBFieldA,
				this.teamBFieldB);
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
		final JLabel tcqALabel = new JLabel(
				"<HTML><b>TCQ A</b></HTML>");
		final JLabel tcqBLabel = new JLabel(
				"<HTML><b>TCQ B</b></HTML>");

		final JLabel teamALabel1 = new JLabel(this.handler.getTeamA().getName());
		final JLabel teamALabel2 = new JLabel(this.handler.getTeamA().getName());
		final JLabel teamBLabel1 = new JLabel(this.handler.getTeamB().getName());
		final JLabel teamBLabel2 = new JLabel(this.handler.getTeamB().getName());

		JPanel tcqAPanel = new JPanel();
		tcqAPanel.setLayout(new MigLayout("wrap 4, insets 0, fill"));
		tcqAPanel.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 6));
		tcqAPanel.add(tcqALabel, "wrap, alignx center, growx");
		tcqAPanel.add(teamALabel1, "sizegroupx name, growx, pushx");
		tcqAPanel.add(this.teamAFieldA, "gapright 20, w 60!, h 45!, wrap");
		tcqAPanel.add(teamBLabel1, "sizegroupx name, growx, pushx");
		tcqAPanel.add(this.teamBFieldA, "w 60!, h 45!");

		JPanel tcqBPanel = new JPanel();
		tcqBPanel.setLayout(new MigLayout("wrap 4, insets 0, fill"));
		tcqBPanel.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 6));
		tcqBPanel.add(tcqBLabel, "wrap, alignx center, growx");
		tcqBPanel.add(teamALabel2, "sizegroupx name, growx, pushx");
		tcqBPanel.add(this.teamAFieldB, "gapright 20, w 60!, wrap, h 45!");
		tcqBPanel.add(teamBLabel2, "sizegroupx name, growx, pushx");
		tcqBPanel.add(this.teamBFieldB, "w 60!, h 45!");

		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("wrap 1, insets 0, fill"));
		panel.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 6));
		panel.add(tcqAPanel, "growx, sizegroupx panel");
		panel.add(tcqBPanel, "gapy 10, growx, sizegroupx panel");

		return panel;
	}
}
