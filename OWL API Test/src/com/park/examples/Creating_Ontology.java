/**
 * Description: create axiom that class B is a subclass of class A
 * Author: PIAO GUANGYUAN
 * Date: 2011.4.14
 * Referece: owl api doc
 */
package com.park.examples;

import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

public class Creating_Ontology {

	public static void main(String[] args) {
		try {
			// Create the manager that we will use to load ont
			OWLOntologyManager  manager = OWLManager.createOWLOntologyManager();
			
			// Create an ont with the name of "http://yonsei.ac.kr/parklize/example01.owl"
			IRI ontologyIRI = IRI.create("http://yonsei.ac.kr/parklize/example.owl");
			
			// Create doc IRI for our ont in F disk(seems related to your workspace disk)
			IRI documentIRI = IRI.create("file:/tmp/MyOnt.owl");
			
			// Set up a mapping between two IRIs made above
			SimpleIRIMapper mapper = new SimpleIRIMapper(ontologyIRI,documentIRI);
			manager.addIRIMapper(mapper);
			
			// Now create ont using ontology URI
			OWLOntology ontology = manager.createOntology(ontologyIRI);
			
			// We need a data factory to create various object from.
			OWLDataFactory factory = manager.getOWLDataFactory();
			
			OWLClass clsA = factory.getOWLClass(IRI.create(ontologyIRI + "#A"));
			OWLClass clsB = factory.getOWLClass(IRI.create(ontologyIRI + "#B"));
			
			// Now create the axiom
			OWLAxiom axiom = factory.getOWLSubClassOfAxiom(clsB, clsA);
			
			// Add the axiom to the ont
			AddAxiom addAxiom = new AddAxiom(ontology,axiom);
			
			// Use the manager to apply the change
			manager.applyChange(addAxiom);
			
			// Class A and B R contained within the SIGNATURE of the ont,, print it
			for (OWLClass cls : ontology.getClassesInSignature()) {
				System.out.println("Referenced class: " + cls);
			}
			
			// We should also find out B is subclass of A
			Set<OWLClassExpression> superClasses = clsB.getSuperClasses(ontology);
			System.out.println("Asserted superclasses of " + clsB + ":");
			for (OWLClassExpression desc : superClasses) {
				System.out.println(desc);
			}
			
			// Save the ont
			manager.saveOntology(ontology);
			
//			manager.saveOntology(ontology,new SystemOutDocumentTarget());
			
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
