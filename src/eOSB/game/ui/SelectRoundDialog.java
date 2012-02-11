package eOSB.game.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import net.miginfocom.swing.MigLayout;

import org.bushe.swing.event.EventBus;

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.ui.actions.CancelButtonAction;
import eOSB.binder.ui.actions.OpenRoundAction;
import eOSB.game.controller.Round;

@SuppressWarnings("serial")
public class SelectRoundDialog extends StandardDialog {
	
  private JList list = new JList();
  private JFrame parent;
  private List<Round> availableRounds;
  private OpenRoundAction openRoundAction;
  private JButton okButton;

  public SelectRoundDialog(JFrame parent, List<Round> availableRounds) {
	  this.parent = parent;
	  this.availableRounds = availableRounds;
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
    panel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

    this.openRoundAction = new OpenRoundAction(this);
    this.setDefaultAction(this.openRoundAction);

    CancelButtonAction cancelAction = new CancelButtonAction(this);
    this.setDefaultCancelAction(cancelAction);

    this.okButton = new JButton();
    this.okButton.setAction(this.openRoundAction);
    this.okButton.requestFocus();
    panel.add(this.okButton);

    JButton button = new JButton();
    button.setAction(cancelAction);
    panel.add(button);

    return panel;
  }

  public JComponent createContentPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("fill, insets 0"));

		JLabel[] labels = new JLabel[this.availableRounds.size()];
		for (int i = 0; i < this.availableRounds.size(); i++) {
			JLabel label = new JLabel(this.availableRounds.get(i).getName());
			labels[i] = label;
		}

		this.list = new JList(labels);
		
		List<String> openedRounds = new ArrayList<String>();
		int lastOpened = -1;
		for (int i = 0; i < this.availableRounds.size(); i++) {
			Round round = this.availableRounds.get(i);
			if (round.wasOpened()) {
				openedRounds.add(round.getName());
				lastOpened = i;
			}
		}
		System.out.println("printing opened rounds, of which there are: " + openedRounds.size());
		for (String string : openedRounds) {
			System.out.println(string);
		}
		
		this.list.setCellRenderer(new RoundSelectionListRenderer(openedRounds));
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.list.addListSelectionListener(new RoundSelectionListListener(this, this.okButton, this.list.getSelectedIndex()));
		this.list.addMouseListener(new DoubleClickRoundListListener(this));

		JScrollPane scroller = new JScrollPane(this.list);
		scroller.setPreferredSize(new Dimension(316, 300));

		if (lastOpened + 1 < this.availableRounds.size()) {
			this.list.setSelectedIndex(lastOpened + 1);
			this.list.ensureIndexIsVisible(lastOpened + 1);
			EventBus.publish(new RoundSelectionListEvent(this, this.availableRounds.get(lastOpened + 1).getName()));
		}
		else {
			this.list.setSelectedIndex(-1);
			EventBus.publish(new RoundSelectionListEvent(this, null));
		}
		
		scroller.getVerticalScrollBar().setUnitIncrement(30);
		panel.add(scroller, "align center, grow, push, span");
		return panel;
  }
}