package com.rohini.fastfoodapp.repository;

import com.rohini.fastfoodapp.model.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rohini
 */
@Repository
public interface ITaxRepository extends JpaRepository<Tax, Long> {
}
