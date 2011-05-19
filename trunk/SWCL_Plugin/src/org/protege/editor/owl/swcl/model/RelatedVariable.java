package org.protege.editor.owl.swcl.model;

import org.semanticweb.owlapi.model.OWLDatatype;

public class RelatedVariable extends Variable{
	
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
