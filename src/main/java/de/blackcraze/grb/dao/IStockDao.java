package de.blackcraze.grb.dao;

import java.time.LocalDateTime;
import java.util.List;

import de.blackcraze.grb.model.entity.Mate;
import de.blackcraze.grb.model.entity.Stock;
import de.blackcraze.grb.model.entity.StockType;

public interface IStockDao extends IBaseDao<Stock> {

    List<Stock> findStocksByMate(Mate mate);

    List<Stock> findStocksByType(StockType type);

    long getTotalAmount(StockType type);

    int delete(Mate mate, StockType type);

    int deleteAll(Mate mate);

    int deleteOlderThan(LocalDateTime limit);

}
