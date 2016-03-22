package eOSB.game.controller;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
import eOSB.game.data.PathStore;

public class DisplayEulaDialog extends StandardDialog {
  private Handler handler;

  public DisplayEulaDialog(Handler handler) {
    this.handler = handler;
    init();
  }

  private void init() {
    this.pack();
    this.setTitle("EULA");
    this.setLocationRelativeTo(this.handler.getFrame());
    this.setMinimumSize(this.getPreferredSize());
    this.setResizable(true);
  }

  public JComponent createBannerPanel() {
  	JLabel message = new JLabel("Users must accept the following licensing agreement:");
		JLabel note = new JLabel("(Accessible via Help > Terms of Agreement)");
	  
		message.setFont(new Font(message.getFont().getName(), Font.PLAIN, message.getFont().getSize() + 2));
		
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
		panel.setLayout(new MigLayout("wrap 1"));
		panel.add(message);
		panel.add(note);
		return panel;
  }

  public ButtonPanel createButtonPanel() {
    ButtonPanel panel = new ButtonPanel();
    panel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

    OpenSelectPackagesDialogAction okAction = new OpenSelectPackagesDialogAction(this);
    CancelButtonAction cancelAction = new CancelButtonAction(this);

    JButton button = new JButton();
    button.setAction(okAction);
    setDefaultAction(okAction);
    button.setText("Accept");
    panel.add(button);

    button = new JButton();
    button.setAction(cancelAction);
    setDefaultCancelAction(cancelAction);
    panel.add(button);

    return panel;
  }

  public JComponent createContentPanel() {
    JPanel panel = new JPanel();
    JTextArea textArea = new JTextArea();

    BufferedReader reader = new BufferedReader(new InputStreamReader(Handler.getResourceAsStream(PathStore.EULA_TXT)));

    String currentLine = null;
    String text = null;
    try
    {
      currentLine = reader.readLine();
      text = "  " + currentLine;
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    do
      try
      {
        currentLine = reader.readLine();
        if (currentLine != null)
          text = text + "\n  " + currentLine;
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    while (currentLine != null);

    textArea.setText(text);
    textArea.setWrapStyleWord(true);
    textArea.setLineWrap(true);
    textArea.setMargin(new Insets(5, 5, 5, 5));
    textArea.setEditable(false);

    panel.setLayout(new MigLayout("insets 5 5 5 5, fill"));

    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(500, 350));
    textArea.setCaretPosition(0);
    panel.add(scrollPane, "grow");
    return panel;
  }
}