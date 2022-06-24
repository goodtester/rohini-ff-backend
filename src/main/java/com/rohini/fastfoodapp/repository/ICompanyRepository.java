package com.rohini.fastfoodapp.repository;

import com.rohini.fastfoodapp.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rohini
 */
@Repository
public interface ICompanyRepository  extends JpaRepository<Company, Long> {



}
