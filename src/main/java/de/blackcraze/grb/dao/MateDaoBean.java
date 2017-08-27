package de.blackcraze.grb.dao;

import de.blackcraze.grb.model.entity.Mate;
import de.blackcraze.grb.model.entity.Stock;
import de.blackcraze.grb.model.entity.StockType;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import java.util.*;
import java.util.Map.Entry;

public class MateDaoBean extends BaseDaoBean<Mate> implements IMateDao {

	@Inject
	private IStockTypeDao stockTypeDao;

	@Inject
	private IStockDao stockDao;

	@Override
	public Optional<Mate> findByDiscord(String discordID) {
		try {
			return Optional.of((Mate) em
					.createQuery(
							"select m from Mate m left join fetch m.stocks left join fetch m.buildings where m.discordId = :discordId")
					.setParameter("discordId", discordID).getSingleResult());
		} catch (NoResultException e) {
			return Optional.empty();
		} catch (NonUniqueResultException e) {
			e.printStackTrace();
			return Optional.empty();
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
				Optional<StockType> type = stockTypeDao.findByName(typeName);
				if (type.isPresent()) {
					Stock stock = new Stock();
					stock.setType(type.get());
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
				.setParameter("name", "%" + name.toLowerCase() + "%")
				.getResultList();
	}

	// TODO more power
	private boolean isStockMent(Stock stock, String name) {
		return stock.getType().getName().equalsIgnoreCase(name);
	}

}
