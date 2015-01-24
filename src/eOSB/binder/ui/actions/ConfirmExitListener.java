package eOSB.binder.ui.actions;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import eOSB.game.controller.Handler;
import eOSB.game.ui.ConfirmExitDialog;

/**
 * Listener used to verify user means to close out of eOSB.
 * @author Caine Jette
 * 
 */
public class ConfirmExitListener extends WindowAdapter {

  private final Handler handler;

  /**
   * Populates the listener with the main handler
   * @param handler the {@link Handler}
   */
  public ConfirmExitListener(Handler handler) {
    this.handler = handler;
  }

  /** {@inheritDoc} */
  @Override
  public void windowClosing(WindowEvent arg0) {
    final ConfirmExitDialog dialog = new ConfirmExitDialog(this.handler);
    dialog.setVisible(true);
  }
}
