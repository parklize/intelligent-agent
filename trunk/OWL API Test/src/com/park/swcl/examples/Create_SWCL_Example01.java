package com.park.swcl.examples;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

/**
 * @author parklize
 * Date: 2011.4.11
 * Description: create swcl classes and instances
 */

public class Create_SWCL_Example01 {
	
	public static void main(String[] args) {
		
		try {
			// create ontology manager to work with
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLDataFactory dataFactory = manager.getOWLDataFactory();
			
			// set base
			String base = "http://iwec.yonsei.ac.kr/swcl";
			PrefixManager pm = new DefaultPrefixManager(base);
			
			// get the reference to the Constraint class
			OWLClass constraint = dataFactory.getOWLClass("#Constraint",pm);
			
			// get the reference to the Variable class(create)
			OWLClass variable = dataFactory.getOWLClass("#Variable",pm);
			
			// get the reference to the numberOfPopulation instance
			OWLNamedIndividual numberOfPopulation = dataFactory.getOWLNamedIndividual("#numberOfPopulation",pm);
			
			// get the reference to the y instance 
			OWLIndividual y = dataFactory.getOWLNamedIndividual("#y",pm);
			
			// create class assertion that numberOfPopulation is the instance of constraint class
			OWLClassAssertionAxiom classAssertion = dataFactory.getOWLClassAssertionAxiom(constraint, numberOfPopulation);
			
			// create class assertion that y is the instance of the Variable
			OWLClassAssertionAxiom yClassAssertion = dataFactory.getOWLClassAssertionAxiom(variable, y);
			
			// create Object property named qualifier
			OWLObjectProperty qualifier = dataFactory.getOWLObjectProperty("#qualifier",pm);
			
			// create Object property assertion that numberOfPopulation qualifier y
			OWLObjectPropertyAssertionAxiom qualifierObjectPropertyAssertion = dataFactory.getOWLObjectPropertyAssertionAxiom(qualifier, numberOfPopulation, y);
			
			// create an ont to add the assertion
			OWLOntology ontology = manager.createOntology(IRI.create(base));
				
			// add the class assertion to an ontoloty
			manager.addAxiom(ontology,classAssertion);
			manager.addAxiom(ontology, yClassAssertion);
			manager.addAxiom(ontology, qualifierObjectPropertyAssertion);
			
			
			// dump the ontology to the sysout
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
