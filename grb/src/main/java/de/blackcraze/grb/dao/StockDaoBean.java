package de.blackcraze.grb.dao;

import java.util.List;

import de.blackcraze.grb.model.entity.Mate;
import de.blackcraze.grb.model.entity.Stock;
import de.blackcraze.grb.model.entity.StockType;

public class StockDaoBean extends BaseDaoBean<Stock> implements IStockDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Stock> findStocksByMate(Mate mate) {
		StringBuilder b = new StringBuilder();
		b.append("select s from Stock s where s.mate = :mate order by s.type.name");
		return em.createQuery(b.toString()).setParameter("mate", mate).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Stock> findStocksByType(StockType type) {
		return em.createQuery("select s from Stock s where s.type = :type order by s.updated desc")
				.setParameter("type", type).getResultList();
	}

}
