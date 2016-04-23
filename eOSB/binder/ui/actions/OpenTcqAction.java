package eOSB.binder.ui.actions;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JToolBar;

import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

import com.jidesoft.dialog.StandardDialog;

import eOSB.game.controller.Tcq;

public class OpenTcqAction extends AbstractAction {

	private StandardDialog parent;
	private JList list;
	private List<Tcq> availableTcqs;
	private List<String> openedTcqs;
	private JButton button;
	private List<JFrame> frames;
	
	public OpenTcqAction(StandardDialog parent, JList list, List<Tcq> availableTcqs, List<String> openedTcqs, JButton button, List<JFrame> frames) {
		this.parent = parent;
		this.list = list;
		this.availableTcqs = availableTcqs;
		this.openedTcqs = openedTcqs;
		this.button = button;
		this.frames = frames;
	}

	public void actionPerformed(ActionEvent ae) {

		String path = null;
		String name = null;
		for (Tcq tcq : availableTcqs) {
			if (tcq.getName().equals(((JLabel)list.getSelectedValue()).getText())) {
				tcq.setPreviouslyOpened(true);
				path = tcq.getLocation();
				name = tcq.getName();
				break;
			}
		}
		
		openedTcqs.add(name);
		
		int lastOpened = -1;
		for (int i = 0; i < this.availableTcqs.size(); i++) {
			Tcq tcq = this.availableTcqs.get(i);
			if (tcq.wasPreviouslyOpened()) {
				lastOpened = i;
			}
		}

		if (lastOpened + 1 < this.availableTcqs.size()) {
			this.list.setSelectedIndex(lastOpened + 1);
			this.button.setEnabled(true);
			
		} else {
			this.list.setSelectedIndex(-1);
			this.button.setEnabled(false);
		}
		
		if (openedTcqs.size() == availableTcqs.size()) {
			list.setSelectedIndex(-1);
			button.setEnabled(false);
		}

		int selectedIndex = list.getSelectedIndex();
		boolean shouldEnable = list.getSelectedIndex() != -1;
		button.setEnabled(shouldEnable);
				
		URL tcqAsUrl = ClassLoader.getSystemClassLoader().getResource(path);
		System.out.println("opening TCQ: " + tcqAsUrl);
		SwingController controller = new SwingController();
		SwingViewBuilder factory = new SwingViewBuilder(controller);
		JFrame viewerFrame = factory.buildViewerFrame();
		controller.openDocument(tcqAsUrl);
		controller.setCompleteToolBar(new JToolBar());
		viewerFrame.setTitle(name);
		
		this.frames.add(viewerFrame);

		viewerFrame.pack();
		viewerFrame.setVisible(true);
	}
}
