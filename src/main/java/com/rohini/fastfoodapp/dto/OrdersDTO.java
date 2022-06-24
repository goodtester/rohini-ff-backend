/**
 * 
 */
package com.rohini.fastfoodapp.dto;

import java.util.Collection;

import com.rohini.fastfoodapp.enumeration.StatusOrder;
import com.rohini.fastfoodapp.model.Product;
import lombok.Data;

/**
 * @author Rohini
 */
@Data
public class OrdersDTO {

//	Order
	private Long idOrder;
	private StatusOrder statusOrder;
	private int amount;
	private int total;

//	Product
	private Product product;
	
//	Additional
	private Collection<com.rohini.fastfoodapp.model.Additional> Additional;
}
