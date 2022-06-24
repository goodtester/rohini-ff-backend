/**
 * 
 */
package com.rohini.fastfoodapp.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import javax.transaction.Transactional;

import com.rohini.fastfoodapp.repository.IAdditionalRepository;
import com.rohini.fastfoodapp.service.interfaces.IAdditionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rohini.fastfoodapp.model.Additional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rohini
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdditionalServiceImp implements IAdditionalService {

	@Autowired
	private final IAdditionalRepository additionalRepository;
	@Autowired
	private final CloudinaryService cloudinaryService;

	@Override
	public Additional create(Additional additional, MultipartFile file) {
		log.info("Saving new additional: " + additional.getName());
		if (additional.getCategory().size()>0) {
			if (file != null) {
				try {
					additional.setImageUrl(cloudinaryService.upload(file, "additional"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return additionalRepository.save(additional);
		} else {
			return null;
		}
	}

	@Override
	public Additional update(Long id, Additional additional, MultipartFile file) {
		log.info("Updating additional: " + additional.getName());
		Optional<Additional> oldAdditional = additionalRepository.findById(id);
		additional.setIdAdditional(id);
		if (additional.getCategory().size()>0) {
			if (file != null) {
				try {
					additional.setImageUrl(cloudinaryService.upload(file, "additional"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(additional.getImageUrl()==null && file == null){
				additional.setImageUrl(null);
			}
			return additionalRepository.save(additional);
		} else {
			return null;
		}
	}

	@Override
	public Boolean delete(Long id_additional) {
		log.info("Deleting the additional id: " + id_additional);
		if (additionalRepository.existsById(id_additional)) {
			additionalRepository.deleteById(id_additional);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Collection<Additional> list() {
		log.info("List all additional");
		return additionalRepository.findAll();
	}

	@Override
	public Boolean exist(Long id_additional) {
		log.info("Searching additional by id: " + id_additional);
		return additionalRepository.existsById(id_additional);
	}

	public Collection<Additional> findByName(String name) {
		log.info("Searching additional by name: " + name);
		return additionalRepository.findByNameStartsWith(name);
	}

	public Collection<Additional> findByCategory(Long idCategory) {
		log.info("Searching additional by idCategory: " + idCategory);
		return additionalRepository.findByIdCategory(idCategory);
	}

}
