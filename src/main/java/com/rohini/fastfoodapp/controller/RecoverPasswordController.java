/**
 * 
 */
package com.rohini.fastfoodapp.controller;

import java.time.Instant;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rohini.fastfoodapp.dto.UserDTO;
import com.rohini.fastfoodapp.model.Response;
import com.rohini.fastfoodapp.service.UserServiceImp;
import lombok.RequiredArgsConstructor;

/**
 * @author Rohini
 */
@RestController
@RequiredArgsConstructor
public class RecoverPasswordController {

	@Autowired
	private final UserServiceImp serviceImp;
  
	@GetMapping(value = "/recover-password")
	public ResponseEntity<Response> recoverPassword(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name = "email") String email) throws MessagingException {
		UserDTO user = serviceImp.findByEmail(email);
		if (user != null) {
			return ResponseEntity.ok(Response.builder().timeStamp(Instant.now())
					.data(Map.of("user",
							serviceImp.sendMail(request, response, email, user.getUsername(), user.getName())))
					.message("Mail sent").status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).build());
		} else {
			return ResponseEntity.ok(Response.builder().timeStamp(Instant.now()).message("The email do not exist")
					.status(HttpStatus.BAD_REQUEST).statusCode(HttpStatus.BAD_REQUEST.value()).build());
		}
	}

	@PostMapping("/reset-password")
	public ResponseEntity<Response> resetPassword(HttpServletRequest request,
			@RequestParam(name = "token") String token) {
		String newPassword = request.getParameter("newPassword");
		String repeatNewPassword = request.getParameter("repeatNewPassword");
		if (newPassword.equals(repeatNewPassword)) {
			String username = serviceImp.validationToken(token);
			if (username != null) {
				if (serviceImp.findByUsername(username) != null) {
					return ResponseEntity.ok(Response.builder().timeStamp(Instant.now())
							.data(Map.of("user", serviceImp.updatePasswordClient(username, newPassword)))
							.message("Updating user").status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).build());
				} else {
					return ResponseEntity.ok(Response.builder().timeStamp(Instant.now())
							.message("The user: " + username + " not exist!").status(HttpStatus.UNAUTHORIZED)
							.statusCode(HttpStatus.UNAUTHORIZED.value()).build());
				}
			} else {
				return ResponseEntity.ok(
						Response.builder().timeStamp(Instant.now()).message("The Token's Signature resulted invalid!")
								.status(HttpStatus.UNAUTHORIZED).statusCode(HttpStatus.UNAUTHORIZED.value()).build());
			}
		} else {
			return ResponseEntity.ok(Response.builder().timeStamp(Instant.now()).message("Passwords must be the same")
					.status(HttpStatus.BAD_REQUEST).statusCode(HttpStatus.BAD_REQUEST.value()).build());
		}
	}
}
