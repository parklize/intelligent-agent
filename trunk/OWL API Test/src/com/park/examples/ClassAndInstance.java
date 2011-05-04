/**
 * Description: create instance for ont class
 * Author: PIAO GUANGYUAN
 * Date: 2011.4.14
 * Referece: owl api doc
 */
package com.park.examples;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

public class ClassAndInstance {
	
	public static void main(String[] args) {
		try {
			
			// create an ont manager to work with
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLDataFactory dataFactory = manager.getOWLDataFactory();
			
			// set base
			String base = "http://iwec.yonsei.ac.kr/swcl/";
			PrefixManager pm = new DefaultPrefixManager(base);
			
			// get the reference to the :Person class (full IRI will be http://iwec.yonsei.ac.kr/swcl/Person)
			OWLClass person = dataFactory.getOWLClass(":Person",pm);
			
			// get the reference to the :Mary individual
			OWLNamedIndividual mary = dataFactory.getOWLNamedIndividual(":Mary",pm);
			
			// create class assertion to specify that Mary is an instance of Person
			OWLClassAssertionAxiom classAssertion = dataFactory.getOWLClassAssertionAxiom(person, mary);
			
			// create ontology and add infos created above
			OWLOntology ontology = manager.createOntology(IRI.create(base));
			
			// add class assertion
			manager.addAxiom(ontology, classAssertion);
			
			// dump the ontology to stdout
			manager.saveOntology(ontology,new SystemOutDocumentTarget());
			
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
