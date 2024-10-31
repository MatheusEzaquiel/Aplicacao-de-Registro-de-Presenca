package com.mbe.ada.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mbe.ada.model.auth.dto.ResponseDTO;
import com.mbe.ada.model.person.Person;
import com.mbe.ada.model.person.dto.CreatePersonDTO;
import com.mbe.ada.model.person.dto.DetailPersonDTO;
import com.mbe.ada.model.photo.Photo;
import com.mbe.ada.model.user.User;
import com.mbe.ada.repository.IPersonRepository;
import com.mbe.ada.repository.IUserRepository;

@Service
public class PersonService {
	
	@Autowired
	IPersonRepository personRepos;
	
	@Autowired
	IUserRepository userRepos;
	
	@Autowired
	PhotoService photoService;

	
	public ResponseDTO save(CreatePersonDTO data) {

		Person personToCreate = new Person(data);
		
		// Verify User existence
        if(data.userId() != null && data.userId()> 0) {
        	
        	Optional<User> user = userRepos.findById(data.userId());
        	
        	if(user.isEmpty())
        		return new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Usuário relacionado à Pessoa não encontrado", null);	
        	
        }
        
        
        Person savedPerson = personRepos.save(personToCreate);
        
        
        if(data.photo() != null) {
        	Photo photoCreated = photoService.save(data.photo(), data.photoName(), savedPerson.getId(), true);
            DetailPersonDTO dto = new DetailPersonDTO(savedPerson, photoCreated.getImageData());
        }

        
        DetailPersonDTO dto = new DetailPersonDTO(savedPerson);
        return new ResponseDTO(HttpStatus.OK.value(), "Nova Pessoa adicionada", dto);	
		
	}

}
