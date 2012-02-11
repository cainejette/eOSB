package eOSB.binder.ui.actions;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

import eOSB.binder.ui.SelectTcqDialog;
import eOSB.game.controller.Tcq;

/**
 * The action associated with opening a {@link Tcq}.
 * @author cjette
 *
 */
public class OpenTcqAction extends AbstractAction {

	private SelectTcqDialog parent;
	private Map<String, Tcq> map;
	private ButtonGroup buttonGroup;

	/**
	 * @param parent the parent dialog
	 * @param handler the {@link Handler}
	 * @param map the mapping of names to {@link Tcq}
	 * @param buttonGroup the ButtonGroup of buttons
	 */
	public OpenTcqAction(SelectTcqDialog parent,
			Map<String, Tcq> map, ButtonGroup buttonGroup) {
		super("Open");
		this.parent = parent;
		this.map = map;
		this.buttonGroup = buttonGroup;
	}

	/** {@inheritDoc} */
	public void actionPerformed(ActionEvent ae) {

		Tcq tcq = this.map.get(this.buttonGroup.getSelection()
				.getActionCommand());
		tcq.setPreviouslyOpened(true);
		this.parent.setSelected(tcq.getButton());
		URL tcqAsUrl = ClassLoader.getSystemClassLoader().getResource(tcq.getLocation());
		SwingController controller = new SwingController();
		SwingViewBuilder factory = new SwingViewBuilder(controller);
		JFrame viewerFrame = factory.buildViewerFrame();
		controller.openDocument(tcqAsUrl);
		controller.setCompleteToolBar(new JToolBar());
		viewerFrame.setTitle(tcq.getName());
		
		viewerFrame.pack();
		viewerFrame.setVisible(true);
	}
}
