/**
 * 
 */
package com.rohini.fastfoodapp.dto.validation;

import lombok.Data;

/**
 * @author Rohini
 */
@Data
public class UserForBillDTO {
	private Long idUser;
	private String urlImage;
	private String username;
	private String name;
}
