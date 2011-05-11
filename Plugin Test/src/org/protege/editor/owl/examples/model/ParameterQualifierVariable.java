package org.protege.editor.owl.examples.model;

public class ParameterQualifierVariable extends Variable{
	
	private Qualifier qualifer;
	private String onProperty;
	
	public Qualifier getQualifer() {
		return qualifer;
	}
	public void setQualifer(Qualifier qualifer) {
		this.qualifer = qualifer;
	}
	public String getOnProperty() {
		return onProperty;
	}
	public void setOnProperty(String onProperty) {
		this.onProperty = onProperty;
	}
	
}
