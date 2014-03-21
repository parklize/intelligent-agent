package org.protege.editor.owl.swcl.utils;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;
/**
 * @author parklize
 * @version 1.0, 2011-11-23
 */
public class AbstractSyntaxRenderer extends JTextArea implements TableCellRenderer{

	public AbstractSyntaxRenderer() {
		setLineWrap(true);   
		setWrapStyleWord(true);   
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		// width estimate
		int maxPreferredHeight = 0;   
		for (int i = 0; i < table.getColumnCount(); i++) {   
			
			setText("" + table.getValueAt(row, i));   
			setSize(table.getColumnModel().getColumn(column).getWidth(), 0);   
			maxPreferredHeight = Math.max(maxPreferredHeight, getPreferredSize().height);  
			
		}   
		
		if (table.getRowHeight(row) != maxPreferredHeight){
			table.setRowHeight(row, maxPreferredHeight);   
		}
			  
		setText(value == null ? "" : value.toString());   
		return this;
	}
}
