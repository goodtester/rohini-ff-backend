/**
 * 
 */
package com.rohini.fastfoodapp.service.interfaces;


import com.rohini.fastfoodapp.dto.OrdersDTO;
import com.rohini.fastfoodapp.enumeration.StatusOrder;
import com.rohini.fastfoodapp.model.Orders;

/**
 * @author Rohini
 */
public interface IOrdersService {

	OrdersDTO create(Orders orders);

	OrdersDTO update(Long id, Orders orders);

	Boolean updateStatus(Long idBill, StatusOrder statusOrder);
	Boolean delete(Long idOrders);

	Boolean exist(Long idOrders);

}
