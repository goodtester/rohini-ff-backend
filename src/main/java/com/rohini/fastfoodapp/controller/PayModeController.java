/**
 * 
 */
package com.rohini.fastfoodapp.controller;

import java.time.Instant;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rohini.fastfoodapp.model.PayMode;
import com.rohini.fastfoodapp.model.Response;
import com.rohini.fastfoodapp.service.PayModeServiceImp;
import lombok.RequiredArgsConstructor;

/**
 * @author Rohini
 */
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/pay-mode")
public class PayModeController {

	@Autowired
	private final PayModeServiceImp serviceImp;

//	CREATE
	@PostMapping()
	public ResponseEntity<Response> savePayMode(@RequestBody @Valid PayMode payMode) {
		return ResponseEntity
				.ok(Response.builder().timeStamp(Instant.now()).data(Map.of("payMode", serviceImp.create(payMode)))
						.message("Create category").status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).build());
	}

//  READ
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EMPLOYEE') OR hasRole('ROLE_CLIENT') ")
	@GetMapping(value = "/list")
	public ResponseEntity<Response> getPayMode() {
		return ResponseEntity.ok(Response.builder().timeStamp(Instant.now()).data(Map.of("payMode", serviceImp.list()))
				.message("List categories").status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).build());
	}

//	UPDATE
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response> updatePayMode(@PathVariable("id") Long id, @RequestBody @Valid PayMode payMode) {
		if (serviceImp.exist(id)) {
			return ResponseEntity.ok(Response.builder().timeStamp(Instant.now())
					.data(Map.of("payMode", serviceImp.update(id,payMode))).message("Update payMode with id:" + id)
					.status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).build());
		} else {
			return ResponseEntity.ok(
					Response.builder().timeStamp(Instant.now()).message("The payMode with id:" + id + " does not exist")
							.status(HttpStatus.BAD_REQUEST).statusCode(HttpStatus.BAD_REQUEST.value()).build());
		}
	}

//	DELETE
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response> deletePayMode(@PathVariable("id") Long id) {
		if (serviceImp.exist(id)) {
			return ResponseEntity.ok(Response.builder().timeStamp(Instant.now())
					.data(Map.of("payMode", serviceImp.delete(id))).message("Delete payMode with id: " + id)
					.status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).build());
		} else {
			return ResponseEntity
					.ok(Response.builder().timeStamp(Instant.now()).message("The payMode " + id + " does not exist")
							.status(HttpStatus.BAD_REQUEST).statusCode(HttpStatus.BAD_REQUEST.value()).build());
		}
	}
}
