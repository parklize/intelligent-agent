import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;


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

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

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
		
//		ArrayList<OWLNamedIndividual> indInA = new ArrayList<OWLNamedIndividual>();
//		ArrayList<IloNumVar> valsInB = new ArrayList<IloNumVar>();

		try {
			File file = new File("Ontology/ve2addedObj.owl");
			
			IloCplex cplex = new IloCplex();
			IloNumVar[] x = cplex.numVarArray(10, 0.0, Double.MAX_VALUE);
			IloNumVar[] y = cplex.numVarArray(4, 0.0, Double.MAX_VALUE);
			
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLDataFactory dataFactory = manager.getOWLDataFactory();
			
			// Load the local copy
			OWLOntology ve = manager.loadOntologyFromOntologyDocument(file);
			
			SWCLOntologyController soc = new SWCLOntologyController(ve);
			String prefix = soc.getPrefix();
			PrefixManager pm = new DefaultPrefixManager(prefix);
			
			ConstraintController con = new ConstraintController(ve, soc);
			ArrayList<Variable> varList = con.getAllVariables();
			
			// 변수의 Description에 의해 변수에 속하는 인스턴스들을 뽑아옴
			// description에 의해 뽑아오게 고침,Need Update
			ArrayList<OWLNamedIndividual> indsInV = new ArrayList<OWLNamedIndividual>();
			ArrayList<OWLNamedIndividual> indsInA = new ArrayList<OWLNamedIndividual>();
			ArrayList<OWLNamedIndividual> indsInB = new ArrayList<OWLNamedIndividual>();
			ArrayList<OWLNamedIndividual> indsInC = new ArrayList<OWLNamedIndividual>();
			ArrayList<OWLNamedIndividual> indsInE = new ArrayList<OWLNamedIndividual>();
			
			
			// indsInV
			OWLClass vendor = dataFactory.getOWLClass(IRI.create(prefix+"#Vendor"));
			Set indsVen = vendor.getIndividuals(ve);
			Iterator vendorIt = indsVen.iterator();
			while(vendorIt.hasNext()){
				indsInV.add((OWLNamedIndividual) vendorIt.next());
			}
			// indsInA
			OWLClass produceWeek = dataFactory.getOWLClass(IRI.create(prefix+"#ProduceWeek"));
			Set indsPro = produceWeek.getIndividuals(ve);
			Iterator indsProIt = indsPro.iterator();
			while(indsProIt.hasNext()){
				indsInA.add((OWLNamedIndividual) indsProIt.next());
			}
			// indsInB
			OWLClass supplying = dataFactory.getOWLClass(IRI.create(prefix+"#Supplying"));
			Set indsSup = supplying.getIndividuals(ve);
			Iterator indsSupIt = indsSup.iterator();
			while(indsSupIt.hasNext()){
				indsInB.add((OWLNamedIndividual) indsSupIt.next());
			}
			// indsInC
			OWLClass spendWeek = dataFactory.getOWLClass(IRI.create(prefix+"#SpendWeek"));
			Set indsSpe = spendWeek.getIndividuals(ve);
			Iterator indsSpeIt = indsSpe.iterator();
			while(indsSpeIt.hasNext()){
				indsInC.add((OWLNamedIndividual) indsSpeIt.next());
			}
			// indsInE
			for(OWLNamedIndividual ind : indsInC){
				OWLObjectProperty spendInventory = dataFactory.getOWLObjectProperty(IRI.create(prefix+"#spendInventory"));
				HashMap spendInventoryVs = (HashMap) ind.getObjectPropertyValues(ve);
				Set spendInventoryV = (Set) spendInventoryVs.get(spendInventory);
				Iterator spendInventoryVIt = spendInventoryV.iterator();
				while(spendInventoryVIt.hasNext()){
					indsInE.add((OWLNamedIndividual) spendInventoryVIt.next());
				}
			}
			
			TestOWL towl = new TestOWL();
			ArrayList<Constraint> consList = towl.getAllConstraints(ve);
			Objective obj = towl.getObjective(ve);
//Utils.printObjective(obj);
				
			IloLinearNumExpr expr = cplex.linearNumExpr();
			
			// obj term blocks
			ArrayList<TermBlock> tblocks = obj.getObjectiveTerm();
			for(TermBlock t: tblocks){
				ArrayList<Parameter> pList = t.getParameters();
				if(pList.size()==1){
					Parameter p = pList.get(0);// b,e
					ArrayList<Factor> fList = t.getFactors();
					Variable v = p.getV();
					// v가 b이면 b의 사이즈에 의해 
					if(v.getName().equals("?b")){
						for(int i=0;i<indsInB.size();i++){
							OWLNamedIndividual ind = indsInB.get(i);
							String owlProperty = fList.get(1).getOwlProperty();
							OWLDataProperty supplyCost = dataFactory.getOWLDataProperty(IRI.create(prefix+"#"+owlProperty));
							HashMap supplyCostV = (HashMap) ind.getDataPropertyValues(ve);
							String sCost = supplyCostV.get(supplyCost).toString();
							StringTokenizer st = new StringTokenizer(sCost,"\"");
							st.nextToken();
							expr.addTerm(x[i], Integer.parseInt(st.nextToken()));
						}
					}else if(v.getName().equals("?e")){
						for(int i=0;i<indsInE.size();i++){
							OWLNamedIndividual ind = indsInE.get(i);
							String owlProperty = fList.get(1).getOwlProperty();
							OWLDataProperty inventoryCost = dataFactory.getOWLDataProperty(IRI.create(prefix+"#"+owlProperty));
							HashMap inventoryCostV = (HashMap) ind.getDataPropertyValues(ve);
							String iCost = inventoryCostV.get(inventoryCost).toString();
							StringTokenizer st = new StringTokenizer(iCost,"\"");
							st.nextToken();
							expr.addTerm(y[i], Integer.parseInt(st.nextToken()));
						}
					}
				}
			}
			
			//obj instruction
			String instruction = obj.getOptimizationInstruction();
			if(instruction.equals("Maximize")){
				cplex.addMaximize(expr);
			}else if(instruction.equals("Minimize")){
				cplex.addMinimize(expr);
			}




			



/*
			for(Constraint c:consList){
System.out.println("Constraint Name:"+c.getName());
				// get qualifiers's list
				ArrayList<Qualifier> qualifierList = c.getQualifiers();
				Iterator it = qualifierList.iterator();
				
				while(it.hasNext()){
					// get qualifier
					Qualifier q = (Qualifier) it.next();
System.out.println("qualifier:"+q.getV().getName());
					//
					 //  get qualifier's class, 
					 //  NEED TO UPDATE description은 manchester syntax이 될수 있으므로 parsing해야 함
					 //
					OWLClass vendor = dataFactory.getOWLClass("#"+q.getV().getDescription(),pm);
					Set vendorList =  vendor.getIndividuals(ve);
					Iterator vendorIt = vendorList.iterator();
					while(vendorIt.hasNext()){
						OWLNamedIndividual vendorInd = (OWLNamedIndividual) vendorIt.next();
System.out.println("ind:"+vendorInd);

					// LHS part
					ArrayList<TermBlock> termblocks = c.getLhs().getTermblocks();
					Iterator termblockIt = termblocks.iterator();
					while(termblockIt.hasNext()){
						TermBlock tb = (TermBlock) termblockIt.next();
						
System.out.println("Termblock sign:"+tb.getSign());
System.out.println("Agg Opp:"+tb.getAggregateOppertor());
						ArrayList<Factor> facList = tb.getFactors();
						Iterator facIt = facList.iterator();
						while(facIt.hasNext()){
							Factor f = (Factor) facIt.next();
System.out.println("Factor Variable:"+f.getV().getName());
System.out.println("Variable Description:"+f.getV().getDescription());
System.out.println("Factor Property:"+f.getOwlProperty());
						}
					}
					
					// NEED UPDATE...변수 a에 들어있는 인스턴스들 추출
					OWLObjectPropertyImpl hasFac = new OWLObjectPropertyImpl(dataFactory,IRI.create(prefix+"#produceWeekOf"));
					OWLObjectPropertyImpl hasProduceWeek = new OWLObjectPropertyImpl(dataFactory,IRI.create(prefix+"#hasProduceWeek"));
//					OWLObjectPropertyExpression hasFacInverse = hasFac.getInverseProperty();
//System.out.println(hasFac+"\n"+hasFacInverse);
					HashMap vendorObjectPorperties = (HashMap) vendorInd.getObjectPropertyValues(ve);
					Set value = (Set) vendorObjectPorperties.get(hasProduceWeek);
					if(value !=null){
						Iterator valueIt = value.iterator();
						while(valueIt.hasNext()){
							// 하나하나의 ProduceWeek 객체
							OWLNamedIndividual ind = (OWLNamedIndividual) valueIt.next();
System.out.println("ind"+ind);
							// NEED UPDATE... factor에서 가져와야 함.
							OWLDataPropertyImpl dp = new OWLDataPropertyImpl(dataFactory, IRI.create(prefix+"#produceCapability"));
							HashMap produceWeek = (HashMap) ind.getDataPropertyValues(ve);
//System.out.println("procudeCapability:"+produceWeek.get(dp).toString());
							// produceCapability값을 가져옴
//							int produceWeekVal = Integer.parseInt((produceWeek.get(dp).toString()).substring(2, 4));

							// 변수 b에 있는 인스턴스,b는 a에 의해 결정되는 것들이므로 a를 백터로 저장했다가 꺼내면서
							// 속하는 b를 하나하나씩 꺼내야 함.
							OWLObjectPropertyImpl produce = new OWLObjectPropertyImpl(dataFactory,IRI.create(prefix+"#produce"));
							HashMap indOPValues = (HashMap) ind.getObjectPropertyValues(ve);
							Set produceVal = (Set) indOPValues.get(produce);
							// 매개 produceweek의 supplying객체들의 개수를 가져옴. 
							int supplyingNum = produceVal.size();
							
							IloNumExpr rexpr = null;
							// 4개의 supplying을 가지고 있으면 
							if(supplyingNum == 4){
								rexpr = cplex.sum(x[0],x[1],x[2],x[3]);
							}else if(supplyingNum == 3){
								rexpr = cplex.sum(x[4],x[5],x[6]);
							}else if(supplyingNum == 2){
								rexpr = cplex.sum(x[7],x[8]);
							}else{
								rexpr = x[9];
							}
							
							// Opp
							String Opp = c.getOpp().getOpp();
System.out.println("Opp:"+Opp);
							if(Opp.equals("greaterThan")){
//								cplex.addGe(produceWeekVal, rexpr);
							}
						}
					}
					
						// RHS
						ArrayList<TermBlock> rtermblocks = c.getRhs().getTermblocks();
						Iterator rtermblockIt = rtermblocks.iterator();
						while(rtermblockIt.hasNext()){
							TermBlock tb = (TermBlock) rtermblockIt.next();
							
	System.out.println("Termblock sign:"+tb.getSign());
	System.out.println("Agg Opp:"+tb.getAggregateOppertor());
							ArrayList<Factor> facList = tb.getFactors();
							Iterator facIt = facList.iterator();
							while(facIt.hasNext()){
								Factor f = (Factor) facIt.next();
	System.out.println("Factor Variable:"+f.getV().getName());
	System.out.println("Variable Description:"+f.getV().getDescription());
	System.out.println("Factor Property:"+f.getOwlProperty());
							}
						}
					}
				}
			}
			*/	
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// get all individuals in a variable
	/*
	private Set<OWLIndividual> indsInVal(Variable v,OWLOntology owl){
		// individuals list in v
		Set<OWLNamedIndividual> indList = new HashSet<OWLNamedIndividual>();
		
		OWLDataFactory df = OWLManager.createOWLOntologyManager().getOWLDataFactory();
		SWCLOntologyController soc = new SWCLOntologyController(owl);
		String prefix = soc.getPrefix();
		PrefixManager pm = new DefaultPrefixManager(prefix);

		String des = v.getDescription();
		StringTokenizer st = new StringTokenizer(des);
		int tkNum = st.countTokens();
		if(tkNum == 1){
			// token 이 하나면 클래스임. 그 클래스에 속하는 객체들 가져오기.
			OWLClass cls = df.getOWLClass(IRI.create("#"+st.nextToken()));
			return cls.getIndividuals(owl);
		}else if(tkNum == 3){
			
			OWLObjectProperty op = df.getOWLObjectProperty(IRI.create("#"+st.nextToken()));
			st.nextToken();
			
			return indList;
		}
	
	}
	*/
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
	
	// get objective from ontology
	private Objective getObjective(OWLOntology owl){
		
		Objective obj = new Objective();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		SWCLOntologyController soc = new SWCLOntologyController(owl);
		String prefix = soc.getPrefix();
    	// controller
    	ConstraintController controller = new ConstraintController(owl, soc);
    	// get all variables to variablesList
    	ArrayList<Variable> variablesList = controller.getAllVariables();
		
		OWLNamedIndividual objInd = new OWLNamedIndividualImpl(factory,IRI.create(prefix+"#objective"));
		
		// get instruction
		OWLDataProperty hasOptInstruction = new OWLDataPropertyImpl(factory, IRI.create(prefix+"#hasOptInstruction"));
		HashMap objIndDP = (HashMap) objInd.getDataPropertyValues(owl);
		Set objIndDPV = (Set)objIndDP.get(hasOptInstruction);
		Iterator objIndDPVIt = objIndDPV.iterator();
		obj.setOptimizationInstruction(objIndDPVIt.next().toString().substring(1,9));
		
		// get Obj Term
		ArrayList<TermBlock> objTerm = new ArrayList<TermBlock>();
		OWLObjectProperty hasObjTerm = new OWLObjectPropertyImpl(factory, IRI.create(prefix+"#hasObjTerm"));
		HashMap objIndOP = (HashMap)objInd.getObjectPropertyValues(owl);
		Set objIndOPV = (Set)objIndOP.get(hasObjTerm);
		Iterator objIndOPVIt = objIndOPV.iterator();
		
		while(objIndOPVIt.hasNext()){
			TermBlock t = new TermBlock();
			// Term block
			OWLNamedIndividual ind = (OWLNamedIndividual) objIndOPVIt.next();
			HashMap indDP = (HashMap) ind.getDataPropertyValues(owl);
			HashMap indOP = (HashMap) ind.getObjectPropertyValues(owl);
			
			// get sign
			OWLDataProperty hasSign = new OWLDataPropertyImpl(factory, IRI.create(prefix+"#hasSign"));
			Set hasSignV = (Set)indDP.get(hasSign);
			Iterator hasSignVIt = hasSignV.iterator();
			t.setSign(hasSignVIt.next().toString().substring(1,2));
			
			// get Agg Opp
			OWLDataProperty hasAggOpp = new OWLDataPropertyImpl(factory, IRI.create(prefix+"#hasAggregateOperation"));
			Set hasAggOppV = (Set)indDP.get(hasAggOpp);
			Iterator hasAggOppVIt = hasAggOppV.iterator();
			t.setAggregateOppertor(hasAggOppVIt.next().toString().substring(1,6));// need update, 1,6
			
			// get parameters
			ArrayList<Parameter> paraList = new ArrayList<Parameter>();
			OWLObjectProperty hasPara = new OWLObjectPropertyImpl(factory,IRI.create(prefix+"#hasParameters"));
			Set hasParaV = (Set) indOP.get(hasPara);
			Iterator hasParaVIt = hasParaV.iterator();
			while(hasParaVIt.hasNext()){
				Parameter p = new Parameter();
				// variable ind
				OWLNamedIndividual v = (OWLNamedIndividual) hasParaVIt.next();
				String vName = soc.getWithoutPrefix(v.toString(), prefix).substring(1);
				Variable via = Utils.findVariableWithName(variablesList, vName);
				p.setV(via);
				paraList.add(p);
			}
			t.setParameters(paraList);
			
			// get factors
			ArrayList<Factor> facList = new ArrayList<Factor>();
			OWLObjectProperty hasFac = new OWLObjectPropertyImpl(factory,IRI.create(prefix+"#hasFactor"));
			Set hasFacV = (Set) indOP.get(hasFac);
			Iterator hasFacVIt = hasFacV.iterator();
			while(hasFacVIt.hasNext()){
				Factor f = new Factor();
				// Factor ind
				OWLNamedIndividual fac = (OWLNamedIndividual) hasFacVIt.next();
				HashMap facDP = (HashMap) fac.getDataPropertyValues(owl);
				HashMap facOP = (HashMap) fac.getObjectPropertyValues(owl);
				OWLDataProperty hasBindingDataProperty = new OWLDataPropertyImpl(factory, IRI.create(prefix+"#hasBindingDataProperty"));
				Set hasBindingDPV = (Set) facDP.get(hasBindingDataProperty);
				Iterator hasBindingDPVIt = hasBindingDPV.iterator();
				while(hasBindingDPVIt.hasNext()){
					String orgStr = hasBindingDPVIt.next().toString();
					String trimOrgStr = orgStr.substring(1);
					int index = trimOrgStr.indexOf("\"");
					// get owlproperty  without prefix
					String str = trimOrgStr.substring(0,index);
					f.setOwlProperty(str);
				}
				// get v
				OWLObjectProperty hasVar = new OWLObjectPropertyImpl(factory, IRI.create(prefix+"#hasVar"));
				Set hasVarOPV = (Set) facOP.get(hasVar);
				Iterator hasVarOPVIt = hasVarOPV.iterator();
				while(hasVarOPVIt.hasNext()){
					OWLNamedIndividual v = (OWLNamedIndividual) hasVarOPVIt.next();
					String vName = soc.getWithoutPrefix(v.toString(), prefix).substring(1);
					Variable via = Utils.findVariableWithName(variablesList, vName);
					f.setV(via);
				}
				facList.add(f);
			}
			t.setFactors(facList);
			
			objTerm.add(t);
		}
		obj.setObjectiveTerm(objTerm);
		return obj;
	}
}
