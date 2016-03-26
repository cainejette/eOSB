package eOSB.game.controller;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventServiceEvent;
import org.bushe.swing.event.EventSubscriber;

import eOSB.binder.controller.TeamNameEvent;
import eOSB.game.data.IconFactory;
import eOSB.score.controller.TeamScoreNumberEvent;

public class InformationFrame extends JFrame implements EventSubscriber<EventServiceEvent> {

	private final Font font = new Font("Courier", Font.BOLD, 100);
	private boolean shouldDisplay = false;

	private JLabel teamALabel = new JLabel("Team A");
	private JLabel teamBLabel = new JLabel("Team B");

	private JLabel teamAScore = new JLabel("0");
	private JLabel teamBScore = new JLabel("0");

	private final Color teamAColor = new Color(235, 114, 24);
	private final Color teamBColor = new Color(150, 150, 255);

	private JLabel minutesLabel = new JLabel("06");
	private JLabel secondsLabel = new JLabel("00");
	private JLabel colonLabel = new JLabel(":");

	public InformationFrame(boolean shouldUseScoreboard, boolean shouldUseTimer) {
		this.setLayout(new MigLayout("wrap 1, insets 0, fill"));

		if (shouldUseScoreboard) {
			EventBus.subscribe(TeamNameEvent.class, this);
			EventBus.subscribe(TeamScoreNumberEvent.class, this);

			this.add(this.createScorePanel(), "growx, align center center");
			this.shouldDisplay = true;
		}

		if (shouldUseTimer) {
			EventBus.subscribe(TimeUpdateEvent.class, this);

			this.add(this.createTimePanel(), "align center center");
			this.shouldDisplay = true;
		}

		this.display();
	}

	private void display() {
		if (this.shouldDisplay) {
			this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			this.getContentPane().setBackground(Color.BLACK);
			this.getContentPane().setForeground(Color.BLACK);

			this.setTitle("eOSB");
			this.setIconImage(new ImageIcon(ClassLoader.getSystemClassLoader().getResource(IconFactory.LOGO)).getImage());
			this.pack();
			this.setMinimumSize(this.getPreferredSize());
			this.setLocationRelativeTo(null);
			this.setVisible(true);
		}
	}

	public void close() {
		this.setVisible(false);
		this.dispose();
	}

	private JPanel createScorePanel() {
		this.teamALabel.setForeground(this.teamAColor);
		this.teamAScore.setForeground(this.teamAColor);

		this.teamBLabel.setForeground(this.teamBColor);
		this.teamBScore.setForeground(this.teamBColor);

		this.teamALabel.setFont(this.font);
		this.teamAScore.setFont(this.font);
		this.teamBLabel.setFont(this.font);
		this.teamBScore.setFont(this.font);

		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("wrap 2, insets 10, fill"));
		panel.setBackground(Color.BLACK);

		panel.add(this.teamALabel, "align center center, gapx 30px 60px");
		panel.add(this.teamAScore, "align center center, gapx 0px 30px");
		panel.add(this.teamBLabel, "align center center, gapx 30px 60px");
		panel.add(this.teamBScore, "align center center, gapx 0px 30px");

		return panel;
	}

	private JPanel createTimePanel() {
		this.minutesLabel.setForeground(Color.WHITE);
		this.colonLabel.setForeground(Color.WHITE);
		this.secondsLabel.setForeground(Color.WHITE);

		this.minutesLabel.setFont(this.font);
		this.colonLabel.setFont(this.font);
		this.secondsLabel.setFont(this.font);

		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("insets 10, fill"));
		panel.setBackground(Color.BLACK);

		panel.add(this.minutesLabel, "align center center, gapx 20px 10px");
		panel.add(this.colonLabel, "align center center, gapx 10px 10px");
		panel.add(this.secondsLabel, "align center center, gapx 10px 20px");

		return panel;
	}

	public void onEvent(EventServiceEvent ese) {
		if (ese instanceof TeamNameEvent) {
			TeamNameEvent tne = (TeamNameEvent) ese;
			this.teamALabel.setText(tne.getTeamAName());
			this.teamBLabel.setText(tne.getTeamBName());
		} else if (ese instanceof TeamScoreNumberEvent) {
			TeamScoreNumberEvent tsne = (TeamScoreNumberEvent) ese;
			this.teamAScore.setText(Integer.toString(tsne.getTeamAScore()));
			this.teamBScore.setText(Integer.toString(tsne.getTeamBScore()));
		} else if (ese instanceof RoundClockTimeUpdateEvent) {
			TimeUpdateEvent tue = (RoundClockTimeUpdateEvent) ese;
			this.minutesLabel.setText(this.addZeroes(String.valueOf(tue.getMinutes()), 2));
			this.secondsLabel.setText(this.addZeroes(String.valueOf(tue.getSeconds()), 2));
		}

	}

	private String addZeroes(String string, int size) {
		while (string.length() < size) {
			string = "0" + string;
		}
		return string;
	}
}
