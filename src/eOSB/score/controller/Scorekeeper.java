package eOSB.score.controller;


import java.util.ArrayList;
import java.util.List;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventServiceEvent;
import org.bushe.swing.event.EventSubscriber;

import eOSB.binder.ui.actions.AddTurnEvent;
import eOSB.game.controller.BuzzerTurn;
import eOSB.game.controller.TcqTurn;
import eOSB.game.controller.TcqTurnA;
import eOSB.game.controller.Team;
import eOSB.game.controller.Turn;
import eOSB.game.controller.WithdrawQuestionEvent;

/**
 * The class encapsulating the scorekeeping functionality of EOSB.
 * 
 * @author Caine Jette
 * 
 */
public class Scorekeeper implements EventSubscriber<EventServiceEvent> {
  
  private List<BuzzerTurn> buzzerTurns;
  private List<Turn> tcqTurns;
  private Team teamA;
  private Team teamB;

  /**
   * @param teamA {@link Team} A
   * @param teamB {@link Team} B
   */
  public Scorekeeper(Team teamA, Team teamB) {
    this.teamA = teamA;
    this.teamB = teamB;
    
    this.buzzerTurns = new ArrayList<BuzzerTurn>();
    this.tcqTurns = new ArrayList<Turn>();
    
    EventBus.subscribe(AddTurnEvent.class, this);
    EventBus.subscribe(WithdrawQuestionEvent.class, this);
  }

  /** {@inheritDoc} */
//  @Override
  public void onEvent(EventServiceEvent ese) {
    if (ese instanceof AddTurnEvent) {
      AddTurnEvent ate = (AddTurnEvent) ese;
      this.addTurn(ate.getTurn());
    }
    else if (ese instanceof WithdrawQuestionEvent) {
      this.removeTurn();
    }
  }
  
  /**
   * Adds a {@link Turn}.
   * @param turn the turn to add
   */
  public void addTurn(Turn turn) {
    if (turn instanceof TcqTurn) {
      if (turn instanceof TcqTurnA) {
        this.teamA.setTcqA(turn.getTeamAScore());
        this.teamB.setTcqA(turn.getTeamBScore());
      }
      else {
        this.teamA.setTcqB(turn.getTeamAScore());
        this.teamB.setTcqB(turn.getTeamBScore());
      }

      this.tcqTurns.add(turn);
    }
    else {
      this.buzzerTurns.add((BuzzerTurn) turn);

//      System.out.println("added turn");
//      for (BuzzerTurn turn1 : this.buzzerTurns) {
//        System.out.println(turn1.getNumber());
//      }
      this.teamA.addToScore(turn.getTeamAScore());
      this.teamB.addToScore(turn.getTeamBScore());
    }
    
    EventBus.publish(new TeamScoreNumberEvent(this, this.teamA.getScore(), this.teamB
        .getScore()));
  }
  
  /**
   * Removes the most recent {@link BuzzerTurn}.
   */
  public Turn removeTurn() {
    if (this.buzzerTurns.size() > 0) {
      Turn turn = this.buzzerTurns.remove(this.buzzerTurns.size() - 1);
      this.teamA.removeFromScore(turn.getTeamAScore());
      this.teamB.removeFromScore(turn.getTeamBScore());
      
//      System.out.println("removing turn");
//      for (BuzzerTurn turn1 : this.buzzerTurns ) {
//        System.out.println(turn1.getNumber());
//      }
      
      EventBus.publish(new TeamScoreNumberEvent(this, this.teamA.getScore(), this.teamB
          .getScore()));
      return turn;
//      EventBus.publish(new LastTurnEvent(this, turn));
    }
    return null;
  }
}
