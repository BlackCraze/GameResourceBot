package de.blackcraze.grb.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractNamed extends AbstractUpdatable {

	private static final long serialVersionUID = 4608310922013562750L;

	@Column(nullable = false)
	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		update();
	}

}
