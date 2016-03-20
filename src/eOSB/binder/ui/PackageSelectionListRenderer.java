package eOSB.binder.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class PackageSelectionListRenderer extends
		DefaultPackageSelectionListRenderer implements ListCellRenderer {

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
				label.setBackground(list.getBackground());
				label.setForeground(list.getForeground());
			}

			label.setFont(new Font(label.getFont().getName(), Font.BOLD, label
					.getFont().getSize()));
			label.setEnabled(list.isEnabled());
			label.setOpaque(true);
			label.setPreferredSize(new Dimension(200, 35));
			return label;
		} 
		else {
			return null;
		}
	}
}
