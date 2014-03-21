package org.protege.editor.owl.swcl.model;

import java.util.ArrayList;
/**
 * @author parklize
 * @version 1.0, 2011-04-23
 */
public class OptModel {
	
	private ArrayList<Constraint> constraints;
	private Objective obj;
	
	public ArrayList<Constraint> getConstraints() {
		return constraints;
	}
	public void setConstraints(ArrayList<Constraint> constraints) {
		this.constraints = constraints;
	}
	public Objective getObj() {
		return obj;
	}
	public void setObj(Objective obj) {
		this.obj = obj;
	}
	
	
	
}
