/**
 * 
 */
package com.rohini.fastfoodapp.dto;
import lombok.Data;

/**
 * @author Rohini
 */
@Data
public class ReportProductDTO {

	private Long idProduct;
	private String name;
	private double amount;
	private double total;
}
