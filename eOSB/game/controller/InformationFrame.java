package eOSB.game.controller;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventServiceEvent;
import org.bushe.swing.event.EventSubscriber;

import eOSB.binder.controller.TeamNameEvent;
import eOSB.game.data.IconFactory;
import eOSB.score.controller.TeamScoreNumberEvent;

public class InformationFrame extends JFrame implements EventSubscriber<EventServiceEvent> {

	private boolean shouldUseTimer;
	private boolean shouldUseScorekeeper;
	
	private final Font timeFont = new Font("Courier", Font.BOLD, 100);
	private boolean shouldDisplay = false;

	private JLabel teamALabel = new JLabel("Team A");
	private final Font font = new Font(teamALabel.getFont().getName(), Font.PLAIN, 150);
	private final Font scoreFont = new Font(teamALabel.getFont().getName(), Font.BOLD, 350);
	private JLabel teamBLabel = new JLabel("Team B");

	private JLabel teamAScore = new JLabel("0");
	private JLabel teamBScore = new JLabel("0");
	private JLabel teamBScore2 = new JLabel("0");

	private final Color teamAColor = new Color(235, 114, 24);
	private final Color teamBColor = new Color(150, 150, 255);

	private JLabel minutesLabel = new JLabel("6");
	private JLabel secondsLabel = new JLabel("00");
	private JLabel colonLabel = new JLabel(":");

	public InformationFrame(boolean shouldUseScoreboard, boolean shouldUseTimer) {
		this.shouldUseTimer = shouldUseTimer;
		this.shouldUseScorekeeper = shouldUseScoreboard;
		
		setLayout(new MigLayout("wrap 1, insets 0, fill"));

		if (shouldUseTimer && !shouldUseScorekeeper) {
			EventBus.subscribe(TimeUpdateEvent.class, this);

			add(createTimePanel(), "grow, sizegroupy 1");
			shouldDisplay = true;
		}
		
		if (shouldUseScoreboard) {
			EventBus.subscribe(TeamNameEvent.class, this);
			EventBus.subscribe(TeamScoreNumberEvent.class, this);

			add(createScorePanel(), "growx, sizegroup y1 ");
			shouldDisplay = true;
		}
		
		display();
	}

	private void display() {
		if (shouldDisplay) {
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			getContentPane().setBackground(Color.BLACK);
			getContentPane().setForeground(Color.BLACK);

			setTitle("eOSB");
			setIconImage(new ImageIcon(ClassLoader.getSystemClassLoader().getResource(IconFactory.LOGO)).getImage());
			pack();
			setMinimumSize(getPreferredSize());
			setLocationRelativeTo(null);
			setVisible(true);
		}
	}

	public void close() {
		setVisible(false);
		dispose();
	}

	private JPanel createScorePanel() {
		teamALabel.setForeground(teamAColor);
		teamAScore.setForeground(teamAColor);
		teamALabel.setFont(font);
		teamAScore.setFont(scoreFont);

		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("insets 10, fill"));
		panel.setBackground(Color.BLACK);
		
		teamBLabel.setForeground(teamBColor);
		teamBScore.setForeground(teamBColor);
		teamBLabel.setFont(font);
		teamBScore.setFont(scoreFont);
		teamBScore2.setFont(scoreFont);

		JPanel aPanel = new JPanel();
		aPanel.setBackground(Color.BLACK);
		aPanel.setLayout(new MigLayout("fill"));
		aPanel.add(teamALabel, "gapafter push");
		
		JPanel scorePanel = new JPanel();
		scorePanel.setLayout(new MigLayout("fill"));
		scorePanel.add(teamAScore, "west, sizegroupx 1, gapafter push");
		scorePanel.setBackground(Color.BLACK);
		
		if (shouldUseTimer) {
			EventBus.subscribe(TimeUpdateEvent.class, this);

			scorePanel.add(createTimePanel(), "align center");
		}
		
		teamBScore.setHorizontalAlignment(SwingConstants.TRAILING);
		scorePanel.add(teamBScore, "east, sizegroupx 1, gapbefore push, growx");
		
		JPanel bPanel = new JPanel();
		bPanel.setLayout(new MigLayout("fill"));
		bPanel.setBackground(Color.BLACK);
		bPanel.add(teamBLabel, "gapbefore push");
		
		panel.add(aPanel, "wrap, growx");
		panel.add(scorePanel, "wrap, growx");
		panel.add(bPanel, "span, growx");

		return panel;
	}
	
	private JPanel createTimePanel() {
		minutesLabel.setForeground(Color.WHITE);
		colonLabel.setForeground(Color.WHITE);
		secondsLabel.setForeground(Color.WHITE);

		minutesLabel.setFont(timeFont);
		colonLabel.setFont(timeFont);
		secondsLabel.setFont(timeFont);

		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("insets 10, fill"));
		panel.setBackground(Color.BLACK);

		panel.add(minutesLabel, "pushx, align right, gapx 20px 10px");
		panel.add(colonLabel, "align center, gapx 10px 10px");
		panel.add(secondsLabel, "pushx, align left, gapx 10px 20px");
		
		return panel;
	}

	public void onEvent(EventServiceEvent ese) {
		if (ese instanceof TeamNameEvent) {
			TeamNameEvent tne = (TeamNameEvent) ese;
			teamALabel.setText(tne.getTeamAName());
			teamBLabel.setText(tne.getTeamBName());
			
			pack();
		} else if (ese instanceof TeamScoreNumberEvent) {
			TeamScoreNumberEvent tsne = (TeamScoreNumberEvent) ese;
			teamAScore.setText(Integer.toString(tsne.getTeamAScore()));
			teamBScore.setText(Integer.toString((tsne.getTeamBScore())));
		} else if (ese instanceof RoundClockTimeUpdateEvent) {
			TimeUpdateEvent tue = (RoundClockTimeUpdateEvent) ese;
			minutesLabel.setText(addZeroes(String.valueOf(tue.getMinutes()), 1));
			secondsLabel.setText(addZeroes(String.valueOf(tue.getSeconds()), 2));
		}
	}

	private String addZeroes(String string, int size) {
		while (string.length() < size) {
			string = "0" + string;
		}
		return string;
	}
}
