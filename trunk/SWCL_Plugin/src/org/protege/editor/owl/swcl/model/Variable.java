package org.protege.editor.owl.swcl.model;
/**
 * @author parklize
 * @version 1.0, 2011-04-23
 */
public class Variable {
	
	private String name;
	private String description;

	public Variable(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
