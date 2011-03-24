/**
 * 
 */
package org.diveintojee.poc.wss4j.persistence;

import java.util.List;

/**
 * @author louis.gueye@gmail.com
 *
 */
public interface PersistenceManager {
	
	String BEAN_ID = "persistenceManager";
	
	/**
	 * @param <T>
	 * @param entityClass
	 * @param id
	 * @return
	 */
	<T> T get(Class<T> entityClass, Long id);

	/**
	 * @param entity
	 */
	Long persist(Object entity);

	/**
	 * @param entity
	 */
	<T> void delete(Class<T> entityClass, Long id);

	/**
	 * @param <T>
	 * @param entityClass
	 * @return
	 */
	<T> List<T> findAll(Class<T> entityClass);

	/**
	 * @param entityClass
	 */
	<T> void clear(Class<T> entityClass);
}
