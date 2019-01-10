package de.blackcraze.grb.dao;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import de.blackcraze.grb.model.entity.StockType;

public interface IStockTypeDao extends IBaseDao<StockType> {

    List<StockType> findAll(Locale locale);

    Optional<StockType> findByKey(String key);

    List<StockType> findByNameLike(String name, Locale locale);

    void trimNames();

}
