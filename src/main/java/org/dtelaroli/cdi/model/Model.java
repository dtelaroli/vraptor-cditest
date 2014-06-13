package org.dtelaroli.cdi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Model {
	
	@Id @GeneratedValue
	protected Long id;
	
	protected String name;

	@Override
	public String toString() {
		return "id: " + id + " name: " + name;
	}
	
}
