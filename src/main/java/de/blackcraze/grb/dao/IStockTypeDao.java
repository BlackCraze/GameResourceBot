package de.blackcraze.grb.dao;

import java.util.List;

import de.blackcraze.grb.model.entity.StockType;

public interface IStockTypeDao extends IBaseDao<StockType> {

	List<StockType> findAll();

	StockType findByName(String name);

	List<StockType> findByNameLike(String name);

}
