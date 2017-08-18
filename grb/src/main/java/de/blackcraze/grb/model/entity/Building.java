package de.blackcraze.grb.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.blackcraze.grb.model.AbstractUpdatable;

@Entity
@Table(name = "BUILDING")
public class Building extends AbstractUpdatable {

	private static final long serialVersionUID = -7525527979309411891L;

	@Column(nullable = false)
	private int level;

	@ManyToOne
	@JoinColumn(nullable = false, name = "MATE_ID")
	private Mate mate;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
		update();
	}

	public Mate getMate() {
		return mate;
	}

	public void setMate(Mate mate) {
		this.mate = mate;
	}

}
