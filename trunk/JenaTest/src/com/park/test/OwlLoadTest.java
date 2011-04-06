package com.park.test;

import java.io.InputStream;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;

public class OwlLoadTest {
	
	public static void main(String argsp[]){
			Model m = ModelFactory.createDefaultModel();//create model with maximum compatibility
			InputStream owlFile = FileManager.get().open("F:\\workspace\\SWCL\\Jena_API_Practise\\doc\\ibs_homework.rdf");
			
			// read the RDF/XML file
			m.read(owlFile, null);
			
			// write it to standart out
			m.write(System.out);
			
//			// list the statements in the model
//			StmtIterator iter = m.listStatements();
//			
//			// print out the predicate, subject and object of each statement
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
			
			
	}
	
}
