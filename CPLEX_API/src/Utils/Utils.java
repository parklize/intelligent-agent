package Utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.TableColumn;

import model.Constraint;
import model.Factor;
import model.LHS;
import model.Objective;
import model.Operator;
import model.Parameter;
import model.Qualifier;
import model.RHS;
import model.TermBlock;
import model.Variable;

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
	
	// get Abstract Syntax from Constraint
	public static String getSWCLAbstractSyntax(ArrayList<Variable> variablesList,Constraint con){
		
		String var="";
		
		for(Variable v:variablesList){
			var += "Variable(" + v.getName()+"  "+v.getDescription()+");\n";
		}
		
		String str=var + "Constraint ";
		
		//Qualifier 
	
		if (con.getQualifiers().size()!=0){				//만약 qualifier사이즈가 0이 아니면 실행
			str+="( Qualifier (Variable (";
			
			for (int i=0; i<con.getQualifiers().size();i++){	// qualifier 사이즈만큼 
				str+=" "+ con.getQualifiers().get(i).getV().getName();
			}
			str+=" ) )";
		}
		
		
		//LHS
		
		str+=" LHS ";
		for (int i=0; i<con.getLhs().getTermblocks().size();i++){
			str+="( TermBlock ( "+ con.getLhs().getTermblocks().get(i).getSign();
			
			if (con.getLhs().getTermblocks().get(i).getAggregateOppertor()!="not use"){			//만약 aggregate가 not use가 아니면 실행
				str+=" "+ con.getLhs().getTermblocks().get(i).getAggregateOppertor()+" Parameter( Variable (";	

				for (int j=0; j<con.getLhs().getTermblocks().get(i).getParameters().size();j++){		//parameter 사이즈 만큼
					str+=	" "+ con.getLhs().getTermblocks().get(i).getParameters().get(j).getV().getName();
				}
				str+=" ) )";
			}
			
			str+=" Factor ( Variable(";
			
			for (int k=0; k<con.getLhs().getTermblocks().get(i).getFactors().size();k++){
				str+=" "+ con.getLhs().getTermblocks().get(i).getFactors().get(k).getV().getName()+" "+ con.getLhs().getTermblocks().get(i).getFactors().get(k).getOwlProperty();
			
			}
			str+=" ) ) ) ) ";
		}
		
		
		//Operator
		str+=con.getOpp().getOpp();
		
		//RHS
		str+=" RHS ";
		for (int i=0; i<con.getRhs().getTermblocks().size();i++){
			str+="( TermBlock ( "+ con.getRhs().getTermblocks().get(i).getSign();
			
			if (con.getRhs().getTermblocks().get(i).getAggregateOppertor()!="not use"){			//만약 aggregate가 not use가 아니면 실행
				str+=" "+ con.getRhs().getTermblocks().get(i).getAggregateOppertor()+" Parameter( Variable (";
				

				for (int j=0; j<con.getRhs().getTermblocks().get(i).getParameters().size();j++){		//parameter 사이즈 만큼
					str+=	" "+ con.getRhs().getTermblocks().get(i).getParameters().get(j).getV().getName();
				}
		
				str+=" ) )";
			}
			
			str+=" Factor ( Variable(";
			
			for (int k=0; k<con.getRhs().getTermblocks().get(i).getFactors().size();k++){
				str+=" "+ con.getRhs().getTermblocks().get(i).getFactors().get(k).getV().getName()+" "+ con.getRhs().getTermblocks().get(i).getFactors().get(k).getOwlProperty();
			
			}
			str+=" ) ) ) )";
			
		}

//		System.out.println(str);
	
		return str;
		
//		int rowCount = tableModel.getRowCount();// =no. of constraints 
//		for(int i=0;i<rowCount;i++){
//			
//			tableModel.setValueAt(str,i,2);
//		}
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
	
	// intersection of two sets
	public static Set intersectionSet(Set s1, Set s2){
		Set intersection = new HashSet();
		for(Object o:s1){
			if(s2.contains(o)){
				intersection.add(o);
			}
		}
		return intersection;
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
			if(tb.getParameters() != null){
				for(Parameter p:tb.getParameters()){
					System.out.println("name:"+p.getV().getName());
					System.out.println("description:"+p.getV().getDescription());
				}
			}
			System.out.println("factors:");
			for(Factor fac:tb.getFactors()){
				System.out.println("name:"+fac.getV().getName());
				System.out.println("descrption:"+fac.getV().getDescription());
				System.out.println("property:"+fac.getOwlProperty());
			}
		}
		
		System.out.println("===opp===");
		Operator op = con.getOpp();
		System.out.println(op.getOpp());
		
		System.out.println("===RHS===");
		RHS rhs = con.getRhs();
		for(TermBlock tb:rhs.getTermblocks()){
			System.out.println("sign:"+tb.getSign());
			System.out.println("agg:"+tb.getAggregateOppertor());
			System.out.println("parameters:");
			if(tb.getParameters() != null){
				for(Parameter p:tb.getParameters()){
					System.out.println("name:"+p.getV().getName());
					System.out.println("description:"+p.getV().getDescription());
				}
			}
			System.out.println("factors:");
			for(Factor fac:tb.getFactors()){
				System.out.println("name:"+fac.getV().getName());
				System.out.println("descrption:"+fac.getV().getDescription());
				System.out.println("property:"+fac.getOwlProperty());
			}
		}
		
	}
	
	public static void printObjective(Objective obj){
		
		System.out.println("Objective:====================");
		System.out.println("Instruction:"+obj.getOptimizationInstruction());
		ArrayList<TermBlock> tb = obj.getObjectiveTerm();
		for(TermBlock t: tb){
			System.out.println("==========================");
			System.out.println("Sign:"+t.getSign());
			System.out.println("AggOpp:"+t.getAggregateOppertor());
			System.out.println("==========Factors==========");
			ArrayList<Factor> fList = t.getFactors();
			for(Factor f:fList){
				System.out.println("Var Name:"+f.getV().getName());
				System.out.println("OWL Property:"+f.getOwlProperty());
			}
			System.out.println("==========Parameters==========");
			ArrayList<Parameter> pList =  t.getParameters();
			for(Parameter p:pList){
				System.out.println("p var name:"+p.getV().getName());
			}
		}
	}
}
