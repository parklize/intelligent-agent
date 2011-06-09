package org.protege.editor.owl.swcl.tab;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxOntologyFormat;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.OWLWorkspace;
import org.protege.editor.owl.swcl.model.Constraint;
import org.protege.editor.owl.swcl.model.Factor;
import org.protege.editor.owl.swcl.model.LHS;
import org.protege.editor.owl.swcl.model.Operator;
import org.protege.editor.owl.swcl.model.Parameter;
import org.protege.editor.owl.swcl.model.Qualifier;
import org.protege.editor.owl.swcl.model.RHS;
import org.protege.editor.owl.swcl.model.TermBlock;
import org.protege.editor.owl.swcl.model.Variable;
import org.protege.editor.owl.swcl.utils.CheckBoxRenderer;
import org.protege.editor.owl.swcl.utils.CheckButtonEditor;
import org.protege.editor.owl.swcl.utils.SWCLOntologyHelper;
import org.protege.editor.owl.swcl.utils.Utils;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.io.WriterDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import uk.ac.manchester.cs.owl.owlapi.OWLClassAssertionImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLClassExpressionImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLDataPropertyImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLNamedIndividualImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;


/**
 * 
 * Author: parklize,pryiyeon
 * Date: 2011.04.20~
 * Description: SWCL view component in Protege
 * 
 */

public class ExampleViewComponent extends AbstractOWLViewComponent implements ActionListener{
	
	// default codes
    private static final long serialVersionUID = -4515710047558710080L;
    private static final Logger log = Logger.getLogger(ExampleViewComponent.class);
    
    // components for our view
    private JPanel menuPanel = null; // menu panel located in top
    private JPanel buttonsPanel = null; // buttons panel located at east of menu panel
    private JPanel constraintsPanel = null; // constraintsPanel contains constraints scroll pane
    private JScrollPane constraintsScrollPane = null; // constriantsScrollPane contains constraints table
    private JTable constraintsTable = null; // constraints table
    private JButton addConstraintButton = null; // add constraint button
    private JButton generateSWCLButton = null; // button to generate SWCL code
    private OWLModelManager owlModelManager = null;
    private OWLWorkspace ow = null;
    private OWLOntology owl = null;
    private OWLClassExpression oc = null;
    private SWCLOntologyHelper soh = null;
	private String prefix = null;
    
    // global variables
	private ArrayList<Variable> variablesList = new ArrayList<Variable>();  
	private ArrayList<Constraint> constraintsList = new ArrayList<Constraint>();

    @Override
    protected void disposeOWLView() {}

    @Override
    protected void initialiseOWLView() throws Exception {
        
    	// set layout 
        setLayout(new BorderLayout());
        
        // add panels to the protege jPanel
        add(getMenuPanel(),BorderLayout.NORTH);
        add(getConstraintsPanel(),BorderLayout.CENTER);
        
        // get info from ontology
        initializeSWCL();
        
        // default codes
//        log.info("Example View Component initialized");
    }
    

    // NEED UPDATE...
    private void initializeSWCL() {       

	        // access to the ontologies, reasoners, search renderings, change management etc.
	        owlModelManager = getOWLModelManager();
	        
	        // get workspace
	        ow = getOWLWorkspace();
	        
			// get selected class from workspace
			oc = ow.getOWLSelectionModel().getLastSelectedClass();
			
			// get ontology
			owl = owlModelManager.getActiveOntology();
			
	    	// new SWCL ontology helper
	    	soh = new SWCLOntologyHelper(owl);
	    	
	    	// prefix 
	    	prefix = soh.getPrefix();
	    	
	    	// get all variables to variablesList
	    	getAllVariables();
	    	
	    	// get all constraints to constraintsList
	    	getAllConstraints();
	    	
	    	// insert all constraints to SWCl tab
	    	DefaultTableModel tableModel = (DefaultTableModel) constraintsTable.getModel();
	    	for(Constraint c:constraintsList){
	    		Utils.printConstraint(c);
	    		System.out.println(Utils.getSWCLAbstractSyntax(variablesList, c));
	    	}
	    	

	}

	private void getAllVariables() {
		try {   
			ArrayList<Constraint> constraintsList = new ArrayList<Constraint>();
	    	
	    	Iterator it = null;
			
			// create Variable class
			OWLClassImpl variableCls = new OWLClassImpl(getOWLDataFactory(),IRI.create(prefix + "#Variable"));
			
			// temporary save at current directory as manchester owl syntax
			String presentDir = System.getProperty("user.dir");
			WriterDocumentTarget wdt = new WriterDocumentTarget(new FileWriter(presentDir + "//temp1.owl"));
	
			OWLOntologyManager manager = owl.getOWLOntologyManager();
			
	//		manager.saveOntology(owl,new SystemOutDocumentTarget());
			manager.saveOntology(owl, new ManchesterOWLSyntaxOntologyFormat(), wdt);
			
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
				Set variablesSet = variableCls.getIndividuals(owl);
				it = variablesSet.iterator();
				while(it.hasNext()){
					OWLIndividual ind = (OWLIndividual) it.next();
					String indName = soh.getIndividualName(ind);
	//System.out.println("indName:"+indName);
					Variable v = new Variable(indName,"");
	//System.out.println("Class: " + "<" + prefix + "#ClassFor"+indName + ">");
					if(readLine.contains("Class: " + "<" + prefix + "#ClassFor"+indName + ">")){
						readLine = br.readLine();
						readLine = br.readLine();
						readLine = br.readLine();
	//System.out.println("1:"+readLine);
						readLine = readLine.replaceAll("        ", "");
						readLine = readLine.replaceAll("<"+prefix+"#","");
						readLine = readLine.replaceAll(">","");
	//System.out.println("2:"+readLine);
						
						v.setDescription(readLine);
						variablesList.add(v);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
			
	}
	
	// NEED TO UPDATE..
	private void getAllConstraints() {
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();
		
		// get all constraints
		OWLClassImpl constraintCls = new OWLClassImpl(getOWLDataFactory(),IRI.create(prefix + "#Constraint"));
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
			
			// MEED UPDATE...
			// get LHS to constraint
			OWLObjectPropertyImpl hasLhs = new OWLObjectPropertyImpl(factory, IRI.create(prefix+"#hasLhs"));
			Set lhsSet = (Set) indObjProperty.get(hasLhs);
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
System.out.println("LHS signStr is:"+signStr);
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
System.out.println("LHS aggStr is:"+aggStr);
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
System.out.println("LHS parameter is:" + parStr);
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
System.out.println("RHS hasBindingDataProperty:"+hasBDPStr);
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
System.out.println("LHS var is:" + hasVarStr);
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
System.out.println("RHS aggStr is:"+aggStr);
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
System.out.println("RHS parameter is:" + parStr);
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
System.out.println("RHS hasBindingDataProperty:"+hasBDPStr);
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
System.out.println("RHS var is:" + hasVarStr);
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
			
			// add con to consList
			constraintsList.add(con);
		}
	}
	
	// initialize the menu panel
    private JPanel getMenuPanel(){
    	
    	if(menuPanel == null){
    		
    	   menuPanel = new JPanel();
    	   menuPanel.setLayout(new BorderLayout());
    	   menuPanel.setPreferredSize(new Dimension(0,20));
    	   menuPanel.add(getButtonsPanel(),BorderLayout.EAST);
    	   
    	}
    	
    	return menuPanel;
    	
    }
    
    // initialize the buttons panel
    private JPanel getButtonsPanel(){
    	
    	if(buttonsPanel == null){
    		
    		buttonsPanel = new JPanel();
    		buttonsPanel.setLayout(new GridLayout());
    		buttonsPanel.setPreferredSize(new Dimension(60,0));
    		buttonsPanel.add(getAddConstraintButton());
    		buttonsPanel.add(getGenerateSWCLButton());
    	}
    	
    	return buttonsPanel;
    	
    }
    
	// initialize the add constraint button
	private JButton getAddConstraintButton(){
		
		if(addConstraintButton == null){
			
			addConstraintButton = new JButton();
			addConstraintButton.setText("+");
			addConstraintButton.addActionListener(this);// add event listener
		}
		
		return addConstraintButton;
		
	}
	
	// initialize the generate SWCL code button
	private JButton getGenerateSWCLButton(){
		
		if(generateSWCLButton == null){
			
			generateSWCLButton = new JButton();
			generateSWCLButton.setText("D");
			generateSWCLButton.addActionListener(this);// add event listener
			
		}
		
		return generateSWCLButton;
	}

    // initialize the constraints panel
    private JPanel getConstraintsPanel(){
    	
    	if(constraintsPanel == null){
    		
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			constraintsPanel = new JPanel();
			constraintsPanel.setLayout(new GridBagLayout());
			constraintsPanel.add(getConstraintsScrollPane(), gridBagConstraints);
			
    	}
    	
    	return constraintsPanel;
    	
    }
  
    // initialize the constraints scroll pane
    private JScrollPane getConstraintsScrollPane(){
    	if(constraintsScrollPane == null){
    	   constraintsScrollPane = new JScrollPane();
    	   constraintsScrollPane.setViewportView(getConstraintsTable());
    	}
    	return constraintsScrollPane;
    }
   
    // initialize the constraints table
	private JTable getConstraintsTable() {
		
		if (constraintsTable == null) {
			// Initialize column headings
			final String[] colHeads = {"Enabled","Constraint Name", "Constraint"};
			// Initialize data with null
			final Object[][] data = null;
			
			DefaultTableModel model = new DefaultTableModel(data,colHeads);
			constraintsTable = new JTable(model);
			
			TableColumn tableColumn = constraintsTable.getColumn("Enabled");
			tableColumn.setMaxWidth(50);
			tableColumn.setMinWidth(50);
			tableColumn.setCellEditor(new CheckButtonEditor(new JCheckBox()));
			tableColumn.setCellRenderer(new CheckBoxRenderer());// renderer for displaying the checkbox component in the cell
			
		}
		
		return constraintsTable;
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		DefaultTableModel tableModel = (DefaultTableModel) constraintsTable.getModel();
		
		// the event of clicking the + button, add one row to constraints table
		if(e.getActionCommand().equals("+")){
			// set alignment of jcheckbox to center
			JCheckBox jb = new JCheckBox();
			jb.setHorizontalAlignment(SwingConstants.CENTER);
			tableModel.addRow(new Object[]{jb,"",""});
			
			// create add constraint component
			AddConstraintsComponent acc = new AddConstraintsComponent(ow,owlModelManager,variablesList, tableModel);
			acc.setVisible(true);
		}
		
		// the event of clicking the G button, generate the SWCL code
		if(e.getActionCommand().equals("D")){
			int rowCount = tableModel.getRowCount();// =no. of constraints 
			
			for(int i=0;i<rowCount;i++){
				JCheckBox jcb = (JCheckBox) tableModel.getValueAt(i, 0);

				if(jcb.isSelected()){
					// delete selected row
					tableModel.removeRow(i);
					// NEED UPDATE... delete info in the ontology
					
				}
				
			}
			
			
		}
		
	}

}