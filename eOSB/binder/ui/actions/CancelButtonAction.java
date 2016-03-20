package eOSB.binder.ui.actions;

import com.jidesoft.dialog.StandardDialog;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class CancelButtonAction extends AbstractAction {
  StandardDialog dialog;

  public CancelButtonAction(StandardDialog dialog) {
    super("Cancel");
    this.dialog = dialog;
  }

  public void actionPerformed(ActionEvent ae) {
    this.dialog.setVisible(true);
    this.dialog.dispose();
  }
}