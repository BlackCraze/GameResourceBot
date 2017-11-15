package de.blackcraze.grb.dao;

import java.util.List;
import java.util.Optional;

import de.blackcraze.grb.model.entity.StockTypeGroup;

public interface IStockTypeGroupDao extends IBaseDao<StockTypeGroup> {

    List<StockTypeGroup> findAll();

    List<StockTypeGroup> findByNameLike(String name);

    List<StockTypeGroup> findByNameLike(List<String> name);

    Optional<StockTypeGroup> findByName(String string);

}
