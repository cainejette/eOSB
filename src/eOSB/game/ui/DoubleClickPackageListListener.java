package eOSB.game.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.ListModel;

import org.bushe.swing.event.EventBus;

import com.jidesoft.dialog.StandardDialog;

public class DoubleClickPackageListListener extends MouseAdapter {

	private StandardDialog dialog;
	private JList list;

	public DoubleClickPackageListListener(StandardDialog dialog, JList list) {
		this.dialog = dialog;
		this.list = list;
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			
			int index = list.locationToIndex(e.getPoint());
			if (index == 0) {
				this.dialog.setVisible(false);
		    EventBus.publish(new PackagesSelectedEvent(this));
				this.dialog.dispose();
			}
		}
	}
}
