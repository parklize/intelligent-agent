package model;

import java.util.ArrayList;

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
