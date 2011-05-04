package org.protege.editor.owl.examples.tab;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JList;
import javax.swing.JComboBox;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.Cursor;
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

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJScrollPane(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (constraintPanel == null) {
			qualifierLabel = new JLabel();
			qualifierLabel.setBounds(new Rectangle(35, 32, 60, 18));
			qualifierLabel.setText("qualifier:");
			operatorLabel = new JLabel();
			operatorLabel.setBounds(new Rectangle(34, 76, 60, 18));
			operatorLabel.setText("operator:");
			constraintPanel = new JPanel();
			constraintPanel.setLayout(null);
			constraintPanel.add(operatorLabel, null);
			constraintPanel.add(getOperatorComboBox(), null);
			constraintPanel.add(qualifierLabel, null);
//			constraintPanel.setPreferredSize(new Dimension(0,800));
		}
		return constraintPanel;
	}

	/**
	 * This method initializes operatorComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getOperatorComboBox() {
		if (operatorComboBox == null) {
			String[] operatorList = {"equal","notEqual","lessThan","lessThanOrEqual","greaterThan","greaterThanOrEqual"};
			operatorComboBox = new JComboBox(operatorList);
			operatorComboBox.setBounds(new Rectangle(125, 76, 140, 27));
		}
		return operatorComboBox;
	}
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (containerScrollPane == null) {
			containerScrollPane = new JScrollPane();
			containerScrollPane.setBounds(new Rectangle(0, 0, 672, 646));
			containerScrollPane.setViewportView(getContainerPanel());
		}
		return containerScrollPane;
	}

	/**
	 * This method initializes containerPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
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

	/**
	 * This method initializes menuPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getMenuPanel() {
		if (menuPanel == null) {
			menuPanel = new JPanel();
			menuPanel.setLayout(null);
			menuPanel.setPreferredSize(new Dimension(0,50));
			menuPanel.add(getOkButton(), null);
			menuPanel.add(getADD(), null);
		}
		return menuPanel;
	}

	/**
	 * This method initializes ADD	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getADD() {
		if (add == null) {
			add = new JButton();
			add.setBounds(new Rectangle(60, 9, 69, 28));
			add.setText("ADD");
		}
		return add;
	}
	/**
	 * This method initializes okButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText("OK");
			okButton.setBounds(new Rectangle(592, 9, 51, 28));
			okButton.addActionListener(this);
		}
		return okButton;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// OK event
		if(e.getActionCommand().equals("OK")){
			System.out.println(operatorComboBox.getSelectedItem());
		}
		
	}
}  
