package com.mbe.ada.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mbe.ada.model.auth.dto.ResponseDTO;
import com.mbe.ada.model.user.User;
import com.mbe.ada.model.user.dto.CreateUserDTO;
import com.mbe.ada.model.user.dto.UserDTO;
import com.mbe.ada.repository.IUserRepository;
import com.mbe.ada.utils.DefaultRestMethods;

@Service
public class UserService<T>  implements DefaultRestMethods<CreateUserDTO> {

	@Autowired
	IUserRepository userRepos;
	
	
	@Override
	public ResponseDTO list() {

		List<User> data = userRepos.findAll();

		if (data.size() == 0)
			return new ResponseDTO(HttpStatus.NO_CONTENT.value(), "Nenhum usuário disponível", null);

		List<UserDTO> usersDTO = data.stream().map(user -> new UserDTO(user)).toList();

		return new ResponseDTO(HttpStatus.OK.value(), "Usuários Retornados com Sucesso", usersDTO);

	}

	@Override
	public ResponseDTO save(CreateUserDTO data) {
		
		User user = new User(data);
		User savedUser = userRepos.save(user);
		return new ResponseDTO(HttpStatus.OK.value(), "Usuário Criado com Sucesso", savedUser);

	}

	@Override
	public ResponseDTO update(Long id) {
		return null;
	}

	@Override
	public ResponseDTO delete(Long id) {
		return null;
	}

}
