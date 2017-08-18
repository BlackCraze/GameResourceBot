package de.blackcraze.grb.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractUpdatable extends BaseEntity {

	private static final long serialVersionUID = 3484504637932715768L;

	@Column
	private Date updated;

	public Date getUpdated() {
		return updated;
	}

	public void update() {
		updated = new Date();
	}
}
