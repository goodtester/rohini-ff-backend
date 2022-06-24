/**
 * 
 */
package com.rohini.fastfoodapp.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import com.rohini.fastfoodapp.dto.UserDTO;
import com.rohini.fastfoodapp.dto.update.UserClientDTO;
import com.rohini.fastfoodapp.dto.update.UserEmployeeDTO;
import com.rohini.fastfoodapp.model.User;

/**
 * @author Rohini
 */
public interface IUserService {

	User create(User user, MultipartFile file);

	UserDTO update(Long id, User user, MultipartFile file);

	UserDTO updateEmployee(UserEmployeeDTO userEmployeeDTO, long id);

	UserDTO updateClient(UserClientDTO userClientDTO, long id, MultipartFile file);

	Boolean delete(Long idUser);

	Boolean exist(Long idUser);

	Boolean exist(String username);

}
