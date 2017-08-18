/**
 *
 */
package de.blackcraze.grb.model;

import java.io.Serializable;

public interface Identifiable extends Serializable {

	String ID = "id";

	/**
	 *
	 */
	Serializable getId();
}
