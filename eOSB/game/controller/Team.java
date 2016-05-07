package eOSB.game.controller;

import eOSB.score.controller.ScoreResult;
import eOSB.score.controller.TcqScoreResult;

/**
 * The object encapsulating a team in eOSB.
 * @author Caine Jette
 * 
 */
public class Team {
  private String name;
  private ScoreResult tcqA = new TcqScoreResult(0);
  private ScoreResult tcqB = new TcqScoreResult(0);
  private int score = 0;

  /**
   * Constructor
   */
  public Team(String teamName) {
    this.name = teamName;
  }
  
  /**
   * Sets the name of this team.
   * @param name the name 
   */
  public void setName(String name) {
    this.name = name;
  }
  
  /**
   * @return the name of this team
   */
  public String getName() {
    return this.name;
  }

  /**
   * Sets the results of {@link Tcq} A
   * @param tcq the number of points awarded to this {@link Team} on the first
   * {@link Tcq}
   */
  public void setTcqA(ScoreResult tcq) {
    if (this.tcqA != null) {
      this.removeFromScore(this.tcqA);
    }
    this.tcqA = tcq;
    this.addToScore(tcq);
  }

  /**
   * @return the number of points awarded to this {@link Team} on the first
   * {@link Tcq}
   */
  public ScoreResult getTcqA() {
    return this.tcqA;
  }

  /**
   * Sets the results of {@link Tcq} B
   * @param tcq the number of points awarded to this {@link Team} on the second
   * {@link Tcq}
   */
  public void setTcqB(ScoreResult tcq) {
    if (this.tcqB != null) {
      this.removeFromScore(this.tcqB);
    }
    this.tcqB = tcq;
    this.addToScore(tcq);
  }

  /**
   * @return the number of points awarded to this {@link Team} on the second
   * {@link Tcq}
   */
  public ScoreResult getTcqB() {
    return this.tcqB;
  }

  /**
   * @return the number of points this {@link Team} currently has
   */
  public int getScore() {
    return this.score;
  }
  
  public void setScore(int score) {
	  this.score = score;
  }

  /**
   * Adds some number of points to the score of this {@link Team}, corresponding
   * to the correctness/incorrectness of their answer.
   * @param questionResult how many points their answer is worth
   */
  public void addToScore(ScoreResult questionResult) {
    this.score += questionResult.getWorth();
  }

  /**
   * Removes some number of points from the score of this {@link Team},
   * corresponding to the correctness/incorrectness of their answer.
   * @param questionResult how many points their answer is worth
   */
  public void removeFromScore(ScoreResult questionResult) {
    this.score -= questionResult.getWorth();
  }
}
