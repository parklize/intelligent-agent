package org.protege.editor.owl.swcl.view;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.SystemColor;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.swcl.controller.ConstraintController;
import org.protege.editor.owl.swcl.controller.SWCLOntologyController;
import org.protege.editor.owl.swcl.model.Factor;
import org.protege.editor.owl.swcl.model.Objective;
import org.protege.editor.owl.swcl.model.OptModel;
import org.protege.editor.owl.swcl.model.Parameter;
import org.protege.editor.owl.swcl.model.TermBlock;
import org.protege.editor.owl.swcl.model.Variable;
import org.protege.editor.owl.swcl.utils.SolverCodeTranslator;
import org.protege.editor.owl.swcl.utils.Utils;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.OWLEntityRemover;

import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;
/**
 * @author parklize
 * @version 1.0, 2011-04-25
 */
public class ObjectiveViewComponent extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	private int termblockNumber = 0;
	private JLabel jLabel = null;
	private JComboBox instructions = null;
	private JLabel jLabel1 = null;
	private JLabel variableLabel = null;
	private JScrollPane termScrollPane = null;
	private JPanel termPanel = null;
	private JPanel variablePanel = null;  
	private JScrollPane variableScrollPane = null;
	private JScrollPane variablesScrollPane = null;
	private JTable variablesTable = null;
	private JButton addVariableButton = null;
	private JButton removeVariableButton = null;
	private TermBlockComponent[] objectiveTermblocks = new TermBlockComponent[100];
	private JButton jButton = null;
//	private TableColumn qualifierVariable = null;
	
	private ArrayList<Variable> totalVariablesList = new ArrayList<Variable>();
	private ArrayList<Variable> variablesList = new ArrayList<Variable>();  //  @jve:decl-index=0:
	private OWLOntology ont = null;  //  @jve:decl-index=0:
	private JButton confirmButton = null;
	private TermBlockComponent[] rhsTermblocks = new TermBlockComponent[100];
	private TermBlockComponent[] lhsTermblocks = new TermBlockComponent[100];
	
	private ConstraintController con = null;  
	private OWLOntologyManager manager = OWLManager.createOWLOntologyManager(); 
	private OWLEntityRemover remover = null;
	private OWLDataFactory dataFactory = null;
	private SWCLOntologyController soc = null;
	private ConstraintController controller = null;  //controllor  //  @jve:decl-index=0:



	/**
	 * This is the default constructor
	 */
	public ObjectiveViewComponent(ArrayList<Variable> variableList, OWLModelManager owlModelManager, ConstraintController con) {
		super();
		initialize(variableList, owlModelManager, con);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize(ArrayList<Variable> totalVariablesList, OWLModelManager owlModelManager, ConstraintController con) {
		
		this.totalVariablesList = totalVariablesList;
		this.variablesList = (ArrayList<Variable>) totalVariablesList.clone();
		this.ont = owlModelManager.getActiveOntology();
		this.con = con;
		this.remover = new OWLEntityRemover(this.manager,Collections.singleton(this.ont));
		this.dataFactory = manager.getOWLDataFactory();
		this.soc = new SWCLOntologyController(ont);
		
		// get controller
		this.controller = new ConstraintController(owlModelManager, soc);

//		if(ont == null){
//			System.out.println("ont is null");
//		}else{
//			System.out.println("ont is not null");
//		}
	
		variableLabel = new JLabel();
		variableLabel.setBounds(new Rectangle(18, 10, 141, 18));
		variableLabel.setText("VARIABLE:");
		jLabel1 = new JLabel();
		jLabel1.setBounds(new Rectangle(18, 198, 141, 18));
		jLabel1.setText("OBJECTIVE TERM:");
		jLabel = new JLabel();
		jLabel.setBounds(new Rectangle(18, 150, 150, 18));
		jLabel.setText("OPTIMIZATION INSTRUCTION:");
		this.setSize(897, 600);
		this.setLayout(null);
		this.add(variableLabel,null);
		this.add(jLabel, null);
		this.add(jLabel1, null);
		this.add(getVariableScrollPane(),null);
		this.add(getInstructionComboBox(), null);
		this.add(getRhsScrollPane(),null);
		this.add(getJButton(), null);
		this.add(getConfirmButton(), null);
	}

	// variableScrollPane
	private JScrollPane getVariableScrollPane() {
		if (variableScrollPane == null) {
			variableScrollPane = new JScrollPane();
			variableScrollPane.setBounds(new Rectangle(180, 10, 620, 120));
			variableScrollPane.setViewportView(getVariablePanel());
		}
		return variableScrollPane;
	}
	
	// variable panel
	private JPanel getVariablePanel() {
		if (variablePanel == null) {
			variablePanel = new JPanel();
			variablePanel.setLayout(null);
			variablePanel.add(getAddVariableButton());
			variablePanel.add(getRemoveVariableButton(), null);
			variablePanel.add(getVariablesScrollPane(), null);
		}
		return variablePanel;
	}
	
	
	// variables scrollpane
	private JScrollPane getVariablesScrollPane() {
		if (variablesScrollPane == null) {
			variablesScrollPane = new JScrollPane();
			variablesScrollPane.setBounds(new Rectangle(27, 8, 445, 103));
			variablesScrollPane.setViewportView(getVariablesTable());
		}
		return variablesScrollPane;
	}

	// variables table
	private JTable getVariablesTable() {
		
		if (variablesTable == null) {
			final String[] colHeads = {"Variable","Domain Description"};
			final String[][] data = null;
			
			DefaultTableModel model = new DefaultTableModel(data,colHeads);
			variablesTable = new JTable(model);
			
			// set variable width
			TableColumn tableColumn = variablesTable.getColumn("Variable");
			tableColumn.setMaxWidth(60);
			tableColumn.setMinWidth(60);
			
			// add variables from totalvariables list to display
			if(totalVariablesList.size() != 0){                                                         
				for(Variable v:totalVariablesList){
					model.addRow(new Object[]{v.getName(),v.getDescription()});
				}
			}
			
			// set jcombobox to third column
//			JComboBox jb = getClassesComboBox();
//			TableColumn propertyColumn = variablesTable.getColumnModel().getColumn(2);
//			propertyColumn.setCellEditor(new DefaultCellEditor(jb));
			

			/*
			 * cell value changed listener			
			 */
			variablesTable.getModel().addTableModelListener(new TableModelListener(){

				public void tableChanged(TableModelEvent e) {
										if(e.getType() == TableModelEvent.UPDATE){
						
						// get changed value
						String newValue = (String) variablesTable.getValueAt(e.getLastRow(),e.getColumn());
						
						if(e.getColumn() == 2){
							if(!newValue.equals("Direct Input")){
								variablesTable.setValueAt(newValue, e.getLastRow(), 1);
							}else{
								variablesTable.setValueAt("", e.getLastRow(), 1);
							}
						}
						
						// check rename variable
						boolean checVariable = false;
						for(Variable v:variablesList){
							if(v.getName().equals(newValue) && !newValue.equals("")){
								checVariable = true;
							}
						}
						
						if(checVariable){
							// show msg when no constraint selected
							JOptionPane.showMessageDialog (null, "Variable name exist!", "Wrong", JOptionPane.INFORMATION_MESSAGE);
							variablesTable.setValueAt("", e.getLastRow(), e.getColumn());
							
						}else{
							
							// set name if column 0 is changed, set description if column 1 is changed
							if(e.getColumn() == 0){
								variablesList.get(e.getLastRow()).setName(newValue);
							}else if(e.getColumn() == 1){
								variablesList.get(e.getLastRow()).setDescription(newValue);
							}
							
							// apply change to qualifierVariable table
	//						Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), qualifierVariable);
//							Utils.refreshComboBox(variablesList, qualifierVariable);
							
							// apply change to termblocks
							for(int i=0;i<100;i++){
								if(rhsTermblocks[i] != null){
	//								Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), rhsTermblocks[i].getParameterColumn());
	//								Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), rhsTermblocks[i].getFactorVariableColumn());
									Utils.refreshComboBox(variablesList, rhsTermblocks[i].getParameterColumn());
									Utils.refreshComboBox(variablesList, rhsTermblocks[i].getFactorVariableColumn());
								}
								if(lhsTermblocks[i] != null){
	//								Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), lhsTermblocks[i].getParameterColumn());
	//								Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), lhsTermblocks[i].getFactorVariableColumn());
									Utils.refreshComboBox(variablesList, lhsTermblocks[i].getParameterColumn());
									Utils.refreshComboBox(variablesList, lhsTermblocks[i].getFactorVariableColumn());
								}
							}				
						}
					}
				}
			});
		}
		return variablesTable;
	}
	
	// add variable button
	private JButton getAddVariableButton() {
		if (addVariableButton == null) {
			addVariableButton = new JButton();
			addVariableButton.setText("+");
			addVariableButton.setBounds(new Rectangle(482, 7, 25, 18));
			addVariableButton.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {

					((DefaultTableModel) variablesTable.getModel()).addRow(new Object[]{"","","Direct Input"});

					// add variable to variablesList
					Variable variable = new Variable("","");
					variablesList.add(variable);
					
					// apply change to qualifierVariable table
//					Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), qualifierVariable);
//					Utils.refreshComboBox(variablesList, qualifierVariable); // bug occurrence
					
					// apply change to termblocks
					for(int i=0;i<100;i++){
						if(objectiveTermblocks[i] != null){
							Utils.refreshComboBox(variablesList,objectiveTermblocks[i].getParameterColumn());
							Utils.refreshComboBox(variablesList,objectiveTermblocks[i].getFactorVariableColumn());
						}
					}
					
				}
			});
		}
		return addVariableButton; 
	}
	
	// remove variable button
	private JButton getRemoveVariableButton() {
		
		if (removeVariableButton == null) {
			removeVariableButton = new JButton();
			removeVariableButton.setText("-");
			removeVariableButton.setBounds(new Rectangle(512, 7, 25, 18));
			removeVariableButton.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {

					DefaultTableModel model = (DefaultTableModel) variablesTable.getModel();
					
					// remove variable to variablesList
					int index = model.getRowCount()-1;
					if(index < 0){
						// show msg when no constraint selected
						JOptionPane.showMessageDialog (null, "No variable to delete!", "Wrong", JOptionPane.INFORMATION_MESSAGE);
					}else{
						
						variablesList.remove(index);

						model.removeRow(index);
						
						// apply change to termblocks
						for(int i=0;i<100;i++){
							if(objectiveTermblocks[i] != null){
								Utils.refreshComboBox(variablesList,objectiveTermblocks[i].getParameterColumn());
								Utils.refreshComboBox(variablesList,objectiveTermblocks[i].getFactorVariableColumn());
							}
						}
					}
				}
			});
		}
		return removeVariableButton;
	}
	
	// get instruction combobox
	private JComboBox getInstructionComboBox() {
		if (instructions == null) {
			final String[] signs = {"Maximize","Minimize"};
			
			instructions = new JComboBox(signs);
			instructions.setBounds(new Rectangle(180, 150, 120, 23));
		}
		return instructions;
	}

	/*
	 * Objective Term part
	 */
	
	// Objective Term scroll pane
	private JScrollPane getRhsScrollPane() {
		if (termScrollPane == null) {
			termScrollPane = new JScrollPane();
			termScrollPane.setBounds(new Rectangle(180, 198, 620, 114));
			termScrollPane.setViewportView(getRhsPanel());
		}
		return termScrollPane;
	}
	
	// Objective Term panel
	private JPanel getRhsPanel() {
		if (termPanel == null) {
			termPanel = new JPanel();
			termPanel.setLayout(null);
		}
		return termPanel;
	}

	// Objective Term panel 
	private JPanel getRHSTermblockPanel(int termblockNumber) {

			objectiveTermblocks[termblockNumber] =  new TermBlockComponent(variablesList,ont);
			
			// termblock initializing...
			objectiveTermblocks[termblockNumber].setLayout(null);
			objectiveTermblocks[termblockNumber].setBounds(new Rectangle(0, 112*termblockNumber, 620, 112));
			objectiveTermblocks[termblockNumber].setBorder(BorderFactory.createLineBorder(SystemColor.activeCaptionBorder, 1));
			
			double rhsPanelHeight = termPanel.getPreferredSize().getHeight()+112;
			// resize rhsPanel
			termPanel.setPreferredSize(new Dimension(0,(int)rhsPanelHeight));
			// check scroll bar necessity 
			termScrollPane.revalidate();

		return objectiveTermblocks[termblockNumber];
	}

	// add term block button
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(823, 198, 34, 17));
			jButton.setText("+");
			jButton.addActionListener(this);
		}
		return jButton;
	}

	// confirm button
	private JButton getConfirmButton() {
		if (confirmButton == null) {
			confirmButton = new JButton();
			confirmButton.setBounds(new Rectangle(747, 342, 52, 22));
			confirmButton.setText("Confirm");
			confirmButton.addActionListener(this);
		}
		return confirmButton;
	}
	
	private OptModel getOptModel() {
		OptModel om = new OptModel();
		Objective obj = new Objective();
		// set instruction
		obj.setOptimizationInstruction(instructions.getSelectedItem().toString());
		
		// set termblocks
		for(int i=0;i<termblockNumber;i++){
			
			TermBlock tb = new TermBlock();
			String sign = objectiveTermblocks[i].getSignComboBox().getSelectedItem().toString();
			tb.setSign(sign);
			String agg = objectiveTermblocks[i].getAggOppComboBox().getSelectedItem().toString();
			tb.setAggregateOppertor(agg);
			// parameter list
			ArrayList<Parameter> pList = new ArrayList<Parameter>();
			JTable parameterTable = objectiveTermblocks[i].getParametersTable();
			DefaultTableModel tModel = (DefaultTableModel) parameterTable.getModel();
			
			for(int u=0;u<tModel.getRowCount();u++){
				String valName = (String)tModel.getValueAt(u, 0);
				Parameter p = new Parameter();
				p.setV(Utils.findVariableWithName(variablesList, valName));
				pList.add(p);
			}
			tb.setParameters(pList);
			
			// factoer list
			ArrayList<Factor> fList = new ArrayList<Factor>();
			JTable factorsTable = objectiveTermblocks[i].getFactorsTable();
			DefaultTableModel fModel = (DefaultTableModel)factorsTable.getModel();
			
			for(int q=0;q<fModel.getRowCount();q++){
				Factor f = new Factor();
				String vName = (String)fModel.getValueAt(q, 0);
				// NEED UPDATE (set Factor's OWLProperty)
				String pro = (String)fModel.getValueAt(q, 1);
				
				f.setV(Utils.findVariableWithName(variablesList, vName));
				f.setOwlProperty(pro);
				fList.add(f);
			}
			tb.setFactors(fList);
			obj.getObjectiveTerm().add(tb);
		}
		
		om.setObj(obj);
		return om;
	}
	
	// action event
	public void actionPerformed(ActionEvent e) {
		
		String actionCommand = e.getActionCommand();
		if(actionCommand.equals("+")){
			
			termPanel.add(getRHSTermblockPanel(termblockNumber),null);
			termPanel.repaint();
			termblockNumber++;
			
		}else if(actionCommand.equals("Confirm")){
			
			// write variables to ontology
			
			this.controller.deleteVariables();
			controller.writeVariablesToOnt(this.variablesList);
//Utils.printVariablesList("in ObjectiveViewComponent", variablesList);
			
			OptModel om = getOptModel();
			OWLClass obj = new OWLClassImpl(this.dataFactory, IRI.create(this.soc.getPrefix()+"#Objective"));
			Set objSet = obj.getIndividuals(ont);
			
			if(objSet != null){
				Iterator it = objSet.iterator();
				while(it.hasNext()){
					OWLNamedIndividual objective = (OWLNamedIndividual) it.next();
					objective.accept(remover);
				}
			}
			con.writeOptModelToOnt(om);
			
			SolverCodeTranslator sct = new SolverCodeTranslator();
			sct.translateSWCL(this.ont); // Eclipse Installed DIR/Ilog.txt·Î

		}
		
	}


}  //  @jve:decl-index=0:visual-constraint="13,-28"
