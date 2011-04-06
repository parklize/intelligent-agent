package com.park.test;

import java.io.InputStream;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.FileManager;


public class OwlLoadTest {
	
	public static void main(String argsp[]){
			Model m = ModelFactory.createDefaultModel();//create model with maximum compatibility
			InputStream owlFile = FileManager.get().open("F:\\workspace\\SWCL\\Jena_API_Practise\\doc\\john.rdf");
			
			//---read the RDF/XML file
			m.read(owlFile, null);
			
			//---Return a copy of the internal mapping from names to URI strings
//			System.out.println(m.getNsPrefixMap());
			
			//---write it to standart out
//			m.write(System.out);
			
			//---list the statements in the model
//			StmtIterator iter = m.listStatements();
			
			//---print out the predicate, subject and object of each statement
//			while (iter.hasNext()) {
//			    Statement stmt      = iter.nextStatement();  // get next statement
//			    Resource  subject   = stmt.getSubject();     // get the subject
//			    Property  predicate = stmt.getPredicate();   // get the predicate
//			    RDFNode   object    = stmt.getObject();      // get the object
//
//			    System.out.print(subject.toString());
//			    System.out.print(" " + predicate.toString() + " ");
//			    if (object instanceof Resource) {
//			       System.out.print(object.toString());
//			    } else {
//			        // object is a literal
//			        System.out.print(" \"" + object.toString() + "\"");
//			    }
//
//			    System.out.println(" .");
//			} 
			
//		    Property family = m.getProperty("http://www.w3.org/2001/vcard-rdf/3.0#Family");
//		    Property given = m.getProperty("http://www.w3.org/2001/vcard-rdf/3.0#Given");
//		    
//		    ResIterator iter = m.listSubjectsWithProperty(family);//An iterator which returns RDF Resources.
//		    if(iter.hasNext()){
//		    	System.out.println("The database contains family for:");
//		    	while(iter.hasNext()){
//		    		System.out.println(" " + iter.next().getProperty(given).getString());
//		    	}
//		    }
			
	}
	
}
