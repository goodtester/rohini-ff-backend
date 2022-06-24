/**
 * 
 */
package com.rohini.fastfoodapp.service;

import java.util.Collection;

import javax.transaction.Transactional;

import com.rohini.fastfoodapp.repository.ISubscriberRepository;
import com.rohini.fastfoodapp.service.interfaces.ISubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rohini.fastfoodapp.model.Subscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rohini
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SubscriberServiceImp implements ISubscriberService {

	@Autowired
	private ISubscriberRepository subscriberRepository;

	public Subscriber create(Subscriber subscriber) {
		log.info("Saving new subscriber: " + subscriber.getEmail());
		return subscriberRepository.save(subscriber);
	}

	@Override
	public Collection<Subscriber> list(Long page) {
		log.info("List subscriber");
		return subscriberRepository.list(page);
	}

	@Override
	public Boolean delete(Long idSubscriber) {
		log.info("Delete subscriber with id: " + idSubscriber);
		subscriberRepository.deleteById(idSubscriber);
		return true;
	}

	@Override
	public Boolean exist(Long idSubscriber) {
		log.info("Searching subscriber with id: " + idSubscriber);
		return subscriberRepository.existsById(idSubscriber);
	}

	@Override
	public Boolean existByEmail(String email) {
		log.info("Searching subscriber with email: " + email);
		return subscriberRepository.findByEmail(email) != null;
	}
	
	@Override
	public Collection<Subscriber> searchByEmail(String email) {
		log.info("Searching subscriber with email: " + email);
		return subscriberRepository.findByEmailStartsWith(email);
	}

}
