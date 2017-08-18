package de.blackcraze.grb.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.apache.commons.lang3.StringUtils;

import de.blackcraze.grb.model.entity.Mate;
import de.blackcraze.grb.model.entity.Stock;
import de.blackcraze.grb.model.entity.StockType;

public class MateDaoBean extends BaseDaoBean<Mate> implements IMateDao {

	@Inject
	private IStockTypeDao stockTypeDao;

	@Inject
	private IStockDao stockDao;

	@Override
	public Mate findByDiscord(String discordId) {
		try {
			return (Mate) em
					.createQuery(
							"select m from Mate m left join fetch m.stocks left join fetch m.buildings where m.discordId = :discordId")
					.setParameter("discordId", discordId).getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (NonUniqueResultException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<String> updateStocks(Mate mate, Map<String, Long> newStocks) {
		List<String> unknown = new ArrayList<>();
		List<Stock> stocks = stockDao.findStocksByMate(mate);
		Set<Entry<String, Long>> toInsert = new HashSet<>();

		for (Entry<String, Long> entry : newStocks.entrySet()) {
			boolean found = false;
			for (Stock stock : stocks) {
				String name = entry.getKey();
				if (isStockMent(stock, name)) {
					found = true;
					stock.setAmount(entry.getValue());
					stockDao.update(stock);
					break;
				}
			}
			if (!found) {
				toInsert.add(entry);
			}
		}

		if (!toInsert.isEmpty()) {
			for (Entry<String, Long> entry : toInsert) {
				String typeName = entry.getKey();
				StockType type = stockTypeDao.findByName(typeName);
				if (type != null) {
					Stock stock = new Stock();
					stock.setType(type);
					stock.setAmount(entry.getValue());
					stock.setMate(mate);
					stockDao.save(stock);
				} else {
					unknown.add(typeName);
					System.err.println("I do not know this stock type: " + typeName);
				}
			}
		}
		return unknown;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Mate> findByNameLike(String name) {
		return em.createQuery("from Mate where lower(name) like :name order by name")
				.setParameter("name", "%" + StringUtils.lowerCase(name) + "%").getResultList();
	}

	// TODO more power
	private boolean isStockMent(Stock stock, String name) {
		return stock.getType().getName().equalsIgnoreCase(name);
	}

}
