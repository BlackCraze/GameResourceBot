package de.blackcraze.grb.dao;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import de.blackcraze.grb.model.entity.Mate;
import net.dv8tion.jda.core.entities.Message;

public interface IMateDao extends IBaseDao<Mate> {

    void save(Mate mate);

    Optional<Mate> findByDiscord(String discordID);

    List<String> updateStocks(Mate mate, Map<String, Long> newStocks);

    List<Mate> findAll();

    List<Mate> findByNameLike(String name);

    List<Mate> findByName(String name);

    Mate getOrCreateMate(Message message, Locale defaultLocale);

    Mate createMate(Locale locale, String discordId, String name);

    List<List<String>> listOrderByOldestStock(Locale locale);

    void delete(String discordId);

}
