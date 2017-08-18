package de.blackcraze.grb.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import de.blackcraze.grb.model.BaseEntity;

public abstract class BaseDaoBean<E extends BaseEntity> implements IBaseDao<E> {

	@Inject
	protected EntityManager em;

	@Override
	public void save(E entity) {
		em.getTransaction().begin();
		em.persist(entity);
		em.getTransaction().commit();
	}

	@Override
	public void update(E entity) {
		em.getTransaction().begin();
		em.merge(entity);
		em.getTransaction().commit();
	}

	@Override
	public void delete(E entity) {
		em.getTransaction().begin();
		em.detach(entity);
		em.getTransaction().commit();
	}

}
