package eOSB.binder.ui;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import net.miginfocom.swing.MigLayout;

import org.bushe.swing.event.EventBus;

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.ui.actions.CloseTcqDialogAction;
import eOSB.binder.ui.actions.OpenTcqAction;
import eOSB.game.controller.Handler;
import eOSB.game.controller.Round;
import eOSB.game.controller.Tcq;
import eOSB.score.actions.OpenTcqResultsDialogAction;

/**
 * The dialog associated with clicking the File-->Open Tcqs button
 * 
 * @author Caine Jette
 * 
 */
public class SelectTcqDialog extends StandardDialog {

	private JFrame parent; 
	private Handler handler;
	private Round round;
	
  private ButtonGroup buttonGroup = new ButtonGroup();
  private Map<String, Tcq> map = new HashMap<String, Tcq>();
  private List<Tcq> availableRounds;
  private List<JRadioButton> radioButtons;

  /**
   * @param handler the {@link Handler}
   */
  public SelectTcqDialog(Handler handler) {
  	this.handler = handler;
  	this.parent = handler.getFrame();
  	this.round = handler.getCurrentRound();
  	
    this.availableRounds = (ArrayList<Tcq>) round.getTcqs();
    this.availableRounds.addAll((ArrayList<Tcq>) round.getTcqSolutions());
    this.radioButtons = new ArrayList<JRadioButton>();

    JRadioButton button;
    for (int i = 0; i < availableRounds.size(); i++) {
      button = availableRounds.get(i).getButton();
      this.map.put(button.getActionCommand(), availableRounds.get(i));
      this.radioButtons.add(button);
      this.buttonGroup.add(button);
    }

    this.init();
  }

  /**
   * Basic dialog initializations.
   */
  private void init() {
    this.setModal(false);
    this.pack();
    this.setTitle("TCQ Selection");
    
    this.setLocationRelativeTo(this.parent);
    this.setResizable(false);
  }

  /**
   * Sets the button as selected.
   * @param button the button to set as selected.
   */
  public void setSelected(JRadioButton button) {
//    button.setFont(new Font("Helvetica", Font.ITALIC, 18));
    int index = this.radioButtons.indexOf(button);
    if (index < this.radioButtons.size()) {
      this.radioButtons.get(index).setSelected(true);
    }
  }

  /** {@inheritDoc} */
  @Override
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

  /** {@inheritDoc} */
  @Override
  public ButtonPanel createButtonPanel() {
    final ButtonPanel panel = new ButtonPanel();
    panel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

    OpenTcqAction openTcqAction = new OpenTcqAction(this, this.map,
        this.buttonGroup);
    CloseTcqDialogAction cancelAction = new CloseTcqDialogAction(this);
    this.setDefaultCancelAction(cancelAction);
    this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

    JButton button;

    button = new JButton();
    button.setAction(openTcqAction);
    this.setDefaultAction(openTcqAction);
    panel.add(button);

    OpenTcqResultsDialogAction addTcqResultsAction = new OpenTcqResultsDialogAction(this,
        this.handler);
    button = new JButton();
    button.setAction(addTcqResultsAction);
    panel.add(button);
    if (!this.handler.isUsingScoreboard()) {
      button.setEnabled(false);
    }

    button = new JButton();
    button.setAction(cancelAction);
    this.setDefaultCancelAction(cancelAction);
    button.setText("Close");
    panel.add(button);
    
    return panel;
  }

  /** {@inheritDoc} */
  @Override
  public JComponent createContentPanel() {

    JTabbedPane pane = new JTabbedPane();
    JPanel questionTab = new JPanel();
    questionTab.setLayout(new GridLayout(2, 1));
    questionTab.add(radioButtons.get(0));
    questionTab.add(radioButtons.get(1));

    JPanel solutionTab = new JPanel();
    solutionTab.setLayout(new GridLayout(2, 1));
    solutionTab.add(radioButtons.get(2));
    solutionTab.add(radioButtons.get(3));

    pane.addTab("Questions", questionTab);
    pane.addTab("Solutions", solutionTab);

    // preselect the round and set its tab as open
    for (int i = 0; i < availableRounds.size(); i++) {
      if (!availableRounds.get(i).wasPreviouslyOpened()) {
        radioButtons.get(i).setSelected(true);
        if (i < 2) {
          pane.setSelectedIndex(0);
        }
        else {
          pane.setSelectedIndex(1);
        }
        break;
      }
    }

    JPanel panel = new JPanel();
    panel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
    panel.add(pane);

    return panel;
  }
}
