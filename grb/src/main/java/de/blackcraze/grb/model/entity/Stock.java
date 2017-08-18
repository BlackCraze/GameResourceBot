package de.blackcraze.grb.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import de.blackcraze.grb.model.AbstractUpdatable;

@Entity
@Table(name = "STOCK", uniqueConstraints = { @UniqueConstraint(columnNames = { "MATE_ID", "STOCK_TYPE_ID" }) })
public class Stock extends AbstractUpdatable {

	private static final long serialVersionUID = 1905329000612462311L;

	@Column(nullable = false)
	private long amount;

	@ManyToOne
	@JoinColumn(nullable = false, name = "STOCK_TYPE_ID")
	private StockType type;

	@ManyToOne
	@JoinColumn(nullable = false, name = "MATE_ID")
	private Mate mate;

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
		update();
	}

	public Mate getMate() {
		return mate;
	}

	public void setMate(Mate mate) {
		this.mate = mate;
	}

	public StockType getType() {
		return type;
	}

	public void setType(StockType type) {
		this.type = type;
	}

}
