/**
 * 
 */
package com.rohini.fastfoodapp.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rohini.fastfoodapp.model.User;

/**
 * @author Rohini
 */
@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
	Collection<User> findByNameStartsWith(String name);

	//this query just search the clients
	@Query(value = "select * from user_app where user_roles=2 order by name LIMIT ?, 10", nativeQuery = true)
	Collection<User> list(Long page);
	
	User findByIdUser(Long idUser);
	
	User findByUsername(String username);
	
	User findByEmail(String email);

}
