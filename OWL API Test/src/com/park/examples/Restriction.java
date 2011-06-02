package com.park.examples;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

public class Restriction {

	public static void main(String[] args) {

		try {
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			String base = "http://org.semanticweb.restrictionexample";
			OWLOntology ont = manager.createOntology(IRI.create(base));
			
		    OWLDataFactory factory = manager.getOWLDataFactory();
		    OWLObjectProperty hasPart = factory.getOWLObjectProperty(IRI.create(base + "#hasPart"));
		    OWLClass nose = factory.getOWLClass(IRI.create(base + "#Nose"));
		    OWLClassExpression hasPartSomeNose = factory.getOWLObjectSomeValuesFrom(hasPart, nose);
		    
		    OWLClass head = factory.getOWLClass(IRI.create(base + "#Head"));
		    OWLSubClassOfAxiom ax = factory.getOWLSubClassOfAxiom(head, hasPartSomeNose);
		    
		    AddAxiom addAx = new AddAxiom(ont, ax);
		    manager.applyChange(addAx);
		    
		 // dump the ontology to the sysout
			manager.saveOntology(ont,new SystemOutDocumentTarget());
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
