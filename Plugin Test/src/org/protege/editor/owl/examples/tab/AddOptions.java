package org.protege.editor.owl.examples.tab;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddOptions extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel optionsContentPane = null;
	private JPanel optionsPanel = null;
	private JList optionsList = null;
	private JPanel menuPanel = null;
	private JButton ok = null;
	private String option = null;

	public AddOptions() {
		super();
		initialize();
	}

	private void initialize() {
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}

	private JPanel getJContentPane() {
		if (optionsContentPane == null) {
			optionsContentPane = new JPanel();
			optionsContentPane.setLayout(new BorderLayout());
			optionsContentPane.add(getMenuPanel(), BorderLayout.SOUTH);
			optionsContentPane.add(getOptionsPanel(), BorderLayout.CENTER);
		}
		return optionsContentPane;
	}

	private JPanel getOptionsPanel() {
		if (optionsPanel == null) {
			optionsPanel = new JPanel();
			optionsPanel.setLayout(new BorderLayout());
			optionsPanel.add(getOptionsList(), BorderLayout.CENTER);
		}
		return optionsPanel;
	}

	private JList getOptionsList() {
		if (optionsList == null) {
			String[] options = {"qualifier","LHS Termblock","RHS Termblock"};
			optionsList = new JList(options);
		}
		return optionsList;
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
			menuPanel.setPreferredSize(new Dimension(0,30));
			menuPanel.add(getOk(), null);
		}
		return menuPanel;
	}

	private JButton getOk() {
		if (ok == null) {
			ok = new JButton();
			ok.setBounds(new Rectangle(222, 5, 62, 18));
			ok.setText("OK");
			ok.addActionListener(this);
		}
		return ok;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().endsWith("OK")){
			this.dispose();
			this.setOption(optionsList.getSelectedValue().toString());
		}
	}

}
