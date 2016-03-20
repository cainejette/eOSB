package eOSB.game.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class RoundSelectionListRenderer extends JLabel implements
		ListCellRenderer {

	private List<String> openedRounds;
	
	public RoundSelectionListRenderer(List<String> openedRounds) {
		this.openedRounds = openedRounds;
	}
	
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean hasFocus) {

		if (value instanceof JLabel) {
			JLabel label = (JLabel) value;
			String name = label.getText();
			this.setText(name);
			
			Font font = list.getFont();
			if (this.openedRounds.contains(name)) {
				setFont(new Font(font.getName(), Font.ITALIC, font.getSize() + 1));
			}
			else {
				setFont(new Font(font.getName(), Font.BOLD, font.getSize() + 1));
			}
			
			setHorizontalTextPosition(SwingConstants.CENTER);
			setHorizontalAlignment(SwingConstants.CENTER);
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
				setEnabled(false);
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			setEnabled(list.isEnabled());
			setOpaque(true);
			setPreferredSize(new Dimension(200, 35));
			return this;
		}
		else {
			return this;
		}
	}

}
