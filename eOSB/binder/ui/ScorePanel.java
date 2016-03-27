package eOSB.binder.ui;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventServiceEvent;
import org.bushe.swing.event.EventSubscriber;

import eOSB.binder.controller.TeamNameEvent;
import eOSB.score.controller.TeamScoreNumberEvent;
import net.miginfocom.swing.MigLayout;

public class ScorePanel extends JPanel implements EventSubscriber<EventServiceEvent> {
	
	private String teamAName;
	private String teamBName;
	private int teamAScore;
	private int teamBScore;
	
	private JLabel teamALabel;
	private JLabel teamBLabel;

	public ScorePanel() {
		teamAName = "Team A";
		teamBName = "Team B";
		
		teamAScore = 0;
		teamBScore = 0;
		
		teamALabel = new JLabel();
		teamBLabel = new JLabel();
		
		EventBus.subscribe(TeamNameEvent.class, this);
		EventBus.subscribe(TeamScoreNumberEvent.class, this);
		
		updateBorders();
		
		setLayout(new MigLayout("fill"));
		add(teamALabel, "pushx, sizegroupx 1");
		add(teamBLabel, "pushx, sizegroupx 1");
	}
	
	private void updateBorders() {
		this.teamALabel.setText(teamAName + ": " + teamAScore);
		this.teamALabel.setFont(new Font("Helvetica", Font.BOLD, 20));
		this.teamBLabel.setText(teamBName + ": " + teamBScore);
		this.teamBLabel.setFont(new Font("Helvetica", Font.BOLD, 20));
	}

	@Override
	public void onEvent(EventServiceEvent e) {
		if (e instanceof TeamNameEvent) {
			TeamNameEvent tne = (TeamNameEvent) e;
			teamAName = tne.getTeamAName();
			teamBName = tne.getTeamBName();
			updateBorders();
		} else if (e instanceof TeamScoreNumberEvent) {
			TeamScoreNumberEvent tsne = (TeamScoreNumberEvent) e;
			teamAScore = tsne.getTeamAScore();
			teamBScore = tsne.getTeamBScore();
			updateBorders();
		}
	}
}
