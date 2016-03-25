package eOSB.game.ui;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import net.miginfocom.swing.MigLayout;

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.ui.actions.CancelButtonAction;
import eOSB.game.actions.AuthenticateUserAction;
import eOSB.game.controller.Handler;

@SuppressWarnings("serial")
public class ValidateUserDialog extends StandardDialog {
  private Handler handler;
  private JPasswordField passwordField;
  private AuthenticateUserAction validateUserAction;
  private JButton okButton = new JButton();
  
  public ValidateUserDialog(Handler handler) {
    this.handler = handler;
    this.passwordField = new JPasswordField(25);
    this.validateUserAction = new AuthenticateUserAction(this, 
    	      this.handler, this.passwordField);
    this.passwordField.addKeyListener(new PasswordKeyListener(this, this.passwordField, this.okButton));
    
    this.init();
  }

  private void init() {
    this.pack();
    this.setTitle("User Validation");

    this.setLocationRelativeTo(this.handler.getFrame());
    this.setVisible(true);
    this.setResizable(false);
    this.setMaximumSize(new Dimension(306, 200));
    this.setPreferredSize(new Dimension(306, 200));
    this.setMinimumSize(new Dimension(306, 200));
  }

  public JComponent createBannerPanel() {
    JLabel message = new JLabel("Enter passphrase:");
    message.setFont(new Font(message.getFont().getName(), Font.PLAIN, message.getFont().getSize() + 2));

    JPanel panel = new JPanel();
    panel.setLayout(new MigLayout("wrap 1"));
    panel.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
    panel.add(message);

    return panel;
  }

  public ButtonPanel createButtonPanel() {
    ButtonPanel panel = new ButtonPanel();
    panel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
    
    setDefaultAction(this.validateUserAction);

    CancelButtonAction cancelAction = new CancelButtonAction(this);
    setDefaultCancelAction(cancelAction);

    this.okButton.setAction(this.validateUserAction);
    this.okButton.setText("OK");
    panel.add(this.okButton);

    JButton button = new JButton();
    button.setAction(cancelAction);
    panel.add(button);

    return panel;
  }

  public JComponent createContentPanel() {
    JPanel panel = new JPanel();

    panel.setLayout(new MigLayout("fill, insets 0"));
    panel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
    panel.add(this.passwordField, "growx");

    return panel;
  }
}