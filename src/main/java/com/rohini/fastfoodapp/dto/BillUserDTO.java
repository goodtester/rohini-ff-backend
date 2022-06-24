/**
 * 
 */
package com.rohini.fastfoodapp.dto;

import java.util.Date;

import com.rohini.fastfoodapp.dto.validation.UserForBillDTO;
import com.rohini.fastfoodapp.enumeration.StatusBill;
import com.rohini.fastfoodapp.model.PayMode;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/**
 * @author Rohini
 */
@Data
public class BillUserDTO {
	private Long idBill;
	private String idTransaction;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
	private Date date;
	private int noTable;
	private int totalPrice;
	private StatusBill statusBill;
	private PayMode payMode;
	private UserForBillDTO userForBill;
	
}
