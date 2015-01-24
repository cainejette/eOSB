package eOSB.game.controller;

import java.awt.Font;

import javax.swing.JRadioButton;

/**
 * Object representing a TCQ.
 * @author cjette
 * 
 */
public class Tcq {
  private String name;
  private String location;
  private boolean previouslyOpened;
  private JRadioButton button;

  public Tcq(String name, String location) {
    this.name = name;
    this.location = location;
		this.previouslyOpened = false;
		this.button = new JRadioButton(this.name);
    this.button.setActionCommand(this.name);
  }
  
  public Tcq(String name, String location, String category, String duration) {
  	this.name = name;
    this.location = location;
		this.previouslyOpened = false;
		this.button = new JRadioButton(this.name + " (" + duration + ")");
		this.button.setActionCommand(this.name);
  }

  /**
   * @return a JRadioButton encapsulating this {@link Tcq}'s information
   */
  public JRadioButton getButton() {
    if (this.previouslyOpened) {
      button.setFont(new Font("Helvetica", Font.ITALIC, 16));
    }
    else {
      button.setFont(new Font("Helvetica", Font.BOLD, 16));
    }

    return button;
  }

  /**
   * Sets whether this {@link Tcq} has been previously opened or not.
   * @param previousOpened the state to set the previouslyOpened state to.
   */
  public void setPreviouslyOpened(boolean previousOpened) {
    this.previouslyOpened = previousOpened;
    if (this.previouslyOpened) {
      button.setFont(new Font("Courier", Font.ITALIC, 12));
    }

    else {
      button.setFont(new Font("Courier", Font.BOLD, 14));
    }
  }

  /**
   * @return true if this {@link Tcq} has been previously opened, false
   * otherwise
   */
  public boolean wasPreviouslyOpened() {
    return this.previouslyOpened;
  }

  /**
   * @return the location of this {@link Tcq}
   */
  public String getLocation() {
    return this.location;
  }
  
  /**
   * @return the name of this {@link Tcq}
   */
  public String getName() {
    return this.name;
  }
}
