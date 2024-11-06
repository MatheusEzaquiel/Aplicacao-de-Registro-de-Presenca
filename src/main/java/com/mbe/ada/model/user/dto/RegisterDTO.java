package com.mbe.ada.model.user.dto;

import java.time.LocalDate;

public record RegisterDTO(String name, String lastname, String cpf, String email, String password, LocalDate birthdate) {
	
	
	
}

