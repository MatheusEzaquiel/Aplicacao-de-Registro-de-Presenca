package com.mbe.ada.model.person.dto;

import java.time.LocalDate;

import jakarta.annotation.Nullable;

public record CreatePersonDTO(
		String name,
		String lastname,
		String email,
		String cpf,
		LocalDate birthDate,
		Boolean isTeacher,
		@Nullable Long userId,
		String photoBase64
		) {
	

}