/**
 * 
 */
package com.rohini.fastfoodapp.security;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.rohini.fastfoodapp.model.Response;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rohini
 */
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		
		if(request.getServletPath().equals("/api/v1/login/") || request.getServletPath().equals("/api/v1/token-refresh/")) {
			filterChain.doFilter(request, response);
		}else {
			// here is where I start to check if the user has permission
			String authorizationHeader = request.getHeader(AUTHORIZATION);
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				try {
					// Remove the word Bearer, because we just want the token
					String token = authorizationHeader.substring("Bearer ".length());
					// Reference to the keyValue
					Algorithm algorithm = Algorithm.HMAC256(OperationUtil.keyValue().getBytes());
					JWTVerifier verifier = JWT.require(algorithm).build();
					DecodedJWT decodeJWT = verifier.verify(token);
					
					String username = decodeJWT.getSubject();
					
					String[] roles = decodeJWT.getClaim("roles").asArray(String.class);
					
					Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
					authorities.add(new SimpleGrantedAuthority(roles[0]));
					
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							username, null,authorities);
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					filterChain.doFilter(request, response);

				} catch (Exception e) {
					log.error("Error logging in AuthorizationFilter: " + e.getMessage());
					response.setHeader("Error ", e.getMessage());
					response.setStatus(401);
					response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
					new ObjectMapper().writeValue(response.getOutputStream(),Response.builder()
							.message(e.getMessage()).status(HttpStatus.UNAUTHORIZED).statusCode(HttpStatus.UNAUTHORIZED.value()).build());
					
				}

			} else {
				filterChain.doFilter(request, response);
			}
		}
			
		

	}
	

}
