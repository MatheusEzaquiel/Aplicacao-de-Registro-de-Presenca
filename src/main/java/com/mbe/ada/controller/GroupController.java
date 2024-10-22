package com.mbe.ada.controller;

import com.mbe.ada.model.group.Group;
import com.mbe.ada.model.group.dto.CreateGroupDTO;
import com.mbe.ada.model.group.dto.DetailGroupDTO;
import com.mbe.ada.model.person.Person;
import com.mbe.ada.model.person.dto.PersonDTO;
import com.mbe.ada.model.user.User;
import com.mbe.ada.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/groups")
public class GroupController{

    @Autowired
    GroupService groupService;

    @GetMapping
    public ResponseEntity<List<DetailGroupDTO>> index() {
        List<DetailGroupDTO> groups = groupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

	@PostMapping
	public ResponseEntity<Group> create(@RequestBody CreateGroupDTO data) {
		
		Group groupCreated = groupService.saveGroup(new Group(data));  
        return ResponseEntity.ok(groupCreated);
        
	}

    @GetMapping("/{id}")
    public ResponseEntity<Group> get(@PathVariable Long id) {
        Optional<Group> group = groupService.getGroupById(id);
        return group.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<Group> update(@PathVariable Long id, @RequestBody Group data) {
    	Group groupUpdated = groupService.updateGroup(id, data);
        return new ResponseEntity<>(groupUpdated, HttpStatus.OK);
    }

	@DeleteMapping("/{id}")
	public ResponseEntity<Group> delete(@PathVariable Long id) {
		Group group = groupService.deleteById(id);
		
		return ResponseEntity.ok(group);
	}

    // Endpoint para adicionar um usuário a um grupo
    @PostMapping("/{groupId}/person/{personId}")
    public ResponseEntity<String> addUserToGroup(@PathVariable Long groupId, @PathVariable Long personId) {
        Optional<Group> group = groupService.addUserToGroup(groupId, personId);
        if (group.isPresent()) {
            return ResponseEntity.ok("Usuário adicionado ao grupo com sucesso.");
        } else {
            return ResponseEntity.badRequest().body("Grupo ou usuário não encontrado.");
        }
    }
    
    @DeleteMapping("/{groupId}/person/{personId}")
    public ResponseEntity<String> removeUserToGroup(@PathVariable Long groupId, @PathVariable Long personId) {
    	
        Integer deletedRows = groupService.removeUserFromGroup(groupId, personId);
        
        if (deletedRows > 0) {
            return ResponseEntity.ok("Usuário removido do grupo com sucesso.");
        } else {
            return ResponseEntity.badRequest().body("Grupo ou usuário não encontrado.");
        }
        
    }

}
