package eOSB.binder.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JTextField;

public class TcqResultsTabListener implements KeyListener {

	private JTextField teamAFieldA;
	private JTextField teamAFieldB;
	private JTextField teamBFieldA;
	private JTextField teamBFieldB;
	private JButton okButton;
	private JButton cancelButton;

	public TcqResultsTabListener(JTextField teamAFieldA, JTextField teamAFieldB, JTextField teamBFieldA,
			JTextField teamBFieldB, JButton okButton, JButton cancelButton) {
		this.teamAFieldA = teamAFieldA;
		this.teamAFieldB = teamAFieldB;
		this.teamBFieldA = teamBFieldA;
		this.teamBFieldB = teamBFieldB;
		this.okButton = okButton;
		this.cancelButton = cancelButton;
	}

	/** {@inheritDoc} */
	// @Override
	public void keyPressed(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_TAB) {
			if (ke.isShiftDown()) {
				if (this.teamAFieldA.hasFocus()) {
					this.cancelButton.requestFocus();
				} else if (this.teamAFieldB.hasFocus()) {
					this.teamBFieldA.requestFocus();
					this.teamBFieldA.selectAll();
				} else if (this.teamBFieldA.hasFocus()) {
					this.teamAFieldA.requestFocus();
					this.teamAFieldA.selectAll();
				} else if (this.teamBFieldB.hasFocus()) {
					this.teamAFieldB.requestFocus();
					this.teamAFieldB.selectAll();
				} else if (this.cancelButton.hasFocus()) {
					this.okButton.requestFocus();
				} else if (this.okButton.hasFocus()) {
					this.teamBFieldB.requestFocus();
					this.teamBFieldB.selectAll();
				}
			} else {
				if (this.teamAFieldA.hasFocus()) {
					this.teamBFieldA.requestFocus();
					this.teamBFieldA.selectAll();
				} else if (this.teamAFieldB.hasFocus()) {
					this.teamBFieldB.requestFocus();
					this.teamBFieldB.selectAll();
				} else if (this.teamBFieldA.hasFocus()) {
					this.teamAFieldB.requestFocus();
					this.teamAFieldB.selectAll();
				} else if (this.teamBFieldB.hasFocus()) {
					this.okButton.requestFocus();
				} else if (this.cancelButton.hasFocus()) {
					this.teamAFieldA.requestFocus();
					this.teamAFieldA.selectAll();
				} else if (this.okButton.hasFocus()) {
					this.cancelButton.requestFocus();
				}
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// no op
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// no op
	}
}
