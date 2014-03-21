package org.protege.editor.owl.swcl.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import javax.swing.JButton;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextArea;
/**
 * @author parklize
 * @version 1.0, 2011-05-20
 */
public class ConstraintConfirmComponent extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel jPanel = null;
	private JButton jButton = null;
	private String str=null;
	private String name = null;
	private DefaultTableModel tableModel=null;
	private JTextArea jTextArea = null;
	private JScrollPane js=null;
	/**
	 * This is the default constructor
	 */
	public ConstraintConfirmComponent(String name,String str,DefaultTableModel tableModel) {
		super();
		
		this.str=str;
		this.name = name;
		this.tableModel=tableModel;
		initialize(str);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize(String str) {
		this.setSize(270, 247);
		this.setContentPane(getJContentPane(str));
		this.setTitle("SWCL Confirmation");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane(String str) {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanel(str), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel(String str) {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(getJButton(), null);
			jPanel.add(getJTextArea(str), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(133, 180, 127, 29));
			jButton.setText("Submit");
			jButton.addActionListener(this);
			
		}
		return jButton;
	}
	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getJTextArea(String str) {
		if (jTextArea == null) {
			jTextArea = new JTextArea();
			jTextArea.setLineWrap(true);
			jTextArea.setText(str);
			jTextArea.setBounds(new Rectangle(6, 6, 250, 170));
		}
		return jTextArea;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getActionCommand().equals("Submit")){

			int rowCount = this.tableModel.getRowCount();// =no. of constraints 
			for(int i=0;i<rowCount;i++){
				if (i==(rowCount-1)){
					this.tableModel.setValueAt(str,i,2);
					this.tableModel.setValueAt(name, i, 1);
				}
			}
			this.dispose();
			}
	}


}  //  @jve:decl-index=0:visual-constraint="10,10"  
