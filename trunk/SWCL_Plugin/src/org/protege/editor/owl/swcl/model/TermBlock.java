package org.protege.editor.owl.swcl.model;

import java.util.ArrayList;


public class TermBlock {
	
	private int id;
	private String sign;
	private String aggregateOppertor;
	private ArrayList<Parameter> parameters;
	private ArrayList<Factor> factors;
	
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
	public ArrayList<Parameter> getParameters() {
		return parameters;
	}
	public void setParameters(ArrayList<Parameter> parameters) {
		this.parameters = parameters;
	}
	public ArrayList<Factor> getFactors() {
		return factors;
	}
	public void setFactors(ArrayList<Factor> factors) {
		this.factors = factors;
	}
	
	
	
}
