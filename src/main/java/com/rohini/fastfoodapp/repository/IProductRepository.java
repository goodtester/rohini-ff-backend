/**
 * 
 */
package com.rohini.fastfoodapp.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rohini.fastfoodapp.model.Product;

/**
 * @author Rohini
 */
@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
	Collection<Product> findByNameStartsWith(String name);

	@Query(value = "SELECT * FROM product order by name LIMIT 10 OFFSET ?", nativeQuery = true)
	Collection<Product> list(Long page);
	
	@Query(value = "SELECT * FROM product where highlight>0 order by highlight asc", nativeQuery = true)
	Collection<Product> findAllOrderByHighlight();
	
	@Query(value = "SELECT pro.* FROM product pro join category ca on ca.id_category=pro.id_category  WHERE ca.name = ?", nativeQuery = true)
	Collection<Product> findByNameCategory(String name);
	
//	@Query(value = "SELECT * FROM product WHERE id_product=?", nativeQuery = true)
	Product findByIdProduct(Long idProduct);
}
