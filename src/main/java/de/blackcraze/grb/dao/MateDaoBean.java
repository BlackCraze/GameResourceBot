package de.blackcraze.grb.dao;

import de.blackcraze.grb.model.entity.Mate;
import de.blackcraze.grb.model.entity.Stock;
import de.blackcraze.grb.model.entity.StockType;
import de.blackcraze.grb.util.PrintUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.lang3.StringUtils;

public class MateDaoBean extends BaseDaoBean<Mate> implements IMateDao {

    @Inject
    private IStockTypeDao stockTypeDao;

    @Inject
    private IStockDao stockDao;

    @Override
    public Optional<Mate> findByDiscord(String discordID) {
        try {
            return Optional.of((Mate) em.createQuery(
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

        newStocks.forEach((stockKey, stockAmount) -> {
            if (Long.MIN_VALUE == stockAmount) {
                // in this case it is the misspelled name see
                // de.blackcraze.grb.util.CommandUtils.parseStocks(Scanner,
                // Locale)
                unknown.add(stockKey);
            } else if (0 == stockAmount.longValue()) {
                System.out.printf("clearing stock %s for player %s%n", stockKey, mate.getName());
                Optional<StockType> type = stockTypeDao.findByKey(stockKey);
                if (type.isPresent()) {
                    stockDao.delete(mate, type.get());
                }
            } else {
                boolean found = false;
                for (Stock stock : stocks) {
                    if (isStockMent(stock, stockKey)) {
                        found = true;
                        stock.setAmount(stockAmount);
                        stockDao.update(stock);
                        break;
                    }
                }
                if (!found) {
                    System.out.printf("Creating new stock %s for player %s%n", stockKey,
                            mate.getName());
                    Optional<StockType> type = stockTypeDao.findByKey(stockKey);
                    if (type.isPresent()) {
                        Stock stock = new Stock();
                        stock.setType(type.get());
                        stock.setAmount(stockAmount);
                        stock.setMate(mate);
                        stockDao.save(stock);
                    } else {
                        unknown.add(stockKey);
                        System.err.println("Unknown stock type: " + stockKey);
                    }
                }
            }
        });
        return unknown;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mate> findAll() {
        return em.createQuery("from Mate order by name").getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mate> findByNameLike(String name) {
        return em.createQuery("from Mate where lower(name) like :name order by name")
                .setParameter("name", "%" + name.toLowerCase() + "%").getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mate> findByName(String name) {
        return em.createQuery("from Mate where lower(name) = :name order by name")
                .setParameter("name", name.toLowerCase()).getResultList();
    }

    @Override
    public Mate getOrCreateMate(Message message, Locale locale) {
        String discordId = message.getAuthor().getId();
        Optional<Mate> mateOptional = findByDiscord(discordId);
        String name = message.getAuthor().getName();
        Member member = message.getMember();
        // will not work on private messages
        if (member != null && member.getNickname() != null) {
            name = member.getNickname();
        }
        if (!mateOptional.isPresent()) {
            if (message.getChannelType().equals(ChannelType.PRIVATE)) {
                throw new IllegalStateException(
                        "unknown user can not automatically be created in private messages");
            }
            return createMate(locale, discordId, name);
        } else {
            Mate mate = mateOptional.get();
            // do not update giuld name by private messages
            if (!message.getChannelType().equals(ChannelType.PRIVATE)) {
                if (!mate.getName().equals(name)) {
                    mate.setName(name);
                    update(mate);
                }
            }
            return mate;
        }
    }

    @Override
    public Mate createMate(Locale locale, String discordId, String name) {
        Mate mate = new Mate();
        mate.setDiscordId(discordId);
        mate.setName(name);
        mate.setLanguage(locale.getLanguage());
        save(mate);
        return mate;
    }

    @Override
    public List<List<String>> listOrderByOldestStock(Locale locale) {
        Date now = new Date();
        List<List<String>> result = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append(" select m.name, count(s), min(s.updated) from Mate m");
        query.append(" left join m.stocks s");
        query.append(" group by m.id, m.name");
        query.append(" order by m.name");
        @SuppressWarnings("unchecked")
        List<Object[]> resultList = em.createQuery(query.toString()).getResultList();
        String lastMate = null;
        for (Object[] row : resultList) {
            String mate = (String) row[0];
            Number amount = (Number) row[1];
            Date updated = (Date) row[2];
            if (!StringUtils.equals(lastMate, mate)) {
                String updateS = updated != null ? PrintUtils.getDiffFormatted(updated, now) : "-";
                String amountS = String.format(locale, "%d", amount.intValue());
                result.add(Arrays.asList(mate, amountS, updateS));
                lastMate = mate;
            }
        }
        return result;
    }

    // TODO more power
    private boolean isStockMent(Stock stock, String name) {
        return stock.getType().getName().equalsIgnoreCase(name);
    }

    @Override
    public void delete(String discordId) {
        em.getTransaction().begin();
        em.createQuery("delete Mate where discordId = :id").setParameter("id", discordId)
                .executeUpdate();
        em.getTransaction().commit();
    }

}
