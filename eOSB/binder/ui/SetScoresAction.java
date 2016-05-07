package eOSB.binder.ui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTextField;

import org.bushe.swing.event.EventBus;

import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.ui.actions.SetScoreEvent;

public class SetScoresAction extends AbstractAction {
	
	private final StandardDialog parent;
	private final JTextField teamAField;
	private final JTextField teamBField;
	
	public SetScoresAction(StandardDialog parent, JTextField teamAField, JTextField teamBField) {
		this.parent = parent;
		this.teamAField = teamAField;
		this.teamBField = teamBField;
	}

	public void actionPerformed(ActionEvent e) {
		SetScoreEvent event = new SetScoreEvent(Integer.parseInt(this.teamAField.getText()), Integer.parseInt(this.teamBField.getText()));
		EventBus.publish(event);
		
		this.parent.setVisible(false);
		this.parent.dispose();
	}
}
