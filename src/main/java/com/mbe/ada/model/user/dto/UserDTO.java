package com.mbe.ada.model.user.dto;

import java.time.LocalDate;

import com.mbe.ada.model.user.User;

public record UserDTO(Long id, String name, String email, String cpf, LocalDate birthDate) {
	
	public UserDTO(User u) {
		this(u.getId(), u.getName(), u.getEmail(), u.getCpf(), u.getBirthDate());
	}

}