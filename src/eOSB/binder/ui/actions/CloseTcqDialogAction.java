package eOSB.binder.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.bushe.swing.event.EventBus;

import eOSB.binder.ui.SelectTcqDialog;
import eOSB.binder.ui.ShowBuzzerQuestionsEvent;
import eOSB.game.controller.Handler;

/**
 * The action associated with closing the {@link SelectTcqDialog}
 * @author cjette
 * 
 */
public class CloseTcqDialogAction extends AbstractAction {

  private SelectTcqDialog dialog;

  /**
   * @param dialog the {@link SelectTcqDialog}
   */
  public CloseTcqDialogAction(SelectTcqDialog dialog) {
    super("Close Dialog");
    this.dialog = dialog;
  }

  /** {@inheritDoc} */
//  @Override
  public void actionPerformed(ActionEvent ae) {
  	ShowBuzzerQuestionsEvent event = new ShowBuzzerQuestionsEvent(this);
  	EventBus.publish(event);

  	this.dialog.setVisible(false);
    this.dialog.dispose();
  }
}
