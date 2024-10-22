package com.mbe.ada.model.group.dto;

import com.mbe.ada.model.group.Group;

public record BasicGroupDTO(Long id, String name) {
	
	public BasicGroupDTO(Group group) {
		this(group.getId(), group.getName());
	}
	
}
