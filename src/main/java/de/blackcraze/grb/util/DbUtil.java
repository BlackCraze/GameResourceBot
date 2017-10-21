package de.blackcraze.grb.util;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import de.blackcraze.grb.core.BotConfig;
import de.blackcraze.grb.dao.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

public class DbUtil extends AbstractModule {

	private static final ThreadLocal<EntityManager> ENTITY_MANAGER_CACHE = new ThreadLocal<>();

	@Override
	public void configure() {
		bind(IMateDao.class).to(MateDaoBean.class);
		bind(IStockDao.class).to(StockDaoBean.class);
		bind(IStockTypeDao.class).to(StockTypeDaoBean.class);
	}

	@Provides
	@Singleton
	public EntityManagerFactory createEntityManagerFactory() {
		Map<String, String> properties = readHerokuDatabaseConnection();
		return Persistence.createEntityManagerFactory("bc-grb", properties);
	}

	private Map<String, String> readHerokuDatabaseConnection() {
//		databaseUrl = "postgres://user:pass@localhost:5432/grb";
		String databaseUrl = BotConfig.DATABASE_URL;
		boolean sslEnabled = !BotConfig.USE_SSL.isEmpty();
		StringTokenizer st = new StringTokenizer(databaseUrl, ":@/");
		@SuppressWarnings("unused")
		String dbVendor = st.nextToken(); // if DATABASE_URL is set
		String userName = st.nextToken();
		String password = st.nextToken();
		String host = st.nextToken();
		String port = st.nextToken();
		String databaseName = st.nextToken();
		String ssl = sslEnabled ? "sslmode=require" : "";
		String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s?%s", host, port, databaseName, ssl);
		Map<String, String> properties = new HashMap<>();
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
		if (Objects.isNull(entityManager)) {
			ENTITY_MANAGER_CACHE.set(entityManager = entityManagerFactory.createEntityManager());
		}
		return entityManager;
	}
}