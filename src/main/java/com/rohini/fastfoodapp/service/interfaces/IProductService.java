/**
 * 
 */
package com.rohini.fastfoodapp.service.interfaces;
import org.springframework.web.multipart.MultipartFile;

import com.rohini.fastfoodapp.model.Product;

/**
 * @author Rohini
 */
public interface IProductService {

	Product create(Product product, MultipartFile file);

	Product update(Long id, Product product, MultipartFile file);

	Boolean delete(Long idProduct);

	Boolean exist(Long idProduct);

}
