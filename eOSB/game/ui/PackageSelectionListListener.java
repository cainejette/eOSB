package eOSB.game.ui;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.bushe.swing.event.EventBus;

import com.jidesoft.dialog.StandardDialog;

public class PackageSelectionListListener implements ListSelectionListener {

	private JButton button;
	private StandardDialog parent;

	public PackageSelectionListListener(StandardDialog parent, JButton button) {
		this.parent = parent;
		this.button = button;
	}

	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			JList list = (JList) e.getSource();
			int index = list.getSelectedIndex();
			if (index != -1) {
				System.out.println("new index: " + index);
				PackageSelectionListEvent event = new PackageSelectionListEvent(list, list.getSelectedIndex());
				EventBus.publish(event);
			} 
		}
	}
}
