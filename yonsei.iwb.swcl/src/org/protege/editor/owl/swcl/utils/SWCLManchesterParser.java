package org.protege.editor.owl.swcl.utils;

import org.protege.editor.owl.swcl.model.Variable;
import org.semanticweb.owlapi.model.OWLOntology;
/**
 * @author parklize
 * @version 1.0, 2011-11-23
 */
public class SWCLManchesterParser {
	
	public void parse(String str){
		
		int a = str.indexOf("11");
		String b = str.replaceAll("Country", "aCountry");
		
		System.out.println(b);
	}
	
	public static void main(String[] args) {
		SWCLManchesterParser smp = new SWCLManchesterParser();
		smp.parse("Country");
	}
}
