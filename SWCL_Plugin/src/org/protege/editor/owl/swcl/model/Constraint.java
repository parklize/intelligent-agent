package org.protege.editor.owl.swcl.model;

import java.util.ArrayList;

public class Constraint {
	
	private String name;
	private ArrayList<Qualifier> qualifiers = new ArrayList<Qualifier>();
	private LHS lhs;
	private Operator opp;
	private RHS rhs;
	

	public ArrayList<Qualifier> getQualifiers() {
		return qualifiers;
	}
	public void setQualifiers(ArrayList<Qualifier> qualifiers) {
		this.qualifiers = qualifiers;
	}
	public LHS getLhs() {
		return lhs;
	}
	public void setLhs(LHS lhs) {
		this.lhs = lhs;
	}
	public Operator getOpp() {
		return opp;
	}
	public void setOpp(Operator opp) {
		this.opp = opp;
	}
	public RHS getRhs() {
		return rhs;
	}
	public void setRhs(RHS rhs) {
		this.rhs = rhs;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
}
