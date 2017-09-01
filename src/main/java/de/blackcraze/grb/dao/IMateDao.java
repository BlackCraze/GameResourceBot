package de.blackcraze.grb.dao;

import de.blackcraze.grb.model.entity.Mate;
import net.dv8tion.jda.core.entities.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IMateDao {

	void save(Mate mate);

	Optional<Mate> findByDiscord(String discordID);

	List<String> updateStocks(Mate mate, Map<String, Long> newStocks);

	List<Mate> findAll();

	List<Mate> findByNameLike(String name);

	Mate getOrCreateMate(User author);

}
