package eOSB.binder.ui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import eOSB.game.controller.Handler;

public class TimePanel extends JPanel {
	
	public TimePanel(Handler handler) {
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		JPanel subPanel = handler.getTimekeeper().getQuestionClock().getPanel();
		subPanel.setBorder(new TitledBorder("Question Timer"));
		this.add(subPanel);
		
		subPanel = handler.getTimekeeper().getRoundClock().getPanel();
		subPanel.setBorder(new TitledBorder("Round Timer"));
		this.add(subPanel);
	}
}
