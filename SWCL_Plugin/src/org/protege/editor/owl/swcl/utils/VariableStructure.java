package org.protege.editor.owl.swcl.utils;

import java.util.ArrayList;

import org.semanticweb.owlapi.model.OWLIndividual;

public class VariableStructure {

	private OWLIndividual parent;
	private ArrayList<OWLIndividual> childrens = new ArrayList<OWLIndividual>();
	
	public OWLIndividual getParent() {
		return parent;
	}
	public void setParent(OWLIndividual parent) {
		this.parent = parent;
	}
	public ArrayList<OWLIndividual> getChildrens() {
		return childrens;
	}
	public void setChildrens(ArrayList<OWLIndividual> childrens) {
		this.childrens = childrens;
	}
	
	
}
