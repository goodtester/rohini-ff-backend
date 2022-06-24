/**
 * 
 */
package com.rohini.fastfoodapp.service.interfaces;

import java.util.Collection;

import org.springframework.web.multipart.MultipartFile;

import com.rohini.fastfoodapp.model.Category;

/**
 * @author Rohini
 */
public interface ICategoryService {

    Category create(Category category, MultipartFile file);

    Category update(Long id,Category category, MultipartFile file);

    Boolean delete(Long idCategory);

    Collection<Category> list();

    Boolean exist(Long idCategory);

}
