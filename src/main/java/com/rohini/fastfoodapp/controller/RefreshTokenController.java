package com.rohini.fastfoodapp.controller;

import com.rohini.fastfoodapp.dto.UserDTO;
import com.rohini.fastfoodapp.enumeration.AppUserRole;
import com.rohini.fastfoodapp.model.Response;
import com.rohini.fastfoodapp.model.User;
import com.rohini.fastfoodapp.security.OperationUtil;
import com.rohini.fastfoodapp.service.BillServiceImp;
import com.rohini.fastfoodapp.service.UserServiceImp;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author Rohini
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/token-refresh")
public class RefreshTokenController {

	@Autowired
	private final UserServiceImp serviceImp;
	@Autowired
	private final BillServiceImp billServiceImp;

	@GetMapping(value = "/unattributed/{idBill}")
	public ResponseEntity<Response> refreshToken2(@PathVariable("idBill") Long idBill, HttpServletRequest request) {
		if (billServiceImp.exist(idBill)) {
			Algorithm algorithm = Algorithm.HMAC256(OperationUtil.keyValue().getBytes());
			String access_token = JWT.create().withKeyId(idBill.toString())
					// give 60 minutes for the token to expire
					.withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
					.withIssuer(request.getRequestURL().toString())
					.withClaim("roles", List.of(AppUserRole.ROLE_UNATTRIBUTED.getAuthority())).sign(algorithm);
			return ResponseEntity.ok(Response.builder().timeStamp(Instant.now())
					.data(Map.of("unattributed", access_token)).message("token unattributed").status(HttpStatus.OK)
					.statusCode(HttpStatus.OK.value()).build());
		}
		return ResponseEntity.ok(Response.builder().timeStamp(Instant.now()).message("the bill does not exist")
				.status(HttpStatus.BAD_REQUEST).statusCode(HttpStatus.BAD_REQUEST.value()).build());
	}

	@GetMapping()
	public ResponseEntity<Response> refreshToken(HttpServletRequest request, HttpServletResponse response)
			throws StreamWriteException, DatabindException, IOException {
		String authorizationHeader = request.getHeader(AUTHORIZATION);

		try {
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				try {
					// Remove the word Bearer, because we just want the token
					String refresh_token = authorizationHeader.substring("Bearer ".length());
					// Reference to the keyValue
					Algorithm algorithm = Algorithm.HMAC256(OperationUtil.keyValue().getBytes());
					JWTVerifier verifier = JWT.require(algorithm).build();
					DecodedJWT decodeJWT = verifier.verify(refresh_token);
					String username = decodeJWT.getSubject();
					if (serviceImp.findByUsername(username) != null) {
						User user = serviceImp.findByUsername(username);

						String access_token = JWT.create().withSubject(user.getUsername())
								.withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
								.withIssuer(request.getRequestURL().toString())
								.withClaim("roles", List.of(user.getUserRoles().getAuthority())).sign(algorithm);

						Map<String, Object> tokens = new HashMap<>();
						tokens.put("valid", true);
						tokens.put("access_token", access_token);
						return ResponseEntity.ok(Response.builder().timeStamp(Instant.now())
								.data(Map.of("tokens", tokens)).message("tokens").status(HttpStatus.OK)
								.statusCode(HttpStatus.OK.value()).build());
					} else {
						log.error("Error username not found");
						return ResponseEntity
								.ok(Response.builder().timeStamp(Instant.now()).message("Error username not found")
										.status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).build());
					}

				} catch (Exception e) {
					log.error("Error logging in AuthorizationFilter: " + e.getMessage());
					return ResponseEntity.ok(Response.builder().timeStamp(Instant.now()).message(e.getMessage())
							.status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).build());
				}

			}
		} catch (Exception e) {
			return ResponseEntity.ok(Response.builder().timeStamp(Instant.now()).message("Refresh token is missing")
					.status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).build());
		}
		return ResponseEntity.ok(Response.builder().timeStamp(Instant.now()).message("Refresh token is missing")
				.status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).build());

	}

	@GetMapping(value = "/user")
	public ResponseEntity<Response> getUser(HttpServletRequest request, HttpServletResponse response)
			throws StreamWriteException, DatabindException, IOException {
		String authorizationHeader = request.getHeader(AUTHORIZATION);
		try {
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				try {
					// Remove the word Bearer, because we just want the token
					String refresh_token = authorizationHeader.substring("Bearer ".length());
					// Reference to the keyValue
					Algorithm algorithm = Algorithm.HMAC256(OperationUtil.keyValue().getBytes());
					JWTVerifier verifier = JWT.require(algorithm).build();
					DecodedJWT decodeJWT = verifier.verify(refresh_token);
					String username = decodeJWT.getSubject();
					if (serviceImp.findByUsername(username) != null) {
						User user = serviceImp.findByUsername(username);
						UserDTO user1 = serviceImp.getUser(username);
						String access_token = JWT.create().withSubject(user.getUsername())
								// 10 minutes before expire
								.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
								.withIssuer(request.getRequestURL().toString())
								.withClaim("roles", List.of(user.getUserRoles().getAuthority())).sign(algorithm);

						Map<String, Object> tokens = new HashMap<>();
						tokens.put("valid", true);
						tokens.put("access_token", access_token);
						Map<String, Object> objectMap = new HashMap<>();
						objectMap.put("user", user1);
						objectMap.put("tokens", tokens);

						return ResponseEntity.ok(Response.builder().timeStamp(Instant.now())
								.data(Map.of("user", objectMap)).message("user").status(HttpStatus.OK)
								.statusCode(HttpStatus.OK.value()).build());
					} else {
						log.error("Error username not found");
						return ResponseEntity
								.ok(Response.builder().timeStamp(Instant.now()).message("Error username not found")
										.status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).build());
					}

				} catch (Exception e) {
					log.error("Error logging in AuthorizationFilter: " + e.getMessage());
					return ResponseEntity.ok(Response.builder().timeStamp(Instant.now()).message(e.getMessage())
							.status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).build());
				}

			}
		} catch (Exception e) {
			return ResponseEntity.ok(Response.builder().timeStamp(Instant.now()).message("Refresh token is missing")
					.status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).build());
		}
		return ResponseEntity.ok(Response.builder().timeStamp(Instant.now()).message("Refresh token is missing")
				.status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).build());

	}

}