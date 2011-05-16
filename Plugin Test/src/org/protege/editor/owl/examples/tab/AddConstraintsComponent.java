package org.protege.editor.owl.examples.tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import org.protege.editor.owl.examples.utils.CheckBoxRenderer;
import javax.swing.JCheckBox;
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
	private int qualifierNumber = 0;
	private int lhsTermblockNumber = 0;
	private int rhsTermblockNumber = 0;
	private JScrollPane termblockScrollpane = null;
	private JPanel addTermblockpanel = null;
	private JPanel addTermblockPanelMenuPanel = null;
	private JPanel addTermblockPanelTermblockPanel = null;
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
	/**
	 * This is the default constructor
	 */
	public AddConstraintsComponent() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(680, 680);
		this.setContentPane(getJContentPane());
		this.setTitle("SWCL");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);// delete process in memory
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
			containerScrollPane.setBounds(new Rectangle(0, 0, 672, 646));
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
			rhsLabel = new JLabel();
			rhsLabel.setBounds(new Rectangle(33, 323, 60, 18));
			rhsLabel.setText("RHS:");
			lhsLable = new JLabel();
			lhsLable.setBounds(new Rectangle(33, 170, 60, 18));
			lhsLable.setText("LHS:");
			qualifierLabel = new JLabel();
			qualifierLabel.setBounds(new Rectangle(33, 45, 75, 18));
			qualifierLabel.setText("QUALIFIER:");
			operatorLabel = new JLabel();
			operatorLabel.setBounds(new Rectangle(33, 295, 75, 18));
			operatorLabel.setText("OPERATOR:");
			constraintPanel = new JPanel();
			constraintPanel.setLayout(null);
			constraintPanel.add(operatorLabel, null);
			constraintPanel.add(getOperatorComboBox(), null);
			constraintPanel.add(qualifierLabel, null);
//			constraintPanel.setPreferredSize(new Dimension(0,800));
			constraintPanel.add(getLhsScrollPane(), null);
			constraintPanel.add(lhsLable, null);
			constraintPanel.add(rhsLabel, null);
			constraintPanel.add(getRhsScrollPane(), null);
			constraintPanel.add(getQualifierScrollPane(), null);
			constraintPanel.add(getTermblockScrollpane(), null);
		}
		return constraintPanel;
	}

	// operator Combobox
	private JComboBox getOperatorComboBox() {
		if (operatorComboBox == null) {
			final String[] operatorList = {"equal","notEqual","lessThan","lessThanOrEqual","greaterThan","greaterThanOrEqual"};
			operatorComboBox = new JComboBox(operatorList);
			operatorComboBox.setBounds(new Rectangle(140, 295, 140, 23));
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
	
	//===LHS scroll pane
	private JScrollPane getLhsScrollPane() {
		if (lhsScrollPane == null) {
			lhsScrollPane = new JScrollPane();
			lhsScrollPane.setBounds(new Rectangle(142, 170, 500, 120));
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
	
	//===Qualifier Scroll pane
	private JScrollPane getQualifierScrollPane() {
		if (qualifierScrollPane == null) {
			qualifierScrollPane = new JScrollPane();
			qualifierScrollPane.setBounds(new Rectangle(142, 45, 500, 120));
			qualifierScrollPane.setViewportView(getQualifierPanel());
		}
		return qualifierScrollPane;
	}

	// Qualifier panel for all qualifiers
	private JPanel getQualifierPanel() {
		if (qualifierPanel == null) {
			qualifierPanel = new JPanel();
			qualifierPanel.setLayout(null);

		}
		return qualifierPanel;
	}

	// qualifier panel 
	private JPanel getQPanel(int qualifierNumber) {

//System.out.println(qualifierNumber);
			JPanel qPanel = new JPanel();
			qPanel.setLayout(new GridBagLayout());
			qPanel.setBounds(new Rectangle(0, 25*qualifierNumber, 500, 25));
			qPanel.setBackground(new Color(238, 0, 0));
			
			double qualifierPanelHeight = qualifierPanel.getPreferredSize().getHeight()+25;
//System.out.println(qualifierPanelHeight);
			qualifierPanel.setPreferredSize(new Dimension(0,(int)qualifierPanelHeight));// resize qualifierPanel
			qualifierScrollPane.revalidate();// check scroll bar necessity 

		return qPanel;
	}
	
	//===RHS scroll pane
	private JScrollPane getRhsScrollPane() {
		if (rhsScrollPane == null) {
			rhsScrollPane = new JScrollPane();
			rhsScrollPane.setBounds(new Rectangle(141, 323, 500, 114));
			rhsScrollPane.setViewportView(getRhsPanel());
		}
		return rhsScrollPane;
	}
	
	// RHS panel
	private JPanel getRhsPanel() {
		if (rhsPanel == null) {
			rhsPanel = new JPanel();
			rhsPanel.setLayout(null);
			rhsPanel.setOpaque(false);
		}
		return rhsPanel;
	}
/*	
	// RHS Termblock panel 
	private JPanel getRHSTermblockPanel(int rhsTermblockNumber) {

//System.out.println(rhsTermblockNumber);
			JPanel rhsTermblock = new JPanel();
			rhsTermblock.setLayout(null);
			rhsTermblock.setBounds(new Rectangle(0, 60*rhsTermblockNumber, 500, 60));
			rhsTermblock.setBackground(new Color(238, 0, 194));
			
			// sign combo box
			String[] sign = {"+","-"};
			JComboBox signComboBox = new JComboBox(sign);
			signComboBox.setBounds(10, 10, 50, 28);
			
			// aggregateOpperator
			String[] aggregateOpp = {" ","sigma","production"};
			JComboBox aggregateOppComboBox = new JComboBox(aggregateOpp);
			aggregateOppComboBox.setBounds(70, 10, 70, 28);
			
			// add components to rhsTermblock
			rhsTermblock.add(signComboBox);
			rhsTermblock.add(aggregateOppComboBox);
			double rhsPanelHeight = rhsPanel.getPreferredSize().getHeight()+60;
//System.out.println(rhsPanelHeight);
			rhsPanel.setPreferredSize(new Dimension(0,(int)rhsPanelHeight));// resize rhsPanel
			rhsScrollPane.revalidate();// check scroll bar necessity 

		return rhsTermblock;
	}
*/	
	//===Termblock pane created when click the add
	private JScrollPane getTermblockScrollpane() {
		if (termblockScrollpane == null) {
			termblockScrollpane = new JScrollPane();
			termblockScrollpane.setBounds(new Rectangle(141, 447, 500, 135));
			termblockScrollpane.setViewportView(getAddTermblockPanel());
		}
		return termblockScrollpane;
	}
	
	// Termblock panel inside the Termblock pane
	private JPanel getAddTermblockPanel() {
		if (addTermblockpanel == null) {
			addTermblockpanel = new JPanel();
			addTermblockpanel.setLayout(new BorderLayout());
			addTermblockpanel.setBounds(new Rectangle(0, 0, 497, 80));
			addTermblockpanel.add(getAddTermblockPanelMenuPanel(), BorderLayout.SOUTH);
			addTermblockpanel.add(getAddTermblockPanelTermblockPanel(), BorderLayout.CENTER);
		}
		return addTermblockpanel;
	}
	
	// Menu panel inside the Termblock panel
	private JPanel getAddTermblockPanelMenuPanel() {
		if (addTermblockPanelMenuPanel == null) {
			addTermblockPanelMenuPanel = new JPanel();
			addTermblockPanelMenuPanel.setLayout(null);
			addTermblockPanelMenuPanel.setPreferredSize(new Dimension(0,30));
			addTermblockPanelMenuPanel.add(getAddTermblock(), null);
		}
		return addTermblockPanelMenuPanel;
	}

	// Termblock panel inside add Termblock panel
	private JPanel getAddTermblockPanelTermblockPanel() {
		if (addTermblockPanelTermblockPanel == null) {
			AggOp = new JLabel();
			AggOp.setBounds(new Rectangle(80, 9, 38, 18));
			AggOp.setHorizontalAlignment(SwingConstants.CENTER);
			AggOp.setText("AggOp");
			signLabel = new JLabel();
			signLabel.setBounds(new Rectangle(15, 9, 38, 18));
			signLabel.setHorizontalTextPosition(SwingConstants.CENTER);
			signLabel.setHorizontalAlignment(SwingConstants.CENTER);
			signLabel.setText("Sign");
			addTermblockPanelTermblockPanel = new JPanel();
			addTermblockPanelTermblockPanel.setLayout(null);
			addTermblockPanelTermblockPanel.setOpaque(false);
			addTermblockPanelTermblockPanel.add(signLabel, null);
			addTermblockPanelTermblockPanel.add(AggOp, null);
			addTermblockPanelTermblockPanel.add(getSignComboBox(), null);
			addTermblockPanelTermblockPanel.add(getAddParameter(), null);
			addTermblockPanelTermblockPanel.add(getAddFactor(), null);
			addTermblockPanelTermblockPanel.add(getAggOpComboBox(), null);
			addTermblockPanelTermblockPanel.add(getParameterComboBox(), null);
			addTermblockPanelTermblockPanel.add(getFactorsComboBox(), null);
			addTermblockPanelTermblockPanel.add(getFactorsScrollPane(), null);
			addTermblockPanelTermblockPanel.add(getParametersScrollPane(), null);
		}
		return addTermblockPanelTermblockPanel;
	}
	
	// sign combobox
	private JComboBox getSignComboBox() {
		if (signComboBox == null) {
			String[] sign = {"+","-"};
			signComboBox = new JComboBox(sign);
			signComboBox.setBounds(new Rectangle(15, 35, 42, 17));
		}
		return signComboBox;
	}
	
	// aggOp combobox
	private JComboBox getAggOpComboBox() {
		if (aggOpComboBox == null) {
			final String[] aggOp = {"not use","sigma","production"};
			aggOpComboBox = new JComboBox(aggOp);
			aggOpComboBox.setBounds(new Rectangle(80, 35, 82, 16));
		}
		return aggOpComboBox;
	}
	// add Parameter button
	private JButton getAddParameter() {
		if (addParameter == null) {
			addParameter = new JButton();
			addParameter.setBounds(new Rectangle(273, 9, 41, 19));
			addParameter.setText("+");
			addParameter.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					
					int selectedIndex = parameterComboBox.getSelectedIndex();
					DefaultTableModel tableModel = (DefaultTableModel) parametersTable.getModel();
					
					if(selectedIndex == 1){
						// set visible class parameter
						
					}else if(selectedIndex == 0){
						//add qualifier parameter
						parametersScrollPane.setVisible(true);
						
						// has value
						TableColumn hasValue = parametersTable.getColumnModel().getColumn(1);
						JComboBox hasValueList = new JComboBox();// get values from existing data
						hasValueList.addItem("A");
						hasValueList.addItem("B");
						hasValue.setCellEditor(new DefaultCellEditor(hasValueList));

						// on property
						TableColumn onProperty = parametersTable.getColumnModel().getColumn(2);
						JComboBox onPropertyList = new JComboBox();// get properties from hasValue
						onPropertyList.addItem("A");
						onProperty.setCellEditor(new DefaultCellEditor(onPropertyList));
						
						// add row
						tableModel.addRow(new Object[]{"","A","A"});
					}
				}
			});
		}
		return addParameter;
	}

	// add Factor button
	private JButton getAddFactor() {
		if (addFactor == null) {
			addFactor = new JButton();
			addFactor.setBounds(new Rectangle(442, 8, 41, 19));
			addFactor.setText("+");
			addFactor.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {

					int selectedIndex = factorsComboBox.getSelectedIndex();
					DefaultTableModel tableModel = (DefaultTableModel) factorsTable.getModel();
					
					if(selectedIndex == 0){
						// add class factors
						factorsScrollPane.setVisible(true);
						
						// variable
						TableColumn variable = factorsTable.getColumnModel().getColumn(0);
						JComboBox variableList = new JComboBox();// get variables from existing data
						variableList.addItem("C");
						variableList.addItem("D");
						variable.setCellEditor(new DefaultCellEditor(variableList));
						
						// property
						TableColumn property = factorsTable.getColumnModel().getColumn(1);
						JComboBox propertyList = new JComboBox();
						propertyList.addItem("D");
						propertyList.addItem("E");
						property.setCellEditor(new DefaultCellEditor(propertyList));
						
						// add row
						tableModel.addRow(new Object[]{"C","C"});
						
					}else if(selectedIndex == 1){
						
					}
					
				}
				
			});
		}
		return addFactor;
	}
	
	// Confirm button
	private JButton getAddTermblock() {
		if (addTermblock == null) {
			addTermblock = new JButton();
			addTermblock.setBounds(new Rectangle(411, 8, 81, 14));
			addTermblock.setText("Confirm");
		}
		return addTermblock;
	}

	// parameters scrollpane
	private JScrollPane getParametersScrollPane() {
		if (parametersScrollPane == null) {
			parametersScrollPane = new JScrollPane();
			parametersScrollPane.setBounds(new Rectangle(168, 36, 186, 55));
			parametersScrollPane.setViewportView(getParametersTable());
			parametersScrollPane.setVisible(false);
		}
		return parametersScrollPane;
	}

	// parameters table
	private JTable getParametersTable() {
		if (parametersTable == null) {
			
			final String[] colHeads = {"Variable","hasValue","Onproperty"};
			final Object[][] data = null;
			
			DefaultTableModel model = new DefaultTableModel(data,colHeads);
			parametersTable = new JTable(model);

		}
		return parametersTable;
	}
	
	// parameters combobox
	private JComboBox getParameterComboBox() {
		if (parameterComboBox == null) {
			final String[] parameters = {"Qparameter","Cparameter"};
			parameterComboBox = new JComboBox(parameters);
			parameterComboBox.setBounds(new Rectangle(173, 8, 90, 18));
		}
		return parameterComboBox;
	}
	
	// factors combobox
	private JComboBox getFactorsComboBox() {
		if (factorsComboBox == null) {
			final String[] factors = {"Cfactor","Ifactor"};
			factorsComboBox = new JComboBox(factors);
			factorsComboBox.setBounds(new Rectangle(344, 8, 90, 18));
		}
		return factorsComboBox;
	}

	// factors scrollpane
	private JScrollPane getFactorsScrollPane() {
		if (factorsScrollPane == null) {
			factorsScrollPane = new JScrollPane();
			factorsScrollPane.setBounds(new Rectangle(367, 31, 126, 60));
			factorsScrollPane.setViewportView(getFactorsTable());
			factorsScrollPane.setVisible(false);
		}
		return factorsScrollPane;
	}

	// factors table
	private JTable getFactorsTable() {
		if (factorsTable == null) {
			final String[] colHeads = {"Variable","Property"};
			final Object[][] data = null;
			
			DefaultTableModel model = new DefaultTableModel(data,colHeads);
			factorsTable = new JTable(model);
		}
		return factorsTable;
	}
	
	
	
	
	
	
	
	
	

	//===Action Performed Control
	@Override
	public void actionPerformed(ActionEvent e) {

		// OK event
		if(e.getActionCommand().equals("OK")){
			System.out.println(operatorComboBox.getSelectedItem());
			System.out.println((rhsPanel.getComponent(1)));
		}
		// ADD event
		if(e.getActionCommand().equals("ADD")){
			String selectedItem = (String) optionsComboBox.getSelectedItem();
			// Add qualifier
			if(selectedItem.equals("QUALIFIER")){
System.out.println("this is qualifier");
				qualifierPanel.add(getQPanel(qualifierNumber),null);
				qualifierPanel.repaint();
				qualifierNumber++;
			}else if(selectedItem.equals("LHS Termblock")){
				
				
			}else if(selectedItem.equals("RHS Termblock")){
System.out.println("this is RHS Termblock");
//				rhsPanel.add(getRHSTermblockPanel(rhsTermblockNumber),null);
				rhsPanel.repaint();
				rhsTermblockNumber++;
			}		
		}	
	}
















} 
 	
	
