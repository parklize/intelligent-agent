package com.park;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;

public class OWLModel {
	
	public static void main(String[] args) {
		
		try {
			JenaOWLModel owlModel = ProtegeOWL.createJenaOWLModelFromReader(new FileReader("F:\\workspace\\SWCL\\Jena_API_Practise\\doc\\ShoppinOntology.owl"));
			System.out.println(owlModel.getOWLIndividuals());
		} catch (OntologyLoadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
	}
	
}
