package de.blackcraze.grb.dao;

import static de.blackcraze.grb.util.InjectorUtils.getStockTypeDao;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;

import javax.persistence.NoResultException;

import de.blackcraze.grb.i18n.Resource;
import de.blackcraze.grb.model.entity.StockType;

public class StockTypeDaoBean extends BaseDaoBean<StockType> implements IStockTypeDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<StockType> findAll() {
		return em.createQuery("from StockType order by name").getResultList();
	}

	@Override
	public Optional<StockType> findByKey(String key) {
		try {
			return Optional
					.of((StockType) em.createQuery("from StockType where lower(name)=:name order by name, id desc")
							.setParameter("name", key.toLowerCase()).setMaxResults(1).getSingleResult());
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	@Override
	public List<StockType> findByNameLike(String name, Locale locale) {
		List<StockType> stocks = getStockTypeDao().findAll();
		Predicate<StockType> filter = stock -> !Resource.getItem(stock.getName(), locale).toLowerCase()
				.contains(name.toLowerCase());
		stocks.removeIf(filter);
		return stocks;
	}

}
