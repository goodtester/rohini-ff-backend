/**
 * 
 */
package com.rohini.fastfoodapp.service.interfaces;

import java.util.Collection;

import com.rohini.fastfoodapp.model.Subscriber;

/**
 * @author Rohini
 */
public interface ISubscriberService {
	
	Subscriber create(Subscriber subscriber);
	
	Collection<Subscriber> list(Long page);

	Boolean delete(Long idSubscriber);

	Boolean exist(Long idSubscriber);

	Boolean existByEmail(String email);
	
	Collection<Subscriber> searchByEmail(String email);


}
