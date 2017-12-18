package de.blackcraze.grb.dao;

import java.util.List;

import de.blackcraze.grb.model.entity.Mate;
import de.blackcraze.grb.model.entity.Stock;
import de.blackcraze.grb.model.entity.StockType;

public class StockDaoBean extends BaseDaoBean<Stock> implements IStockDao {

    @SuppressWarnings("unchecked")
    @Override
    public List<Stock> findStocksByMate(Mate mate) {
        return em.createQuery("select s from Stock s where s.mate = :mate order by s.type.name")
                .setParameter("mate", mate).getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Stock> findStocksByType(StockType type) {
        return em.createQuery("select s from Stock s where s.type = :type order by s.updated desc")
                .setParameter("type", type).getResultList();
    }

    @Override
    public long getTotalAmount(StockType type) {
        Number number = (Number) em.createQuery("select sum(s.amount) from Stock s where s.type = :type")
                .setParameter("type", type).setMaxResults(1).getSingleResult();
        return number != null ? number.longValue() : 0;
    }

    @Override
    public int delete(Mate mate, StockType type) {
        em.getTransaction().begin();
        int rowCount = em.createQuery("delete Stock where type = :type and mate = :mate").setParameter("type", type)
                .setParameter("mate", mate).executeUpdate();
        em.getTransaction().commit();
        return rowCount;
    }

    @Override
    public int deleteAll(Mate mate) {
        em.getTransaction().begin();
        int rowCount = em.createQuery("delete Stock s where mate = :mate").setParameter("mate", mate).executeUpdate();
        em.getTransaction().commit();
        return rowCount;
    }

}
