package com.mbe.ada.model.person.dto;

import java.util.List;

import com.mbe.ada.model.group.dto.BasicGroupDTO;
import com.mbe.ada.model.person.Person;

public record DetailPersonDTO(
		Long id,
		String name,
		String lastname,
		String email,
		String cpf,
		String birthDate,
		Boolean isTeacher,
		Long userId,
		String photo,
		List<BasicGroupDTO> groups
		) {
	
	public DetailPersonDTO(Person p) {
		this(p.getId(), p.getName(), p.getLastname(), p.getEmail(), p.getCpf(), p.getBirthDate().toString(), p.getIsTeacher(), p.getUserId(), null, null);
	}
	
	public DetailPersonDTO(Person p, String photo) {
		this(p.getId(), p.getName(), p.getLastname(), p.getEmail(), p.getCpf(), p.getBirthDate().toString(), p.getIsTeacher(), p.getUserId(), photo, null);
	}
	
	public DetailPersonDTO(Person p, String photo, List<BasicGroupDTO> groups) {
		this(p.getId(), p.getName(), p.getLastname(), p.getEmail(), p.getCpf(), p.getBirthDate().toString(), p.getIsTeacher(), p.getUserId(), photo, groups);
	}


}