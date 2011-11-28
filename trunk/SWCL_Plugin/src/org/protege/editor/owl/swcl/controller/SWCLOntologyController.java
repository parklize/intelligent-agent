package org.protege.editor.owl.swcl.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLProperty;
/**
 * @author parklize
 * @version 1.0, 2011-11-23
 */
public class SWCLOntologyController {

	private OWLOntology ont;
	private OWLOntologyManager owlManager;
	
	
	public SWCLOntologyController(OWLOntology ont){
		this.ont = ont;
		this.owlManager = ont.getOWLOntologyManager();
	}
	
	/**
	 * 
	 * @return prefix
	 */
	public String getPrefix(){
		// get prefix
		String ontString = ont.toString();
		int start = ontString.indexOf("<");
		int end = ontString.indexOf(">");
		
		return ontString.substring(start+1, end);
	}
	
	/**
	 * 
	 * @return get all datatype properties without prefix
	 */
	public ArrayList<String> getPropertyList(){
		ArrayList<String> propertyList = new ArrayList<String>();
		Set dataTypePropertySet = ont.getDataPropertiesInSignature();
		Iterator it = dataTypePropertySet.iterator();
		while(it.hasNext()){
			propertyList.add(getPropertyName((OWLProperty)it.next()));
		}
		return propertyList;
	}
	
	/**
	 * 
	 * @return get all objective properties without prefix
	 */
	public ArrayList<String> getObjectPropertyList(){
		ArrayList<String> propertyList = new ArrayList<String>();
		Set objectTypePropertySet = ont.getObjectPropertiesInSignature();
		Iterator it = objectTypePropertySet.iterator();
		while(it.hasNext()){
			propertyList.add(getPropertyName((OWLProperty)it.next()));
		}
		return propertyList;
	}
	
	/**
	 * 
	 * @return get all classes without prefix
	 */
	public ArrayList<String> getClassList(){
		ArrayList<String> classList = new ArrayList<String>();
		Set classSet = ont.getClassesInSignature();
		Iterator it = classSet.iterator();
		while(it.hasNext()){
			classList.add(getClassName((OWLClass) it.next()));
		}
		return classList;
	}
	
	/**
	 * 
	 * @return get all individuals without prefix
	 */
	public ArrayList<String> getIndividualsList(){
		ArrayList<String> indList = new ArrayList<String>();
		Set indSet = ont.getIndividualsInSignature();
		Iterator it = indSet.iterator();
		while(it.hasNext()){
			indList.add(getIndividualName((OWLIndividual) it.next()));
		}
		return indList;
	}
	
	/**
	 * 
	 * @param OWLClass
	 * @return class name without prefix
	 */
	public String getClassName(OWLClass owlClass){
		String classNameWithoutPrefix = "";
		String iriName = owlClass.getIRI().toString();
		StringTokenizer st = new StringTokenizer(iriName,"#");
		while(st.hasMoreTokens()){
			classNameWithoutPrefix = st.nextToken();
		}
		return classNameWithoutPrefix;
	}
	
	/**
	 * 
	 * @param OWLIndividual
	 * @return individual name without prefix
	 */
	public String getIndividualName(OWLIndividual ind){
		String owlIndWithoutPrefix = "";
		String iriName = ((OWLNamedIndividual) ind).getIRI().toString();
		StringTokenizer st = new StringTokenizer(iriName,"#");
		while(st.hasMoreTokens()){
			owlIndWithoutPrefix = st.nextToken();
		}
		return owlIndWithoutPrefix;
	}
	
	/**
	 * 
	 * @param OWL class name
	 * @return get owl class with specific name
	 */
	public OWLClass getOWLClass(String name){
		Set classSet = ont.getClassesInSignature();
		Iterator it = classSet.iterator();
		while(it.hasNext()){
			OWLClass owlClass = (OWLClass) it.next();
			if(name.equals(getClassName(owlClass))){
				return owlClass;
			}
//			classList.add(getClassName((OWLClass) it.next()));
		}
		return null;
	}
	
	/**
	 * 
	 * @param OWL Individual name
	 * @return get owl individual with specific name
	 */
	public OWLIndividual getOWLIndividual(String name){
		Set individualSet = ont.getIndividualsInSignature();
		Iterator it = individualSet.iterator();
		while(it.hasNext()){
			OWLIndividual owlIndividual = (OWLIndividual) it.next();
			if(name.equals(getIndividualName((OWLNamedIndividual) owlIndividual))){
				return owlIndividual;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param OWLProperty
	 * @return property name without prefix
	 */
	public String getPropertyName(OWLProperty owlProperty){
		String propertyNameWithoutPrefix = "";
		String iriName = owlProperty.getIRI().toString();
		StringTokenizer st = new StringTokenizer(iriName,"#");
		while(st.hasMoreTokens()){
			propertyNameWithoutPrefix = st.nextToken();
		}
		return propertyNameWithoutPrefix;
	}
	
	/**
	 * 
	 * @param String with prefix
	 * @param prefix
	 * @return  get without prefix
	 */
	public String getWithoutPrefix(String prefixStr,String prefix){
		
		String newStr = prefixStr;
		newStr = newStr.replaceAll("<"+prefix, "");
		newStr = newStr.replaceAll(">", "");
		
		return newStr;
	}
	
}
