package com.park;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.Jena;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFIndividual;
import edu.stanford.smi.protegex.owl.swrl.SWRLRuleEngine;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRuleEngineFactory;
import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLRuleEngineException;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParseException;

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
			
			//---print current owlModel to console
//			Jena.dumpRDF(owlModel.getOntModel());
			
			SWRLFactory factory = new SWRLFactory(owlModel);
			SWRLRuleEngine ruleEngine = SWRLRuleEngineFactory.create(owlModel);
			SWRLImp imp = factory.createImp("HasParent-Rule","hasParent(?x,?y) ^ hasSister(?x,?z) -> hasParent(?z,?y)");
			
			ruleEngine.infer();
			ruleEngine.writeInferredKnowledge2OWL();
			//---print current owlModel to console
			Jena.dumpRDF(owlModel.getOntModel());
			
		} catch (OntologyLoadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SWRLParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SWRLRuleEngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
}
