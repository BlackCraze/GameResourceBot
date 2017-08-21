package de.blackcraze.grb.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import de.blackcraze.grb.model.AbstractNamed;

@Entity
@Table(name = "STOCK_TYPE")
public class StockType extends AbstractNamed {

	private static final long serialVersionUID = 3639150984500441408L;

	@Column(nullable = false)
	private long price;

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

}
