/**
 * 
 */
package com.rohini.fastfoodapp.controller;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.time.Instant;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.rohini.fastfoodapp.enumeration.AppUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.rohini.fastfoodapp.model.User;
import com.rohini.fastfoodapp.security.OperationUtil;
import com.rohini.fastfoodapp.dto.update.UserClientDTO;
import com.rohini.fastfoodapp.dto.update.UserEmployeeDTO;
import com.rohini.fastfoodapp.model.Response;
import com.rohini.fastfoodapp.service.UserServiceImp;
import lombok.RequiredArgsConstructor;

/**
 * @author Rohini
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

	@Autowired
	private final UserServiceImp serviceImp;
  
//	CREATE
	@PostMapping()
	public ResponseEntity<Response> saveUser(@RequestParam("request") @Valid String strUser,
			@RequestParam("userimage") @Nullable MultipartFile file) {

		try {
			User user = new ObjectMapper().readValue(strUser, User.class);
			if (serviceImp.findByUsername(user.getUsername()) == null) {
				if (serviceImp.findByEmail(user.getEmail()) == null) {
					return ResponseEntity.ok(Response.builder().timeStamp(Instant.now())
							.data(Map.of("user", serviceImp.create(user, file))).message("Create user")
					 		.status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).build());
				} else {
					return ResponseEntity.ok(Response.builder().timeStamp(Instant.now())
							.message("The email: " + user.getEmail() + " already exist").status(HttpStatus.BAD_REQUEST)
							.statusCode(HttpStatus.BAD_REQUEST.value()).build());
				}
			} else {
				return ResponseEntity.ok(Response.builder().timeStamp(Instant.now())
						.message("The username: " + user.getUsername() + " already exist")
						.status(HttpStatus.BAD_REQUEST).statusCode(HttpStatus.BAD_REQUEST.value()).build());
			}

		} catch (JsonProcessingException e) {
			return ResponseEntity.ok(
					Response.builder().timeStamp(Instant.now()).message("Error creating the user: " + e.getMessage())
							.status(HttpStatus.BAD_REQUEST).statusCode(HttpStatus.BAD_REQUEST.value()).build());
		}

	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EMPLOYEE')")
//  READ
	@GetMapping(value = "/list/{page}")
	public ResponseEntity<Response> getUser(@PathVariable("page") Long page) {
		return ResponseEntity.ok(Response.builder().timeStamp(Instant.now()).data(Map.of("user", serviceImp.list(page)))
				.message("List users").status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).build());
	}

//	UPDATE
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EMPLOYEE') OR hasRole('ROLE_CLIENT')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response> updateUser(@PathVariable("id") Long id,
			@RequestParam("request") @Valid String strUser, @RequestParam("profileimage") @Nullable MultipartFile file,
			HttpServletRequest request) {
		User userOld = serviceImp.findById(id);
		if (userOld != null) {
			AppUserRole userRole = userOld.getUserRoles();
			if (request.isUserInRole("ROLE_CLIENT")
					&& (userRole.getAuthority() == "ROLE_ADMIN" || userRole.getAuthority() == "ROLE_EMPLOYEE")) {
				return ResponseEntity.ok(
						Response.builder().timeStamp(Instant.now()).message(("Error user without permission to update"))
								.status(HttpStatus.BAD_REQUEST).statusCode(HttpStatus.BAD_REQUEST.value()).build());
			}
			if (request.isUserInRole("ROLE_EMPLOYEE") && (userRole.getAuthority() == "ROLE_ADMIN")) {
				return ResponseEntity.ok(
						Response.builder().timeStamp(Instant.now()).message(("Error user without permission to update"))
								.status(HttpStatus.BAD_REQUEST).statusCode(HttpStatus.BAD_REQUEST.value()).build());
			}

			String authorizationHeader = request.getHeader(AUTHORIZATION);
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				try {
					String token = authorizationHeader.substring("Bearer ".length());
					Algorithm algorithm = Algorithm.HMAC256(OperationUtil.keyValue().getBytes());
					JWTVerifier verifier = JWT.require(algorithm).build();
					DecodedJWT decodeJWT = verifier.verify(token);

					if (request.isUserInRole("ROLE_CLIENT") && userRole.getAuthority() == "ROLE_CLIENT") {
						if (userOld.getUsername().equals(decodeJWT.getSubject().toString())) {
							UserClientDTO userClientDTO = new ObjectMapper().readValue(strUser, UserClientDTO.class);
							return ResponseEntity.ok(Response.builder().timeStamp(Instant.now())
									.data(Map.of("user", serviceImp.updateClient(userClientDTO, id, file)))
									.message("Update user with id:" + id).status(HttpStatus.OK)
									.statusCode(HttpStatus.OK.value()).build());
						} else {
							return ResponseEntity.ok(Response.builder().timeStamp(Instant.now())
									.message(("Error updating user, you can not update other clients"))
									.status(HttpStatus.BAD_REQUEST).statusCode(HttpStatus.BAD_REQUEST.value()).build());
						}
					}
					return actionForRole(id, strUser, request, file);
				} catch (Exception e) {
					return ResponseEntity.ok(Response.builder().timeStamp(Instant.now())
							.message(("Error updating user by client: " + e.getMessage()))
							.status(HttpStatus.BAD_REQUEST).statusCode(HttpStatus.BAD_REQUEST.value()).build());
				}
			}
		}
		return ResponseEntity
				.ok(Response.builder().timeStamp(Instant.now()).message("The user with id:" + id + " does not exist")
						.status(HttpStatus.BAD_REQUEST).statusCode(HttpStatus.BAD_REQUEST.value()).build());
	}

//	DELETE
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response> deleteUser(@PathVariable("id") Long id) {
		if (serviceImp.exist(id)) {
			return ResponseEntity.ok(Response.builder().timeStamp(Instant.now())
					.data(Map.of("user", serviceImp.delete(id))).message("Delete user with id: " + id)
					.status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).build());
		} else {
			return ResponseEntity
					.ok(Response.builder().timeStamp(Instant.now()).message("The user " + id + " does not exist")
							.status(HttpStatus.BAD_REQUEST).statusCode(HttpStatus.BAD_REQUEST.value()).build());
		}
	}

//	SEARCH BY NAME
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EMPLOYEE')")
	@GetMapping(value = "/{name}")
	public ResponseEntity<Response> getUserByName(@PathVariable("name") String name) {

		if (serviceImp.findByName(name) != null) {
			return ResponseEntity.ok(Response.builder().timeStamp(Instant.now())
					.data(Map.of("User", serviceImp.findByName(name))).message("Get user by name: " + name)
					.status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).build());

		} else {
			return ResponseEntity.ok(
					Response.builder().timeStamp(Instant.now()).message("The user called " + name + " does not exist")
							.status(HttpStatus.BAD_REQUEST).statusCode(HttpStatus.BAD_REQUEST.value()).build());
		}
	}

//	SEARCH BY EMAIL
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EMPLOYEE')")
	@GetMapping(value = "/email/{email}")
	public ResponseEntity<Response> getUserByEmail(@PathVariable("email") String email) {
		if (serviceImp.findByEmail(email) != null) {
			return ResponseEntity.ok(Response.builder().timeStamp(Instant.now())
					.data(Map.of("User", serviceImp.findByEmail(email))).message("Get user by email: " + email)
					.status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).build());
		} else {
			return ResponseEntity.ok(Response.builder().timeStamp(Instant.now())
					.message("The User with mail: " + email + " does not exist").status(HttpStatus.BAD_REQUEST)
					.statusCode(HttpStatus.BAD_REQUEST.value()).build());
		}
	}

//	SEARCH BY EMAIL VALIDATION
	@GetMapping(value = "/is-valid-email/{email}")
	public ResponseEntity<Response> getUserByEmailValidation(@PathVariable("email") String email) {
		if (serviceImp.findByEmailValid(email) != null) {
			return ResponseEntity.ok(Response.builder().timeStamp(Instant.now())
					.data(Map.of("User", serviceImp.findByEmailValid(email))).message("Get user by email: " + email)
					.status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).build());
		} else {
			return ResponseEntity.ok(Response.builder().timeStamp(Instant.now())
					.message("The User with mail: " + email + " does not exist").status(HttpStatus.BAD_REQUEST)
					.statusCode(HttpStatus.BAD_REQUEST.value()).build());
		}
	}

//	SEARCH BY USERNAME VALIDATION	
	@GetMapping(value = "/is-valid-username/{username}")
	public ResponseEntity<Response> getUserByNameValidation(@PathVariable("username") String username) {

		if (serviceImp.findByUsernameValidation(username) != null) {
			return ResponseEntity.ok(Response.builder().timeStamp(Instant.now())
					.data(Map.of("User", serviceImp.findByUsernameValidation(username)))
					.message("Get user by name: " + username).status(HttpStatus.OK).statusCode(HttpStatus.OK.value())
					.build());

		} else {
			return ResponseEntity.ok(Response.builder().timeStamp(Instant.now())
					.message("The user called " + username + " does not exist").status(HttpStatus.BAD_REQUEST)
					.statusCode(HttpStatus.BAD_REQUEST.value()).build());
		}
	}

	private ResponseEntity<Response> actionForRole(Long id, String strUser, HttpServletRequest request,
			MultipartFile file) throws JsonMappingException, JsonProcessingException {
		if (request.isUserInRole("ROLE_EMPLOYEE")) {
			UserEmployeeDTO userEmployeeDTO = new ObjectMapper().readValue(strUser, UserEmployeeDTO.class);
			return ResponseEntity.ok(Response.builder().timeStamp(Instant.now())
					.data(Map.of("user", serviceImp.updateEmployee(userEmployeeDTO, id)))
					.message("Update user with id:" + id).status(HttpStatus.OK).statusCode(HttpStatus.OK.value())
					.build());

		} else if (request.isUserInRole("ROLE_ADMIN")) {
			User user = new ObjectMapper().readValue(strUser, User.class);
			return ResponseEntity.ok(Response.builder().timeStamp(Instant.now())
					.data(Map.of("user", serviceImp.update(id, user, file))).message("Update user with id:" + id)
					.status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).build());
		}
		return null;
	}

}
