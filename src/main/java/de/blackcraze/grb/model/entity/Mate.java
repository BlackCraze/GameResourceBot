package de.blackcraze.grb.model.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import de.blackcraze.grb.model.AbstractNamed;
import de.blackcraze.grb.model.Device;

@Entity
@Table(name = "MATE")
public class Mate extends AbstractNamed {

    private static final long serialVersionUID = 1453233261285704063L;

    @Column(nullable = false, unique = true)
    private String discordId;

    @Column
    private String language;

    @Enumerated(EnumType.STRING)
    @Column
    private Device device;

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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

}
