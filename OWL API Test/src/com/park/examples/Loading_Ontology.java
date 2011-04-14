/**
 * Description: load ont
 * Author: PIAO GUANGYUAN
 * Date: 2011.4.14
 * Referece: owl api doc
 */
package com.park.examples;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class Loading_Ontology {

	public static void main(String[] args) {

		try {
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			
			IRI iri = IRI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl");
			OWLOntology pizzaOntology;
			
			pizzaOntology = manager.loadOntologyFromOntologyDocument(iri);
			System.out.println("Loaded ontology: " + pizzaOntology);
			
			File file = new File("/tmp/pizza.owl");
			
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
