/**
 * 
 */
package org.diveintojee.poc.wss4j.persistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.diveintojee.poc.wss4j.domain.PersistableEntity;
import org.springframework.stereotype.Component;

/**
 * @author louis.gueye@gmail.com
 */
@Component(PersistenceManager.BEAN_ID)
public class PersistenceManagerImpl implements PersistenceManager {

   private Map<Class<?>, Map<Long, Object>> repository = new HashMap<Class<?>, Map<Long, Object>>();

   /**
    * @see org.diveintojee.poc.wss4j.persistence.PersistenceManager#get(java.lang.Class, java.lang.Object)
    */
   @SuppressWarnings("unchecked")
   @Override
   public <T> T get(Class<T> entityClass, Long id) {

      if (entityClass == null)
         return null;

      if (id == null)
         return null;

      Map<Long, Object> entities = getRepository().get(entityClass);

      if (MapUtils.isEmpty(entities))
         return null;

      return (T) entities.get(id);

   }

   /**
    * @see org.diveintojee.poc.wss4j.persistence.PersistenceManager#persist(java.lang.Object)
    */
   @Override
   public Long persist(Object entity) {

      if (entity == null)
         return null;

      Map<Long, Object> entities = getRepository().get(entity.getClass());

      if (entities == null)
         getRepository().put(entity.getClass(), new HashMap<Long, Object>());

      entities = getRepository().get(entity.getClass());

      Set<Long> idsAsSet = entities.keySet();

      List<Long> idsAsList = new ArrayList<Long>(idsAsSet);

      Collections.sort(idsAsList);

      Long entityId = ((PersistableEntity) entity).getId();

      if (entityId == null) {

         Long lastId = (idsAsList.size() == 0) ? null : idsAsList.get(idsAsList.size() - 1);

         if (lastId == null)
            ((PersistableEntity) entity).setId(0L);
         else
            ((PersistableEntity) entity).setId(Long.valueOf(lastId.longValue() + 1));

      }

      getRepository().get(entity.getClass()).put(((PersistableEntity) entity).getId(), entity);

      return ((PersistableEntity) entity).getId();

   }

   /**
    * @see org.diveintojee.poc.wss4j.persistence.PersistenceManager#delete(java.lang.Class, java.lang.Object)
    */
   @Override
   public <T> void delete(Class<T> entityClass, Long id) {

      if (entityClass == null)
         return;

      if (id == null)
         return;

      Map<Long, Object> entities = getRepository().get(entityClass);

      if (MapUtils.isEmpty(entities))
         return;

      entities.remove(id);

   }

   /**
    * @see org.diveintojee.poc.wss4j.persistence.PersistenceManager#findAll(java.lang.Class)
    */
   @SuppressWarnings({"unchecked", "rawtypes"})
   @Override
   public <T> List<T> findAll(Class<T> entityClass) {

      if (entityClass == null)
         return null;

      Map<Long, Object> entities = getRepository().get(entityClass);

      if (MapUtils.isEmpty(entities))
         return null;

      return new ArrayList(entities.values());

   }

   /**
    * @return
    */
   private Map<Class<?>, Map<Long, Object>> getRepository() {
      return this.repository;
   }

   /**
    * @see org.diveintojee.poc.wss4j.persistence.PersistenceManager#clear(java.lang.Class)
    */
   @Override
   public <T> void clear(Class<T> entityClass) {

      if (entityClass == null)
         return;

      getRepository().remove(entityClass);

   }

}
