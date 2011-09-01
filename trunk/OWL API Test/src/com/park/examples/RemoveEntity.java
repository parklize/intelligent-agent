package com.park.examples;

import java.util.Collections;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.OWLEntityRemover;

public class RemoveEntity {
	
	public static void main(String[] args) {
		
		try {
			
			OWLOntologyManager man = OWLManager.createOWLOntologyManager();
			OWLOntology ont = man.loadOntologyFromOntologyDocument(IRI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl"));
			OWLEntityRemover remover = new OWLEntityRemover(man, Collections.singleton(ont));
System.out.println("Number of individuals: " + ont.getIndividualsInSignature().size());
			for (OWLNamedIndividual ind : ont.getIndividualsInSignature()) {
		                ind.accept(remover);
		    }
			man.applyChanges(remover.getChanges());
System.out.println("Number of individuals: " + ont.getIndividualsInSignature().size());
			remover.reset();
			
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
