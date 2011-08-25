package org.protege.editor.owl.swcl.view;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.SystemColor;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JButton;

import org.protege.editor.owl.swcl.model.Variable;
import org.semanticweb.owlapi.model.OWLOntology;

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

	/**
	 * This is the default constructor
	 */
	public ObjectiveViewComponent(ArrayList<Variable> variableList, OWLOntology ont) {
		super();
		initialize(variableList, ont);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize(ArrayList<Variable> variableList, OWLOntology ont) {
		
		this.variablesList = variableList;
		this.ont = ont;

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
		this.setSize(800, 600);
		this.setLayout(null);
		this.add(jLabel, null);
		this.add(jLabel1, null);
		
		this.add(getInstructionComboBox(), null);
		this.add(getRhsScrollPane(),null);
		this.add(getJButton(), null);
	}

	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getInstructionComboBox() {
		if (instructions == null) {
			final String[] signs = {"Maximize","Minimize"};
			
			instructions = new JComboBox(signs);
			instructions.setBounds(new Rectangle(180, 40, 120, 23));
		}
		return instructions;
	}

	/*
	 * RHS part
	 */
	
	// RHS scroll pane
	private JScrollPane getRhsScrollPane() {
		if (termScrollPane == null) {
			termScrollPane = new JScrollPane();
			termScrollPane.setBounds(new Rectangle(180, 98, 500, 114));
			termScrollPane.setViewportView(getRhsPanel());
		}
		return termScrollPane;
	}
	
	// RHS panel
	private JPanel getRhsPanel() {
		if (termPanel == null) {
			termPanel = new JPanel();
			termPanel.setLayout(null);
		}
		return termPanel;
	}

	// RHS Termblock panel 
	private JPanel getRHSTermblockPanel(int termblockNumber) {

			objectiveTermblocks[termblockNumber] =  new TermBlockComponent(variablesList,ont);
			
			// termblock initializing...
			objectiveTermblocks[termblockNumber].setLayout(null);
			objectiveTermblocks[termblockNumber].setBounds(new Rectangle(0, 112*termblockNumber, 500, 112));
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
			jButton.setBounds(new Rectangle(703, 100, 34, 17));
			jButton.setText("+");
			jButton.addActionListener(this);
		}
		return jButton;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getActionCommand().equals("+")){
			
			termPanel.add(getRHSTermblockPanel(termblockNumber),null);
			termPanel.repaint();
			termblockNumber++;
		}
		
	}
}
