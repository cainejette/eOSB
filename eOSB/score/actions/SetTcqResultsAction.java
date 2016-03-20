package eOSB.score.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTextField;

import org.bushe.swing.event.EventBus;

import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.ui.actions.AddTurnEvent;
import eOSB.game.controller.Handler;
import eOSB.game.controller.TcqTurnA;
import eOSB.game.controller.TcqTurnB;
import eOSB.game.controller.Team;
import eOSB.game.controller.Turn;
import eOSB.score.controller.TcqScoreResult;

/**
 * The action that adds TCQ results to the {@link Team} scores.
 * @author cjette
 * 
 */
public class SetTcqResultsAction extends AbstractAction {

  private final StandardDialog parent;
  private final Handler handler;
  private final JTextField teamAFieldA;
  private final JTextField teamAFieldB;
  private final JTextField teamBFieldA;
  private final JTextField teamBFieldB;

  /**
   * @param parent the parent dialog
   * @param handler the {@link Handler}
   * @param teamAFieldA teamA tcqA
   * @param teamAFieldB teamA tcqB
   * @param teamBFieldA teamB tcqA
   * @param teamBFieldB teamB tcqB
   */
  public SetTcqResultsAction(StandardDialog parent, Handler handler, JTextField teamAFieldA,
      JTextField teamAFieldB, JTextField teamBFieldA, JTextField teamBFieldB) {
    super("Add Scores");
    this.parent = parent;
    this.handler = handler;
    this.teamAFieldA = teamAFieldA;
    this.teamAFieldB = teamAFieldB;
    this.teamBFieldA = teamBFieldA;
    this.teamBFieldB = teamBFieldB;
  }

  /** {@inheritDoc} */
//  @Override
  public void actionPerformed(ActionEvent ae) {
    Turn tcqA = new TcqTurnA(new TcqScoreResult(Integer.parseInt(this.teamAFieldA.getText())),
        new TcqScoreResult(Integer.parseInt(this.teamBFieldA.getText())));
    EventBus.publish(new AddTurnEvent(tcqA));

    Turn tcqB = new TcqTurnB(new TcqScoreResult(Integer.parseInt(this.teamAFieldB.getText())),
        new TcqScoreResult(Integer.parseInt(this.teamBFieldB.getText())));
    EventBus.publish(new AddTurnEvent(tcqB));

    this.parent.setVisible(false);
    this.parent.dispose();
  }
}
