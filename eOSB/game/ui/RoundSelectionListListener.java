package eOSB.game.ui;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.jidesoft.dialog.StandardDialog;

public class RoundSelectionListListener implements ListSelectionListener {

	private JButton button;
	private StandardDialog parent;

	public RoundSelectionListListener(StandardDialog parent, JButton button, int initialSelection) {
		this.parent = parent;
		this.button = button;
		
		if (initialSelection == -1) {
			this.button.setEnabled(false);
			this.parent.setDefaultAction(null);
		}
	}
	
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			JList list = (JList) e.getSource();
			if (list.getSelectedIndex() != -1) {
				this.button.setEnabled(true);
				this.parent.setDefaultAction(this.button.getAction());
			}
			else {
				this.button.setEnabled(false);
				this.parent.setDefaultAction(null);
			}
		}
	}
}
