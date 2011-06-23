package org.protege.editor.owl.swcl.view;

import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;

import org.protege.editor.owl.swcl.controller.SWCLOntologyController;
import org.protege.editor.owl.swcl.model.*;
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
	private JButton removeParameterButton = null;
	private JScrollPane factorScrollPane = null;
	private JTable factorTable = null;
	private TableColumn factorVariableColumn = null;
	private JButton addFactorButton = null;
	private JButton removeFactorButton = null;
	private OWLOntology ont = null;
	private SWCLOntologyController swclOntologyHelper = null;

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
		this.swclOntologyHelper = new SWCLOntologyController(ont);
		initialize();
	}

	// initializing..
	private void initialize() {
		factorLabel = new JLabel();
		factorLabel.setBounds(new Rectangle(334, 18, 46, 18));
		factorLabel.setText("factor");
		parameterLabel = new JLabel();
		parameterLabel.setBounds(new Rectangle(190, 18, 82, 18));
		parameterLabel.setText("Parameter");
		aggregateOp = new JLabel();
		aggregateOp.setBounds(new Rectangle(80, 18, 98, 18));
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
		this.add(getRemoveParameterButton(),null);
		this.add(getFactorScrollPane(), null);
		this.add(getAddFactorButton(), null);
		this.add(getRemoveFactorButton(),null);
	}

	// sign combobox
	private JComboBox getSign() {
		if (signComboBox == null) {
			final String[] signs = {"+","-"};
			
			signComboBox = new JComboBox(signs);
			signComboBox.setBounds(new Rectangle(7, 40, 40, 23));
		}
		return signComboBox;
	}

	// aggregate opperators
	private JComboBox getAgg() {
		if (aggOppComboBox == null) {
			final String[] aggOpps = {"not use","sigma","production"};
			aggOppComboBox = new JComboBox(aggOpps);
			aggOppComboBox.setBounds(new Rectangle(77, 40, 83, 23));
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
			parametersScrollPane.setBounds(new Rectangle(190, 40, 115, 62));
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
			addParameterButton.setBounds(new Rectangle(250, 18, 25, 18));
			addParameterButton.setText("+");
			addParameterButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					if(aggOppComboBox.getSelectedItem().equals("not use")){
						
						// show msg when no constraint selected
						JOptionPane.showMessageDialog (null, "Select aggOpp!", "Wrong", JOptionPane.INFORMATION_MESSAGE);
						
					}else{
						DefaultTableModel model = (DefaultTableModel) parametersTable.getModel();
						model.addRow(new Object[]{variablesList.get(0).getName()});
					}
					
				}});
		}
		return addParameterButton;
	}
	
	// remove parameter button
	private JButton getRemoveParameterButton() {
		
		if (removeParameterButton == null) {
			
			removeParameterButton = new JButton();
			removeParameterButton.setBounds(new Rectangle(280, 18, 25, 18));
			removeParameterButton.setText("-");
			removeParameterButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					DefaultTableModel model = (DefaultTableModel) parametersTable.getModel();
					int index = model.getRowCount()-1;
					
					if(index < 0){
						// show msg when no constraint selected
						JOptionPane.showMessageDialog (null, "No parameter to delete!", "Wrong", JOptionPane.INFORMATION_MESSAGE);
					}else{
						model.removeRow(index);
					}
					
				}});
		}
		return removeParameterButton;
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
				// refine the properties about SWCL, do not display these properties 
				if(!str.equals("bindingClass") && !str.equals("hasOperator") && !str.equals("hasAggregateOperation") && !str.equals("hasSign") && !str.equals("hasBindingDataProperty")){
					jb.addItem(str);
				}
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
			addFactorButton.setBounds(new Rectangle(372, 18, 25, 18));
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
	
	// remove factor button
	private JButton getRemoveFactorButton() {
		if (removeFactorButton == null) {
			removeFactorButton = new JButton();
			removeFactorButton.setBounds(new Rectangle(402, 18, 25, 18));
			removeFactorButton.setText("-");
			removeFactorButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {

					DefaultTableModel model = (DefaultTableModel) factorTable.getModel();
					int index = model.getRowCount()-1;
					if(index < 0){
						// show msg when no constraint selected
						JOptionPane.showMessageDialog (null, "No factor to delete!", "Wrong", JOptionPane.INFORMATION_MESSAGE);
					}else{
						model.removeRow(index);
					}
					
				}
				
			});
		}
		return removeFactorButton;
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
