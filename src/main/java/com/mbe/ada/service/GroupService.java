package com.mbe.ada.service;

import com.mbe.ada.model.group.Group;
import com.mbe.ada.model.group.dto.BasicGroupDTO;
import com.mbe.ada.model.group.dto.DetailGroupDTO;
import com.mbe.ada.model.person.Person;
import com.mbe.ada.model.person.dto.BasicPersonDTO;
import com.mbe.ada.model.person.dto.DetailPersonDTO;
import com.mbe.ada.model.person.dto.PersonDTO;
import com.mbe.ada.repository.IGroupRepository;
import com.mbe.ada.repository.IPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupService {

    @Autowired
    private IGroupRepository groupRepository;

    @Autowired
    private IPersonRepository personRepository;

    // Salva ou atualiza um grupo
    public Group saveGroup(Group group) {
        return groupRepository.save(group);
    }

    // Busca todos os grupos
    public List<DetailGroupDTO> getAllGroups() {
        
    	//return groupRepository.findAll();
        
    	List<DetailGroupDTO> dataDTO = groupRepository.findAll().stream()
    			.map(group -> {
    				
    			List<BasicPersonDTO> personsDTO = group.getPersons()
    				.stream()
    				.map(person -> new BasicPersonDTO(person))
    				.toList();
    					
    				
    				return new DetailGroupDTO(group, personsDTO);
    	})
    	.toList();
    	
		return dataDTO;
        
    }

    // Busca um grupo pelo ID
    public Optional<Group> getGroupById(Long id) {
        return groupRepository.findById(id);
    }
    
    public Group updateGroup(Long id, Group data) {
    	
    	 Optional<Group> groupOpt = groupRepository.findById(id);
         
         if (groupOpt.isEmpty())
         	throw new RuntimeException("Grupo Não enontrado");
         
         Group groupToUpdate = groupOpt.get();
         
         groupToUpdate.updateValues(data);
         return groupRepository.save(groupToUpdate);
         
    }

    // Deleta um grupo pelo ID
    public Group deleteById(Long id) {
        Optional<Group> groupOpt = groupRepository.findById(id);
        
        Group groupToDelete = groupOpt.get();
        groupToDelete.setActive(false);
        
		return groupRepository.save(groupToDelete);

    }

    // Adiciona um usuário a um grupo
    public Optional<Group> addUserToGroup(Long groupId, Long personId) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        Optional<Person> personOptional = personRepository.findById(personId);

        if (groupOptional.isPresent() && personOptional.isPresent()) {
            Group group = groupOptional.get();
            Person person = personOptional.get();

            // Adiciona o usuário ao grupo
            group.getPersons().add(person);

            // Salva a atualização no "banco de dados"
            groupRepository.save(group);

            return Optional.of(group);
        }
        return Optional.empty();
    }
    
    public Integer removeUserFromGroup(Long groupId, Long personId) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        Optional<Person> personOptional = personRepository.findById(personId);

        if (groupOptional.isPresent() && personOptional.isPresent()) {
            Group group = groupOptional.get();
            Person person = personOptional.get();
            
            // Remove diretamente delete from
            group.getPersons().remove(person);					 
            return groupRepository.save(group) != null ? 1 : 0;
            
            //return groupRepository.deactivateUserInGroup(groupId, personId);	// Remove logicamente isactive = false

        }
        
        return 0;
    }

} 