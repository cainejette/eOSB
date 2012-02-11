package eOSB.game.controller;

import org.bushe.swing.event.EventServiceEvent;

import eOSB.game.data.XmlParser;

public class RoundLoadedEvent implements EventServiceEvent {

	private Object source;
	private XmlParser parser;
	
	public RoundLoadedEvent(Object source, XmlParser parser) {
		this.source = source;
		this.parser = parser;
	}
	
	public Object getSource() {
		return this.source;
	}
	
	public XmlParser getParser() {
		return this.parser;
	}
}
