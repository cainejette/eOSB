package eOSB.binder.controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import eOSB.game.controller.Handler;

public class OpenTcqPreambleDialogAction extends AbstractAction {

  private Handler handler;
  
  public OpenTcqPreambleDialogAction(Handler handler) {
    this.handler = handler;
  }
  
  public void actionPerformed(ActionEvent ae) {
//    TcqPreambleDialog dialog = new TcqPreambleDialog(this.handler);
//    dialog.setVisible(true);
  }
}
