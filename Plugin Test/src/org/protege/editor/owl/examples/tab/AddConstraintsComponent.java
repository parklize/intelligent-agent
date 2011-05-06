package org.protege.editor.owl.examples.tab;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import java.awt.Cursor;
import java.awt.Color;
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
	private int qualifierNumber = 0;
	private int lhsTermblockNumber = 0;
	private int rhsTermblockNumber = 0;
	
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
			rhsLabel.setBounds(new Rectangle(33, 302, 60, 18));
			rhsLabel.setText("RHS:");
			lhsLable = new JLabel();
			lhsLable.setBounds(new Rectangle(34, 149, 60, 18));
			lhsLable.setText("LHS:");
			qualifierLabel = new JLabel();
			qualifierLabel.setBounds(new Rectangle(34, 452, 60, 18));
			qualifierLabel.setText("qualifier:");
			operatorLabel = new JLabel();
			operatorLabel.setBounds(new Rectangle(32, 45, 60, 18));
			operatorLabel.setText("operator:");
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
		}
		return constraintPanel;
	}

	// operator Combobox
	private JComboBox getOperatorComboBox() {
		if (operatorComboBox == null) {
			String[] operatorList = {"equal","notEqual","lessThan","lessThanOrEqual","greaterThan","greaterThanOrEqual"};
			operatorComboBox = new JComboBox(operatorList);
			operatorComboBox.setBounds(new Rectangle(140, 45, 140, 27));
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
			String[] options = {"qualifier","LHS Termblock","RHS Termblock"};
			optionsComboBox = new JComboBox(options);
			optionsComboBox.setBounds(new Rectangle(27, 10, 119, 28));
		}
		return optionsComboBox;
	}
	
	// LHS scroll pane
	private JScrollPane getLhsScrollPane() {
		if (lhsScrollPane == null) {
			lhsScrollPane = new JScrollPane();
			lhsScrollPane.setBounds(new Rectangle(142, 149, 501, 112));
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

	// RHS scroll pane
	private JScrollPane getRhsScrollPane() {
		if (rhsScrollPane == null) {
			rhsScrollPane = new JScrollPane();
			rhsScrollPane.setBounds(new Rectangle(141, 302, 501, 112));
			rhsScrollPane.setViewportView(getRhsPanel());
		}
		return rhsScrollPane;
	}

	// RHS panel
	private JPanel getRhsPanel() {
		if (rhsPanel == null) {
			rhsPanel = new JPanel();
			rhsPanel.setLayout(new GridBagLayout());
		}
		return rhsPanel;
	}

	// Qualifier Scroll pane
	private JScrollPane getQualifierScrollPane() {
		if (qualifierScrollPane == null) {
			qualifierScrollPane = new JScrollPane();
			qualifierScrollPane.setBounds(new Rectangle(142, 452, 501, 112));
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

System.out.println(qualifierNumber);
			JPanel qPanel = new JPanel();
			qPanel.setLayout(new GridBagLayout());
			qPanel.setBounds(new Rectangle(0, 25*qualifierNumber, 499, 25));
			qPanel.setBackground(new Color(238, 238, 0));
			
			double qualifierPanelHeight = qualifierPanel.getPreferredSize().getHeight()+25;
System.out.println(qualifierPanelHeight);
			qualifierPanel.setPreferredSize(new Dimension(0,(int)qualifierPanelHeight));// resize qualifierPanel
			qualifierScrollPane.revalidate();// check scroll bar necessity 

		return qPanel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// OK event
		if(e.getActionCommand().equals("OK")){
			System.out.println(operatorComboBox.getSelectedItem());
		}
		// ADD event
		if(e.getActionCommand().equals("ADD")){
			String selectedItem = (String) optionsComboBox.getSelectedItem();
			// Add qualifier
			if(selectedItem.equals("qualifier")){
				qualifierPanel.add(getQPanel(qualifierNumber),null);
				qualifierPanel.repaint();
				qualifierNumber++;
			}else if(selectedItem.equals("LHS Termblock")){
				
			}else if(selectedItem.equals("RHS Termblock")){
				
			}
			
		}
		
	}

}  