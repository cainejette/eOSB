package eOSB.binder.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import eOSB.game.controller.Handler;
import eOSB.game.ui.ValidateUserDialog;

/**
 * The action associated with clicking File --> Open round
 * @author Caine Jette
 * 
 */
public class OpenValidateUserDialogAction extends AbstractAction {

  private Handler handler;

  /**
   * @param handler the handler to pass validation for user to
   */
  public OpenValidateUserDialogAction(Handler handler) {
    this.handler = handler;
  }

  /**
   * {@inheritDoc}
   */
  public void actionPerformed(ActionEvent ae) {
    ValidateUserDialog dialog = new ValidateUserDialog(this.handler);
  }
}
