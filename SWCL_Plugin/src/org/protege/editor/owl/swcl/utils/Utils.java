package org.protege.editor.owl.swcl.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.TableColumn;

import org.protege.editor.owl.swcl.model.Constraint;
import org.protege.editor.owl.swcl.model.Factor;
import org.protege.editor.owl.swcl.model.LHS;
import org.protege.editor.owl.swcl.model.Parameter;
import org.protege.editor.owl.swcl.model.Qualifier;
import org.protege.editor.owl.swcl.model.TermBlock;
import org.protege.editor.owl.swcl.model.Variable;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

public class Utils {
	
	public static void refreshComboBox(ArrayList<Variable> vList, TableColumn tc){
		JComboBox jb = new JComboBox();
		for(Variable v:vList){
			jb.addItem(v.getName());
		}
		tc.setCellEditor(new DefaultCellEditor(jb));
	}
	
	public static void refreshTotalArrayList(ArrayList<Variable> total, ArrayList<Variable> sub1){
		
			total.clear();// clear all elements
			if(sub1.size()!=0){
				for(Variable v:sub1){
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
	
	public static Variable findVariableWithName(ArrayList<Variable> vList,String name){
		for(Variable v:vList){
			if(name.equals(v.getName())){
				return v;
			}
		}
		return null;
	}
	
	// NEED TO UPTATE...
	public static void syntaxTest(OWLOntology ont){
		OWLOntologyManager manager = ont.getOWLOntologyManager();
		try {
			SystemOutDocumentTarget sdt = new SystemOutDocumentTarget();
			manager.saveOntology(ont,sdt);
			manager.saveOntology(ont,new FileOutputStream("D://test.txt"));

		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// test purpose
	public static void printVariablesList(String s,ArrayList<Variable> list){
		for(Variable v:list){
			System.out.println(s+v.getName());
			System.out.println("Description:"+v.getDescription());
		}
	}
	
	public static void printConstraint(Constraint con){
		System.out.println("===qualifiers===");
		for(Qualifier q:con.getQualifiers()){
			System.out.println("name:"+q.getV().getName());
			System.out.println("description:"+q.getV().getDescription());
		}
		System.out.println("===LHS===");
		LHS lhs = con.getLhs();
		for(TermBlock tb:lhs.getTermblocks()){
			System.out.println("sign:"+tb.getSign());
			System.out.println("agg:"+tb.getAggregateOppertor());
			System.out.println("parameters:");
			for(Parameter p:tb.getParameters()){
				System.out.println("name:"+p.getV().getName());
				System.out.println("description:"+p.getV().getDescription());
			}
			System.out.println("factors");
			for(Factor fac:tb.getFactors()){
				System.out.println("name:"+fac.getV().getName());
				System.out.println("descrption:"+fac.getV().getDescription());
			}
		}
	}
	
}
