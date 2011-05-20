package org.protege.editor.owl.swcl.model;

public class TermBlock {
	
	private int id;
	private String sign;
	private String aggregateOppertor;
	private Variable variable;
	private Factor factor;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getAggregateOppertor() {
		return aggregateOppertor;
	}
	public void setAggregateOppertor(String aggregateOppertor) {
		this.aggregateOppertor = aggregateOppertor;
	}
	public Variable getVariable() {
		return variable;
	}
	public void setVariable(Variable variable) {
		this.variable = variable;
	}
	public Factor getFactor() {
		return factor;
	}
	public void setFactor(Factor factor) {
		this.factor = factor;
	}
	
	
}
