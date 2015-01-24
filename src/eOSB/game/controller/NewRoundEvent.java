package eOSB.game.controller;

import org.bushe.swing.event.EventServiceEvent;

public class NewRoundEvent implements EventServiceEvent {
  
	private Object source;
	private String name;

  public NewRoundEvent(Object source, String name) {
    this.source = source;
    this.name = name;
  }

  public Object getSource() {
    return this.source;
  }

  public String getName() {
    return this.name;
  }
}