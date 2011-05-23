package org.protege.editor.owl.swcl.model;

import org.semanticweb.owlapi.model.OWLProperty;

public class Factor {

	private Variable v;
	private OWLProperty owlProperty;
	
	public Variable getV() {
		return v;
	}
	public void setV(Variable v) {
		this.v = v;
	}
	public OWLProperty getOwlProperty() {
		return owlProperty;
	}
	public void setOwlProperty(OWLProperty owlProperty) {
		this.owlProperty = owlProperty;
	}
	
	
}
