/**
 * 
 */
package com.rohini.fastfoodapp.dto;

import com.rohini.fastfoodapp.enumeration.Status;
import lombok.Data;

/**
 * @author Rohini
 */
@Data
public class UserDTO {
	private Long idUser;
	private String name;
	private String username;
	private String urlImage;
	private int phone;
	private String email;
	private int discountPoint;
	private Status status;
}
