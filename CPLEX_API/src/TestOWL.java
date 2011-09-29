import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
			
			File file = new File("Ontology/VE_addC2_addObj.owl");
			
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLDataFactory dataFactory = manager.getOWLDataFactory();
			
			// Load the local copy
			OWLOntology ve = manager.loadOntologyFromOntologyDocument(file);
			
			SWCLOntologyController soc = new SWCLOntologyController(ve);
			String prefix = soc.getPrefix();
			PrefixManager pm = new DefaultPrefixManager(prefix);
			
			ConstraintController con = new ConstraintController(ve, soc);
			ArrayList<Variable> varList = con.getAllVariables();
			
			TestOWL towl = new TestOWL();
			Objective obj = towl.getObjective(ve);
			towl.generateIlogCode(obj,towl.getAllConstraints(ve),ve);
			
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	// generate Ilog file
	private void generateIlogCode(Objective obj, ArrayList<Constraint> consList, OWLOntology owl){
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		SWCLOntologyController soc = new SWCLOntologyController(owl);
		String prefix = soc.getPrefix();
		
		try {
			
			ArrayList<String> varsList = new ArrayList<String>();// 생성한 변수들 저장했다가,, 변수 생성할때 이미 생성했는지 체크할 때 쓰자
			StringBuffer varDecStr = new StringBuffer("");// store var part 
			StringBuffer subjectToStr = new StringBuffer(" subject to {\n");// store subject to part
			StringBuffer objectiveStr = new StringBuffer("");// store obj part
			
			FileWriter fw = new FileWriter("IloFile.txt");// Ilog파일 생성 경로
			BufferedWriter bw = new BufferedWriter(fw);
			StringTokenizer st = null;
			
			// objectiveStr 만들기
			// instruction part
			if(obj.getOptimizationInstruction().equals("Maximize")){
				objectiveStr.append("maximize\n");
			}else{
				objectiveStr.append("minimize\n");
			}
			// objective termblocks
			ArrayList<TermBlock> objTList = obj.getObjectiveTerm();
			Iterator objTListIt = objTList.iterator();

			// termblock 개수만큼 가로 확장
			while(objTListIt.hasNext()){
				TermBlock  tb = (TermBlock) objTListIt.next();
				ArrayList<Parameter> pList = tb.getParameters();
				if(pList == null){
					// 파라미터가 없을때
				}else{
					// 파라미터가 있을때
				}
			
				StringBuffer tbStr = new StringBuffer("");
				objectiveStr.append(tb.getSign()+tbStr);
			}
			// subjectToStr 만들기
			for(Constraint c:consList){
//Utils.printConstraint(c);
//System.out.println("Constraint Name:"+c.getName());
				// get qualifiers's list
				ArrayList<Qualifier> qualifierList = c.getQualifiers();
				Iterator it = qualifierList.iterator();
				
				while(it.hasNext()){
					// get qualifier
					Qualifier q = (Qualifier) it.next();
//System.out.println("qualifier:"+q.getV().getName());
					Set<OWLIndividual> indsSet = getIndividuals(q.getV(), owl,null);
					Iterator indsSetIt = indsSet.iterator();
					
					// qualifier 에 의한 loop
					while(indsSetIt.hasNext()){
						
						OWLIndividual ind = (OWLIndividual) indsSetIt.next();// qualifier ind
						// Opp
						String opp = getChangedOpp(c.getOpp().getOpp());
//	System.out.println("Opp:"+opp);
	
						ArrayList<TermBlock> termblocks = c.getLhs().getTermblocks();
						Iterator termblockIt = termblocks.iterator();
						Set<OWLIndividual> varS = new HashSet<OWLIndividual>();//변수에 있는 ind개수
						
						// LHS Termblock에서의 parameter가 있으면 그 속하는 클래스 변수,없으면 Factor변수로 loop수 결정
						// a produceweekOf v이면 , a의 개수에 의해 결정, 파라미터가 있고 b produceBy a이면 a에 의 해 결정
						if(termblockIt.hasNext()){
							TermBlock tb = (TermBlock) termblockIt.next();
							ArrayList<Parameter> pList = tb.getParameters();
							if(pList == null){
//System.out.println("pList size is zero");
								// 파라미터가 없으면 factor로 판단, anyone
								ArrayList<Factor> fList = tb.getFactors();
								Factor f = fList.get(0);
								String des = f.getV().getDescription();
								st = new StringTokenizer(des);
								String var = null;
								if(st.countTokens() == 3){
									var = st.nextToken();
									var = st.nextToken();
									var = st.nextToken();
								}
								if(var.equals(q.getV().getName())){
									varS = getIndividuals(f.getV(), owl,ind);
								}else{
									// need update
								}
							}else{
								// 파라미터 가 있을때
								String aggOp = tb.getAggregateOppertor();
								if(aggOp.equals("sigma")){
									// Sigma일때
									Parameter p = (Parameter) pList.get(0);// parameter하나라고 가정,need update
									Variable v = p.getV();
//System.out.println(v.getName()+":"+v.getDescription());
//System.out.println(indInV);
								// 하고 잇는데..	
								}else{
									// Production 일때
								}
							}
						}
						
						// 한개 constraint에서 나오는 모든 제약식들
						Iterator varSIt = varS.iterator();
						while(varSIt.hasNext()){
							OWLIndividual indInV = (OWLIndividual) varSIt.next();// a에들어있는 ind
							StringBuffer lhsStr = new StringBuffer("   ");
							StringBuffer rhsStr = new StringBuffer("");
							
							// lhsStr 생성
							ArrayList<TermBlock> lhsT = c.getLhs().getTermblocks();
							Iterator lhsTIt = lhsT.iterator();
							while(lhsTIt.hasNext()){
								// 가로 Str생성
								TermBlock tb = (TermBlock) lhsTIt.next();
								StringBuffer tbStr = new StringBuffer("");
								
								ArrayList pList = tb.getParameters();
								if(pList == null){
									// 파라미터가 없으면 factor로 감
									ArrayList<Factor> fList = tb.getFactors();
									for(int i=0; i<fList.size(); i++){
										// factor 있는 만큼 곱합
										Factor f = fList.get(i);
										OWLDataProperty owlP = factory.getOWLDataProperty(IRI.create(prefix+"#"+f.getOwlProperty()));
										HashMap dpVs = (HashMap) indInV.getDataPropertyValues(owl);
										Set dpV = (Set) dpVs.get(owlP);
										if(dpV == null){
											// null잡아서 변수 생성해야 함,need update
										}else{
											Iterator dpVIt = dpV.iterator();
											while(dpVIt.hasNext()){
												// str tokenize with the format of "0"^^xsd:int
												String val = dpVIt.next().toString();
												st = new StringTokenizer(val,"\"");
												tbStr.append(st.nextToken());
												
											}
										}
									}
								}else{
									// termblock에 파라미터가 있을때
//System.out.println("===has parameter===");
								}
								lhsStr.append(tb.getSign()+tbStr);
							}
							
							// rhsStr 생성
							ArrayList<TermBlock> rhsT = c.getRhs().getTermblocks();
							Iterator rhsTIt = rhsT.iterator();
							while(rhsTIt.hasNext()){
								// 가로 Str생성
								TermBlock tb = (TermBlock) rhsTIt.next();
								StringBuffer tbStr = new StringBuffer("");
								
								ArrayList pList = tb.getParameters();
								if(pList == null){
									// 파라미터가 없으면 factor로 감
									ArrayList<Factor> fList = tb.getFactors();
									for(int i=0; i<fList.size(); i++){
										// factor 있는 만큼 곱합
										Factor f = fList.get(i);
										OWLDataProperty owlP = factory.getOWLDataProperty(IRI.create(prefix+"#"+f.getOwlProperty()));
										HashMap dpVs = (HashMap) indInV.getDataPropertyValues(owl);
										Set dpV = (Set) dpVs.get(owlP);
										if(dpV == null){
											// null잡아서 변수 생성해야 함,need update
											System.out.println("nullllll");
										}else{
											Iterator dpVIt = dpV.iterator();
											while(dpVIt.hasNext()){
												// str tokenize with the format of "0"^^xsd:int
												String val = dpVIt.next().toString();
												st = new StringTokenizer(val,"\"");
												tbStr.append(st.nextToken());
												
											}
										}
									}
								}else{
									// termblock에 파라미터가 있을때
//System.out.println("===has parameter===");
									String aggOp = tb.getAggregateOppertor();
									if(aggOp.equals("sigma")){
										// Sigma일때
										Parameter p = (Parameter) pList.get(0);// parameter하나라고 가정,need update
										Variable v = p.getV();
//System.out.println(v.getName()+":"+v.getDescription());
//System.out.println(indInV);
										Set inds = getIndividuals(v, owl, indInV);
										Iterator indsIt = inds.iterator();
										while(indsIt.hasNext()){
											//b에 들어있는 하나하나의 객체
											OWLIndividual indF = (OWLIndividual) indsIt.next();
//System.out.println(indF);
											ArrayList<Factor> fList = tb.getFactors();
											for(int i=0; i<fList.size(); i++){
												// factor 있는 만큼 곱합
												Factor f = fList.get(i);
												OWLDataProperty owlP = factory.getOWLDataProperty(IRI.create(prefix+"#"+f.getOwlProperty()));
//System.out.println("owlP:"+owlP);
												HashMap dpVs = (HashMap) indF.getDataPropertyValues(owl);
												Set dpV = (Set) dpVs.get(owlP);
												Iterator dpVIt = dpV.iterator();
												while(dpVIt.hasNext()){
													// str tokenize with the format of "0"^^xsd:int
													String val = dpVIt.next().toString();
//System.out.println("val:"+val);
													if(val.equals("\"\"^^xsd:int")){
														// ""^^xsd:int잡아서 변수 생성해야 함,need update
//System.out.println("property값이 널이네요,, 변수 생성 합시다");
														// property + ind형태로 변수 생성 및 코드 생성
														// 이미 선언한 변수인지 체크해줘야지
														String varName = soc.getWithoutPrefix(owlP.toString().replace("#", ""), prefix)+soc.getWithoutPrefix(indF.toString().replace("#", ""), prefix);
														if(varsList.contains(varName)){
															// 이미 선언했네요,걍 넘어가
														}else{
															varsList.add(varName);
															varDecStr.append("dvar int+ "+varName+"\n");
														}
														tbStr.append(soc.getWithoutPrefix(owlP.toString().replace("#", ""), prefix)+soc.getWithoutPrefix(indF.toString().replace("#", ""), prefix)+"+");
													}else{
														st = new StringTokenizer(val,"\"");
														tbStr.append(st.nextToken());
													}
													
												}
											}
										}
										
									}else{
										// Production 일때
									}
								}
								rhsStr.append(tb.getSign()+tbStr);
							}
							
							// subject to 에 lhsStr+opp+rhsStr 추가 및 행 바꾸기
							subjectToStr.append(lhsStr).append(opp).append(rhsStr).append("\n");
						}
					}
				}
			}
			
			objectiveStr.append(";");
			subjectToStr.append(" }");
			bw.write(varDecStr.toString()+"\n");// 변수 선언부분 출력
			bw.write(objectiveStr.toString()+"\n");
			bw.write(subjectToStr.toString());// subject to 부분 출력
			
			// close buffered writer
			if(bw!=null){
				bw.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// get opp in regular form
	private String getChangedOpp(String opp) {
		
		if(opp.equals("equal")){
			return "==";
		}else if(opp.equals("notEqual")){
			return "!=";
		}else if(opp.equals("lessThan")){
			return "<";
		}else if(opp.equals("lessThanOrEqual")){
			return "<=";
		}else if(opp.equals("greaterThan")){
			return ">";
		}else if(opp.equals("greaterThanOrEqual")){
			return ">=";
		}
			
		return null;
	}
	
	// get individuals in variable 
	private Set<OWLIndividual> getIndividuals(Variable v,OWLOntology owl,OWLIndividual ind){
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		SWCLOntologyController soc = new SWCLOntologyController(owl);
		String prefix = soc.getPrefix();
		
		String des = v.getDescription();
		StringTokenizer st = new StringTokenizer(des);
		
		Set<OWLIndividual> indsSet = new HashSet<OWLIndividual>();
		
		if(st.countTokens()==1){
			// class description example: Vendor
			OWLClass cls = factory.getOWLClass(IRI.create(prefix+"#"+st.nextToken()));
			return cls.getIndividuals(owl);
		}else if(st.countTokens() == 3){
			// class description example: produceWeek of ?v
			String desc = st.nextToken();
//System.out.println("val desc:"+desc);
			OWLObjectProperty obp = factory.getOWLObjectProperty(IRI.create(prefix+"#"+desc));
			Set obpSet = obp.getInverses(owl);
			Iterator obpSetIt = obpSet.iterator();
			OWLObjectProperty inverseObp = null;
			while(obpSetIt.hasNext()){
				inverseObp = (OWLObjectProperty) obpSetIt.next();// get hasProduceWeek
			}
			HashMap indVs = (HashMap) ind.getObjectPropertyValues(owl);
			Set indV = (Set) indVs.get(inverseObp);
			if(indV != null){
				Iterator indVIt = indV.iterator();
				while(indVIt.hasNext()){
					indsSet.add((OWLIndividual) indVIt.next());
				}
			}else{
				System.out.println("getIndividuals()에서 에러 발생:"+inverseObp+"값이 없습니다...");
			}
			
//System.out.println("cls:"+clss);
		}
		return indsSet;
	}
	
	// get all constraints from ontology
	private ArrayList<Constraint> getAllConstraints(OWLOntology owl) {
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		SWCLOntologyController soc = new SWCLOntologyController(owl);
		String prefix = soc.getPrefix();
		
		ArrayList<Constraint> constraintsList = new ArrayList<Constraint>();
		
    	// controller
    	ConstraintController controller = new ConstraintController(owl, soc);
    	// get all variables to variablesList
    	ArrayList<Variable> variablesList = controller.getAllVariables();


		
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
	
	// get objective from ontology, not used
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
