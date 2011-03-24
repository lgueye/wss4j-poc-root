/**
 * 
 */
package org.diveintojee.poc.wss4j.domain.services;

import java.util.List;

import javax.jws.WebService;

import org.diveintojee.poc.wss4j.domain.Product;

/**
 * @author louis.gueye@gmail.com
 */
@WebService
public interface ProductService {

   String BEAN_ID = "productService";

   String WEBSERVICE_ENDPOINT_INTERFACE = "org.diveintojee.poc.wss4j.domain.services.ProductService";

   /**
    * @return
    */
   List<Product> list();

   /**
    * @param term
    * @return
    */
   List<Product> findByDescription(String term);

   /**
    * @param product
    * @return
    */
   Long add(Product product);

   /**
    * @param product
    */
   void update(Product product);

   /**
    * @param id
    */
   void delete(Long id);

   /**
    * @param product
    * @return
    */
   Product get(Long id);

   /**
	 * 
	 */
   void clear();

}
