/**
 * Description: load ont
 * Author: PIAO GUANGYUAN
 * Date: 2011.4.14
 * Referece: owl api doc
 */
package com.park.examples;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.coode.owlapi.rdf.model.RDFGraph;
import org.coode.owlapi.rdf.model.RDFNode;
import org.coode.owlapi.rdf.model.RDFTranslator;
import org.coode.owlapi.rdfxml.parser.AnonymousNodeChecker;
import org.coode.owlapi.rdfxml.parser.OWLRDFConsumer;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import uk.ac.manchester.cs.owl.owlapi.turtle.parser.OWLRDFConsumerAdapter;
import uk.ac.manchester.cs.owl.owlapi.turtle.parser.TurtleParser;

public class Loading_Ontology {

	public static void main(String[] args) {

		try {
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLDataFactory factory = manager.getOWLDataFactory();
			
			// set base
			String base = "http://www.iwec.yonsei.ac.kr/ontology/polulation.owl";
			PrefixManager pm = new DefaultPrefixManager(base);
			
			IRI iri = IRI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl");
			OWLOntology pizzaOntology;
			
			pizzaOntology = manager.loadOntologyFromOntologyDocument(iri);
			System.out.println("Loaded ontology: " + pizzaOntology);
			
			// load from local
			File file = new File("C:\\Documents and Settings\\parklize\\πŸ≈¡ »≠∏È\\temp\\1c.owl");
			OWLOntology local = manager.loadOntologyFromOntologyDocument(file);
			manager.saveOntology(local, new SystemOutDocumentTarget());
			
			OWLNamedIndividual y = factory.getOWLNamedIndividual(IRI.create(base+"#?y"));
			OWLAnnotationProperty bindingClass = factory.getOWLAnnotationProperty(IRI.create(base+"#bindingClass"));
			Set annotationAssertion = y.getAnnotationAssertionAxioms(local);
			Set ann = y.getAnnotations(local, bindingClass);
			if(ann != null){
				Iterator it = ann.iterator();
				while(it.hasNext()){
					System.out.println(it.next());
				}
			}
			RDFTranslator rt = new RDFTranslator(manager,local,true);
			
			RDFGraph rg = rt.getGraph();
			rg.dumpTriples(new FileWriter("D:\\tem.owl"));
			// test binding class type
//			RDFNode y = (RDFNode) factory.get
//			OWLRDFConsumerAdapter oca = new OWLRDFConsumerAdapter(manager, local);
//			Set<IRI> predicts = oca.getPredicatesBySubject(IRI.create(base+"#?y"));
//			Iterator it = predicts.iterator();
//			while(it.hasNext()){
//				System.out.println(it.next());
//			}
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
