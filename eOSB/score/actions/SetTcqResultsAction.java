package eOSB.score.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTextField;

import org.bushe.swing.event.EventBus;

import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.ui.actions.AddTurnEvent;
import eOSB.game.controller.TcqTurn;
import eOSB.game.controller.Turn;
import eOSB.score.controller.TcqScoreResult;

public class SetTcqResultsAction extends AbstractAction {

	private final StandardDialog parent;
	private final JTextField teamAFieldA;
	private final JTextField teamAFieldB;
	private final JTextField teamBFieldA;
	private final JTextField teamBFieldB;

	public SetTcqResultsAction(StandardDialog parent, JTextField teamAFieldA, JTextField teamAFieldB,
			JTextField teamBFieldA, JTextField teamBFieldB) {
		super("Add Scores");
		this.parent = parent;
		this.teamAFieldA = teamAFieldA;
		this.teamAFieldB = teamAFieldB;
		this.teamBFieldA = teamBFieldA;
		this.teamBFieldB = teamBFieldB;
	}

	public void actionPerformed(ActionEvent ae) {
		Turn tcqA = new TcqTurn(true,
				new TcqScoreResult(Integer.parseInt(this.teamAFieldA.getText())),
				new TcqScoreResult(Integer.parseInt(this.teamBFieldA.getText())));
		EventBus.publish(new AddTurnEvent(tcqA));

		Turn tcqB = new TcqTurn(false,
				new TcqScoreResult(Integer.parseInt(this.teamAFieldB.getText())),
				new TcqScoreResult(Integer.parseInt(this.teamBFieldB.getText())));
		EventBus.publish(new AddTurnEvent(tcqB));

		this.parent.setVisible(false);
		this.parent.dispose();
	}
}
