package com.mbe.ada.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mbe.ada.model.auth.dto.ResponseDTO;
import com.mbe.ada.model.user.User;
import com.mbe.ada.model.user.dto.CreateUserDTO;
import com.mbe.ada.model.user.dto.UserDTO;
import com.mbe.ada.repository.IUserRepository;
import com.mbe.ada.utils.AdaUtils;
import com.mbe.ada.utils.DefaultRestMethods;

@Service
public class UserService<T>  implements DefaultRestMethods<CreateUserDTO> {

	@Autowired
	IUserRepository userRepos;
	
	
	@Override
	public ResponseDTO list() {

		List<User> data = userRepos.findAll();

		if (data.size() == 0)
			return new ResponseDTO(HttpStatus.NO_CONTENT.value(), "Nenhum Usuário encontrado", null);

		List<UserDTO> usersDTO = data.stream().map(user -> new UserDTO(user)).toList();

		return new ResponseDTO(HttpStatus.OK.value(), "Usuários Retornados com Sucesso", usersDTO);

	}

	@Override
	public ResponseDTO save(CreateUserDTO data) {

		// E-mail Validation
		Optional<User> personOptEmail = userRepos.findByEmail(data.email());
		if (personOptEmail.isPresent())
			return new ResponseDTO(HttpStatus.CONFLICT.value(), "Validação: Este E-mail já é utilizado", null);

		// CPF Validation
		Optional<User> personOptCPF = userRepos.findByCpf(data.cpf());
		if (personOptCPF.isPresent())
			return new ResponseDTO(HttpStatus.CONFLICT.value(), "Validação: Este CPF já é utilizado", null);

		if (!AdaUtils.isValidCPF(data.cpf()))
			return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Validação: Este CPF não é Válido", false);

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
