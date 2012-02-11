package eOSB.binder.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import eOSB.binder.ui.SelectTcqDialog;
import eOSB.game.controller.Handler;

/**
 * The action associated with clicking the TCQ Button
 * @author Caine Jette
 * 
 */
public class OpenSelectTcqDialogAction extends AbstractAction {

  private Handler handler;

  /**
   * @param handler the {@link Handler}
   */
  public OpenSelectTcqDialogAction(Handler handler) {
    super("TCQs");
    this.handler = handler;
  }

  /** {@inheritDoc} */
//  @Override
  public void actionPerformed(ActionEvent ae) {
    SelectTcqDialog dialog = new SelectTcqDialog(this.handler);
    dialog.setVisible(true);
  }
}
