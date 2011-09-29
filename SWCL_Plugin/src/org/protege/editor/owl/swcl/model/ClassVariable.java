package org.protege.editor.owl.swcl.model;

import org.semanticweb.owlapi.model.OWLClass;

public class ClassVariable extends Variable{
	
	public ClassVariable(String name, String description) {
		super(name, description);
	}

	private OWLClass bindingClass;

	public OWLClass getBindingClass() {
		return bindingClass;
	}

	public void setBindingClass(OWLClass bindingClass) {
		this.bindingClass = bindingClass;
	}
	
	
}