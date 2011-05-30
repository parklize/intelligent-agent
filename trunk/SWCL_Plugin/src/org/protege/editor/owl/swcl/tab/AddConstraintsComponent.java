package org.protege.editor.owl.swcl.tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.OWLWorkspace;
import org.protege.editor.owl.swcl.model.ClassVariable;
import org.protege.editor.owl.swcl.model.Constraint;
import org.protege.editor.owl.swcl.model.Factor;
import org.protege.editor.owl.swcl.model.LHS;
import org.protege.editor.owl.swcl.model.Operator;
import org.protege.editor.owl.swcl.model.Parameter;
import org.protege.editor.owl.swcl.model.Qualifier;
import org.protege.editor.owl.swcl.model.RHS;
import org.protege.editor.owl.swcl.model.RelatedVariable;
import org.protege.editor.owl.swcl.model.TermBlock;
import org.protege.editor.owl.swcl.model.Variable;
import org.protege.editor.owl.swcl.utils.OWLClassHelper;
import org.protege.editor.owl.swcl.utils.OWLComponentFactoryImplExtension;
import org.protege.editor.owl.swcl.utils.Utils;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntology;

import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import java.util.ArrayList;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import java.awt.SystemColor;
import javax.swing.border.EtchedBorder;
import javax.swing.border.BevelBorder;
/**
 * 
 * Author: parklize,pryiyeon
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
	private int lhsTermblockNumber = 0;
	private int rhsTermblockNumber = 0;
	private JLabel variableLabel = null;
	private JScrollPane variableScrollPane = null;
	private JPanel variablePanel = null;
	private JButton addVariableButton = null;
	private JScrollPane variablesScrollPane = null;
	private JTable variablesTable = null;
	private JTextPane manchesterSyntax = null;
	private JScrollPane qualifiersScrollPane = null;
	private JTable qualifiersTable = null;
	private JButton addQualifierButton = null;
	private JTextPane classExpressionTextPane = null;
	private JComponent com = null;
	private TableColumn qualifierVariable = null;
	private JPanel jPanel = null;
	private JButton classExpressionApplyButton = null;
	
	private TermBlockComponent[] rhsTermblocks = new TermBlockComponent[100];
	private TermBlockComponent[] lhsTermblocks = new TermBlockComponent[100];
	
/*
 * should be afforded from ExampleViewComponent
 */
	private ArrayList<Variable> totalVariablesList = new ArrayList<Variable>();  //  @jve:decl-index=0:
	private ArrayList<Variable> variablesList = new ArrayList<Variable>();  //  @jve:decl-index=0:
	private OWLOntology ont = null;
	private OWLClassExpression oc = null;
    private OWLClassHelper owlClassHelper = null;
    private OWLEditorKit oek = null;
    private OWLComponentFactoryImplExtension ocfe = null;
    private DefaultTableModel tableModel = null;

	
    
	// initialing...
	public AddConstraintsComponent(OWLOntology ont,OWLClassExpression oc,ArrayList<Variable> totalVariablesList,OWLEditorKit oek, DefaultTableModel tableModel) {
		super();
		 // get variables already announced
		this.totalVariablesList = totalVariablesList;
		this.ont = ont;
		this.oc = oc;
		this.oek = oek;
		ocfe = new OWLComponentFactoryImplExtension(oek);
		this.tableModel = tableModel;
		initialize();
		this.classExpressionTextPane = getClassExpressionPane();

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
			variableLabel.setBounds(new Rectangle(33, 30, 66, 18));
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
		}
		return constraintPanel;
	}

	// variableScrollPane
	private JScrollPane getVariableScrollPane() {
		if (variableScrollPane == null) {
			variableScrollPane = new JScrollPane();
			variableScrollPane.setBounds(new Rectangle(142, 30, 500, 155));
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
			variablePanel.add(getJPanel2(), null);
			variablePanel.add(getClassExpressionApplyButton(), null);
		}
		return variablePanel;
	}
	
	// variables scrollpane
	private JScrollPane getVariablesScrollPane() {
		if (variablesScrollPane == null) {
			variablesScrollPane = new JScrollPane();
			variablesScrollPane.setBounds(new Rectangle(27, 36, 118, 105));
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
			variablesTable.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
				}
				
				@Override
				public void mouseExited(MouseEvent e) {}
				
				@Override
				public void mouseEntered(MouseEvent e) {}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					int selectedRow = variablesTable.getSelectedRow();
					int selectedColumn = variablesTable.getSelectedColumn();
					if(selectedColumn == 1){
						// get class description from variablesTable 
						getClassExpressionPane().setText((String) variablesTable.getValueAt(selectedRow, selectedColumn));
					}
					
				}
			});
//			TableColumn hasValue = variablesTable.getColumnModel().getColumn(1);

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
						Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), qualifierVariable);

						// apply change to termblocks
						for(int i=0;i<100;i++){
							if(rhsTermblocks[i] != null){
								Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), rhsTermblocks[i].getParameterColumn());
								Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), rhsTermblocks[i].getFactorVariableColumn());
							}
							if(lhsTermblocks[i] != null){
								Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), lhsTermblocks[i].getParameterColumn());
								Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), lhsTermblocks[i].getFactorVariableColumn());
							}
						}				
					}
				}
			});
		}
		return variablesTable;
	}
	
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

	
	// add variable button
	private JButton getAddVariableButton() {
		if (addVariableButton == null) {
			addVariableButton = new JButton();
			addVariableButton.setText("+");
			addVariableButton.setBounds(new Rectangle(95, 6, 49, 17));
			addVariableButton.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {

					((DefaultTableModel) variablesTable.getModel()).addRow(new Object[]{"",""});

					// add variable to variablesList
					Variable variable = new Variable("","");
					variablesList.add(variable);
					
					// apply change to qualifierVariable table
					Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), qualifierVariable);
					
					// apply change to termblocks
					for(int i=0;i<100;i++){
						if(rhsTermblocks[i] != null){
							Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), rhsTermblocks[i].getParameterColumn());
							Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), rhsTermblocks[i].getFactorVariableColumn());
						}
						if(lhsTermblocks[i] != null){
							Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), lhsTermblocks[i].getParameterColumn());
							Utils.refreshComboBox(Utils.sumArrayList(totalVariablesList, variablesList), lhsTermblocks[i].getFactorVariableColumn());
						}
					}
					
				}
			});
		}
		return addVariableButton;
	}
	
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

			lhsTermblocks[lhsTermblockNumber] =  new TermBlockComponent(Utils.sumArrayList(totalVariablesList, variablesList));
			
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

			rhsTermblocks[rhsTermblockNumber] =  new TermBlockComponent(Utils.sumArrayList(totalVariablesList, variablesList));
			
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
				String valName = (String)tableModel.getValueAt(u, 0);
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
				String valName = (String)tableModel.getValueAt(u, 0);
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
				fList.add(f);
			}
			
			tb.setFactors(fList);
			
			rhs.getTermblocks().add(tb);
		}
		con.setRhs(rhs);
		//==================end of set RHS==============
		return con;
	}
	
	
	//Abstract Syntax 출력
	public void getSWCLAbstractSyntax(Constraint con,DefaultTableModel tableModel){
		
		String str="Constraint ";
		
		//Qualifier 
	
		if (con.getQualifiers().size()!=0){				//만약 qualifier사이즈가 0이 아니면 실행
			str+="( Qualifier (Variable (";
			
			for (int i=0; i<con.getQualifiers().size();i++){	// qualifier 사이즈만큼 
				str+=" "+ con.getQualifiers().get(i).getV().getName();
			}
			str+=" ) )";
		}
		
		
		//LHS
		
		str+=" LHS ";
		for (int i=0; i<con.getLhs().getTermblocks().size();i++){
			str+="( TermBlock ( "+ con.getLhs().getTermblocks().get(i).getSign();
			
			if (con.getLhs().getTermblocks().get(i).getAggregateOppertor()!="not use"){			//만약 aggregate가 not use가 아니면 실행
				str+=" "+ con.getLhs().getTermblocks().get(i).getAggregateOppertor()+" Parameter( Variable (";
					
				for (int j=0; j<con.getLhs().getTermblocks().get(i).getParameters().size();j++){		//parameter 사이즈 만큼
					str+=	" "+ con.getLhs().getTermblocks().get(i).getParameters().get(j).getV().getName();
				}
				str+=" ) )";
			}
			
			str+=" Factor ( Variable(";
			
			for (int k=0; k<con.getLhs().getTermblocks().get(i).getFactors().size();k++){
				str+=" "+ con.getLhs().getTermblocks().get(i).getFactors().get(k).getV().getName()+" "+ con.getLhs().getTermblocks().get(i).getFactors().get(k).getOwlProperty();
			
			}
			str+=" ) ) ) ) ";
		}
		
		
		//Operator
		str+=con.getOpp().getOpp();
		
		//RHS
		str+=" RHS ";
		for (int i=0; i<con.getRhs().getTermblocks().size();i++){
			str+="( TermBlock ( "+ con.getRhs().getTermblocks().get(i).getSign();
			
			if (con.getRhs().getTermblocks().get(i).getAggregateOppertor()!="not use"){			//만약 aggregate가 not use가 아니면 실행
				str+=" "+ con.getRhs().getTermblocks().get(i).getAggregateOppertor()+" Parameter( Variable (";
					
				for (int j=0; j<con.getRhs().getTermblocks().get(i).getParameters().size();j++){		//parameter 사이즈 만큼
					str+=	" "+ con.getRhs().getTermblocks().get(i).getParameters().get(j).getV().getName();
				}
				str+=" ) )";
			}
			
			str+=" Factor ( Variable(";
			
			for (int k=0; k<con.getRhs().getTermblocks().get(i).getFactors().size();k++){
				str+=" "+ con.getRhs().getTermblocks().get(i).getFactors().get(k).getV().getName()+" "+ con.getRhs().getTermblocks().get(i).getFactors().get(k).getOwlProperty();
			
			}
			str+=" ) ) ) )";
			
		}

//		System.out.println(str);
	
		ConstraintConfirmComponent test =new ConstraintConfirmComponent(str,tableModel);
		test.setVisible(true);
		
//		int rowCount = tableModel.getRowCount();// =no. of constraints 
//		for(int i=0;i<rowCount;i++){
//			
//			tableModel.setValueAt(str,i,2);
//		}
	}
	@Override
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
		
		// NEED UPDATE,OK event
		if(e.getActionCommand().equals("OK")){
			// add varibaleList to totalVariablesList
			Utils.addArrayList(totalVariablesList, variablesList);
			Constraint con = getConstraint();
			getSWCLAbstractSyntax(con, tableModel);
		}
	}


} 
 	
	
