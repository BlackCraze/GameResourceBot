package de.blackcraze.grb.dao;

import de.blackcraze.grb.model.entity.StockType;

import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StockTypeDaoBean extends BaseDaoBean<StockType> implements IStockTypeDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<StockType> findAll() {
		return em.createQuery("from StockType order by name")
				.getResultList();
	}

	@Override
	public Optional<StockType> findByName(String name) {
		try {
			return Optional.of((StockType) em.createQuery("from StockType where lower(name)=:name order by name, id desc")
					.setParameter("name", name.toLowerCase())
					.setMaxResults(1)
					.getSingleResult());
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StockType> findByNameLike(String name) {
		if (name.isEmpty()) {
			return Collections.emptyList();
		} else {
			return em.createQuery("from StockType where lower(name) like :name order by name")
					.setParameter("name", "%" + name.toLowerCase() + "%")
					.getResultList();
		}
	}

}
