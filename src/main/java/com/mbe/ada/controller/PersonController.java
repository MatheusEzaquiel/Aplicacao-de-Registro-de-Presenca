package com.mbe.ada.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbe.ada.model.auth.dto.ResponseDTO;
import com.mbe.ada.model.group.dto.BasicGroupDTO;
import com.mbe.ada.model.person.Person;
import com.mbe.ada.model.person.dto.CreatePersonDTO;
import com.mbe.ada.model.person.dto.DetailPersonDTO;
import com.mbe.ada.model.person.dto.PersonDTO;
import com.mbe.ada.model.person.dto.UpdatePersonDTO;
import com.mbe.ada.model.user.User;
import com.mbe.ada.repository.IGroupRepository;
import com.mbe.ada.repository.IPersonRepository;
import com.mbe.ada.repository.IUserRepository;
import com.mbe.ada.service.AttachmentService;
import com.mbe.ada.service.ImageUtils;
import com.mbe.ada.service.PersonService;

@RestController
@RequestMapping(value = "/persons")
public class PersonController {
	
	@Autowired
	IPersonRepository personRepos;

	@Autowired
	IUserRepository userRepos;
	
	@Autowired
	AttachmentService attachmentService;
	
	@Autowired 
	IGroupRepository groupRepos;
	
	@Autowired
	PersonService personService;
	
	@Autowired
	ImageUtils imageUtils;
	
	@GetMapping
	public  ResponseEntity<ResponseDTO>  index() {		
		ResponseDTO response = personService.list();
        return new ResponseEntity<ResponseDTO>(response, HttpStatusCode.valueOf(response.status()));
	}
	
	@GetMapping("/students")
	public ResponseEntity<List<DetailPersonDTO>>  listStudents() {		
		
		List<Person> data = personRepos.findByIsTeacherFalse();
		
		if (data.size() == 0)
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

		List<DetailPersonDTO> dataDTO = data.stream().map(person -> {

			String imageData = attachmentService.getImageDataByPersonId(person.getId());

			List<BasicGroupDTO> groupsDTO = person.getGroups().stream().map(group -> new BasicGroupDTO(group))
					.collect(Collectors.toList());

			return new DetailPersonDTO(person, imageData, groupsDTO);
		}).toList();

		return new ResponseEntity<List<DetailPersonDTO>>(dataDTO, HttpStatus.OK);
	}
	
	@GetMapping("/students/inactive")
	public ResponseEntity<List<DetailPersonDTO>>  listInactiveStudents() {		
		
		List<Person> data = personRepos.findByIsTeacherFalseAndIsActiveFalse();
		
		if(data.size() == 0)
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
		List<DetailPersonDTO> dataDTO = data.stream().map(person -> {
			String imageData = attachmentService.getImageDataByPersonId(person.getId());
			return new DetailPersonDTO(person, imageData);
		}).toList();
		
		return new ResponseEntity<List<DetailPersonDTO>>(dataDTO, HttpStatus.OK);
	}
	
	@GetMapping("/teachers")
	public ResponseEntity<List<DetailPersonDTO>>  listTeachers() {		
		
		List<Person> data = personRepos.findByIsTeacherTrue();
		
		if(data.size() == 0)
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

		List<DetailPersonDTO> dataDTO = data.stream().map(person -> {

			String imageData = attachmentService.getImageDataByPersonId(person.getId());

			List<BasicGroupDTO> groupsDTO = person.getGroups().stream().map(group -> new BasicGroupDTO(group))
					.collect(Collectors.toList());

			return new DetailPersonDTO(person, imageData, groupsDTO);
		}).toList();

		return new ResponseEntity<List<DetailPersonDTO>>(dataDTO, HttpStatus.OK);
	}

	/*@PostMapping(consumes = "multipart/form-data")
	public ResponseEntity<DetailPersonDTO> create(
			@RequestParam("name") String name,
			@RequestParam("lastname") String lastname,
			@RequestParam("email") String email,
			@RequestParam("cpf") String cpf,
			@RequestParam("birthDate") LocalDate birthDate,
			@RequestParam("isTeacher") Boolean isTeacher,
			@RequestParam(value="userId" ,required = false) Long userId,
			@RequestParam("photo") MultipartFile photo) {
		
		CreatePersonDTO data = new CreatePersonDTO(name, lastname, email, cpf, birthDate, isTeacher, userId);

		Person personToCreate = new Person(data);

    	// Verify User existence
        if(data.userId() != null && data.userId()> 0) {
        	
        	Optional<User> user = userRepos.findById(data.userId());
        	
        	if(user.isEmpty())
        		return new ResponseEntity("Usuário relacionado à Pessoa não encontrado", HttpStatus.NOT_FOUND);	
        	
        }
        
        
        Person savedPerson = personRepos.save(personToCreate);

        Photo photoCreated = photoService.create(photo, savedPerson.getId(), true);
        
        DetailPersonDTO dto = new DetailPersonDTO(savedPerson, photoCreated.getImageData());
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }*/
	
	@PostMapping
	public ResponseEntity<ResponseDTO> create(@RequestBody CreatePersonDTO data) {
		ResponseDTO response = personService.save(data);
        return new ResponseEntity<ResponseDTO>(response, HttpStatusCode.valueOf(response.status()));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> get(@PathVariable Long id) {
    	ResponseDTO response = personService.get(id);
        return new ResponseEntity<ResponseDTO>(response, HttpStatusCode.valueOf(response.status()));  
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDTO> update(@PathVariable Long id, @RequestBody UpdatePersonDTO data) {
    	ResponseDTO response = personService.update(id, data);
        return new ResponseEntity<ResponseDTO>(response, HttpStatusCode.valueOf(response.status()));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable Long id) {
    	ResponseDTO response = personService.delete(id);
        return new ResponseEntity<ResponseDTO>(response, HttpStatusCode.valueOf(response.status()));
        
    }

}

