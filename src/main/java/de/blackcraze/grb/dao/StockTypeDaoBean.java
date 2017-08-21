package de.blackcraze.grb.dao;

import java.util.Collections;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.commons.lang3.StringUtils;

import de.blackcraze.grb.model.entity.StockType;

public class StockTypeDaoBean extends BaseDaoBean<StockType> implements IStockTypeDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<StockType> findAll() {
		return em.createQuery("from StockType order by name").getResultList();
	}

	@Override
	public StockType findByName(String name) {
		try {
			return (StockType) em.createQuery("from StockType where lower(name)=:name order by name, id desc")
					.setParameter("name", StringUtils.lowerCase(name)).setMaxResults(1).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StockType> findByNameLike(String name) {
		if (StringUtils.isEmpty(name)) {
			return Collections.emptyList();
		} else {
			return em.createQuery("from StockType where lower(name) like :name order by name")
					.setParameter("name", "%" + StringUtils.lowerCase(name) + "%").getResultList();
		}
	}

}
