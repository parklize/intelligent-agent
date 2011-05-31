package org.protege.editor.owl.swcl.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLProperty;

public class SWCLOntologyHelper {

	private OWLOntology ont;
	private OWLOntologyManager owlManager;
	
	
	public SWCLOntologyHelper(OWLOntology ont){
		this.ont = ont;
		this.owlManager = ont.getOWLOntologyManager();
	}
	
	public ArrayList<String> getPropertyList(){
		ArrayList<String> propertyList = new ArrayList<String>();
		Set dataTypePropertySet = ont.getDataPropertiesInSignature();
		Iterator it = dataTypePropertySet.iterator();
		while(it.hasNext()){
			propertyList.add(getPropertyName((OWLProperty)it.next()));
		}
		return propertyList;
	}
	
	// return class name without prefix
	public String getClassName(OWLClass owlClass){
		String classNameWithoutPrefix = "";
		String iriName = owlClass.getIRI().toString();
		StringTokenizer st = new StringTokenizer(iriName,"#");
		while(st.hasMoreTokens()){
			classNameWithoutPrefix = st.nextToken();
		}
		return classNameWithoutPrefix;
	}
	
	// return property name without prefix
	public String getPropertyName(OWLProperty owlProperty){
		String propertyNameWithoutPrefix = "";
		String iriName = owlProperty.getIRI().toString();
		StringTokenizer st = new StringTokenizer(iriName,"#");
		while(st.hasMoreTokens()){
			propertyNameWithoutPrefix = st.nextToken();
		}
		return propertyNameWithoutPrefix;
		
	}
}
