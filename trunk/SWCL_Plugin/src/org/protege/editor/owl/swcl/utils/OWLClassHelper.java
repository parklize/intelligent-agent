package org.protege.editor.owl.swcl.utils;

import java.util.StringTokenizer;

import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

public class OWLClassHelper {
	
	private OWLClassImpl owlClass = null;
	private StringTokenizer st = null;
	private String className = null;
	
	public OWLClassHelper(OWLClassImpl owlClass){
		this.owlClass = owlClass;
	}
	
	public String getClassName(){
		String iriName = owlClass.getIRI().toString();
		st = new StringTokenizer(iriName,"#");
		while(st.hasMoreTokens()){
			this.className = st.nextToken();
		}
		return this.className;
	}
}
