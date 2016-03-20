package eOSB.game.controller;

import org.bushe.swing.event.EventServiceEvent;

import eOSB.game.data.QuestionXMLParser;

public class RoundLoadedEvent implements EventServiceEvent {

	private Object source;
	private QuestionXMLParser parser;
	
	public RoundLoadedEvent(Object source, QuestionXMLParser parser) {
		this.source = source;
		this.parser = parser;
	}
	
	public Object getSource() {
		return this.source;
	}
	
	public QuestionXMLParser getParser() {
		return this.parser;
	}
}
