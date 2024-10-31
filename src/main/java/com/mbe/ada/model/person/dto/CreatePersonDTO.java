package com.mbe.ada.model.person.dto;

import java.time.LocalDate;

import com.mbe.ada.model.person.Person;
import com.mbe.ada.model.photo.Photo;

import jakarta.annotation.Nullable;

public record CreatePersonDTO(
		String name,
		String lastname,
		String email,
		String cpf,
		LocalDate birthDate,
		Boolean isTeacher,
		@Nullable Long userId,
		String photoName,
		String photo
		) {
	

}