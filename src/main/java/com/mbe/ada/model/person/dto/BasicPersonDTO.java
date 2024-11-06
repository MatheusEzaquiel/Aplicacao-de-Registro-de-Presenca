package com.mbe.ada.model.person.dto;

import com.mbe.ada.model.person.Person;

public record BasicPersonDTO(Long id, String name, String lastname, String cpf, Long userId) {
	
	public BasicPersonDTO(Person p) {
		this(p.getId(), p.getName(), p.getLastname(), p.getCpf(), p.getUserId());
	}

}