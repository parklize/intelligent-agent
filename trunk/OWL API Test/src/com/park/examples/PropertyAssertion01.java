/**
 * @author parklize
 * Date: 2011.4.11
 * Description: Property Assertion (Mathew has father who is Peter)
 * Referece: owl api doc
 */
package com.park.examples;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;


public class PropertyAssertion01 { 
	
	public static void main(String[] args) {
		try {
			// create ac ontology manager to work with
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLDataFactory factory = manager.getOWLDataFactory();
			
			// set base
			String base = "http://iwec.yonsei.ac.kr/swcl";
			PrefixManager pm = new DefaultPrefixManager(base);
			
			// create ont
			OWLOntology ont = manager.createOntology(IRI.create(base));
			
			// create individuals named matthew and petter
			OWLIndividual matthew = factory.getOWLNamedIndividual(IRI.create(base + "#matthew"));
			OWLIndividual peter = factory.getOWLNamedIndividual(IRI.create(base + "#petter"));
			
			// link subject to object
			OWLObjectProperty hasFather = factory.getOWLObjectProperty(IRI.create(base + "#hasFather"));
			
			// create actual assertion(triple) that matthew hasFather peter
			OWLObjectPropertyAssertionAxiom assertion = factory.getOWLObjectPropertyAssertionAxiom(hasFather, matthew, peter);
			
			// add axiom to our ont and save
			AddAxiom addAxiomChange = new AddAxiom(ont,assertion);
			manager.applyChange(addAxiomChange);
			
			// specify that matthew is an instance of Person class
			OWLClass person = factory.getOWLClass(IRI.create(base + "#Person"));
			OWLClassAssertionAxiom ax = factory.getOWLClassAssertionAxiom(person, matthew);
			
			manager.addAxiom(ont, ax);
			
			manager.saveOntology(ont, new SystemOutDocumentTarget());
			
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
