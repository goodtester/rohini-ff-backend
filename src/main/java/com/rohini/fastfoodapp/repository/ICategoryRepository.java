/**
 * 
 */
package com.rohini.fastfoodapp.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rohini.fastfoodapp.model.Category;

/**
 * @author Rohini
 */
@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
	Collection<Category> findAllByNameStartsWith(String name);

	Category findByName(String name);
}
