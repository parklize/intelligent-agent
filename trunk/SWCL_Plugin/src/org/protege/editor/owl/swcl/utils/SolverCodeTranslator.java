package org.protege.editor.owl.swcl.utils;


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
import org.protege.editor.owl.swcl.controller.ConstraintController;
import org.protege.editor.owl.swcl.controller.SWCLOntologyController;
import org.protege.editor.owl.swcl.model.Constraint;
import org.protege.editor.owl.swcl.model.Factor;
import org.protege.editor.owl.swcl.model.LHS;
import org.protege.editor.owl.swcl.model.Objective;
import org.protege.editor.owl.swcl.model.Operator;
import org.protege.editor.owl.swcl.model.Parameter;
import org.protege.editor.owl.swcl.model.Qualifier;
import org.protege.editor.owl.swcl.model.RHS;
import org.protege.editor.owl.swcl.model.TermBlock;
import org.protege.editor.owl.swcl.model.Variable;
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
import org.semanticweb.owlapi.model.OWLOntologyManager;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLDataPropertyImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLNamedIndividualImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

public class SolverCodeTranslator {

	public static void translateSWCL(OWLOntology owl) {
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		
		// Load the local copy
		OWLOntology ve = owl;
		
		SWCLOntologyController soc = new SWCLOntologyController(ve);
		String prefix = soc.getPrefix();
		SolverCodeTranslator sct = new SolverCodeTranslator();
		Objective obj = sct.getObjective(ve);
		
System.out.println("Got objective, start getting constraints related to it...");
		
		ArrayList<Constraint> allConstraints = sct.getAllConstraints(ve);
		ArrayList<Constraint> consList = sct.getRelatedConstraints(obj,allConstraints,ve);
//System.out.println(consList.size());
//for(Constraint c:consList){
//	System.out.println(c.getName());
//}
		sct.generateIlogCode(obj,consList,ve);
		
System.out.println("Generating Ilog Code finished....");
	}
	
	
	// get related constraint from objective
	private ArrayList<Constraint> getRelatedConstraints(Objective obj,
			ArrayList<Constraint> allConstraints, OWLOntology owl) {
		
		ArrayList<Constraint> relatedCons = new ArrayList<Constraint>();
		
		// default
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		SWCLOntologyController soc = new SWCLOntologyController(owl);
		String prefix = soc.getPrefix();
		ConstraintController con = new ConstraintController(owl, soc);
		
		// get {classexpression,property} set from objective
		ArrayList<TermBlock> objTerms = obj.getObjectiveTerm();
		ArrayList<Factor> factors = new ArrayList<Factor>();
		for(TermBlock t:objTerms){
			ArrayList<Factor> facs = t.getFactors();
			factors.addAll(facs);
		}
		
		// loop all constraints to find out related factor
		for(Constraint c:allConstraints){
			ArrayList<TermBlock> lhsT = c.getLhs().getTermblocks();
			for(TermBlock t:lhsT){
				ArrayList<Factor> facs = t.getFactors();
				// check related factors exist or not in facs
				if(checkFactors(owl,factors,facs)){
					relatedCons.add(c);
				}
			}
			ArrayList<TermBlock> rhsT = c.getRhs().getTermblocks();
			for(TermBlock t:rhsT){
				ArrayList<Factor> facs = t.getFactors();
				// check related factors exist or not in facs
				if(checkFactors(owl,factors,facs)){
					if(relatedCons.size()==0){
						relatedCons.add(c);// initialize
					}else{
						if(relatedCons.contains(c)){
							// if already exist, skip...
						}else{
							relatedCons.add(c);
						}
					}
				}
			}
		}
		
		// return related constraints
		return relatedCons;
	}


	// check whether two factors are related to each other
	private boolean checkFactors(OWLOntology owl, ArrayList<Factor> factors, ArrayList<Factor> facs) {
		
		for(Factor fac:factors){
			for(Factor f:facs){
				if(f.getOwlProperty().equals(fac.getOwlProperty())){
					
					String fDescription = f.getV().getDescription();// latter one
					String facDescription = fac.getV().getDescription();// formar one
					
					Set indsList_latter = getIndividuals(f.getV(), owl, null);
					Set indsList_former = getIndividuals(fac.getV(), owl, null);
					Set intersectionSet = Utils.intersectionSet(indsList_former, indsList_latter);// get intersection
					
					// If there's intersection return ture
					if(intersectionSet.size()==0){
						return false;
					}else{
						return true;
					}
					
				}
			}
		}
		
		return false;
	}


	// generate Ilog file
	private void generateIlogCode(Objective obj, ArrayList<Constraint> consList, OWLOntology owl){
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		SWCLOntologyController soc = new SWCLOntologyController(owl);
		String prefix = soc.getPrefix();
		ConstraintController con = new ConstraintController(owl, soc);
		ArrayList<Variable> varList = con.getAllVariables();
		
		try {
			
			ArrayList<String> varsList = new ArrayList<String>();// 생성한 변수들 저장했다가,, 변수 생성할때 이미 생성했는지 체크할 때 쓰자
			StringBuffer varDecStr = new StringBuffer("");// store var part 
			StringBuffer subjectToStr = new StringBuffer(" subject to {\n");// store subject to part
			StringBuffer objectiveStr = new StringBuffer("");// store obj part
			
			FileWriter fw = new FileWriter("IloFile.txt");// Ilog파일 생성 경로
			BufferedWriter bw = new BufferedWriter(fw);
			StringTokenizer st = null;
			
			// objectiveStr 만들기
			// objective termblocks
			ArrayList<TermBlock> objTList = obj.getObjectiveTerm();
			Iterator objTListIt = objTList.iterator();
//System.out.println(objTList.size());
			// termblock 개수만큼 가로 확장
			while(objTListIt.hasNext()){
				TermBlock  tb = (TermBlock) objTListIt.next();
				StringBuffer tbStr = new StringBuffer("");// termblock str for every termblock
				ArrayList<Factor> fList = tb.getFactors();
				
				// parameter
				ArrayList<Parameter> pList = tb.getParameters();
				if(pList == null){
					// 파라미터가 없을때
System.out.println("no parameter");
				}else{
					// 파라미터가 있을때
					Parameter p = pList.get(0);// parameter 하나라고 가정,need update
					String des = p.getV().getDescription();
//System.out.println("des:"+des);
					
					Set total = getIndividuals(p.getV(), owl, null);
//System.out.println("total:"+total.size());					
					// 변수에 들어있는 객체들의 개수만큼 
					Iterator totalIt = total.iterator();

					while(totalIt.hasNext()){
						OWLIndividual ind = (OWLIndividual) totalIt.next();
						StringBuffer fStr = new StringBuffer("");
						for(int j=0; j<fList.size(); j++){
							Factor f = (Factor) fList.get(j);
							OWLDataProperty dp = factory.getOWLDataProperty(IRI.create(prefix+"#"+f.getOwlProperty()));
//System.out.println("dp:"+dp);
							HashMap dpVs = (HashMap) ind.getDataPropertyValues(owl);
							Set dpV = (Set) dpVs.get(dp);// property value set
							Iterator dpVIt = dpV.iterator();
							while(dpVIt.hasNext()){
								String val = dpVIt.next().toString(); //property value
								if(val.equals("\"\"^^xsd:int")){
									// ""^^xsd:int잡아서 변수 생성해야 함,need update
									// property + ind형태로 변수 생성 및 코드 생성
									// 이미 선언한 변수인지 체크해줘야지
									String varName = soc.getWithoutPrefix(dp.toString().replace("#", ""), prefix)+soc.getWithoutPrefix(ind.toString().replace("#", ""), prefix);
									if(varsList.contains(varName)){
										// 이미 선언했네요,걍 넘어가
									}else{
										varsList.add(varName);
										varDecStr.append("dvar int+ "+varName+";\n");
									}
									fStr.append(soc.getWithoutPrefix(dp.toString().replace("#", ""), prefix)+soc.getWithoutPrefix(ind.toString().replace("#", ""), prefix)+"*");
								}else{
									st = new StringTokenizer(val,"\"");
									fStr.append(st.nextToken()+"*");
								}
							}
						}
						tbStr.append(tb.getSign()+fStr);// add factor string to termblock string
						tbStr = new StringBuffer(tbStr.substring(0, tbStr.length()-1));// delete every termblock's *
					}
					objectiveStr.append(tbStr);
				}
			}
			
			objectiveStr = new StringBuffer(objectiveStr.substring(1));// delete first +
			
			// instruction part
			if(obj.getOptimizationInstruction().equals("Maximize")){
				objectiveStr = new StringBuffer("maximize\n"+objectiveStr);
			}else{
				objectiveStr = new StringBuffer("minimize\n"+objectiveStr);
			}
			
			// subjectToStr 만들기
			for(Constraint c:consList){
//Utils.printConstraint(c);
				
				
//System.out.println("Constraint Name:"+c.getName());
				// get qualifiers's list
				ArrayList<Qualifier> qualifierList = c.getQualifiers();
				ArrayList<Set<OWLIndividual>> qList = new ArrayList<Set<OWLIndividual>>();
				
				for(int i=0;i<qualifierList.size();i++){
					ArrayList<OWLIndividual> indList = new ArrayList<OWLIndividual>();// qualifier에 들어있는 객체들 담을 객체
					Variable v = qualifierList.get(i).getV();
					String vDes = v.getDescription().trim();// qualifer description,like Vendor,Week
//System.out.println("vDes:"+vDes);
					OWLClass cls = factory.getOWLClass(IRI.create(prefix+"#"+vDes));
					Set<OWLIndividual> indsSet = getIndividuals(v, owl, null);
					qList.add(i, indsSet);
//System.out.println("indsSet Size:"+indsSet.size());
				}
				
				// qualifer 가 하나인 경우,, 
				int qListSize = qList.size();
				int i = 0;
				
				// loop에서 각 qualifier들의 ind저장
				// 아래에서 des에서 variable비교해서 그 variable일때 이 ind넣음
				OWLIndividual[] indIndex = new OWLIndividual[qListSize];
				subGenerate(varList, subjectToStr, varDecStr, varsList, owl,factory,prefix,c,qualifierList,qList,i,indIndex);
				
			}
			
			objectiveStr.append(";");
			subjectToStr.append(" }");
			bw.write(varDecStr.toString()+"\n");// 변수 선언부분 출력
			bw.write(objectiveStr.toString()+"\n\n");// objective 부분 출력
			bw.write(subjectToStr.toString());// subject to 부분 출력
			
			// close buffered writer
			if(bw!=null){
				bw.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	 * 중점적으로 하고 있음,,,, 어려운 부분임.... 미치겠다,,,,,,,,,,,,,,,,,,,
	 */
	private void subGenerate(ArrayList<Variable> varList, StringBuffer subjectToStr, StringBuffer varDecStr, ArrayList<String> varsList, OWLOntology owl, OWLDataFactory factory, String prefix, Constraint c,ArrayList<Qualifier> qualifierList, ArrayList<Set<OWLIndividual>> qList,int i,OWLIndividual[] indIndex) {
		
		SWCLOntologyController soc = new SWCLOntologyController(owl);
		
		int qListSize = qList.size();

		if(qListSize>i){
			Set<OWLIndividual> indSet = qList.get(i); // 첫번째 qualifier에 들어있는 ind set

				for(OWLIndividual ind:indSet){
//System.out.println(ind);
//System.out.println("i:"+i);
//System.out.println("qListSize:"+qListSize);
					indIndex[i] = ind;
//System.out.println("indIndex[0]"+indIndex[0]); //첫번째 qualifier에 대응하는 ind
//System.out.println("indIndex[1]"+indIndex[1]); //두번째 qualifier에 대응하는 ind
					if(qListSize>i+1){
//System.out.println("==i:"+i);
						subGenerate(varList, subjectToStr, varDecStr, varsList, owl, factory, prefix,c, qualifierList, qList, i+1, indIndex);
					}else{
						
						
						// Opp
						String opp = getChangedOpp(c.getOpp().getOpp());

						
						
						
						// lhsStr생성
						StringBuffer lhsStr = new StringBuffer("");
						
						ArrayList<TermBlock> lhsTermblocks = c.getLhs().getTermblocks();// get lhs termblocks
						Iterator lhsTBIt = lhsTermblocks.iterator();
					
						while(lhsTBIt.hasNext()){
							TermBlock tb = (TermBlock) lhsTBIt.next();// LSH termblock
//System.out.println("tbtbtbt");
							StringBuffer tbStr = new StringBuffer("");
							
							ArrayList<Parameter> pList = tb.getParameters();// parameter list
							if(pList == null){
								// 파라미터가 없을때
								ArrayList<Factor> fList = tb.getFactors();
								Variable v = fList.get(0).getV();
								String des = v.getDescription();
								String desArray[] = des.split(" and\\s*");
//System.out.println("des:"+des);
								Set<OWLIndividual> inds = getIndsInVar(varList, desArray,owl, factory, prefix,c, qualifierList, qList, i+1, indIndex);
//System.out.println(inds.size());
								Iterator indsIt = inds.iterator();
								while(indsIt.hasNext()){
									OWLIndividual fInd = (OWLIndividual) indsIt.next();
//System.out.println("fInd:"+fInd);
									for(int p=0; p<fList.size(); p++){
										// factor 있는 만큼 곱합
										Factor f = fList.get(p);
										OWLDataProperty owlP = factory.getOWLDataProperty(IRI.create(prefix+"#"+f.getOwlProperty()));
//System.out.println("owlP:"+owlP);
										HashMap dpVs = (HashMap) fInd.getDataPropertyValues(owl);
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
												String varName = soc.getWithoutPrefix(owlP.toString().replace("#", ""), prefix)+soc.getWithoutPrefix(fInd.toString().replace("#", ""), prefix);
												if(varsList.contains(varName)){
													// 이미 선언했네요,걍 넘어가
												}else{
													varsList.add(varName);
													varDecStr.append("dvar int+ "+varName+";\n");
												}
												tbStr.append(soc.getWithoutPrefix(owlP.toString().replace("#", ""), prefix)+soc.getWithoutPrefix(fInd.toString().replace("#", ""), prefix)+"*");
											}else{
//System.out.println("val:"+val);
												StringTokenizer st = new StringTokenizer(val,"\"");
												tbStr.append(st.nextToken()+"*");
											}
										}
									}
								}
								lhsStr.append(tb.getSign()+tbStr);// add factor string to termblock string
								lhsStr = new StringBuffer(lhsStr.substring(0, lhsStr.length()-1));// delete every termblock's *
//System.out.println("lhsStr:"+lhsStr);
							}else{
								// 파라미터 가 있을때
								Parameter p = (Parameter) pList.get(0);// parameter하나라고 가정,need update
								Variable v = p.getV();
								String des = v.getDescription();
								String desArray[] = des.split(" and\\s*");
								
								Set<OWLIndividual> inds = getIndsInVar(varList, desArray,owl, factory, prefix,c, qualifierList, qList, i+1, indIndex);
								Iterator indsIt = inds.iterator();
//for(OWLIndividual inddddd:inds){
//	
//	System.out.println(v.getName()+"에서 나온 ind:"+inddddd);
//}
//System.out.println("====");
								String aggOp = tb.getAggregateOppertor();
								if(aggOp.equals("sigma")){
									while(indsIt.hasNext()){
										//b에 들어있는 하나하나의 객체
										OWLIndividual indF = (OWLIndividual) indsIt.next();
										ArrayList<Factor> fList = tb.getFactors();
										StringBuffer fStr = new StringBuffer("");
										
										for(int k=0;k<fList.size();k++){
											// factor 있는 만큼 곱합
											Factor f = fList.get(k);
											OWLDataProperty owlP = factory.getOWLDataProperty(IRI.create(prefix+"#"+f.getOwlProperty()));
//System.out.println("indF:"+indF);
//System.out.println("owlP:"+owlP);
											HashMap dpVs = (HashMap)indF.getDataPropertyValues(owl);
											Set dpV = (Set)dpVs.get(owlP);
											Iterator dpVIt = dpV.iterator();
											while(dpVIt.hasNext()){
												String val = dpVIt.next().toString();
//System.out.println("val:"+val);
												if(val.equals("\"\"^^xsd:int")){
													// ""^^xsd:int잡아서 변수 생성해야 함,need update
//System.out.println("property값이 널이네요,, 변수 생성 합시다");
												    // property + ind형태로 변수 생성 및 코드 생성
													// 이미 선언한 변수인지 체크해줘야지
													String varName = soc.getWithoutPrefix(owlP.toString().replace("#", ""), prefix)+soc.getWithoutPrefix(indF.toString().replace("#", ""), prefix);
//System.out.println("varName:"+varName);
													if(varsList.contains(varName)){
														// 이미 선언했네요,걍 넘어가
													}else{
														varsList.add(varName);
														varDecStr.append("dvar int+ "+varName+";\n");
													}
													tbStr.append(tb.getSign()+varName);
												}else{
													StringTokenizer st = new StringTokenizer(val,"\"");
													tbStr.append(st.nextToken()+"*");
												}
											}
										}
									}
								}else{
									//production 일때
								}
								lhsStr.append(tbStr);
							}
						}
						
						if(lhsStr.length()>=1){
							lhsStr = new StringBuffer(lhsStr.substring(1));// delete first +
//System.out.println("lhsStr:"+lhsStr);
						}
						
						
						
						
						// rhs 생성
						StringBuffer rhsStr = new StringBuffer("");
						
						ArrayList<TermBlock> rhsTermblocks = c.getRhs().getTermblocks();// get rhs termblocks
						Iterator rhsTBIt = rhsTermblocks.iterator();
						
						while(rhsTBIt.hasNext()){
							TermBlock tb = (TermBlock) rhsTBIt.next();// RSH termblock
							StringBuffer tbStr = new StringBuffer("");
							
							ArrayList<Parameter> pList = tb.getParameters();// parameter list
							if(pList == null){
								ArrayList<Factor> fList = tb.getFactors();
								Variable v = fList.get(0).getV();
								String des = v.getDescription();
								String desArray[] = des.split("and\\s*");
//System.out.println(des);
								Set<OWLIndividual> inds = getIndsInVar(varList, desArray,owl, factory, prefix,c, qualifierList, qList, i+1, indIndex);
//System.out.println(inds.size());
								Iterator indsIt = inds.iterator();
								while(indsIt.hasNext()){
									OWLIndividual fInd = (OWLIndividual) indsIt.next();
//System.out.println("fInd:"+fInd);
									for(int p=0; p<fList.size(); p++){
										// factor 있는 만큼 곱합
										Factor f = fList.get(p);
										OWLDataProperty owlP = factory.getOWLDataProperty(IRI.create(prefix+"#"+f.getOwlProperty()));
									//System.out.println("owlP:"+owlP);
										HashMap dpVs = (HashMap) fInd.getDataPropertyValues(owl);
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
												String varName = soc.getWithoutPrefix(owlP.toString().replace("#", ""), prefix)+soc.getWithoutPrefix(fInd.toString().replace("#", ""), prefix);
												if(varsList.contains(varName)){
													// 이미 선언했네요,걍 넘어가
												}else{
													varsList.add(varName);
													varDecStr.append("dvar int+ "+varName+";\n");
												}
												tbStr.append(soc.getWithoutPrefix(owlP.toString().replace("#", ""), prefix)+soc.getWithoutPrefix(fInd.toString().replace("#", ""), prefix)+"*");
											}else{
//System.out.println("owlP:"+owlP+"val:"+val);
												StringTokenizer st = new StringTokenizer(val,"\"");
												tbStr.append(st.nextToken()+"*");
											}
										}
									}
								}
								rhsStr.append(tb.getSign()+tbStr);// add factor string to termblock string
								rhsStr = new StringBuffer(rhsStr.substring(0, rhsStr.length()-1));// delete every termblock's *
//System.out.println("rhsStr:"+rhsStr);
							}else{
								// 파라미터 가 있을때
								Parameter p = (Parameter) pList.get(0);// parameter하나라고 가정,need update
								Variable v = p.getV();
								String des = v.getDescription();
								String desArray[] = des.split("and\\s*");
								
								Set<OWLIndividual> inds = getIndsInVar(varList, desArray,owl, factory, prefix,c, qualifierList, qList, i+1, indIndex);
								Iterator indsIt = inds.iterator();
//	for(OWLIndividual inddddd:inds){
//		System.out.println(inddddd);
//	}
//	System.out.println("====");
								String aggOp = tb.getAggregateOppertor();
								if(aggOp.equals("sigma")){
									while(indsIt.hasNext()){
										//b에 들어있는 하나하나의 객체
										OWLIndividual indF = (OWLIndividual) indsIt.next();
										ArrayList<Factor> fList = tb.getFactors();
										StringBuffer fStr = new StringBuffer("");
										
										for(int k=0;k<fList.size();k++){
											// factor 있는 만큼 곱합
											Factor f = fList.get(k);
											OWLDataProperty owlP = factory.getOWLDataProperty(IRI.create(prefix+"#"+f.getOwlProperty()));
//System.out.println("owlP:"+owlP);
											HashMap dpVs = (HashMap)indF.getDataPropertyValues(owl);
											Set dpV = (Set)dpVs.get(owlP);
											Iterator dpVIt = dpV.iterator();
											while(dpVIt.hasNext()){
												String val = dpVIt.next().toString();
//System.out.println("val:"+val);
												if(val.equals("\"\"^^xsd:int")){
													// ""^^xsd:int잡아서 변수 생성해야 함,need update
//System.out.println("property값이 널이네요,, 변수 생성 합시다");
												    // property + ind형태로 변수 생성 및 코드 생성
													// 이미 선언한 변수인지 체크해줘야지
													String varName = soc.getWithoutPrefix(owlP.toString().replace("#", ""), prefix)+soc.getWithoutPrefix(indF.toString().replace("#", ""), prefix);
//System.out.println("varName:"+varName);
													if(varsList.contains(varName)){
														// 이미 선언했네요,걍 넘어가
													}else{
														varsList.add(varName);
														varDecStr.append("dvar int+ "+varName+";\n");
													}
													tbStr.append(tb.getSign()+varName);
												}else{
													StringTokenizer st = new StringTokenizer(val,"\"");
													tbStr.append(st.nextToken()+"*");
												}
											}
										}
									}
								}else{
									//production 일때
								}
								rhsStr.append(tbStr);
							}
						}
						
						if(rhsStr.length()>=1){
							rhsStr = new StringBuffer(rhsStr.substring(1)+";");// delete first +
//System.out.println("rhsStr:"+rhsStr);
						}
						
//System.out.println("final lhsStr:"+lhsStr);
//System.out.println("final rhsStr:"+rhsStr);
						
						// lhs+sign+rhs
						if(lhsStr.length()>=1 && rhsStr.length()>=1){
//System.out.println("printing...");
							// v가 v1이고 v4Week4인 것을 방지하기 위해서 
							subjectToStr.append("   ").append(lhsStr).append(opp).append(rhsStr).append("\n");// subject to 에 lhsStr+opp+rhsStr 추가 및 행 바꾸기
						}
					}
				}
		}
	}



	private Set<OWLIndividual> getIndsInVar(ArrayList<Variable> varList, String desArray[], OWLOntology owl, OWLDataFactory factory, String prefix, Constraint c,ArrayList<Qualifier> qualifierList, ArrayList<Set<OWLIndividual>> qList,int i,OWLIndividual[] indIndex) {
		Set<OWLIndividual> inds = new HashSet<OWLIndividual>();// 최종 intersection담을 set, a에 객체들
		for(String s:desArray){
//System.out.println("s:"+s);
			String partDes = s.trim();//앞뒤 blank들을 없앤것.
//System.out.println("partDes:"+partDes);		
			String splitStr[] = partDes.split(" ");
//for(String s1:splitStr){
//System.out.println("s1:"+s1);
//}
			// consumption과 같은 하나짜리 일때
			if(splitStr.length == 1){
				OWLClass cls = factory.getOWLClass(IRI.create(prefix+"#"+s.trim()));
//System.out.println("cls:"+cls);
				Set indsSet = cls.getIndividuals(owl);
				if(inds.size()==0){
					// 초기화 
					inds = indsSet;
				}else{
					// intersection
					inds = Utils.intersectionSet(inds, indsSet);
				}
			}else{
				// hasProduceWeek value w 과 같은 세개짜리 일때, 
				// w는 qualifier리스트에서 찾아서 inds+index형태로 객체 써보자
				Set indsSet = new HashSet();
				
				// case 1: qualifier들을 돌면서 변수 판단
				// qualifier중에 변수랑 맞으면 
				if(checkVarInQualifiers(splitStr[2],qualifierList)){
					for(int j=0; j<qualifierList.size(); j++){
	//System.out.println(qualifierList.get(j).getV().getName());
						if(qualifierList.get(j).getV().getName().equals(splitStr[2])){
							// qualifier중 변수랑 같을 때
							OWLObjectProperty objP = factory.getOWLObjectProperty(IRI.create(prefix+"#"+splitStr[0]));
	//System.out.println("objP:"+objP);
							Set inverseObjPSet = objP.getInverses(owl);
							Iterator inverseObjPIt = inverseObjPSet.iterator();
							OWLObjectProperty inverseObjP = null;// inverse property
							while(inverseObjPIt.hasNext()){
								inverseObjP = (OWLObjectProperty) inverseObjPIt.next();
							}
							OWLIndividual indexedInd = indIndex[j];	
//	System.out.println("inverseObjP:"+inverseObjP);						
//	System.out.println("indexedInd:"+indexedInd);
							HashMap indVs = (HashMap) indexedInd.getObjectPropertyValues(owl);
							Set indV = (Set) indVs.get(inverseObjP);
							if(indV != null){
								Iterator indVIt = indV.iterator();
								while(indVIt.hasNext()){
									indsSet.add((OWLIndividual) indVIt.next());
								}
							}else{
								System.out.println("getIndsInVar()에서 에러 발생:"+indexedInd+":"+inverseObjP+"값이 없습니다...");
							}
						}
					}
				}else{
					// case2: qualifier가 아닌 변수로 description에 들어있을때
					
					Variable v = Utils.findVariableWithName(varList, splitStr[2]);
//System.out.println("v name:"+v.getName());
//System.out.println("v des:"+v.getDescription());
					// variable이 des길이가 하나인(class name)으로 되었을때
					OWLClass cls = factory.getOWLClass(IRI.create(prefix+"#"+v.getDescription().trim()));
//System.out.println("case2 cls:"+cls);
					Set<OWLIndividual> individualsSet = cls.getIndividuals(owl);
					Iterator individualsSetIt = individualsSet.iterator();
					while(individualsSetIt.hasNext()){
						OWLIndividual indexedInd = (OWLIndividual) individualsSetIt.next();
//System.out.println("case2 indexedInd:"+indexedInd);
						OWLObjectProperty objP = factory.getOWLObjectProperty(IRI.create(prefix+"#"+splitStr[0]));
						Set inverseObjPSet = objP.getInverses(owl);
						Iterator inverseObjPIt = inverseObjPSet.iterator();
						OWLObjectProperty inverseObjP = null;// inverse property
						while(inverseObjPIt.hasNext()){
							inverseObjP = (OWLObjectProperty) inverseObjPIt.next();
						}
						HashMap indVs = (HashMap) indexedInd.getObjectPropertyValues(owl);
						Set indV = (Set) indVs.get(inverseObjP);
						if(indV != null){
							Iterator indVIt = indV.iterator();
							while(indVIt.hasNext()){
								indsSet.add((OWLIndividual) indVIt.next());
							}
						}else{
							System.out.println("getIndsInVar()에서 에러 발생:"+indexedInd+":"+inverseObjP+"값이 없습니다...");
						}
					}
				}

				if(inds.size()==0){
					// 초기화 
					inds = indsSet;	
				}else{
					// intersection
					inds = Utils.intersectionSet(inds, indsSet);	
				}
				
			}
		}	
		return inds;
	}


	// qualiferlist에 들어있는 변수있지 체크
	private boolean checkVarInQualifiers(String varName, ArrayList<Qualifier> qualifierList) {
		for(Qualifier q:qualifierList){
			String variableName = q.getV().getName();
			if(varName.equals(variableName)){
				return true;
			}
		}
		return false;
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
		
		Set<OWLIndividual> inds = new HashSet<OWLIndividual>();// 최종 intersection담을 set
//System.out.println("this is getIndividuals()");
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		SWCLOntologyController soc = new SWCLOntologyController(owl);
		String prefix = soc.getPrefix();
    	// controller
    	ConstraintController controller = new ConstraintController(owl, soc);
    	// get all variables to variablesList
    	ArrayList<Variable> variablesList = controller.getAllVariables();
		
		String des = v.getDescription();
		String desArray[] = des.split("and\\s*");
		
		// description이 하나일때
		if(desArray.length==1){
			
			String clsDes = desArray[0].trim();
			// class description example: Vendor
			OWLClass cls = factory.getOWLClass(IRI.create(prefix+"#"+clsDes));
			inds = cls.getIndividuals(owl);
			
		}else{
			// description이 여러개 and로 묶여있을때
			/**
			 * Need Update
			 */
			Set indsSet = new HashSet();
			for(String pd:desArray){
				
				String partDes = pd.trim();// delete blanks 
				// 각자 description에서 inds구해서 intersection해야 함.... 어떻게 할까???
//System.out.println("partDes:"+partDes);
				String partDesArray[] = partDes.split(" ");
//for(String s:partDesArray){
//	System.out.println("s:"+s);
//}
				
				if(partDesArray.length==1){
					
					Set indsSet_01 = new HashSet();
					String clsDes = partDesArray[0];
					OWLClass cls = factory.getOWLClass(IRI.create(prefix+"#"+clsDes));
					indsSet_01 = cls.getIndividuals(owl);
					if(indsSet.size()==0){
						indsSet = indsSet_01;
					}else{
						indsSet = Utils.intersectionSet(indsSet, indsSet_01);
					}
				}else{
					
					Set indsSet_02 = new HashSet();
					// get datatype property (ex:hasConsumptionVendor value ?z)
					OWLObjectProperty odp = factory.getOWLObjectProperty(IRI.create(prefix+"#"+partDesArray[0].trim()));
					// get inverse property
					Set inverseObjPSet = ((OWLObjectPropertyExpression) odp).getInverses(owl);
					Iterator inverseObjPIt = inverseObjPSet.iterator();
					OWLObjectProperty inverseObjP = null;// inverse property
					while(inverseObjPIt.hasNext()){
						inverseObjP = (OWLObjectProperty) inverseObjPIt.next();
					}
					// get all individuals from ?z
					Variable var = Utils.findVariableWithName(variablesList, partDesArray[2]);
					Set indsList = getIndividuals(var, owl, null);
					Iterator indsListIt = indsList.iterator();
					while(indsListIt.hasNext()){
						OWLIndividual individual = (OWLIndividual) indsListIt.next();
						HashMap indValues = (HashMap) individual.getObjectPropertyValues(owl);
						Set indV = (Set) indValues.get(inverseObjP);
						indsSet_02.addAll(indV);
					}
					
					if(indsSet.size()==0){
						indsSet = indsSet_02;
					}else{
						indsSet = Utils.intersectionSet(indsSet, indsSet_02);
					}
				}
			}// end of for
			
			inds = indsSet;
		}
		return inds;
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
	
	private ArrayList<OWLIndividual> getAllInds(ArrayList<VariableStructure> vsList){
		ArrayList total = new ArrayList();
		Iterator vsListIt = vsList.iterator();
		// 변수에 대한 모든 객채들 모음
		while(vsListIt.hasNext()){
			ArrayList chList = ((VariableStructure)vsListIt.next()).getChildrens();
			Iterator chListIt = chList.iterator();
			while(chListIt.hasNext()){
				total.add(chListIt.next());
			}
		}
		return total;
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
