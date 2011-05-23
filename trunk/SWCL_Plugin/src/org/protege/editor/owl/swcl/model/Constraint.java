package org.protege.editor.owl.swcl.model;

public class Constraint {
	
	private Qualifier qualifier;
	private LHS lsh;
	private Operator opp;
	private RHS rhs;
	
	public Qualifier getQualifier() {
		return qualifier;
	}
	public void setQualifier(Qualifier qualifier) {
		this.qualifier = qualifier;
	}
	public LHS getLsh() {
		return lsh;
	}
	public void setLsh(LHS lsh) {
		this.lsh = lsh;
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

	
}
