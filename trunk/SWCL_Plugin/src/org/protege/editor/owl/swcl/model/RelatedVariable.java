package org.protege.editor.owl.swcl.model;

import org.semanticweb.owlapi.model.OWLDatatype;

public class RelatedVariable extends Variable{
	
	public RelatedVariable(String name, String description) {
		super(name, description);
		// TODO Auto-generated constructor stub
	}
	private Variable hasValue;
	private OWLDatatype datatypeProperty;
	
	public Variable getHasValue() {
		return hasValue;
	}
	public void setHasValue(Variable hasValue) {
		this.hasValue = hasValue;
	}
	public OWLDatatype getDatatypeProperty() {
		return datatypeProperty;
	}
	public void setDatatypeProperty(OWLDatatype datatypeProperty) {
		this.datatypeProperty = datatypeProperty;
	}
	
	
}
