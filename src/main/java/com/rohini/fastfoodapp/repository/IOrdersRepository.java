/**
 * 
 */
package com.rohini.fastfoodapp.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rohini.fastfoodapp.model.Orders;

/**
 * @author Rohini
 */
@Repository
public interface IOrdersRepository extends JpaRepository<Orders, Long> {

	@Query(value = "SELECT ord.* FROM orders ord join bill bi on ord.id_bill=bi.id_bill where bi.id_bill=?", nativeQuery = true)
	Collection<Orders> findByIdBill(Long idBill);

	@Modifying
	@Query(value = "UPDATE bill SET total_price=(SELECT SUM(COALESCE(total,0)) FROM orders WHERE id_bill=:idBill) WHERE id_bill=:idBill", nativeQuery = true)
	void setTotalPriceBill(@Param("idBill") Long idBill);

}
