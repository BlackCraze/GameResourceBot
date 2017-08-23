package de.blackcraze.grb.util;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

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
		return Persistence.createEntityManagerFactory("bc-gbr", readHerokuDatabaseConnection());
	}

	private Map<String, String> readHerokuDatabaseConnection() {
//		databaseUrl = "postgres://user:pass@localhost:5432/gbr";
		String databaseUrl = System.getenv("DATABASE_URL");

		StringTokenizer st = new StringTokenizer(databaseUrl, ":@/");
		String dbVendor = st.nextToken(); // if DATABASE_URL is set
		String userName = st.nextToken();
		String password = st.nextToken();
		String host = st.nextToken();
		String port = st.nextToken();
		String databaseName = st.nextToken();
		String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s?sslmode=require", host, port, databaseName);
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("javax.persistence.jdbc.url", jdbcUrl);
		properties.put("javax.persistence.jdbc.user", userName);
		properties.put("javax.persistence.jdbc.password", password);
		properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
		properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		return properties;
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