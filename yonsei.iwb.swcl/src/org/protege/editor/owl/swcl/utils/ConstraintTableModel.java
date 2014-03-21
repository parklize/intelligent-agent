package org.protege.editor.owl.swcl.utils;

import javax.swing.table.DefaultTableModel;
/**
 * @author parklize
 * @version 1.0, 2011-11-23
 */
public class ConstraintTableModel extends DefaultTableModel {

	 public ConstraintTableModel(Object[][] data, String[] colHeads) {
		 super(data,colHeads);
	}

	public boolean isCellEditable(int row, int column) 
     { 
		 if(row != 0){
			 return false; 
		 }
		 	return true;
     } 

}
