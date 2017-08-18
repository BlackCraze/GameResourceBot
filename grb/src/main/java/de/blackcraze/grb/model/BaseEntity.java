package de.blackcraze.grb.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity implements Identifiable {

	private static final long serialVersionUID = 5472935172178883286L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable = false, unique = true)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		return idEquals(obj);
	}

	@Override
	public int hashCode() {
		return idHashCode();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " #" + getId();
	}

	private boolean idEquals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj != null && this.getClass().isAssignableFrom(obj.getClass())) {
			final Identifiable that = (Identifiable) obj;
			return this.getId() == null ? (that.getId() == null && super.equals(obj))
					: this.getId().equals(that.getId());
		}
		return false;
	}

	private int idHashCode() {
		return getId() == null ? 0 : getId().hashCode();
	}

}
