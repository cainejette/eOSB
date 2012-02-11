package eOSB.score.controller;

import org.bushe.swing.event.EventServiceEvent;

import eOSB.game.controller.Turn;

public class LastTurnEvent implements EventServiceEvent {

  private Object source;
  private Turn turn;

  public LastTurnEvent(Object source, Turn turn) {
    this.source = source;
    this.turn = turn;
  }
  
  public Object getSource() {
    return this.source;
  }
  
  public Turn getTurn() {
    return this.turn;
  }
}
