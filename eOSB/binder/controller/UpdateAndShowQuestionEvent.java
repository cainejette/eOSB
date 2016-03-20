package eOSB.binder.controller;

import eOSB.game.controller.Question;

/**
 * Event fired to update subscribers of a new {@link Question}.
 * @author cjette
 *
 */
public class UpdateAndShowQuestionEvent implements QuestionEvent {

  private Object source;
  private Question question;
  private boolean enableTeamA;
  private boolean enableTeamB;

  /**
   * @param source the source firing this event
   * @param question the {@link Question} wrapped by this event
   * @param enableTeamA true if {@link Team} A should be enabled, false otherwise
   * @param enableTeamB true if Team B should be enabled, false otherwise
   */
  public UpdateAndShowQuestionEvent(Object source, Question question, boolean enableTeamA, boolean enableTeamB) {
    this.source = source;
    this.question = question;
    this.enableTeamA = enableTeamA;
    this.enableTeamB = enableTeamB;
  }

  /** {@inheritDoc} */
//  @Override
  public Object getSource() {
    return this.source;
  }

  /**
   * @return the {@link Question} wrapped by this event
   */
  public Question getQuestion() {
    return this.question;
  }
  
  /**
   * @return true if {@link Team} A should be enabled, false otherwise
   */
  public boolean shouldEnableTeamA() {
    return this.enableTeamA;
  }
  
  /**
   * @return true if {@link Team} B should be enabled, false otherwise
   */
  public boolean shouldEnableTeamB() {
    return this.enableTeamB;
  }
}
