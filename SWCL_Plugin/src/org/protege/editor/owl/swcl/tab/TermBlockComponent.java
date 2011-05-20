package org.protege.editor.owl.swcl.tab;

import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Rectangle;
import javax.swing.JComboBox;

public class TermBlockComponent extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel signLabel = null;
	private JLabel aggregateOp = null;
	private JComboBox signComboBox = null;
	private JComboBox aggOppComboBox = null;
	private JLabel parameterLabel = null;
	private JLabel factorLabel = null;
	
	// default constructor
	public TermBlockComponent() {
		super();
		initialize();
	}

	// initializing..
	private void initialize() {
		factorLabel = new JLabel();
		factorLabel.setBounds(new Rectangle(421, 12, 46, 18));
		factorLabel.setText("factor");
		parameterLabel = new JLabel();
		parameterLabel.setBounds(new Rectangle(294, 12, 82, 18));
		parameterLabel.setText("Parameter");
		aggregateOp = new JLabel();
		aggregateOp.setBounds(new Rectangle(136, 12, 98, 18));
		aggregateOp.setText("AggregateOp");
		signLabel = new JLabel();
		signLabel.setBounds(new Rectangle(51, 12, 38, 18));
		signLabel.setText("SIGN");
		this.setSize(500, 120);
		this.setLayout(null);
		this.add(signLabel, null);
		this.add(aggregateOp, null);
		this.add(getJComboBox(), null);
		this.add(getJComboBox1(), null);
		this.add(parameterLabel, null);
		this.add(factorLabel, null);
	}

	// sign combobox
	private JComboBox getJComboBox() {
		if (signComboBox == null) {
			final String[] signs = {"+","-"};
			
			signComboBox = new JComboBox(signs);
			signComboBox.setBounds(new Rectangle(36, 63, 49, 27));
		}
		return signComboBox;
	}

	// aggregate opperators
	private JComboBox getJComboBox1() {
		if (aggOppComboBox == null) {
			final String[] aggOpps = {"not use","sigma","production"};
			aggOppComboBox = new JComboBox(aggOpps);
			aggOppComboBox.setBounds(new Rectangle(129, 63, 103, 27));
		}
		return aggOppComboBox;
	}



}
