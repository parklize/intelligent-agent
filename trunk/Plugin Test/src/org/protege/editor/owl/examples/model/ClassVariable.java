package org.protege.editor.owl.examples.model;

import org.semanticweb.owlapi.model.OWLClass;

public class ClassVariable extends Variable{
	
	private OWLClass bindingClass;

	public OWLClass getBindingClass() {
		return bindingClass;
	}

	public void setBindingClass(OWLClass bindingClass) {
		this.bindingClass = bindingClass;
	}
	
	
}
