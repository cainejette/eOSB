package eOSB.binder.ui;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.ui.actions.CancelButtonAction;
import eOSB.binder.ui.actions.SetTeamNamesAction;
import eOSB.game.controller.Handler;
import eOSB.game.controller.Team;
import eOSB.game.data.IconFactory;

/**
 * The dialog used to set {@link Team} names.
 * @author cjette
 * 
 */
public class SetTeamNamesDialog extends StandardDialog {

  private Handler handler;

  private JTextField teamAField = new JTextField(15);
  private JTextField teamBField = new JTextField(15);

  /**
   * @param handler the {@link Handler}
   */
  public SetTeamNamesDialog(Handler handler) {
    this.handler = handler;
    this.init();
  }

  /**
   * Basic dialog initializations.
   */
  private void init() {
    this.pack();
    this.setTitle("Set Team Names");
//    this.setIconImage(new ImageIcon(SetTeamNamesDialog.class.getResource(IconFactory.LOGO))
//        .getImage());
    this.setLocationRelativeTo(this.handler.getFrame());
    this.setResizable(false);
    this.setVisible(true);
  }

  /** {@inheritDoc} */
  @Override
  public JComponent createBannerPanel() {
  	JLabel message = new JLabel("Input desired team names:");
//    JLabel note = new JLabel("<HTML>(<i>previously opened</i>, <b>not yet opened</b>)");
    message.setFont(new Font(message.getFont().getName(), Font.PLAIN, message.getFont().getSize() + 2));

    JPanel panel = new JPanel();
    panel.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 6));
    panel.setLayout(new MigLayout());
    panel.add(message, "wrap");
//    panel.add(note);
    return panel;
  }

  /** {@inheritDoc} */
  @Override
  public ButtonPanel createButtonPanel() {
    ButtonPanel panel = new ButtonPanel();
    panel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

    SetTeamNamesAction setTeamNamesAction = new SetTeamNamesAction(this, this.handler,
        this.teamAField, this.teamBField);
    this.setDefaultAction(setTeamNamesAction);

    CancelButtonAction cancelAction = new CancelButtonAction(this);
    this.setDefaultCancelAction(cancelAction);

    JButton button = new JButton();
    button.setAction(setTeamNamesAction);
    button.requestFocus();
    panel.add(button);

    button = new JButton();
    button.setAction(cancelAction);
    panel.add(button);

    return panel;
  }

  /** {@inheritDoc} */
  @Override
  public JComponent createContentPanel() {
    final JLabel teamALabel = new JLabel(this.handler.getTeamA().getName());
    final JLabel teamBLabel = new JLabel(this.handler.getTeamB().getName());

    JPanel panel = new JPanel();
    panel.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
    panel.setLayout(new MigLayout( "wrap 2, insets 0"));
    panel.add(teamALabel);
    panel.add(this.teamAField, "gapx 5");
    panel.add(teamBLabel, "gapy 10");
    panel.add(this.teamBField, "gapx 5, gapy 10");

    return panel;
  }
}
