package de.blackcraze.grb.util;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import de.blackcraze.grb.dao.IMateDao;
import de.blackcraze.grb.dao.IStockDao;
import de.blackcraze.grb.dao.IStockTypeDao;
import de.blackcraze.grb.dao.MateDaoBean;
import de.blackcraze.grb.dao.StockDaoBean;
import de.blackcraze.grb.dao.StockTypeDaoBean;

public class DbUtil extends AbstractModule {

	private static final ThreadLocal<EntityManager> ENTITY_MANAGER_CACHE = new ThreadLocal<EntityManager>();

	@Override
	public void configure() {
		bind(IMateDao.class).to(MateDaoBean.class);
		bind(IStockDao.class).to(StockDaoBean.class);
		bind(IStockTypeDao.class).to(StockTypeDaoBean.class);
	}

	@Provides
	@Singleton
	public EntityManagerFactory createEntityManagerFactory() {
		return Persistence.createEntityManagerFactory("bc-gbr");
	}

	@Provides
	public EntityManager createEntityManager(EntityManagerFactory entityManagerFactory) {
		EntityManager entityManager = ENTITY_MANAGER_CACHE.get();
		if (entityManager == null) {
			ENTITY_MANAGER_CACHE.set(entityManager = entityManagerFactory.createEntityManager());
		}
		return entityManager;
	}
}