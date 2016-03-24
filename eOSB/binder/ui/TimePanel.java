package eOSB.binder.ui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import eOSB.game.controller.Handler;

public class TimePanel extends JPanel {
	
	public TimePanel(Handler handler) {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		JPanel subPanel = handler.getTimekeeper().getClockPanel(handler.getTimekeeper().getRoundClock());
		subPanel.setBorder(new TitledBorder("Round Timer"));
		this.add(subPanel);

		subPanel = handler.getTimekeeper().getClockPanel(handler.getTimekeeper().getQuestionClock());
		subPanel.setBorder(new TitledBorder("Question Timer"));
		this.add(subPanel);
	}
}
