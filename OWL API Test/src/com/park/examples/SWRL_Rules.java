package com.park.examples;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyDocumentTarget;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.io.WriterDocumentTarget;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

public class SWRL_Rules {

	public static void main(String[] args) {
		

		
		try {
			
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			
			IRI ontologyIRI = IRI.create("http://www.co-ode.org/ontologies/testont.owl");
			IRI documentIRI = IRI.create("file:/tmp/SWRLTest.owl");
			
			// Set up a mapping, which maps the ontology to the document IRI
			SimpleIRIMapper mapper = new SimpleIRIMapper(ontologyIRI, documentIRI);
			manager.addIRIMapper(mapper);
			
			OWLOntology ontology = manager.createOntology(ontologyIRI);
			OWLDataFactory factory = manager.getOWLDataFactory();
			
			
			OWLClass clsA = factory.getOWLClass(IRI.create(ontologyIRI+"#A"));
			OWLClass clsB = factory.getOWLClass(IRI.create(ontologyIRI+"#B"));
			
			SWRLVariable var = factory.getSWRLVariable(IRI.create(ontologyIRI+"#x"));
			SWRLRule rule = factory.getSWRLRule(
					Collections.singleton(factory.getSWRLClassAtom(clsA, var)),
					Collections.singleton(factory.getSWRLClassAtom(clsB, var))
			);
			
			manager.applyChange(new AddAxiom(ontology,rule));
			
			// dumping
			manager.saveOntology(ontology, new SystemOutDocumentTarget());
			// save to document
			WriterDocumentTarget wdt = new WriterDocumentTarget(new FileWriter("D://x.owl"));
			manager.saveOntology(ontology,wdt);
			
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
