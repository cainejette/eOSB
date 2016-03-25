package eOSB.game.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.bushe.swing.event.EventBus;

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.ui.actions.CancelButtonAction;
import eOSB.binder.ui.actions.OpenRoundAction;
import eOSB.game.controller.Round;
import eOSB.game.data.IconFactory;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class SelectRoundDialog extends StandardDialog {

	private JList list;
	private JFrame parent;
	private List<Round> availableRounds;
	private JButton okButton;
	
	private JCheckBox timekeeper;
	private JCheckBox scorekeeper;

	public SelectRoundDialog(JFrame parent, List<Round> availableRounds, boolean useTimekeeper, boolean useScorekeeper) {
		super(parent, true);
		this.parent = parent;
		this.availableRounds = availableRounds;
		
		JLabel[] labels = new JLabel[this.availableRounds.size()];
		for (int i = 0; i < this.availableRounds.size(); i++) {
			JLabel label = new JLabel(this.availableRounds.get(i).getName());
			labels[i] = label;
		}
		
		this.list = new JList(labels);

		this.timekeeper = new JCheckBox("Timekeeper", useTimekeeper);
		this.scorekeeper = new JCheckBox("Scorekeeper", useScorekeeper);

		this.init();
	}

	private void init() {
		this.pack();
		this.setTitle("Round Selection");

		this.setLocationRelativeTo(this.parent);
		this.setResizable(false);
		System.out.println("round dialog size: " + this.getSize());
	}

	public JComponent createBannerPanel() {
		JLabel message = new JLabel("Select a round:");
		JLabel note = new JLabel("<HTML>(<i>previously opened</i>, <b>not yet opened</b>)");
		message.setFont(new Font(message.getFont().getName(), Font.PLAIN, message.getFont().getSize() + 2));

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 6));
		panel.setLayout(new MigLayout());
		panel.add(message, "wrap");
		panel.add(note);
		return panel;
	}

	public ButtonPanel createButtonPanel() {
		ButtonPanel panel = new ButtonPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
		panel.setLayout(new MigLayout());

		JButton cancelButton = new JButton();
		CancelButtonAction cancelAction = new CancelButtonAction(this);
		this.setDefaultCancelAction(cancelAction);		
		cancelButton.setAction(cancelAction);
		
		ImageIcon cancelIcon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource(IconFactory.INCORRECT));
		cancelButton.setIcon(cancelIcon);
		
		panel.add(cancelButton, "w 150!, h 75!");

		this.okButton = new JButton();
		OpenRoundAction okAction = new OpenRoundAction(this, this.list, this.timekeeper, this.scorekeeper);
		this.okButton.setAction(okAction);
		this.setDefaultAction(okAction);
		this.okButton.requestFocus();
		
		ImageIcon okIcon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource(IconFactory.NEXT));
		okButton.setIcon(okIcon);

		panel.add(this.okButton, "gapleft 10, w 150!, h 75!");

		return panel;
	}

	public JComponent createContentPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("fill, insets 0"));

		List<String> openedRounds = new ArrayList<String>();
		int lastOpened = -1;
		for (int i = 0; i < this.availableRounds.size(); i++) {
			Round round = this.availableRounds.get(i);
			if (round.wasOpened()) {
				openedRounds.add(round.getName());
				lastOpened = i;
			}
		}

		this.list.setCellRenderer(new RoundSelectionListRenderer(openedRounds));
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.list.addListSelectionListener(
				new RoundSelectionListListener(this, this.okButton, this.list.getSelectedIndex()));
		this.list.addMouseListener(new DoubleClickRoundListListener(this, this.list, this.timekeeper, this.scorekeeper));

		JScrollPane scroller = new JScrollPane(this.list);
		scroller.setPreferredSize(new Dimension(316, 250));

		if (lastOpened + 1 < this.availableRounds.size()) {
			this.list.setSelectedIndex(lastOpened + 1);
//			this.list.getSe
//			this.list.
			this.list.ensureIndexIsVisible(lastOpened - 5);
		} else {
			this.list.setSelectedIndex(-1);
		}

		scroller.getVerticalScrollBar().setUnitIncrement(30);
		panel.add(scroller, "align center, grow, push, span");
		
		panel.add(makePackageSelectionPanel(), "spanx, growx");
		return panel;
	}
	
	private JPanel makePackageSelectionPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(6, 15, 0, 6));
		panel.setLayout(new MigLayout("fill"));
		
		JLabel message = new JLabel("Additional options:");
		message.setFont(new Font(message.getFont().getName(), Font.PLAIN, message.getFont().getSize() + 2));
		panel.add(message, "wrap");
		panel.add(timekeeper, "spanx 3");
		panel.add(scorekeeper, "grow");
		
		return panel;
	}
}