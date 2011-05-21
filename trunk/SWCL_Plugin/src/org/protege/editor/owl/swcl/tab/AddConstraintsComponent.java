package org.protege.editor.owl.swcl.tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.rmi.CORBA.Util;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.swcl.model.ClassVariable;
import org.protege.editor.owl.swcl.model.RelatedVariable;
import org.protege.editor.owl.swcl.model.Variable;
import org.protege.editor.owl.swcl.utils.CheckBoxRenderer;
import org.protege.editor.owl.swcl.utils.OWLClassHelper;
import org.protege.editor.owl.swcl.utils.Utils;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.Iterator;
/**
 * 
 * Author: parklize
 * Date: 2011.04.25
 * Description: component for adding constraint
 *
 */
public class AddConstraintsComponent extends JFrame implements ActionListener{

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
	private JPanel termblockPanel = null;
	private int lhsTermblockNumber = 0;
	private int rhsTermblockNumber = 0;
	private JScrollPane termblockScrollpane = null;
	private JPanel addTermblockpanel = null;
	private JPanel addTermblockPanelMenuPanel = null;
//	private JPanel addTermblockPanelTermblockPanel = null;
	private JButton jButton = null;
	private JLabel signLabel = null;
	private JLabel AggOp = null;
	private JButton addTermblock = null;
	private JComboBox signComboBox = null;
	private JButton addParameter = null;
	private JButton addFactor = null;
	private JComboBox aggOpComboBox = null;
	private JScrollPane parametersScrollPane = null;
	private JTable parametersTable = null;
	private JComboBox parameterComboBox = null;
	private JComboBox factorsComboBox = null;
	private JScrollPane factorsScrollPane = null;
	private JTable factorsTable = null;
	private JLabel variableLabel = null;
	private JScrollPane variableScrollPane = null;
	private JPanel variablePanel = null;
	private JButton addVariableButton = null;
	private JScrollPane variablesScrollPane = null;
	private JTable variablesTable = null;
	private JButton addrelatedVariableButton = null;
	private JScrollPane relatedVariablesScrollPane = null;
	private JTable relatedVariablesTable = null;
	
	private TermBlockComponent[] rhsTermblocks = new TermBlockComponent[100];
	
/*
 * should be afforded from ExampleViewComponent
 */
	private ArrayList<Variable> totalVariablesList = new ArrayList<Variable>();
	private ArrayList<Variable> variablesList = new ArrayList<Variable>();  
	private ArrayList<Variable>	classVariablesList = new ArrayList<Variable>(); 
	private ArrayList<Variable>	relatedVariablesList = new ArrayList<Variable>();  
	
	private OWLOntology owl = null;
    private OWLClassHelper owlClassHelper = null;
	private JScrollPane qualifiersScrollPane = null;
	private JTable qualifiersTable = null;
	private JButton addQualifierButton = null;
	
	private TableColumn qualifierVariable = null;
	private TableColumn relatedVariable = null;
	
	private Utils util = new Utils();
    
	// initialing...
	public AddConstraintsComponent(OWLOntology owl,ArrayList<Variable> totalVariablesList) {
		super();
		initialize();
		 // get variables already announced
		this.totalVariablesList = totalVariablesList;
		this.owl = owl;
		
Utils.printVariablesList("variablesList:", variablesList);
	}
	
	// for test purpose
	public AddConstraintsComponent(){
		super();
		initialize();
	}
	
	// initializing frame
	private void initialize() {
		this.setSize(680, 800);
		this.setContentPane(getJContentPane());
		this.setTitle("SWCL");
//		this.setDefaultCloseOperation(EXIT_ON_CLOSE);// delete process in memory
	}

	// jContentPane
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJScrollPane(), null);
		}
		return jContentPane;
	}
	
	// content Scroll pane
	private JScrollPane getJScrollPane() {
		if (containerScrollPane == null) {
			containerScrollPane = new JScrollPane();
			containerScrollPane.setBounds(new Rectangle(0, 0, 675, 767));
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
			variableLabel = new JLabel();
			variableLabel.setBounds(new Rectangle(33, 25, 66, 18));
			variableLabel.setText("VARIABLE:");
			rhsLabel = new JLabel();
			rhsLabel.setBounds(new Rectangle(33, 443, 60, 18));
			rhsLabel.setText("RHS:");
			lhsLable = new JLabel();
			lhsLable.setBounds(new Rectangle(33, 290, 60, 18));
			lhsLable.setText("LHS:");
			qualifierLabel = new JLabel();
			qualifierLabel.setBounds(new Rectangle(33, 165, 75, 18));
			qualifierLabel.setText("QUALIFIER:");
			operatorLabel = new JLabel();
			operatorLabel.setBounds(new Rectangle(33, 415, 75, 18));
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
		}
		return constraintPanel;
	}

	// variableScrollPane
	private JScrollPane getVariableScrollPane() {
		if (variableScrollPane == null) {
			variableScrollPane = new JScrollPane();
			variableScrollPane.setBounds(new Rectangle(142, 25, 500, 120));
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
			variablePanel.add(getAddrelatedVariableButton(), null);
			variablePanel.add(getRelatedVariablesScrollPane(), null);
		}
		return variablePanel;
	}
	
	// variables scrollpane
	private JScrollPane getVariablesScrollPane() {
		if (variablesScrollPane == null) {
			variablesScrollPane = new JScrollPane();
			variablesScrollPane.setBounds(new Rectangle(27, 36, 118, 67));
			variablesScrollPane.setViewportView(getVariablesTable());
		}
		return variablesScrollPane;
	}

	// variables table
	private JTable getVariablesTable() {
		if (variablesTable == null) {
			final String[] colHeads = {"Variable","Class"};
			final String[][] data = null;
			
			DefaultTableModel model = new DefaultTableModel(data,colHeads);
			variablesTable = new JTable(model);
			TableColumn hasValue = variablesTable.getColumnModel().getColumn(1);
/*
 * should get classes from protege ontology			
 */
			JComboBox values = new JComboBox();// get values from existing data
			values.addItem("A");
			values.addItem("B");
			
			hasValue.setCellEditor(new DefaultCellEditor(values));
/*
 * cell value changed listener			
 */
			variablesTable.getModel().addTableModelListener(new TableModelListener(){

				public void tableChanged(TableModelEvent e) {
					
					if(e.getType() == TableModelEvent.UPDATE){

						String newValue = (String) variablesTable.getValueAt(e.getLastRow(),e.getColumn());	
						classVariablesList.get(e.getLastRow()).setName(newValue);
						
						Utils.refreshTotalArrayList(variablesList, classVariablesList, relatedVariablesList);
						
						// apply change to qualifierVariable table
						Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), qualifierVariable);
						// apply change to relatedVariable table
						Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), relatedVariable);
						// apply change to termblocks
						for(int i=0;i<100;i++){
							if(rhsTermblocks[i] != null){
								Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), rhsTermblocks[i].getParameterColumn());
								Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), rhsTermblocks[i].getFactorVariableColumn());
							}
						}
						
					}
				}
			});
		}
		return variablesTable;
	}

	// related variables scroll pane
	private JScrollPane getRelatedVariablesScrollPane() {
		if (relatedVariablesScrollPane == null) {
			relatedVariablesScrollPane = new JScrollPane();
			relatedVariablesScrollPane.setBounds(new Rectangle(158, 35, 160, 69));
			relatedVariablesScrollPane.setViewportView(getRelatedVariablesTable());
		}
		return relatedVariablesScrollPane;
	}

	// related variables table
	private JTable getRelatedVariablesTable() {
		if (relatedVariablesTable == null) {
			final String[] colHeads = {"Variable","¡ô","Variable","Property"};
			final String[][] data = null;
			
			DefaultTableModel model = new DefaultTableModel(data,colHeads);
			relatedVariablesTable = new JTable(model);
			
			// row 3
			relatedVariable = relatedVariablesTable.getColumnModel().getColumn(2);
			
			// refresh variable combobox
			Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), relatedVariable);
			
			// row 4
			TableColumn property = relatedVariablesTable.getColumnModel().getColumn(3);
			JComboBox properties = new JComboBox();
			properties.addItem("C");
			properties.addItem("D");
			property.setCellEditor(new DefaultCellEditor(properties));
			
			relatedVariablesTable.getModel().addTableModelListener(new TableModelListener() {
				
				@Override
				public void tableChanged(TableModelEvent e) {
					
					if(e.getType() == TableModelEvent.UPDATE && e.getColumn() == 0){
						// set new name
						String newValue = (String) relatedVariablesTable.getValueAt(e.getLastRow(),e.getColumn());	
						relatedVariablesList.get(e.getLastRow()).setName(newValue);
						// refresh variables list
						Utils.refreshTotalArrayList(variablesList, classVariablesList, relatedVariablesList);
						// apply change to qualifierVariable table
						Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), qualifierVariable);
						// apply change to relatedVariable table
						Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), relatedVariable);
						// apply change to termblocks
						for(int i=0;i<100;i++){
							if(rhsTermblocks[i] != null){
								Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), rhsTermblocks[i].getParameterColumn());
								Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), rhsTermblocks[i].getFactorVariableColumn());
							}
						}
					}
				}
			});
		}
		return relatedVariablesTable;
	}
	
	// add variable button
	private JButton getAddVariableButton() {
		if (addVariableButton == null) {
			addVariableButton = new JButton();
			addVariableButton.setText("+");
			addVariableButton.setBounds(new Rectangle(95, 6, 49, 17));
			addVariableButton.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {

					((DefaultTableModel) variablesTable.getModel()).addRow(new Object[]{"","A"});
/*
 * binding class editing..					
 */
					// add variable to variablesList
					ClassVariable variable = new ClassVariable();
					variable.setName("");// default name
					classVariablesList.add(variable);
					
					Utils.refreshTotalArrayList(variablesList, classVariablesList, relatedVariablesList);
					// apply change to qualifierVariable table
					Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), qualifierVariable);
					// apply change to relatedVariable table
					Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), relatedVariable);
					// apply change to termblocks
					for(int i=0;i<100;i++){
						if(rhsTermblocks[i] != null){
							Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), rhsTermblocks[i].getParameterColumn());
							Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), rhsTermblocks[i].getFactorVariableColumn());
						}
					}
					
				}
			});
		}
		return addVariableButton;
	}
	// add related variable button x=y.hasPart
	private JButton getAddrelatedVariableButton() {
		if (addrelatedVariableButton == null) {
			addrelatedVariableButton = new JButton();
			addrelatedVariableButton.setBounds(new Rectangle(269, 6, 49, 17));
			addrelatedVariableButton.setText("+");
			addrelatedVariableButton.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					
					if(totalVariablesList.size()==0){
						// first create constraint
						((DefaultTableModel)relatedVariablesTable.getModel()).addRow(new Object[]{"","¡ô",variablesList.get(0).getName(),"A"});
					}else{
						// not first time
						((DefaultTableModel)relatedVariablesTable.getModel()).addRow(new Object[]{"","¡ô",totalVariablesList.get(0).getName(),"A"});
					}
/*
 * binding information editing...					
 */
					Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), relatedVariable);// refresh variables combobox 
					// add variable to variablesList
					RelatedVariable variable = new RelatedVariable();
					variable.setName("");
					relatedVariablesList.add(variable);
					
					Utils.refreshTotalArrayList(variablesList, classVariablesList, relatedVariablesList);// refresh variables list
					// apply change to qualifierVariable table
					Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), qualifierVariable);
					// apply change to relatedVariable table
					Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), relatedVariable);
					// apply change to termblocks
					for(int i=0;i<100;i++){
						if(rhsTermblocks[i] != null){
							Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), rhsTermblocks[i].getParameterColumn());
							Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), rhsTermblocks[i].getFactorVariableColumn());
						}
					}
				}
				
			});
		}
		return addrelatedVariableButton;
	}
	// operator Combobox
	private JComboBox getOperatorComboBox() {
		if (operatorComboBox == null) {
			final String[] operatorList = {"equal","notEqual","lessThan","lessThanOrEqual","greaterThan","greaterThanOrEqual"};
			operatorComboBox = new JComboBox(operatorList);
			operatorComboBox.setBounds(new Rectangle(140, 415, 140, 23));
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
			menuPanel.add(getADD(), null);
			menuPanel.add(getOptionsComboBox(), null);
		}
		return menuPanel;
	}

	// Add Button
	private JButton getADD() {
		if (add == null) {
			add = new JButton();
			add.setBounds(new Rectangle(190, 10, 69, 28));
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
			okButton.setBounds(new Rectangle(592, 9, 51, 28));
			okButton.addActionListener(this);
		}
		return okButton;
	}
	
	// options
	private JComboBox getOptionsComboBox() {
		if (optionsComboBox == null) {
			final String[] options = {"QUALIFIER","LHS Termblock","RHS Termblock"};
			optionsComboBox = new JComboBox(options);
			optionsComboBox.setBounds(new Rectangle(27, 10, 119, 28));
		}
		return optionsComboBox;
	}
	
	/*
	 * LHS
	 */
	
	// LHS Scrollpane
	private JScrollPane getLhsScrollPane() {
		if (lhsScrollPane == null) {
			lhsScrollPane = new JScrollPane();
			lhsScrollPane.setBounds(new Rectangle(142, 287, 500, 120));
			lhsScrollPane.setViewportView(getLhsPanel());
		}
		return lhsScrollPane;
	}

	// LHS panel
	private JPanel getLhsPanel() {
		if (lhsPanel == null) {
			lhsPanel = new JPanel();
			lhsPanel.setLayout(new GridBagLayout());
		}
		return lhsPanel;
	}
	
	/*
	 * qualifier
	 */
	private JScrollPane getQualifierScrollPane() {
		if (qualifierScrollPane == null) {
			qualifierScrollPane = new JScrollPane();
			qualifierScrollPane.setBounds(new Rectangle(142, 162, 500, 120));
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
			Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), qualifierVariable);
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
					// TODO Auto-generated method stub
					DefaultTableModel model = (DefaultTableModel) qualifiersTable.getModel();
					model.addRow(new Object[]{variablesList.get(0).getName()});
					
				}
				
			});
		}
		return addQualifierButton;
	}
	
	/*
	 * RHS
	 */
	
	// RHS scroll pane
	private JScrollPane getRhsScrollPane() {
		if (rhsScrollPane == null) {
			rhsScrollPane = new JScrollPane();
			rhsScrollPane.setBounds(new Rectangle(141, 440, 500, 114));
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

			rhsTermblocks[rhsTermblockNumber] =  new TermBlockComponent(Utils.sumArrayList(totalVariablesList, variablesList));
			
			// termblock initializing...
			rhsTermblocks[rhsTermblockNumber].setLayout(null);
			rhsTermblocks[rhsTermblockNumber].setBounds(new Rectangle(0, 60*rhsTermblockNumber, 500, 60));
			rhsTermblocks[rhsTermblockNumber].setBackground(new Color(238, 0, 194));
			
			double rhsPanelHeight = rhsPanel.getPreferredSize().getHeight()+60;
			// resize rhsPanel
			rhsPanel.setPreferredSize(new Dimension(0,(int)rhsPanelHeight));
			// check scroll bar necessity 
			rhsScrollPane.revalidate();

		return rhsTermblocks[rhsTermblockNumber];
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// OK event
		if(e.getActionCommand().equals("OK")){
			Utils.addArrayList(totalVariablesList, variablesList);
		}
		// ADD event
		if(e.getActionCommand().equals("ADD")){
			
			String selectedItem = (String) optionsComboBox.getSelectedItem();
			// Add qualifier
			if(selectedItem.equals("QUALIFIER")){
				
			}else if(selectedItem.equals("LHS Termblock")){
				
			}else if(selectedItem.equals("RHS Termblock")){
				rhsPanel.add(getRHSTermblockPanel(rhsTermblockNumber),null);
				rhsPanel.repaint();
				rhsTermblockNumber++;
			}		
		}	
	}




























} 
 	
	
