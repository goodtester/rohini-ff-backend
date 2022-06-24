/**
 * 
 */
package com.rohini.fastfoodapp.service.interfaces;

import java.util.Collection;

import org.springframework.web.multipart.MultipartFile;

import com.rohini.fastfoodapp.model.Additional;

/**
 * @author Rohini
 */
public interface IAdditionalService {

	Additional create(Additional additional, MultipartFile file);

	Additional update(Long id,Additional additional,MultipartFile file);

	Boolean delete(Long id_Additional);

	Collection<Additional> list();

	Boolean exist(Long id_Additional);
}
