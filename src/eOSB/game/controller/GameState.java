package eOSB.game.controller;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JTextArea;

/**
 * Object encapsulating the state of the current {@link Round}
 * @author cjette
 *
 */
public class GameState {
	private boolean teamACorrect;
	private boolean teamAIncorrect;
	private boolean teamAInterrupt;
	private boolean teamBCorrect;
	private boolean teamBIncorrect;
	private boolean teamBInterrupt;
	private boolean backButton;
	private boolean submitButton;

	public GameState(boolean teamACorrect, boolean teamAIncorrect, boolean teamAInterrupt, boolean teamBCorrect, boolean teamBIncorrect, boolean teamBInterrupt, boolean backButton, boolean submitButton) {
		this.teamACorrect = teamACorrect;
		this.teamAIncorrect = teamAIncorrect;
		this.teamAInterrupt = teamAInterrupt;
		
		this.teamBCorrect = teamBCorrect;
		this.teamBIncorrect = teamBIncorrect;
		this.teamBInterrupt = teamBInterrupt;
		
		this.backButton = backButton;
		this.submitButton = submitButton;
	}
	
	public boolean isTeamACorrectEnabled() {
		return this.teamACorrect;
	}
	
	public boolean isTeamAIncorrectEnabled() {
		return this.teamAIncorrect;
	}
	
	public boolean isTeamAInterruptEnabled() {
		return this.teamAInterrupt;
	}
	
	public boolean isTeamBCorrectEnabled() {
		return this.teamBCorrect;
	}
	
	public boolean isTeamBIncorrectEnabled() {
		return this.teamBIncorrect;
	}
	
	public boolean isTeamBInterruptEnabled() {
		return this.teamBInterrupt;
	}
	
	public boolean isBackEnabled() {
		return this.backButton;
	}
	
	public boolean isSubmitEnabled() {
		return this.submitButton;
	}
}
