package org.protege.editor.owl.swcl.model;
/**
 * @author parklize
 * @version 1.0, 2011-04-23
 */
public class Qualifier {
	
	public Qualifier(Variable v) {
		super();
		this.v = v;
	}

	private Variable v;

	public Variable getV() {
		return v;
	}

	public void setV(Variable v) {
		this.v = v;
	}
	
}
