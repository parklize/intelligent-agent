package com.park.test;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFIndividual;

public class SWRLTest {
	public static void main(String[] args) {
		
		try {
			//---create jena model
			OWLModel owlModel = ProtegeOWL.createJenaOWLModel();
			//---create person calss
			OWLNamedClass personClass = owlModel.createOWLNamedClass("Person");
			//---add properties to person class
			OWLObjectProperty hasParent = owlModel.createOWLObjectProperty("hasParent");
			hasParent.setDomain(personClass);
			hasParent.setRange(personClass);
			
			OWLObjectProperty hasSister = owlModel.createOWLObjectProperty("hasSister");
			hasSister.setDomain(personClass);
			hasSister.setRange(personClass);
			
			//---make individual
			RDFIndividual sara = personClass.createRDFIndividual("Sara");//parent of Darwin
			RDFIndividual darwin = personClass.createRDFIndividual("Darwin");
			RDFIndividual serina = personClass.createRDFIndividual("Serina");//sister of Darwin
			
			darwin.setPropertyValue(hasParent, sara);//set sara as Darwin's parent
			darwin.setPropertyValue(hasSister, serina);//set serina as Darwin's sister
			
			
		} catch (OntologyLoadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
