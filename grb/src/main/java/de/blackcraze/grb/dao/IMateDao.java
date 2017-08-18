package de.blackcraze.grb.dao;

import java.util.List;
import java.util.Map;

import de.blackcraze.grb.model.entity.Mate;

public interface IMateDao {

	void save(Mate mate);

	Mate findByDiscord(String discrodId);

	List<String> updateStocks(Mate mate, Map<String, Long> newStocks);

	List<Mate> findByNameLike(String name);

}
