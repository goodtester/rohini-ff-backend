/**
 * 
 */
package com.rohini.fastfoodapp.security;

/**
 * @author Rohini
 */
public final class OperationUtil {

	private static final String KEYWORD = "FASTFOODAPP_22";

	private OperationUtil() {
		throw new java.lang.UnsupportedOperationException("Utility class and cannot be instantiated");
	}

	public static String keyValue() {
		return KEYWORD;
	}
}
