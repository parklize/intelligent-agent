package org.protege.editor.owl.swcl.view;

import javax.swing.DefaultCellEditor;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import org.protege.editor.owl.swcl.model.*;
import org.protege.editor.owl.swcl.utils.SWCLOntologyHelper;
import org.protege.editor.owl.swcl.utils.Utils;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.JButton;
/**
 * 
 * Author: parklize
 * Date: 2011.04.20~
 * Description: TermBlockComponent
 */
public class TermBlockComponent extends JPanel {

	private static final long serialVersionUID = 1L;
	
	// components attrs
	private JLabel signLabel = null;
	private JLabel aggregateOp = null;
	private JComboBox signComboBox = null;
	private JComboBox aggOppComboBox = null;
	private JLabel parameterLabel = null;
	private JLabel factorLabel = null;
	private TableColumn parameterColumn = null;
	private JTable parametersTable = null;
	private JScrollPane parametersScrollPane = null;
		
	// swcl attrs
	private ArrayList<Variable> variablesList = null;  //  @jve:decl-index=0:
	private JButton addParameterButton = null;
	private JScrollPane factorScrollPane = null;
	private JTable factorTable = null;
	private TableColumn factorVariableColumn = null;
	private JButton addFactorButton = null;
	private OWLOntology ont = null;
	private SWCLOntologyHelper swclOntologyHelper = null;

	// default constructor
	public TermBlockComponent() {
		super();
		initialize();
	}
	// default constructor
	public TermBlockComponent(ArrayList<Variable> variablesList,OWLOntology ont) {
		super();
		this.variablesList = variablesList;
		this.ont = ont;
		this.swclOntologyHelper = new SWCLOntologyHelper(ont);
		initialize();
	}

	// initializing..
	private void initialize() {
		factorLabel = new JLabel();
		factorLabel.setBounds(new Rectangle(353, 18, 46, 18));
		factorLabel.setText("factor");
		parameterLabel = new JLabel();
		parameterLabel.setBounds(new Rectangle(216, 18, 82, 18));
		parameterLabel.setText("Parameter");
		aggregateOp = new JLabel();
		aggregateOp.setBounds(new Rectangle(90, 18, 98, 18));
		aggregateOp.setText("aggregateOp");
		signLabel = new JLabel();
		signLabel.setBounds(new Rectangle(7, 18, 38, 18));
		signLabel.setText("sign");
		this.setSize(500, 112);
		this.setLayout(null);
		this.add(signLabel, null);
		this.add(aggregateOp, null);
		this.add(getSign(), null);
		this.add(getAgg(), null);
		this.add(parameterLabel, null);
		this.add(factorLabel, null);
		this.add(getParameterScrollPane(), null);
		this.add(getAddParameterButton(), null);
		this.add(getFactorScrollPane(), null);
		this.add(getAddFactorButton(), null);
	}

	// sign combobox
	private JComboBox getSign() {
		if (signComboBox == null) {
			final String[] signs = {"+","-"};
			
			signComboBox = new JComboBox(signs);
			signComboBox.setBounds(new Rectangle(7, 40, 49, 27));
		}
		return signComboBox;
	}

	// aggregate opperators
	private JComboBox getAgg() {
		if (aggOppComboBox == null) {
			final String[] aggOpps = {"not use","sigma","production"};
			aggOppComboBox = new JComboBox(aggOpps);
			aggOppComboBox.setBounds(new Rectangle(90, 40, 103, 27));
		}
		return aggOppComboBox;
	}
	
	/*
	 * Parameter
	 */

	// parameters scrollpane
	private JScrollPane getParameterScrollPane() {
		if (parametersScrollPane == null) {
			parametersScrollPane = new JScrollPane();
			parametersScrollPane.setBounds(new Rectangle(217, 40, 73, 62));
			parametersScrollPane.setViewportView(getParameterTable());
		}
		return parametersScrollPane;
	}
	
	// parameters table
	private JTable getParameterTable() {
		if (parametersTable == null) {
			final String[] colHeads = {"Variable"};
			final String[][] data = null;
			
			DefaultTableModel dt = new DefaultTableModel(data,colHeads);
			parametersTable = new JTable(dt);
			parameterColumn = parametersTable.getColumnModel().getColumn(0);
			// initialize parameter's variable combobox
			Utils.refreshComboBox(variablesList, parameterColumn);
			
		}
		return parametersTable;
	}
	
	// add parameter button
	private JButton getAddParameterButton() {
		if (addParameterButton == null) {
			addParameterButton = new JButton();
			addParameterButton.setBounds(new Rectangle(290, 18, 43, 18));
			addParameterButton.setText("+");
			addParameterButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					DefaultTableModel model = (DefaultTableModel) parametersTable.getModel();
					model.addRow(new Object[]{variablesList.get(0).getName()});
					
				}});
		}
		return addParameterButton;
	}
	/*
	 * Factor
	 */
	private JScrollPane getFactorScrollPane() {
		if (factorScrollPane == null) {
			factorScrollPane = new JScrollPane();
			factorScrollPane.setBounds(new Rectangle(334, 40, 135, 62));
			factorScrollPane.setViewportView(getFactorTable());
		}
		return factorScrollPane;
	}
	
	// factor table
	private JTable getFactorTable() {
		if (factorTable == null) {
			
			final String[] colHeads = {"Variable","Property"};
			final String[][] data = null;
			
			DefaultTableModel dt = new DefaultTableModel(data,colHeads);
			factorTable = new JTable(dt);
			
			factorVariableColumn  = factorTable.getColumnModel().getColumn(0);		
			// initialize factor variable combobox
			Utils.refreshComboBox(variablesList, factorVariableColumn);
			
			JComboBox jb = new JComboBox();
			ArrayList<String> propertyList = swclOntologyHelper.getPropertyList();
			for(String str:propertyList){
				jb.addItem(str);
			}
			TableColumn propertyColumn = factorTable.getColumnModel().getColumn(1);
			propertyColumn.setCellEditor(new DefaultCellEditor(jb));
			
		}
		return factorTable;
	}

	// add factor button
	private JButton getAddFactorButton() {
		if (addFactorButton == null) {
			addFactorButton = new JButton();
			addFactorButton.setBounds(new Rectangle(406, 18, 43, 18));
			addFactorButton.setText("+");
			addFactorButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {

					DefaultTableModel model = (DefaultTableModel) factorTable.getModel();
					model.addRow(new Object[]{variablesList.get(0).getName()});
					
				}
				
			});
		}
		return addFactorButton;
	}
	
	/*
	 * getter & setter
	 */
	public JLabel getSignLabel() {
		return signLabel;
	}
	public void setSignLabel(JLabel signLabel) {
		this.signLabel = signLabel;
	}
	public JLabel getAggregateOp() {
		return aggregateOp;
	}
	public void setAggregateOp(JLabel aggregateOp) {
		this.aggregateOp = aggregateOp;
	}
	public JComboBox getSignComboBox() {
		return signComboBox;
	}
	public void setSignComboBox(JComboBox signComboBox) {
		this.signComboBox = signComboBox;
	}
	public JComboBox getAggOppComboBox() {
		return aggOppComboBox;
	}
	public void setAggOppComboBox(JComboBox aggOppComboBox) {
		this.aggOppComboBox = aggOppComboBox;
	}
	public JLabel getParameterLabel() {
		return parameterLabel;
	}
	public void setParameterLabel(JLabel parameterLabel) {
		this.parameterLabel = parameterLabel;
	}
	public JLabel getFactorLabel() {
		return factorLabel;
	}
	public void setFactorLabel(JLabel factorLabel) {
		this.factorLabel = factorLabel;
	}
	public TableColumn getParameterColumn() {
		return parameterColumn;
	}
	public void setParameterColumn(TableColumn parameterColumn) {
		this.parameterColumn = parameterColumn;
	}
	public JTable getParametersTable() {
		return parametersTable;
	}
	public void setParametersTable(JTable parametersTable) {
		this.parametersTable = parametersTable;
	}
	public JScrollPane getParametersScrollPane() {
		return parametersScrollPane;
	}
	public void setParametersScrollPane(JScrollPane parametersScrollPane) {
		this.parametersScrollPane = parametersScrollPane;
	}
	public ArrayList<Variable> getVariablesList() {
		return variablesList;
	}
	public void setVariablesList(ArrayList<Variable> variablesList) {
		this.variablesList = variablesList;
	}
	public JButton getAddParameter() {
		return addParameterButton;
	}
	public void setAddParameter(JButton addParameter) {
		this.addParameterButton = addParameter;
	}
	public TableColumn getFactorVariableColumn() {
		return factorVariableColumn;
	}
	public void setFactorVariableColumn(TableColumn factorVariableColumn) {
		this.factorVariableColumn = factorVariableColumn;
	}
	public void setFactorScrollPane(JScrollPane factorScrollPane) {
		this.factorScrollPane = factorScrollPane;
	}
	public void setFactorTable(JTable factorTable) {
		this.factorTable = factorTable;
	}
	public JTable getFactorsTable(){
		return this.factorTable;
	}



	


}