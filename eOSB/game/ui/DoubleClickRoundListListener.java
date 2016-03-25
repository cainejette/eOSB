package eOSB.game.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;

import org.bushe.swing.event.EventBus;

import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.ui.actions.RoundSelectedEvent;

public class DoubleClickRoundListListener extends MouseAdapter {

	private StandardDialog dialog;
	private JList roundList;
	private JCheckBox useTimekeeper;
	private JCheckBox useScorekeeper;

	public DoubleClickRoundListListener(StandardDialog dialog, JList roundList, JCheckBox useTimekeeper, JCheckBox useScorekeeper) {
		this.dialog = dialog;
		this.roundList = roundList;
		this.useTimekeeper = useTimekeeper;
		this.useScorekeeper = useScorekeeper;
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			this.dialog.setVisible(false);
			EventBus.publish(new RoundSelectedEvent(this, ((JLabel)roundList.getSelectedValue()).getText(), useTimekeeper.isSelected(), useScorekeeper.isSelected()));
			this.dialog.dispose();
		}
	}
}
