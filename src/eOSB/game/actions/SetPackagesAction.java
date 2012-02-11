package eOSB.game.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.bushe.swing.event.EventBus;

import eOSB.game.ui.PackagesSelectedEvent;
import eOSB.game.ui.SelectPackageDialog;

public class SetPackagesAction extends AbstractAction {
  private SelectPackageDialog dialog;

  public SetPackagesAction(SelectPackageDialog dialog) {
    super("OK");
    this.dialog = dialog;
  }

  public void actionPerformed(ActionEvent ae) {
    this.dialog.setVisible(false);
    this.dialog.dispose();

    EventBus.publish(new PackagesSelectedEvent(this));
  }
}