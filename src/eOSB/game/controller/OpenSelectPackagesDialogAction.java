package eOSB.game.controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.bushe.swing.event.EventBus;

public class OpenSelectPackagesDialogAction extends AbstractAction {
  private DisplayEulaDialog dialog;

  public OpenSelectPackagesDialogAction(DisplayEulaDialog dialog) {
    this.dialog = dialog;
  }

  public void actionPerformed(ActionEvent arg0) {
    this.dialog.setVisible(false);
    this.dialog.dispose();
    
    EventBus.publish(new ShowSelectPackagesDialogEvent(this));
  }
}