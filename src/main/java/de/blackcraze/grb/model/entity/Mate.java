package de.blackcraze.grb.model.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import de.blackcraze.grb.model.AbstractNamed;

@Entity
@Table(name = "MATE")
public class Mate extends AbstractNamed {

	private static final long serialVersionUID = 1453233261285704063L;

	@Column(nullable = false, unique = true)
	private String discordId;

	@OneToMany(mappedBy = "mate", fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<Building> buildings;

	@OneToMany(mappedBy = "mate", fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<Stock> stocks;

	public Set<Building> getBuildings() {
		return buildings;
	}

	public Set<Stock> getStocks() {
		return stocks;
	}

	public String getDiscordId() {
		return discordId;
	}

	public void setDiscordId(String discordId) {
		this.discordId = discordId;
	}

}
