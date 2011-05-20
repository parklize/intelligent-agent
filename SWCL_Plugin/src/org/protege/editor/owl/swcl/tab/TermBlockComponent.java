package org.protege.editor.owl.swcl.tab;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JComboBox;
import org.protege.editor.owl.swcl.model.*;

public class TermBlockComponent extends JPanel {

	private static final long serialVersionUID = 1L;
	
	// components attrs
	private JLabel signLabel = null;
	private JLabel aggregateOp = null;
	private JComboBox signComboBox = null;
	private JComboBox aggOppComboBox = null;
	private JLabel parameterLabel = null;
	private JLabel factorLabel = null;
	
	// swcl attrs
	private ArrayList<Variable> variablesList = null;  //  @jve:decl-index=0:

	private JComboBox parameterComboBox = null;

	private JComboBox factorV = null;

	private JComboBox factorP = null;
	
	// default constructor
	public TermBlockComponent() {
		super();
		initialize();
	}
	// default constructor
	public TermBlockComponent(ArrayList<Variable> variablesList) {
		super();
		initialize();
		this.variablesList = variablesList;
	}

	// initializing..
	private void initialize() {
		factorLabel = new JLabel();
		factorLabel.setBounds(new Rectangle(399, 6, 46, 18));
		factorLabel.setText("factor");
		parameterLabel = new JLabel();
		parameterLabel.setBounds(new Rectangle(262, 6, 82, 18));
		parameterLabel.setText("Parameter");
		aggregateOp = new JLabel();
		aggregateOp.setBounds(new Rectangle(136, 6, 98, 18));
		aggregateOp.setText("AggregateOp");
		signLabel = new JLabel();
		signLabel.setBounds(new Rectangle(51, 6, 38, 18));
		signLabel.setText("SIGN");
		this.setSize(500, 60);
		this.setLayout(null);
		this.add(signLabel, null);
		this.add(aggregateOp, null);
		this.add(getSign(), null);
		this.add(getAgg(), null);
		this.add(parameterLabel, null);
		this.add(factorLabel, null);
		this.add(getParameter(), null);
		this.add(getJComboBox(), null);
		this.add(getJComboBox2(), null);
	}

	// sign combobox
	private JComboBox getSign() {
		if (signComboBox == null) {
System.out.println("What's wrong?");
			final String[] signs = {"+","-"};
			
			signComboBox = new JComboBox(signs);
			signComboBox.setBounds(new Rectangle(36, 30, 49, 27));
		}
		return signComboBox;
	}

	// aggregate opperators
	private JComboBox getAgg() {
		if (aggOppComboBox == null) {
			final String[] aggOpps = {"not use","sigma","production"};
			aggOppComboBox = new JComboBox(aggOpps);
			aggOppComboBox.setBounds(new Rectangle(129, 30, 103, 27));
		}
		return aggOppComboBox;
	}
	// parameter combobox
	private JComboBox getParameter() {
		if (parameterComboBox == null) {
			parameterComboBox = new JComboBox();
			parameterComboBox.setBounds(new Rectangle(264, 30, 62, 27));
		}
		return parameterComboBox;
	}

	// factor variable
	private JComboBox getJComboBox() {
		if (factorV == null) {
			factorV = new JComboBox();
			factorV.setBounds(new Rectangle(340, 30, 62, 27));
		}
		return factorV;
	}

	// factor parameter
	private JComboBox getJComboBox2() {
		if (factorP == null) {
			factorP = new JComboBox();
			factorP.setBounds(new Rectangle(420, 30, 62, 27));
		}
		return factorP;
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

	public ArrayList<Variable> getVariablesList() {
		return variablesList;
	}

	public void setVariablesList(ArrayList<Variable> variablesList) {
		this.variablesList = variablesList;
	}

	public JComboBox getParameterComboBox() {
		return parameterComboBox;
	}

	public void setParameterComboBox(JComboBox parameterComboBox) {
		this.parameterComboBox = parameterComboBox;
	}

	public JComboBox getFactorV() {
		return factorV;
	}

	public void setFactorV(JComboBox factorV) {
		this.factorV = factorV;
	}

	public JComboBox getFactorP() {
		return factorP;
	}

	public void setFactorP(JComboBox factorP) {
		this.factorP = factorP;
	}


	


}
