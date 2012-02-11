package eOSB.game.controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.bushe.swing.event.EventBus;

import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.ui.ShowBuzzerQuestionsEvent;

public class CancelAndShowAction extends AbstractAction {
  StandardDialog dialog;

  public CancelAndShowAction(StandardDialog dialog) {
    super("Cancel");
    this.dialog = dialog;
  }

  public void actionPerformed(ActionEvent ae) {
  	ShowBuzzerQuestionsEvent event = new ShowBuzzerQuestionsEvent(this);
  	EventBus.publish(event);
  	
    this.dialog.setVisible(true);
    this.dialog.dispose();
  }
}