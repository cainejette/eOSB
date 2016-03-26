package eOSB.game.controller;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import org.bushe.swing.event.EventBus;

import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.ui.ShowBuzzerQuestionsEvent;

public class CancelAndShowAction extends AbstractAction {
	StandardDialog dialog;
	private List<JFrame> frames;

	public CancelAndShowAction(StandardDialog dialog) {
		this.dialog = dialog;
	}

	public CancelAndShowAction(StandardDialog dialog, List<JFrame> frames) {
		this.dialog = dialog;
		this.frames = frames;
	}

	public void actionPerformed(ActionEvent ae) {

		if (this.frames != null) {
			for (JFrame frame : this.frames) {
				frame.setVisible(false);
				frame.dispose();
			}
		}
		
		ShowBuzzerQuestionsEvent event = new ShowBuzzerQuestionsEvent(this);
		EventBus.publish(event);

		this.dialog.setVisible(false);
		this.dialog.dispose();
	}
}