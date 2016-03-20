package eOSB.binder.controller;

import javax.swing.JButton;

public class ButtonState {

  private JButton correctButton;
  private JButton incorrectButton;
  private JButton interruptButton;

  public ButtonState(JButton correctButton, JButton incorrectButton, JButton interruptButton) {
    this.correctButton = correctButton;
    this.incorrectButton = incorrectButton;
    this.interruptButton = interruptButton;
  }

  public boolean isCorrect() {
    return this.correctButton.isEnabled() && this.correctButton.isSelected();
  }

  public boolean isInterrupt() {
    return this.interruptButton.isEnabled() && this.interruptButton.isSelected() && this.isIncorrect();
  }

  public boolean isIncorrect() {
    return this.incorrectButton.isEnabled() && this.incorrectButton.isSelected();
  }
  
  public void setInterrupt(boolean interrupt) {
    if (this.interruptButton.isEnabled() ) {
      this.interruptButton.setSelected(interrupt);
    }
  }
  
  public void setIncorrect(boolean incorrect) {
    if (this.incorrectButton.isEnabled()) {
      this.incorrectButton.setSelected(incorrect);
    }
  }
  
  public void setCorrect(boolean correct) {
    if (this.correctButton.isEnabled()) {
      this.correctButton.setSelected(correct);
    }
  }
}
