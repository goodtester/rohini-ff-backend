package com.rohini.fastfoodapp.service;

import java.util.Collection;

import javax.transaction.Transactional;

import com.rohini.fastfoodapp.repository.IPayModeRepository;
import com.rohini.fastfoodapp.service.interfaces.IPayModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rohini.fastfoodapp.model.PayMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rohini
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PayModeServiceImp implements IPayModeService {

	@Autowired
	private final IPayModeRepository payModeRepository;

	@Override
	public PayMode create(PayMode payMode) {
		log.info("Saving new category: " + payMode.getName());
		return payModeRepository.save(payMode);
	}

	@Override
	public PayMode update(Long id, PayMode payMode) {
		log.info("Updating category: " + payMode.getName());
		return payModeRepository.save(payMode);
	}

	@Override
	public Boolean delete(Long idPayMode) {
		log.info("Deleting the category id: " + idPayMode);
		if (payModeRepository.existsById(idPayMode)) {
			payModeRepository.deleteById(idPayMode);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Collection<PayMode> list() {
		log.info("List all categories");
		return payModeRepository.findAll();
	}

	@Override
	public Boolean exist(Long idPayMode) {
		log.info("Searching category by id: " + idPayMode);
		return payModeRepository.existsById(idPayMode);
	}

}
