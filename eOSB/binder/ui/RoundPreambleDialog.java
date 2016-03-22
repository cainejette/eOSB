package eOSB.binder.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.ui.actions.CancelButtonAction;
import eOSB.game.controller.Handler;
import eOSB.game.data.PathStore;

public class RoundPreambleDialog extends StandardDialog {
  private Handler handler;
  
  public RoundPreambleDialog(Handler handler) {
    this.handler = handler;
    this.init();
  }
  
  private void init() {
    this.pack();
    this.setTitle("Round Preamble");
    this.setLocationRelativeTo(this.handler.getFrame());
    this.setMinimumSize(this.getPreferredSize());
    this.setResizable(true);
  }
  
  @Override
  public JComponent createBannerPanel() {
  	JLabel message = new JLabel("Please read the following at the start of each round:");
		JLabel note = new JLabel("(Teams may collectively waive this right)");
	  
		message.setFont(new Font(message.getFont().getName(), Font.PLAIN, message.getFont().getSize() + 2));
		
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
		panel.setLayout(new MigLayout("wrap 1"));
		panel.add(message);
		panel.add(note);
		return panel;  
	}

  @Override
  public ButtonPanel createButtonPanel() {
    ButtonPanel panel = new ButtonPanel();
    panel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
    
    AbstractAction action = new AbstractAction() {
      public void actionPerformed(ActionEvent ae) {
        RoundPreambleDialog.this.setVisible(false);
        RoundPreambleDialog.this.dispose();
      }
    };
    
    JButton button = new JButton("OK");
    button.setAction(action);
    button.setText("OK");
    this.setDefaultAction(action);
    this.setDefaultCancelAction(new CancelButtonAction(this));
    
    panel.add(button);
    return panel;
  }

  @Override
  public JComponent createContentPanel() {
    String text = this.readText();
    JTextArea textArea = new JTextArea(text);
    textArea.setWrapStyleWord(true);
    textArea.setLineWrap(true);
    textArea.setEditable(false);
    textArea.setMargin(new Insets(5, 5, 5, 5));
    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(500, 350));
    
    final JPanel panel = new JPanel();
    panel.setLayout(new MigLayout("wrap 1, fill, insets 5 5 5 5"));
    panel.add(scrollPane, "grow, push");
    return panel;
  }
  
  private String readText() {
    BufferedReader reader = null;
    String text = "";
    String currentLine = "";
    String previousLine = "";

    reader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(
        PathStore.ROUND_PREAMBLE)));
    
    try {
      currentLine = reader.readLine();
      previousLine = currentLine;
      currentLine = reader.readLine();
      text += previousLine + "\n";
    }

    catch (IOException ioe) {
      ioe.printStackTrace();
    }

    while (currentLine != null) {
      text += currentLine + "\n";
      
      try {
        previousLine = currentLine;
        currentLine = reader.readLine();
      }

      catch (IOException exception) {
        exception.printStackTrace();
      }
    }
    
    return text;
  }
}
