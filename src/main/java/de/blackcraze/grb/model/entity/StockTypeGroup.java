package de.blackcraze.grb.model.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import de.blackcraze.grb.model.AbstractNamed;

@Entity
@Table(name = "STOCK_TYPE_GROUP")
public class StockTypeGroup extends AbstractNamed {

    /** The serialVersionUID. */
    private static final long serialVersionUID = 2316406327909862146L;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "STOCK_TYPE_GROUP_JOIN", joinColumns = @JoinColumn(name = "TYPE_ID"), inverseJoinColumns = @JoinColumn(name = "GROUP_ID"))
    @OrderBy("name")
    @Fetch(FetchMode.SELECT)
    private List<StockType> types;

    public List<StockType> getTypes() {
        return types;
    }

    public void setTypes(List<StockType> types) {
        this.types = types;
    }

}
