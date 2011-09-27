package com.park.examples;


import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;


public class GetInverseProperty {

	public static void main(String[] args) {

		try {
			File file = new File("Ontology/ve2addedObj.owl");
			
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLDataFactory dataFactory = manager.getOWLDataFactory();
			
			// Load the local copy
			OWLOntology ve = manager.loadOntologyFromOntologyDocument(file);
//			manager.saveOntology(ve,new SystemOutDocumentTarget());
			SWCLOntologyController soc = new SWCLOntologyController(ve);
			String prefix = soc.getPrefix();
			PrefixManager pm = new DefaultPrefixManager(prefix);
			
			OWLObjectProperty produceWeekOf = dataFactory.getOWLObjectProperty(IRI.create(prefix+"#produceWeekOf"));
			produceWeekOf.getInverses(ve);//[<http://www.semanticweb.org/ontologies/2011/8/Ontology1314840683578.owl#hasProduceWeek>]
			
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
