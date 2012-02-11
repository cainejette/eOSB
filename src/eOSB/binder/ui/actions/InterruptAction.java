package eOSB.binder.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

public class InterruptAction extends AbstractAction {

  private JButton button;
  
  public InterruptAction(JButton button) {
    this.button = button;
  }
  
  /** {@inheritDoc} */
//  @Override
  public void actionPerformed(ActionEvent ae) {
    if (this.button.isSelected()) {
      this.button.setSelected(false);
    }
    else {
      this.button.setSelected(true);
    }
  }
}
