package de.blackcraze.grb.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.NoResultException;

import de.blackcraze.grb.model.AbstractNamed;
import de.blackcraze.grb.model.entity.StockTypeGroup;

public class StockTypeGroupDaoBean extends BaseDaoBean<StockTypeGroup> implements IStockTypeGroupDao {

    @SuppressWarnings("unchecked")
    @Override
    public List<StockTypeGroup> findAll() {
        return em.createQuery("from StockTypeGroup order by name").getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<StockTypeGroup> findByNameLike(String name) {
        return em.createQuery("from StockTypeGroup where lower(name) like :name order by name")
                .setParameter("name", "%" + name.toLowerCase() + "%").getResultList();
    }

    @Override
    public List<StockTypeGroup> findByNameLike(List<String> names) {
        Set<StockTypeGroup> result = new HashSet<>();// avoid double values
        for (String name : names) {
            result.addAll(findByNameLike(name));
        }
        ArrayList<StockTypeGroup> sortedResult = new ArrayList<>(result);
        Collections.sort(sortedResult, new Comparator<AbstractNamed>() {

            @Override
            public int compare(AbstractNamed o1, AbstractNamed o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return sortedResult;
    }

    @Override
    public Optional<StockTypeGroup> findByName(String name) {
        try {
            return Optional
                    .of((StockTypeGroup) em.createQuery("from StockTypeGroup where lower(name)=:name order by name")
                            .setParameter("name", name.toLowerCase()).setMaxResults(1).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
