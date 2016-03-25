package eOSB.time.controller;

import javax.swing.JPanel;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventServiceEvent;
import org.bushe.swing.event.EventSubscriber;

import eOSB.binder.controller.QuestionEvent;
import eOSB.binder.controller.UpdateQuestionEvent;
import eOSB.game.controller.Question;

public class Timekeeper implements EventSubscriber<EventServiceEvent> {

	private CountDownTimer roundClock;
	private CountDownTimer questionClock;

	public Timekeeper() {
		this.roundClock = new CountDownTimer(360000, true);
		this.questionClock = new CountDownTimer(5000, false);

		EventBus.subscribe(QuestionEvent.class, this);
	}

	public void close() {
		this.roundClock.close();
		this.questionClock.close();
	}

	public JPanel getClockPanel(CountDownTimer clock) {
		return clock.getStandaloneTimer();
	}

	public CountDownTimer getQuestionClock() {
		return this.questionClock;
	}

	public CountDownTimer getRoundClock() {
		return this.roundClock;
	}

	public void onEvent(EventServiceEvent e) {
		if (e instanceof UpdateQuestionEvent) {
			this.questionClock.pause();

			UpdateQuestionEvent e2 = (UpdateQuestionEvent) e;
			Question question = e2.getQuestion();
			
			if (question != null) {
				switch (question.getType()) {
				case TOSSUP:
					this.questionClock.setTime(5000, true);
					this.questionClock.clearThresholds();
					break;
				case BONUS:
					this.questionClock.setTime(20000, true);
					this.questionClock.addThreshold(5000);    		  
					break;
				}
			}
		}
	}
}

