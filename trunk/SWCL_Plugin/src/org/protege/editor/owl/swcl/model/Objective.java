package org.protege.editor.owl.swcl.model;

import java.util.ArrayList;

public class Objective {

	private String optimizationInstruction;
	private ArrayList<TermBlock> objectiveTerm = new ArrayList<TermBlock>();
	
	public String getOptimizationInstruction() {
		return optimizationInstruction;
	}
	public void setOptimizationInstruction(String optimizationInstruction) {
		this.optimizationInstruction = optimizationInstruction;
	}
	public ArrayList<TermBlock> getObjectiveTerm() {
		return objectiveTerm;
	}
	public void setObjectiveTerm(ArrayList<TermBlock> objectiveTerm) {
		this.objectiveTerm = objectiveTerm;
	}
	
	
}
