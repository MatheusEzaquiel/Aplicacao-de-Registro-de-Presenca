package com.mbe.ada.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.mbe.ada.model.user.User;
import com.mbe.ada.repository.IGroupRepository;
import com.mbe.ada.repository.IPersonRepository;
import com.mbe.ada.repository.IUserRepository;
import com.mbe.ada.service.ImageUtils;
import com.mbe.ada.service.PersonService;
import com.mbe.ada.service.PhotoService;

@RestController
@RequestMapping(value = "/persons")
public class PersonController {
	
	@Autowired
	IPersonRepository personRepos;

	@Autowired
	IUserRepository userRepos;
	
	@Autowired
	PhotoService photoService;
	
	@Autowired 
	IGroupRepository groupRepos;
	
	@Autowired
	PersonService personService;
	

	@Autowired
	ImageUtils imageUtils;
	
	@GetMapping
	public ResponseEntity<List<DetailPersonDTO>>  index() {		
		
		List<Person> data = personRepos.findByIsActiveTrue();
		
		if(data.size() == 0)
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
		
		List<DetailPersonDTO> dataDTO = data.stream()
		.map(person -> {
			
			String imageData = imageUtils.getImageBase64(person.getCpf(), Person.class.toString());
			
			if(person.getGroups() != null) {
				List<BasicGroupDTO> groupsDTO = person.getGroups().stream()
				.map(group -> new BasicGroupDTO(group))
				.collect(Collectors.toList());
				
				return new DetailPersonDTO(person, imageData, groupsDTO);
			}
			
			return new DetailPersonDTO(person, imageData, null);
		
			
		})
		.toList();
		
		return new ResponseEntity<List<DetailPersonDTO>>(dataDTO, HttpStatus.OK);
	}
	
	@GetMapping("/students")
	public ResponseEntity<List<DetailPersonDTO>>  listStudents() {		
		
		List<Person> data = personRepos.findByIsTeacherFalse();
		
		if (data.size() == 0)
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

		List<DetailPersonDTO> dataDTO = data.stream().map(person -> {

			String imageData = photoService.getImageDataByPersonId(person.getId());

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
			String imageData = photoService.getImageDataByPersonId(person.getId());
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

			String imageData = photoService.getImageDataByPersonId(person.getId());

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
        return new ResponseEntity<ResponseDTO>(response, HttpStatus.CREATED);
        
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DetailPersonDTO> get(@PathVariable Long id) {
        
    	Optional<Person> personOpt = personRepos.findById(id);
        
        if (personOpt.isEmpty()) 
        	return new ResponseEntity("Pessoa não encontrada", HttpStatus.NOT_FOUND);
        
        
        String imageData = photoService.getImageDataByPersonId(id);
        
        
        List<BasicGroupDTO> groupsDTO = personOpt.get().getGroups()
        		.stream()
        		.map(group -> new BasicGroupDTO(group))
				.collect(Collectors.toList());
	
		DetailPersonDTO dto = new DetailPersonDTO(personOpt.get(), imageData, groupsDTO);
		
        return new ResponseEntity<DetailPersonDTO>(dto, HttpStatus.OK);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<PersonDTO> update(@PathVariable Long id, @RequestBody PersonDTO data) {
    	
        Optional<Person> personToUpdt = personRepos.findById(id);
        
        if (personToUpdt.isEmpty())
        	return new ResponseEntity("Pessoa não encontrada", HttpStatus.NOT_FOUND);
        
        Person personToUpdate = personToUpdt.get();
        

    	// Verify User existence
        if(data.userId() != null && data.userId()> 0) {
        	
        	Optional<User> user = userRepos.findById(data.userId());
        	
        	if(user.isEmpty())
        		return new ResponseEntity("Usuário relacionado à Pessoa não encontrado", HttpStatus.NOT_FOUND);	
        	
        }
        
        personToUpdate.updateValues(data);
        Person updatedPerson = personRepos.save(personToUpdate);
        PersonDTO updatedDTO = new PersonDTO(updatedPerson);
        return new ResponseEntity<>(updatedDTO, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
    	
        Optional<Person> personOpt = personRepos.findById(id);
        
        if (personOpt.isEmpty())
        	return new ResponseEntity("Pessoa não encontrada", HttpStatus.NOT_FOUND);

        Person personToDelete = personOpt.get();
        personToDelete.setIsActive(false);
        personRepos.save(personToDelete);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

