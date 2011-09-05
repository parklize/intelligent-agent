import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


import model.Constraint;
import model.Factor;
import model.LHS;
import model.Operator;
import model.Parameter;
import model.Qualifier;
import model.RHS;
import model.TermBlock;
import model.Variable;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import Utils.Utils;

import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLDataPropertyImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLNamedIndividualImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

import controller.ConstraintController;
import controller.SWCLOntologyController;


public class TestOWL {

	public static void main(String[] args) {

		try {
			
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			
			File file = new File("Ontology/ve2cadded.owl");
			
			// Load the local copy
			OWLOntology ve = manager.loadOntologyFromOntologyDocument(file);
			SWCLOntologyController soc = new SWCLOntologyController(ve);
			
			ConstraintController con = new ConstraintController(ve, soc);
			ArrayList<Variable> varList = con.getAllVariables();
			
//			Utils.printVariablesList("variable:", varList);
//			
//			for(Variable v:varList){
//				
//			}

//			System.out.println(soc.getPrefix());
			TestOWL towl = new TestOWL();
			ArrayList<Constraint> consList = towl.getAllConstraints(ve);
			
			for(Constraint c:consList){
				
				// parse every constraint
				Utils.printConstraint(c);
			
			}
			
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// get all constraints from ontology
	private ArrayList<Constraint> getAllConstraints(OWLOntology owl) {
		
		ArrayList<Constraint> constraintsList = new ArrayList<Constraint>();
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		SWCLOntologyController soc = new SWCLOntologyController(owl);

		
    	// controller
    	ConstraintController controller = new ConstraintController(owl, soc);
    	// get all variables to variablesList
    	ArrayList<Variable> variablesList = controller.getAllVariables();
		String prefix = soc.getPrefix();

		
		// get all constraints
		OWLClassImpl constraintCls = new OWLClassImpl(factory,IRI.create(prefix + "#Constraint"));
		// get individuals of constraint class
		Set constraintSet = constraintCls.getIndividuals(owl);
		Iterator it = constraintSet.iterator();
		
		while(it.hasNext()){
			
			Constraint con = new Constraint();
			OWLIndividual ind = (OWLIndividual) it.next();
			HashMap indDataProperty = (HashMap) ind.getDataPropertyValues(owl);
			HashMap indObjProperty = (HashMap) ind.getObjectPropertyValues(owl);
			
			// get name to constraint
			String indStr = ind.toString();
			indStr = indStr.replaceAll("<"+prefix+"#", "");
			indStr = indStr.replaceAll(">", "");
			con.setName(indStr);
			
			// get qualifier to constriant
			OWLObjectPropertyImpl hasQualifier = new OWLObjectPropertyImpl(factory,IRI.create(prefix+"#hasQualifier"));
			Set qua = (Set) indObjProperty.get(hasQualifier);
			
			if(qua != null){
				
				Iterator ito = qua.iterator();
				
				String quaStr = null;
				ArrayList<Qualifier> quaList = new ArrayList<Qualifier>();
				while(ito.hasNext()){
					OWLNamedIndividualImpl qualifier = (OWLNamedIndividualImpl) ito.next();
					quaStr = qualifier.toString();
					quaStr = quaStr.replaceAll("<"+prefix+"#", "");
					quaStr = quaStr.replaceAll(">", "");
	//System.out.println("quaStr is:"+quaStr);
					Variable v = Utils.findVariableWithName(variablesList, quaStr);
					Qualifier q = new Qualifier(v);
					quaList.add(q);	
				}
				con.setQualifiers(quaList);
			}
			// MEED UPDATE...
			// get LHS to constraint
			OWLObjectPropertyImpl hasLhs = new OWLObjectPropertyImpl(factory, IRI.create(prefix+"#hasLhs"));
			Set lhsSet = (Set) indObjProperty.get(hasLhs);
			if(lhsSet != null){
				Iterator itLhs = lhsSet.iterator();
				ArrayList<TermBlock> ltbList = new ArrayList<TermBlock>();
				
				String lhsStr = null;
				while(itLhs.hasNext()){
					
					TermBlock tb = new TermBlock();
					
					// get lhs termblock individual
					OWLNamedIndividualImpl lhsInd = (OWLNamedIndividualImpl) itLhs.next();
					HashMap lhsDataProperty = (HashMap) lhsInd.getDataPropertyValues(owl);
					HashMap lhsObjectProperty = (HashMap) lhsInd.getObjectPropertyValues(owl);
					
					// get hasSign value
					OWLDataPropertyImpl hasSign = new OWLDataPropertyImpl(factory,IRI.create(prefix+"#hasSign"));
					Set si = (Set) lhsDataProperty.get(hasSign);
					Iterator signIt = si.iterator();
					
					String signStr = null;
					while(signIt.hasNext()){
						
						OWLLiteralImpl sign = (OWLLiteralImpl) signIt.next();
						signStr = sign.getLiteral();
						signStr = signStr.replaceAll("\"", "");
						signStr = signStr.replaceAll("\\^\\^xsd:string", "");
	//System.out.println("LHS signStr is:"+signStr);
						tb.setSign(signStr);
					}
					
					// get hasAggregateOperation value
					OWLDataPropertyImpl hasAgg = new OWLDataPropertyImpl(factory, IRI.create(prefix+"#hasAggregateOperation"));
					Set aggSet = (Set) lhsDataProperty.get(hasAgg);
					if(aggSet != null){
						Iterator aggIt = aggSet.iterator();
						
						String aggStr = null;
						while(aggIt.hasNext()){
							OWLLiteralImpl agg = (OWLLiteralImpl) aggIt.next();
							aggStr = agg.toString();
							aggStr = aggStr.replaceAll("\"", "");
							aggStr = aggStr.replaceAll("\\^\\^xsd:string", "");
	//System.out.println("LHS aggStr is:"+aggStr);
							tb.setAggregateOppertor(aggStr);
						}
					}else{
						tb.setAggregateOppertor("not use");
					}
					
					// get parameter value
					OWLObjectPropertyImpl hasPar = new OWLObjectPropertyImpl(factory, IRI.create(prefix+"#hasParameters"));
					Set parSet = (Set) lhsObjectProperty.get(hasPar);
					
					if(parSet != null){
						
						// parameters list
						ArrayList<Parameter> parList = null;
						Iterator parIt = parSet.iterator();
						
						String parStr = null;
						while(parIt.hasNext()){
							
							parList = new ArrayList<Parameter>();
							Parameter p = new Parameter();
							
							OWLNamedIndividualImpl parameter = (OWLNamedIndividualImpl) parIt.next();
							parStr = parameter.toString();
							parStr = parStr.replaceAll("<"+prefix+"#", "");
							parStr = parStr.replaceAll(">", "");
	//System.out.println("LHS parameter is:" + parStr);
							Variable v = Utils.findVariableWithName(variablesList, parStr);
							p.setV(v);
							parList.add(p);
						}
						
						tb.setParameters(parList);
					}
					
					// get factors
					OWLObjectPropertyImpl hasFac = new OWLObjectPropertyImpl(factory,IRI.create(prefix+"#hasFactor"));
					Set facSet = (Set) lhsObjectProperty.get(hasFac);
					
					if(facSet != null){
						
						Iterator facIt = facSet.iterator();
						// factors list
						ArrayList<Factor> facList = new ArrayList<Factor>();
						
						//factor name
						String facStr = null;
						while(facIt.hasNext()){
							
							Factor f = new Factor();
							OWLNamedIndividualImpl factor = (OWLNamedIndividualImpl) facIt.next();//rhsFactor
							HashMap factorDataProperty = (HashMap) factor.getDataPropertyValues(owl);
							HashMap factorObjProperty = (HashMap) factor.getObjectPropertyValues(owl);
							
							// has Binding Data property
							OWLDataPropertyImpl hasBindingDataProperty = new OWLDataPropertyImpl(factory, IRI.create(prefix+"#hasBindingDataProperty"));
							Set hasBDPSet = (Set) factorDataProperty.get(hasBindingDataProperty);
							
							if(hasBDPSet != null){
								
								Iterator hasBDPSetIt = hasBDPSet.iterator();
								
								String hasBDPStr = null;
								while(hasBDPSetIt.hasNext()){
									OWLLiteral hasBDP = (OWLLiteralImpl)hasBDPSetIt.next();
									hasBDPStr = hasBDP.toString();
									hasBDPStr = hasBDPStr.replaceAll("\"", "");
									hasBDPStr = hasBDPStr.replaceAll("\\^\\^xsd:string", "");
	//System.out.println("RHS hasBindingDataProperty:"+hasBDPStr);
									f.setOwlProperty(hasBDPStr);
								}
							}
							
							// has Variable
							OWLObjectPropertyImpl hasVar = new OWLObjectPropertyImpl(factory, IRI.create(prefix+"#hasVar"));
							Set hasVarSet = (Set) factorObjProperty.get(hasVar);
							
							if(hasVarSet != null){
								
								Iterator hasVarSetIt = hasVarSet.iterator();
								
								String hasVarStr = null;
								while(hasVarSetIt.hasNext()){
									OWLNamedIndividualImpl var = (OWLNamedIndividualImpl) hasVarSetIt.next();
									hasVarStr = var.toString();
									hasVarStr = hasVarStr.replaceAll("<"+prefix+"#", "");
									hasVarStr = hasVarStr.replaceAll(">", "");
	//System.out.println("LHS var is:" + hasVarStr);
									Variable v = Utils.findVariableWithName(variablesList, hasVarStr);
									f.setV(v);
								}
							}
							
							facList.add(f);
						}
						tb.setFactors(facList);
					}
					
					// add to tb list
					ltbList.add(tb);
					
					// add to constraint
					LHS lhs = new LHS();
					lhs.setTermblocks(ltbList);
					con.setLhs(lhs);
					
				}
			}
			
			// get operator to constraint
			OWLDataPropertyImpl hasOperator = new OWLDataPropertyImpl(factory, IRI.create(prefix+"#hasOperator"));
			Set op = (Set) indDataProperty.get(hasOperator);
			Iterator opIt = op.iterator();

			String operatorStr = null;
			while(opIt.hasNext()){
				OWLLiteralImpl operator = (OWLLiteralImpl) opIt.next();
				operatorStr= operator.getLiteral();
				operatorStr = operatorStr.replaceAll("\"", "");
				operatorStr = operatorStr.replaceAll("\\^\\^xsd:string", "");
			}
			Operator operatorCon = new Operator(operatorStr);
			con.setOpp(operatorCon);
			
			// NEED UPDATE...
			// get RHS to constraint
			OWLObjectPropertyImpl hasRhs = new OWLObjectPropertyImpl(factory, IRI.create(prefix+"#hasRhs"));
			Set rhsSet = (Set) indObjProperty.get(hasRhs);
			if(rhsSet != null){
				Iterator itRhs = rhsSet.iterator();
				ArrayList<TermBlock> rtbList = new ArrayList<TermBlock>();
				
				String rhsStr = null;
				while(itRhs.hasNext()){
					
					TermBlock tb = new TermBlock();
					// get rhs termblock individual
					OWLNamedIndividualImpl rhsInd = (OWLNamedIndividualImpl) itRhs.next();
					HashMap rhsDataProperty = (HashMap) rhsInd.getDataPropertyValues(owl);
					HashMap rhsObjectProperty = (HashMap) rhsInd.getObjectPropertyValues(owl);
					
					// get hasSign value
					OWLDataPropertyImpl hasSign = new OWLDataPropertyImpl(factory,IRI.create(prefix+"#hasSign"));
					Set si = (Set) rhsDataProperty.get(hasSign);
					Iterator signIt = si.iterator();
					
					String signStr = null;
					while(signIt.hasNext()){
						OWLLiteralImpl sign = (OWLLiteralImpl) signIt.next();
						signStr = sign.getLiteral();
						signStr = signStr.replaceAll("\"", "");
						signStr = signStr.replaceAll("\\^\\^xsd:string", "");
	//System.out.println("RHS singStr is:"+signStr);
						tb.setSign(signStr);
					}
					
					// get hasAggregateOperation value
					OWLDataPropertyImpl hasAgg = new OWLDataPropertyImpl(factory, IRI.create(prefix+"#hasAggregateOperation"));
					Set aggSet = (Set) rhsDataProperty.get(hasAgg);
					if(aggSet != null){
						Iterator aggIt = aggSet.iterator();
						
						String aggStr = null;
						while(aggIt.hasNext()){
							OWLLiteralImpl agg = (OWLLiteralImpl) aggIt.next();
							aggStr = agg.toString();
							aggStr = aggStr.replaceAll("\"", "");
							aggStr = aggStr.replaceAll("\\^\\^xsd:string", "");
	//System.out.println("RHS aggStr is:"+aggStr);
							tb.setAggregateOppertor(aggStr);
						}
					}else{
						tb.setAggregateOppertor("not use");
					}
					
					// get parameter value
					OWLObjectPropertyImpl hasPar = new OWLObjectPropertyImpl(factory, IRI.create(prefix+"#hasParameters"));
					Set parSet = (Set) rhsObjectProperty.get(hasPar);
					
					if(parSet != null){
						
						Iterator parIt = parSet.iterator();
						// parameters list
						ArrayList<Parameter> parList = null;
						
						// parameter name
						String parStr = null;
						while(parIt.hasNext()){
							
							parList = new ArrayList<Parameter>();
							Parameter p = new Parameter();
							
							OWLNamedIndividualImpl parameter = (OWLNamedIndividualImpl) parIt.next();
							parStr = parameter.toString();
							parStr = parStr.replaceAll("<"+prefix+"#", "");
							parStr = parStr.replaceAll(">", "");
	//System.out.println("RHS parameter is:" + parStr);
							Variable v = Utils.findVariableWithName(variablesList, parStr);
							p.setV(v);
							parList.add(p);
						}
						tb.setParameters(parList);
					}
					
					// get factors
					OWLObjectPropertyImpl hasFac = new OWLObjectPropertyImpl(factory,IRI.create(prefix+"#hasFactor"));
					Set facSet = (Set) rhsObjectProperty.get(hasFac);
					
					if(facSet != null){
						
						Iterator facIt = facSet.iterator();
						// factors list
						ArrayList<Factor> facList = new ArrayList<Factor>();
						
						//factor name
						String facStr = null;
						while(facIt.hasNext()){
							
							Factor f = new Factor();
							OWLNamedIndividualImpl factor = (OWLNamedIndividualImpl) facIt.next();//rhsFactor
							HashMap factorDataProperty = (HashMap) factor.getDataPropertyValues(owl);
							HashMap factorObjProperty = (HashMap) factor.getObjectPropertyValues(owl);
							
							// has Binding Data property
							OWLDataPropertyImpl hasBindingDataProperty = new OWLDataPropertyImpl(factory, IRI.create(prefix+"#hasBindingDataProperty"));
							Set hasBDPSet = (Set) factorDataProperty.get(hasBindingDataProperty);
							
							if(hasBDPSet != null){
								
								Iterator hasBDPSetIt = hasBDPSet.iterator();
								
								String hasBDPStr = null;
								while(hasBDPSetIt.hasNext()){
									OWLLiteral hasBDP = (OWLLiteralImpl)hasBDPSetIt.next();
									hasBDPStr = hasBDP.toString();
									hasBDPStr = hasBDPStr.replaceAll("\"", "");
									hasBDPStr = hasBDPStr.replaceAll("\\^\\^xsd:string", "");
	//System.out.println("RHS hasBindingDataProperty:"+hasBDPStr);
									f.setOwlProperty(hasBDPStr);
								}
							}
							
							// has Variable
							OWLObjectPropertyImpl hasVar = new OWLObjectPropertyImpl(factory, IRI.create(prefix+"#hasVar"));
							Set hasVarSet = (Set) factorObjProperty.get(hasVar);
							
							if(hasVarSet != null){
								
								Iterator hasVarSetIt = hasVarSet.iterator();
								
								String hasVarStr = null;
								while(hasVarSetIt.hasNext()){
									OWLNamedIndividualImpl var = (OWLNamedIndividualImpl) hasVarSetIt.next();
									hasVarStr = var.toString();
									hasVarStr = hasVarStr.replaceAll("<"+prefix+"#", "");
									hasVarStr = hasVarStr.replaceAll(">", "");
	//System.out.println("RHS var is:" + hasVarStr);
									Variable v = Utils.findVariableWithName(variablesList, hasVarStr);
									f.setV(v);
								}
							}
							
							facList.add(f);
						}
						tb.setFactors(facList);
					}
					
					// add to tb list
					rtbList.add(tb);
					
					// add to constraint
					RHS rhs = new RHS();
					rhs.setTermblocks(rtbList);
					con.setRhs(rhs);
				}
		}
			// add con to consList
			constraintsList.add(con);
		}
		
		return constraintsList;
	}
	
	
}
