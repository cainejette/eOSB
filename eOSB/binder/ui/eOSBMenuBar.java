package eOSB.binder.ui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import eOSB.binder.controller.Binder;
import eOSB.binder.controller.FireTcqPreambleEventAction;
import eOSB.binder.controller.OpenAboutEOSBAction;
import eOSB.binder.ui.actions.OpenConfirmExitDialogAction;
import eOSB.binder.ui.actions.OpenPdfAction;
import eOSB.binder.ui.actions.OpenSetScoresDialogAction;
import eOSB.binder.ui.actions.OpenSetTeamNamesDialogAction;
import eOSB.binder.ui.actions.OpenValidateUserDialogAction;
import eOSB.binder.ui.actions.SetFontSizeAction;
import eOSB.game.controller.Handler;
import eOSB.game.data.PathStore;

public class eOSBMenuBar extends JMenuBar {
	
	private Handler handler;
	private Binder binder;
	
	public eOSBMenuBar(Handler handler, Binder binder) {
		this.handler = handler;
		this.binder = binder;
		
		this.add(createFileMenu());
		this.add(createOptionsMenu());
		this.add(createHelpMenu());
	}
	
	private boolean isMac() {
		return System.getProperty("os.name").toLowerCase().indexOf("mac") > -1;
	}
	
	private JMenu createFileMenu() {		
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);

		JMenuItem menuItem = new JMenuItem();
		menuItem.setAction(new OpenValidateUserDialogAction(this.handler));
		
		if (isMac())
			menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.META_MASK));
		else 
			menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		
		menuItem.setMnemonic(KeyEvent.VK_R);
		menuItem.setText("Open Round");

		menu.add(menuItem);

		menuItem = new JMenuItem();
		menuItem.setAction(new FireTcqPreambleEventAction());

		menuItem.setText("Open TCQs");
		
		if (isMac())
			menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.META_MASK));
		else 
			menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
		
		menuItem.setMnemonic(KeyEvent.VK_T);
		menuItem.setEnabled(this.binder.shouldEnableTcqs());

		menu.add(menuItem);
		menu.addSeparator();

		menuItem = new JMenuItem();
		menuItem.setAction(new OpenConfirmExitDialogAction(this.handler));
		menuItem.setMnemonic(KeyEvent.VK_X);
		
		if (isMac())
			menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.META_MASK));
		else 
			menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		menu.add(menuItem);
		
		return menu;
	}
	
	private JMenu createOptionsMenu() {
		JMenu menu = new JMenu("Options");
		menu.setMnemonic(KeyEvent.VK_O);

		menu.add(this.createFontSizeMenu());
		menu.addSeparator();

		JMenuItem menuItem = new JMenuItem("Set Team Names");
		menuItem.setAction(new OpenSetTeamNamesDialogAction(this.handler));
		menuItem.setEnabled(this.handler.isUsingScoreboard());
		menuItem.setMnemonic(KeyEvent.VK_N);
		
		if (isMac()) 
			menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.META_MASK));
		else
			menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		
		menu.add(menuItem);

		menu.addSeparator();

		menuItem = new JMenuItem("Manual score override");
		menuItem.setAction(new OpenSetScoresDialogAction(this.handler));
		menuItem.setEnabled(this.handler.isUsingScoreboard());
		menu.add(menuItem);
		
		return menu;
	}
	
	private JMenu createFontSizeMenu() {
		JMenu menu = new JMenu("Font Size");
		menu.setMnemonic(KeyEvent.VK_S);
		ButtonGroup buttonGroup = new ButtonGroup();

		for (int size = 16; size < 36; size += 4) {
			JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(size + " point");
			menuItem.setAction(new SetFontSizeAction(this.binder, size));
			menuItem.setText(size + " point"); 
			buttonGroup.add(menuItem);
			
			if (this.binder.getFontSize() == size) {
				menuItem.setSelected(true);
			}

			menu.add(menuItem);
		}
		
		return menu;
	}
	
	private JMenu createHelpMenu() {
		JMenu menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_H);

		JMenuItem menuItem = new JMenuItem();
		menuItem.setAction(new OpenPdfAction(PathStore.COMPETITION_RULES));
		menuItem.setText("Competition Rules");
		menuItem.setMnemonic(KeyEvent.VK_C);
		menu.add(menuItem);
		menu.addSeparator();
		
		menuItem = new JMenuItem();
		menuItem.setAction(new OpenAboutEOSBAction(this.binder.getFrame()));
		menuItem.setText("About eOSB");
		menu.add(menuItem);
		
		return menu;
	}
}
