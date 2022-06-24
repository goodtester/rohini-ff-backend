/**
 * 
 */
package com.rohini.fastfoodapp.dto;

import java.util.Collection;

import lombok.Data;

/**
 * @author Rohini
 */
@Data
public class UserBillOrdersDTO {

	private BillUserDTO billUserDTO;

	private Collection<OrdersDTO> ordersDTO;

}
