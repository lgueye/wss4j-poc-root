/**
 * 
 */
package org.diveintojee.poc.wss4j.domain;


/**
 * @author louis.gueye@gmail.com
 */
public class AbstractPersistableEntity implements PersistableEntity {

   protected Long id;

   /**
    * @see org.diveintojee.poc.wss4j.domain.PersistableEntity#getId()
    */
   @Override
   public Long getId() {
      return this.id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   /**
    * @see java.lang.Object#hashCode()
    */
   @Override
   public final int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      return result;
   }

   /**
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public final boolean equals(Object obj) {

      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (!(obj instanceof AbstractPersistableEntity)) {
         return false;
      }
      AbstractPersistableEntity other = (AbstractPersistableEntity) obj;
      if (id == null) {
         if (other.id != null) {
            return false;
         }
      } else if (!id.equals(other.id)) {
         return false;
      }
      return true;
   }

}
