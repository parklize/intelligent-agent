package org.protege.editor.owl.swcl.view;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxOntologyFormat;
import org.protege.editor.core.ui.split.ViewSplitPane;
import org.protege.editor.core.ui.split.ViewSplitPaneDivider;
import org.protege.editor.core.ui.view.View;
import org.protege.editor.core.ui.view.ViewComponent;
import org.protege.editor.core.ui.view.ViewsPane;
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
import org.protege.editor.owl.swcl.utils.SWCLOntologyHelper;
import org.protege.editor.owl.swcl.utils.Utils;
import org.protege.editor.owl.ui.OWLWorkspaceViewsTab;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLRenderer;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
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
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChangeListener;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import java.awt.SystemColor;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
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
	private JScrollPane variablesScrollPane = null;
	private JTable variablesTable = null;
	private JScrollPane qualifiersScrollPane = null;
	private JTable qualifiersTable = null;
	private JButton addQualifierButton = null;
	private JTextPane classExpressionTextPane = null;
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
	
/*
 * should be afforded from ExampleViewComponent
 */
	private ArrayList<Variable> totalVariablesList = new ArrayList<Variable>();  //  @jve:decl-index=0:
	private ArrayList<Variable> variablesList = new ArrayList<Variable>();  //  @jve:decl-index=0:
	private OWLOntology ont = null;  //  @jve:decl-index=0:
	private OWLClassExpression oc = null;
	private String abstractSyntax = null;
	private Constraint con = null;
	
	// create ontology manager to work with
	private OWLWorkspace ow = null;
	private OWLModelManager owlModelManager = null;
	private OWLOntologyManager manager = null;  //  @jve:decl-index=0:
	private OWLDataFactory dataFactory = null;  //  @jve:decl-index=0:
	private String base = null;  //  @jve:decl-index=0:
	private PrefixManager pm = null;  //  @jve:decl-index=0:
	private SWCLOntologyHelper soh = null;


//    private OWLClassHelper owlClassHelper = null;
//    private OWLEditorKit oek = null;
//    private OWLComponentFactoryImplExtension ocfe = null;


	
    
	// initialing...
	public ModifyConstraintsComponent(Constraint con, OWLWorkspace ow, OWLModelManager owlModelManager, ArrayList<Variable> totalVariablesList, DefaultTableModel tableModel) {
		super();
		preinitialize(ow, owlModelManager,totalVariablesList,tableModel);
		initialize();
//		this.classExpressionTextPane = getClassExpressionPane();

	}
	
	private void preinitialize(OWLWorkspace ow, OWLModelManager owlModelManager, ArrayList<Variable> totalVariablesList, DefaultTableModel tableModel){
		 // get variables already announced
		this.ow = ow;
		this.owlModelManager = owlModelManager;
		this.totalVariablesList = totalVariablesList;
		this.variablesList = totalVariablesList;
		this.ont = owlModelManager.getActiveOntology();
//		this.oc = oc;
//		this.oek = oek;
//		ocfe = new OWLComponentFactoryImplExtension(oek);
		this.tableModel = tableModel;
		// create ontology manager to work with
		this.manager = OWLManager.createOWLOntologyManager(); 
		this.dataFactory = manager.getOWLDataFactory();  
		this.soh = new SWCLOntologyHelper(ont);
		// set base
		this.base = soh.getPrefix();
		this.pm = new DefaultPrefixManager(base);  //  @jve:decl-index=0:

	}
	
	// for test purpose
	public ModifyConstraintsComponent(){
		super();
		initialize();
	}
	
	// initializing frame
	private void initialize() {
		this.setSize(680, 963);
		this.setContentPane(getJContentPane());
		this.setTitle("SWCL");
	}

	// jContentPane
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJScrollPane(), null);
			jContentPane.add(getJPanel2(), null);
		}
		return jContentPane;
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
			addVariableButton.setBounds(new Rectangle(422, 7, 49, 17));
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
			menuPanel.add(getJButton(),null);
			menuPanel.add(getADD(), null);
			menuPanel.add(getOptionsComboBox(), null);
		}
		return menuPanel;
	}

	private JPanel getJPanel2() {
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

	
	private JTextArea getJTextArea() {
		if (abstractSyntaxArea == null) {
			abstractSyntaxArea = new JTextArea();
			abstractSyntaxArea.setLineWrap(true);
			abstractSyntaxArea.setBounds(new Rectangle(144, 18, 501, 112));
		}
		return abstractSyntaxArea;
	}

	
	private JButton getJButton() {
		if (submitButton == null) {
			submitButton = new JButton();
			submitButton.setBounds(new Rectangle(592, 9, 51, 24));
			submitButton.setText("Submit");
			submitButton.addActionListener(this);
		}
		return submitButton;
	}
	
	// Add Button
	private JButton getADD() {
		if (add == null) {
			add = new JButton();
			add.setBounds(new Rectangle(190, 10, 69, 24));
			add.setText("ADD");
			add.addActionListener(this);
		}
		return add;
	}
	
	// OK button
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText("OK");
			okButton.setBounds(new Rectangle(532, 9, 51, 24));
			okButton.addActionListener(this);
		}
		return okButton;
	}
	
	// options
	private JComboBox getOptionsComboBox() {
		if (optionsComboBox == null) {
			final String[] options = {"LHS Termblock","RHS Termblock"};
			optionsComboBox = new JComboBox(options);
			optionsComboBox.setBounds(new Rectangle(27, 10, 119, 28));
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
		}
		return qualifiersTable;
	}

	// add qualifier button
	private JButton getAddQualifierButton() {
		if (addQualifierButton == null) {
			addQualifierButton = new JButton();
			addQualifierButton.setBounds(new Rectangle(149, 8, 49, 17));
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
	
	/*
	 * RHS part
	 */
	
	// RHS scroll pane
	private JScrollPane getRhsScrollPane() {
		if (rhsScrollPane == null) {
			rhsScrollPane = new JScrollPane();
			rhsScrollPane.setBounds(new Rectangle(141, 570, 500, 114));
			rhsScrollPane.setViewportView(getRhsPanel());
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
			for(Variable v:totalVariablesList){
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
				p.setV(Utils.findVariableWithName(totalVariablesList, valName));
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
				
				f.setV(Utils.findVariableWithName(totalVariablesList, vName));
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
				p.setV(Utils.findVariableWithName(totalVariablesList, valName));
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
				f.setV(Utils.findVariableWithName(totalVariablesList, vName));
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

	// NEED UPDATE
	private void writeVariablesToOnt() {
		
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
			SWCLOntologyHelper tempSoh = new SWCLOntologyHelper(tempOnt);
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
	private void writeConstraintToOnt() {
		// add constraint axiom
		OWLClass conClass = dataFactory.getOWLClass("#Constraint",pm);
		OWLIndividual conInd = dataFactory.getOWLNamedIndividual("#"+con.getName(),pm);
		OWLClassAssertionAxiom classAssertion = dataFactory.getOWLClassAssertionAxiom(conClass, conInd);
		manager.addAxiom(ont, classAssertion);
		
		// add has qualifier axiom
		OWLObjectProperty hasQualifier = dataFactory.getOWLObjectProperty(IRI.create(base + "#hasQualifier"));
		ArrayList<Qualifier> qualifierList = con.getQualifiers();
		for(Qualifier q:qualifierList){
			OWLIndividual ind = soh.getOWLIndividual(q.getV().getName());
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
	
	
	public void actionPerformed(ActionEvent e) {
		
		// Class expression change apply event
		if(e.getActionCommand().equals("Apply")){
			int selectedRow = variablesTable.getSelectedRow();
			int selectedColumn = variablesTable.getSelectedColumn();
			variablesTable.getModel().setValueAt(classExpressionTextPane.getText(), selectedRow, selectedColumn);
//System.out.println(variablesTable.getModel().getValueAt(selectedRow, selectedColumn));			
		}
		
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
		
		// OK event
		if(e.getActionCommand().equals("OK")){

			this.con = getConstraint();
			this.abstractSyntax = Utils.getSWCLAbstractSyntax(variablesList, con);
			abstractSyntaxArea.setText(abstractSyntax);
			
		}
		
		// submit action
		if(e.getActionCommand().equals("Submit")){
			
			int rowCount = this.tableModel.getRowCount();// =no. of constraints 
			
			for(int i=0;i<rowCount;i++){
				if (i==(rowCount-1)){
					this.tableModel.setValueAt(this.abstractSyntax,i,2);
					this.tableModel.setValueAt(this.con.getName(), i, 1);
				}
			}
			
			// write variables to ontology
			writeVariablesToOnt();
			// write constraint to ontology
			writeConstraintToOnt();
			
			// add varibaleList to totalVariablesList
			this.totalVariablesList = this.variablesList;
			
			this.dispose();

		}
	}
} 
 	
	
