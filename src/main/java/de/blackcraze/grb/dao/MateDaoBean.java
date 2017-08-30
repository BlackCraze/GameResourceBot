package de.blackcraze.grb.dao;

import de.blackcraze.grb.model.entity.Mate;
import de.blackcraze.grb.model.entity.Stock;
import de.blackcraze.grb.model.entity.StockType;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

		newStocks.forEach((stockName, stockAmount) -> {
			boolean found = false;
			for (Stock stock : stocks) {
				if (isStockMent(stock, stockName)) {
					found = true;
					stock.setAmount(stockAmount);
					stockDao.update(stock);
					break;
				}
			}
			if (!found) {
				System.out.printf("Creating new stock %s for player %s%n", stockName, mate.getName());
				Optional<StockType> type = stockTypeDao.findByName(stockName);
				if (type.isPresent()) {
					Stock stock = new Stock();
					stock.setType(type.get());
					stock.setAmount(stockAmount);
					stock.setMate(mate);
					stockDao.save(stock);
				} else {
					unknown.add(stockName);
					System.err.println("Unknown stock type: " + stockName);
				}
			}
		});
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
