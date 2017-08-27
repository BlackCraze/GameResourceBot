package de.blackcraze.grb.dao;

import de.blackcraze.grb.model.entity.StockType;

import java.util.List;
import java.util.Optional;

public interface IStockTypeDao extends IBaseDao<StockType> {

	List<StockType> findAll();

	Optional<StockType> findByName(String name);

	List<StockType> findByNameLike(String name);

}
