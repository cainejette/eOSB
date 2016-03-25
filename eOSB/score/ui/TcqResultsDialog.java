package eOSB.score.ui;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.controller.TcqResultsTabListener;
import eOSB.binder.ui.actions.CancelButtonAction;
import eOSB.game.controller.Handler;
import eOSB.score.actions.SetTcqResultsAction;
import net.miginfocom.swing.MigLayout;

public class TcqResultsDialog extends StandardDialog {

	private final Handler handler;
	private JTextField teamAFieldA = new JTextField(8);
	private JTextField teamAFieldB = new JTextField(8);
	private JTextField teamBFieldA = new JTextField(8);
	private JTextField teamBFieldB = new JTextField(8);
	private JButton okButton;
	private JButton cancelButton;

	public TcqResultsDialog(Handler handler) {
		this.handler = handler;

		this.teamAFieldA.setText(Integer.toString(this.handler.getTeamA().getTcqA().getWorth()));
		this.teamAFieldB.setText(Integer.toString(this.handler.getTeamA().getTcqB().getWorth()));
		this.teamBFieldA.setText(Integer.toString(this.handler.getTeamB().getTcqA().getWorth()));
		this.teamBFieldB.setText(Integer.toString(this.handler.getTeamB().getTcqB().getWorth()));

		this.init();
	}

	private void init() {
		this.pack();
		this.setTitle("TCQ Results");
		this.setLocationRelativeTo(this.handler.getFrame());
		this.setResizable(false);
		TcqResultsTabListener keyListener = new TcqResultsTabListener(this.teamAFieldA, this.teamAFieldB,
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
		final ButtonPanel panel = new ButtonPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

		SetTcqResultsAction setTcqResultsAction = new SetTcqResultsAction(this, this.teamAFieldA, this.teamAFieldB,
				this.teamBFieldA, this.teamBFieldB);
		CancelButtonAction cancelAction = new CancelButtonAction(this);

		this.okButton = new JButton(setTcqResultsAction);
		this.setDefaultAction(setTcqResultsAction);
		panel.add(this.okButton);

		this.cancelButton = new JButton(cancelAction);
		this.setDefaultCancelAction(cancelAction);
		panel.add(this.cancelButton);

		return panel;
	}

	@Override
	public JComponent createContentPanel() {

		final JLabel tcqALabel = new JLabel(
				"<HTML><b>" + this.handler.getCurrentRound().getName() + " TCQ A</b></HTML>");
		final JLabel tcqBLabel = new JLabel(
				"<HTML><b>" + this.handler.getCurrentRound().getName() + " TCQ B</b></HTML>");

		final JLabel teamALabel1 = new JLabel(this.handler.getTeamA().getName());
		final JLabel teamALabel2 = new JLabel(this.handler.getTeamA().getName());
		final JLabel teamBLabel1 = new JLabel(this.handler.getTeamB().getName());
		final JLabel teamBLabel2 = new JLabel(this.handler.getTeamB().getName());

		JPanel tcqAPanel = new JPanel();
		tcqAPanel.setLayout(new MigLayout("wrap 4, insets 0, fill"));
		tcqAPanel.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 6));
		tcqAPanel.add(tcqALabel, "wrap, alignx center, growx");
		tcqAPanel.add(teamALabel1, "sizegroupx name, growx, pushx");
		tcqAPanel.add(this.teamAFieldA, "gapright 20, sizegroupx label");
		tcqAPanel.add(teamBLabel1, "sizegroupx name");
		tcqAPanel.add(this.teamBFieldA, "sizegroupx label");

		JPanel tcqBPanel = new JPanel();
		tcqBPanel.setLayout(new MigLayout("wrap 4, insets 0, fill"));
		tcqBPanel.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 6));
		tcqBPanel.add(tcqBLabel, "wrap, alignx center, growx");
		tcqBPanel.add(teamALabel2, "sizegroupx name, growx, pushx");
		tcqBPanel.add(this.teamAFieldB, "gapright 20, sizegroupx label");
		tcqBPanel.add(teamBLabel2, "sizegroupx name, growx, pushx");
		tcqBPanel.add(this.teamBFieldB, "sizegroupx label");

		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("wrap 1, insets 0, fill"));
		panel.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 6));
		panel.add(tcqAPanel, "growx, sizegroupx panel");
		panel.add(tcqBPanel, "gapy 10, growx, sizegroupx panel");

		return panel;
	}
}
