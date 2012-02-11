package eOSB.binder.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JTextField;

/**
 * Listener to control the flow of tab key presses in the
 * {@link TcqResultsDialog}.
 * @author cjette
 * 
 */
public class TcqResultsTabListener implements KeyListener {

  private JTextField teamAFieldA;
  private JTextField teamAFieldB;
  private JTextField teamBFieldA;
  private JTextField teamBFieldB;
  private JButton okButton;
  private JButton cancelButton;

  /**
   * @param teamAFieldA teamA's tcqA field
   * @param teamAFieldB teamA's tcqB field
   * @param teamBFieldA teamB's tcqA field
   * @param teamBFieldB teamB's tcqB field
   * @param okButton the OK button
   * @param cancelButton the CANCEL button
   */
  public TcqResultsTabListener(JTextField teamAFieldA, JTextField teamAFieldB,
      JTextField teamBFieldA, JTextField teamBFieldB, JButton okButton, JButton cancelButton) {
    this.teamAFieldA = teamAFieldA;
    this.teamAFieldB = teamAFieldB;
    this.teamBFieldA = teamBFieldA;
    this.teamBFieldB = teamBFieldB;
    this.okButton = okButton;
    this.cancelButton = cancelButton;
  }

  /** {@inheritDoc} */
//  @Override
  public void keyPressed(KeyEvent ke) {
    if (ke.getKeyCode() == KeyEvent.VK_TAB) {
      if (ke.isShiftDown()) {
        if (this.teamAFieldA.hasFocus()) {
          this.cancelButton.requestFocus();
        }
        else if (this.teamAFieldB.hasFocus()) {
          this.teamBFieldA.requestFocus();
          this.teamBFieldA.selectAll();
        }
        else if (this.teamBFieldA.hasFocus()) {
          this.teamAFieldA.requestFocus();
          this.teamAFieldA.selectAll();
        }
        else if (this.teamBFieldB.hasFocus()) {
          this.teamAFieldB.requestFocus();
          this.teamAFieldB.selectAll();
        }
        else if (this.cancelButton.hasFocus()) {
          this.okButton.requestFocus();
        }
        else if (this.okButton.hasFocus()) {
          this.teamBFieldB.requestFocus();
          this.teamBFieldB.selectAll();
        }
      }
      else {
        if (this.teamAFieldA.hasFocus()) {
          this.teamBFieldA.requestFocus();
          this.teamBFieldA.selectAll();
        }
        else if (this.teamAFieldB.hasFocus()) {
          this.teamBFieldB.requestFocus();
          this.teamBFieldB.selectAll();
        }
        else if (this.teamBFieldA.hasFocus()) {
          this.teamAFieldB.requestFocus();
          this.teamAFieldB.selectAll();
        }
        else if (this.teamBFieldB.hasFocus()) {
          this.okButton.requestFocus();
        }
        else if (this.cancelButton.hasFocus()) {
          this.teamAFieldA.requestFocus();
          this.teamAFieldA.selectAll();
        }
        else if (this.okButton.hasFocus()) {
          this.cancelButton.requestFocus();
        }
      }
    }
  }

  /** {@inheritDoc} */
//  @Override
  public void keyReleased(KeyEvent arg0) {
  }

  /** {@inheritDoc} */
//  @Override
  public void keyTyped(KeyEvent arg0) {
  }
}
