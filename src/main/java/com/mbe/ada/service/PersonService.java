package com.mbe.ada.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.google.gson.Gson;
import com.mbe.ada.model.auth.dto.ResponseDTO;
import com.mbe.ada.model.group.dto.BasicGroupDTO;
import com.mbe.ada.model.person.Person;
import com.mbe.ada.model.person.dto.CreatePersonDTO;
import com.mbe.ada.model.person.dto.DetailPersonDTO;
import com.mbe.ada.model.person.dto.PersonDTO;
import com.mbe.ada.model.person.dto.UpdatePersonDTO;
import com.mbe.ada.model.photo.Photo;
import com.mbe.ada.model.user.User;
import com.mbe.ada.recognitionApi.RecognitionAPI;
import com.mbe.ada.repository.IPersonRepository;
import com.mbe.ada.repository.IUserRepository;
import com.mbe.ada.utils.AdaUtils;

@Service
public class PersonService {

	@Autowired
	IPersonRepository personRepos;

	@Autowired
	IUserRepository userRepos;

	@Autowired
	AttachmentService attachmentService;

	@Autowired
	ImageUtils imageUtils;
	
	
	public ResponseDTO list() {
		
		List<Person> personList = personRepos.findByIsActiveTrue();

		if (personList.isEmpty()) {
		    return new ResponseDTO(HttpStatus.NO_CONTENT.value(), "Sem Pessoas encontradas", null);
		}

		List<DetailPersonDTO> personsDTO = personList.stream().map(person -> {
		    String imgBase64 = attachmentService.getImageDataByPersonId(person.getId());
		    /*if (imgBase64 != null) {
		        imgBase64 = imgBase64.split(",")[1]; // remove o prefixo
		    }*/

		    List<BasicGroupDTO> groupsDTO = null;
		    if (person.getGroups() != null) {
		        groupsDTO = person.getGroups().stream()
		                .map(BasicGroupDTO::new)
		                .collect(Collectors.toList());
		    }

		    return new DetailPersonDTO(person, imgBase64, groupsDTO);
		}).toList();

		return new ResponseDTO(HttpStatus.OK.value(), "Lista de Pessoas retornada", personsDTO);

	}
	
	public ResponseDTO get(Long id) {
		 
    	Optional<Person> personOpt = personRepos.findById(id);
        
        if (personOpt.isEmpty()) 
        	return new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Pessoa não encontrada", null);
        
        
        //String photoBase64 = imageUtils.getImageBase64(personOpt.get().getCpf(), Person.class.toString());
        String imgBase64 = attachmentService.getImageDataByPersonId(personOpt.get().getId());
	    /*if (imgBase64 != null) {
	        imgBase64 = imgBase64.split(",")[1]; // remove o prefixo
	    }*/
        
        List<BasicGroupDTO> groupsDTO = personOpt.get().getGroups()
        		.stream()
        		.map(group -> new BasicGroupDTO(group))
				.collect(Collectors.toList());
	
		DetailPersonDTO detailPersonDTO = new DetailPersonDTO(personOpt.get(), imgBase64, groupsDTO);
		
        return new ResponseDTO(HttpStatus.OK.value(), "Usuário Retornado", detailPersonDTO);
	}

	public ResponseDTO save(CreatePersonDTO data) {

		DetailPersonDTO dto;
		RecognitionAPI recognitionAPI = new RecognitionAPI(); 
		Gson gson = new Gson();

		String photoBase64;
		
		
		
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
		
		// Remove o prefixo "data:image/png;base64"
		photoBase64 = data.photoBase64().split(",")[1];
		System.out.println(photoBase64);
		String jsonResponse = recognitionAPI.getEncodingFromImage(photoBase64);
		ResponseDTO responseAPI = gson.fromJson(jsonResponse, ResponseDTO.class);
		
		if(responseAPI.data() != null)
			personToCreate.setEncoding(responseAPI.data().toString());
		
		Person savedPerson = personRepos.save(personToCreate);

		if (data.photoBase64() != null) {
			Photo photoCreated = attachmentService.save(data.photoBase64(), savedPerson.getCpf(), savedPerson.getId(), true);
			dto = new DetailPersonDTO(savedPerson, photoCreated.getImageData());
		} else
			dto = new DetailPersonDTO(savedPerson);
		
		return new ResponseDTO(HttpStatus.OK.value(), "Nova Pessoa adicionada", dto);

	}


	public ResponseDTO update(Long id, UpdatePersonDTO data) {

        Optional<Person> personToUpdt = personRepos.findById(id);
        
        if (personToUpdt.isEmpty())
        	return new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Pessoa não encontrada", null);
        
        Person personToUpdate = personToUpdt.get();
        

    	// Verify User existence
        if(data.userId() != null && data.userId()> 0) {
        	
        	Optional<User> user = userRepos.findById(data.userId());
        	
        	if(user.isEmpty())
        		return new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Usuário Relacionado não encontrado", null);
        }
        
        personToUpdate.updateValues(data);
        Person updatedPerson = personRepos.save(personToUpdate);
        if (data.photoBase64() != null) {
        	attachmentService.save(data.photoBase64(), updatedPerson.getCpf(), updatedPerson.getId(), true);
        }
        DetailPersonDTO detailPersonDTO = new DetailPersonDTO(updatedPerson, ImageUtils.getImageBase64(updatedPerson.getCpf(), User.class.toString()));
        return new ResponseDTO(HttpStatus.OK.value(), "Usuário Atualizado", detailPersonDTO);
	}

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
