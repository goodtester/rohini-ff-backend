/**
 * 
 */
package com.rohini.fastfoodapp.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rohini.fastfoodapp.model.Additional;

/**
 * @author Rohini
 */
@Repository
public interface IAdditionalRepository extends JpaRepository<Additional, Long> {

	Additional findByIdAdditional(Long idAdditional);

	Collection<Additional> findByNameStartsWith(String name);
	
	Additional findByName(String name);
	
	@Query(value = "SELECT additional.*	FROM public.additional additional join additional_category category on category.id_additional=additional.id_additional "
			+ "where category.id_category=:idcategory", nativeQuery = true)
	Collection<Additional> findByIdCategory(@Param("idcategory") Long idCategory);
}
