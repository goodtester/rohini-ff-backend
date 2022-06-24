/**
 * 
 */
package com.rohini.fastfoodapp.dto.update;

import com.rohini.fastfoodapp.enumeration.Status;
import lombok.Data;

/**
 * @author Rohini
 */
@Data
public class UserClientDTO {
	private String name;
	private String urlImage;
	private int phone;
	private String email;
	private String password;
	private Status status;
}
