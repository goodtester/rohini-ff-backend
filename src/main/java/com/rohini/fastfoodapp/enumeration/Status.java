package com.rohini.fastfoodapp.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rohini
 */
@Getter
@AllArgsConstructor
public enum Status {

	INACTIVE(0),
	ACTIVE(1);
	private final int status;
}
