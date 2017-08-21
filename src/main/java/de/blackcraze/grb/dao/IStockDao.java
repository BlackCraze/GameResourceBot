package de.blackcraze.grb.dao;

import java.util.List;

import de.blackcraze.grb.model.entity.Mate;
import de.blackcraze.grb.model.entity.Stock;
import de.blackcraze.grb.model.entity.StockType;

public interface IStockDao extends IBaseDao<Stock> {

	List<Stock> findStocksByMate(Mate mate);

	List<Stock> findStocksByType(StockType type);

}
