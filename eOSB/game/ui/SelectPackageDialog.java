package eOSB.game.ui;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import net.miginfocom.swing.MigLayout;

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;

import eOSB.binder.ui.PackageSelectionListRenderer;
import eOSB.binder.ui.actions.CancelButtonAction;
import eOSB.game.actions.SetPackagesAction;
import eOSB.game.controller.Handler;
import eOSB.game.controller.PackageSelectionOptions;

public class SelectPackageDialog extends StandardDialog {

	private JFrame parent;
	private SetPackagesAction selectPackageAction;
	private JButton okButton;
	private JList list;
	private int packageSelection;

	public SelectPackageDialog(JFrame parent, int packageSelection) {
		System.out.println("[SelectPackageDialog] building dialog");
		this.parent = parent;
		this.packageSelection = packageSelection;
		this.init();
	}

	private void init() {
		this.pack();
		this.setTitle("Package Selection");

		this.setLocationRelativeTo(this.parent);
		this.setResizable(false);
		this.setPreferredSize(new Dimension(316, 200));
		this.setMinimumSize(new Dimension(316, 200));
	}

	public JComponent createBannerPanel() {
		JLabel message = new JLabel("Select additional software packages:");
	  message.setFont(new Font(message.getFont().getName(), Font.PLAIN, message.getFont().getSize() + 2));

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 6));
		panel.setLayout(new MigLayout("wrap 1"));
		panel.add(message);
		return panel;
	}

	public ButtonPanel createButtonPanel() {
		ButtonPanel panel = new ButtonPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

		this.selectPackageAction = new SetPackagesAction(this);
		CancelButtonAction cancelAction = new CancelButtonAction(this);

		this.okButton = new JButton();
		this.okButton.setAction(this.selectPackageAction);
		setDefaultAction(this.selectPackageAction);
		panel.add(this.okButton);

		JButton button = new JButton();
		button.setAction(cancelAction);
		setDefaultCancelAction(cancelAction);
		panel.add(button);

		return panel;
	}

	public JComponent createContentPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("insets 0, fill"));

		JLabel[] labels = new JLabel[4];
		labels[PackageSelectionOptions.BINDER] = new JLabel("None");
		labels[PackageSelectionOptions.BINDER_TIMEKEEPER] = new JLabel("Timekeeper");
		labels[PackageSelectionOptions.BINDER_SCOREKEEPER] = new JLabel("Scorekeeper");
		labels[PackageSelectionOptions.BINDER_TIMEKEEPER_SCOREKEEPER] = new JLabel(
				"Timekeeper + Scorekeeper");
		this.list = new JList(labels);

		this.list.setCellRenderer(new PackageSelectionListRenderer());
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.list.addListSelectionListener(new PackageSelectionListListener(
				this, this.okButton));
		this.list.addMouseListener(new DoubleClickPackageListListener(this, this.list));
		
		this.list.setSelectedIndex(this.packageSelection);

		panel.add(this.list, "grow, push, span");
		return panel;
	}
}