package eOSB.binder.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.bushe.swing.event.EventBus;

import eOSB.game.controller.Handler;
import eOSB.game.controller.WithdrawQuestionEvent;

/**
 * The action associated with clicking the "Back" button
 * @author Caine Jette
 * 
 */
public class BackButtonAction extends AbstractAction {
  
  private Handler handler;

  public BackButtonAction(Handler handler) {
    this.handler = handler;
  }

  /** {@inheritDoc} */
//  @Override
  public void actionPerformed(ActionEvent event) {
    this.handler.previousQuestion();
  }
}
