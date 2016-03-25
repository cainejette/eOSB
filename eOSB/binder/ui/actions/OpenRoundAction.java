package eOSB.binder.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;

import org.bushe.swing.event.EventBus;

import com.jidesoft.dialog.StandardDialog;

public class OpenRoundAction extends AbstractAction {

	private StandardDialog dialog;
	private JList roundList;
	private JCheckBox useTimekeeper;
	private JCheckBox useScorekeeper;

	public OpenRoundAction(StandardDialog dialog, JList roundList, JCheckBox useTimekeeper, JCheckBox useScorekeeper) {
		this.dialog = dialog;
		this.roundList = roundList;
		this.useTimekeeper = useTimekeeper;
		this.useScorekeeper = useScorekeeper;
	}

	public void actionPerformed(ActionEvent e) {

		this.dialog.setVisible(false);

		System.out.println("[OpenRoundAction/actionPerformed] sending RoundSelectedEvent");
		EventBus.publish(new RoundSelectedEvent(this, ((JLabel)roundList.getSelectedValue()).getText(), useTimekeeper.isSelected(), useScorekeeper.isSelected()));

		this.dialog.dispose();
	}
}
