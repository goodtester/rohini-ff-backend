/**
 * 
 */
package com.rohini.fastfoodapp.service.interfaces;

import com.rohini.fastfoodapp.dto.BillUserDTO;
import com.rohini.fastfoodapp.dto.UserBillOrdersDTO;
import com.rohini.fastfoodapp.enumeration.StatusBill;
import com.rohini.fastfoodapp.enumeration.StatusOrder;
import com.rohini.fastfoodapp.model.Bill;

import java.util.Collection;

/**
 * @author Rohini
 */
public interface IBillService {

	BillUserDTO create(Bill bill);

	UserBillOrdersDTO findByIdBill(Long idBill);

	Collection<UserBillOrdersDTO> findByNewIdUser(String username, StatusBill statusBill, String startDate, String endDate);

	Collection<UserBillOrdersDTO> findByOrder(StatusOrder statusOrder, String startDate, String endDate);
	
	BillUserDTO update(Long idBill, Bill bill);

	BillUserDTO updateStatusBill(Long idBill, StatusBill statusBill);

	Boolean delete(Long idBill);

	Boolean exist(Long idBill);
}
