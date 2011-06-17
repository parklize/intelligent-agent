package org.protege.editor.owl.swcl.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxOntologyFormat;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.swcl.model.Constraint;
import org.protege.editor.owl.swcl.model.Qualifier;
import org.protege.editor.owl.swcl.model.Variable;
import org.protege.editor.owl.swcl.utils.Utils;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.WriterDocumentTarget;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.OWLEntityRemover;

import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLNamedIndividualImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

public class ConstraintController {
	
	private SWCLOntologyController soh = null;
	private OWLOntologyManager manager = null;
	private OWLDataFactory dataFactory = null;
	private String base = null;
	private PrefixManager pm = null; 
	private OWLOntology ont = null;
	private OWLEntityRemover remover = null;
	
	public ConstraintController(OWLModelManager owlModelManager,SWCLOntologyController soh){
		
		this.soh = soh;
		this.manager = OWLManager.createOWLOntologyManager();
		this.dataFactory = manager.getOWLDataFactory();
		this.base = soh.getPrefix();
		this.pm = new DefaultPrefixManager(base);
		this.ont = owlModelManager.getActiveOntology();
		this.remover = new OWLEntityRemover(this.manager,Collections.singleton(this.ont));
	}
	
	// NEED UPDATE
	public void writeVariablesToOnt(ArrayList<Variable> variablesList) {
		
//Utils.printVariablesList("variablesList:", variablesList);
		
		String prefix = soh.getPrefix();
		
		// get the reference to the Variable class(create)
		OWLClass variable = dataFactory.getOWLClass("#Variable",pm);
		
		// save temporary
		WriterDocumentTarget wdt;
		FileWriter fw = null;

		try {
			
//System.out.println("Prefix:"+prefix);

			for(Variable v:variablesList){
System.out.println("v:"+v.getName());
//				OWLClass variableCls = dataFactory.getOWLClass("#"+v.getName(),pm);
				// create subclass axiom
//				OWLAxiom axiom = dataFactory.getOWLSubClassOfAxiom(variableCls, variable);
//				AddAxiom addAxiom = new AddAxiom(ont,axiom);
//				manager.applyChange(addAxiom);
				
				// get the reference to the y instance 
				OWLIndividual individual = dataFactory.getOWLNamedIndividual("#"+v.getName(),pm);
				
				// create class assertion that y is the instance of the Variable
				OWLClassAssertionAxiom classAssertion = dataFactory.getOWLClassAssertionAxiom(variable, individual);
				
				// add axiom to ontology
				AddAxiom addAxiomChange = new AddAxiom(ont,classAssertion);
				manager.applyChange(addAxiomChange);
				
				// ow is constitute of ViewSplitePane and JPanel
//				ViewSplitPane jp = (ViewSplitPane) ow.getComponent(0);
//		//System.out.println(jp.getComponentCount());// 2
//				ViewSplitPaneDivider vpd = (ViewSplitPaneDivider) jp.getComponent(0);//ViewSplitPaneDivider
//				ViewSplitPane vsp = (ViewSplitPane) jp.getComponent(1);//ViewSplitPane
//		//System.out.println(vsp.getComponentCount());// 2
//				ViewSplitPaneDivider vsd1 = (ViewSplitPaneDivider) vsp.getComponent(0);
//				JTabbedPane jtp = (JTabbedPane) vsp.getComponent(1);
//		//System.out.println(jtp.getComponentCount());// 9
//				OWLWorkspaceViewsTab ovt = (OWLWorkspaceViewsTab) jtp.getComponent(2);
//		//System.out.println(ovt.getComponentCount());// 1
//				ViewsPane vp = (ViewsPane) ovt.getComponent(0);
////				vp.setVisible(false);
////				vp.dispose();
//		//System.out.println(vp.getComponentCount());// 1
//				JPanel j = (JPanel) vp.getComponent(0);
//		//System.out.println(j.getComponentCount());// 5
//				JPanel j1 = (JPanel) j.getComponent(1);
//				// this is left workspace of class tab
//				JComponent j2 = (JComponent) j1.getComponent(0);
//				j2.repaint();
//				
////				j2.setVisible(false);
//				System.out.println(j2.getComponentCount());// 2
//				System.out.println(j2.getComponent(0).getClass());
//				System.out.println(j2.getComponent(1).getClass());
//				
//				View v1 = (View) j2.getComponent(0);
//System.out.println(v1.getViewName());// Class hierarchy
//				View v2 = (View) j2.getComponent(1);
//System.out.println(v2.getViewName());// Class hierarchy (inferred)
//
//				ViewComponent vc = v1.getViewComponent();
//				vc.repaint();
			}
			
			for(Variable v:variablesList){

				String[] str = v.getDescription().split(" ");
				// class
				for(int i=0;i<str.length;i++){
					// if s is property,individual or class, then add prefix
					for(String cls:soh.getClassList()){
						if(str[i].equals(cls)){
							str[i] = "<" + prefix + "#" + str[i] +">";
						}
					}
					for(String pro:soh.getObjectPropertyList()){
						if(str[i].equals(pro)){
							str[i] = "<" + prefix + "#" + str[i] +">";
						}
					}
					for(String ind:soh.getIndividualsList()){
						if(str[i].equals(ind)){
							str[i] = "<" + prefix + "#" + str[i] +">";
						}
					}
				}
				
				String newDes = "";
				for(String s:str){
					newDes = newDes + " " + s;
				}
				
				v.setDescription(newDes);
//System.out.println(newDes);
			}
			
			// temporary save at current directory as manchester owl syntax
			String presentDir = System.getProperty("user.dir");
			wdt = new WriterDocumentTarget(new FileWriter(presentDir + "//temp.owl"));
			manager.saveOntology(ont, new ManchesterOWLSyntaxOntologyFormat(), wdt);
			
			// get all variables and dump to ontology
			for(Variable v:variablesList){
//System.out.println("Description:"+v.getDescription());
				fw = (FileWriter) wdt.getWriter();
				char[] cs = ("Class: " + "<" + prefix + "#ClassFor" + v.getName() + ">\n\n" +
						"    EquivalentTo:\n"+
						"        "+v.getDescription()+"\n\n").toCharArray();
				for(char c: cs){
					fw.write(c);
				}
				fw.flush();
			}
			
			// read temp ontology to ont
			File file = new File(presentDir + "//temp.owl");
			OWLOntology tempOnt = manager.loadOntologyFromOntologyDocument(file);

			// read classes created from temp ontology to ont
			SWCLOntologyController tempSoh = new SWCLOntologyController(tempOnt);
			ArrayList<String> classList = tempSoh.getClassList();
			for(String str:classList){
				for(Variable v:variablesList){
					if(str.equals("ClassFor"+v.getName())){
//System.out.println("new class: "+str);
						// property
						OWLDataProperty bindingClass = dataFactory.getOWLDataProperty(IRI.create(this.base+"#bindingClass"));
						// binding class
						OWLClass owlClass = tempSoh.getOWLClass(str);
						// get axioms to ont
						Set axioms = tempOnt.getAxioms(owlClass);
						Iterator it = axioms.iterator();
						while(it.hasNext()){
//System.out.println("axioms: "+it.next().toString());
							manager.addAxiom(ont, (OWLAxiom) it.next());
							
						}
						OWLNamedIndividual owlInd = (OWLNamedIndividual) soh.getOWLIndividual(v.getName());
//System.out.println("owlInd:"+owlInd.toString());
						OWLDataPropertyAssertionAxiom assertion = dataFactory.getOWLDataPropertyAssertionAxiom(bindingClass, owlInd, str);
						
						manager.addAxiom(ont, assertion);
//						manager.saveOntology(ont,new SystemOutDocumentTarget());
					}
				}
			}
		} catch (IOException e) {e.printStackTrace();
		} catch (OWLOntologyStorageException e) {e.printStackTrace();
		} catch (OWLOntologyCreationException e) {e.printStackTrace();
		} finally {
			if(fw != null){
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		// set description with manchester syntax back
		for(Variable v:variablesList){
			
			String des = v.getDescription();
			des = des.replaceAll("<"+prefix+"#", "");
			des = des.replaceAll(">", "");
			v.setDescription(des);
		}
	}
	
	
	// add constraint part to ontology
	public void writeConstraintToOnt(Constraint con) {
		// add constraint axiom
		OWLClass conClass = dataFactory.getOWLClass("#Constraint",pm);
		OWLIndividual conInd = dataFactory.getOWLNamedIndividual("#"+con.getName(),pm);
		OWLClassAssertionAxiom classAssertion = dataFactory.getOWLClassAssertionAxiom(conClass, conInd);
		manager.addAxiom(ont, classAssertion);
		
		// add has qualifier axiom
		OWLObjectProperty hasQualifier = dataFactory.getOWLObjectProperty(IRI.create(base + "#hasQualifier"));
		ArrayList<Qualifier> qualifierList = con.getQualifiers();
		for(Qualifier q:qualifierList){
//System.out.println("qualifier is:"+q.getV().getName());
			OWLIndividual ind = soh.getOWLIndividual(q.getV().getName());
System.out.println("ind is:"+ind.toString());
			OWLObjectPropertyAssertionAxiom assertion = dataFactory.getOWLObjectPropertyAssertionAxiom(hasQualifier, conInd, ind);
			AddAxiom addAxiomChange = new AddAxiom(ont,assertion);
			manager.applyChange(addAxiomChange);
		}
		
		// add LHS axiom NEED UPDATE...
		//LHSTermBlock 가져가기
		for (int i=0; i<con.getLhs().getTermblocks().size();i++){
			
			OWLClass LhsTermBClass= dataFactory.getOWLClass("#LhsTermBlock",pm);
			OWLIndividual lhsTermBInd = dataFactory.getOWLNamedIndividual("#"+"lhsTermBlock"+(i+1),pm);
			OWLClassAssertionAxiom LhsTermBAssertion = dataFactory.getOWLClassAssertionAxiom(LhsTermBClass, lhsTermBInd);
			manager.addAxiom(ont, LhsTermBAssertion);
			
			OWLObjectProperty hasLhs= dataFactory.getOWLObjectProperty(IRI.create(base + "#hasLhs"));
			OWLClassExpression hasTermBlockAllLhs = dataFactory.getOWLObjectAllValuesFrom(hasLhs, LhsTermBClass);
			OWLSubClassOfAxiom ax = dataFactory.getOWLSubClassOfAxiom(conClass, hasTermBlockAllLhs);
			AddAxiom addAx = new AddAxiom(ont, ax);
			manager.applyChange(addAx);
			
			// constraint->hasLhs->lhsTermblockInd
			OWLObjectPropertyAssertionAxiom hasLhsAx = dataFactory.getOWLObjectPropertyAssertionAxiom(hasLhs, conInd, lhsTermBInd);
			AddAxiom hasLhsAddAx = new AddAxiom(ont,hasLhsAx);
			manager.applyChange(hasLhsAddAx);
			
			
			//sign 가져가기

			OWLDataProperty hasSign = dataFactory.getOWLDataProperty(IRI.create(base + "#hasSign"));
			OWLDataPropertyAssertionAxiom assertionSign = dataFactory.getOWLDataPropertyAssertionAxiom(hasSign, lhsTermBInd, con.getLhs().getTermblocks().get(i).getSign());
			AddAxiom SignAxiom = new AddAxiom(ont, assertionSign);
			manager.applyChange(SignAxiom);

			
			//aggregateOperation 가져가기  (AggreOper 있으면 parameter도 가져가요)
			if (con.getLhs().getTermblocks().get(i).getAggregateOppertor()!="not use"){	
				OWLDataProperty hasAggregateOperation = dataFactory.getOWLDataProperty(IRI.create(base + "#hasAggregateOperation"));
				OWLDataPropertyAssertionAxiom assertionAggreO = dataFactory.getOWLDataPropertyAssertionAxiom(hasAggregateOperation, lhsTermBInd, con.getLhs().getTermblocks().get(i).getAggregateOppertor());
				AddAxiom AggreOAxiom = new AddAxiom(ont, assertionAggreO);
				manager.applyChange(AggreOAxiom);

				
				for (int j=0; j<con.getLhs().getTermblocks().get(i).getParameters().size();j++){
					OWLObjectProperty hasParameters = dataFactory.getOWLObjectProperty(IRI.create(base + "#hasParameters"));
					OWLIndividual Pind = soh.getOWLIndividual(con.getLhs().getTermblocks().get(i).getParameters().get(j).getV().getName());
					OWLObjectPropertyAssertionAxiom assertionParameters = dataFactory.getOWLObjectPropertyAssertionAxiom(hasParameters, lhsTermBInd, Pind);
					AddAxiom ParametersAxiom = new AddAxiom(ont, assertionParameters);
					manager.applyChange(ParametersAxiom);

				}
			}
			
			// factor 가져가기
			for (int k=0; k<con.getLhs().getTermblocks().get(i).getFactors().size();k++){
				OWLClass lhsFactorClass= dataFactory.getOWLClass("#LhsFactor",pm);
				OWLIndividual lhsFactorInd = dataFactory.getOWLNamedIndividual("#"+"lhsFactor"+(k+1),pm);
				OWLClassAssertionAxiom LhsFactorAssertion = dataFactory.getOWLClassAssertionAxiom(lhsFactorClass, lhsFactorInd);
				manager.addAxiom(ont, LhsFactorAssertion);
				
				OWLObjectProperty hasFactor= dataFactory.getOWLObjectProperty(IRI.create(base + "#hasFactor"));
				OWLClassExpression hasFactorAllLhsTermB = dataFactory.getOWLObjectAllValuesFrom(hasFactor, lhsFactorClass);
				OWLSubClassOfAxiom axF = dataFactory.getOWLSubClassOfAxiom(LhsTermBClass, hasFactorAllLhsTermB);
				AddAxiom addAxF = new AddAxiom(ont, axF);
				manager.applyChange(addAxF);
				
				
				OWLObjectProperty hasVar = dataFactory.getOWLObjectProperty(IRI.create(base + "#hasVar"));
				OWLIndividual Varind = soh.getOWLIndividual(con.getLhs().getTermblocks().get(i).getFactors().get(k).getV().getName());
				OWLObjectPropertyAssertionAxiom assertionVar = dataFactory.getOWLObjectPropertyAssertionAxiom(hasVar, lhsFactorInd, Varind);
				AddAxiom VarAxiom = new AddAxiom(ont, assertionVar);
				manager.applyChange(VarAxiom);
				
				OWLDataProperty hasBindingDataProperty = dataFactory.getOWLDataProperty(IRI.create(base + "#hasBindingDataProperty"));
				OWLDataPropertyAssertionAxiom assertionBindingDataProperty = dataFactory.getOWLDataPropertyAssertionAxiom(hasBindingDataProperty, lhsFactorInd, con.getLhs().getTermblocks().get(i).getFactors().get(k).getOwlProperty());
				AddAxiom BindingDataPropertyAxiom = new AddAxiom(ont, assertionBindingDataProperty);
				manager.applyChange(BindingDataPropertyAxiom);
				
				// lhsTermBInd->hasFactor->lhsFactorInd
				OWLObjectPropertyAssertionAxiom facAx = dataFactory.getOWLObjectPropertyAssertionAxiom(hasFactor, lhsTermBInd, lhsFactorInd);
				AddAxiom addFaxAx = new AddAxiom(ont,facAx);
				manager.applyChange(addFaxAx);
			}
			
			
		}
		
		// add operator axiom
		OWLDataProperty hasOperator = dataFactory.getOWLDataProperty(IRI.create(base + "#hasOperator"));
		OWLDataPropertyAssertionAxiom assertion = dataFactory.getOWLDataPropertyAssertionAxiom(hasOperator, conInd, con.getOpp().getOpp());
		AddAxiom oppAxiom = new AddAxiom(ont, assertion);
		manager.applyChange(oppAxiom);
		
		// add RHS axiom NEED UPDATE...
		//RHSTermBlock 가져가기
		for (int i=0; i<con.getRhs().getTermblocks().size();i++){
			
			OWLClass RhsTermBClass= dataFactory.getOWLClass("#RhsTermBlock",pm);
			OWLIndividual rhsTermBInd = dataFactory.getOWLNamedIndividual("#"+"rhsTermBlock"+(i+1),pm);
			OWLClassAssertionAxiom RhsTermBAssertion = dataFactory.getOWLClassAssertionAxiom(RhsTermBClass, rhsTermBInd);
			manager.addAxiom(ont, RhsTermBAssertion);
			
			OWLObjectProperty hasRhs= dataFactory.getOWLObjectProperty(IRI.create(base + "#hasRhs"));
			OWLClassExpression hasTermBlockAllRhs = dataFactory.getOWLObjectAllValuesFrom(hasRhs, RhsTermBClass);
			OWLSubClassOfAxiom ax = dataFactory.getOWLSubClassOfAxiom(conClass, hasTermBlockAllRhs);
			AddAxiom addAx = new AddAxiom(ont, ax);
			manager.applyChange(addAx);
			
			// constraint->hasLhs->lhsTermblockInd
			OWLObjectPropertyAssertionAxiom hasRhsAx = dataFactory.getOWLObjectPropertyAssertionAxiom(hasRhs, conInd, rhsTermBInd);
			AddAxiom hasRhsAddAx = new AddAxiom(ont,hasRhsAx);
			manager.applyChange(hasRhsAddAx);
			
			//sign 가져가기
			OWLDataProperty hasSign = dataFactory.getOWLDataProperty(IRI.create(base + "#hasSign"));
			OWLDataPropertyAssertionAxiom assertionSign = dataFactory.getOWLDataPropertyAssertionAxiom(hasSign, rhsTermBInd, con.getRhs().getTermblocks().get(i).getSign());
			AddAxiom SignAxiom = new AddAxiom(ont, assertionSign);
			manager.applyChange(SignAxiom);

			
			//aggregateOperation 가져가기  (AggreOper 있으면 parameter도 가져가요)
			if (con.getRhs().getTermblocks().get(i).getAggregateOppertor()!="not use"){	
				OWLDataProperty hasAggregateOperation = dataFactory.getOWLDataProperty(IRI.create(base + "#hasAggregateOperation"));
				OWLDataPropertyAssertionAxiom assertionAggreO = dataFactory.getOWLDataPropertyAssertionAxiom(hasAggregateOperation, rhsTermBInd, con.getRhs().getTermblocks().get(i).getAggregateOppertor());
				AddAxiom AggreOAxiom = new AddAxiom(ont, assertionAggreO);
				manager.applyChange(AggreOAxiom);

				
				for (int j=0; j<con.getRhs().getTermblocks().get(i).getParameters().size();j++){
					OWLObjectProperty hasParameters = dataFactory.getOWLObjectProperty(IRI.create(base + "#hasParameters"));
					OWLIndividual Pind = soh.getOWLIndividual(con.getRhs().getTermblocks().get(i).getParameters().get(j).getV().getName());
					OWLObjectPropertyAssertionAxiom assertionParameters = dataFactory.getOWLObjectPropertyAssertionAxiom(hasParameters, rhsTermBInd, Pind);
					AddAxiom ParametersAxiom = new AddAxiom(ont, assertionParameters);
					manager.applyChange(ParametersAxiom);

				}
			}
			
			// factor 가져가기
			for (int k=0; k<con.getRhs().getTermblocks().get(i).getFactors().size();k++){
				
				OWLClass rhsFactorClass= dataFactory.getOWLClass("#RhsFactor",pm);
				OWLIndividual rhsFactorInd = dataFactory.getOWLNamedIndividual("#"+"rhsFactor"+(k+1),pm);
				OWLClassAssertionAxiom RhsFactorAssertion = dataFactory.getOWLClassAssertionAxiom(rhsFactorClass, rhsFactorInd);
				manager.addAxiom(ont, RhsFactorAssertion);
				
				OWLObjectProperty hasFactor= dataFactory.getOWLObjectProperty(IRI.create(base + "#hasFactor"));
				OWLClassExpression hasFactorAllRhsTermB = dataFactory.getOWLObjectAllValuesFrom(hasFactor, rhsFactorClass);
				OWLSubClassOfAxiom axF = dataFactory.getOWLSubClassOfAxiom(RhsTermBClass, hasFactorAllRhsTermB);
				AddAxiom addAxF = new AddAxiom(ont, axF);
				manager.applyChange(addAxF);
				
				
				OWLObjectProperty hasVar = dataFactory.getOWLObjectProperty(IRI.create(base + "#hasVar"));
				OWLIndividual Varind = soh.getOWLIndividual(con.getRhs().getTermblocks().get(i).getFactors().get(k).getV().getName());
				OWLObjectPropertyAssertionAxiom assertionVar = dataFactory.getOWLObjectPropertyAssertionAxiom(hasVar, rhsFactorInd, Varind);
				AddAxiom VarAxiom = new AddAxiom(ont, assertionVar);
				manager.applyChange(VarAxiom);
				
				OWLDataProperty hasBindingDataProperty = dataFactory.getOWLDataProperty(IRI.create(base + "#hasBindingDataProperty"));
				OWLDataPropertyAssertionAxiom assertionBindingDataProperty = dataFactory.getOWLDataPropertyAssertionAxiom(hasBindingDataProperty, rhsFactorInd, con.getRhs().getTermblocks().get(i).getFactors().get(k).getOwlProperty());
				AddAxiom BindingDataPropertyAxiom = new AddAxiom(ont, assertionBindingDataProperty);
				manager.applyChange(BindingDataPropertyAxiom);
			
				// lhsTermBInd->hasFactor->lhsFactorInd
				OWLObjectPropertyAssertionAxiom facAx = dataFactory.getOWLObjectPropertyAssertionAxiom(hasFactor, rhsTermBInd, rhsFactorInd);
				AddAxiom addFaxAx = new AddAxiom(ont,facAx);
				manager.applyChange(addFaxAx);
			}
			
		}
	}

	// delete delete selected constraint  NEED UPDATE....
	public void deleteConstraint(String conName) {
		
		OWLNamedIndividualImpl ind = new OWLNamedIndividualImpl(this.dataFactory, IRI.create(this.base+"#"+conName));
		HashMap indObjProperty = (HashMap) ind.getObjectPropertyValues(this.ont);
		
		// LHS part of removement
		OWLObjectPropertyImpl hasLhs = new OWLObjectPropertyImpl(this.dataFactory, IRI.create(this.base+"#hasLhs"));
		Set lhsInd = (Set) indObjProperty.get(hasLhs);
		if(lhsInd != null){
			Iterator lhsIndIt = lhsInd.iterator();
			while(lhsIndIt.hasNext()){
				OWLNamedIndividual lhs = (OWLNamedIndividual) lhsIndIt.next();
				HashMap lhsObjProperty = (HashMap) lhs.getObjectPropertyValues(this.ont);
				OWLObjectPropertyImpl hasFactor = new OWLObjectPropertyImpl(this.dataFactory, IRI.create(this.base+"#hasFactor"));
				Set lhsFactor = (Set) lhsObjProperty.get(hasFactor);
				if(lhsFactor != null){
					Iterator lhsFactorIt = lhsFactor.iterator();
					while(lhsFactorIt.hasNext()){
						OWLNamedIndividual factor = (OWLNamedIndividual) lhsFactorIt.next();
						// remove LHS factor
						factor.accept(remover);
					}
				}
				// remove LHS TermBlock
				lhs.accept(remover);
			}
		}
		
		// RHS part of removement
		OWLObjectPropertyImpl hasRhs = new OWLObjectPropertyImpl(this.dataFactory, IRI.create(this.base+"#hasRhs"));
		Set rhsInd = (Set) indObjProperty.get(hasRhs);
		if(rhsInd != null){
			Iterator rhsIndIt = rhsInd.iterator();
			while(rhsIndIt.hasNext()){
				OWLNamedIndividual rhs = (OWLNamedIndividual) rhsIndIt.next();
				HashMap rhsObjProperty = (HashMap) rhs.getObjectPropertyValues(this.ont);
				OWLObjectPropertyImpl hasFactor = new OWLObjectPropertyImpl(this.dataFactory, IRI.create(this.base+"#hasFactor"));
				Set rhsFactor = (Set) rhsObjProperty.get(hasFactor);
				if(rhsFactor != null){
					Iterator rhsFactorIt = rhsFactor.iterator();
					while(rhsFactorIt.hasNext()){
						OWLNamedIndividual factor = (OWLNamedIndividual) rhsFactorIt.next();
						// remove LHS factor
						factor.accept(remover);
					}
				}
				// remove LHS TermBlock
				rhs.accept(remover);
			}
		}
		
		// remove constraint individual
		ind.accept(remover);
		
		manager.applyChanges(remover.getChanges());
		
	}

	public ArrayList<Variable> getAllVariables() {
		
		ArrayList<Variable> variablesList = new ArrayList<Variable>();
		
		try {   
	    	Iterator it = null;
			
			// create Variable class
			OWLClassImpl variableCls = new OWLClassImpl(dataFactory,IRI.create(base + "#Variable"));
			
			// temporary save at current directory as manchester owl syntax
			String presentDir = System.getProperty("user.dir");
			WriterDocumentTarget wdt = new WriterDocumentTarget(new FileWriter(presentDir + "//temp1.owl"));
	
			OWLOntologyManager manager = ont.getOWLOntologyManager();
			
	//		manager.saveOntology(owl,new SystemOutDocumentTarget());
			manager.saveOntology(ont, new ManchesterOWLSyntaxOntologyFormat(), wdt);
			
			// read temp ontology to ont
			FileReader file = new FileReader(presentDir + "//temp1.owl");
			BufferedReader br = new BufferedReader(file);
	//		OWLOntologyManager mng = OWLManager.createOWLOntologyManager();
	//		OWLOntology tempOnt = mng.loadOntologyFromOntologyDocument(file);
	//		mng.saveOntology(tempOnt,new SystemOutDocumentTarget());
	
	
			String readLine;
	//System.out.println("indName:"+indName);
			// get all variables
			while((readLine = br.readLine()) != null){
	//System.out.println("readLine:"+readLine);	
				// get all variables from ontology
				Set variablesSet = variableCls.getIndividuals(ont);
				if(variablesSet != null){

					it = variablesSet.iterator();
					while(it.hasNext()){
						OWLIndividual ind = (OWLIndividual) it.next();
						String indName = soh.getIndividualName(ind);
		//System.out.println("indName:"+indName);
						Variable v = new Variable(indName,"");
		//System.out.println("Class: " + "<" + prefix + "#ClassFor"+indName + ">");
						if(readLine.contains("Class: " + "<" + base + "#ClassFor"+indName + ">")){
							readLine = br.readLine();
							readLine = br.readLine();
							readLine = br.readLine();
		//System.out.println("1:"+readLine);
							readLine = readLine.replaceAll("        ", "");
							readLine = readLine.replaceAll("<"+base+"#","");
							readLine = readLine.replaceAll(">","");
		//System.out.println("2:"+readLine);
							
							v.setDescription(readLine);
							variablesList.add(v);
						}
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
		return variablesList;
			
	}
	
	// delete all variables in the ontology
	public void deleteVariables(){
		
		OWLClass variable = new OWLClassImpl(this.dataFactory, IRI.create(base+"#Variable"));
		Set varSet = variable.getIndividuals(ont);
		if(varSet != null){
			Iterator it = varSet.iterator();
			while(it.hasNext()){
				OWLNamedIndividual var = (OWLNamedIndividual) it.next();
				var.accept(remover);
			}
		}
	}
}
