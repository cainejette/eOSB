package eOSB.binder.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class DefaultPackageSelectionListRenderer extends
		DefaultListCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean hasFocus) {

		if (value instanceof JLabel) {
			JLabel label = (JLabel) value;
			String name = label.getText();
			setText(name);
			
			label.setHorizontalTextPosition(SwingConstants.CENTER);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			if (isSelected) {
				label.setBackground(list.getSelectionBackground());
				label.setForeground(list.getSelectionForeground());
			} else {
				label.setBackground(Color.LIGHT_GRAY);
//				label.setForeground(Color.GRAY);
			}
			label.setFont(new Font(label.getFont().getName(), Font.BOLD, label.getFont().getSize()));

			label.setOpaque(true);
			label.setPreferredSize(new Dimension(200, 35));
			return label;
		} else {
			return null;
		}
	}
}
