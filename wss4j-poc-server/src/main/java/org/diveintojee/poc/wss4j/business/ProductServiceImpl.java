/**
 * 
 */
package org.diveintojee.poc.wss4j.business;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.jws.WebService;

import org.apache.commons.lang.StringUtils;
import org.diveintojee.poc.wss4j.domain.Product;
import org.diveintojee.poc.wss4j.domain.Role;
import org.diveintojee.poc.wss4j.domain.services.ProductService;
import org.diveintojee.poc.wss4j.persistence.PersistenceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author louis.gueye@gmail.com
 */
@Component(ProductService.BEAN_ID)
@WebService(endpointInterface = ProductService.WEBSERVICE_ENDPOINT_INTERFACE)
public class ProductServiceImpl implements ProductService {

   @Autowired
   private PersistenceManager persistenceManager;

   /**
    * @see org.diveintojee.poc.wss4j.domain.services.ProductService#list()
    */
   @Override
   public List<Product> list() {

      return persistenceManager.findAll(Product.class);

   }

   /**
    * @see org.diveintojee.poc.wss4j.domain.services.ProductService#findByDescription(java.lang.String)
    */
   @Override
   public List<Product> findByDescription(String term) {

      List<Product> results = new ArrayList<Product>();

      if (StringUtils.isEmpty(term))
         return results;

      List<Product> products = list();

      for (Product product : products) {

         if (product.getDescription().contains(term))
            results.add(product);

      }

      return results;

   }

   /**
    * @see org.diveintojee.poc.wss4j.domain.services.ProductService#add(org.diveintojee.poc.wss4j.domain.Product)
    */
   @Override
   @RolesAllowed( {Role.ADMIN_ROLE_ID} )
   public Long add(Product product) {

      if (product == null)
         return null;

      return persistenceManager.persist(product);

   }

   /**
    * @see org.diveintojee.poc.wss4j.domain.services.ProductService#update(org.diveintojee.poc.wss4j.domain.Product)
    */
   @Override
   @RolesAllowed( {Role.ADMIN_ROLE_ID} )
   public void update(Product product) {

      if (product == null)
         return;

      if (product.getId() == null)
         throw new IllegalArgumentException();

      persistenceManager.persist(product);

   }

   /**
    * @see org.diveintojee.poc.wss4j.domain.services.ProductService#get(java.lang.Long)
    */
   @Override
   public Product get(Long id) {

      return persistenceManager.get(Product.class, id);

   }

   /**
    * @see org.diveintojee.poc.wss4j.domain.services.ProductService#delete(java.lang.Long)
    */
   @Override
   @RolesAllowed( {Role.ADMIN_ROLE_ID} )
   public void delete(Long id) {

      persistenceManager.delete(Product.class, id);

   }

   /**
    * @see org.diveintojee.poc.wss4j.domain.services.ProductService#clear()
    */
   @Override
   @RolesAllowed( {Role.ADMIN_ROLE_ID} )
   public void clear() {

      persistenceManager.clear(Product.class);

   }

}
