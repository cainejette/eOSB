package eOSB.binder.controller;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.ui.SelectTcqDialog;
import eOSB.game.controller.CancelAndShowAction;
import eOSB.game.controller.Handler;
import eOSB.game.data.IconFactory;
import eOSB.game.data.PathStore;
import net.miginfocom.swing.MigLayout;

public class TcqPreambleDialog extends StandardDialog {

	private Handler handler;
	private Binder binder;

	public TcqPreambleDialog(Handler handler, Binder binder) {
//		super(binder.getFrame(), true);
		this.handler = handler;
		this.binder = binder;
		this.init();
	}

	private void init() {
		this.pack();
		this.setTitle("TCQ Preamble");
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	@Override
	public JComponent createBannerPanel() {
		JLabel message = new JLabel("Please read the following at the start of the TCQs:");

//		message.setFont(new Font(message.getFont().getName(), Font.PLAIN, binder.getFontSize()));
		message.setFont(new Font(message.getFont().getName(), Font.PLAIN, message.getFont().getSize() + 2));

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
		panel.setLayout(new MigLayout());
		panel.add(message);
		return panel;
	}

	@Override
	public ButtonPanel createButtonPanel() {
		ButtonPanel panel = new ButtonPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
		panel.setLayout(new MigLayout("fill"));

		JButton cancelButton = new JButton();
		CancelAndShowAction cancelAction = new CancelAndShowAction(this);
		this.setDefaultCancelAction(cancelAction);		
		cancelButton.setAction(cancelAction);
		
		ImageIcon cancelIcon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource(IconFactory.INCORRECT));
		cancelButton.setIcon(cancelIcon);
		
		panel.add(cancelButton, "growx, sizegroupx 1, w 150:300:, h 65!");

		AbstractAction action = new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				new SelectTcqDialog(handler);

				TcqPreambleDialog.this.setVisible(false);
				TcqPreambleDialog.this.dispose();
			}
		};

		JButton okButton = new JButton();
		okButton.setAction(action);
		this.setDefaultAction(action);
		
		ImageIcon okIcon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource(IconFactory.NEXT));
		okButton.setIcon(okIcon);
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		panel.add(okButton, "growx, sizegroupx 1, w 150:300:, h 65!");
		return panel;
	}

	@Override
	public JComponent createContentPanel() {
		String text = this.readText();
		JTextArea textArea = new JTextArea(text);
		textArea.setWrapStyleWord(true);
		textArea.setFont(new Font(textArea.getFont().getName(), textArea.getFont().getStyle(), binder.getFontSize()));
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setMargin(new Insets(5, 5, 5, 5));
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(800, 300));
//		scrollPane.setMinimumSize(new Dimension(200, 200));

		final JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("wrap 1, fill, insets 5 5 5 5"));
		panel.add(scrollPane, "grow, push");
		return panel;
	}

	private String readText() {
		Scanner scanner = new Scanner(ClassLoader.getSystemClassLoader().getResourceAsStream(PathStore.TCQ_PREAMBLE)).useDelimiter("\\Z"); 
		return scanner.next();
	}
}
