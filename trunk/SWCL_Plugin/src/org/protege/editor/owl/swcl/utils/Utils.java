package org.protege.editor.owl.swcl.utils;

import java.util.ArrayList;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.TableColumn;

import org.protege.editor.owl.swcl.model.Variable;

public class Utils {
	
	public static void refreshComboBox(ArrayList<Variable> vList, TableColumn tc){
		JComboBox jb = new JComboBox();
		for(Variable v:vList){
			jb.addItem(v.getName());
		}
		tc.setCellEditor(new DefaultCellEditor(jb));
	}
	
	public static void refreshTotalArrayList(ArrayList<Variable> total, ArrayList<Variable> sub1, ArrayList<Variable> sub2){
		
			total.clear();// clear all elements
			if(sub1.size()!=0){
				for(Variable v:sub1){
					total.add(v);
				}
			}
			if(sub2.size()!=0){
				for(Variable v:sub2){
					total.add(v);
				}
			}
	}
	
	public static void addArrayList(ArrayList<Variable> list1, ArrayList<Variable> list2){
		for(Variable v:list2){
			list1.add(v);
		}
	}
	
	public static ArrayList<Variable> sumArrayList(ArrayList<Variable> list1,ArrayList<Variable> list2){
		ArrayList<Variable> sumArrayList = new ArrayList<Variable>();
		for(Variable v:list1){
			sumArrayList.add(v);
		}
		for(Variable v:list2){
			sumArrayList.add(v);
		}
		return sumArrayList;
	}
	
	// test purpose
	public static void printVariablesList(String s,ArrayList<Variable> list){
		for(Variable v:list){
			System.out.println(s+v.getName());
		}
	}
	
}
