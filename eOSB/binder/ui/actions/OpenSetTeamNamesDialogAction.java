package eOSB.binder.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import eOSB.binder.ui.SetTeamNamesDialog;
import eOSB.game.controller.Handler;

/**
 * The action associated with setting user-defined names for the teams
 * @author Caine Jette
 * 
 */
public class OpenSetTeamNamesDialogAction extends AbstractAction {

  private final Handler handler;

  /**
   * Populates the action with the necessary data
   * @param handler the handle on the main handler
   */
  public OpenSetTeamNamesDialogAction(Handler handler) {
    super("Set Team Names");
    this.handler = handler;
  }

  /**
   * {@inheritDoc}
   */
//  @Override
  public void actionPerformed(ActionEvent ae) {
    SetTeamNamesDialog dialog = new SetTeamNamesDialog(this.handler);
  }
}
