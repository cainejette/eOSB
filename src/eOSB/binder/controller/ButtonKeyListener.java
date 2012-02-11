package eOSB.binder.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;

public class ButtonKeyListener implements KeyListener {

  private JButton teamA_correct;
  private JButton teamA_incorrect;
  private JButton teamA_interrupt;

  private JButton teamB_correct;
  private JButton teamB_incorrect;
  private JButton teamB_interrupt;

  public ButtonKeyListener(JButton teamA_correct, JButton teamA_incorrect,
      JButton teamA_interrupt, JButton teamB_correct, JButton teamB_incorrect,
      JButton teamB_interrupt) {
    this.teamA_correct = teamA_correct;
    this.teamA_incorrect = teamA_incorrect;
    this.teamA_interrupt = teamA_interrupt;

    this.teamB_correct = teamB_correct;
    this.teamB_incorrect = teamB_incorrect;
    this.teamB_interrupt = teamB_interrupt;
  }

//  @Override
  public void keyPressed(KeyEvent e) {
    if (e.isControlDown()) {
      if (e.getKeyCode() == KeyEvent.VK_A) {
        if (this.teamA_correct.isEnabled()) {
          this.teamA_correct.doClick();
        }
      }
      else if (e.getKeyCode() == KeyEvent.VK_S) {
        if (this.teamA_incorrect.isEnabled()) {
          this.teamA_incorrect.doClick();
        }
      }
      else if (e.getKeyCode() == KeyEvent.VK_W){
        if (this.teamA_interrupt.isEnabled()) {
          this.teamA_interrupt.doClick();
        }
      }
    }
  }

//  @Override
  public void keyReleased(KeyEvent e) {
  }

//  @Override
  public void keyTyped(KeyEvent e) {
  }
}
