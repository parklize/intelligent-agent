package org.protege.editor.owl.swcl.view;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.OWLWorkspace;
import org.protege.editor.owl.swcl.controller.ConstraintController;
import org.protege.editor.owl.swcl.controller.SWCLOntologyController;
import org.protege.editor.owl.swcl.model.Constraint;
import org.protege.editor.owl.swcl.model.Factor;
import org.protege.editor.owl.swcl.model.LHS;
import org.protege.editor.owl.swcl.model.Operator;
import org.protege.editor.owl.swcl.model.Parameter;
import org.protege.editor.owl.swcl.model.Qualifier;
import org.protege.editor.owl.swcl.model.RHS;
import org.protege.editor.owl.swcl.model.TermBlock;
import org.protege.editor.owl.swcl.model.Variable;
import org.protege.editor.owl.swcl.utils.Utils;
import org.semanticweb.owlapi.model.OWLOntology;


import java.util.ArrayList;
import javax.swing.BorderFactory;
import java.awt.SystemColor;
import javax.swing.JTextArea;
/**
 * 
 * Author: parklize,pryiyeon
 * Date: 2011.04.25
 * Description: component for adding constraint
 *
 */
public class ModifyConstraintsComponent extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel constraintPanel = null;
	private JLabel operatorLabel = null;
	private JComboBox operatorComboBox = null;
	private JButton okButton = null;
	private JLabel qualifierLabel = null;
	private JScrollPane containerScrollPane = null;
	private JPanel containerPanel = null;
	private JPanel menuPanel = null;
	private JButton add = null;
	private JButton del = null;
	private JComboBox optionsComboBox = null;
	private JScrollPane lhsScrollPane = null;
	private JPanel lhsPanel = null;
	private JLabel lhsLable = null;
	private JLabel rhsLabel = null;
	private JScrollPane rhsScrollPane = null;
	private JPanel rhsPanel = null;
	private JScrollPane qualifierScrollPane = null;
	private JPanel qualifierPanel = null;
	private int lhsTermblockNumber = 0;
	private int rhsTermblockNumber = 0;
	private JLabel variableLabel = null;
	private JScrollPane variableScrollPane = null;
	private JPanel variablePanel = null;
	private JButton addVariableButton = null;
	private JButton removeVariableButton = null;
	private JScrollPane variablesScrollPane = null;
	private JTable variablesTable = null;
	private JScrollPane qualifiersScrollPane = null;
	private JTable qualifiersTable = null;
	private JButton addQualifierButton = null;
	private JButton removeQualifierButton = null;
	private TableColumn qualifierVariable = null;
    private DefaultTableModel tableModel = null;
	private JLabel constraintName = null;
	private JTextField constraintNameField = null;
	private JPanel abstractSyntaxPanel = null;
	private JLabel abstractSyntaxLabel = null;
	private JTextArea abstractSyntaxArea = null;
	private JButton submitButton = null;
	
	private TermBlockComponent[] rhsTermblocks = new TermBlockComponent[100];
	private TermBlockComponent[] lhsTermblocks = new TermBlockComponent[100];
	
	private ConstraintController controller = null;
	
/*
 * should be afforded from ExampleViewComponent
 */
	private ArrayList<Variable> totalVariablesList = new ArrayList<Variable>();  //  @jve:decl-index=0:
	private ArrayList<Variable> variablesList = new ArrayList<Variable>();  //  @jve:decl-index=0:
	private OWLOntology ont = null;  //  @jve:decl-index=0:
	private String abstractSyntax = null;
	private Constraint con = null;
	private Constraint oldCon = null;
	private ArrayList<Constraint> constraintList = null;
	
	// create ontology manager to work with
	private SWCLOntologyController soh = null;
	private JScrollPane wholeContainerScrollPane = null;
	private JPanel wholePanel = null;

	// initialing...
	public ModifyConstraintsComponent(Constraint con, OWLWorkspace ow, OWLModelManager owlModelManager, ArrayList<Variable> totalVariablesList, DefaultTableModel tableModel,  ArrayList<Constraint> constraintList) {
		super();
		preinitialize(con, ow, owlModelManager,totalVariablesList,tableModel,constraintList);
		initialize();
//		this.classExpressionTextPane = getClassExpressionPane();

	}
	
	private void preinitialize(Constraint con, OWLWorkspace ow, OWLModelManager owlModelManager, ArrayList<Variable> totalVariablesList, DefaultTableModel tableModel,ArrayList<Constraint> constraintList){
		 // get variables already announced
		this.totalVariablesList = totalVariablesList;
Utils.printVariablesList("total at first:", this.totalVariablesList);
		this.variablesList = (ArrayList<Variable>) totalVariablesList.clone();
		this.ont = owlModelManager.getActiveOntology();
		this.con = con;
		this.oldCon = con;
		this.tableModel = tableModel;
		this.soh = new SWCLOntologyController(ont);
		this.constraintList = constraintList;
		
		// set base
		this.controller = new ConstraintController(owlModelManager, soh);

	}
	
	// for test purpose
	public ModifyConstraintsComponent(){
		super();
		initialize();
	}
	
	// initializing frame
	private void initialize() {
		this.setSize(680, 770);
		this.setContentPane(getJContentPane());
		this.setTitle("SWCL");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		// set location of the frame in the window
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
	    int frameH=this.getHeight();
		int frameW=this.getWidth();
		this.setLocation((screenWidth-frameW) / 2, (screenHeight-frameH) / 2);
	}

	// jContentPane,default
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getWholeContrainerScrollPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}
	
	// whole scroll pane
	private JScrollPane getWholeContrainerScrollPane(){
		if(wholeContainerScrollPane == null){
			wholeContainerScrollPane = new JScrollPane();
			wholeContainerScrollPane.setViewportView(getConainerPane());
			wholeContainerScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			wholeContainerScrollPane.revalidate();
		}
		return wholeContainerScrollPane;
	}
	
	// whole panel
	private JPanel getConainerPane() {
		if (wholePanel == null) {
			wholePanel = new JPanel();
			wholePanel.setPreferredSize(new Dimension(650,950));
			wholePanel.setLayout(null);
			wholePanel.add(getJScrollPane(), null);
			wholePanel.add(getAbstractSyntaxPanel(), null);
		}
		return wholePanel;
	}
	
	// content Scroll pane
	private JScrollPane getJScrollPane() {
		if (containerScrollPane == null) {
			containerScrollPane = new JScrollPane();
			containerScrollPane.setBounds(new Rectangle(0, 0, 675, 750));
			containerScrollPane.setViewportView(getContainerPanel());
		}
		return containerScrollPane;
	}
	
	// containter panel
	private JPanel getContainerPanel() {
		if (containerPanel == null) {
			containerPanel = new JPanel();
			containerPanel.setLayout(new BorderLayout());
			containerPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			containerPanel.add(getMenuPanel(), BorderLayout.SOUTH);
			containerPanel.add(getJPanel(), BorderLayout.CENTER);
		}
		return containerPanel;
	}
	
	// constraint Panel
	private JPanel getJPanel() {
		if (constraintPanel == null) {
			constraintName = new JLabel();
			constraintName.setBounds(new Rectangle(33, 17, 66, 18));
			constraintName.setText("NAME:");
			variableLabel = new JLabel();
			variableLabel.setBounds(new Rectangle(33, 60, 66, 18));
			variableLabel.setText("VARIABLE:");
			rhsLabel = new JLabel();
			rhsLabel.setBounds(new Rectangle(33, 570, 60, 18));
			rhsLabel.setText("RHS:");
			lhsLable = new JLabel();
			lhsLable.setBounds(new Rectangle(33, 400, 60, 18));
			lhsLable.setText("LHS:");
			qualifierLabel = new JLabel();
			qualifierLabel.setBounds(new Rectangle(33, 230, 75, 18));
			qualifierLabel.setText("QUALIFIER:");
			operatorLabel = new JLabel();
			operatorLabel.setBounds(new Rectangle(33, 530, 75, 18));
			operatorLabel.setText("OPERATOR:");
			constraintPanel = new JPanel();
			constraintPanel.setLayout(null);
			constraintPanel.add(operatorLabel, null);
			constraintPanel.add(getOperatorComboBox(), null);
			constraintPanel.add(qualifierLabel, null);
			constraintPanel.add(getLhsScrollPane(), null);
			constraintPanel.add(lhsLable, null);
			constraintPanel.add(rhsLabel, null);
			constraintPanel.add(getRhsScrollPane(), null);
			constraintPanel.add(getQualifierScrollPane(), null);
			constraintPanel.add(variableLabel, null);
			constraintPanel.add(getVariableScrollPane(), null);
			constraintPanel.add(constraintName, null);
			constraintPanel.add(getJTextField(), null);
		}
		return constraintPanel;
	}
	
	// constraint name field
	private JTextField getJTextField() {
		
		if (constraintNameField == null) {
			constraintNameField = new JTextField();
			constraintNameField.setBounds(new Rectangle(141, 17, 211, 18));
			constraintNameField.setText(con.getName());
		}
		
		return constraintNameField;
	}
	
	// variableScrollPane
	private JScrollPane getVariableScrollPane() {
		if (variableScrollPane == null) {
			variableScrollPane = new JScrollPane();
			variableScrollPane.setBounds(new Rectangle(142, 60, 500, 120));
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
			variablePanel.add(getRemoveVariableButton());
			variablePanel.add(getVariablesScrollPane(), null);
//			variablePanel.add(getJPanel2(), null);
//			variablePanel.add(getClassExpressionApplyButton(), null);
		}
		return variablePanel;
	}
	
	// variables scrollpane
	private JScrollPane getVariablesScrollPane() {
		if (variablesScrollPane == null) {
			variablesScrollPane = new JScrollPane();
			variablesScrollPane.setBounds(new Rectangle(27, 8, 385, 103));
			variablesScrollPane.setViewportView(getVariablesTable());
		}
		return variablesScrollPane;
	}

	// variables table
	private JTable getVariablesTable() {
		if (variablesTable == null) {
			final String[] colHeads = {"Variable","Description"};
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

			/*
			 * cell value changed listener			
			 */
			variablesTable.getModel().addTableModelListener(new TableModelListener(){

				public void tableChanged(TableModelEvent e) {
					
					if(e.getType() == TableModelEvent.UPDATE){
						
						// get changed value
						String newValue = (String) variablesTable.getValueAt(e.getLastRow(),e.getColumn());
						
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
							Utils.refreshComboBox(variablesList, qualifierVariable);
							
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
/*	
	// class expression panel
	private JPanel getJPanel2() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
			jPanel.setBounds(new Rectangle(160, 14, 226, 126));
	        jPanel.add(getClassExpressionComponent());
	        jPanel.setVisible(false);
		}
		return jPanel;
	}
	*/
/*	
	// get class expression component
	private JComponent getClassExpressionComponent(){
		
		com = ocfe.getOWLClassDescriptionEditor(oc).getEditorComponent();
		
		return com;
	}
	// get class expression text pane
	private JTextPane getClassExpressionPane(){
		
		JTabbedPane tp = (JTabbedPane) com.getComponent(0);
		JScrollPane jsp = (JScrollPane) tp.getComponentAt(0);
		return (JTextPane) jsp.getViewport().getComponent(0);
		
	}
*/
	
	// add variable button
	private JButton getAddVariableButton() {
		if (addVariableButton == null) {
			addVariableButton = new JButton();
			addVariableButton.setText("+");
			addVariableButton.setBounds(new Rectangle(422, 7, 25, 18));
			addVariableButton.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {

					((DefaultTableModel) variablesTable.getModel()).addRow(new Object[]{"",""});

					// add variable to variablesList
					Variable variable = new Variable("","");
					variablesList.add(variable);
					
					// apply change to qualifierVariable table
//					Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), qualifierVariable);
					Utils.refreshComboBox(variablesList, qualifierVariable);
					
					// apply change to termblocks
					for(int i=0;i<100;i++){
						if(rhsTermblocks[i] != null){
//							Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), rhsTermblocks[i].getParameterColumn());
//							Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), rhsTermblocks[i].getFactorVariableColumn());
							Utils.refreshComboBox(variablesList,rhsTermblocks[i].getParameterColumn());
							Utils.refreshComboBox(variablesList,rhsTermblocks[i].getFactorVariableColumn());
						}
						if(lhsTermblocks[i] != null){
//							Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), lhsTermblocks[i].getParameterColumn());
//							Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), lhsTermblocks[i].getFactorVariableColumn());
							Utils.refreshComboBox(variablesList,lhsTermblocks[i].getParameterColumn());
							Utils.refreshComboBox(variablesList,lhsTermblocks[i].getFactorVariableColumn());
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
			removeVariableButton.setBounds(new Rectangle(452, 7, 25, 18));
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
	
						// apply change to qualifierVariable table
	//					Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), qualifierVariable);
						Utils.refreshComboBox(variablesList, qualifierVariable);
						
						// apply change to termblocks
						for(int i=0;i<100;i++){
							if(rhsTermblocks[i] != null){
	//							Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), rhsTermblocks[i].getParameterColumn());
	//							Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), rhsTermblocks[i].getFactorVariableColumn());
								Utils.refreshComboBox(variablesList,rhsTermblocks[i].getParameterColumn());
								Utils.refreshComboBox(variablesList,rhsTermblocks[i].getFactorVariableColumn());
							}
							if(lhsTermblocks[i] != null){
	//							Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), lhsTermblocks[i].getParameterColumn());
	//							Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), lhsTermblocks[i].getFactorVariableColumn());
								Utils.refreshComboBox(variablesList,lhsTermblocks[i].getParameterColumn());
								Utils.refreshComboBox(variablesList,lhsTermblocks[i].getFactorVariableColumn());
							}
						}
					}
				}
			});
		}
		return removeVariableButton;
	}
/*	
	// class expression change apply button
	private JButton getClassExpressionApplyButton() {
		if (classExpressionApplyButton == null) {
			classExpressionApplyButton = new JButton();
			classExpressionApplyButton.setBounds(new Rectangle(396, 122, 65, 17));
			classExpressionApplyButton.setText("Apply");
			classExpressionApplyButton.addActionListener(this);
			classExpressionApplyButton.setVisible(false);
		}
		return classExpressionApplyButton;
	}
*/	
	// operator Combobox
	private JComboBox getOperatorComboBox() {
		if (operatorComboBox == null) {
			final String[] operatorList = {"equal","notEqual","lessThan","lessThanOrEqual","greaterThan","greaterThanOrEqual"};
			operatorComboBox = new JComboBox(operatorList);
			operatorComboBox.setBounds(new Rectangle(140, 530, 140, 23));
			operatorComboBox.setSelectedItem(con.getOpp().getOpp());
		}
		return operatorComboBox;
	}
	
	// menu panel
	private JPanel getMenuPanel() {
		if (menuPanel == null) {
			menuPanel = new JPanel();
			menuPanel.setLayout(null);
			menuPanel.setPreferredSize(new Dimension(0,50));
			menuPanel.add(getOkButton(), null);
			menuPanel.add(getSubmitButton(),null);
			menuPanel.add(getADD(), null);
			menuPanel.add(getDEL(), null);
			menuPanel.add(getOptionsComboBox(), null);
		}
		return menuPanel;
	}

	// abstract syntax panel
	private JPanel getAbstractSyntaxPanel() {
		if (abstractSyntaxPanel == null) {
			abstractSyntaxLabel = new JLabel();
			abstractSyntaxLabel.setText("Abstract Syntax:");
			abstractSyntaxLabel.setBounds(new Rectangle(33, 20, 97, 18));
			abstractSyntaxPanel = new JPanel();
			abstractSyntaxPanel.setLayout(null);
			abstractSyntaxPanel.setBounds(new Rectangle(0, 749, 673, 180));
			abstractSyntaxPanel.add(abstractSyntaxLabel, null);
			abstractSyntaxPanel.add(getJTextArea(), null);
//			abstractSyntaxPanel.add(getJButton(), null);
		}
		return abstractSyntaxPanel;
	}

	// abstract syntax area
	private JTextArea getJTextArea() {
		if (abstractSyntaxArea == null) {
			abstractSyntaxArea = new JTextArea();
			abstractSyntaxArea.setLineWrap(true);
			abstractSyntaxArea.setBounds(new Rectangle(144, 18, 501, 112));
		}
		return abstractSyntaxArea;
	}

	// submit button
	private JButton getSubmitButton() {
		if (submitButton == null) {
			submitButton = new JButton();
			submitButton.setBounds(new Rectangle(592, 9, 51, 23));
			submitButton.setText("Submit");
			submitButton.addActionListener(this);
		}
		return submitButton;
	}
	
	// Add Button
	private JButton getADD() {
		if (add == null) {
			add = new JButton();
			add.setBounds(new Rectangle(190, 10, 69, 23));
			add.setText("ADD");
			add.addActionListener(this);
		}
		return add;
	}
	
	// Del Button
	private JButton getDEL() {
		if (del == null) {
			del = new JButton();
			del.setBounds(new Rectangle(267, 10, 69, 23));
			del.setText("DEL");
			del.addActionListener(this);
		}
		return del;
	}
	
	// OK button
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText("OK");
			okButton.setBounds(new Rectangle(532, 9, 51, 23));
			okButton.addActionListener(this);
		}
		return okButton;
	}
	
	// options
	private JComboBox getOptionsComboBox() {
		if (optionsComboBox == null) {
			final String[] options = {"LHS Termblock","RHS Termblock"};
			optionsComboBox = new JComboBox(options);
			optionsComboBox.setBounds(new Rectangle(27, 10, 119, 23));
		}
		return optionsComboBox;
	}
	
	/*
	 * LHS part
	 */
	
	// LHS Scrollpane
	private JScrollPane getLhsScrollPane() {
		if (lhsScrollPane == null) {
			lhsScrollPane = new JScrollPane();
			lhsScrollPane.setBounds(new Rectangle(142, 400, 500, 114));
			lhsScrollPane.setViewportView(getLhsPanel());
			
			// add lhs termblocks from constraint
			ArrayList<TermBlock> lhsTBList = con.getLhs().getTermblocks();
			
			for(TermBlock tb:lhsTBList){
				
				TermBlockComponent tbc = (TermBlockComponent) getLHSTermblockPanel(lhsTermblockNumber);
				
				// set sign
				tbc.getSignComboBox().setSelectedItem(tb.getSign());
				
				// set agg 
				tbc.getAggOppComboBox().setSelectedItem(tb.getAggregateOppertor());
				
				// set Parameter
				DefaultTableModel dtmPar = (DefaultTableModel) tbc.getParametersTable().getModel();
				ArrayList<Parameter> parList = tb.getParameters();
				if(parList != null){
					for(Parameter p:parList){
						dtmPar.addRow(new Object[]{p.getV().getName()});
					}
				}
				
				// set Factor
				DefaultTableModel dtmFactor = (DefaultTableModel) tbc.getFactorsTable().getModel();
				ArrayList<Factor> facList = tb.getFactors();
				if(facList != null){
					for(Factor f:facList){
						dtmFactor.addRow(new Object[]{f.getV().getName(),f.getOwlProperty()});
					}
				}
				
				lhsPanel.add(tbc,null);
				lhsPanel.repaint();
				lhsTermblockNumber++;

			}
			
		}
		return lhsScrollPane;
	}

	// LHS panel
	private JPanel getLhsPanel() {
		if (lhsPanel == null) {
			lhsPanel = new JPanel();
			lhsPanel.setLayout(null);
		}
		return lhsPanel;
	}
	
	// LHS Termblock panel 
	private JPanel getLHSTermblockPanel(int lhsTermblockNumber) {

			lhsTermblocks[lhsTermblockNumber] =  new TermBlockComponent(variablesList,ont);
			
			// termblock initializing...
			lhsTermblocks[lhsTermblockNumber].setLayout(null);
			lhsTermblocks[lhsTermblockNumber].setBounds(new Rectangle(0, 112*lhsTermblockNumber, 500, 112));
			lhsTermblocks[lhsTermblockNumber].setBorder(BorderFactory.createLineBorder(SystemColor.activeCaptionBorder, 1));
			
			double lhsPanelHeight = lhsPanel.getPreferredSize().getHeight()+112;
			// resize rhsPanel
			lhsPanel.setPreferredSize(new Dimension(0,(int)lhsPanelHeight));
			// check scroll bar necessity 
			lhsScrollPane.revalidate();

		return lhsTermblocks[lhsTermblockNumber];
	}
	
	/*
	 * qualifier part
	 */
	private JScrollPane getQualifierScrollPane() {
		if (qualifierScrollPane == null) {
			qualifierScrollPane = new JScrollPane();
			qualifierScrollPane.setBounds(new Rectangle(142, 230, 500, 120));
			qualifierScrollPane.setViewportView(getQualifierPanel());
		}
		return qualifierScrollPane;
	}

	// Qualifier panel for all qualifiers
	private JPanel getQualifierPanel() {
		if (qualifierPanel == null) {
			qualifierPanel = new JPanel();
			qualifierPanel.setLayout(null);

			qualifierPanel.add(getQualifiersScrollPane(), null);
			qualifierPanel.add(getAddQualifierButton(), null);
			qualifierPanel.add(getRemoveQualifierButton(), null);
		}
		return qualifierPanel;
	}
	
	// qualifiers scrollpane
	private JScrollPane getQualifiersScrollPane() {
		if (qualifiersScrollPane == null) {
			qualifiersScrollPane = new JScrollPane();
			qualifiersScrollPane.setBounds(new Rectangle(23, 7, 105, 97));
			qualifiersScrollPane.setViewportView(getQualifiersTable());
		}
		return qualifiersScrollPane;
	}

	// qualifiers table
	private JTable getQualifiersTable() {
		if (qualifiersTable == null) {
			final String[] colHeads = {"Variable"};
			final String[][] data = null;
			
			DefaultTableModel model = new DefaultTableModel(data,colHeads);
			qualifiersTable = new JTable(model);
			qualifierVariable = qualifiersTable.getColumnModel().getColumn(0);
			// refresh variable combobox
//			Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), qualifierVariable);
			Utils.refreshComboBox(variablesList, qualifierVariable);
			
			// get qualifiers from constraint
			ArrayList<Qualifier> quaList = con.getQualifiers();
			for(Qualifier q:quaList){
				model.addRow(new Object[]{q.getV().getName()});
			}
		}
		return qualifiersTable;
	}

	// add qualifier button
	private JButton getAddQualifierButton() {
		if (addQualifierButton == null) {
			addQualifierButton = new JButton();
			addQualifierButton.setBounds(new Rectangle(138, 8, 25, 18));
			addQualifierButton.setText("+");
			addQualifierButton.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					
					DefaultTableModel model = (DefaultTableModel) qualifiersTable.getModel();
					model.addRow(new Object[]{Utils.sumArrayList(totalVariablesList, variablesList).get(0).getName()});
					
				}
				
			});
		}
		return addQualifierButton;
	}
	
	// remove qualifier button
	private JButton getRemoveQualifierButton() {
		
		if (removeQualifierButton == null) {
			
			removeQualifierButton = new JButton();
			removeQualifierButton.setBounds(new Rectangle(168, 8, 25, 18));
			removeQualifierButton.setText("-");
			removeQualifierButton.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					
					DefaultTableModel model = (DefaultTableModel) qualifiersTable.getModel();
					int index = model.getRowCount()-1;
					if(index < 0){
						// show msg when no constraint selected
						JOptionPane.showMessageDialog (null, "No qualifier to delete!", "Wrong", JOptionPane.INFORMATION_MESSAGE);
					}else{
						model.removeRow(index);
					}
				}
				
			});
		}
		return removeQualifierButton;
	}
	/*
	 * RHS part
	 */
	
	// RHS scroll pane
	private JScrollPane getRhsScrollPane() {
		if (rhsScrollPane == null) {
			rhsScrollPane = new JScrollPane();
			rhsScrollPane.setBounds(new Rectangle(141, 570, 500, 114));
			rhsScrollPane.setViewportView(getRhsPanel());
			
			// add rhs termblocks from constraint
			ArrayList<TermBlock> rhsTBList = con.getRhs().getTermblocks();
			
			for(TermBlock tb:rhsTBList){
				
				TermBlockComponent tbc = (TermBlockComponent) getRHSTermblockPanel(rhsTermblockNumber);
				
				// set sign
				tbc.getSignComboBox().setSelectedItem(tb.getSign());
				
				// set agg 
				tbc.getAggOppComboBox().setSelectedItem(tb.getAggregateOppertor());
				
				// set Parameter
				DefaultTableModel dtmPar = (DefaultTableModel) tbc.getParametersTable().getModel();
				ArrayList<Parameter> parList = tb.getParameters();
				if(parList != null){
					for(Parameter p:parList){
						dtmPar.addRow(new Object[]{p.getV().getName()});
					}
				}
				
				// set Factor
				DefaultTableModel dtmFactor = (DefaultTableModel) tbc.getFactorsTable().getModel();
				ArrayList<Factor> facList = tb.getFactors();
				if(facList != null){
					for(Factor f:facList){
						dtmFactor.addRow(new Object[]{f.getV().getName(),f.getOwlProperty()});
					}
				}
				
				rhsPanel.add(tbc,null);
				rhsPanel.repaint();
				rhsTermblockNumber++;

			}
			
		}
		return rhsScrollPane;
	}
	
	// RHS panel
	private JPanel getRhsPanel() {
		if (rhsPanel == null) {
			rhsPanel = new JPanel();
			rhsPanel.setLayout(null);
		}
		return rhsPanel;
	}

	// RHS Termblock panel 
	private JPanel getRHSTermblockPanel(int rhsTermblockNumber) {

			rhsTermblocks[rhsTermblockNumber] =  new TermBlockComponent(variablesList,ont);
			
			// termblock initializing...
			rhsTermblocks[rhsTermblockNumber].setLayout(null);
			rhsTermblocks[rhsTermblockNumber].setBounds(new Rectangle(0, 112*rhsTermblockNumber, 500, 112));
			rhsTermblocks[rhsTermblockNumber].setBorder(BorderFactory.createLineBorder(SystemColor.activeCaptionBorder, 1));
			
			double rhsPanelHeight = rhsPanel.getPreferredSize().getHeight()+112;
			// resize rhsPanel
			rhsPanel.setPreferredSize(new Dimension(0,(int)rhsPanelHeight));
			// check scroll bar necessity 
			rhsScrollPane.revalidate();

		return rhsTermblocks[rhsTermblockNumber];
	}
	
	/*
	 * utils for current GUI
	 */
	// get constraints from GUI
	private Constraint getConstraint(){
		// new and initialize constraint
		Constraint con = new Constraint();
		
		// set name
		con.setName(constraintNameField.getText());
		
		// set qualifier
		DefaultTableModel tableModel = (DefaultTableModel)qualifiersTable.getModel();
		
		for(int i=0;i<tableModel.getRowCount();i++){
			String valName = (String)tableModel.getValueAt(i, 0);
			for(Variable v:variablesList){
				if(valName.equals(v.getName())){
					Qualifier qua = new Qualifier(v);
					con.getQualifiers().add(qua);
				}
			}
		}
		
		//============set LHS===========
		LHS lhs = new LHS();
		
		for(int j=0;j<lhsTermblockNumber;j++){
			TermBlock tb = new TermBlock();
//System.out.println(j);
			String sign = lhsTermblocks[j].getSignComboBox().getSelectedItem().toString();
			tb.setSign(sign);
			
//System.out.println("sign is :" + sign);
			String agg = lhsTermblocks[j].getAggOppComboBox().getSelectedItem().toString();
			tb.setAggregateOppertor(agg);
//System.out.println("agg is:" + agg);
			
			// parameter list
			ArrayList<Parameter> pList = new ArrayList<Parameter>();
			JTable parameterTable = lhsTermblocks[j].getParametersTable();
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
			JTable factorsTable = lhsTermblocks[j].getFactorsTable();
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
			
			lhs.getTermblocks().add(tb);
		}
		con.setLhs(lhs);
		//==================end of set LHS==============
		
		// set opp
		con.setOpp(new Operator(operatorComboBox.getSelectedItem().toString()));
		
		//============set RHS===========
		RHS rhs = new RHS();
		
		for(int j=0;j<rhsTermblockNumber;j++){
			TermBlock tb = new TermBlock();
//System.out.println(j);
			String sign = rhsTermblocks[j].getSignComboBox().getSelectedItem().toString();
			tb.setSign(sign);
			
//System.out.println("sign is :" + sign);
			String agg = rhsTermblocks[j].getAggOppComboBox().getSelectedItem().toString();
			tb.setAggregateOppertor(agg);
//System.out.println("agg is:" + agg);
			
			// parameter list
			ArrayList<Parameter> pList = new ArrayList<Parameter>();
			JTable parameterTable = rhsTermblocks[j].getParametersTable();
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
			JTable factorsTable = rhsTermblocks[j].getFactorsTable();
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
			
			rhs.getTermblocks().add(tb);
		}
		con.setRhs(rhs);
		//==================end of set RHS==============
		return con;
	}

	
	public void actionPerformed(ActionEvent e) {
		
//		// Class expression change apply event
//		if(e.getActionCommand().equals("Apply")){
//			int selectedRow = variablesTable.getSelectedRow();
//			int selectedColumn = variablesTable.getSelectedColumn();
//			variablesTable.getModel().setValueAt(classExpressionTextPane.getText(), selectedRow, selectedColumn);
////System.out.println(variablesTable.getModel().getValueAt(selectedRow, selectedColumn));			
//		}
		
		// ADD event
		if(e.getActionCommand().equals("ADD")){
			
			String selectedItem = (String) optionsComboBox.getSelectedItem();
			
			if(selectedItem.equals("LHS Termblock")){
				
				lhsPanel.add(getLHSTermblockPanel(lhsTermblockNumber),null);
				lhsPanel.repaint();
				lhsTermblockNumber++;
				
			}else if(selectedItem.equals("RHS Termblock")){
				
				rhsPanel.add(getRHSTermblockPanel(rhsTermblockNumber),null);
				rhsPanel.repaint();
				rhsTermblockNumber++;
				
			}		
		}	
		
		// DEL event
		if(e.getActionCommand().equals("DEL")){
			
			String selectedItem = (String) optionsComboBox.getSelectedItem();
			
			if(selectedItem.equals("LHS Termblock")){
				
				if(lhsTermblockNumber < 1){
					// show msg when no constraint selected
					JOptionPane.showMessageDialog (null, "No termblock to delete!", "Wrong", JOptionPane.INFORMATION_MESSAGE);
				}else{
					lhsPanel.remove(lhsPanel.getComponentCount()-1);
					double lhsPanelHeight = lhsPanel.getPreferredSize().getHeight()-112;
					// resize rhsPanel
					lhsPanel.setPreferredSize(new Dimension(0,(int)lhsPanelHeight));
					lhsPanel.revalidate();
					lhsPanel.repaint();
					lhsTermblockNumber--;
				}
				
			}else if(selectedItem.equals("RHS Termblock")){
				
				if(rhsTermblockNumber < 1){
					// show msg when no constraint selected
					JOptionPane.showMessageDialog (null, "No termblock to delete!", "Wrong", JOptionPane.INFORMATION_MESSAGE);
				}else{
					rhsPanel.remove(rhsPanel.getComponentCount()-1);
					double rhsPanelHeight = rhsPanel.getPreferredSize().getHeight()-112;
					// resize rhsPanel
					rhsPanel.setPreferredSize(new Dimension(0,(int)rhsPanelHeight));
					rhsPanel.revalidate();
					rhsPanel.repaint();
					rhsTermblockNumber--;
				}
				
			}	
		}	
		
		// OK event
		if(e.getActionCommand().equals("OK")){

			this.con = getConstraint();
			String newName = this.con.getName();
			
			// check the constraint name exist
			boolean checkCon = false;
			
			// did not change the name
			if(newName.equals(this.oldCon.getName())){
				checkCon = false;
			}else{
				for(Constraint constraint:this.constraintList){
					if(constraint.getName().equals(newName)){
						checkCon = true;
					}
				}
			}
			
			if(checkCon){
				// show msg when no constraint selected
				JOptionPane.showMessageDialog (null, "Constraint name exist!", "Wrong", JOptionPane.INFORMATION_MESSAGE);
			}else{
	
				this.abstractSyntax = Utils.getSWCLAbstractSyntax(variablesList, con);
				abstractSyntaxArea.setText(abstractSyntax);
			}
		}
		
		// submit action
		if(e.getActionCommand().equals("Submit")){
			
			
			String newName = this.con.getName();
			
			// check the constraint name exist
			boolean checkCon = false;
			
			// did not change the name
			if(newName.equals(this.oldCon.getName())){
				checkCon = false;
			}else{
				for(Constraint constraint:this.constraintList){
					if(constraint.getName().equals(newName)){
						checkCon = true;
					}
				}
			}
			
			if(checkCon){
				// show msg when no constraint selected
				JOptionPane.showMessageDialog (null, "Constraint name exist!", "Wrong", JOptionPane.INFORMATION_MESSAGE);
			}else{
	
				int rowCount = this.tableModel.getRowCount();// =no. of constraints 
				
				for(int i=0;i<rowCount;i++){
					if (i==(rowCount-1)){
						this.tableModel.setValueAt(this.abstractSyntax,i,2);
						this.tableModel.setValueAt(this.con.getName(), i, 1);
					}
				}
				
				// write variables to ontology
				this.controller.writeVariablesToOnt(this.variablesList);
		
	//System.out.println("old constraint name is:" + this.oldCon.getName());
				// delete constraint first
				this.controller.deleteConstraint(this.oldCon.getName());
				
				// write constraint to ontology
				this.controller.writeConstraintToOnt(this.con);
				
				// add varibaleList to totalVariablesList

				this.totalVariablesList = this.variablesList;
Utils.printVariablesList("total:", this.totalVariablesList);
Utils.printVariablesList("variables:", variablesList);				
				this.dispose();
			}
		}
	}
} 
 	
	
