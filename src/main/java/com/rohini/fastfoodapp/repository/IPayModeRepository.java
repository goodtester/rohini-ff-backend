/**
 * 
 */
package com.rohini.fastfoodapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rohini.fastfoodapp.model.PayMode;

/**
 * @author Rohini
 */
@Repository
public interface IPayModeRepository extends JpaRepository<PayMode, Long>{
}
