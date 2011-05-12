package org.protege.editor.owl.examples.model;

public class ParameterRestrictionVariable extends Variable{
	
	private Variable hasValue;
	private String onProperty;
	
	public Variable getHasValue() {
		return hasValue;
	}
	public void setHasValue(Variable hasValue) {
		this.hasValue = hasValue;
	}
	public String getOnProperty() {
		return onProperty;
	}
	public void setOnProperty(String onProperty) {
		this.onProperty = onProperty;
	}
	

}
