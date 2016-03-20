package eOSB.game.controller;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import net.miginfocom.swing.MigLayout;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventServiceEvent;
import org.bushe.swing.event.EventSubscriber;

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;

public class ProgressBarDialog extends StandardDialog implements EventSubscriber<EventServiceEvent> {

	public ProgressBarDialog() {
		System.out.println("[ProgressBarDialog/constructor] opening progress bar");
		this.setUndecorated(true);
		EventBus.subscribe(RoundLoadedEvent.class, this);
	}
	
	@Override
	public JComponent createBannerPanel() {
		return null;
	}

	@Override
	public ButtonPanel createButtonPanel() {
		return null;
	}

	@Override
	public JComponent createContentPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("insets 0 ,fill, wrap 1"));
		panel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		
		JLabel label = new JLabel("Loading round...");
		panel.add(label, "align center");
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setString("Loading round...");
		progressBar.setStringPainted(false);
		progressBar.setIndeterminate(true);
		
		panel.add(progressBar, "align center");
		return panel;
	}

	public void onEvent(EventServiceEvent event) {
		if (event instanceof RoundLoadedEvent) {
			System.out.println("[ProgressBarDialog/onEvent] receiving round loaded event, closing progress bar");
			this.setVisible(false);
			this.dispose();
		}
	}
}

