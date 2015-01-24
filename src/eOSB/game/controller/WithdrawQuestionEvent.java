package eOSB.game.controller;

import eOSB.binder.controller.QuestionEvent;

/**
 * Event fired to update subscribers that the most {@link Question} has been
 * withdrawn from the stack.
 * @author cjette
 * 
 */
public class WithdrawQuestionEvent implements QuestionEvent {

  public Object source;

  /**
   * @param source the source object firing this event
   */
  public WithdrawQuestionEvent(Object source) {
    this.source = source;
  }

  /** {@inheritDoc} */
//  @Override
  public Object getSource() {
    return this.source;
  }
}
