package org.protege.editor.owl.swcl.view;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.SystemColor;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

import org.protege.editor.owl.swcl.controller.ConstraintController;
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
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
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
	private JScrollPane termScrollPane = null;
	private JPanel termPanel = null;
	private TermBlockComponent[] objectiveTermblocks = new TermBlockComponent[100];
	private JButton jButton = null;
	
	private ArrayList<Variable> variablesList = new ArrayList<Variable>();  //  @jve:decl-index=0:
	private OWLOntology ont = null;  //  @jve:decl-index=0:
	private JButton confirmButton = null;
	
	private ConstraintController con = null;
	
	private OWLOntologyManager manager = OWLManager.createOWLOntologyManager();  //  @jve:decl-index=0:
	

	/**
	 * This is the default constructor
	 */
	public ObjectiveViewComponent(ArrayList<Variable> variableList, OWLOntology ont, ConstraintController con) {
		super();
		initialize(variableList, ont, con);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize(ArrayList<Variable> variableList, OWLOntology ont, ConstraintController con) {
		
		this.variablesList = variableList;
		this.ont = ont;
		this.con = con;

if(ont == null){
	System.out.println("ont is null");
}else{
	System.out.println("ont is not null");
}
	
		
		jLabel1 = new JLabel();
		jLabel1.setBounds(new Rectangle(18, 98, 141, 18));
		jLabel1.setText("Objective Term:");
		jLabel = new JLabel();
		jLabel.setBounds(new Rectangle(18, 32, 141, 18));
		jLabel.setText("Opimization Instruction:");
		this.setSize(897, 600);
		this.setLayout(null);
		this.add(jLabel, null);
		this.add(jLabel1, null);
		
		this.add(getInstructionComboBox(), null);
		this.add(getRhsScrollPane(),null);
		this.add(getJButton(), null);
		this.add(getConfirmButton(), null);
	}

	// get instruction combobox
	private JComboBox getInstructionComboBox() {
		if (instructions == null) {
			final String[] signs = {"Maximize","Minimize"};
			
			instructions = new JComboBox(signs);
			instructions.setBounds(new Rectangle(180, 40, 120, 23));
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
			termScrollPane.setBounds(new Rectangle(180, 98, 620, 114));
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
			jButton.setBounds(new Rectangle(823, 98, 34, 17));
			jButton.setText("+");
			jButton.addActionListener(this);
		}
		return jButton;
	}

	// confirm button
	private JButton getConfirmButton() {
		if (confirmButton == null) {
			confirmButton = new JButton();
			confirmButton.setBounds(new Rectangle(747, 242, 52, 22));
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
			OptModel om = getOptModel();
			con.writeOptModelToOnt(om);
			SolverCodeTranslator sct = new SolverCodeTranslator();
			sct.translateSWCL(this.ont); // D:\eclipse\Ilog.txt·Î
		}
		
	}


}  //  @jve:decl-index=0:visual-constraint="10,10"
