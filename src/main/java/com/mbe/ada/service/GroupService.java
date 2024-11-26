package com.mbe.ada.service;

import com.mbe.ada.model.auth.dto.ResponseDTO;
import com.mbe.ada.model.group.Group;
import com.mbe.ada.model.group.dto.DetailGroupDTO;
import com.mbe.ada.model.person.Person;
import com.mbe.ada.model.person.dto.BasicPersonDTO;
import com.mbe.ada.repository.IGroupRepository;
import com.mbe.ada.repository.IPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    
    public ResponseDTO updateByID(Long id, Group data) {
    	
    	List<BasicPersonDTO> persons = new ArrayList<BasicPersonDTO>();
    	
    	 Optional<Group> groupOpt = groupRepository.findById(id);
         
         if (groupOpt.isEmpty())
        	 return new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Grupo não encontrado", null);
         
         Group groupToUpdate = groupOpt.get();
         groupToUpdate.updateValues(data);
         Group updatedGroup = groupRepository.save(groupToUpdate);
         
         // List Persons
         if(updatedGroup.getPersons().size() > 0) {
        	 
        	 for(Person person : updatedGroup.getPersons())
        		 persons.add(new BasicPersonDTO(person));
        	 
         }
         
         DetailGroupDTO detailGroup = new DetailGroupDTO(updatedGroup, persons);
         return new ResponseDTO(HttpStatus.OK.value(), "Grupo Atualizado", detailGroup);
         
    }

    // Deleta um grupo pelo ID
    public ResponseDTO deleteById(Long id) {
    	
        Optional<Group> groupOpt = groupRepository.findById(id);
        
        if(groupOpt.isEmpty())
        	return new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Grupo não encontrado", null);

        if(!groupOpt.get().isActive())
        	return new ResponseDTO(HttpStatus.FOUND.value(), "O Grupo já está Inativado", null);

        
        groupOpt.get().setActive(false);
		groupRepository.save(groupOpt.get());
		
		return new ResponseDTO(HttpStatus.OK.value(), "Grupo Inativado", null);
    }
    
    public ResponseDTO addPersonToGroup(Long groupId, Long personId) {
    	
    	List<BasicPersonDTO> persons = new ArrayList<BasicPersonDTO>();
    	
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        Optional<Person> personOptional = personRepository.findById(personId);
        
        if(groupOptional.isEmpty())
        	return new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Grupo não encontrado", null);
        
        if(personOptional.isEmpty())
        	return new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Pessoa não encontrada", null);
        

        if (groupOptional.isPresent() && personOptional.isPresent()) {
        	
            Group group = groupOptional.get();
            Person person = personOptional.get();

            // Add user to Group
            group.getPersons().add(person);

            Group groupWithPersons = groupRepository.save(group);
            
            
            // List Persons
            if(groupWithPersons.getPersons().size() > 0) {
           	 
           	 for(Person pers : groupWithPersons.getPersons())
           		 persons.add(new BasicPersonDTO(pers));
           	 
            }
            
            DetailGroupDTO data = new DetailGroupDTO(groupWithPersons, persons);
            
            return new ResponseDTO(HttpStatus.OK.value(), "Parceiro adicionado ao Curso", data);
        }
        

        return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Erro ao Adicionar Pessoa ao Grupo", null);
    }
    
    public ResponseDTO removePersonFromGroup(Long groupId, Long personId) {
    	
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        Optional<Person> personOptional = personRepository.findById(personId);
        
        
        if(groupOptional.isEmpty())
        	return new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Grupo não encontrado", null);
        
        if(personOptional.isEmpty())
        	return new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Pessoa não encontrada", null);
        

        if (groupOptional.isPresent() && personOptional.isPresent()) {
        	
            Group group = groupOptional.get();
            Person person = personOptional.get();
            
            // Remove from Group
            group.getPersons().remove(person);	
            
            int InactivePersonsQty = groupRepository.save(group) != null ? 1 : 0;
            
            return new ResponseDTO(HttpStatus.OK.value(), "Parceiro Removido do Grupo", InactivePersonsQty);

        }
        
        return new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Erro ao Remover Pessoa ao Grupo", null);
    }

} 