package de.blackcraze.grb.dao;

import de.blackcraze.grb.model.BaseEntity;

public interface IBaseDao<E extends BaseEntity> {

	void save(E entity);

	void update(E entity);

	void delete(E entity);

}
