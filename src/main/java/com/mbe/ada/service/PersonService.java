package com.mbe.ada.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mbe.ada.model.auth.dto.ResponseDTO;
import com.mbe.ada.model.person.Person;
import com.mbe.ada.model.person.dto.CreatePersonDTO;
import com.mbe.ada.model.person.dto.DetailPersonDTO;
import com.mbe.ada.model.photo.Photo;
import com.mbe.ada.model.user.User;
import com.mbe.ada.repository.IPersonRepository;
import com.mbe.ada.repository.IUserRepository;
import com.mbe.ada.utils.AdaUtils;
import com.mbe.ada.utils.DefaultRestMethods;

@Service
public class PersonService implements DefaultRestMethods<CreatePersonDTO> {

	@Autowired
	IPersonRepository personRepos;

	@Autowired
	IUserRepository userRepos;

	@Autowired
	PhotoService photoService;

	public ResponseDTO save(CreatePersonDTO data) {

		DetailPersonDTO dto;
		
		Person personToCreate = new Person(data);

		// Verify User existence
		if (data.userId() != null && data.userId() > 0) {

			Optional<User> user = userRepos.findById(data.userId());

			if (user.isEmpty())
				return new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Usuário relacionado à Pessoa não encontrado",
						null);

		}

		// E-mail Validation
		Optional<Person> personOptEmail = personRepos.findByEmail(data.email());
		if (personOptEmail.isPresent())
			return new ResponseDTO(HttpStatus.CONFLICT.value(), "Validação: Este E-mail já é utilizado", null);

		// CPF Validation
		Optional<Person> personOptCPF = personRepos.findByCpf(data.cpf());
		if (personOptCPF.isPresent())
			return new ResponseDTO(HttpStatus.CONFLICT.value(), "Validação: Este CPF já é utilizado", null);

		if (!AdaUtils.isValidCPF(data.cpf()))
			return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Validação: Este CPF não é Válido", false);

		Person savedPerson = personRepos.save(personToCreate);

		if (data.photoBase64() != null) {
			Photo photoCreated = photoService.save(data.photoBase64(), savedPerson.getCpf(), savedPerson.getId(), true);
			dto = new DetailPersonDTO(savedPerson, photoCreated.getImageData());
		} else
			dto = new DetailPersonDTO(savedPerson);
		
		return new ResponseDTO(HttpStatus.OK.value(), "Nova Pessoa adicionada", dto);

	}

	@Override
	public ResponseDTO list() {
		return null;
	}

	@Override
	public ResponseDTO update(Long id) {
		return null;
	}

	@Override
	public ResponseDTO delete(Long id) {

		Optional<Person> personOpt = personRepos.findById(id);

		if (personOpt.isEmpty())
			return new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Pessoa não encontrada", null);

		Person personToDelete = personOpt.get();
		personToDelete.setIsActive(false);
		personRepos.save(personToDelete);
		
		return new ResponseDTO(HttpStatus.OK.value(), "Pessoa Desativada", null);
        
	}

}
