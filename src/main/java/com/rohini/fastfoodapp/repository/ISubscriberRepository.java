/**
 * 
 */
package com.rohini.fastfoodapp.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rohini.fastfoodapp.model.Subscriber;

/**
 * @author Rohini
 */
@Repository
public interface ISubscriberRepository extends JpaRepository<Subscriber, Long>  {
	
	@Query(value = "SELECT * FROM subscriber order by email OFFSET ? ROWS FETCH NEXT 10 ROWS ONLY", nativeQuery = true)
	Collection<Subscriber> list(Long page);
	
	Collection<Subscriber> findByEmailStartsWith(String email);
	
	Subscriber findByEmail(String email);

}
