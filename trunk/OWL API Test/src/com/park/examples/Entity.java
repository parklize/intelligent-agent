package com.park.examples;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

public class Entity {
	
	public static void main(String[] args) {
		

		try {
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			
			OWLDataFactory factory = manager.getOWLDataFactory();
			
			OWLOntology ontology = manager.createOntology(IRI.create("http://www.semanticweb.org/owlapi/ontologies/ontology"));
			
			PrefixManager pm = new DefaultPrefixManager("http://www.semanticweb.org/owlapi/ontologies/ontology#");
			// Now we use the prefix manager and just specify an abbreviated IRI
			OWLClass clsAMethodB = factory.getOWLClass(":A", pm);
			
			OWLDeclarationAxiom declarationAxiom = factory.getOWLDeclarationAxiom(clsAMethodB);
			manager.addAxiom(ontology, declarationAxiom);
			
			manager.saveOntology(ontology, new SystemOutDocumentTarget());
			
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
