package com.mbe.ada.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbe.ada.model.auth.dto.ResponseDTO;
import com.mbe.ada.model.user.dto.CreateUserDTO;
import com.mbe.ada.service.UserService;

import jakarta.validation.Valid;


@RestController
@RequestMapping(value="/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	
	@GetMapping
	public ResponseEntity<ResponseDTO> index() {		
		
		ResponseDTO responseDTO = userService.list();
		return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.valueOf(responseDTO.status()));
		
	}
	
	@PostMapping
	public ResponseEntity<ResponseDTO> create(@Valid @RequestBody CreateUserDTO data) {
		
		ResponseDTO responseDTO = userService.save(data);
		return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.valueOf(responseDTO.status()));

	}

}
