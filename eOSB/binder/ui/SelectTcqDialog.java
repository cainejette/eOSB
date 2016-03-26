package eOSB.binder.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.ui.actions.OpenTcqAction;
import eOSB.game.controller.CancelAndShowAction;
import eOSB.game.controller.Handler;
import eOSB.game.controller.Round;
import eOSB.game.controller.Tcq;
import eOSB.game.data.IconFactory;
import eOSB.game.ui.RoundSelectionListCellRenderer;
import eOSB.game.ui.RoundSelectionListListener;
import eOSB.score.actions.OpenTcqResultsDialogAction;
import net.miginfocom.swing.MigLayout;

public class SelectTcqDialog extends StandardDialog {

	private JList list;
	private JFrame parent;
	private List<Tcq> availableTcqs;
	private List<String> openedTcqs;
	private JButton okButton;

	private Round round;
	private Handler handler;

	public SelectTcqDialog(Handler handler) {
		this.handler = handler;
		this.parent = handler.getFrame();
		this.round = handler.getCurrentRound();

		this.availableTcqs = new ArrayList<Tcq>();
		availableTcqs.addAll(round.getTcqs());
		availableTcqs.addAll(round.getTcqSolutions());
		this.openedTcqs = new ArrayList<String>();
		
		JLabel[] labels = new JLabel[this.availableTcqs.size()];
		for (int i = 0; i < this.availableTcqs.size(); i++) {
			JLabel label = new JLabel(this.availableTcqs.get(i).getName());
			labels[i] = label;
		}
		
		this.list = new JList(labels);

		this.init();
	}

	private void init() {
		this.setModal(false);
		this.pack();
		this.setTitle("TCQ Selection");

		this.setLocationRelativeTo(this.parent);
		this.setResizable(false);
		this.setVisible(true);
	}

	public JComponent createBannerPanel() {
		JLabel message = new JLabel("Select a TCQ:");
		JLabel note = new JLabel("<HTML>(<i>previously opened</i>, <b>not yet opened</b>)");
		message.setFont(new Font(message.getFont().getName(), Font.PLAIN, message.getFont().getSize() + 2));

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 6));
		panel.setLayout(new MigLayout());
		panel.add(message, "wrap");
		panel.add(note);
		return panel;
	}

	@Override
	public ButtonPanel createButtonPanel() {
		ButtonPanel panel = new ButtonPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
		panel.setLayout(new MigLayout("wrap 2"));

		JButton cancelButton = new JButton();
		CancelAndShowAction cancelAction = new CancelAndShowAction(this);
		this.setDefaultCancelAction(cancelAction);		
		cancelButton.setAction(cancelAction);
		
		ImageIcon cancelIcon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource(IconFactory.INCORRECT));
		cancelButton.setIcon(cancelIcon);
		
		panel.add(cancelButton, "w 150!, h 75!");

		this.okButton = new JButton();
		OpenTcqAction okAction = new OpenTcqAction(this, this.list, this.availableTcqs, this.openedTcqs, this.okButton);
		this.okButton.setAction(okAction);
		this.setDefaultAction(okAction);
		this.okButton.requestFocus();
		
		ImageIcon okIcon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource(IconFactory.NEXT));
		okButton.setIcon(okIcon);

		panel.add(this.okButton, "gapleft 10, w 150!, h 75!");
		
		if (this.handler.isUsingScoreboard()) {
			OpenTcqResultsDialogAction addTcqResultsAction = new OpenTcqResultsDialogAction(this, this.handler);
			JButton button = new JButton();
			button.setAction(addTcqResultsAction);
			panel.add(button, "span, growx, h 50!, gaptop 10");
		}

		return panel;
	}

	@Override
	public JComponent createContentPanel() {

		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("fill, insets 0, debug"));

		this.list.setCellRenderer(new RoundSelectionListCellRenderer(this.openedTcqs));
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.list.addListSelectionListener(
				new RoundSelectionListListener(this, this.okButton, this.list.getSelectedIndex()));
//		this.list.addMouseListener(new DoubleClickRoundListListener(this, this.list, this.timekeeper, this.scorekeeper));
		this.list.setSelectedIndex(0);
		
		JScrollPane scroller = new JScrollPane(this.list);
		scroller.setPreferredSize(new Dimension(316, 145));
		
		panel.add(scroller, "align center, grow, push, span");
		
		return panel;
	}
}
