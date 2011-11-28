package org.protege.editor.owl.swcl.utils;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
/**
 * @author parklize
 * @version 1.0, 2011-11-23
 */ 
public class CheckBoxRenderer implements TableCellRenderer {
 
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
       
    	if (value == null)
            return null;
        return (Component) value;
    }
}