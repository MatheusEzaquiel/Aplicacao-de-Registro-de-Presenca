package com.mbe.ada.controller;

import com.mbe.ada.model.auth.dto.ResponseDTO;
import com.mbe.ada.model.group.Group;
import com.mbe.ada.model.group.dto.CreateGroupDTO;
import com.mbe.ada.model.group.dto.DetailGroupDTO;
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
    public ResponseEntity<ResponseDTO> update(@PathVariable Long id, @RequestBody Group data) {
    	ResponseDTO response = groupService.updateByID(id, data);
    	return new ResponseEntity<ResponseDTO>(response, HttpStatus.valueOf(response.status()));
    }

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseDTO> delete(@PathVariable Long id) {
		ResponseDTO response = groupService.deleteById(id);
		return new ResponseEntity<ResponseDTO>(response, HttpStatus.valueOf(response.status()));
	}

    // Endpoint para adicionar um usuário a um grupo
    @PostMapping("/{groupId}/person/{personId}")
    public ResponseEntity<ResponseDTO> addPerson(@PathVariable Long groupId, @PathVariable Long personId) {
    	ResponseDTO response = groupService.addPersonToGroup(groupId, personId);
        return new ResponseEntity<ResponseDTO>(response, HttpStatus.valueOf(response.status()));
    }
    
    @DeleteMapping("/{groupId}/person/{personId}")
    public ResponseEntity<ResponseDTO> removePerson(@PathVariable Long groupId, @PathVariable Long personId) {
        ResponseDTO response = groupService.removePersonFromGroup(groupId, personId);
        return new ResponseEntity<ResponseDTO>(response, HttpStatus.valueOf(response.status()));
    }

}
