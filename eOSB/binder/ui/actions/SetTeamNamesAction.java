package eOSB.binder.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTextField;

import org.bushe.swing.event.EventBus;

import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.controller.TeamNameEvent;
import eOSB.game.controller.Handler;
import eOSB.game.controller.Team;

/**
 * The action associated with setting names for each {@link Team}
 * @author cjette
 * 
 */
public class SetTeamNamesAction extends AbstractAction {

  private StandardDialog parent;
  private Handler handler;
  private JTextField teamAField;
  private JTextField teamBField;

  /**
   * @param parent the parent dialog
   * @param handler the {@link Handler}
   * @param teamAField the field from which to extract {@link Team} A's new name
   * @param teamBField the field from which to extract {@link Team} B's new name
   */
  public SetTeamNamesAction(StandardDialog parent, Handler handler, JTextField teamAField,
      JTextField teamBField) {
    super("OK");
    this.parent = parent;
    this.handler = handler;
    this.teamAField = teamAField;
    this.teamBField = teamBField;
  }

  /** {@inheritDoc} */
//  @Override
  public void actionPerformed(ActionEvent ae) {
    String teamAName = this.teamAField.getText().trim();
    String teamBName = this.teamBField.getText().trim();

    if (teamAName.equals("")) {
      teamAName = "Team A";
    }
    if (teamBName.equals("")) {
      teamBName = "Team B";
    }
    
    EventBus.publish(new TeamNameEvent(this, teamAName, teamBName));

    this.parent.setVisible(false);
    this.parent.dispose();
  }
}
