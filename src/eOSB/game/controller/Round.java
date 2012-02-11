package eOSB.game.controller;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JRadioButton;

/**
 * Object encapsulating a round of eOSB.
 * @author cjette
 * 
 */
public class Round {
  private int currentQuestion = -1;
  private ArrayList<Question> questions = new ArrayList<Question>();
  private boolean previouslyOpened = false;
  private String fileLocation;
  private String roundName;
  private List<Tcq> tcqs;
  private List<Tcq> tcqSolutions;

  /**
   * @param roundName the name of this {@link Round}
   * @param fileLocation the location of this round
   * @param tcqs the list of {link Tcq}s for this round
   * @param tcqSolutions the list of tcq solutions
   */
  public Round(String roundName, String fileLocation, List<Tcq> tcqs, List<Tcq> tcqSolutions) {
    this.roundName = roundName;
    this.fileLocation = fileLocation;
    this.tcqs = tcqs;
    this.tcqSolutions = tcqSolutions;
  }

  /**
   * @return the list of {@link Question} comprising this {@link Round}
   */
  public List<Question> getQuestions() {
    return this.questions;
  }

  /**
   * Adds a {@link Question}.
   * @param question the question to add
   */
  public void addQuestion(Question question) {
    this.questions.add(question);
  }

  /**
   * Gets the next {@link Question} in the round, if it exists. Will return null
   * otherwise, so use {@link #hasNextQuestion()} to first check.
   * @return the next question in the round, or null.
   */
  public Question getNextQuestion() {
    this.currentQuestion = this.currentQuestion + 1;

    if (this.currentQuestion < this.questions.size()) {
      return this.questions.get(this.currentQuestion);
    }

    else {
      return null;
    }
  }

  /**
   * @return true if there is another {@link Question} in the {@link Round},
   * false otherwise.
   */
  public boolean hasNextQuestion() {
    return (this.currentQuestion + 1) < this.questions.size();
  }

  /**
   * Gets the previous {@link Question} in the {@link Round}, if it exists. Will
   * return null otherwise, so use {@link #hasPreviousQuestion()} to first
   * check.
   * @return the previous question in the round, or null.
   */
  public Question getPreviousQuestion() {
    this.currentQuestion = this.currentQuestion - 1;

    if (this.currentQuestion >= 0) {
      return this.questions.get(this.currentQuestion);
    }
    else {
      return null;
    }
  }

  /**
   * @return true if there is another {@link Question} in this {@link Round},
   * false otherwise.
   */
  public boolean hasPreviousQuestion() {
    return this.currentQuestion >= 0;
  }

  /**
   * @return the current {@link Question}
   */
  public Question getCurrentQuestion() {
    return this.questions.get(this.currentQuestion);
  }

  /**
   * Sets this {@link Round} as having been opened. This is used to help users
   * track which round they are probably on.
   */
  public void setOpened() {
    this.previouslyOpened = true;
  }

  /**
   * @return true if this round has already been opened, false otherwise.
   */
  public boolean wasOpened() {
    return this.previouslyOpened;
  }

  /**
   * @return the location of this {@link Round}
   */
  public String getLocation() {
    return this.fileLocation;
  }

  /**
   * @return the name of this {@link Round}
   */
  public String getName() {
    return this.roundName;
  }

  /**
   * @return a JRadioButton encapsulating this round's name, location, and
   * whether it's been opened.
   */
  public JRadioButton getButton() {
    JRadioButton button = new JRadioButton(this.roundName);
    button.setActionCommand(this.roundName);

    if (this.previouslyOpened) {
      button.setFont(new Font("Helvetica", Font.ITALIC, 28));
    }

    else {
      button.setFont(new Font("Helvetica", Font.BOLD, 28));
    }

    return button;
  }

  /**
   * @return a list of the {@link Tcq}s for this {@link Round}
   */
  public List<Tcq> getTcqs() {
    return this.tcqs;
  }
  
  /**
   * @return a list of the {@link Tcq} solutions for this {@link Round}
   */
  public List<Tcq> getTcqSolutions() {
    return this.tcqSolutions;
  }
  
  /**
   * Sets the current question number of this {@link Round}
   * @param questionNumber
   */
  public void setCurrentQuestion(int questionNumber) {
    this.currentQuestion = questionNumber;
  }
}
